package com.vanilla.triangularsequence

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanilla.triangularsequence.databinding.SingleRowTsBinding


/**
 * Created by Ram Mandal on 14/03/2023
 * @System: Apple M1 Pro
 */
class TriangularSequenceListAdapter(private val listItem: ArrayList<ListData>) :
    RecyclerView.Adapter<TriangularSequenceListAdapter.TSViewHolder>() {

    inner class TSViewHolder(val binding: SingleRowTsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TSViewHolder {
        val binding = SingleRowTsBinding.inflate(LayoutInflater.from(parent.context))
        return TSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TSViewHolder, position: Int) {
        with(holder) {
            with(listItem[position]) {
                binding.indexValue.text = this.index
                Glide.with(holder.binding.imageView.context)
                    .load(Uri.parse(icon))
                    .circleCrop()
                    .into(holder.binding.imageView)
            }
        }
    }

    override fun getItemCount(): Int = listItem.size
}