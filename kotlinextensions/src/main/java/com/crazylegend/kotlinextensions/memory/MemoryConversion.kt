package com.crazylegend.kotlinextensions.memory


/**
 * Created by crazy on 2/24/19 to long live and prosper !
 */

fun getMemoryUsage(transform: (Long, Long, Long, Long, Int) -> String = { total, free, max, usage, percent -> "$usage / $max MB in use ($percent%)" }): Pair<Int, String> {
    val total = Runtime.getRuntime().totalMemory().bytes.toMegaBytes
    val free = Runtime.getRuntime().freeMemory().bytes.toMegaBytes
    val max = Runtime.getRuntime().maxMemory().bytes.toMegaBytes
    val usage = total - free
    val percent = ((usage.toDouble()/max) * 100).toInt()
    return percent to transform(total, free, max, usage, percent)
}

val Long.bits: BitValue get() = BitValue(this)
inline val Long.bit: BitValue get() = bits

inline val Int.bits: BitValue get() = toLong().bits
inline val Int.bit: BitValue get() = bits

inline val Long.bytes: BitValue get() = times(8).bits
inline val Long.byte: BitValue get() = bytes
inline val Long.B: BitValue get() = bytes

inline val Int.bytes: BitValue get() = toLong().bytes
inline val Int.byte: BitValue get() = bytes
inline val Int.B: BitValue get() = bytes

inline val Long.kibibytes: BitValue get() = times(1024).bytes
inline val Long.kibibyte: BitValue get() = kibibytes
inline val Long.KiB: BitValue get() = kibibytes

inline val Int.kibibytes: BitValue get() = toLong().kibibytes
inline val Int.kibibyte: BitValue get() = kibibytes
inline val Int.KiB: BitValue get() = kibibytes

inline val Long.kilobytes: BitValue get() = times(1000).bytes
inline val Long.kilobyte: BitValue get() = kilobytes
inline val Long.KB: BitValue get() = kilobytes

inline val Int.kilobytes: BitValue get() = toLong().kilobytes
inline val Int.kilobyte: BitValue get() = kilobytes
inline val Int.KB: BitValue get() = kilobytes

inline val Long.mebibytes: BitValue get() = times(1024).kibibytes
inline val Long.mebibyte: BitValue get() = mebibytes
inline val Long.MiB: BitValue get() = mebibytes

inline val Int.mebibytes: BitValue get() = toLong().mebibytes
inline val Int.mebibyte: BitValue get() = mebibytes
inline val Int.MiB: BitValue get() = mebibytes

inline val Long.megabytes: BitValue get() = times(1000).kilobytes
inline val Long.megabyte: BitValue get() = megabytes
inline val Long.MB: BitValue get() = megabytes

inline val Int.megabytes: BitValue get() = toLong().megabytes
inline val Int.megabyte: BitValue get() = megabytes
inline val Int.MB: BitValue get() = megabytes

inline val Long.gibibytes: BitValue get() = times(1024).mebibytes
inline val Long.gibibyte: BitValue get() = gibibytes
inline val Long.GiB: BitValue get() = gibibytes

inline val Int.gibibytes: BitValue get() = toLong().gibibytes
inline val Int.gibibyte: BitValue get() = gibibytes
inline val Int.GiB: BitValue get() = gibibytes

inline val Long.gigabytes: BitValue get() = times(1000).megabytes
inline val Long.gigabyte: BitValue get() = gigabytes
inline val Long.GB: BitValue get() = gigabytes

inline val Int.gigabytes: BitValue get() = toLong().gigabytes
inline val Int.gigabyte: BitValue get() = gigabytes
inline val Int.GB: BitValue get() = gigabytes

inline val Long.tebibytes: BitValue get() = times(1024).gibibytes
inline val Long.tebibyte: BitValue get() = tebibytes
inline val Long.TiB: BitValue get() = tebibytes

inline val Int.tebibytes: BitValue get() = toLong().tebibytes
inline val Int.tebibyte: BitValue get() = tebibytes
inline val Int.TiB: BitValue get() = tebibytes

inline val Long.terabytes: BitValue get() = times(1000).gigabytes
inline val Long.terabyte: BitValue get() = terabytes
inline val Long.TB: BitValue get() = terabytes

inline val Int.terabytes: BitValue get() = toLong().terabytes
inline val Int.terabyte: BitValue get() = terabytes
inline val Int.TB: BitValue get() = terabytes

inline val Long.pebibytes: BitValue get() = times(1024).tebibytes
inline val Long.pebibyte: BitValue get() = pebibytes
inline val Long.PiB: BitValue get() = pebibytes

inline val Int.pebibytes: BitValue get() = toLong().pebibytes
inline val Int.pebibyte: BitValue get() = pebibytes
inline val Int.PiB: BitValue get() = pebibytes

inline val Long.petabytes: BitValue get() = times(1000).terabytes
inline val Long.petabyte: BitValue get() = petabytes
inline val Long.PB: BitValue get() = petabytes

inline val Int.petabytes: BitValue get() = toLong().petabytes
inline val Int.petabyte: BitValue get() = petabytes
inline val Int.PB: BitValue get() = petabytes

data class BitValue internal constructor(val toBits: Long) {
    val toBytes = toBits / 8
    val B = toBytes
    val toKibibytes = toBytes / 1024
    val toKiloBytes = toBytes / 1000
    val KiB = toKibibytes
    val KB = toKiloBytes
    val toMebibytes = toKibibytes / 1024
    val toMegaBytes = toKiloBytes / 1000
    val MiB = toMebibytes
    val MB = toMegaBytes
    val toGibibytes = toMebibytes / 1024
    val toGigabytes = toMegaBytes / 1000
    val GiB = toGibibytes
    val GB = toGigabytes
    val toTebibytes = toGibibytes / 1024
    val toTerabytes = toGigabytes / 1000
    val TiB = toTebibytes
    val TB = toTerabytes
    val toPebibytes = toTebibytes / 1024
    val toPetabytes = toTerabytes / 1000
    val PiB = toPebibytes
    val PB = toPetabytes

    operator fun plus(other: BitValue) = BitValue(toBits + other.toBits)
    operator fun minus(other: BitValue) = BitValue(toBits - other.toBits)
    operator fun times(mult: Long) = BitValue(mult*toBits)
    operator fun div(div: Long) = BitValue(toBits/div)
    operator fun div(div: BitValue) = toBits/div.toBits

}