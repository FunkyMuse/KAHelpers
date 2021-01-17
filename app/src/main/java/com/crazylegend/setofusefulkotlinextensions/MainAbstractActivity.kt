package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.crazylegend.coroutines.textChanges
import com.crazylegend.customviews.AppRater
import com.crazylegend.customviews.autoStart.AutoStartHelper
import com.crazylegend.customviews.autoStart.ConfirmationDialogAutoStart
import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.context.isGestureNavigationEnabled
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.gestureNavigation.EdgeToEdge
import com.crazylegend.kotlinextensions.internetdetector.InternetDetector
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.misc.RunCodeEveryXLaunchOnAppOpened
import com.crazylegend.kotlinextensions.transition.StaggerTransition
import com.crazylegend.kotlinextensions.transition.utils.fadeRecyclerTransition
import com.crazylegend.kotlinextensions.views.asSearchView
import com.crazylegend.kotlinextensions.views.getEditTextSearchView
import com.crazylegend.kotlinextensions.views.setOnClickListenerCooldown
import com.crazylegend.recyclerview.*
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.retrofitResult.retryWhenInternetIsAvailable
import com.crazylegend.rx.clearAndDispose
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestPlaceHolderAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewHolderShimmer
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityMainBinding
import com.crazylegend.viewbinding.viewBinder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class MainAbstractActivity : AppCompatActivity() {

    private val testAVM by viewModels<TestAVM>()

    private val testPlaceHolderAdapter by lazy {
        TestPlaceHolderAdapter()
    }
    private val generatedAdapter by lazy {
        TestViewBindingAdapter()
    }

    private val exampleGeneratedAdapter by lazy {
        generateRecycler<TestModel, TestViewHolderShimmer, CustomizableCardViewBinding>(
                ::TestViewHolderShimmer,
                CustomizableCardViewBinding::inflate) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

    private val compositeDisposable by lazy {
        CompositeDisposable()
    }

    private val internetDetector by lazy {
        InternetDetector(this)
    }

    private val activityMainBinding by viewBinder(ActivityMainBinding::inflate)
    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    private val fade = fadeRecyclerTransition(activityMainBinding.recycler, savedItemAnimator)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setSupportActionBar(activityMainBinding.toolbar)

        activityMainBinding.test.setOnClickListenerCooldown {
            testAVM.getposts()
        }
        lifecycleScope.launchWhenResumed {
            testAVM.posts.collect {
                updateUI(it)
            }
        }

        RunCodeEveryXLaunchOnAppOpened.runCode(this, 2) {
            debug("TEST RUN AT 2 LAUNCHES")
        }

        exampleGeneratedAdapter.forItemClickListener = forItemClickListener { _, _, _ ->

        }

        activityMainBinding.recycler.hideOnScroll(5, hide = {
            activityMainBinding.test.hide()
        }, show = {
            activityMainBinding.test.show()
        })

        AppRater.appLaunched(this, supportFragmentManager, 0, 0) {
            appTitle = getString(R.string.app_name)
            buttonsBGColor = getCompatColor(R.color.colorAccent)
        }

        activityMainBinding.recycler.addSwipe(this) {
            swipeDirection = RecyclerSwipeItemHandler.SwipeDirs.BOTH
            drawableLeft = android.R.drawable.ic_delete
            drawLeftBackground = true
            leftBackgroundColor = R.color.colorPrimary
            drawableRight = android.R.drawable.ic_input_get
        }

        AutoStartHelper.checkAutoStart(this, dialogBundle = bundleOf(
                Pair(ConfirmationDialogAutoStart.CANCEL_TEXT, "Dismiss"),
                Pair(ConfirmationDialogAutoStart.CONFIRM_TEXT, "Allow"),
                Pair(ConfirmationDialogAutoStart.DO_NOT_SHOW_AGAIN_VISIBILITY, true)
        ))

        if (isGestureNavigationEnabled()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                EdgeToEdge.setUpRoot(this, activityMainBinding.root, android.R.color.transparent)
            } else {
                EdgeToEdge.setUpRoot(activityMainBinding.root)
            }
            EdgeToEdge.setUpAppBar(activityMainBinding.appBar, activityMainBinding.toolbar)
            EdgeToEdge.setUpScrollingContent(activityMainBinding.recycler)
        }

    }

    private fun updateUI(retrofitResult: RetrofitResult<List<TestModel>>) {
        retrofitResult.retryWhenInternetIsAvailable(internetDetector, this, retry = {
            testAVM.getposts()
        })
        when (retrofitResult) {
            is RetrofitResult.Success -> {
                val stagger = StaggerTransition()
                TransitionManager.beginDelayedTransition(activityMainBinding.recycler, stagger)
                if (activityMainBinding.recycler.adapter != generatedAdapter) {
                    activityMainBinding.recycler.adapter = generatedAdapter
                    savedItemAnimator = activityMainBinding.recycler.itemAnimator
                    activityMainBinding.recycler.itemAnimator = null
                    TransitionManager.beginDelayedTransition(activityMainBinding.recycler, fade)
                }
                generatedAdapter.submitList(retrofitResult.value)
                val wrappedList = retrofitResult.value.toMutableList()
                activityMainBinding.recycler.addDrag(generatedAdapter, wrappedList)
            }
            RetrofitResult.Loading -> {
                activityMainBinding.recycler.adapter = testPlaceHolderAdapter
            }
            RetrofitResult.EmptyData -> {
                activityMainBinding.recycler.adapter = generatedAdapter
                generatedAdapter.submitList(emptyList())
            }
            is RetrofitResult.Error -> {
                activityMainBinding.recycler.adapter = generatedAdapter
                generatedAdapter.submitList(emptyList())
            }
            is RetrofitResult.ApiError -> {
                activityMainBinding.recycler.adapter = generatedAdapter
                generatedAdapter.submitList(emptyList())
            }
        }.exhaustive
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)

        searchItem.asSearchView()?.apply {
            queryHint = "Search by title"

            getEditTextSearchView?.textChanges(true, 200L)
                    ?.map { it?.toString() }
                    ?.onEach {
                        debug("TEXT $it")
                    }?.launchIn(lifecycleScope)

        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

}





