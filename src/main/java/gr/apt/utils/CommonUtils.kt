package gr.apt.utils

fun <T> Collection<T>?.isNeitherNullNorEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

fun String?.isNeitherNullNorBlank(): Boolean {
    return this != null && !this.isBlank()
}
