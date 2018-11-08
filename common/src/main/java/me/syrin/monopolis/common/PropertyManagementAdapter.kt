package me.syrin.monopolis.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.property_management_property.view.*
import me.syrin.monopolis.common.game.tiles.Property

class PropertyManagementAdapter(private val properties: List<Property>) :
        RecyclerView.Adapter<PropertyManagementAdapter.MyViewHolder>() {

    class MyViewHolder(val propertyView: LinearLayout) : RecyclerView.ViewHolder(propertyView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PropertyManagementAdapter.MyViewHolder {
        // create a new view
        val propertyView = LayoutInflater.from(parent.context).inflate(R.layout.property_management_property, parent, false) as LinearLayout

        return MyViewHolder(propertyView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.propertyView.text_view_property_name.text = properties[position].name
        // TODO: disable/enable buttons and edit state text for mortgage/houses

        holder.propertyView.button_minus.setOnClickListener {
            // TODO: sell hotel, sell house, mortgage
        }
        holder.propertyView.button_plus.setOnClickListener {
            // TODO: unmortgage, add house, add hotel
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = properties.size
}