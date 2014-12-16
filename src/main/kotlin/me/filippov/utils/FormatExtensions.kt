package me.filippov.utils

fun Int.format(format: String): String {
    return java.lang.String.format(format, this)
}