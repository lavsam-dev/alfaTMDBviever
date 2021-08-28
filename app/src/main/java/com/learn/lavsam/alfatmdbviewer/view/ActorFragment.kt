package com.learn.lavsam.alfatmdbviewer.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.ActorFragmentBinding
import com.learn.lavsam.alfatmdbviewer.model.AppStateActor
import com.learn.lavsam.alfatmdbviewer.model.data.Actor
import com.learn.lavsam.alfatmdbviewer.viewmodel.ActorViewModel

class ActorFragment : Fragment() {
    private lateinit var viewModel: ActorViewModel
    private var _binding: ActorFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ActorFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ActorViewModel::class.java)
    }

    private val adapter = ActorFragmentAdapter(object : ActorFragment.OnItemViewClickListener {
        override fun onItemViewClick(actor: Actor) {
            activity?.supportFragmentManager?.apply {
                beginTransaction()
                    .replace(R.id.container, DetailMovieFragment.newInstance(Bundle().apply {
                        putParcelable(DetailMovieFragment.BUNDLE_EXTRA, actor)
                    }))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }
    })

    private fun renderData(appStateActor: AppStateActor) {
        when (appStateActor) {
            is AppStateActor.Success -> {
                adapter.setActor(appStateActor.actor)
            }
            is AppStateActor.Loading -> {
            }
            is AppStateActor.Error -> {
                binding.actorMain.showSnackBarAction(getString(R.string.error_appstate),
                    getString(R.string.reload_appstate),
                    { viewModel.getActorFromWebSource() })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.actorList.adapter = adapter
        val observer = Observer<AppStateActor> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getActorFromWebSource()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(actor: Actor)
    }
}