package org.alba.hortus

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform