package ch.bailu.gtk.builder

import ch.bailu.gtk.Directories
import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.kotlin.KotlinApiWriter
import com.squareup.kotlinpoet.FileSpec
import java.io.IOException

class ModelBuilder(private val directories: Directories) : BuilderInterface {

    private var namespace: NamespaceModel = NamespaceModel()

    override fun buildStructure(structure: StructureTag) {
        val model = StructureModel(structure, namespace)

        if (model.isSupported) {
            writeKotlinFile(model)
            if (model.hasNativeCalls()) {
                writeJavaJnaFile(model)
            }
        }
    }

    override fun buildNamespaceStart(namespace: NamespaceTag, namespaceConfig: NamespaceConfig) {
        this.namespace = NamespaceModel(namespace, namespaceConfig)
    }


    @Throws(IOException::class)
    override fun buildNamespaceEnd() {
        // functions
        var model = StructureModel(namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeJavaJnaFile(model)
        }


        // constants
        model = StructureModel(this.namespace, namespace.constants)
        writeJavaFile(model)
    }

    override fun buildAlias(aliasTag: AliasTag) {}


    @Throws(IOException::class)
    override fun buildEnumeration(enumeration: EnumerationTag) {
        val model = StructureModel(namespace, enumeration)
        writeJavaFile(model)
    }


    override fun buildCallback(callbackTag: CallbackTag) {}

    private fun writeKotlinFile(model: StructureModel) {
        val fileSpecBuilder = FileSpec.builder(model.nameSpaceModel.fullNamespace, model.apiName)
        model.write(KotlinApiWriter(fileSpecBuilder))
        fileSpecBuilder.build().writeTo(directories.javaBaseDir)
    }

    @Throws(IOException::class)
    private fun writeJavaFile(model: StructureModel) {
//        directories.getJavaWriter(model.apiName, namespace).use {
//            model.write(JavaApiWriter(TextWriter(it), Configuration.createJavaDocConfig(it)))
//        }
    }

    @Throws(IOException::class)
    private fun writeJavaJnaFile(model: StructureModel) {
//        directories.getJavaJnaWriter(model, namespace).use {
//            model.write(JavaImpWriter(TextWriter(it)))
//        }
    }
}
