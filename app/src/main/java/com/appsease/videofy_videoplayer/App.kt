//package com.appsease.videofy_videoplayer
package com.appsease.videofy_videoplayer

import android.app.Application
import com.appsease.videofy_videoplayer.database.repository.VideoMetadataCacheRepository
import com.appsease.videofy_videoplayer.di.DatabaseModule
import com.appsease.videofy_videoplayer.di.FileManagerModule
import com.appsease.videofy_videoplayer.di.PreferencesModule
import com.appsease.videofy_videoplayer.di.domainModule
import com.appsease.videofy_videoplayer.presentation.crash.CrashActivity
import com.appsease.videofy_videoplayer.presentation.crash.GlobalExceptionHandler
import `is`.xyz.mpv.FastThumbnails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
class App : Application() {
  private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
  private val metadataCache: VideoMetadataCacheRepository by inject()

  override fun onCreate() {
    super.onCreate()

    // Initialize Koin
    startKoin {
      androidContext(this@App)
      modules(
        PreferencesModule,
        DatabaseModule,
        FileManagerModule,
        domainModule,
      )
    }

    Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler(applicationContext, CrashActivity::class.java))

    FastThumbnails.initialize(this)

    // Perform cache maintenance on app startup (non-blocking)
    applicationScope.launch {
      runCatching {
        metadataCache.performMaintenance()
      }
    }
  }
}
