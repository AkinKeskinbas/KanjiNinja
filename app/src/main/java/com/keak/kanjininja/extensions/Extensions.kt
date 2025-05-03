package com.keak.kanjininja.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import timber.log.Timber
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

fun String.takeBeforeParensAndRemoveSpaces(): String {
    Timber.tag("Question").d("raw answer --> $this")

    val normalized = this
        .replace('（', '(')  // Japonca '(' -> ASCII '('
        .replace('）', ')')  // Japonca ')' -> ASCII ')'

    val question = normalized
        .substringBefore("(")                 // Artık her şey ASCII parantez
        .replace("\\s".toRegex(), "")         // Boşlukları sil

    Timber.tag("Question").d("RealAnswer --> $question")
    return question
}

fun String.extractKanaInParentheses(): String {
    return this
        .replace("（", "(")
        .replace("）", ")")
        .substringAfter("(", "")
        .substringBefore(")", "")
        .replace(" ", "")
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

