package com.foyer.hbc.presentation.match

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.foyer.hbc.R
import com.foyer.hbc.base.BaseFragment
import com.foyer.hbc.databinding.FragmentMatchsBinding
import com.foyer.hbc.domain.data.matchs.MatchState
import com.foyer.hbc.domain.data.matchs.TEAM
import com.foyer.hbc.presentation.common.GridSpacingItemDecoration
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchsFagment : BaseFragment<FragmentMatchsBinding>(), MatchContract.ViewEvent,
    AdapterView.OnItemSelectedListener {

    ///////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        private const val COLUMN_NUMBER = 3
        private const val MATCH_SPACING = 20
        private const val DRIVE_PREFIX_URL = "http://docs.google.com/viewer?url="
    }

    override fun getViewBinding(): FragmentMatchsBinding {
        return FragmentMatchsBinding.inflate(layoutInflater)
    }

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val viewModel: MatchViewModel by viewModel()
    private lateinit var matchsAdapter: MatchsAdapter

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    override fun initUI() {
        initRecyclerView()
        binding?.matchBroonsCheckboxFilter?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.applyFilter()
            } else {
                viewModel.resetFilter()
            }
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.team,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding?.headerSelector?.adapter = adapter
        }
        binding?.headerSelector?.onItemSelectedListener = this
    }

    override fun initData() {
        viewModel.getMatchs(TEAM.SG)
    }

    override fun collectData() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    MatchState.Loading -> {
                        binding?.matchsRecyclerview?.visibility = View.INVISIBLE
                        showLoader()
                    }
                    is MatchState.HasMatch -> {
                        hideLoader()
                        binding?.matchBroonsCheckboxFilter?.isEnabled = true
                        binding?.matchsRecyclerview?.visibility = View.VISIBLE
                        matchsAdapter.setData(it.matchs)
                    }
                    MatchState.Error -> {
                        hideLoader()
                        binding?.matchError?.visibility = View.VISIBLE
                        binding?.matchBroonsCheckboxFilter?.visibility = View.GONE
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // EVENTS
    ///////////////////////////////////////////////////////////////////////////

    override fun onClickMatch(pdfPath: String?) {
        pdfPath?.let {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(DRIVE_PREFIX_URL + it))
            startActivity(browserIntent)
        }
    }


    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        viewModel.getMatchs(TEAM.values()[p2])
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        // Nothing to do
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun initRecyclerView() {
        matchsAdapter = MatchsAdapter(this)
        binding?.matchsRecyclerview?.apply {
            addItemDecoration(
                GridSpacingItemDecoration(
                    COLUMN_NUMBER,
                    MATCH_SPACING,
                    true
                )
            )
            layoutManager = GridLayoutManager(
                requireContext(),
                COLUMN_NUMBER
            )
            adapter = matchsAdapter
        }
    }
}