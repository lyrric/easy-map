package io.github.lyrric.util;



import net.openhft.compiler.CompilerUtils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * 是否是原始类型
     * @param clazz
     * @return
     */
//    public static boolean isPrimitive(Class<?> clazz){
//        return PRIMITIVE_TO_WRAPPER_TYPES.containsKey(clazz);
//    }
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
     * 获取包装对象的原始类型
     * @return
     */
    public static Class<?> getPrimitiveType(Class<?> clazz){
        return clazz.isPrimitive() ? clazz : WRAPPER_TO_PRIMITIVE_TYPES.get(clazz);
    }

    /**
     * 获取原始类型的包装对象
     * @return
     */
    public static Class<?> getWrapperType(Class<?> clazz){
        return PRIMITIVE_TO_WRAPPER_TYPES.get(clazz);
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
        //return ((ParameterizedTypeImpl) actualTypeArgument[0]).getActualTypeArguments();
    }


    /**
     * 获取自身class
     * @param type
     * @return
     */
    public static Class<?> getSelfClass(Type type){
        if(hasGenerics(type)){
            return ((ParameterizedTypeImpl) type).getRawType();
        }else{
            return (Class<?>)type;
        }
    }

    /**
     * getKey
     * @param type
     * @return
     */
    public static String getKey(Type type){
        return type.getTypeName();
//        if(hasGenerics(type)){
//            return type.getTypeName();
//            StringBuilder name = new StringBuilder(getSelfClass(type).getName());
//            Type[] generics = getGenerics(type);
//            for (Type generic : generics) {
//                name.append(",").append(getKey(generic));
//            }
//            return name.toString();
//        }else{
//            return ((Class<?>) type).getName();
//        }
    }

    public static Type wrapType(Class<?> rawType,
                                Class<?> ownerType,
                                Class<?> actualClassArgument){
        Class<?>[] actualTypeArguments = {actualClassArgument};
        return ParameterizedTypeImpl.make(rawType,
                actualTypeArguments,
                ownerType);
    }

    public static byte[] compile(String fileName, String javaCode) throws IOException {
        Map<String, byte[]> results = new HashMap<>();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null,
                null, null);
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, javaCode);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null,
                    Collections.singletonList("-implicit:none"), null, Collections.singletonList(javaFileObject));
            if (task.call()) {
                results = manager.getClassBytes();
            }
        }
        return new ArrayList<>(results.values()).get(0);
    }

    public static void hftCompile(String className, String javaCode) throws ClassNotFoundException {
        Class<?> aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
    }
}
