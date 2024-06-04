package com.example.kitateman.itemUi

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.kitateman.R

class InputPassword : AppCompatEditText {
    private lateinit var eyeIcon: Drawable
    private lateinit var eyeOffIcon: Drawable
    private var isPasswordVisible: Boolean = false

    var onPasswordValidityChanged: ((Boolean) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye)!!
        eyeOffIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_off)!!
        setCompoundDrawablesWithIntrinsicBounds(null, null, eyeOffIcon, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    onPasswordValidityChanged?.invoke(false)
                } else {
                    onPasswordValidityChanged?.invoke(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = if (layoutDirection == LAYOUT_DIRECTION_RTL) {
                    compoundDrawables[0]
                } else {
                    compoundDrawables[2]
                }

                drawableEnd?.let {
                    if (event.rawX >= (right - it.bounds.width())) {
                        isPasswordVisible = !isPasswordVisible
                        inputType = if (isPasswordVisible) {
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        } else {
                            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                        }
                        setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            if (isPasswordVisible) eyeIcon else eyeOffIcon,
                            null
                        )
                        setSelection(text?.length ?: 0)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}
