package io.github.lyrric.easymapstruct;

import io.github.lyrric.easymapstruct.generator.ClassGenerator;
import io.github.lyrric.easymapstruct.model.GenericArrayTypeImpl;
import io.github.lyrric.easymapstruct.model.ParameterizedTypeImpl;
import io.github.lyrric.easymapstruct.model.generate.ClassInfo;
import io.github.lyrric.easymapstruct.util.ClassTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangxiaodong
 */
@Slf4j
public class EasyMapStruct {

    private static final Map<String, ClassInfo> instanceMap = new ConcurrentHashMap<>();



    public static <T> T mapSingleton(Object source, Class<T> targetClass) {
        if(source == null){
            return null;
        }
        //放一亿个心，不会报错
        @SuppressWarnings("unchecked")
        T result = (T) map(source.getClass(), targetClass, source);
        return result;
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass)  {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
        Type sourceType = ParameterizedTypeImpl.make(List.class, null, sourceList.get(0).getClass());
        Type targetType = ParameterizedTypeImpl.make(List.class, null, targetClass);
        @SuppressWarnings("unchecked")
        //放一亿个心，不会报错
        List<T> result = (List<T>) map(sourceType, targetType, sourceList);
        return result;
    }
    public static <T> List<T> mapList(Object[] sourceArray, Class<T> targetClass)  {
        return null;
    }
    public static <T> Set<T> mapSet(Set<?> sourceSet, Class<T> targetClass)  {
        if (sourceSet == null || sourceSet.size() == 0) {
            return Collections.emptySet();
        }
        Type sourceType = ParameterizedTypeImpl.make(Set.class, null, sourceSet.stream().findFirst().get().getClass());
        Type targetType = ParameterizedTypeImpl.make(Set.class, null, targetClass);
        @SuppressWarnings("unchecked")
        //放一亿个心，不会报错
        Set<T> result = (Set<T>) map(sourceType, targetType, sourceSet);
        return result;
    }

    public static <T> T[] mapArray(Object[] sourceArray, Class<T> targetClass){
        if(sourceArray == null ) return null;
        if(sourceArray.length == 0) return (T[]) new Object[0];
        String targetArrayClassName = "[L" + targetClass.getTypeName() + ";";
        try {
            Class<?> targetArrayClass = Class.forName(targetArrayClassName);
            GenericArrayTypeImpl sourceType = GenericArrayTypeImpl.make(sourceArray.getClass());
            GenericArrayTypeImpl targetType = GenericArrayTypeImpl.make(targetArrayClass);
            @SuppressWarnings("unchecked")
            T[] result = (T[]) map(sourceType, targetType, sourceArray);
            return result;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object map(Type source, Type target, Object sourceData) {
        ClassInfo classInfo = getClassInfo(source, target);
        Object instance = classInfo.getInstance();
        try {
            Method method = findMethod(instance.getClass());
            return method.invoke(instance, sourceData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static ClassInfo getClassInfo(Type source, Type target) {
        String key = ClassTypeUtil.getKey(source, target);
        return instanceMap.computeIfAbsent(key, k->{
            ClassInfo classInfo = new ClassGenerator().convertObject(
                    source, target);
            String javaSourceString = classInfo.toJavaSourceString();
            try {
                long start = System.currentTimeMillis();
                Class<?> clazz = ClassTypeUtil.hftCompile(classInfo.getPackageStr()+"."+classInfo.getClassName(), javaSourceString);
                classInfo.setClazz(clazz);
                long end = System.currentTimeMillis();
                log.info("key:{}, class name: {} 编译耗时：{}", key, classInfo.getClassName(), (end - start));
                Object instance = clazz.newInstance();
                classInfo.setInstance(instance);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                //我生成的代码有啥东西我还不知道吗，抛你大爷的异常
                String errMsg = String.format("generate class exception, source type: %s, target type: %s, error message: %s", source, target, e.getMessage());
                log.error(errMsg);
                throw new GenerateClassException(errMsg);
            }
            return classInfo;
        });
    }

    static private Method findMethod(Class<?> clazz){
        return Arrays.stream(clazz.getDeclaredMethods()).filter(m -> "convert".equals(m.getName())).findFirst().get();
    }
}
