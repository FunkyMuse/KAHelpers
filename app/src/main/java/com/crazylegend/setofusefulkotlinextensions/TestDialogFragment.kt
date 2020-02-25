package com.crazylegend.setofusefulkotlinextensions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.setofusefulkotlinextensions.databinding.DialogTestBinding


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */
class TestDialogFragment : DialogFragment() {


    private val binding by viewBinding(DialogTestBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}