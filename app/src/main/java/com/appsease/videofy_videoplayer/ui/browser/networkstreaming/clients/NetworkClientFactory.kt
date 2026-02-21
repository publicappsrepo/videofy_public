package com.appsease.videofy_videoplayer.ui.browser.networkstreaming.clients

import com.appsease.videofy_videoplayer.domain.network.NetworkConnection
import com.appsease.videofy_videoplayer.domain.network.NetworkProtocol

object NetworkClientFactory {
  fun createClient(connection: NetworkConnection): NetworkClient =
    when (connection.protocol) {
      NetworkProtocol.SMB -> SmbClient(connection)
      NetworkProtocol.FTP -> FtpClient(connection)
      NetworkProtocol.WEBDAV -> WebDavClient(connection)
    }
}
