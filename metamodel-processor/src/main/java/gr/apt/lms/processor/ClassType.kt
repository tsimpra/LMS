package gr.apt.lms.processor

internal class ClassType(givenName: String) {
    val packageName: String
    val typeName: String
    val fullname: String

    init {
        val (p, t, _) = className(givenName)
        packageName = p
        typeName = t
        fullname = "$p.$t"
    }

    val shouldBeImported = !fullname.startsWith("java.lang.") && !fullname.startsWith("kotlin.")

    override fun hashCode() = fullname.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassType) return false
        return fullname == other.fullname
    }
}
