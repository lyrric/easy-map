package io.github.lyrric.easymapstruct;

import io.github.lyrric.easymapstruct.generator.ClassGenerator;
import io.github.lyrric.easymapstruct.model.generate.ClassInfo;
import io.github.lyrric.easymapstruct.util.ClassTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangxiaodong
 */
@Slf4j
public class EasyMapStruct {

    private static final Map<String, ClassInfo> instanceMap = new ConcurrentHashMap<>();



    public static <T> T mapSingleton(Object source, Class<T> targetClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(source == null){
            return null;
        }
        //放一亿个心，不会报错
        @SuppressWarnings("unchecked")
        T result = (T) map(source.getClass(), targetClass, source);
        return result;
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) throws  NoSuchMethodException,  IllegalAccessException, InvocationTargetException {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
        Type source = ClassTypeUtil.wrapType(List.class, null, sourceList.get(0).getClass());
        Type target = ClassTypeUtil.wrapType(List.class, null, targetClass);
        @SuppressWarnings("unchecked")
        //放一亿个心，不会报错
        List<T> result = (List<T>) map(source, target, sourceList);
        return result;
    }

    public static <T> Set<T> mapSet(Set<?> sourceSet, Class<T> targetClass) throws  NoSuchMethodException,  IllegalAccessException, InvocationTargetException {
        if (sourceSet == null || sourceSet.size() == 0) {
            return Collections.emptySet();
        }
        Type source = ClassTypeUtil.wrapType(Set.class, null, sourceSet.stream().findFirst().get().getClass());
        Type target = ClassTypeUtil.wrapType(Set.class, null, targetClass);
        @SuppressWarnings("unchecked")
        //放一亿个心，不会报错
        Set<T> result = (Set<T>) map(source, target, sourceSet);
        return result;
    }

    private static Object map(Type source, Type target, Object sourceData) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ClassInfo classInfo = getClassInfo(source, target);
        Object instance = classInfo.getInstance();
        Method method = instance.getClass().getMethod("convert", ClassTypeUtil.getSelfClass(source));
        return method.invoke(instance, sourceData);
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
}
