package gr.apt.lms.utils

fun <T> Collection<T>?.isNeitherNullNorEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

fun String?.isNeitherNullNorBlank(): Boolean {
    return this != null && !this.isBlank()
}

val stringComparator: (String?, String?) -> Int =
    { a: String?, b: String? -> if (a == null) -1 else if (b == null) 1 else a.compareTo(b) }
