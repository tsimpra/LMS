package gr.apt.exception

import java.io.Serializable

class LmsException : Exception, Serializable {
    constructor() : super() {}
    constructor(msg: String) : super(msg) {}
    constructor(msg: String, e: Exception) : super(msg, e) {}

    companion object {
        private const val serialVersionUID = 1L
    }
}