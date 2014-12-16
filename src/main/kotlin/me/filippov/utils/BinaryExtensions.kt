package me.filippov.utils

fun Int.toBytes(): Quad<Byte, Byte, Byte, Byte> {
    return Quad((this.toInt() and 255).toByte(),
        ((this.toInt() shr 8) and 255).toByte(),
        ((this.toInt() shr 16) and 255).toByte(),
        ((this.toInt() shr 24) and 255).toByte())
}

fun Quad<Byte, Byte, Byte, Byte>.toInt(): Int {
    return (this.first.toInt() and 255) +
        ((this.second.toInt() and 255) shl 8) +
        ((this.third.toInt() and 255) shl 16) +
        ((this.fourth.toInt() and 255) shl 24)
}

fun Long.toBytes(): Eight<Byte, Byte, Byte, Byte, Byte, Byte, Byte, Byte> {
    return Eight(
        (this.toInt() and 255).toByte(),
        ((this.toLong() shr 8) and 255).toByte(),
        ((this.toLong() shr 16) and 255).toByte(),
        ((this.toLong() shr 24) and 255).toByte(),
        ((this.toLong() shr 32) and 255).toByte(),
        ((this.toLong() shr 40) and 255).toByte(),
        ((this.toLong() shr 48) and 255).toByte(),
        ((this.toLong() shr 56) and 255).toByte())
}

fun Eight<Byte, Byte, Byte, Byte, Byte, Byte, Byte, Byte>.toLong(): Long {
    return (this.first.toLong() and 255) +
        ((this.second.toLong() and 255) shl 8) +
        ((this.third.toLong() and 255) shl 16) +
        ((this.fourth.toLong() and 255) shl 24) +
        ((this.fifth.toLong() and 255) shl 32) +
        ((this.sixth.toLong() and 255) shl 40) +
        ((this.seventh.toLong() and 255) shl 48) +
        ((this.eighth.toLong() and 255) shl 56);
}