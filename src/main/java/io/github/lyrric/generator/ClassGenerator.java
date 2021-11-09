package io.github.lyrric.generator;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.conversion.ConversionFactory;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.model.generate.MethodInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


//    private final Class<?> sourceClass;
//    private final Class<?> targetClass;
//

    /**
     * 保存已经生成过的source class to target class的方法，避免重复生成
     * key is source class, value is a map of (key is target class and value is MethodInfo) map
     */
    private Map<Class<?>, Map<Class<?>, MethodInfo>> methodMap = new HashMap<>();
    /**
     * 保存已生成的方法名称，避免重复
     */
    private Set<String> methodNames = new HashSet<>();
    /**
     * 动态引入的包
     */
    private Set<String> importClasses = new HashSet<>();



    public ClassInfo generate(Class<?> sourceClass, Class<?> targetClass){
        ClassInfo classInfo = new ClassInfo();
        if(couldDirectConvert(sourceClass) || couldDirectConvert(targetClass)){
            //一个是基本数据类型，另一个不是基本数据类型，这种情况是错误的，并且无法进行转换
            // TODO: 2021/11/5 warning
            LOGGER.error("class {} can not convert to {}",sourceClass.getName(), targetClass.getName());
            return null;
        }
        generateMethod(Modifier.PUBLIC, sourceClass, targetClass);
        for (Map<Class<?>, MethodInfo> value : methodMap.values()) {
            classInfo.addMethods(new ArrayList<>(value.values()));
        }
        classInfo.setImportClasses(importClasses);
        classInfo.setClassName(generateClassName(targetClass.getSimpleName()));
        return classInfo;
    }

    private MethodInfo generateMethod(Modifier modifier, Class<?> sourceClass, Class<?> targetClass){
        String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        methodInfo.addCode(targetClass.getCanonicalName() + " target = new " + targetClass.getCanonicalName() + "();");
        //获取字段
        Method[] sourceMethods = sourceClass.getDeclaredMethods();
        Method[] targetMethods = targetClass.getDeclaredMethods();
        for (Method sourceMethod : sourceMethods) {
            String sourceMethodName = sourceMethod.getName();
            if (sourceMethodName.startsWith("get") || sourceMethodName.startsWith("is")) {
                String setterMethodName = sourceMethodName.startsWith("g") ?
                        sourceMethodName.replaceFirst("g", "s") :
                        //boolean类型getter方法可能是以is开头
                        sourceMethodName.replaceFirst("is", "set");
                Arrays.stream(targetMethods).filter(targetMethod -> targetMethod.getName().equals(setterMethodName))
                        .findFirst()
                        .ifPresent(targetMethod -> {
                            Class<?> sourceFieldClass = sourceMethod.getReturnType();
                            Class<?> targetFieldClass = targetMethod.getParameterTypes()[0];
                            if (couldDirectConvert(sourceFieldClass) && couldDirectConvert(targetFieldClass)) {
                                //基本数据类型之间可以直接进行转换
                                ConversionFactory.getConversion(targetFieldClass)
                                        .map(conversion->conversion.convert(sourceMethod, targetMethod))
                                        .ifPresent(fieldConversionResult -> {
                                            methodInfo.addCodes(fieldConversionResult.getCodes());
                                            importClasses.addAll(fieldConversionResult.getImportLines());
                                        });
                            }else //到这里其中一个必然不是基本数据类型
                                if (couldDirectConvert(sourceFieldClass) || couldDirectConvert(targetFieldClass)) {
                                //如果一个是基本数据类型，另一个不是基本数据类型则无法进行转换
                                LOGGER.warn("class {} can not convert to {}",sourceClass.getName(), targetClass.getName());
                            }else{
                                    //复杂对象之间进行转换
                                    //先判断是否已经生成过对应的转换
                                    Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceClass, targetClass);
                                    MethodInfo m = generatedMethod.orElse(generateMethod(Modifier.PRIVATE, sourceFieldClass, targetFieldClass));
                                    methodInfo.addCode(Constant.TARGET + "." + targetMethod + "(" + m.getMethodName()+");");
                            }

                        });

            }
        }
        methodInfo.addCode("return target;");
        return methodInfo;
    }

    /**
     * 类名生成规则：目标类名+$Conversion+no，no从1开始
     * @param className
     * @return
     */
    private String generateClassName(String className){
        return className + "$Conversion" + NO.getAndIncrement();
    }

    /**
     * 生成方法名称
     * @param sourceClass
     * @param targetClass
     * @return
     */
    private String generateMethodName(Class<?> sourceClass, Class<?> targetClass){
        String methodName = sourceClass.getSimpleName() + "To" + targetClass.getSimpleName();
        //我就不信你这个方法能重名Integer.MAX_VALUE次
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            if(!methodNames.contains(methodName)){
                break;
            }
            methodName = methodName + i;
        }
        return methodName;
    }



    private Optional<MethodInfo> getGeneratedMethod(Class<?> sourceClass, Class<?> targetClass){
        Map<Class<?>, MethodInfo> map = methodMap.get(sourceClass);
        if(map != null){
            return Optional.of(map.get(targetClass));
        }
        return Optional.empty();
    }


    private void saveMethod(MethodInfo methodInfo){
        Map<Class<?>, MethodInfo> map = methodMap.computeIfAbsent(methodInfo.getArgType(), k -> new HashMap<>());
        map.put(methodInfo.getReturnType(), methodInfo);
        methodNames.add(methodInfo.getMethodName());
    }
}
