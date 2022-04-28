package com.thk.cleantodo.util

import android.util.Log

inline fun <reified T> T.logd(message: String) = Log.d(T::class.java.simpleName, message)