package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.delegates.activityAVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.recyclerview.*
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.AppRater
import com.crazylegend.kotlinextensions.views.toggleVisibility
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import com.crazylegend.setofusefulkotlinextensions.adapter.TestViewHolder
import com.crazylegend.setofusefulkotlinextensions.databinding.ActivityMainBinding

class MainAbstractActivity : AppCompatActivity() {

    private val testAVM by activityAVM<TestAVM>(arrayOf(TestModel("", 1, "", 2), 1, ""))


    private val generatedAdapter by lazy {
        activityMainBinding.recycler.generateVerticalAdapter<TestModel, TestViewHolder>(
                layout = R.layout.recycler_view_item,
                viewHolder = TestViewHolder::class.java) { item, holder, _ ->
            holder.bind(item)
        }
    }

    private val adapter by recyclerAdapter<TestModel, TestViewHolder>(
            layout = R.layout.recycler_view_item,
            viewHolder = TestViewHolder::class.java) { item, holder, _ ->
        holder.bind(item)
    }

    private val activityMainBinding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.test.setOnClickListener {
            activityMainBinding.recycler.toggleVisibility()
        }

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


        testAVM.posts.observe(this, Observer {
            it?.apply {
                when (it) {
                    is RetrofitResult.Success -> {
                        generatedAdapter.submitList(it.value)

                        val wrappedList = it.value.toMutableList()
                        activityMainBinding.recycler.addDrag(generatedAdapter, wrappedList)

                    }
                    RetrofitResult.Loading -> {
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
    }


}


