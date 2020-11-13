package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.crazylegend.customviews.AppRater
import com.crazylegend.customviews.autoStart.AutoStartHelper
import com.crazylegend.customviews.autoStart.ConfirmationDialogAutoStart
import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.context.isGestureNavigationEnabled
import com.crazylegend.kotlinextensions.delegates.activityAVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.gestureNavigation.EdgeToEdge
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.misc.RunCodeEveryXLaunchOnAppOpened
import com.crazylegend.kotlinextensions.transition.StaggerTransition
import com.crazylegend.kotlinextensions.transition.interpolators.FAST_OUT_SLOW_IN
import com.crazylegend.kotlinextensions.transition.utils.LARGE_EXPAND_DURATION
import com.crazylegend.kotlinextensions.transition.utils.plusAssign
import com.crazylegend.kotlinextensions.transition.utils.transitionSequential
import com.crazylegend.recyclerview.*
import com.crazylegend.recyclerview.clickListeners.forItemClickListener
import com.crazylegend.retrofit.retrofitResult.RetrofitResult
import com.crazylegend.rx.clearAndDispose
import com.crazylegend.rxbindings.textChanges
import com.crazylegend.security.*
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestPlaceHolderAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewHolderShimmer
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityMainBinding
import com.crazylegend.viewbinding.viewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class MainAbstractActivity : AppCompatActivity() {

    private val testAVM by activityAVM<TestAVM>(arrayOf(TestModel("", 1, "", 2), 1, ""))

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

    private val activityMainBinding by viewBinding(ActivityMainBinding::inflate) {

    }
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
        setSupportActionBar(activityMainBinding.toolbar)

        debug("SECURITY isTestKeyBuild ${isTestKeyBuild()}")
        debug("SECURITY detectDebugger ${detectDebugger()}")
        debug("SECURITY isDebuggable ${isDebuggable()}")
        debug("SECURITY isDebuggerAttached ${isDebuggerAttached()}")
        debug("SECURITY zygoteCallCount ${zygoteCallCount()}")
        debug("SECURITY getSingInfoMD5 ${getSingInfoMD5(BuildConfig.APPLICATION_ID)}")
        debug("SECURITY isEmulator ${isEmulator()}")
        debug("SECURITY getShaSignature ${getShaSignature(BuildConfig.APPLICATION_ID)}")


        /*canAuthenticate(hardwareUnavailable = {
            //some message about hardware missing
        }, noFingerprintsEnrolled = {
            //make user action to enroll fingerprints
        }) {
            biometricAuth(promptInfoAction = {
                setTitle("Verification required")
                setSubtitle("Action paused")
                setDescription("Please verify your identity to proceed with the action")
                setDeviceCredentialAllowed(true)
                this
            }, onAuthFailed = {
                //auth failed action
            }, onAuthError = { errorCode, errorMessage ->
                //handle auth error message and codes

            }) {
                //handle successful authentication
            }
        }*/

        activityMainBinding.test.setOnClickListener {
            lifecycleScope.launch {
                testAVM.getposts().collect {
                    updateUI(it)
                }
            }
        }

        RunCodeEveryXLaunchOnAppOpened.runCode(this, 2) {
            debug("TEST RUN AT 2 LAUNCHES")
        }

        exampleGeneratedAdapter.forItemClickListener = forItemClickListener { _, _, _ ->

        }

        activityMainBinding.recycler.addOnScrollListener(object : HideOnScrollListener(5) {
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
                EdgeToEdge.setUpRoot(this, activityMainBinding.root)
            } else {
                EdgeToEdge.setUpRoot(activityMainBinding.root)
            }
            EdgeToEdge.setUpAppBar(activityMainBinding.appBar, activityMainBinding.toolbar)
            EdgeToEdge.setUpScrollingContent(activityMainBinding.recycler)
        }

        lifecycleScope.launchWhenResumed {
            testAVM.apiTestState.collect {
                updateUI(it)
            }
        }


        val testFile = File(filesDir, "testfile.txt")
        encryptFileSafely(testFile, fileContent = "JETPACK SECURITY".toByteArray())
        val file = getEncryptedFile(testFile)
        debug("TEXT DECRYPTED ${file.readText()}")
        debug("TEXT ENCRYPTED ${testFile.readText()}")
    }

    private fun updateUI(retrofitResult: RetrofitResult<List<TestModel>>) {
        val stagger = StaggerTransition()

        when (retrofitResult) {
            is RetrofitResult.Success -> {
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
            }
            is RetrofitResult.Error -> {
            }
            is RetrofitResult.ApiError -> {
            }
        }.exhaustive
    }

    private var searchView: SearchView? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)

        searchItem?.apply {
            searchView = this.actionView as SearchView?
        }

        searchView?.queryHint = "Search by title"

        searchView?.textChanges(debounce = 1000L, compositeDisposable = compositeDisposable) {
            testAVM.filterBy(it)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clearAndDispose()
    }

}


