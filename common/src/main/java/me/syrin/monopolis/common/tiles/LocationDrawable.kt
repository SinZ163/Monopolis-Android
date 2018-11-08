package me.syrin.monopolis.common.tiles

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import me.syrin.monopolis.common.game.Player
import kotlin.math.abs


class LocationDrawable(val players: List<Player>, val rotation: Int) : Drawable() {
    override fun draw(canvas: Canvas) {
        canvas.rotate(rotation*90.0f, bounds.width()/2.0f, bounds.height()/2.0f)
        val width = if(rotation % 2 == 0) bounds.width() else bounds.height()
        val height = if(rotation % 2 == 0) bounds.height() else bounds.width()

        val offsetWidth = if(rotation % 2 == 0) 0 else (abs(width - height) / 2)
        val offsetHeight = -offsetWidth


        // Draw the background as black
        // 0.90 - 0.03*index
        //     0.25*index%4, 0.75 + 0.1*index/4 -> 0.25*index%4+0.25, 0.75 + 0.1*index/4 + 0.1
        players.forEachIndexed { index, player ->
            val startX = offsetWidth + 0.25f*(index%4)*width
            val endX = offsetWidth + 0.25f*(index%4 +1)*width
            val startY = offsetHeight + (0.75f + 0.1f*(index/4))*height
            val endY = offsetHeight + (0.85f + 0.1f*(index/4))*height
            val paint = Paint().apply { setARGB(180,player.colour[0], player.colour[1], player.colour[2]) }
            canvas.drawRect(startX, startY, endX, endY, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}