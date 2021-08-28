package com.learn.lavsam.alfatmdbviewer.view

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBarAction (text: String, actionText: String, action: (View) -> Unit, length: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, text, length)
        .setAction(actionText, action)
        .show()
}

fun View.showSnackBar (text: String, length: Int = Snackbar.LENGTH_INDEFINITE) {
    Snackbar.make(this, text, length)
        .show()
}

fun View.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_LONG).show()
}
