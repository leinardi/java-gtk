package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.*
import ch.bailu.gtk.writer.*
import java.io.Writer

class JavaImpWriter(writer : Writer) : CodeWriter(writer) {

    override fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        start(3)
        a("package ${namespaceModel.getFullNamespace()};")
        end(3)
    }

    override fun writeClass(structureModel : StructureModel) {
        start();
        a("class " + structureModel.impName + " {\n");
    }

    override fun writeInterface(structureModel : StructureModel) {}

    override fun writeInternalConstructor(structureModel : StructureModel) {}

    override fun writeUnsupported(model : Model) {}

    override fun writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        start();
        a("    static native ${methodModel.returnType.impType} ${methodModel.apiName}(${getSelfSignature(methodModel.getParameters())});\n")
        end(1);
    }


    override fun writeMallocConstructor(structureModel : StructureModel) {
        start()
        a("    static native long newFromMalloc();\n")
        end(1)
    }

    override fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel) {}

    override fun writeFactory(structureModel : StructureModel, methodModel : MethodModel) {}


    override fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel) {
        start();
        a("    static native long " + methodModel.apiName);
        writeFactorySignature(methodModel);
        a(";\n");
        end(1);
    }

    override fun writeConstant(parameterModel : ParameterModel) {}


    override fun writeEnd() {
        a("}\n")
    }


    override fun writeSignal(structureModel : StructureModel, methodModel : MethodModel) {

        a("""
            
    static native void ${getJavaSignalMethodName(methodModel.name)}(long _self);
    static ${methodModel.returnType.impType} ${getImpJavaSignalCallbackName(methodModel.name)}(${getSelfSignature(methodModel.getParameters())}) {
        String signal = "${methodModel.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Callback.get(_self, signal)) {
            ${getSignalInterfaceCall(structureModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        a("""
            
    static ${methodModel.returnType.impType} ${getImpJavaSignalCallbackName(methodModel.name)}(${getSignature(methodModel.getParameters(), "")}) {
        String signal = "${methodModel.apiName}";
        long emitter = ${getEmitter(methodModel)};
        
        for (java.lang.Object observer : ch.bailu.gtk.Callback.get(emitter, signal)) {
            ${getSignalInterfaceCall(structureModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())

    }

    private fun getEmitter(methodModel: MethodModel) : String {
        return try {
            val last = methodModel.getParameters().last {
                it.apiType == "ch.bailu.gtk.type.Pointer"
            }

            last.name
        } catch (e : NoSuchElementException) {
            "0"
        }
    }

    private fun getDefaultReturn(methodModel : MethodModel) : String {
        if (!methodModel.returnType.isVoid) {
            return "return ${methodModel.returnType.impDefaultConstant};"
        }
        return ""
    }

    override fun writeField(structureModel : StructureModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        start();
        a("static native ${parameterModel.impType} ${getJavaFieldGetterName(parameterModel.name)}(${getSelfSignature(parameters)});\n")

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            a("static native void ${getJavaFieldSetterName(parameterModel.name)}(${getSelfSignature(parameters)});\n")
        }

        end(1);
    }

    override
    fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        start();
        a("    static native ${methodModel.returnType.impType} ${methodModel.apiName}(${getSignature(methodModel.getParameters(), "")});\n")
        end(1);

    }

    private fun getSignalInterfaceCall(structureModel : StructureModel, methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.returnType.isVoid) {
            result.append("return ")
        }
        result.append("((${structureModel.apiName}.${getJavaSignalInterfaceName(methodModel.name)})observer).${getJavaSignalMethodName(methodModel.name)}(${getSignalInterfaceCallSignature(methodModel)})")

        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
            result.append(".getCPointer()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = " "

        for (p in methodModel.getParameters()) {
            result.append(del)
            if (p.isJavaNative) {
                result.append(p.name)
            } else {
                result.append("new ${p.apiType}(${p.name})")
            }
            del = ", ";
        }
        return result.toString()

    }


    private fun writeFactorySignature(methodModel : MethodModel) {
        a("(");

        var del = " ";


        for (p in methodModel.getParameters()) {
            a(del + p.impType + " " + p.name);
            del = ", ";
        }
        a(")");
    }

    private fun getSelfSignature(parameters : List<ParameterModel>) : String {
        return "long _self${getSignature(parameters, ", ")}"
    }

    private fun getSignature(parameters : List<ParameterModel>, firstDel : String) : String {
        val result = StringBuilder()
        var del = firstDel

        for (p in parameters) {
            if (!p.isCallback) {
                result.append("${del}${p.impType} ${p.name}")
                del = ", "
            }
        }
        return result.toString()
    }
}