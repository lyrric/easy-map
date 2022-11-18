package io.github.lyrric.easymapstruct.conversion;

import io.github.lyrric.easymapstruct.constant.Constant;
import io.github.lyrric.easymapstruct.util.ClassTypeUtil;

import java.lang.reflect.Type;

/**
 * @author wangxiaodong
 */
public class ConversionUtil {


    public static String convertArray(Class<?> sourceClass, Class<?>  targetClass, String source){
        String conversionCode;
        if (sourceClass.equals(targetClass)) {
            conversionCode = source;
        } else {
            conversionCode = ConversionFactory.getConversion(sourceClass, targetClass)
                    .map(BaseConversion::getConversionCode).orElse(null);
        }
        if (conversionCode == null) {
            return null;
        }
        conversionCode = Constant.TARGET + "[i]=" + conversionCode + ";";

        if (!sourceClass.isPrimitive() && !sourceClass.equals(targetClass)) {
            conversionCode = "if("+source+" != null){" +
                    conversionCode +
                    "}";
        }
        conversionCode = conversionCode.replace("<SOURCE>", source);
        return conversionCode;
    }


    public static String convert(Type sourceType, Type targetType, String source){
        Class<?> sourceFieldClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetFieldClass = ClassTypeUtil.getSelfClass(targetType);
        String conversionCode;
        if (sourceType.equals(targetType)) {
            conversionCode = Constant.TARGET + ".<SET-METHOD-NAME>(<SOURCE>);";
        } else {
            conversionCode = ConversionFactory.getConversion(sourceFieldClass, targetFieldClass)
                    .map(BaseConversion::getConversionCode).orElse(null);
            if (conversionCode != null) {
                conversionCode = Constant.TARGET + ".<SET-METHOD-NAME>(" + conversionCode + ");";
                //诸如Long->long这种转换时需要判断是否为null
                if (!sourceFieldClass.isPrimitive() ) {
                    conversionCode = "if(<SOURCE> != null){" +
                            conversionCode +
                            "}";
                }
            }
        }

        if(conversionCode != null){
            conversionCode = conversionCode.replace("<SOURCE>",source);
        }
        return conversionCode;
    }

    public static String convertToArray(Type sourceType, Type targetType, String source, int index) {
        Class<?> sourceFieldClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetFieldClass = ClassTypeUtil.getSelfClass(targetType);
        String conversionCode;
        if (sourceType.equals(targetType)) {
            conversionCode = Constant.TARGET + "[" + index + "]=<SOURCE>;";
        } else {
            conversionCode = ConversionFactory.getConversion(sourceFieldClass, targetFieldClass)
                    .map(BaseConversion::getConversionCode).orElse(null);
            if (conversionCode != null) {
                conversionCode = Constant.TARGET + "[" + index + "]=" + conversionCode;
                //诸如Long->long这种转换时需要判断是否为null
                if (!sourceFieldClass.isPrimitive() ) {
                    conversionCode = "if(<SOURCE> != null){" +
                            conversionCode +
                            "}";
                }
            }
        }

        if(conversionCode != null){
            conversionCode = conversionCode.replace("<SOURCE>",source);
        }
        return conversionCode;
    }


}
