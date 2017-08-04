package com.franctan.lonelyplanetcurrencyguide.main_activity

import android.app.Dialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class ChooseCurrencyDialogFragment : DialogFragment() {

    val SELECTED_INDEX_KEY = "SelectedIndex"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: MainViewModel

    lateinit var materialDialog: MaterialDialog

    companion object Factory {
        fun create(): ChooseCurrencyDialogFragment {
            val fragment: ChooseCurrencyDialogFragment = ChooseCurrencyDialogFragment()
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        viewModel = ViewModelProviders.of(activity, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val selectedIndex: Int = getCurrentSelectedIndex(savedInstanceState)

        materialDialog = MaterialDialog.Builder(activity)
                .title("Choose a currency")
                .items(viewModel.currencyList)
                .negativeText("Cancel")
                .positiveText("Choose")
                .onNegative { _, _ -> dismiss() }
                .itemsCallbackSingleChoice(selectedIndex) { dialog, itemView, which, text ->
                    viewModel.chooseCurrency(text.toString())
                    true
                }
                .build()

        return materialDialog
    }

    fun getCurrentSelectedIndex(savedInstanceState: Bundle?): Int {
        return savedInstanceState?.get(SELECTED_INDEX_KEY) as Int? ?: viewModel.currentIndexForSelectedCurrency
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        val selectedIndex = materialDialog.selectedIndex
        outState?.putInt(SELECTED_INDEX_KEY, selectedIndex)
    }
}