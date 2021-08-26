package com.learn.lavsam.alfatmdbviewer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.lavsam.alfatmdbviewer.databinding.MainFragmentRecyclerItemBinding
import com.learn.lavsam.alfatmdbviewer.model.data.Movie

class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>()  {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainFragmentAdapter.MainViewHolder {
        val binding = MainFragmentRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainFragmentAdapter.MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MainViewHolder(private val binding: MainFragmentRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {
            textViewPopularity.text = movie.vote_average.toString()
            textViewOriginalTitle.text = movie.original_title
            textViewTitle.text = movie.title
            textViewYearOfRelease.text = movie.release_date.toString()
            movie.poster_path?.let { imageViewPoster.setImageResource(it) }
            root.setOnClickListener { onItemViewClickListener?.onItemViewClick(movie) }
        }
    }
}