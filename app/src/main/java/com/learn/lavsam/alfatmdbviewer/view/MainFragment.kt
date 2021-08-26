package com.learn.lavsam.alfatmdbviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.MainFragmentBinding
import com.learn.lavsam.alfatmdbviewer.model.AppState
import com.learn.lavsam.alfatmdbviewer.model.data.Movie
import com.learn.lavsam.alfatmdbviewer.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val adapter = MainFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(movie: Movie) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailMovieFragment.newInstance(Bundle().apply {
                        putParcelable(DetailMovieFragment.BUNDLE_EXTRA, movie)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mainFragmentRecyclerView.adapter = adapter
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getMovieFromWebSource()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setMovie(appState.movieData)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.main.showSnackBar(getString(R.string.error_appstate),
                    getString(R.string.reload_appstate),
                    { viewModel.getMovieFromWebSource() })
            }
        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(movie: Movie)
    }
}