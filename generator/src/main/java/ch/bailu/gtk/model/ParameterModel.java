package ch.bailu.gtk.model;

import ch.bailu.gtk.converter.Util;
import ch.bailu.gtk.converter.Filter;
import ch.bailu.gtk.converter.JavaNames;
import ch.bailu.gtk.converter.JniTypeConverter;
import ch.bailu.gtk.tag.ParameterTag;

public class ParameterModel extends Model {
    private final String name;

    private final CType cType;
    private final ClassType classType;
    private final JavaType jType;

    //private final String value;

    private final JniTypeConverter jniConverter;

    final private boolean isWriteable;

    private MethodModel callbackModel = null;

    private final ParameterTag parameterTag;

    public ParameterModel(String namespace, ParameterTag parameterTag, boolean toUpper, boolean supportsDirectAccess) {

        this.parameterTag = parameterTag;

        if (toUpper) {
            name = JavaNames.fixToken(parameterTag.getName().toUpperCase());
         } else {
            name = JavaNames.fixToken(parameterTag.getName());
        }

        classType = new ClassType(namespace, parameterTag, supportsDirectAccess);
        if (classType.isClass()) {
            cType = new CType("void*");
            jType = new JavaType("long");

        } else if (Util.isEnum(namespace, parameterTag)) {
            cType = new CType("int");
            jType = new JavaType("int");

        } else {
            cType = new CType(parameterTag.getType());
            jType = new JavaType(parameterTag.getType());
        }

        if (classType.isCallback()) {
            callbackModel = new MethodModel(namespace, classType.getCallbackTag());
        }


        jniConverter = JniTypeConverter.factory(this);

        //setSupported("private", parameter.isPrivate());
        setSupported("value", Filter.values(name, getValue()));
        setSupported("jType", jType.isValid());
        setSupported("callback", isCallbackSupported());

        this.isWriteable = parameterTag.isWriteable();
    }


    private boolean isCallbackSupported() {
        if (isCallback()) {
            return callbackModel.isSupported() && !callbackModel.hasCallback();
        }
        return true;
    }

    public String getValue() {
        return saveString(parameterTag.getValue());
    }
    public String getName() {
        return saveString(name);
    }

    public String getApiType() {
        if (isCallback()) {
            return callbackModel.getSignalInterfaceName();
        }

        if (classType.isClass()) {
            return classType.getFullName();
        }

        return jType.getType();
    }

    public String getImpType() {
        return jType.getType();
    }

    public boolean isVoid() {
        return jType.isVoid();
    }

    public boolean isJavaNative() {
        return classType.isClass() == false;
    }

    public String getJniType() {
        return jniConverter.getJniType();
    }



    public String getGtkType() {
        return cType.getName();
    }


    @Override
    public String toString() {
        String supported="";
        if (isSupported()) {
            supported = "(s)";
        }

        return collonList(new String[] {
                supported,
                parameterTag.getType(),
                parameterTag.getTypeName(), // <-
                parameterTag.getName(),     // <-
                parameterTag.getValue(),
                getGtkType(),
                getApiType()});
    }

    public String collonList(String[] strings) {
        String del = "";
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String s: strings) {
            builder.append(del);
            builder.append(s);
            del = ":";
        }
        return builder.append("]").toString();
    }

    private static String saveString(String in) {
        if (in == null) return "";
        return in;
    }

    public String getImpDefaultConstant() {
       return jniConverter.getImpDefaultConstant();
    }

    public String getJniSignatureID() {
        return jniConverter.getJniSignatureID();
    }


    public String getJniCallbackMethodName() {
        return jniConverter.getJniCallbackMethodName();
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public boolean isCallback() {
        return classType.isCallback();
    }

    public MethodModel getCallbackModel() {
        return callbackModel;
    }

    public JniTypeConverter getJniConverter() {
        return jniConverter;
    }

    public boolean isDirectType() {
        return classType.isDirectType();
    }
}
