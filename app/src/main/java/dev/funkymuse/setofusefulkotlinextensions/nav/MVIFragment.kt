package dev.funkymuse.setofusefulkotlinextensions.nav

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.funkymuse.common.ifTrue
import dev.funkymuse.internetdetector.InternetDetector
import dev.funkymuse.lifecycle.repeatingJobOnStarted
import dev.funkymuse.lifecycle.viewCoroutineScope
import dev.funkymuse.retrofit.throwables.isNoConnectionException
import dev.funkymuse.retrofit.viewstate.event.ViewStatefulEvent
import dev.funkymuse.retrofit.viewstate.event.asApiErrorBody
import dev.funkymuse.retrofit.viewstate.event.getAsThrowable
import dev.funkymuse.retrofit.viewstate.event.isApiError
import dev.funkymuse.retrofit.viewstate.event.isError
import dev.funkymuse.retrofit.viewstate.state.handleApiError
import dev.funkymuse.retrofit.viewstate.state.showEmptyDataOnErrors
import dev.funkymuse.retrofit.viewstate.state.showLoadingWhenDataIsLoaded
import dev.funkymuse.retrofit.viewstate.state.showLoadingWhenDataNotLoaded
import dev.funkymuse.setofusefulkotlinextensions.R
import dev.funkymuse.setofusefulkotlinextensions.TestAVM
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestViewBindingAdapter
import dev.funkymuse.setofusefulkotlinextensions.databinding.FragmentTestBinding
import dev.funkymuse.view.setIsNotRefreshing
import dev.funkymuse.view.setIsRefreshing
import dev.funkymuse.view.setOnClickListenerCooldown
import dev.funkymuse.viewbinding.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MVIFragment : Fragment(R.layout.fragment_test) {

    private val binding by viewBinding(FragmentTestBinding::bind)

    private val adapter by lazy { TestViewBindingAdapter() }
    private val internetDetector by lazy { InternetDetector(requireContext()) }
    private val testAVM by viewModels<TestAVM>()

    private var snackBar: Snackbar? = null
    private var toast: Toast? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        repeatingJobOnStarted { testAVM.posts.collect { retrofitResult -> updateUIState(retrofitResult) } }
        repeatingJobOnStarted { testAVM.viewEvent.collect { viewEvent -> handleViewEvents(viewEvent) } }

        binding.swipeToRefresh.setOnRefreshListener {
            binding.swipeToRefresh.setIsRefreshing()
            getApiPosts()
            binding.swipeToRefresh.setIsNotRefreshing()
        }
        binding.staticPosts.setOnClickListenerCooldown {
            testAVM.sendEvent(TestAVM.TestAVMIntent.GetRandomPosts)
        }
    }

    private fun handleViewEvents(viewStatefulEvent: ViewStatefulEvent) {
        viewStatefulEvent.isError.and(testAVM.viewState.isDataLoaded).ifTrue(::showErrorSnack)
        viewStatefulEvent.isApiError.ifTrue {
            toast?.cancel()
            toast = Toast.makeText(requireContext(), handleApiError(testAVM.savedStateHandle, viewStatefulEvent.asApiErrorBody), LENGTH_LONG)
            toast?.show()
        }
    }

    private fun getApiPosts(){
        testAVM.sendEvent(TestAVM.TestAVMIntent.GetPosts)
    }

    private fun updateUIState(apiResult: ViewStatefulEvent) {
        apiResult.getAsThrowable?.isNoConnectionException?.ifTrue(::retryOnInternetAvailable)
        binding.error.isVisible = testAVM.viewState.showEmptyDataOnErrors
        binding.centerBigLoading.isVisible = testAVM.viewState.showLoadingWhenDataNotLoaded
        binding.progress.isVisible = testAVM.viewState.showLoadingWhenDataIsLoaded
        adapter.submitList(testAVM.viewState.payload)
    }

    private fun showErrorSnack() {
        snackBar?.dismiss()
        snackBar = Snackbar.make(requireView(), "Error has occurred", Snackbar.LENGTH_LONG)
        snackBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        toast?.cancel()
        snackBar = null
        toast = null
    }

    private fun retryOnInternetAvailable() {
        viewCoroutineScope.launch { observeInternetConnectivity() }
    }

    private suspend fun observeInternetConnectivity() {
        internetDetector.state.collect { hasConnection -> hasConnection.ifTrue { getApiPosts() } }
    }

}