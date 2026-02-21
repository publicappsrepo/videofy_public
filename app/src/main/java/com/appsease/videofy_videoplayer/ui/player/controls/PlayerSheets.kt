package com.appsease.videofy_videoplayer.ui.player.controls

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.appsease.videofy_videoplayer.preferences.preference.collectAsState
import com.appsease.videofy_videoplayer.ui.player.Decoder
import com.appsease.videofy_videoplayer.ui.player.Panels
import com.appsease.videofy_videoplayer.ui.player.Sheets
import com.appsease.videofy_videoplayer.ui.player.TrackNode
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.AspectRatioSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.AudioTracksSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.ChaptersSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.DecodersSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.FrameNavigationSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.MoreSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.PlaybackSpeedSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.PlaylistSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.SubtitlesSheet
import com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.VideoZoomSheet
import dev.vivvvek.seeker.Segment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.compose.koinInject
import androidx.compose.runtime.collectAsState as composeCollectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PlayerSheets(
  sheetShown: Sheets,
  viewModel: com.appsease.videofy_videoplayer.ui.player.PlayerViewModel,
  // subtitles sheet
  subtitles: ImmutableList<TrackNode>,
  onAddSubtitle: (Uri) -> Unit,
  onToggleSubtitle: (Int) -> Unit,
  isSubtitleSelected: (Int) -> Boolean,
  onRemoveSubtitle: (Int) -> Unit,
  // audio sheet
  audioTracks: ImmutableList<TrackNode>,
  onAddAudio: (Uri) -> Unit,
  onSelectAudio: (TrackNode) -> Unit,
  // chapters sheet
  chapter: Segment?,
  chapters: ImmutableList<Segment>,
  onSeekToChapter: (Int) -> Unit,
  // Decoders sheet
  decoder: Decoder,
  onUpdateDecoder: (Decoder) -> Unit,
  // Speed sheet
  speed: Float,
  speedPresets: List<Float>,
  onSpeedChange: (Float) -> Unit,
  onAddSpeedPreset: (Float) -> Unit,
  onRemoveSpeedPreset: (Float) -> Unit,
  onResetSpeedPresets: () -> Unit,
  onMakeDefaultSpeed: (Float) -> Unit,
  onResetDefaultSpeed: () -> Unit,
  // More sheet
  sleepTimerTimeRemaining: Int,
  onStartSleepTimer: (Int) -> Unit,
  onOpenPanel: (Panels) -> Unit,
  onShowSheet: (Sheets) -> Unit,
  onDismissRequest: () -> Unit,
) {
  when (sheetShown) {
    Sheets.None -> {}
    Sheets.SubtitleTracks -> {
      val subtitlesPicker =
        rememberLauncherForActivityResult(
          ActivityResultContracts.OpenDocument(),
        ) {
          if (it == null) return@rememberLauncherForActivityResult
          onAddSubtitle(it)
        }

      val subtitlesPreferences = koinInject<com.appsease.videofy_videoplayer.preferences.SubtitlesPreferences>()
      val savedPickerPath = subtitlesPreferences.pickerPath.get()

      var showFilePicker by remember { mutableStateOf(false) }

      if (showFilePicker) {
          com.appsease.videofy_videoplayer.ui.browser.dialogs.FilePickerDialog(
              isOpen = true,
              currentPath = savedPickerPath ?: android.os.Environment.getExternalStorageDirectory().absolutePath,
              onDismiss = { showFilePicker = false },
              onFileSelected = { path ->
                  showFilePicker = false
                  val parent = java.io.File(path).parent
                  if (parent != null) {
                      subtitlesPreferences.pickerPath.set(parent)
                  }
                   onAddSubtitle(Uri.parse("file://$path"))
              },
              onSystemPickerRequest = {
                  showFilePicker = false
                  subtitlesPicker.launch(
                    arrayOf(
                      "text/plain",
                      "text/srt",
                      "text/vtt",
                      "application/x-subrip",
                      "application/x-subtitle",
                      "text/x-ssa",
                      "*/*",
                    ),
                  )
              }
          )
      }

      SubtitlesSheet(
        tracks = subtitles.toImmutableList(),
        onToggleSubtitle = onToggleSubtitle,
        isSubtitleSelected = isSubtitleSelected,
        onAddSubtitle = {
             showFilePicker = true
        },
        onRemoveSubtitle = onRemoveSubtitle,
        onOpenSubtitleSettings = { onOpenPanel(Panels.SubtitleSettings) },
        onOpenSubtitleDelay = { onOpenPanel(Panels.SubtitleDelay) },
        onDismissRequest = onDismissRequest,
      )
    }

    Sheets.AudioTracks -> {
      val audioPicker =
        rememberLauncherForActivityResult(
          ActivityResultContracts.OpenDocument(),
        ) {
          if (it == null) return@rememberLauncherForActivityResult
          onAddAudio(it)
        }
      AudioTracksSheet(
        tracks = audioTracks,
        onSelect = onSelectAudio,
        onAddAudioTrack = { audioPicker.launch(arrayOf("*/*")) },
        onOpenDelayPanel = { onOpenPanel(Panels.AudioDelay) },
        onDismissRequest,
      )
    }

    Sheets.Chapters -> {
      if (chapter == null) return
      ChaptersSheet(
        chapters,
        currentChapter = chapter,
        onClick = { onSeekToChapter(chapters.indexOf(it)) },
        onDismissRequest,
      )
    }

    Sheets.Decoders -> {
      DecodersSheet(
        selectedDecoder = decoder,
        onSelect = onUpdateDecoder,
        onDismissRequest,
      )
    }

    Sheets.More -> {
      MoreSheet(
        remainingTime = sleepTimerTimeRemaining,
        onStartTimer = onStartSleepTimer,
        onDismissRequest = onDismissRequest,
        onEnterFiltersPanel = { onOpenPanel(Panels.VideoFilters) },
      )
    }

    Sheets.PlaybackSpeed -> {
      PlaybackSpeedSheet(
        speed,
        onSpeedChange = onSpeedChange,
        speedPresets = speedPresets,
        onAddSpeedPreset = onAddSpeedPreset,
        onRemoveSpeedPreset = onRemoveSpeedPreset,
        onResetPresets = onResetSpeedPresets,
        onMakeDefault = onMakeDefaultSpeed,
        onResetDefault = onResetDefaultSpeed,
        onDismissRequest = onDismissRequest,
      )
    }

    Sheets.VideoZoom -> {
      val videoZoom by viewModel.videoZoom.composeCollectAsState()
      VideoZoomSheet(
        videoZoom = videoZoom,
        onSetVideoZoom = viewModel::setVideoZoom,
        onDismissRequest = onDismissRequest,
      )
    }

    Sheets.AspectRatios -> {
      val playerPreferences = koinInject<com.appsease.videofy_videoplayer.preferences.PlayerPreferences>()
      val customRatiosSet by playerPreferences.customAspectRatios.collectAsState()
      val currentRatio by playerPreferences.currentAspectRatio.collectAsState()
      val customRatios =
        customRatiosSet.mapNotNull { str ->
          val parts = str.split("|")
          if (parts.size == 2) {
            com.appsease.videofy_videoplayer.ui.player.controls.components.sheets.AspectRatio(
              label = parts[0],
              ratio = parts[1].toDoubleOrNull() ?: return@mapNotNull null,
              isCustom = true,
            )
          } else {
            null
          }
        }

      AspectRatioSheet(
        currentRatio = currentRatio.toDouble(),
        customRatios = customRatios,
        onSelectRatio = { ratio ->
          viewModel.setCustomAspectRatio(ratio)
        },
        onAddCustomRatio = { label, ratio ->
          playerPreferences.customAspectRatios.set(customRatiosSet + "$label|$ratio")
          viewModel.setCustomAspectRatio(ratio)
        },
        onDeleteCustomRatio = { ratio ->
          val toRemove = "${ratio.label}|${ratio.ratio}"
          playerPreferences.customAspectRatios.set(customRatiosSet - toRemove)
          // If the deleted ratio is currently active, reset to default
          if (kotlin.math.abs(currentRatio - ratio.ratio) < 0.01) {
            viewModel.setCustomAspectRatio(-1.0)
          }
        },
        onDismissRequest = onDismissRequest,
      )
    }

    Sheets.FrameNavigation -> {
      val currentFrame by viewModel.currentFrame.composeCollectAsState()
      val totalFrames by viewModel.totalFrames.composeCollectAsState()
      FrameNavigationSheet(
        currentFrame = currentFrame,
        totalFrames = totalFrames,
        onUpdateFrameInfo = viewModel::updateFrameInfo,
        onPause = viewModel::pause,
        onUnpause = viewModel::unpause,
        onPauseUnpause = viewModel::pauseUnpause,
        onSeekTo = { position, _ -> viewModel.seekTo(position) },
        onDismissRequest = onDismissRequest,
      )
    }


    Sheets.Playlist -> {
      // Refresh playlist items when sheet is shown
      LaunchedEffect(Unit) {
        viewModel.refreshPlaylistItems()
      }

      // Observe playlist updates
      val playlist by viewModel.playlistItems.collectAsState()
      val playerPreferences = koinInject<com.appsease.videofy_videoplayer.preferences.PlayerPreferences>()

      if (playlist.isNotEmpty()) {
        val playlistImmutable = playlist.toImmutableList()
        val totalCount = viewModel.getPlaylistTotalCount()
        val isM3U = viewModel.isPlaylistM3U()
        PlaylistSheet(
          playlist = playlistImmutable,
          onDismissRequest = onDismissRequest,
          onItemClick = { item ->
            viewModel.playPlaylistItem(item.index)
          },
          totalCount = totalCount,
          isM3UPlaylist = isM3U,
          playerPreferences = playerPreferences,
        )
      }
    }
  }
}
