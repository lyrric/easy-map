package io.github.lyrric.generator;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.conversion.ConversionUtil;
import io.github.lyrric.model.FieldConversionResult;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.model.generate.MethodInfo;
import io.github.lyrric.util.ClassTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
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
     * 保存已生成的方法名称，避免重复
     */
    private final Set<String> methodNames = new HashSet<>();
    /**
     * 动态引入的包
     */
    private final Set<String> importClasses = new HashSet<>();


    public ClassInfo generate(Class<?> sourceClass, Class<?> targetClass) {
        ClassInfo classInfo = new ClassInfo();
        if (ClassTypeUtil.couldDirectConvert(sourceClass) || ClassTypeUtil.couldDirectConvert(targetClass)) {
            //一个是基本数据类型，另一个不是基本数据类型，这种情况是错误的，并且无法进行转换
            // TODO: 2021/11/5 warning
            LOGGER.error("class {} can not convert to {}", sourceClass.getName(), targetClass.getName());
            return null;
        }
        convertSingle(Modifier.PUBLIC, sourceClass, targetClass);
        for (Map<Class<?>, MethodInfo> value : methodMap.values()) {
            classInfo.addMethods(new ArrayList<>(value.values()));
        }
        classInfo.setImportClasses(importClasses);
        classInfo.setClassName(generateClassName(targetClass.getSimpleName()));
        return classInfo;
    }

    private MethodInfo convertSingle(final Modifier modifier, final Class<?> sourceClass, final Class<?> targetClass) {
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
                        FieldConversionResult convertResult =  ConversionUtil.convert(sourceMethod, targetMethod);
                        if(convertResult == null){
                            //直接转换失败，判断是不是复杂对象
                            if(!ClassTypeUtil.couldDirectConvert(sourceFieldClass)
                                    && !ClassTypeUtil.couldDirectConvert(targetFieldClass)){
                                //复杂对象之间进行转换
                                //先判断是否已经生成过对应的转换
                                Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceFieldClass, targetFieldClass);
                                MethodInfo m = generatedMethod.orElse(convertSingle(Modifier.PRIVATE, sourceFieldClass, targetFieldClass));
                                convertResult = FieldConversionResult.ofSingleCode(Constant.TARGET + "." + targetMethod + "(" + m.getMethodName() + ");");
                            }
                        }
                        if(convertResult != null){
                            methodInfo.addCodes(convertResult.getCodes());
                            importClasses.addAll(convertResult.getImportLines());
                        }
                    });
        }
        methodInfo.addCode("return target;");
        return methodInfo;
    }

    /**
     * list to list
     * @return
     */
    public MethodInfo convertList(final Modifier modifier,
                                   final Class<?> sourceClass,
                                   final Class<?> targetClass){
        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        methodInfo.addCode("if (source == null){return null;}");
        methodInfo.addCode(targetClass.getCanonicalName() + " target = new ArrayList<" + targetClass.getName() + ">();");
        methodInfo.addCode("for("+targetClass.getName()+" sub: source){");
        MethodInfo m = getGeneratedMethod(sourceClass, targetClass).
                orElse(convertSingle(Modifier.PRIVATE, sourceClass, targetClass));
        methodInfo.addCode("target.add("+m.getMethodName()+"(sub));");
        methodInfo.addCode("}");
        methodInfo.addCode("return target;");
        return methodInfo;
    }

    /**
     * set to set
     * @return
     */
    private MethodInfo convertSet(final Modifier modifier, final Class<?> sourceClass, final Class<?> targetClass){
        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
        saveMethod(methodInfo);
        methodInfo.addCode(targetClass.getCanonicalName() + " target = new " + targetClass.getCanonicalName() + "();");

        return methodInfo;
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
}
