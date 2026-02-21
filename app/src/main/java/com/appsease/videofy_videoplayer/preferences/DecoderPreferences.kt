package com.appsease.videofy_videoplayer.preferences

import com.appsease.videofy_videoplayer.preferences.preference.PreferenceStore
import com.appsease.videofy_videoplayer.preferences.preference.getEnum
import com.appsease.videofy_videoplayer.ui.player.Debanding

class DecoderPreferences(
  preferenceStore: PreferenceStore,
) {
  val tryHWDecoding = preferenceStore.getBoolean("try_hw_dec", true)
  val gpuNext = preferenceStore.getBoolean("gpu_next")
  val useYUV420P = preferenceStore.getBoolean("use_yuv420p", false)

  val debanding = preferenceStore.getEnum("debanding", Debanding.None)
  val debandIterations = preferenceStore.getInt("deband_iterations", 1)
  val debandThreshold = preferenceStore.getInt("deband_threshold", 48)
  val debandRange = preferenceStore.getInt("deband_range", 16)
  val debandGrain = preferenceStore.getInt("deband_grain", 32)

  val brightnessFilter = preferenceStore.getInt("filter_brightness")
  val saturationFilter = preferenceStore.getInt("filter_saturation")
  val gammaFilter = preferenceStore.getInt("filter_gamma")
  val contrastFilter = preferenceStore.getInt("filter_contrast")
  val hueFilter = preferenceStore.getInt("filter_hue")
  val sharpnessFilter = preferenceStore.getInt("filter_sharpness")

  // Anime4K Preferences
  val enableAnime4K = preferenceStore.getBoolean("enable_anime4k", false)
  val anime4kMode = preferenceStore.getString("anime4k_mode", "OFF")
  val anime4kQuality = preferenceStore.getString("anime4k_quality", "FAST")
}
