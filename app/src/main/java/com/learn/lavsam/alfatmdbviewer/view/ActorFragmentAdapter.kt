package com.learn.lavsam.alfatmdbviewer.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.lavsam.alfatmdbviewer.databinding.ActorRecyclerItemBinding
import com.learn.lavsam.alfatmdbviewer.model.data.Actor
import com.learn.lavsam.alfatmdbviewer.view.ActorFragmentAdapter.ActorViewHolder

class ActorFragmentAdapter(private var onItemViewClickListener: ActorFragment.OnItemViewClickListener?) :
    RecyclerView.Adapter<ActorViewHolder>() {

    private var actorData: List<Actor> = listOf()

    fun setActor(data: List<Actor>) {
        actorData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorViewHolder {
        val binding = ActorRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ActorViewHolder(binding)
    }

    inner class ActorViewHolder(private val binding: ActorRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Actor) = with(binding) {
            actorName.text = actor.name
            when (actor.id) {
            }
            root.setOnClickListener { onItemViewClickListener?.onItemViewClick(actor) }
        }
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actorData[position])
    }

    override fun getItemCount(): Int {
        return actorData.size
    }
}