package io.github.lyrric.easymapstruct.util;


import net.openhft.compiler.CompilerUtils;

import java.lang.reflect.GenericArrayType;
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
     * @param type
     * @return
     */
    public static boolean couldDirectConvert(Type type){
        if (type instanceof Class) {
            Class<?> clazz = (Class<?>)type;
            List<Class<?>> classes = Arrays.asList(
                    String.class,
                    Date.class,
                    LocalDate.class,
                    LocalDateTime.class
            );
            return clazz.isPrimitive() || isWrapper(clazz) || classes.contains(clazz);
        }
        return false;
    }

//    public static boolean isCollection(final Type type){
//        return Collection.class.isAssignableFrom(getSelfClass(type));
//    }
//    public static boolean isArray(final Type type){
//        return getSelfClass(type).isArray();
//    }
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
     * 获取Type的定义代码
     * 基本对象返回如：io.github.User
     * 泛型ArrayList返回如：java.util.ArrayList<io.github.User>
     * 泛型HashSet返回如：java.util.hashSet<io.github.User>
     * 数组返回如：io.github.User[]
     * type.getCanonicalName()
     *     对于带有泛型的类来说，如List，会返回“java.util.List”，缺少了泛型信息。
     *     对于其他类型的类来说，是正常的。
     * type.getTypeName()
     *     对于带有泛型的类来说，会返回如“java.util.List<SourcePerson>”的字符串，也是我们需要的格式。
     *     对于内部类来说，会返回如“com.test.model.TargetPerson$SubTargetItem”格式，这个格式是JVM内部认可的格式，
     *          但是无法直接在代码中使用，代码中访问内部类必须是com.test.model.TargetPerson.SubTargetItem
     *  所以这里需要判断类型， 根据不同的类型，用不同的方法。
     * @param type
     * @return
     */
    public static String getClassDefinitionCode(Type type){
        if (type instanceof Class) {
            return ((Class<?>) type).getCanonicalName();
        } else if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType)type;
            return pType.getRawType().getTypeName()+"<"+
                    ((Class<?>)pType.getActualTypeArguments()[0]).getCanonicalName()+">";
        }else if (type instanceof GenericArrayType) {
            return ((GenericArrayType) type).getGenericComponentType().getTypeName();
        }else {
            throw new RuntimeException("error");
        }
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
     * 获取自身class
     * @param type
     * @return
     */
    public static Class<?> getSelfClass(Type type){
        if(hasGenerics(type)){
            return (Class<?>) (((ParameterizedType) type).getRawType());
        }else{
            return (Class<?>)type;
        }
    }
    public static String getKey(Type sourceClass, Type targetClass) {
        return sourceClass.getTypeName() + "->" +  targetClass.getTypeName();
    }


    static final Map<String, Class<?>> primitiveClassMap = new HashMap<>();

    static {
        primitiveClassMap.put("char", char.class);
        primitiveClassMap.put("byte", byte.class);
        primitiveClassMap.put("boolean", boolean.class);
        primitiveClassMap.put("short", short.class);
        primitiveClassMap.put("int", int.class);
        primitiveClassMap.put("long", long.class);
        primitiveClassMap.put("float", float.class);
        primitiveClassMap.put("double", double.class);
    }
    public static Class<?> getClassByName(String className) {
        return Optional.ofNullable(primitiveClassMap.get(className)).orElseGet(()-> {
            try {
                return (Class)Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Class<?> hftCompile(String className, String javaCode) throws ClassNotFoundException {
        return CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
    }
}
