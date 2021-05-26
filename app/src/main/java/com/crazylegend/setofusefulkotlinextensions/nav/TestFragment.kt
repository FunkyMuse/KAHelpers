package com.crazylegend.setofusefulkotlinextensions.nav

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.crazylegend.collections.generateRandomStringList
import com.crazylegend.customviews.databinding.CustomizableCardViewBinding
import com.crazylegend.recyclerview.generateRecyclerWithHolder
import com.crazylegend.setofusefulkotlinextensions.R
import com.crazylegend.setofusefulkotlinextensions.databinding.FragmentTestBinding
import com.crazylegend.viewbinding.viewBinding

/**
 * Created by Hristijan, date 2/15/21
 */
class TestFragment : Fragment(R.layout.fragment_test) {

    private val binding by viewBinding(FragmentTestBinding::bind)

    private val testAdapter by lazy {
        generateRecyclerWithHolder<String, CustomizableCardViewBinding>(CustomizableCardViewBinding::inflate){
            item, position, _, binding, _ ->
            binding.title.text = "Title ${position+1}"
            binding.content.text = item
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = testAdapter
        testAdapter.submitList(generateRandomStringList(100))
    }
}