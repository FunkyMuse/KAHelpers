package com.crazylegend.setofusefulkotlinextensions


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.animations.transition.utils.fadeRecyclerTransition
import com.crazylegend.context.getColorCompat
import com.crazylegend.context.isGestureNavigationEnabled
import com.crazylegend.coroutines.textChanges
import com.crazylegend.customviews.AppRater
import com.crazylegend.customviews.autoStart.AutoStartHelper
import com.crazylegend.customviews.autoStart.ConfirmationDialogAutoStart
import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.internetdetector.InternetDetector
import com.crazylegend.kotlinextensions.gestureNavigation.EdgeToEdge
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.misc.RunCodeEveryXLaunch
import com.crazylegend.kotlinextensions.views.asSearchView
import com.crazylegend.kotlinextensions.views.setQueryAndExpand
import com.crazylegend.lifecycle.repeatingJobOnStarted
import com.crazylegend.recyclerview.RecyclerSwipeItemHandler
import com.crazylegend.recyclerview.addSwipe
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.recyclerview.generateRecycler
import com.crazylegend.recyclerview.hideOnScroll
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.retrofit.throwables.NoConnectionException
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestPlaceHolderAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewHolderShimmer
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityMainBinding
import com.crazylegend.view.getEditTextSearchView
import com.crazylegend.view.setOnClickListenerCooldown
import com.crazylegend.viewbinding.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
                CustomizableCardViewBinding::inflate
        ) { item, holder, _, _ ->
            holder.bind(item)
        }
    }

    private val internetDetector by lazy {
        InternetDetector(this)
    }



    private val activityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    private val fade get() = fadeRecyclerTransition(activityMainBinding.recycler, savedItemAnimator)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        setSupportActionBar(activityMainBinding.toolbar)

        savedInstanceState?.getString("query", null)?.let { savedQuery = it }

        activityMainBinding.test.setOnClickListenerCooldown {
            testAVM.getPosts()
        }
        activityMainBinding.recycler.adapter = generatedAdapter

        repeatingJobOnStarted {
           /* testAVM.posts.collect {
                updateUI(it)
            }*/
        }

        RunCodeEveryXLaunch.runCode(this, 2) {
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
            buttonsBGColor = getColorCompat(R.color.colorAccent)
        }

        activityMainBinding.recycler.addSwipe(this) {
            swipeDirection = RecyclerSwipeItemHandler.SwipeDirs.BOTH
            drawableLeft = android.R.drawable.ic_delete
            drawLeftBackground = true
            leftBackgroundColor = R.color.colorPrimary
            drawableRight = android.R.drawable.ic_input_get
        }

        AutoStartHelper.checkAutoStart(
                this, dialogBundle = bundleOf(
                Pair(ConfirmationDialogAutoStart.CANCEL_TEXT, "Dismiss"),
                Pair(ConfirmationDialogAutoStart.CONFIRM_TEXT, "Allow"),
                Pair(ConfirmationDialogAutoStart.DO_NOT_SHOW_AGAIN_VISIBILITY, true)
        )
        )

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
       /* retrofitResult.asMVIResult(testAVM.resultMVI) { getAsSuccess.isNotNullOrEmpty }
        testAVM.resultMVI
                .result
                .onError { retryOnInternetAvailable(it) }
                .onSuccess {
                    activityMainBinding.progressBar.gone()
                    generatedAdapter.submitList(it)
                }*/


    }

    private fun retryOnInternetAvailable(throwable: Throwable) {
        if (throwable is NoConnectionException) {
            lifecycleScope.launch {
                internetDetector.state.collect { hasConnection ->
                    if (hasConnection) {
                        testAVM.getPosts()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedQuery?.let { outState.putString("query", it) }
    }

    private var savedQuery: String? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)

        searchItem.setQueryAndExpand(savedQuery)
        searchItem.asSearchView()?.apply {
            queryHint = "Search by title"
            getEditTextSearchView?.textChanges(skipInitialValue = true, debounce = 350L)
                    ?.map { it?.toString() }
                    ?.onEach {
                        debug("TEXT $it")
                        savedQuery = it
                    }?.launchIn(lifecycleScope)
        }

        return super.onCreateOptionsMenu(menu)
    }

}








