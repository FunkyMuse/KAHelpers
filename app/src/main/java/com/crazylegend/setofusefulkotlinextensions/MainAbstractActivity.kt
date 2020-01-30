package com.crazylegend.setofusefulkotlinextensions


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.crazylegend.kotlinextensions.context.getCompatColor
import com.crazylegend.kotlinextensions.delegates.activityVM
import com.crazylegend.kotlinextensions.exhaustive
import com.crazylegend.kotlinextensions.log.debug
import com.crazylegend.kotlinextensions.recyclerview.RecyclerSwipeItemHandler
import com.crazylegend.kotlinextensions.recyclerview.clickListeners.forItemClickListenerDSL
import com.crazylegend.kotlinextensions.recyclerview.initRecyclerViewAdapter
import com.crazylegend.kotlinextensions.recyclerview.recyclerSwipe
import com.crazylegend.kotlinextensions.retrofit.RetrofitResult
import com.crazylegend.kotlinextensions.views.AppRater
import com.crazylegend.setofusefulkotlinextensions.adapter.TestAdapter22
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.android.synthetic.main.activity_main.*

class MainAbstractActivity : AppCompatActivity(R.layout.activity_main) {

    private val testAVM by activityVM<TestAVM>()
    private val adapter by lazy {
        TestAdapter22()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppRater.appLaunched(this, supportFragmentManager, 0, 0) {
            appTitle = getString(R.string.app_name)
            buttonsBGColor = getCompatColor(R.color.colorAccent)
        }

        recycler.initRecyclerViewAdapter(adapter)

        adapter.forItemClickListener = forItemClickListenerDSL { position, item, _ ->
            debug("CLICKED AT ${item.title} at position $position")
        }
        adapter.submitList(listOf(
                TestModel("1", 1, "1", 1),
                TestModel("1", 1, "1", 1),
                TestModel("1", 1, "1", 1),
                TestModel("1", 1, "1", 1),
                TestModel("1", 1, "1", 1)
        ))

        recyclerSwipe(this, {
            debug("SWIPED at left $it")
        }, {
            debug("SWIPED AT right $it")
        }, {
            swipeDirection = RecyclerSwipeItemHandler.SwipeDirs.BOTH
            drawableLeft = android.R.drawable.ic_delete
            drawLeftBackground = true
            leftBackgroundColor = R.color.colorPrimary
            drawableRight = android.R.drawable.ic_input_get
        }).attachToRecyclerView(recycler)

        testAVM.posts?.observe(this, Observer {
            it?.apply {
                when (it) {
                    is RetrofitResult.Success -> {
                        adapter.submitList(it.value)
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


