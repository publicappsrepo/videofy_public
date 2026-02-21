package com.appsease.videofy_videoplayer.di

import com.appsease.videofy_videoplayer.domain.anime4k.Anime4KManager
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val domainModule = module {
    single { Anime4KManager(androidContext()) }
}
