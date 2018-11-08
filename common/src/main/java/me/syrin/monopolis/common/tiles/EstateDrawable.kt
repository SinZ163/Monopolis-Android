package me.syrin.monopolis.common.tiles

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import me.syrin.monopolis.common.game.tiles.Estate
import me.syrin.monopolis.common.game.tiles.PropertySet
import kotlin.math.abs

class EstateDrawable(val reference: Estate, val rotation: Int) : Drawable() {
    private val paints: Map<PropertySet, Paint> = mapOf(
            PropertySet.Brown to Paint().apply { setARGB(255, 132, 75, 50)},
            PropertySet.LightBlue to Paint().apply { setARGB(255, 152, 197, 222)},
            PropertySet.Pink to Paint().apply { setARGB(255, 191, 51, 133)},
            PropertySet.Orange to Paint().apply { setARGB(255, 218, 131, 26)},
            PropertySet.Red to Paint().apply { setARGB(255, 209, 24, 32)},
            PropertySet.Yellow to Paint().apply { setARGB(255, 223, 213, 0)},
            PropertySet.Green to Paint().apply { setARGB(255, 27, 157, 79)},
            PropertySet.DarkBlue to Paint().apply { setARGB(255, 0, 100, 167)}
    )
    private val blackPaint = Paint().apply {
        setARGB(255,0,0,0)
    }
    private val textPaint: Paint = Paint().apply {
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.5f, Resources.getSystem().displayMetrics)
        setARGB(255,0,0,0)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
    }
    override fun draw(canvas: Canvas) {

        canvas.rotate(rotation*90.0f, bounds.width()/2.0f, bounds.height()/2.0f)
        val width = if(rotation % 2 == 0) bounds.width() else bounds.height()
        val height = if(rotation % 2 == 0) bounds.height() else bounds.width()

        val offsetWidth = if(rotation % 2 == 0) 0 else (abs(width - height) / 2)
        val offsetHeight = -offsetWidth

        if(reference.propertySet == PropertySet.Utility || reference.propertySet == PropertySet.Railroad) {
            return
        }
        // Draw the background as black
        canvas.drawRect(Rect(offsetWidth+0,offsetHeight+0,offsetWidth+width,offsetHeight+height), blackPaint)
        // Draw the background of the board
        canvas.drawRect(Rect(offsetWidth+1,offsetHeight+1,offsetWidth+width-1,offsetHeight + height-1), Paint().apply {setARGB(255,206, 230, 208)})
        // Draw the PropertySet colour
        canvas.drawRect(Rect(offsetWidth+1,offsetHeight+1,offsetWidth + width-1,(offsetHeight + 0.21*height).toInt()), paints[reference.propertySet]!!)

        canvas.drawRect(Rect(offsetWidth, (offsetHeight + 0.21*height).toInt(), offsetWidth + width, (offsetHeight + 0.23* height).toInt()), blackPaint)

        val words = arrayListOf<String>()
        for (word in reference.name.toUpperCase().split(' ')) {
            if (word.contains('-')) {
                words.add(word.substring(0, word.indexOf('-') +1))
                words.add(word.substring(word.indexOf('-')+1))
            } else {
                words.add(word)
            }
        }
        var textY = offsetHeight + 0.35f*height
        for (word in words) {
            canvas.drawText(word, offsetWidth + 0.5f*width, textY, textPaint)
            textY += textPaint.descent() - textPaint.ascent()
        }

        canvas.drawText("₩${reference.price}", offsetWidth + 0.5f*width, offsetHeight + 0.95f*height, textPaint)
        val owner = reference.owner
        if (owner != null) {
            val paint = Paint().apply { setARGB(180,owner.colour[0], owner.colour[1], owner.colour[2]) }
            canvas.drawRect(offsetWidth.toFloat() + 1, offsetHeight + 0.95f*height, offsetWidth + width.toFloat() - 1, offsetHeight + height.toFloat() - 1, paint)
        }

        Log.i("EstateDrawable", "$rotation, $offsetWidth + width, $offsetHeight + height")
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}