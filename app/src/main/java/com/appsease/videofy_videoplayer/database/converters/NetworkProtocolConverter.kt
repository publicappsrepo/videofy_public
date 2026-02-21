package com.appsease.videofy_videoplayer.database.converters

import androidx.room.TypeConverter
import com.appsease.videofy_videoplayer.domain.network.NetworkProtocol

/**
 * Type converter for NetworkProtocol enum
 */
class NetworkProtocolConverter {
  @TypeConverter
  fun fromNetworkProtocol(protocol: NetworkProtocol): String = protocol.name

  @TypeConverter
  fun toNetworkProtocol(value: String): NetworkProtocol = NetworkProtocol.valueOf(value)
}
