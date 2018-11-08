package me.syrin.monopolis.common


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import me.syrin.monopolis.common.game.Monopolis
import me.syrin.monopolis.common.game.tiles.*
import me.syrin.monopolis.common.tiles.CardDrawDrawable
import me.syrin.monopolis.common.tiles.EstateDrawable
import me.syrin.monopolis.common.tiles.PropertyDrawable
import me.syrin.monopolis.common.tiles.TaxDrawable

class BoardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false)

    }
    fun updateTile(tile:Tile, index: Int) {
        val tileImageID = resources.getIdentifier("tile$index", "id", activity!!.packageName)
        val tileImage = view!!.findViewById(tileImageID) as ImageView
        tileImage.setImageDrawable(when(tile) {
            is Go -> resources.getDrawable(R.drawable.go, null)
            is Jail -> resources.getDrawable(R.drawable.jail, null)
            is FreeParking -> resources.getDrawable(R.drawable.freeparking, null)
            is GoToJail -> resources.getDrawable(R.drawable.gotojail, null)
            is Estate -> EstateDrawable(tile, index / 10)
            is Railroad -> PropertyDrawable(tile, index / 10)
            is Utility -> PropertyDrawable(tile, index / 10)
            is CardDraw -> CardDrawDrawable(tile, index / 10)
            is Tax -> TaxDrawable(tile, index / 10)
            else -> null
        })
    }

    fun initialiseBoard(monopolis: Monopolis) {
        monopolis.tiles.forEachIndexed { index, tile ->
            updateTile(tile, index)
        }
    }
}

class SquareConstraintLayout : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?,
                @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.v("SquareConstraintLayout", "Width: $widthMeasureSpec, Height: $heightMeasureSpec")
        var height = heightMeasureSpec
        var width = widthMeasureSpec
        Log.v("SquareConstraintLayout", "NewWidth: $width, NewHeight: $height")

        //wrap_content causes -2**31 -1 for the value
        if (height < 0) {
            height = width
        }
        if (width < 0) {
            width = height
        }
        if (width < height) {
            Log.v("SquareConstraintLayout", "Using width")
            super.onMeasure(width, height)
        } else {
            Log.v("SquareConstraintLayout", "Using height")
            super.onMeasure(width, height)
        }
    }
}
