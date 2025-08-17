package com.cequea.cryptobuddy.utils

import android.annotation.SuppressLint
import android.content.Context
import com.cequea.cryptobuddy.R


object DrawableResolver {
    private val cache = mutableMapOf<String, Int>()

    @SuppressLint("DiscouragedApi")
    fun getDrawableId(context: Context, name: String): Int {
        return cache.getOrPut(name) {
            val id = context.resources.getIdentifier(name, "drawable", context.packageName)
            if (id != 0) id else R.drawable.bitcoin
        }
    }
}