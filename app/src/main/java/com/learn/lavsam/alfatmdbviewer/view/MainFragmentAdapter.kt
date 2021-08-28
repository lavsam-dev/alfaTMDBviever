package com.learn.lavsam.alfatmdbviewer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.lavsam.alfatmdbviewer.R
import com.learn.lavsam.alfatmdbviewer.databinding.MainFragmentRecyclerItemBinding
import com.learn.lavsam.alfatmdbviewer.model.data.Movie

class MainFragmentAdapter(private var onItemViewClickListener: MainFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<MainFragmentAdapter.MainViewHolder>() {

    private var movieData: List<Movie> = listOf()

    fun setMovie(data: List<Movie>) {
        movieData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainFragmentAdapter.MainViewHolder {
        val binding = MainFragmentRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainFragmentAdapter.MainViewHolder, position: Int) {
        holder.bind(movieData[position])
    }

    override fun getItemCount(): Int {
        return movieData.size
    }

    inner class MainViewHolder(private val binding: MainFragmentRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {
            textViewPopularity.text = movie.vote_average.toString()
            textViewTitle.text = movie.title
            textViewYearOfRelease.text = movie.release_date.toString()
            when (movie.id) {
                10401 -> imageViewPoster.setImageResource(R.drawable.the_girl_on_the_bridge_poster)
                680 -> imageViewPoster.setImageResource(R.drawable.pulpfiction_poster)
                9659 -> imageViewPoster.setImageResource(R.drawable.madmax_poster)
                19124 -> imageViewPoster.setImageResource(R.drawable.blind_fury_poster)
                10681 -> imageViewPoster.setImageResource(R.drawable.wall_e_poster)
                679 -> imageViewPoster.setImageResource(R.drawable.aliens_poster)
            }
            root.setOnClickListener { onItemViewClickListener?.onItemViewClick(movie) }
        }
    }
}