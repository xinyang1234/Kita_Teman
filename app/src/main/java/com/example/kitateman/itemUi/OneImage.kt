package com.example.kitateman.itemUi

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.example.kitateman.R

class OneImage(
    context: Context, attrs: AttributeSet?
) : AppCompatButton(context, attrs) {

    private val drawable = AppCompatResources.getDrawable(context, R.drawable.baseline_image_24)

    init {
        background = AppCompatResources.getDrawable(context, R.drawable.bg_edit_text_with_stroke)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawable?.let {
            val left = paddingLeft
            val top = paddingTop
            val right = width - paddingRight
            val bottom = height - paddingBottom

            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }
}