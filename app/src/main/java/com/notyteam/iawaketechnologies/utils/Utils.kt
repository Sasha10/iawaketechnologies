package com.notyteam.iawaketechnologies.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.util.concurrent.TimeUnit


fun splitToComponentTimes(biggy: Int): String? {

    val minutes = (biggy % 3600) / 60;
    val seconds = biggy % 60;
    val songTime = String.format("%02d:%02d", minutes, seconds)
    return  songTime
}

fun ImageView.loadImage(
    url: String,
    progress: View? = null,
    onSuccess: (resource: Drawable) -> Unit = {},
    onError: () -> Unit = {}
) {
    if (url != "") {

        progress?.visible()
        val options = RequestOptions()
                .encodeQuality(70)
                .centerInside()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        Glide.with(this.context)
                .load(url)
                .apply(options)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress?.gone()
                        onError()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress?.gone()
                        onSuccess(resource)
                        return false
                    }
                })
                .into(this)
    }
}

fun milliSecondsToTimer(milliseconds: Long): String {
    var finalTimerString = ""
    var secondsString = ""

    val minutes = milliseconds % (1000 * 60 * 60) / (1000 * 60)
    val seconds = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()

    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }

    finalTimerString = "$finalTimerString$minutes:$secondsString"

    return finalTimerString;
}