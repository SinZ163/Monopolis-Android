package me.syrin.monopolis.common.tiles

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import me.syrin.monopolis.common.game.tiles.Property
import kotlin.math.abs

class PropertyDrawable(val reference: Property, val rotation: Int) : Drawable() {
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


        // Draw the background as black
        canvas.drawRect(Rect(offsetWidth+0,offsetHeight+0,offsetWidth+width,offsetHeight+height), blackPaint)
        // Draw the background of the board
        canvas.drawRect(Rect(offsetWidth+1,offsetHeight+2,offsetWidth+width-1,offsetHeight + height-1), Paint().apply {setARGB(255,206, 230, 208)})

        val words = arrayListOf<String>()
        for (word in reference.name.toUpperCase().split(' ')) {
            if (word.contains('-')) {
                words.add(word.substring(0, word.indexOf('-') +1))
                words.add(word.substring(word.indexOf('-')+1))
            } else {
                words.add(word)
            }
        }
        var textY = offsetHeight + 0.1f*height
        for (word in words) {
            canvas.drawText(word, offsetWidth + 0.5f*width, textY, textPaint)
            textY += textPaint.descent() - textPaint.ascent()
        }


        // TODO: Draw image for Utility / Railroad

        canvas.drawText("₩${reference.price}", offsetWidth + 0.5f*width, offsetHeight + 0.95f*height, textPaint)
        val owner = reference.owner
        if (owner != null) {
            val paint = Paint().apply { setARGB(180,owner.colour[0], owner.colour[1], owner.colour[2]) }
            canvas.drawRect(offsetWidth.toFloat() + 1, offsetHeight + 0.95f*height, offsetWidth + width.toFloat() - 1, offsetHeight + height.toFloat() - 1, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}