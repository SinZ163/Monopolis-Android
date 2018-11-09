package me.syrin.monopolis.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.property_management_property.view.*
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.tiles.Estate
import me.syrin.monopolis.common.game.tiles.Property
import me.syrin.monopolis.common.network.DowngradePropertyPacket
import me.syrin.monopolis.common.network.UpgradePropertyPacket
import kotlin.math.abs

class PropertyManagementAdapter(private val game: Monopolis, private var properties: List<Property>) :
        RecyclerView.Adapter<PropertyManagementAdapter.MyViewHolder>() {

    class MyViewHolder(val propertyView: LinearLayout) : RecyclerView.ViewHolder(propertyView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PropertyManagementAdapter.MyViewHolder {
        // create a new view
        val propertyView = LayoutInflater.from(parent.context).inflate(R.layout.property_management_property, parent, false) as LinearLayout

        return MyViewHolder(propertyView)
    }
    fun update(properties: List<Property>) {
        this.properties = properties
        notifyDataSetChanged()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.propertyView.text_view_property_name.text = properties[position].name
        // TODO: disable/enable buttons and edit state text for mortgage/houses

        val property = properties[position]
        if (property.mortgaged) {
            holder.propertyView.button_minus.isEnabled = false
            holder.propertyView.text_view_state.text = game.activity.getString(R.string.mortgaged)
            holder.propertyView.button_plus.isEnabled = property.canMortgage(game)
        } else if (property is Estate) {
            val set = property.getPropertySet(game)
            var canSell = true
            var canBuy = true
            for (item in set) {
                // What
                if (item !is Estate) break

                if (item.owner != property.owner) {
//                    canSell = false
                    canBuy = false
                    continue
                }
                // can buy/sell as long as the difference with the set is <= 1
                if(abs(item.houseCount + 1 - property.houseCount) > 1) {
                    canBuy = false
                } else {
                    if (property.houseCount == 4 && game.hotelCount == 0) canBuy = false
                    if (property.houseCount < 4 && game.hotelCount == 0) canBuy = false
                }
                if (abs(item.houseCount - 1 - property.houseCount) > 1) {
                    canSell = false
                } else if (property.houseCount == 5 && game.houseCount < 4) canSell = false
            }
            holder.propertyView.button_minus.isEnabled = canSell
            holder.propertyView.button_plus.isEnabled = canBuy

            // state text
            when {
                property.houseCount == 0 -> // its just rent
                    holder.propertyView.text_view_state.text = game.activity.getString(R.string.no_houses)
                property.houseCount < 5 -> // there are houses
                    holder.propertyView.text_view_state.text = property.houseCount.toString() + game.activity.getString(R.string.space_houses)
                property.houseCount == 5 -> // there is a hotel
                    holder.propertyView.text_view_state.text = game.activity.getString(R.string.hotel)
            }
        }

        holder.propertyView.button_minus.setOnClickListener {
            game.send(DowngradePropertyPacket(property.owner!!.name, game.tiles.indexOf(property)), true)
        }
        holder.propertyView.button_plus.setOnClickListener {
            game.send(UpgradePropertyPacket(property.owner!!.name, game.tiles.indexOf(property)), true)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = properties.size
}