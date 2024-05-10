package org.wit.gym.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class GymModel(
    var id: String? = "",
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var contactNumber: String = "",
    var members: Int = 0,
    var activity: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable


@Parcelize
data class GymModelStorage(
    var id: String? = "",
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var contactNumber: String = "",
    var members: Int = 0,
    var activity: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Parcelize
data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
) : Parcelable

@Exclude
fun GymModel.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "image" to image.toString(),
        "contactNumber" to contactNumber,
        "members" to members,
        "activity" to activity,
        "lat" to lat,
        "lng" to lng,
        "zoom" to zoom
    )
}