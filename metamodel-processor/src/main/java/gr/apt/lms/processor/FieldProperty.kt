package gr.apt.lms.processor

internal class FieldProperty(
    val classType: ClassTypeV,
    val fieldType: FieldType,
    name: String,
    val mutable: Boolean,
) :
    Comparable<FieldProperty> {

    val name = if (reserved.contains(name)) "`$name`" else name

    override fun hashCode() = name.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldProperty) return false
        if (name != other.name) return false
        return true
    }

    override operator fun compareTo(other: FieldProperty) = name.compareTo(other.name)

    private companion object {
        private val reserved = setOf("var", "val", "object")
    }
}
