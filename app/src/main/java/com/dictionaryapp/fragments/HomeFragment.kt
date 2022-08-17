package com.dictionaryapp.fragments

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.dictionaryapp.adapter.ItemListener
import com.dictionaryapp.adapter.MeaningsAdapter
import com.dictionaryapp.adapter.PhoneticsAdapter
import com.dictionaryapp.base_classes.BaseFragment
import com.dictionaryapp.data.DictionaryDataManager
import com.dictionaryapp.data.NetworkResult
import com.dictionaryapp.data.models.DictionaryAPI
import com.dictionaryapp.data.models.Meanings
import com.dictionaryapp.data.models.Phonetics
import com.dictionaryapp.databinding.HomeFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : BaseFragment<HomeFragmentBinding>(), ItemListener {
    override fun bindingInflater(): HomeFragmentBinding =
        HomeFragmentBinding.inflate(layoutInflater)


    private val dictionaryDataManager = DictionaryDataManager()
    private var word: String = "hi"


    override fun setup() {
        binding.apply {
            refreshButton.setOnClickListener {
                getDictionaryData()
            }

        }
        getDictionaryData()
    }

    private fun getDictionaryData() {
        lifecycleScope.launch(Dispatchers.Main) {
            dictionaryDataManager.getDictionary(word).collect(::onGetResponse)
        }
    }

    private fun onGetResponse(state: NetworkResult<List<DictionaryAPI>>) {
        when (state) {
            is NetworkResult.Fail -> onResponseFail(state.message)
            is NetworkResult.Loading -> onResponseLoading()
            is NetworkResult.Success -> onResponseSuccess(state.data[0])
        }
    }

    private fun onResponseSuccess(dictionary: DictionaryAPI) {
        setupMeaningsAdapter(dictionary.meanings)
        setupPhoneticsAdapter(dictionary.phonetics)
        binding.apply {
            errorScreen.hide()
            loading.hide()
            content.show()
            word.text = dictionary.word

        }
    }


    private fun onResponseLoading() {
        binding.apply {
            errorScreen.hide()
            loading.show()
            content.hide()
        }
    }

    private fun onResponseFail(message: String) {
        binding.apply {
            errorScreen.show()
            loading.hide()
            content.hide()
            errorText.text = message
        }
    }

    private fun setupMeaningsAdapter(meanings: List<Meanings>) {
        val meaningsAdapter = MeaningsAdapter(meanings, this)
        binding.meanings.adapter = meaningsAdapter
    }

    override fun onClickItem(singleMeaning: Meanings) {

    }


    private fun setupPhoneticsAdapter(phonetics: List<Phonetics>) {
        val phoneticsAdapter = PhoneticsAdapter(phonetics)
        binding.wordPhonetics.adapter = phoneticsAdapter
    }

    private fun View.hide() {
        this.visibility = View.GONE
    }

    private fun View.show() {
        this.visibility = View.VISIBLE
    }


}