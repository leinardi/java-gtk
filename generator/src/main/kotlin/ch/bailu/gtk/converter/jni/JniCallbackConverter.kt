package ch.bailu.gtk.converter.jni

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.writer.getJniCallbackName

class JniCallbackConverter(parameterModel: ParameterModel) : JniTypeConverter() {

    private val parameterModel = parameterModel

   
    override fun getAllocateResourceString(classModel: ClassModel): String {
        return "const ${parameterModel.gtkType} __${parameterModel.name} = (${parameterModel.gtkType}) ${getJniCallbackName(classModel, parameterModel)};"
    }

    override fun getFreeResourcesString(): String {
        return ""
    }

    override fun getCallSignatureString(classModel: ClassModel): String {
        return " __${parameterModel.name}"
    }

    override fun getJniType(): String {
        return "jlong"
    }


    override fun getImpDefaultConstant(): String {
        return "0"
    }

    override fun getJniSignatureID(): String {
        return "J"
    }

    override fun getJniCallbackMethodName(): String {
        return "CallStaticLongMethod"
    }
}