package com.udacity

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()

    private var angle = 0f
    private var rectWidth = 0f

    private var arcColor = 0
    private var rectColor = 0
    private var loadingColor = 0
    private var textColor = 0

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            arcColor = getColor(R.styleable.LoadingButton_arcColor, 0)
            rectColor = getColor(R.styleable.LoadingButton_rectColor, 0)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, 0)
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rect = RectF(0f, 0f, widthSize.toFloat(), 150f)
        val paint = Paint().apply {
            color = textColor
            style = Paint.Style.STROKE
            textAlign = Paint.Align.CENTER

            typeface = Typeface.create("", Typeface.BOLD)
        }

        val paint2 = Paint().apply {
            color = rectColor
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER

            typeface = Typeface.create("", Typeface.BOLD)
        }

        canvas?.drawRoundRect(rect,10f,10f,paint)

        val rect2 = RectF(0f, 0f, width.toFloat(), 150f)
        canvas?.drawRoundRect(rect2, 10f, 10f, paint2)
        paint.textSize = 60.0f
        paint.style = Paint.Style.FILL
        if (buttonState == ButtonState.Completed) {

            canvas?.drawText("Download", (widthSize / 2).toFloat(), (heightSize / 2).toFloat(), paint)
        }else if (buttonState == ButtonState.Loading){

            val rect2 = RectF(0f, 0f, rectWidth, 150f)
            paint2.color = loadingColor
            canvas?.drawRoundRect(rect2, 10f, 10f, paint2)
            canvas?.drawText("We are loading", (widthSize / 2).toFloat(), (heightSize / 2).toFloat(), paint)
            val rectF = RectF(20f, 20f, 120f, 120f)
            paint.color = arcColor
            canvas?.drawArc(rectF, 270f, angle, true, paint)
        }
    }

    fun animateArc(){
        buttonState = ButtonState.Loading
        angle = 0f
        valueAnimator = ValueAnimator.ofFloat(0f,360f)

        valueAnimator.duration = 4000
        //valueAnimator.setFloatValues(0f,50f)
        valueAnimator.addUpdateListener {

            angle = it.getAnimatedValue() as Float
            rectWidth = angle * (width.toFloat()/360f)
            Log.d("FLUX","-->" +rectWidth )
            Log.d("FLUX","------>" +it.getAnimatedValue() )
            invalidate()
            if (it.getAnimatedValue() == 360f){
                buttonState = ButtonState.Completed
            }

        }
        valueAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
                MeasureSpec.getSize(w),
                heightMeasureSpec,
                0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}