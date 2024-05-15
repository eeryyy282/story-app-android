package com.example.storyappjuzairi.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.storyappjuzairi.R

class ButtonLogin : AppCompatButton {


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var txtColor: Int = 0
    private var enabledBackground: Drawable
    private var disableBackground: Drawable

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.white)
        enabledBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_button_login_register) as Drawable
        disableBackground = ContextCompat.getDrawable(
            context,
            R.drawable.bg_button_login_register_disable
        ) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disableBackground
        setTextColor(txtColor)
        textSize = 16f
        gravity = CENTER
        text =
            if (isEnabled) context.getString(R.string.text_enabled_button_login) else context.getString(
                R.string.text_disabled_button_login
            )
    }
}