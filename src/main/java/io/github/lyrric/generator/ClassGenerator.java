package io.github.lyrric.generator;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.conversion.ConversionUtil;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.model.generate.MethodInfo;
import io.github.lyrric.util.ClassTypeUtil;
import io.github.lyrric.util.JavaCodeFormattingUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ClassGenerator {

    /**
     * 生成类的当前编号
     */
    private static final AtomicInteger NO = new AtomicInteger(1);

    /**
     * 保存已经生成过的source type to target type，避免重复生成
     * string->MethodInfo
     * 其中key是sources type的name相加，再加上target type的name
     */
    private final Map<String, MethodInfo> methodsMap = new HashMap<>();
    /**
     * 保存已生成的方法名称，避免重复
     */
    private final Set<String> methodNames = new HashSet<>();


    public ClassInfo convertObject(Type sourceType, Type targetType) {
        ClassInfo classInfo = new ClassInfo();
        Class<?> targetClass = ClassTypeUtil.getSelfClass(targetType);
        generateMethod(Modifier.PUBLIC, sourceType, targetType);
        classInfo.addMethods(new ArrayList<>(methodsMap.values()));
        classInfo.setClassName(generateClassName(targetClass.getSimpleName()));
        log.info("generated class, key:{}->{} , name:{}", sourceType.getTypeName(), targetType.getTypeName(), classInfo.getClassName());
        log.debug(JavaCodeFormattingUtil.tryFormat(classInfo.toJavaSourceString()));
        return classInfo;
    }


    private MethodInfo genConvertObjectMethod(final Modifier modifier,
                                              final Type sourceType,
                                              final Type targetType) {
        Class<?> sourceClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getSelfClass(targetType);
        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetType, methodName, sourceType);
        saveMethod(methodInfo);
        methodInfo.addCode("if(source == null) return null;");
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
                        Type sourceFieldType = sourceMethod.getGenericReturnType();
                        Type targetFieldType = targetMethod.getGenericParameterTypes()[0];
                        Class<?> sourceFieldClass = ClassTypeUtil.getSelfClass(sourceFieldType);
                        Class<?> targetFieldClass = ClassTypeUtil.getSelfClass(targetFieldType);
                        String convertCode = ConversionUtil.convert(sourceFieldType, targetFieldType, getterCode(sourceMethod));

                        if(convertCode != null){
                            convertCode = setterCode(targetMethod, convertCode);
                        }
                        if(convertCode == null){
                            //直接转换失败，判断是不是复杂对象
                            if(!ClassTypeUtil.couldDirectConvert(sourceFieldClass)
                                    && !ClassTypeUtil.couldDirectConvert(targetFieldClass)){
                                //复杂对象之间进行转换
                                //先判断是否已经生成过对应的转换
                                Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceFieldType, targetFieldType);
                                MethodInfo m = generatedMethod.orElse(generateMethod(Modifier.PRIVATE, sourceFieldType, targetFieldType));
                                convertCode = Constant.TARGET + "." + targetMethod.getName() + "(" + m.getMethodName()+"("+getterCode(sourceMethod)+"));";
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
     * @param sourceType 源类型，List<S>
     * @param targetType 目标类型，List<T>
     * @return
     */
    private MethodInfo genConvertListMethod(final Modifier modifier,
                                   final Type sourceType,
                                   final Type targetType){
        Type sourceGeneric = ClassTypeUtil.getGenerics(sourceType)[0];
        Type targetGeneric = ClassTypeUtil.getGenerics(targetType)[0];
        Class<?> sourceClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getSelfClass(targetType);

        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetType, methodName, sourceType);
        saveMethod(methodInfo);
        methodInfo.addCode("if (source == null){return null;}");
        if(targetClass.isInterface()){
            methodInfo.addCode(targetType.getTypeName()+" target = new java.util.ArrayList<>();");
        }else{
            methodInfo.addCode(targetType.getTypeName()+" target = new " + targetType.getTypeName() + "();");
        }

        methodInfo.addCode("for(" + sourceGeneric.getTypeName() + " sub: source){");
        if(ClassTypeUtil.couldDirectConvert(sourceClass) && ClassTypeUtil.couldDirectConvert(targetClass)){
            //诸如integer，long，date，String类型的可以直接进行转换
            String convertCode = ConversionUtil.convert(sourceGeneric, targetGeneric, "sub");
            if(convertCode != null){
                methodInfo.addCode(convertCode.replace("<SET-METHOD-NAME>", "add"));
            }
        }else {
            //复杂类型
            MethodInfo m = generateMethod(Modifier.PRIVATE, sourceGeneric, targetGeneric);
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
    private MethodInfo generateMethod(final Modifier modifier,
                                      final Type sourceType,
                                      final Type targetType){
        //已经生成过了，直接返回
        Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceType, targetType);
        if(generatedMethod.isPresent()) {
            return generatedMethod.get();
        }
        //获取原始类型
        Class<?> sourceClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetClass = ClassTypeUtil.getSelfClass(targetType);
        if (ClassTypeUtil.couldDirectConvert(sourceClass) || ClassTypeUtil.couldDirectConvert(targetClass)) {
            //一个是基本数据类型，另一个不是基本数据类型，这种情况是错误的，并且无法进行转换
            log.error("class {} can not convert to {}", sourceClass.getName(), targetClass.getName());
            return null;
        }
        if(List.class.isAssignableFrom(sourceClass) && List.class.isAssignableFrom(targetClass)){
            //List之间的转换,直接重新生成，避免
            return genConvertListMethod(modifier,
                    sourceType, targetType);
        }
        //普通对象之间的转换
        return genConvertObjectMethod(modifier,
                sourceType,
                targetType);
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


    private Optional<MethodInfo> getGeneratedMethod(Type sourceType, Type targetType) {
        String key = ClassTypeUtil.getKey(sourceType, targetType);
        return Optional.ofNullable(methodsMap.get(key));
    }

    private void saveMethod(MethodInfo methodInfo) {
        String key = ClassTypeUtil.getKey(methodInfo.getArgType(), methodInfo.getReturnType());
        methodsMap.put(key, methodInfo);
        methodNames.add(methodInfo.getMethodName());
    }



    private String getterCode(Method sourceMethod){
        return Constant.SOURCE + "." + sourceMethod.getName() + "()";
    }

    private String setterCode(Method targetMethod, String conversionCode){
        return conversionCode.replace("<SET-METHOD-NAME>",targetMethod.getName());
    }

}
