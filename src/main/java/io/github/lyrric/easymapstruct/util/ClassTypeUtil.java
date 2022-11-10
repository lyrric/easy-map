package io.github.lyrric.easymapstruct.util;


import io.github.lyrric.easymapstruct.model.ParameterizedTypeImpl;
import net.openhft.compiler.CompilerUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author wangxiaodong
 */
public class ClassTypeUtil {

    /**
     * WRAPPER_TO_PRIMITIVE_TYPES
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPES;
    /**
     * PRIMITIVE_TO_WRAPPER_TYPES
     */
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPES;

    static {
        WRAPPER_TO_PRIMITIVE_TYPES = new HashMap<>();
        WRAPPER_TO_PRIMITIVE_TYPES.put(Boolean.class, boolean.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Byte.class, byte.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Character.class, char.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Double.class, double.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Float.class, float.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Integer.class, int.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Short.class, short.class);
        WRAPPER_TO_PRIMITIVE_TYPES.put(Long.class, long.class);
        PRIMITIVE_TO_WRAPPER_TYPES = new HashMap<>();
        PRIMITIVE_TO_WRAPPER_TYPES.put(boolean.class, Boolean.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(byte.class, Byte.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(char.class, Character.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(double.class, Double.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(float.class, Float.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(int.class, Integer.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(short.class, Short.class);
        PRIMITIVE_TO_WRAPPER_TYPES.put(long.class, Long.class);
    }
    /**
     * 是否是包装类型
     * @param clazz
     * @return
     */
    public static boolean isWrapper(Class<?> clazz){
        return WRAPPER_TO_PRIMITIVE_TYPES.containsKey(clazz);
    }
    /**
     * 是否可以直接转换
     * @param clazz
     * @return
     */
    public static boolean couldDirectConvert(Class<?> clazz){
        List<Class<?>> classes = Arrays.asList(
                String.class,
                Date.class,
                LocalDate.class,
                LocalDateTime.class
        );
        return clazz.isPrimitive() || isWrapper(clazz) || classes.contains(clazz);
    }

    /**
     * 获取原始类型的包装对象
     * @return
     */
    public static Class<?> getWrapperType(Class<?> clazz){
        return PRIMITIVE_TO_WRAPPER_TYPES.get(clazz);
    }
    /**
     * 获取包装对象的原始类型
     * @return
     */
    public static Class<?> getPrimitiveType(Class<?> clazz){
        return clazz.isPrimitive() ? clazz : WRAPPER_TO_PRIMITIVE_TYPES.get(clazz);
    }

    /**
     * 是否有泛型
     * @param type
     * @return
     */
    public static boolean hasGenerics(Type type){
        return type instanceof ParameterizedType;
    }

    /**
     * 获取泛型列表
     * @param type
     * @return
     */
    public static Type[] getGenerics(Type type){
        return ((ParameterizedType)type).getActualTypeArguments();
    }


    /**
     * 获取自身class
     * @param type
     * @return
     */
    public static Class<?> getSelfClass(Type type){
        if(hasGenerics(type)){
            return (Class<?>) (((ParameterizedTypeImpl) type).getRawType());
        }else{
            return (Class<?>)type;
        }
    }
    public static String getKey(Type sourceClass, Type targetClass) {
        return sourceClass.getTypeName() + "->" +  targetClass.getTypeName();
    }

    public static Type wrapType(Class<?> rawType,
                                Class<?> ownerType,
                                Class<?> actualClassArgument){
        Class<?>[] actualTypeArguments = {actualClassArgument};
        return ParameterizedTypeImpl.make(rawType,
                actualTypeArguments,
                ownerType);
    }

    public static Class<?> hftCompile(String className, String javaCode) throws ClassNotFoundException {
        return CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
    }
}
