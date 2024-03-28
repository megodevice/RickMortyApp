package com.iliazusik.rickmortyapp.ui.character

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ilia_zusik.rickmortyapp.R
import com.ilia_zusik.rickmortyapp.databinding.ItemEpisodeBinding
import com.iliazusik.rickmortyapp.data.Episode


class EpisodesRecyclerViewAdapter :
    ListAdapter<Episode, EpisodesRecyclerViewAdapter.EpisodeViewHolder>(DIFF_UTIL_CALLBACK) {

    private val linearInterpolator = LinearInterpolator()

    private companion object {
        val DIFF_UTIL_CALLBACK: DiffUtil.ItemCallback<Episode> =
            object : DiffUtil.ItemCallback<Episode>() {
                override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                    return oldItem.url == newItem.url
                }

                override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                    return oldItem == newItem
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) :
        ViewHolder(binding.root) {

        private var isClicked: Boolean = false
        private var ce: Pair<String, String> = Pair(String(), String())

        fun onBind(episode: Episode) {
            ce = getSeasonEpisodeNumbers(episode.episode)
            binding.apply {
                (root.context.getString(R.string.season) + ' ' + ce.first).apply {
                    tvSeasonNumber.text = this
                }
                (root.context.getString(R.string.episode) + ' ' + ce.second).apply {
                    tvEpisodeNumber.text = this
                }

                tvEpisodeName.text = episode.name
                tvAirDate.text = episode.airDate
            }
        }

        private fun getSeasonEpisodeNumbers(season: String) : Pair<String, String> {
            val s = season.lastIndexOf('S')
            val e = season.lastIndexOf('E')
            var sNumber = season.substring(s + 1, e)
            var eNumber = season.substring(e + 1, season.length)
            if (sNumber.length > 1 && sNumber.first() == '0')
                sNumber = sNumber.removeRange(0,1)
            if (eNumber.length > 1 && eNumber.first() == '0')
                eNumber = eNumber.removeRange(0,1)
            return Pair(sNumber, eNumber)
        }

        init {
            val rotateTo180 = RotateAnimation(
                0f,
                -180f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            val rotateFrom180 = RotateAnimation(
                -180f,
                0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            rotateTo180.fillAfter = true
            rotateTo180.setDuration(300)
            rotateTo180.interpolator = linearInterpolator
            rotateFrom180.fillAfter = true
            rotateFrom180.setDuration(300)
            rotateFrom180.interpolator = linearInterpolator

            binding.root.setOnClickListener {
                binding.apply {
                    if (isClicked) {
                        tvAirDate.visibility = View.GONE
                        tvAirDateLabel.visibility = View.GONE
                        tvEpisodeName.visibility = View.GONE
                        tvEpisodeNameLabel.visibility = View.GONE
                        ivArrow.startAnimation(rotateFrom180)
                    } else {
                        tvAirDate.visibility = View.VISIBLE
                        tvAirDateLabel.visibility = View.VISIBLE
                        tvEpisodeName.visibility = View.VISIBLE
                        tvEpisodeNameLabel.visibility = View.VISIBLE
                        ivArrow.startAnimation(rotateTo180)
                    }
                    isClicked = !isClicked
                }

            }
        }
    }
}