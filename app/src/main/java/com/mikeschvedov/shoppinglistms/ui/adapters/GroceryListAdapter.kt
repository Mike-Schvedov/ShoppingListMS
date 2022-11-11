package com.mikeschvedov.shoppinglistms.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikeschvedov.shoppinglistms.R
import com.mikeschvedov.shoppinglistms.models.GroceryItem

import javax.inject.Inject

class GroceryListAdapter @Inject constructor(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder>() {

    var list: MutableList<GroceryItem> = mutableListOf()

    fun interface OnItemClickListener{
        fun onItemClicked(
            item: GroceryItem
        )
    }

    // add new data
    fun setNewData(newData: List<GroceryItem>) {
        // passing the new and old list into the callback
        val diffCallback = DiffUtilCallbackShips(list, newData)
        // we get the result
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        // we clear the old list
        list.clear()
        // and replace it with the new list
        list.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.nameTextView_xml)
        var amountTextView: TextView = itemView.findViewById(R.id.amountTextView_xml)
        var isMarkedCheckBox: ImageView = itemView.findViewById(R.id.isBoughtItemview)
        var cardView: CardView = itemView.findViewById(R.id.cardview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        return GroceryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_holder,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {

        val item = list[position]

        // --- Setting the title --- //
        holder.nameTextView.text = item.name

        // --- Setting the amount --- //
        if (item.amount != null){
            holder.amountTextView.text = item.amount
        }else{
            holder.amountTextView.text = ""
        }

        // --- Setting isMarked UI --- //
        if (item.marked) {
            holder.isMarkedCheckBox.setImageResource(R.drawable.checkbox_ckecked)
        } else {
            holder.isMarkedCheckBox.setImageResource( R.drawable.checkbox_empty)
        }

        // Changing the color accordingly
        if(item.marked){
            holder.cardView.setCardBackgroundColor(Color.GRAY)
        }else{
           holder.cardView.setCardBackgroundColor(Color.WHITE)
        }

        // --- Setting isMarked --- //
        holder.isMarkedCheckBox.setOnClickListener {
            listener.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class DiffUtilCallbackShips(
    private val oldList: List<GroceryItem>,
    private val newList: List<GroceryItem>
) :
    DiffUtil.Callback() {

    // old size
    override fun getOldListSize(): Int = oldList.size

    // new list size
    override fun getNewListSize(): Int = newList.size

    // if items are same
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.name == newItem.name
    }

    // check if contents are same
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem == newItem
    }
}