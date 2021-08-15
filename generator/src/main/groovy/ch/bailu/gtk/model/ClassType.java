package ch.bailu.gtk.model;

import ch.bailu.gtk.Configuration;
import ch.bailu.gtk.converter.AliasTable;
import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.converter.StructureTable;
import ch.bailu.gtk.tag.ParameterTag;


public class ClassType implements ClassTypeInterface {

    private final NamespaceType type;

    private boolean valid = true;


    public ClassType() {
        type = new NamespaceType("", "");
        valid = false;

    }

    
    public ClassType(String namespace, ParameterTag parameter) {
        this(namespace, parameter.getTypeName(), parameter.getType());
    }

    public ClassType(String namespace, String typeName, String ctype) {
        this(namespace, typeName, new CType(ctype));
    }
    
    public ClassType(String namespace, String typeName, CType ctype) {
        type = convertAlias(namespace, typeName);
        setValid(type.isValid() && isInTable(type) && ctype.isSinglePointer());
    }

    private NamespaceType convertAlias(String namespace, String type) {
        return new NamespaceType(namespace, AliasTable.instance().convert(namespace, type));
    }

    private boolean isInTable(NamespaceType n) {
        return StructureTable.instance().contains(n.getNamespace(), n.getName());
    }


    private void setValid(boolean v) {
        valid = v && valid;
    }

    public boolean isValid() {
        return valid;
    }

    public String getFullName() {
        if (isValid() && !type.hasCurrentNamespace()) {
                return getFullNamespace() + "." + type.getName();
        }
        return getName();
    }

    @Override
    public String getNamespace() {
        return type.getNamespace();
    }

    @Override
    public String getFullNamespace() {
        return Configuration.BASE_NAME_SPACE_DOT + type.getNamespace();
    }

    @Override
    public String getName() {
        return type.getName();
    }
}