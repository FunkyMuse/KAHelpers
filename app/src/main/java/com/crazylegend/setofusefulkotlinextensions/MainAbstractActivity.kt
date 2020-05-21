package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.delegates.activityAVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.recyclerview.HideOnScrollListener
import com.crazylegend.kotlinextensions.recyclerview.RecyclerSwipeItemHandler
import com.crazylegend.kotlinextensions.recyclerview.addDrag
import com.crazylegend.kotlinextensions.recyclerview.addSwipe
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.rx.bindings.textChanges
import com.crazylegend.kotlinextensions.rx.clearAndDispose
import com.crazylegend.kotlinextensions.transition.StaggerTransition
import com.crazylegend.kotlinextensions.transition.interpolators.FAST_OUT_SLOW_IN
import com.crazylegend.kotlinextensions.transition.utils.LARGE_EXPAND_DURATION
import com.crazylegend.kotlinextensions.transition.utils.plusAssign
import com.crazylegend.kotlinextensions.transition.utils.transitionSequential
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.AppRater
import com.crazylegend.kotlinextensions.views.toggleVisibility
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestPlaceHolderAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityMainBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class MainAbstractActivity : AppCompatActivity() {

    private val testAVM by activityAVM<TestAVM>(arrayOf(TestModel("", 1, "", 2), 1, ""))

    private val testPlaceHolderAdapter by lazy {
        TestPlaceHolderAdapter()
    }
    private val generatedAdapter by lazy {
        TestViewBindingAdapter()
    }


    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    private val activityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    private val fade = transitionSequential {
        duration = LARGE_EXPAND_DURATION
        interpolator = FAST_OUT_SLOW_IN
        this += Fade(Fade.OUT)
        this += Fade(Fade.IN)
        addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                if (savedItemAnimator != null) {
                    activityMainBinding.recycler.itemAnimator = savedItemAnimator
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.test.setOnClickListener {
            activityMainBinding.recycler.toggleVisibility()
        }

        activityMainBinding.recycler.addOnScrollListener(object : HideOnScrollListener(5){
            override fun onHide() {
                activityMainBinding.test.hide()
            }

            override fun onShow() {
                activityMainBinding.test.show()
            }
        })
        AppRater.appLaunched(this, supportFragmentManager, 0, 0) {
            appTitle = getString(R.string.app_name)
            buttonsBGColor = getCompatColor(R.color.colorAccent)
        }

        generatedAdapter.forItemClickListener = forItemClickListenerDSL { position, item, view ->
            debug("SADLY CLICKED HERE $item")
        }
        activityMainBinding.recycler.addSwipe(this) {
            swipeDirection = RecyclerSwipeItemHandler.SwipeDirs.BOTH
            drawableLeft = android.R.drawable.ic_delete
            drawLeftBackground = true
            leftBackgroundColor = R.color.colorPrimary
            drawableRight = android.R.drawable.ic_input_get
        }


        val stagger = StaggerTransition()

        testAVM.posts.observe(this, Observer {
            it?.apply {
                when (it) {
                    is RetrofitResult.Success -> {
                            TransitionManager.beginDelayedTransition(activityMainBinding.recycler, stagger)
                            if (activityMainBinding.recycler.adapter != generatedAdapter) {
                                activityMainBinding.recycler.adapter = generatedAdapter
                                savedItemAnimator = activityMainBinding.recycler.itemAnimator
                                activityMainBinding.recycler.itemAnimator = null
                                TransitionManager.beginDelayedTransition(activityMainBinding.recycler, fade)
                            }
                            generatedAdapter.submitList(it.value)
                            val wrappedList = it.value.toMutableList()
                            activityMainBinding.recycler.addDrag(generatedAdapter, wrappedList)
                    }
                    RetrofitResult.Loading -> {
                        activityMainBinding.recycler.adapter = testPlaceHolderAdapter
                        debug(it.toString())
                    }
                    RetrofitResult.EmptyData -> {
                        debug(it.toString())
                    }
                    is RetrofitResult.Error -> {
                        debug(it.toString())
                    }
                    is RetrofitResult.ApiError -> {
                        debug(it.toString())
                    }
                }.exhaustive
            }
        })

        testAVM.filteredPosts.observe(this, Observer {
            generatedAdapter.submitList(it)
        })
    }

    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)

        searchItem?.apply {
            searchView = this.actionView as SearchView?
        }

        searchView?.queryHint = "Search by title"

        searchView?.textChanges(debounce = 1000L, compositeDisposable = compositeDisposable){
            testAVM.filterBy(it)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

}


