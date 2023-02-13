package com.example.dictionary.mvvm.presentation.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dictionary.R
import com.example.dictionary.databinding.HistoryFragmentBinding
import com.example.dictionary.mvvm.model.data.AppStateMVVM
import com.example.dictionary.mvvm.presentation.view.TranslationDialogFragment
import com.example.dictionary.mvvm.presentation.viewModel.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment(private var searchedWord: String) : Fragment() {

    private lateinit var binding: HistoryFragmentBinding
    private lateinit var model: HistoryViewModel
    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = HistoryFragmentBinding.inflate(inflater)
        return binding.historyFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initViews()
    }

    private fun initViews() {
        binding.historyFragmentRecyclerview.adapter = adapter
        model.getData(searchedWord, false)
    }

    private fun initViewModel() {
        if (binding.historyFragmentRecyclerview.adapter != null) {
            throw IllegalStateException("ViewModel not initialised")
        }
        val viewModel: HistoryViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(viewLifecycleOwner) {
            renderData(it)
        }
    }

    private fun renderData(appState: AppStateMVVM) {
        when (appState) {
            is AppStateMVVM.Success -> {
                val dataModel = appState.data
                appState.data?.let {
                    if (it.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_data_header),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (searchedWord == "") {
                            dataModel?.let { list ->
                                adapter.setData(list)
                            }
                        } else {
                            var wordFound = false
                            dataModel?.let {
                                for (i in dataModel.indices) {
                                    if (dataModel[i].text.equals(searchedWord)) {
                                        wordFound = true
                                        val dialog = TranslationDialogFragment(
                                            dataModel[i].text.toString(),
                                            "Слово есть в истории запросов",
                                            ""
                                        )
                                        dialog.show(
                                            childFragmentManager,
                                            "translation with image dialog"
                                        )
                                    }
                                }
                                if (!wordFound) {
                                    Toast.makeText(
                                        context,
                                        "Слово не найдено в истории запросов",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
            is AppStateMVVM.Loading -> {}
            is AppStateMVVM.Error -> {}
        }
    }
}