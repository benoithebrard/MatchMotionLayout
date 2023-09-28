package com.example.matchmotionlayout.ui.utils

import android.content.res.Resources

internal val Number.dpToPx: Float get() = (toFloat() * Resources.getSystem().displayMetrics.density)