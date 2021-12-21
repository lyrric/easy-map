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


}
