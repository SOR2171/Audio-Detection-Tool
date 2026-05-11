package com.github.sor2171.audiodetectiontool

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform