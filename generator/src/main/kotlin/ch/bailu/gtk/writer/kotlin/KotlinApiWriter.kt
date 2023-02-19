package ch.bailu.gtk.writer.kotlin

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.writer.CodeWriter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec

class KotlinApiWriter(private val fileBuilder: FileSpec.Builder) : CodeWriter {
    private var typeBuilder: TypeSpec.Builder? = null
    private var companionBuilder: TypeSpec.Builder? = null

    override fun writeStart(structureModel: StructureModel, namespaceModel: NamespaceModel) {
        fileBuilder.addFileComment("This is a generated file. Do not modify.")
    }

    override fun writeClass(structureModel: StructureModel) {
        val superClass = ClassName(structureModel.parent.nameSpaceModel.fullNamespace, structureModel.parent.apiName)
        typeBuilder = TypeSpec.classBuilder(structureModel.apiName)
            .addKdoc("%L", structureModel.doc)
            .superclass(superClass)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("name", String::class)
                    .build()
            )
    }

    override fun writeInterface(structureModel: StructureModel) {
        typeBuilder = TypeSpec.interfaceBuilder("MyInterface")
            .addFunction(
                FunSpec.builder("myInterfaceFunction")
                    .addModifiers(KModifier.ABSTRACT)
                    .build()
            )
    }

    override fun writeUnsupported(model: Model) {
        fileBuilder.addFileComment("\n${model}")
    }

    override fun writeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        checkNotNull(typeBuilder).apply {
            addFunction(
                FunSpec.builder(methodModel.apiName).apply {
                    if (methodModel.parameters.isNotEmpty()
                        || methodModel.doc.length > 3
                        || !methodModel.returnType.isVoid
                    ) {
                        addKdoc("%L", methodModel.doc)
                    }
                    if (methodModel.throwsError) {
                        addAnnotation(
                            AnnotationSpec.builder(Throws::class)
                                .addMember("%T::class", ClassName("ch.bailu.gtk.type.exception", "AllocationError"))
                                .build()
                        )
                    }
                    val returnClass =
                        ClassName("", methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace))
                    returns(returnClass)
                    addStatement("%L", "TODO()")
                }.build()
            )
        }
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        if (companionBuilder == null) {
            companionBuilder = TypeSpec.companionObjectBuilder()
        }
        companionBuilder = checkNotNull(companionBuilder).addFunction(
            FunSpec.builder(methodModel.apiName).apply {
                if (methodModel.parameters.isNotEmpty() ||
                    methodModel.doc.length > 3 ||
                    !methodModel.returnType.isVoid
                ) {
                    addKdoc("%L", methodModel.doc)
                }
                if (methodModel.throwsError) {
                    addAnnotation(
                        AnnotationSpec.builder(Throws::class)
                            .addMember(
                                "%T::class",
                                ClassName("ch.bailu.gtk.type.exception", "AllocationError")
                            )
                            .build()
                    )
                }
                val returnClass =
                    ClassName(
                        "",
                        methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)
                    )
                returns(returnClass)
                addStatement("%L", "TODO()")
            }.build()
        )
    }

    override fun writeInternalConstructor(structureModel: StructureModel) {
        val cPointerClass = ClassName("ch.bailu.gtk.type", "CPointer")
        val cPointerParameter = ParameterSpec.builder("pointer", cPointerClass)
            .build()
        checkNotNull(typeBuilder).apply {
            primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(cPointerParameter)
                    .build()
            ).addSuperclassConstructorParameter("%N", cPointerParameter)
        }
    }


    override fun writeMallocConstructor(structureModel: StructureModel) {
        if (!structureModel.hasDefaultConstructor()) {
            checkNotNull(typeBuilder).apply {
                addFunction(
                    FunSpec.constructorBuilder()
                        .callSuperConstructor("TODO()")
                        .build()
                )
                addFunction(
                    FunSpec.builder("destroy")
                        .addStatement("%L", "TODO()")
                        .build()
                )
            }
        }
    }

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {
        checkNotNull(typeBuilder)
            .addFunction(
                FunSpec.constructorBuilder().apply {
                    if (methodModel.parameters.isNotEmpty() || methodModel.doc.length > 3) {
                        addKdoc("%L", methodModel.doc)
                    }
                    methodModel.parameters.forEach { parameter ->
                        addParameter(
                            ParameterSpec.builder(
                                parameter.name,
                                ClassName(
                                    "", parameter.getApiTypeName(structureModel.nameSpaceModel.namespace)
                                )
                            ).build()
                        )
                    }
                    callSuperConstructor("TODO()")
                }
                    .build()
            )
    }

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {
//        out.start(1)
//        javaDoc.writeFactory(structureModel, methodModel)
//        out.a(
//            """
//            public static ${structureModel.apiName} ${methodModel.apiName}${structureModel.apiName}(${
//                getSignature(
//                    structureModel,
//                    methodModel.parameters
//                )
//            }) ${getThrowsExtension(methodModel)} {
//                CPointer __self = new CPointer(${structureModel.jnaName}.INST().${methodModel.gtkName}(${
//                getCallSignature(
//                    methodModel,
//                    "",
//                    "getClassHandler().getInstance()"
//                )
//            }));
//                if (__self.isNull()) {
//                    ${getThrowsOnNullStatement(structureModel, methodModel)};
//                }
//                return new ${structureModel.apiName}(__self);
//            }
//
//        """, 4
//        )
//        out.end(1)
    }

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
    }


    override fun writeConstant(structureModel: StructureModel, parameterModel: ParameterModel) {
//        out.start(0)
//        javaDoc.writeConstant(structureModel, parameterModel)
//
//        var value = parameterModel.value
//        var type = parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace)
//
//        if (parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace).endsWith("Str")) {
//            type = "String"
//            value = "\"$value\""
//        }
//
//        out.a("    " + type + " " + parameterModel.name + " = " + value + ";\n")
//        out.end(1)
    }

    override fun writeEnd() {
    }

    override fun writeGetTypeFunction(structureModel: StructureModel) {
//        out.start(1)
//        out.a("    public static long getTypeID() { return ${structureModel.jnaName}.INST().${structureModel.typeFunction}(); }\n")
//        out.end(0)
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
//        out.start(1)
//
//        out.a(
//            """
//            public final static String ${methodModel.signalNameVariable} = "${methodModel.name}";
//
//        """, 4
//        )
//
//        javaDoc.writeSignal(structureModel, methodModel)
//        out.a(
//            """
//            public ch.bailu.gtk.lib.handler.SignalHandler ${Names.getJavaCallbackMethodName(methodModel.name)}(${
//                Names.getJavaCallbackInterfaceName(
//                    methodModel.name
//                )
//            } signal) {
//                return new ch.bailu.gtk.lib.handler.SignalHandler(this, ${methodModel.signalNameVariable}, to${
//                Names.getJavaCallbackInterfaceName(
//                    methodModel.name
//                )
//            }(signal));
//            }
//        """, 4
//        )
//        out.end(1)
    }


    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        if (isSignal) {
            writeSignalInterface(structureModel, methodModel)
        } else {
            writeCallbackInterface(structureModel, methodModel)
        }
    }

    private fun writeSignalInterface(structureModel: StructureModel, methodModel: MethodModel) {
//        val iName = Names.getJavaCallbackInterfaceName(methodModel.name)
//        val mName = Names.getJavaCallbackMethodName(methodModel.name)
//
//        out.start(1)
//        out.a("    public interface ${Names.getJavaCallbackInterfaceName(methodModel.name)} {\n")
//
//        javaDoc.writeCallback(structureModel, methodModel)
//
//        out.a(
//            """
//                ${methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${mName}(${
//                getSignature(
//                    structureModel,
//                    methodModel.parameters
//                )
//            });
//            }
//
//            private static ${structureModel.jnaName}.${iName} to${iName}(${iName} in) {
//                ${structureModel.jnaName}.${iName} out = null;
//                if (in != null) {
//                    out = (${
//                getCallbackOutSignature(
//                    methodModel,
//                    "__self",
//                    "__data"
//                )
//            }) -> in.${mName}${getCallbackInSignature(structureModel, methodModel)};
//                }
//                return out;
//            }
//        """, 4
//        )
//
//        out.end(1)
    }

    private fun writeCallbackInterface(structureModel: StructureModel, methodModel: MethodModel) {
//        val iName = Names.getJavaCallbackInterfaceName(methodModel.name)
//        val mName = Names.getJavaCallbackMethodName(methodModel.name)
//
//        out.start(1)
//        out.a("    public interface ${Names.getJavaCallbackInterfaceName(methodModel.name)} {\n")
//
//        javaDoc.writeCallback(structureModel, methodModel)
//
//        out.a(
//            """
//                ${methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${mName}(${
//                getSignature(
//                    structureModel,
//                    methodModel.parameters,
//                    "ch.bailu.gtk.lib.handler.CallbackHandler __self"
//                )
//            });
//            }
//
//            private static ${structureModel.jnaName}.${iName} to${iName}(ch.bailu.gtk.type.Pointer instance, String methodName, $iName in) {
//                ${structureModel.jnaName}.${iName} out = null;
//                if (in != null) {
//                    ch.bailu.gtk.lib.handler.CallbackHandler __callback = new ch.bailu.gtk.lib.handler.CallbackHandler(instance, methodName);
//                    out = (${getCallbackOutSignature(methodModel)}) -> in.${mName}${
//                getCallbackInSignature(
//                    structureModel,
//                    methodModel,
//                    "__callback"
//                )
//            };
//                    __callback.register(out);
//                }
//                return out;
//            }
//        """, 4
//        )
//
//        out.end(1)
    }

//    private fun getCallbackInSignature(
//        structureModel: StructureModel,
//        methodModel: MethodModel,
//        prefix: String = ""
//    ): String {
//        val result = StringBuilder()
//
//        result.append("(${getSignalInterfaceCallSignature(structureModel, methodModel, prefix)})")
//
//        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
//            result.append(".getCPointer()")
//        }
//        return result.toString()
//    }
//
//    private fun getSignalInterfaceCallSignature(
//        structureModel: StructureModel,
//        methodModel: MethodModel,
//        prefix: String = ""
//    ): String {
//        val result = StringBuilder(prefix)
//        var del = if (prefix.isNotEmpty()) ", " else ""
//
//        for (p in (methodModel).parameters) {
//            result.append(del)
//            if (p.isJavaNative) {
//                result.append("_").append(p.name)
//            } else {
//                result.append("new ${p.getApiTypeName(structureModel.nameSpaceModel.namespace)}(new CPointer(_${p.name}))")
//            }
//            del = ", "
//        }
//        return result.toString()
//    }
//
//    private fun getCallbackOutSignature(methodModel: MethodModel, prefix: String = "", postfix: String = ""): String {
//        val result = StringBuilder(prefix)
//
//        var del = if (prefix.isEmpty()) "" else ", "
//
//        methodModel.parameters.forEach {
//            result.append("${del}_${it.name}")
//            del = ", "
//        }
//
//        if (postfix.isNotEmpty()) {
//            result.append("${del}${postfix}")
//        }
//
//        return result.toString()
//    }
//
//    private fun getSignature(
//        structureModel: StructureModel,
//        parameters: List<ParameterModel>,
//        prefix: String = ""
//    ): String {
//        val result = StringBuilder(prefix)
//        var del = if (prefix.isEmpty()) "" else ", "
//
//        for (p in parameters) {
//            val nonnull = if (!p.isJavaNative) {
//                if (p.nullable) {
//                    "@Nullable "
//                } else {
//                    "@Nonnull "
//                }
//            } else {
//                ""
//            }
//
//            result.append("${del}${nonnull}${p.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${p.name}")
//            del = ", "
//        }
//        return result.toString()
//    }
//
//    private fun getCallSignature(methodModel: MethodModel, prefix: String, self: String): String {
//        val result = StringBuilder(prefix)
//        var del = if (prefix.isEmpty()) "" else ", "
//
//
//        for (p in methodModel.parameters) {
//            if (p.isCallback && p.callbackModel != null) {
//                val iName = Names.getJavaCallbackInterfaceName(p.callbackModel.name)
//                result.append("${del}to${iName}(${self}, \"${methodModel.apiName}\", ${p.name})")
//            } else if (p.isJavaNative) {
//                result.append("${del}${p.name}")
//            } else if (p.nullable) {
//                result.append("${del}toCPointer(${p.name})")
//            } else {
//                result.append("${del}toCPointerNotNull(${p.name})")
//            }
//            del = ", "
//        }
//
//        if (methodModel.throwsError) {
//            result.append("${del}0L")
//        }
//        return result.toString()
//    }

    override fun writeBeginStruct(structureModel: StructureModel, fields: ModelList<ParameterModel>) {
//        out.start(0)
//        out.a(
//            """
//            private ${structureModel.jnaName}.Fields _fields;
//
//            ${structureModel.jnaName}.Fields toFields() {
//                if (_fields == null) {
//                    _fields = new ${structureModel.jnaName}.Fields(getCPointer());
//                }
//                return _fields;
//            }
//        """, 4
//        )
//        out.end(0)
    }

    override fun writeEndStruct() {}
    override fun writeBeginInstace(namespaceModel: NamespaceModel) {}
    override fun writeEndInstance() {

    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
//        val parameters: MutableList<ParameterModel> = ArrayList()
//
//        val getter = Names.getJavaFieldGetterName(parameterModel.name)
//        val setter = Names.getJavaFieldSetterName(parameterModel.name)
//
//        out.start(1)
//        javaDoc.writeField(structureModel, parameterModel)
//
//        if (parameterModel.isJavaNative) {
//            out.a(
//                """
//                public ${parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${getter}() {
//                    toFields().readField("${parameterModel.name}");
//                    return toFields().${parameterModel.name};
//                }
//                """, 4
//            )
//
//        } else {
//            out.a(
//                """
//                public ${parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${getter}() {
//                    toFields().readField("${parameterModel.name}");
//                    return new ${parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace)}(new CPointer(toFields().${parameterModel.name}));
//                }
//                """, 4
//            )
//        }
//
//        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
//            parameters.add(parameterModel)
//            if (parameterModel.isJavaNative) {
//                out.a(
//                    """
//                    public void ${setter}(${getSignature(structureModel, parameters)}) {
//                        toFields().${parameterModel.name} = ${parameterModel.name};
//                        toFields().writeField("${parameterModel.name}");
//                    }
//                """, 4
//                )
//            } else {
//                out.a(
//                    """
//                    public void ${setter}(${getSignature(structureModel, parameters)}) {
//                        toFields().${parameterModel.name} = ${parameterModel.name}.getCPointer();
//                        toFields().writeField("${parameterModel.name}");
//                    }
//                """, 4
//                )
//            }
//        }
//        out.end(1)
    }

    override fun close() {
        fileBuilder.addType(
            checkNotNull(typeBuilder).apply {
                companionBuilder?.let { addType(it.build()) }
            }.build()
        )
    }
}
