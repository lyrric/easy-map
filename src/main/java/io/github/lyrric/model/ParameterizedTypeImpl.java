package io.github.lyrric.model;

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


}
