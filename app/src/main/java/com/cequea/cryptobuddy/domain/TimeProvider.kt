package com.cequea.cryptobuddy.domain

interface TimeProvider {
    fun nowMillis(): Long
}

class SystemTimeProvider : TimeProvider {
    override fun nowMillis(): Long = System.currentTimeMillis()
}