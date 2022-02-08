package uz.task.base

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import uz.task.utils.inflate

abstract class BaseAdapter<T>(@LayoutRes val layoutID: Int) : RecyclerView.Adapter<ViewHolder>() {

    var listener: ((data: T) -> Unit)? = null
    var loadMoreDataListener: (() -> Unit)? = null
    protected var items = arrayListOf<T>()

    @SuppressLint("NotifyDataSetChanged")
    open fun addData(data: List<T>) {
        if (items.isEmpty()) {
            items.addAll(data)
            notifyDataSetChanged()
        } else {
            val position = items.size
            items.addAll(data)
            notifyItemRangeChanged(position, data.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(layoutID))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            bindViewHolder(this, items[holder.adapterPosition])
            holder.itemView.setOnClickListener {
                if (holder.adapterPosition != -1)
                    listener?.invoke(items[holder.adapterPosition])
            }

            if (adapterPosition == itemCount - 1) loadMoreDataListener?.invoke()
        }
    }

    abstract fun bindViewHolder(holder: ViewHolder, data: T)

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)