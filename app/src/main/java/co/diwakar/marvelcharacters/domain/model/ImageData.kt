package co.diwakar.marvelcharacters.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageData(
    val path: String?,
    val extension: String?
) : Parcelable {
    /**
     * Uses to get complete path (with extension) with https for
     * secure communication
     * */
    fun getCompletePath(): String? {
        return if (path != null && extension != null) {
            "$path.$extension".replace("http", "https")
        } else {
            null
        }
    }
}