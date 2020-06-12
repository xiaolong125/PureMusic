/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flygo.puremusic.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.flygo.puremusic.R

/**
 * Created by hefuyi on 2016/10/24.
 */
class ForegroundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {
    private var mForeground: Drawable? = null

    /**
     * Supply a drawable resource that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawableResId The drawable resource to be drawn on top of the children.
     */
    fun setForegroundResource(drawableResId: Int) {
        setForeground(context.resources.getDrawable(drawableResId))
    }

    /**
     * Supply a Drawable that is to be rendered on top of all of the child
     * views in the frame layout.
     *
     * @param drawable The Drawable to be drawn on top of the children.
     */
    override fun setForeground(drawable: Drawable) {
        if (mForeground === drawable) {
            return
        }
        if (mForeground != null) {
            mForeground!!.callback = null
            unscheduleDrawable(mForeground)
        }
        mForeground = drawable
        if (drawable != null) {
            drawable.callback = this
            if (drawable.isStateful) {
                drawable.state = drawableState
            }
        }
        requestLayout()
        invalidate()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mForeground
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        if (mForeground != null) mForeground!!.jumpToCurrentState()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        if (mForeground != null && mForeground!!.isStateful) {
            mForeground!!.state = drawableState
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mForeground != null) {
            mForeground!!.setBounds(0, 0, measuredWidth, measuredHeight)
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mForeground != null) {
            mForeground!!.setBounds(0, 0, w, h)
            invalidate()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mForeground != null) {
            mForeground!!.draw(canvas)
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundImageView)
        val foreground =
            a.getDrawable(R.styleable.ForegroundImageView_android_foreground)
        foreground?.let { setForeground(it) }
        a.recycle()
    }
}