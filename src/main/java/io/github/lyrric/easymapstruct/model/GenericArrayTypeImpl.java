package io.github.lyrric.easymapstruct.model;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * 泛型数组
 */
public class GenericArrayTypeImpl implements GenericArrayType {

    /**
     * 数组元素的类型
     */
    private final Type componentType;


    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public String getTypeName() {
        return toString();
    }

    public static GenericArrayTypeImpl make(Type componentType){
        return new GenericArrayTypeImpl(componentType);
    }

    @Override
    public String toString() {
        Type var1 = this.getGenericComponentType();
        StringBuilder var2 = new StringBuilder();
        if (var1 instanceof Class) {
            var2.append(((Class<?>)var1).getName());
        } else {
            var2.append(var1.toString());
        }
        var2.append("[]");
        return var2.toString();
    }
}
