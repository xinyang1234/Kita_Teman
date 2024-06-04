package com.example.kitateman.itemUi

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.kitateman.R

class OneButtonCamera : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = 0

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        background = AppCompatResources.getDrawable(context, R.drawable.bg_button_disable)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textSize = 12f
        gravity = Gravity.CENTER
        text = context.getString(R.string.camera_title)
        setTextColor(txtColor)
    }
}