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

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.IntEvaluator
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.blankj.utilcode.util.ConvertUtils.dp2px
import com.blankj.utilcode.util.ScreenUtils
import com.flygo.puremusic.R
import com.flygo.puremusic.databinding.FragmentPlayerBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

/**
 * Create by KunMinX at 19/10/29
 */
class PlayerSlideListener(
    private val binding: FragmentPlayerBinding,
    private val slidingUpPanelLayout: SlidingUpPanelLayout
) : SlidingUpPanelLayout.PanelSlideListener {
    
    private var albumImageDrawable: Drawable? = null
    private var titleEndTranslationX = 0f
    private var artistEndTranslationX = 0f
    private var artistNormalEndTranslationY = 0f
    private var artistFullEndTranslationY = 0f
    private var contentNormalEndTranslationY = 0f
    private var contentFullEndTranslationY = 0f
    private var lyricLineHeight = 0f
    private var lyricFullHeight = 0f
    private var lyricLineStartTranslationY = 0f
    private var lyricLineEndTranslationY = 0f
    private var lyricFullTranslationY = 0f
    private var markStartX = 0
    private var previousStartX = 0
    private var playPauseStartX = 0
    private var nextStartX = 0
    private var playqueueStartX = 0
    private var playPauseEndX = 0
    private var previousEndX = 0
    private var markEndX = 0
    private var nextEndX = 0
    private var playqueueEndX = 0
    private var iconContainerStartY = 0
    private var iconContainerEndY = 0
    private val screenWidth: Int
    private val screenHeight: Int
    private val intEvaluator = IntEvaluator()
    private val floatEvaluator = FloatEvaluator()
    private val colorEvaluator = ArgbEvaluator()
    private val nowPlayingCardColor: Int
    private val playPauseDrawableColor: Int
    private var mStatus =
        Status.COLLAPSED

    enum class Status {
        EXPANDED, COLLAPSED, FULLSCREEN
    }

    override fun onPanelSlide(
        panel: View,
        slideOffset: Float
    ) {
        val params =
            binding.albumArt.getLayoutParams() as CoordinatorLayout.LayoutParams

        //animate albumImage
        val tempImgSize =
            intEvaluator.evaluate(slideOffset, dp2px(55F), screenWidth)
        params.width = tempImgSize
        params.height = tempImgSize
        binding.albumArt.setLayoutParams(params)

        //animate title and artist
        binding.title?.setTranslationX(
            floatEvaluator.evaluate(
                slideOffset,
                0,
                titleEndTranslationX
            )
        )
        binding.artist?.setTranslationX(
            floatEvaluator.evaluate(
                slideOffset,
                0,
                artistEndTranslationX
            )
        )
        binding.artist?.setTranslationY(
            floatEvaluator.evaluate(
                slideOffset,
                0,
                artistNormalEndTranslationY
            )
        )
        binding.summary?.setTranslationY(
            floatEvaluator.evaluate(
                slideOffset,
                0,
                contentNormalEndTranslationY
            )
        )

        //aniamte icons
        binding.playPause.setX(intEvaluator.evaluate(slideOffset, playPauseStartX, playPauseEndX) as Float)
        binding.playPause.setCircleAlpah(intEvaluator.evaluate(slideOffset, 0, 255))
        binding.playPause.drawableColor = 
            colorEvaluator.evaluate(
                slideOffset,
                playPauseDrawableColor,
                nowPlayingCardColor
            ) as Int

        binding.previous.setX(intEvaluator.evaluate(slideOffset, previousStartX, previousEndX)as Float)
        binding.mark?.setX(intEvaluator.evaluate(slideOffset, markStartX, markEndX)as Float)
        binding.next.setX(intEvaluator.evaluate(slideOffset, nextStartX, nextEndX)as Float)
        binding.icPlayList?.setX(intEvaluator.evaluate(slideOffset, playqueueStartX, playqueueEndX)as Float)
        binding.mark?.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1))
        binding.previous.setAlpha(floatEvaluator.evaluate(slideOffset, 0, 1))
        binding.iconContainer.setY(
            intEvaluator.evaluate(
                slideOffset,
                iconContainerStartY,
                iconContainerEndY
            )as Float
        )
        val params1 =
            binding.summary?.getLayoutParams() as CoordinatorLayout.LayoutParams
        params1.height =
            intEvaluator.evaluate(slideOffset, dp2px(55f), dp2px(60f))
        binding.summary.setLayoutParams(params1)

        //animate lyric view
        binding.lyricView.setTranslationY(lyricLineStartTranslationY - (lyricLineStartTranslationY - lyricLineEndTranslationY) * slideOffset)
    }

    override fun onPanelStateChanged(
        panel: View, previousState: PanelState,
        newState: PanelState
    ) {
        if (previousState == PanelState.COLLAPSED) {
            if (binding.songProgressNormal?.getVisibility() !== View.INVISIBLE) {
                binding.songProgressNormal?.setVisibility(View.INVISIBLE)
            }
            if (binding.mark?.getVisibility() !== View.VISIBLE) {
                binding.mark?.setVisibility(View.VISIBLE)
            }
            if (binding.previous.getVisibility() !== View.VISIBLE) {
                binding.previous.setVisibility(View.VISIBLE)
            }
        } else if (previousState == PanelState.EXPANDED) {
            if (mStatus == Status.FULLSCREEN) {
                animateToNormal()
            }
        }
        if (newState == PanelState.EXPANDED) {
            mStatus = Status.EXPANDED
            toolbarSlideIn(panel.context)
            binding.mark?.setClickable(true)
            binding.previous.setClickable(true)
            binding.topContainer.setOnClickListener{
                if (mStatus == Status.EXPANDED) {
                    animateToFullscreen()
                } else if (mStatus == Status.FULLSCREEN) {
                    animateToNormal()
                } else {
                    slidingUpPanelLayout.panelState = PanelState.COLLAPSED
                }
            }

        } else if (newState == PanelState.COLLAPSED) {
            mStatus = Status.COLLAPSED
            if (binding.songProgressNormal?.getVisibility() !== View.VISIBLE) {
                binding.songProgressNormal?.setVisibility(View.VISIBLE)
            }
            if (binding.mark?.getVisibility() !== View.GONE) {
                binding.mark?.setVisibility(View.GONE)
            }
            if (binding.previous.getVisibility() !== View.GONE) {
                binding.previous.setVisibility(View.GONE)
            }
            binding.topContainer.setOnClickListener{
                if (slidingUpPanelLayout.isTouchEnabled) {
                    slidingUpPanelLayout.panelState = PanelState.EXPANDED
                }
            }
        } else if (newState == PanelState.DRAGGING) {
            if (binding.customToolbar?.getVisibility() !== View.INVISIBLE) {
                binding.customToolbar?.setVisibility(View.INVISIBLE)
            }
            if (binding.lyricView.getVisibility() !== View.VISIBLE) {
                binding.lyricView.setVisibility(View.VISIBLE)
            }
        }
    }

    private fun caculateTitleAndArtist() {
        val titleBounds = Rect()
        binding.title?.getPaint()?.getTextBounds(
            binding.title.getText().toString(), 0,
            binding.title.text.length, titleBounds
        )
        val titleWidth = titleBounds.width()
        val artistBounds = Rect()
        binding.artist?.paint?.getTextBounds(
            binding.artist.getText().toString(), 0,
            binding.artist.getText().length, artistBounds
        )
        val artistWidth = artistBounds.width()
        titleEndTranslationX = screenWidth / 2 - titleWidth / 2 - dp2px(67f)as Float
        artistEndTranslationX = screenWidth / 2 - artistWidth / 2 - dp2px(67f) as Float
        artistNormalEndTranslationY = dp2px(12f) as Float
        artistFullEndTranslationY = 0f
        contentNormalEndTranslationY = screenWidth + dp2px(32f) as Float
        contentFullEndTranslationY = dp2px(32f) as Float
        if (mStatus == Status.COLLAPSED) {
            binding.title?.setTranslationX(0f)
            binding.artist?.setTranslationX(0f)
        } else {
            binding.title?.setTranslationX(titleEndTranslationX)
            binding.artist?.setTranslationX(artistEndTranslationX)
        }
    }

    private fun caculateIcons() {
        markStartX = binding.mark!!.getLeft()
        previousStartX = binding.previous.getLeft()
        playPauseStartX = binding.playPause.getLeft()
        nextStartX = binding.next.getLeft()
        playqueueStartX = binding.icPlayList!!.getLeft()
        val size: Int = dp2px(36f)
        val gap = (screenWidth - 5 * size) / 6
        playPauseEndX = screenWidth / 2 - size / 2
        previousEndX = playPauseEndX - gap - size
        markEndX = previousEndX - gap - size
        nextEndX = playPauseEndX + gap + size
        playqueueEndX = nextEndX + gap + size
        iconContainerStartY = binding.iconContainer.getTop()
        iconContainerEndY =
            screenHeight - 3 * binding.iconContainer.getHeight() - binding.seekBottom.getHeight()
    }

    private fun caculateLyricView() {
        val lyricFullMarginTop: Int = (binding.customToolbar!!.getTop()
                + binding.customToolbar.getHeight() + dp2px(32f))
        val lyricFullMarginBottom: Int = (binding.iconContainer.getBottom()
                + binding.iconContainer.getHeight() + dp2px(32f))
        lyricLineHeight = dp2px(32f) as Float
        lyricFullHeight = screenHeight - lyricFullMarginTop - lyricFullMarginBottom as Float
        lyricLineStartTranslationY = screenHeight as Float
        val gapBetweenArtistAndLyric: Int =
            (iconContainerEndY - contentNormalEndTranslationY - binding.summary!!.getHeight() )as Int
        lyricLineEndTranslationY =
            iconContainerEndY - gapBetweenArtistAndLyric / 2 - lyricLineHeight / 2
        lyricFullTranslationY = (binding.customToolbar.getTop()
                + binding.customToolbar.getHeight() + dp2px(32f)) as Float
    }

    private fun toolbarSlideIn(context: Context) {
        binding.customToolbar!!.post({
            val animation =
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.toolbar_slide_in
                )
            animation.setAnimationListener(object :
                Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    binding.customToolbar.setVisibility(View.VISIBLE)
                }

                override fun onAnimationEnd(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
            })
            binding.customToolbar.startAnimation(animation)
        })
    }

    private fun animateToFullscreen() {
        //album art fullscreen
        albumImageDrawable = binding.albumArt.getDrawable()

        //animate title and artist
        val contentAnimation: Animation =
            object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    binding.summary!!.setTranslationY(contentNormalEndTranslationY - (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime)
                    binding.artist!!.setTranslationY(artistNormalEndTranslationY - (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime)
                }
            }
        contentAnimation.duration = 150
        binding.artist!!.startAnimation(contentAnimation)

        //animate lyric
        val lyricAnimation: Animation =
            object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    val lyricLayout =
                        binding.lyricView.getLayoutParams() as CoordinatorLayout.LayoutParams
                    lyricLayout.height =
                        (lyricLineHeight + (lyricFullHeight - lyricLineHeight) * interpolatedTime).toInt()
                    binding.lyricView.setLayoutParams(lyricLayout)
                    binding.lyricView.setTranslationY(lyricLineEndTranslationY - (lyricLineEndTranslationY - lyricFullTranslationY) * interpolatedTime)
                }
            }
        lyricAnimation.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.lyricView.setHighLightTextColor(Color.BLACK)
                binding.lyricView.setPlayable(true)
                binding.lyricView.setTouchable(true)
                binding.lyricView.setOnClickListener{ animateToNormal()}
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        lyricAnimation.duration = 150
        binding.lyricView.startAnimation(lyricAnimation)
        mStatus = Status.FULLSCREEN
    }

    private fun animateToNormal() {
        //album art
        val imageLayout =
            binding.albumArt.getLayoutParams() as CoordinatorLayout.LayoutParams
        imageLayout.height = screenWidth
        imageLayout.width = screenWidth
        binding.albumArt.setLayoutParams(imageLayout)

        //animate title and artist
        val contentAnimation: Animation =
            object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    binding.summary!!.setTranslationY(contentFullEndTranslationY + (contentNormalEndTranslationY - contentFullEndTranslationY) * interpolatedTime)
                    binding.artist!!.setTranslationY(artistFullEndTranslationY + (artistNormalEndTranslationY - artistFullEndTranslationY) * interpolatedTime)
                }
            }
        contentAnimation.duration = 300
        binding.artist!!.startAnimation(contentAnimation)

        //adjust lyricview
        val lyricAnimation: Animation =
            object : Animation() {
                override fun applyTransformation(
                    interpolatedTime: Float,
                    t: Transformation
                ) {
                    val layoutParams =
                        binding.lyricView.getLayoutParams() as CoordinatorLayout.LayoutParams
                    layoutParams.height =
                        (lyricFullHeight - (lyricFullHeight - lyricLineHeight) * interpolatedTime).toInt()
                    binding.lyricView.setLayoutParams(layoutParams)
                    binding.lyricView.setTranslationY(lyricFullTranslationY + (lyricLineEndTranslationY - lyricFullTranslationY) * interpolatedTime)
                }
            }
        lyricAnimation.setAnimationListener(object :
            Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.lyricView.setPlayable(false)
                binding.lyricView.setHighLightTextColor(binding.lyricView.defaultColor)
                binding.lyricView.setTouchable(false)
                binding.lyricView.setClickable(false)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        lyricAnimation.duration = 300
        binding.lyricView.setPlayable(false)
        binding.lyricView.startAnimation(lyricAnimation)
        mStatus = Status.EXPANDED
    }

    init {
        screenWidth = ScreenUtils.getScreenWidth()
        screenHeight = ScreenUtils.getScreenHeight()
        playPauseDrawableColor = Color.BLACK
        nowPlayingCardColor = Color.WHITE
        caculateTitleAndArtist()
        caculateIcons()
        caculateLyricView()
        binding.playPause.drawableColor = playPauseDrawableColor
    }
}