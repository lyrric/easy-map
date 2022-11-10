package io.github.lyrric.easymapstruct.model;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author xiaodong.wang
 * @date 2022/2/18 14:13
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    /**
     * 泛型，如List<String>中的String
     */
    private final Type[] actualTypeArguments;
    private final Type   ownerType;
    /**
     * 本身的类型，如List
     */
    private final Class<?>   rawType;

    public ParameterizedTypeImpl(Type[] actualTypeArguments, Type ownerType, Class<?> rawType){
        this.actualTypeArguments = actualTypeArguments;
        this.ownerType = ownerType;
        this.rawType = rawType;
    }

    public static ParameterizedTypeImpl make(Class<?> rawType,
                                             Type[] actualTypeArguments,
                                             Type ownerType) {
        return new ParameterizedTypeImpl(actualTypeArguments, ownerType,rawType);
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.ownerType != null) {
            if (this.ownerType instanceof Class) {
                sb.append(((Class)this.ownerType).getName());
            } else {
                sb.append(this.ownerType.toString());
            }

            sb.append(".");
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl)this.ownerType).rawType.getName() + "$", ""));
            } else {
                sb.append(this.rawType.getName());
            }
        } else {
            sb.append(this.rawType.getName());
        }

        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            sb.append("<");
            boolean first = true;
            for (Type t : this.actualTypeArguments) {
                if (!first) {
                    sb.append(", ");
                }

                if (t instanceof Class) {
                    sb.append(((Class<?>) t).getName());
                } else {
                    sb.append(t.toString());
                }

                first = false;
            }

            sb.append(">");
        }

        return sb.toString();
    }


}
