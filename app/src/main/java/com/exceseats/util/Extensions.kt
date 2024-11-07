package com.exceseats.util

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import java.text.SimpleDateFormat
import java.util.*

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun GeoPoint.toLatLng(): LatLng = LatLng(latitude, longitude)

fun LatLng.toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)

fun ChatMessage.formatTime(): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
