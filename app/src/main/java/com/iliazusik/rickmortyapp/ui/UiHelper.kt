package com.iliazusik.rickmortyapp.ui

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.ilia_zusik.rickmortyapp.BuildConfig
import com.ilia_zusik.rickmortyapp.R

object UiHelper {

    fun getSeasonEpisodeNumbers(season: String): Pair<String, String> {
        val s = season.lastIndexOf('S')
        val e = season.lastIndexOf('E')
        var sNumber = season.substring(s + 1, e)
        var eNumber = season.substring(e + 1, season.length)
        if (sNumber.length > 1 && sNumber.first() == '0') sNumber = sNumber.removeRange(0, 1)
        if (eNumber.length > 1 && eNumber.first() == '0') eNumber = eNumber.removeRange(0, 1)
        return Pair(sNumber, eNumber)
    }

    fun changeVisibility(isVisible: Boolean, vararg views: View) {
        for (view in views) {
            view.isVisible = isVisible
        }
    }

    fun setStatusDot(status: String, tvStatus: TextView) {
        when (status) {
            BuildConfig.ALIVE -> {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_dot_alive, 0, 0, 0
                )
            }

            BuildConfig.DEATH -> {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_dot_death, 0, 0, 0
                )
            }

            else -> {
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_dot_unknown, 0, 0, 0
                )
            }
        }
    }

}