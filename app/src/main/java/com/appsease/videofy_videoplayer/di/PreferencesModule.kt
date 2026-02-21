package com.appsease.videofy_videoplayer.di

import com.appsease.videofy_videoplayer.preferences.AdvancedPreferences
import com.appsease.videofy_videoplayer.preferences.AppearancePreferences
import com.appsease.videofy_videoplayer.preferences.AudioPreferences
import com.appsease.videofy_videoplayer.preferences.BrowserPreferences
import com.appsease.videofy_videoplayer.preferences.DecoderPreferences
import com.appsease.videofy_videoplayer.preferences.FoldersPreferences
import com.appsease.videofy_videoplayer.preferences.GesturePreferences
import com.appsease.videofy_videoplayer.preferences.PlayerPreferences
import com.appsease.videofy_videoplayer.preferences.SettingsManager
import com.appsease.videofy_videoplayer.preferences.SubtitlesPreferences
import com.appsease.videofy_videoplayer.preferences.preference.AndroidPreferenceStore
import com.appsease.videofy_videoplayer.preferences.preference.PreferenceStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val PreferencesModule =
  module {
    single { AndroidPreferenceStore(androidContext()) }.bind(PreferenceStore::class)

    single { AppearancePreferences(get()) }
    singleOf(::PlayerPreferences)
    singleOf(::GesturePreferences)
    singleOf(::DecoderPreferences)
    singleOf(::SubtitlesPreferences)
    singleOf(::AudioPreferences)
    singleOf(::AdvancedPreferences)
    singleOf(::BrowserPreferences)
    singleOf(::FoldersPreferences)
    singleOf(::SettingsManager)
  }
