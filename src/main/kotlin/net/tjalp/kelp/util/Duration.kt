package net.tjalp.kelp.util

import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong

/**
 * Returns a formatted representation of this time, with all units included
 *
 * @ return String
 */

fun Duration.formatFull(): String {
    return format(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)
}

/**
 * Returns a formatted representation of this time, including days, hours, and minutes
 *
 * @return String
 */
fun Duration.formatBasic(): String {
    return format(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES)
}

/**
 * Returns a short formatted representation of this time, with all units included
 *
 * @return String
 */
fun Duration.formatFullShort(): String {
    return formatShort(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)
}

/**
 * Returns a short formatted representation of this time, including days, hours, and minutes
 *
 * @return String
 */
fun Duration.formatBasicShort(): String {
    return formatShort(TimeUnit.DAYS, TimeUnit.HOURS, TimeUnit.MINUTES)
}

/**
 * Returns a formatted representation of this time
 *
 * @param units The time units to include
 * @return String
 */

fun Duration.format(vararg units: TimeUnit): String {
    val millis = toMillis()
    val days = millis / 1000 / 86400
    val hours = millis / 1000 / 3600 % 24
    val minutes = millis / 1000 / 60 % 60
    val seconds = millis / 1000 % 60
    val joiner = ListJoiner()

    if (TimeUnit.DAYS in units && days > 0) {
        joiner.add(days.toString() + " day".pluralize(days.toInt()))
    }

    if (TimeUnit.HOURS in units && hours > 0) {
        joiner.add(hours.toString() + " hour".pluralize(hours.toInt()))
    }

    if (TimeUnit.MINUTES in units && minutes > 0) {
        joiner.add(minutes.toString() + " minute".pluralize(minutes.toInt()))
    }

    if (TimeUnit.SECONDS in units && seconds > 0) {
        joiner.add(seconds.toString() + " second".pluralize(seconds.toInt()))
    }

    // Add 0 seconds if empty
    if (TimeUnit.SECONDS in units && seconds == 0L && joiner.size() == 0) {
        joiner.add("0 seconds")
    }

    return joiner.toString()
}


/**
 * Returns a formatted representation of this time in a shortened format
 *
 * @param units The time units to include
 * @return String
 */
fun Duration.formatShort(vararg units: TimeUnit): String {
    val millis = toMillis()
    val days = millis / 1000 / 86400
    val hours = millis / 1000 / 3600 % 24
    val minutes = millis / 1000 / 60 % 60
    val seconds = millis / 1000 % 60
    val joiner = ListJoiner()

    if (TimeUnit.DAYS in units && days > 0) {
        joiner.add("${days}d")
    }

    if (TimeUnit.HOURS in units && hours > 0) {
        joiner.add("${hours}h")
    }

    if (TimeUnit.MINUTES in units && minutes > 0) {
        joiner.add("${minutes}m")
    }

    if (TimeUnit.SECONDS in units && seconds > 0) {
        joiner.add("${seconds}s")
    }

    return joiner.toString()
}

/**
 * Converts this duration to the total length in Minecraft ticks.
 */
fun Duration.toTicks(): Long {
    return (toMillis().toDouble() / 50.0).roundToLong()
}
