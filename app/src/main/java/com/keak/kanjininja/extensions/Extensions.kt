package com.keak.kanjininja.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


fun Boolean?.orFalse(): Boolean {
    return this ?: false
}

fun Int?.orZero(): Int {
    return this ?: 0
}
fun Double?.orZero(): Double {
    return this ?: 0.0
}
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}

tailrec fun Context.findActivity(): Activity = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> throw IllegalStateException()
}
const val EMPTY_STRING = ""

