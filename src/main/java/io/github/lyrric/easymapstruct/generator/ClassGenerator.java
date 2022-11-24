package io.github.lyrric.easymapstruct.generator;

import io.github.lyrric.easymapstruct.constant.Constant;
import io.github.lyrric.easymapstruct.conversion.ConversionUtil;
import io.github.lyrric.easymapstruct.model.generate.ClassInfo;
import io.github.lyrric.easymapstruct.model.generate.MethodInfo;
import io.github.lyrric.easymapstruct.util.ClassTypeUtil;
import io.github.lyrric.easymapstruct.util.JavaCodeFormattingUtil;
import lombok.extern.slf4j.Slf4j;

import javax.lang.model.element.Modifier;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
        generateMethod(Modifier.PUBLIC, sourceType, targetType);
        classInfo.addMethods(new ArrayList<>(methodsMap.values()));
        classInfo.setClassName(generateClassName());
        log.info("class generated, key:{}->{} , name:{}", sourceType.getTypeName(), targetType.getTypeName(), classInfo.getClassName());
        log.debug(JavaCodeFormattingUtil.tryFormat(classInfo.toJavaSourceString()));
        return classInfo;
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
        //source type is single object
        if (sourceType instanceof Class) {
            if (targetType instanceof Class) {
                if (((Class<?>) targetType).isArray() && ((Class<?>) sourceType).isArray()) {
                    //target type is array, convert array to array
                    return genArray2Array(modifier, (Class<?>) sourceType,(Class<?>)  targetType);
                }
                //single object convert to single object
                return genSingleObject2SingleObject(modifier, (Class<?>) sourceType, (Class<?>)targetType);
            }
        } else if (sourceType instanceof GenericArrayType) {
            //source type is generic array
            if (targetType instanceof GenericArrayType) {
                //target type is generic array
                return null;
            }
            if (targetType instanceof ParameterizedType
                    && Collection.class.isAssignableFrom(ClassTypeUtil.getSelfClass(targetType))) {
                //target type is collection, convert generic array to collection
                // TODO: 2022/11/18  convert generic array to collection
                return null;
            }
        }else if (sourceType instanceof ParameterizedType) {
            //source type is Parameterized Type
            Class<?> sourceClass = ClassTypeUtil.getSelfClass(sourceType);
            if (Collection.class.isAssignableFrom(sourceClass)) {
                //source type is collection
                if (targetType instanceof GenericArrayType) {
                    //target type is array, convert collection to array
                    // TODO: 2022/11/18  convert collection to array
                }
                if (targetType instanceof ParameterizedType
                        && Collection.class.isAssignableFrom(ClassTypeUtil.getSelfClass(targetType))) {
                    //target type is collection, convert collection to collection
                    return genCollection2Collection(modifier, (ParameterizedType) sourceType, (ParameterizedType)targetType);
                }
            }
        }
        return null;
    }

    private MethodInfo genSingleObject2SingleObject(final Modifier modifier,
                                              final Class<?> sourceClass,
                                              final Class<?> targetClass) {

        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetClass, methodName, sourceClass);
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
                        String convertCode = null;
                        if (ClassTypeUtil.couldDirectConvert(sourceFieldType) && ClassTypeUtil.couldDirectConvert(targetFieldType) ) {
                            convertCode = ConversionUtil.convert(sourceFieldType, targetFieldType, getterCode(sourceMethod));
                            if (convertCode != null) {
                                convertCode = setterCode(targetMethod, convertCode);
                            }
                        }else{
                            //复杂对象之间进行转换
                            //先判断是否已经生成过对应的转换
                            Optional<MethodInfo> generatedMethod = getGeneratedMethod(sourceFieldType, targetFieldType);
                            MethodInfo m = generatedMethod.orElse(generateMethod(Modifier.PRIVATE, sourceFieldType, targetFieldType));
                            if(m != null){
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
     * convert collection to collection
     * @param sourceType 源类型，e.g. List<S>
     * @param targetType 目标类型，e.g.List<T>
     * @return
     */
    private MethodInfo genCollection2Collection(final Modifier modifier,
                                                  final ParameterizedType sourceType,
                                                  final ParameterizedType targetType){
        //Type sourceGeneric = sourceType.getOwnerType();
        Type sourceGeneric = sourceType.getActualTypeArguments()[0];
        Type targetGeneric = targetType.getActualTypeArguments()[0];
        Class<?> sourceClass = (Class<?>)sourceType.getRawType();
        Class<?> targetClass = (Class<?>)targetType.getRawType();

        final String methodName = modifier == Modifier.PUBLIC ? "convert" : generateMethodName(sourceClass, targetClass);
        MethodInfo methodInfo = new MethodInfo(modifier, targetType, methodName, sourceType);
        methodInfo.addCode("if(source == null){return null;}");

        if(targetClass.isInterface()){
            //抽象类，如List、Set，无法直接new
            String newInstanceCode = null;
            if(List.class.isAssignableFrom(targetClass)){
                newInstanceCode = ClassTypeUtil.getClassDefinitionCode(targetType) + " target = new java.util.ArrayList<>();";
            }else if(Set.class.isAssignableFrom(targetClass)){
                newInstanceCode = ClassTypeUtil.getClassDefinitionCode(targetType) + " target = new java.util.HashSet<>();";
            }else {
                return null;
            }
            methodInfo.addCode(newInstanceCode);
        }else{
            methodInfo.addCode(ClassTypeUtil.getClassDefinitionCode(targetType) + " target = new " + ClassTypeUtil.getClassDefinitionCode(targetType) + "();");
        }
        methodInfo.addCode("for(" + ClassTypeUtil.getClassDefinitionCode(sourceGeneric) + " sub: source){");

        if(ClassTypeUtil.couldDirectConvert(sourceClass) && ClassTypeUtil.couldDirectConvert(targetClass)){
            //诸如integer，long，date，String类型的可以直接进行转换
            String convertCode = ConversionUtil.convert(sourceGeneric, targetGeneric, "sub");
            if(convertCode != null){
                methodInfo.addCode(convertCode.replace("<SET-METHOD-NAME>", "add"));
            }
        }else {
            //复杂类型
            MethodInfo m = generateMethod(Modifier.PRIVATE, sourceGeneric, targetGeneric);
            if(m != null){
                methodInfo.addCode("target.add(" + m.getMethodName() + "(sub));");
            }
        }
        methodInfo.addCode("}");
        methodInfo.addCode("return target;");
        saveMethod(methodInfo);
        return methodInfo;
    }


    public MethodInfo genArray2Array(final Modifier modifier,
                                     final Class<?> sourceType,
                                     final Class<?> targetType) {

        final String methodName = modifier == Modifier.PUBLIC ? "convert" :
                generateMethodName(sourceType, targetType);
        String sourceSelfClassName = sourceType.getTypeName().replace("[]", "");
        String targetSelfClassName = targetType.getTypeName().replace("[]", "");
        Class<?> sourceSelfClass = ClassTypeUtil.getClassByName(sourceSelfClassName);
        Class<?> targetSelfClass = ClassTypeUtil.getClassByName(targetSelfClassName);
        String convertCode = null;
        MethodInfo m = null;
        if (ClassTypeUtil.couldDirectConvert(sourceSelfClass) && ClassTypeUtil.couldDirectConvert(targetSelfClass)) {
            convertCode = ConversionUtil.convertArray(sourceSelfClass, targetSelfClass, "sub");
        }else{
            m = generateMethod(Modifier.PRIVATE, sourceSelfClass, targetSelfClass);
        }
        MethodInfo methodInfo = new MethodInfo(modifier, targetType, methodName, sourceType);
        methodInfo.addCode("if(source == null) return null;");
        methodInfo.addCode(ClassTypeUtil.getClassDefinitionCode(targetType) + " target=new " +
                ClassTypeUtil.getClassDefinitionCode(targetType).replace("[]","[source.length];"));
        methodInfo.addCode("for(int i=0;i<source.length;i++){");
        methodInfo.addCode(ClassTypeUtil.getClassDefinitionCode(sourceType).replace("[]", "") + " sub = source[i];");
        if (convertCode != null) {
            methodInfo.addCode(convertCode);
        } else if (m != null) {
            methodInfo.addCode("target[i]="+m.getMethodName()+"(sub);");
        }
        methodInfo.addCode("}");
        methodInfo.addCode("return target;");
        saveMethod(methodInfo);
        return methodInfo;


    }

    /**
     * 类名生成规则：目标类名+$Conversion+no，no从1开始
     *
     * @return
     */
    private String generateClassName() {
        return  "GeneratedConversion" + NO.getAndIncrement();
    }

    /**
     * 生成方法名称
     *
     * @param sourceClass
     * @param targetClass
     * @return
     */
    private String generateMethodName(Class<?> sourceClass, Class<?> targetClass) {
        String methodName = (sourceClass.getSimpleName() + "To" +
                targetClass.getSimpleName())
                .replaceAll("\\[]","");
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
