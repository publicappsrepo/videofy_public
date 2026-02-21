package com.appsease.videofy_videoplayer.ui.browser.recentlyplayed

import com.appsease.videofy_videoplayer.database.entities.PlaylistEntity
import com.appsease.videofy_videoplayer.domain.media.model.Video

sealed class RecentlyPlayedItem {
  abstract val timestamp: Long

  data class VideoItem(
    val video: Video,
    override val timestamp: Long,
  ) : RecentlyPlayedItem()

  data class PlaylistItem(
    val playlist: PlaylistEntity,
    val videoCount: Int,
    val mostRecentVideoPath: String,
    override val timestamp: Long,
  ) : RecentlyPlayedItem()
}
