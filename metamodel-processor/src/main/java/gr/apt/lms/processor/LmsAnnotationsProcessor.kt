/*
 * (c) 2022 by Panayotis Katsaloulis
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

/*
 * Ksp processor that processes all classes annotated with javax.persistence.Entity
 * or gr.apt.lms.utils.Dto annotation. This ksp processor is based on the processor
 * and the methods that Panayotis Katsaloulis has created.
 */
package gr.apt.lms.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated

internal const val ENTITY = "javax.persistence.Entity"
internal const val DTO = "gr.apt.lms.utils.Dto"

class LmsAnnotationsProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        LmsAnnotationsProcessor(environment)
}

private class LmsAnnotationsProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        metaModelCreator(environment, resolver)
        return emptyList()
    }
}
