package com.yourcompany.excesseats.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

// Context Extensions
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

// Fragment Extensions
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.showToast(message, duration)
}

// View Extensions
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionListener: View.OnClickListener? = null
) {
    val snackbar = Snackbar.make(this, message, duration)
    if (action != null && actionListener != null) {
        snackbar.setAction(action, actionListener)
    }
    snackbar.show()
}

// ImageView Extensions
fun ImageView.loadImage(url: String?) {
    if (url != null) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}

fun ImageView.loadImage(uri: Uri?) {
    if (uri != null) {
        Glide.with(context)
            .load(uri)
            .centerCrop()
            .into(this)
    }
}

// String Extensions
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return length >= Constants.MIN_PASSWORD_LENGTH
}

// Date Extensions
fun Date.formatToString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}

fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    return Date(this).formatToString(pattern)
}

// Bitmap Extensions
fun Bitmap.toByteArray(quality: Int = Constants.IMAGE_COMPRESSION_QUALITY): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, size)
}

// List Extensions
fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

// Null Safety Extensions
fun <T> T?.orDefault(default: T): T {
    return this ?: default
}

// Distance Extensions
fun Double.formatDistance(): String {
    return when {
        this < 1.0 -> String.format("%.0f m", this * 1000)
        this < 10.0 -> String.format("%.1f km", this)
        else -> String.format("%.0f km", this)
    }
}

// Time Extensions
fun Long.getTimeAgo(): String {
    val now = System.currentTimeMillis()
    val diff = now - this

    return when {
        diff < Constants.HOUR_IN_MILLIS -> "Just now"
        diff < Constants.DAY_IN_MILLIS -> "${diff / Constants.HOUR_IN_MILLIS}h ago"
        diff < Constants.WEEK_IN_MILLIS -> "${diff / Constants.DAY_IN_MILLIS}d ago"
        else -> Date(this).formatToString("MMM dd, yyyy")
    }
}

// Collection Extensions
fun <T> List<T>.takeIfNotEmpty(): List<T>? {
    return if (isNotEmpty()) this else null
}

// Result Extensions
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (isSuccess) action(getOrNull()!!)
    return this
}

inline fun <T> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    if (isFailure) action(exceptionOrNull()!!)
    return this
}
