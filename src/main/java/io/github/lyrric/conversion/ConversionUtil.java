package io.github.lyrric.conversion;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.util.ClassTypeUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author wangxiaodong
 */
public class ConversionUtil {


    public static String convert(Type sourceType, Type targetType, String source){
        Class<?> sourceFieldClass = ClassTypeUtil.getSelfClass(sourceType);
        Class<?> targetFieldClass = ClassTypeUtil.getSelfClass(targetType);
        String conversionCode;
        if (sourceType.equals(targetType)) {
            conversionCode = "<SOURCE>";
        } else {
            conversionCode = ConversionFactory.getConversion(sourceFieldClass, targetFieldClass)
                    .map(BaseConversion::getConversionCode).orElse(null);
        }
        if(conversionCode != null){
            conversionCode = conversionCode.replace("<SOURCE>",source);
        }
        return conversionCode;
    }




}
