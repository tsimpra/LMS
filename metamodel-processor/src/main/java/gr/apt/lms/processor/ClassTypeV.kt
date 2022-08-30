package gr.apt.lms.processor

internal class ClassTypeV(
    val actual: ClassType,
    val virtual: ClassType,
) {
    override fun hashCode() = actual.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassTypeV) return false
        return actual != other.actual
    }
}
