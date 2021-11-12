package io.github.lyrric.generator;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.conversion.ConversionUtil;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.model.generate.MethodInfo;
import io.github.lyrric.util.ClassTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangxiaodong
 */
public class ClassGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassGenerator.class);

    /**
     * 生成类的当前编号
     */
    private static final AtomicInteger NO = new AtomicInteger(1);


    /**
     * 保存已经生成过的source class to target class的方法，避免重复生成
     * key is source class, value is a map of (key is target class and value is MethodInfo) map
     */
    private final Map<Class<?>, Map<Class<?>, MethodInfo>> methodMap = new HashMap<>();
    /**
     * hash->MethodInfo
     * 其中hash是sources class的hash code相加，再加上target class的hash code
     */
    private final Map<String, MethodInfo> methodsMap = new HashMap<>();
    /**
     * 保存已生成的方法名称，避免重复
     */
    private final Set<String> methodNames = new HashSet<>();


    public ClassInfo convertObject(Type sourceType, Type targetType) {
        ClassInfo classInfo = new ClassInfo();
        Class<?> sourceClass = ClassTypeUtil.getClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getClass(targetType);
        if (ClassTypeUtil.couldDirectConvert(sourceClass) || ClassTypeUtil.couldDirectConvert(targetClass)) {
            //一个是基本数据类型，另一个不是基本数据类型，这种情况是错误的，并且无法进行转换
            LOGGER.error("class {} can not convert to {}", sourceClass.getName(), targetClass.getName());
            return null;
        }
        //普通对象之间进行转换
        genConvertObjectMethod(Modifier.PUBLIC, sourceClass, targetClass);
        for (Map<Class<?>, MethodInfo> value : methodMap.values()) {
            classInfo.addMethods(new ArrayList<>(value.values()));
        }
        classInfo.setClassName(generateClassName(targetClass.getSimpleName()));
        return classInfo;
    }

    /**
     * convertList
     * @param sourceClass 源类型，List<T>中的T
     * @param targetClass 目标类型，List<T>中的T
     * @return
     */
    public ClassInfo convertList(Class<?> sourceClass, Class<?> targetClass) {
        ClassInfo classInfo = new ClassInfo();

        for (Map<Class<?>, MethodInfo> value : methodMap.values()) {
            classInfo.addMethods(new ArrayList<>(value.values()));
        }
        classInfo.setClassName(generateClassName(targetClass.getSimpleName()));
        return classInfo;
    }

    private MethodInfo genConvertObjectMethod(final Modifier modifier,
                                              final Type sourceType,
                                              final Type targetType) {
        Class<?> sourceClass = ClassTypeUtil.getClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getClass(targetType);
        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        methodInfo.addCode(targetClass.getCanonicalName() + " target = new " + targetClass.getCanonicalName() + "();");
        //获取字段
        Method[] sourceMethods = sourceClass.getDeclaredMethods();
        Method[] targetMethods = targetClass.getDeclaredMethods();
        for (Method sourceMethod : sourceMethods) {
            String sourceMethodName = sourceMethod.getName();
            if (!sourceMethodName.startsWith("get") && !sourceMethodName.startsWith("is")) {
                continue;
            }
            String setterMethodName = sourceMethodName.startsWith("g") ?
                    sourceMethodName.replaceFirst("g", "s") :
                    //boolean类型getter方法可能是以is开头
                    sourceMethodName.replaceFirst("is", "set");
            Arrays.stream(targetMethods).filter(targetMethod -> targetMethod.getName().equals(setterMethodName))
                    .findFirst()
                    .ifPresent(targetMethod -> {
                        Class<?> sourceFieldClass = sourceMethod.getReturnType();
                        Class<?> targetFieldClass = targetMethod.getParameterTypes()[0];
                        String convertCode = ConversionUtil.convert(sourceFieldClass, targetFieldClass, getterCode(sourceMethod));

                        if(convertCode != null){
                            convertCode = setterCode(targetMethod, convertCode);
                        }
                        if(convertCode == null){
                            //直接转换失败，判断是不是复杂对象
                            if(!ClassTypeUtil.couldDirectConvert(sourceFieldClass)
                                    && !ClassTypeUtil.couldDirectConvert(targetFieldClass)){
                                //复杂对象之间进行转换
                                //先判断是否已经生成过对应的转换
                                Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceFieldClass, targetFieldClass);
                                MethodInfo m = generatedMethod.orElse(genConvertObjectMethod(Modifier.PRIVATE, sourceFieldClass, targetFieldClass));
                                convertCode = Constant.TARGET + "." + targetMethod + "(" + m.getMethodName() + ");";
                            }
                        }
                        if(convertCode != null){
                            methodInfo.addCode(convertCode);
                        }
                    });
        }
        methodInfo.addCode("return target;");
        return methodInfo;
    }


    /**
     * convertList
     * @param sourceType 源类型，List<T>中的T
     * @param targetType 目标类型，List<T>中的T
     * @return
     */
    private MethodInfo genConvertListMethod(final Modifier modifier,
                                   final Type sourceType,
                                   final Type targetType){
        Class<?> sourceClass = ClassTypeUtil.getClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getClass(targetType);
        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        methodInfo.addCode("if (source == null){return null;}");
        methodInfo.addCode(targetClass.getCanonicalName() + " target = new ArrayList<" + targetClass.getName() + ">();");
        methodInfo.addCode("for("+targetClass.getName()+" sub: source){");
        if(ClassTypeUtil.couldDirectConvert(sourceClass) && ClassTypeUtil.couldDirectConvert(targetClass)){
            //诸如integer，long，date，String类型的可以直接进行转换
            String convertCode = ConversionUtil.convert(sourceClass, targetClass, "sub");
            if(convertCode != null){
                methodInfo.addCode("target.add("+convertCode+");");
            }
        }else {
            //复杂类型
            MethodInfo m = generateMethod(sourceType, targetType);
            methodInfo.addCode("target.add("+m.getMethodName()+"(sub));");
        }

        methodInfo.addCode("}");
        methodInfo.addCode("return target;");
        return methodInfo;
    }


    /**
     * 未知的两个对象之间的转换
     * @param sourceType
     * @param targetType
     * @return
     */
    private MethodInfo generateMethod(final Type sourceType,
                                      final Type targetType){
        Class<?> sourceClass = ClassTypeUtil.getClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getClass(targetType);
        final String methodName = generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(Modifier.PRIVATE, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        if(List.class.isAssignableFrom(sourceClass) && List.class.isAssignableFrom(targetClass)){
            //List之间的转换,直接重新生成，避免
            return genConvertListMethod(Modifier.PRIVATE,
                    ClassTypeUtil.getGenerics(sourceType)[0],
                    ClassTypeUtil.getGenerics(targetType)[0]);
        }
        if(true){
            //普通对象之间的转换
            //先看看是否已经生成过转换方法
            return genConvertObjectMethod(Modifier.PRIVATE,
                    ClassTypeUtil.getGenerics(sourceType)[0],
                    ClassTypeUtil.getGenerics(targetType)[0]);
        }
        return null;
    }

    /**
     * 类名生成规则：目标类名+$Conversion+no，no从1开始
     *
     * @param className
     * @return
     */
    private String generateClassName(String className) {
        return className + "$Conversion" + NO.getAndIncrement();
    }

    /**
     * 生成方法名称
     *
     * @param sourceClass
     * @param targetClass
     * @return
     */
    private String generateMethodName(Class<?> sourceClass, Class<?> targetClass) {
        String methodName = sourceClass.getSimpleName() + "To" + targetClass.getSimpleName();
        //我就不信你这个方法能重名Integer.MAX_VALUE次
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if (!methodNames.contains(methodName)) {
                break;
            }
            methodName = methodName + i;
        }
        return methodName;
    }


    private Optional<MethodInfo> getGeneratedMethod(Class<?> sourceClass, Class<?> targetClass) {
        Map<Class<?>, MethodInfo> map = methodMap.get(sourceClass);
        if (map != null) {
            return Optional.of(map.get(targetClass));
        }
        return Optional.empty();
    }


    private void saveMethod(MethodInfo methodInfo) {
        Map<Class<?>, MethodInfo> map = methodMap.computeIfAbsent(methodInfo.getArgType(), k -> new HashMap<>());
        map.put(methodInfo.getReturnType(), methodInfo);
        methodNames.add(methodInfo.getMethodName());
    }

    private static String getterCode(Method sourceMethod){
        return Constant.SOURCE + "." + sourceMethod.getName() + "()";
    }

    private static String setterCode(Method targetMethod, String conversionCode){
        return Constant.TARGET + "." + targetMethod.getName() + "(" + conversionCode + ");";
    }

}
