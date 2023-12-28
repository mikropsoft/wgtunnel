package com.zaneschepke.wireguardautotunnel.util
import com.zaneschepke.wireguardautotunnel.R

enum class Error(val code : Int,) {
    NONE(0),
    SSID_EXISTS(2),
    ROOT_DENIED(3),
    FILE_EXTENSION(4),
    NO_FILE_EXPLORER(5),
    INVALID_QR(6),
    GENERAL(1);
    fun getMessage() : Int {
        return when(this.code) {
            1 -> R.string.unknown_error
            2 -> R.string.error_ssid_exists
            3 -> R.string.error_root_denied
            4 -> R.string.error_file_extension
            5 -> R.string.error_no_file_explorer
            6 -> R.string.error_invalid_code
            else -> R.string.unknown_error
        }
    }
}