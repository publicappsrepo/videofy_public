package com.appsease.videofy_videoplayer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.appsease.videofy_videoplayer.database.converters.NetworkProtocolConverter
import com.appsease.videofy_videoplayer.database.dao.NetworkConnectionDao
import com.appsease.videofy_videoplayer.database.dao.PlaybackStateDao
import com.appsease.videofy_videoplayer.database.dao.PlaylistDao
import com.appsease.videofy_videoplayer.database.dao.RecentlyPlayedDao
import com.appsease.videofy_videoplayer.database.dao.VideoMetadataDao
import com.appsease.videofy_videoplayer.database.entities.PlaybackStateEntity
import com.appsease.videofy_videoplayer.database.entities.PlaylistEntity
import com.appsease.videofy_videoplayer.database.entities.PlaylistItemEntity
import com.appsease.videofy_videoplayer.database.entities.RecentlyPlayedEntity
import com.appsease.videofy_videoplayer.database.entities.VideoMetadataEntity
import com.appsease.videofy_videoplayer.domain.network.NetworkConnection

@Database(
  entities = [
    PlaybackStateEntity::class,
    RecentlyPlayedEntity::class,
    VideoMetadataEntity::class,
    NetworkConnection::class,
    PlaylistEntity::class,
    PlaylistItemEntity::class,
  ],
  version = 7,
  exportSchema = true,
)
@TypeConverters(NetworkProtocolConverter::class)
abstract class VideofyExDatabase : RoomDatabase() {
  abstract fun videoDataDao(): PlaybackStateDao

  abstract fun recentlyPlayedDao(): RecentlyPlayedDao

  abstract fun videoMetadataDao(): VideoMetadataDao

  abstract fun networkConnectionDao(): NetworkConnectionDao

  abstract fun playlistDao(): PlaylistDao
}
