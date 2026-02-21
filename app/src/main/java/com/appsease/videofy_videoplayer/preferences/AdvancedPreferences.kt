package com.appsease.videofy_videoplayer.preferences

import com.appsease.videofy_videoplayer.BuildConfig
import com.appsease.videofy_videoplayer.preferences.preference.PreferenceStore

class AdvancedPreferences(
  preferenceStore: PreferenceStore,
) {
  val videofyConfStorageUri = preferenceStore.getString("videofy_conf_storage_location_uri")
  val videofyConf = preferenceStore.getString("videofy.conf")
  val inputConf = preferenceStore.getString("input.conf")

  val verboseLogging = preferenceStore.getBoolean("verbose_logging", BuildConfig.BUILD_TYPE != "release")

  val enabledStatisticsPage = preferenceStore.getInt("enabled_stats_page", 0)

  val enableRecentlyPlayed = preferenceStore.getBoolean("enable_recently_played", true)

  val enableLuaScripts = preferenceStore.getBoolean("enable_lua_scripts", false)
  val selectedLuaScripts = preferenceStore.getStringSet("selected_lua_scripts", emptySet())

}
