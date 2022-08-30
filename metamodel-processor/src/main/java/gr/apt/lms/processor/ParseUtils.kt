package gr.apt.lms.processor

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import java.util.*

// it should bring top level object hierarchy first, so properties should be inherited top to bottom
internal fun getAllClassDeclarations(
    target: KSClassDeclaration,
    registry: LinkedHashMap<ClassType, Collection<FieldProperty>>,
): Pair<ClassType, Collection<FieldProperty>>? {

    // Check and return if registry already contains this information
    val classType = ClassType(target.qualifiedName?.asString() ?: return null)
    registry[classType]?.let { return classType to it }

    // Initialize registry
    val properties = TreeSet<FieldProperty>()
    registry[classType] = properties

    // Get properties from parent classes
    target.superTypes.map { it.resolve().declaration }
        .filterIsInstance<KSClassDeclaration>()
        .forEach { properties.addAll(getAllClassDeclarations(it, registry)?.second ?: return@forEach) }

    // Add local properties
    target.getAllProperties().forEach {
        val type = FieldType.guessFieldType(it)
        val actualType = ClassType(resolveClassName(it.type, false) ?: return@forEach)
        val virtualType = if (type.isCollection)
            ClassType((resolveClassName(it.type, true) ?: return@forEach)) else actualType
        properties.add(
            FieldProperty(
                ClassTypeV(actualType, virtualType),
                type,
                it.simpleName.getShortName(),
                it.isMutable,
            ),
        )
    }
    return classType to properties
}

internal fun className(classNode: KSClassDeclaration, imports: MutableMap<String, String>): String? {
    val simpleName = classNode.toString()
    return if (imports.containsKey(simpleName))
        simpleName
    else {
        imports[simpleName] = classNode.qualifiedName?.asString() ?: return null
        simpleName
    }
}

internal fun resolveClassName(type: KSTypeReference, shouldUseGenerics: Boolean): String? {
    val ctype = if (shouldUseGenerics) type.element?.typeArguments?.get(0)?.type ?: return null else type
    return ctype.resolve().declaration.qualifiedName?.asString() ?: return null
}

internal fun className(fullname: String): Triple<String, String, String?> {
    if (!fullname.contains('.')) throw IllegalArgumentException("Class name should be set with a valid `package.class` definition")
    val (classname, generics) = if (fullname.contains('<'))
        fullname.substringBeforeLast('<') to '<' + fullname.substringAfter('<')
    else
        fullname to null
    val packagename = classname.substringBeforeLast('.')
    val basename = classname.substringAfterLast('.')
    return Triple(packagename, basename, generics)
}
