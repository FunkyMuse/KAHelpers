package dev.funkymuse.setofusefulkotlinextensions


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dev.funkymuse.animations.transition.utils.fadeRecyclerTransition
import dev.funkymuse.context.getColorCompat
import dev.funkymuse.context.isGestureNavigationEnabled
import dev.funkymuse.coroutines.textChanges
import dev.funkymuse.customviews.AppRater
import dev.funkymuse.customviews.autoStart.AutoStartHelper
import dev.funkymuse.customviews.autoStart.ConfirmationDialogAutoStart
import dev.funkymuse.internetdetector.InternetDetector
import dev.funkymuse.kotlinextensions.log.debug
import dev.funkymuse.kotlinextensions.misc.RunCodeEveryXLaunch
import dev.funkymuse.kotlinextensions.views.asSearchView
import dev.funkymuse.kotlinextensions.views.setQueryAndExpand
import dev.funkymuse.lifecycle.repeatingJobOnStarted
import dev.funkymuse.recyclerview.RecyclerSwipeItemHandler
import dev.funkymuse.recyclerview.addSwipe
import dev.funkymuse.recyclerview.generateRecycler
import dev.funkymuse.recyclerview.hideOnScroll
import dev.funkymuse.retrofit.apiresult.ApiResult
import dev.funkymuse.retrofit.throwables.NoConnectionException
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestModel
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestPlaceHolderAdapter
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestViewHolderShimmer
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding
import dev.funkymuse.setofusefulkotlinextensions.databinding.ActivityMainBinding
import dev.funkymuse.view.getEditTextSearchView
import dev.funkymuse.view.setOnClickListenerCooldown
import dev.funkymuse.viewbinding.viewBinding
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
            testAVM.sendEvent(TestAVM.TestAVMIntent.GetPosts)
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

        exampleGeneratedAdapter.forItemClickListener = { _, _, _ ->

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
            enableEdgeToEdge()
        }
    }

    private fun updateUI(apiResult: ApiResult<List<TestModel>>) {
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
                        testAVM.sendEvent(TestAVM.TestAVMIntent.GetPosts)
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








