package net.tjalp.kelp.util

/**
 * Return a pluralized version of the string
 * given the specified amount of units.
 *
 * @param count The unit count
 * @param plural Optional plural version of the string
 * @return Optionally pluralized string
 */
fun String.pluralize(count: Long, plural: String? = null): String {
    return if(count > 1L || count == 0L) {
        plural ?:
        if(this.endsWith("s")
            || this.endsWith("sh")
            || this.endsWith("ch")
            || this.endsWith("x")
            || this.endsWith("z")) {
            this + "es"
        } else {
            this + "s"
        }
    } else {
        this
    }
}

fun String.pluralize(count: Int, plural: String? = null): String = pluralize(count.toLong(), plural)