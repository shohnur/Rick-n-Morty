package uz.task.ui.adapters

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_character.view.*
import uz.task.R
import uz.task.base.BaseAdapter
import uz.task.base.ViewHolder
import uz.task.network.models.AllCharactersResp

class CharacterAdapter(var c: Context) :
    BaseAdapter<AllCharactersResp.Result>(R.layout.item_character) {
    override fun bindViewHolder(holder: ViewHolder, data: AllCharactersResp.Result) {
        holder.itemView.apply {
            data.apply {
                nameC.text = name
                statusC.text = status
                locationC.text = origin.name
                Glide.with(c).load(image).into(imageC)
            }
        }
    }
}