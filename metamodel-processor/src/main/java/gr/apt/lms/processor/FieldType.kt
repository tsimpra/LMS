package gr.apt.lms.processor

import com.google.devtools.ksp.symbol.KSPropertyDeclaration

private val String.plain: String
    get() {
        return replace("`", "")
    }

internal enum class FieldType(val isCollection: Boolean, val isReference: Boolean = true, val fromDB: Boolean) {

    FIELD(false, false, true),
    REFERENCE(false, true, true),
    COLLECTION(true, false, true),
    GENERIC(false, false, false);

    companion object {
        fun guessFieldType(type: KSPropertyDeclaration): FieldType {
            for (ann in type.annotations) {
                val name = ann.shortName.asString()
                if (name.endsWith("OneToMany") || name.endsWith("ManyToMany"))
                    return COLLECTION
                else if (name.endsWith("JoinColumn") || name.endsWith("OneToOne") || name.endsWith("ManyToOne"))
                    return REFERENCE
                else if (name.endsWith("Column"))
                    return FIELD
            }
            return GENERIC
        }
    }

    val isField = !isCollection && !isReference

    fun asCode(actualType: String, mapperName: String, name: String, mutable: Boolean) = when {
        isField -> asFieldCode(actualType, name, name.plain, mutable)
        isReference -> asReferenceCode(mapperName, name, name.plain, mutable)
        isCollection -> asCollectionCode(mapperName, name, name.plain, mutable)
        else -> throw IllegalArgumentException()
    }

    private fun asFieldCode(actualType: String, name: String, plainName: String, mutable: Boolean) =
        """    fun $name(name: String? = null) = add("$plainName", name ?: "$plainName", $actualType::class, { it.$name }${if (mutable) ", { e, _, v -> e.$name = v }" else ""})
"""


    private fun asReferenceCode(
        mapperName: String,
        name: String,
        plainName: String,
        mutable: Boolean,
    ) = """    fun $name(
        name: String? = null,
        references: Boolean = false,
        collections: Boolean = false,
        empty: Boolean = false,
        mapper: $mapperName? = null,
        mapping: $mapperName.() -> Unit
    ) = (if (mapper == null) $mapperName(references, collections, empty) else mapper.clone()).apply(mapping).let { m ->
        add("$plainName", name ?: "$plainName", Map::class,
            { e -> e.$name?.let { m[it] } },
            ${if (mutable) "{ e, _, v -> e.$name = m[v as Map<String,Any>] }" else "{ _, _, _ -> throw IllegalArgumentException(\"Key is immutable: \'$name\'\") }"}
    )
    }
"""


    private fun asCollectionCode(
        mapperName: String,
        name: String,
        plainName: String,
        mutable: Boolean,
    ) = """    fun $name(
        name: String? = null,
        references: Boolean = false,
        collections: Boolean = false,
        empty: Boolean = false,
        mapper: $mapperName? = null,
        mapping: $mapperName.() -> Unit
    ) = (if (mapper == null) $mapperName(references, collections, empty) else mapper.clone()).apply(mapping).let { m ->
        add("$plainName", name ?: "$plainName", Collection::class,
            { e -> e.$name?.map { if (it != null) m[it] else null } },
            ${
        if (mutable) "{ e, _, v -> e.$name = v.mapNotNull { t -> if (t != null) m[t as Map<String,Any>] else null }.toMutableList() }"
        else "{ _, _, _ -> throw IllegalArgumentException(\"Key is immutable: \'$name\'\") }"
    } 
    )
    }
"""

}
