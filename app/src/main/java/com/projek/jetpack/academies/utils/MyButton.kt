package com.projek.jetpack.academies.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.projek.jetpack.R

class MyButton: AppCompatButton {
    private var enableBackground:Drawable?=null
    private var disableBackground:Drawable?=null
    private var textColour:Int=0

    constructor(context:Context):super(context){
        init()
    }
    constructor(context:Context,attrs:AttributeSet):super(context,attrs){
        init()
    }
    constructor(context:Context,attrs:AttributeSet,defStyleAttr:Int):super(context,attrs,defStyleAttr){
        init()
    }
    override fun onDraw(canvas:Canvas){
        super.onDraw(canvas)
        background=if(isEnabled) enableBackground else disableBackground
        setTextColor(textColour)
        textSize=12f
        gravity=CENTER
    }
    private fun init(){
        val resources=resources
        enableBackground=resources.getDrawable(R.drawable.bg_button,Resources.getSystem().newTheme())
        disableBackground=resources.getDrawable(R.drawable.bg_button_disable,Resources.getSystem().newTheme())
        textColour=ContextCompat.getColor(context,android.R.color.background_light)
    }

}