package io.github.lyrric.conversion;

import io.github.lyrric.constant.Constant;

import java.lang.reflect.Method;

/**
 * @author wangxiaodong
 */
public class ConversionUtil {


    public static String convert( Class<?> sourceClass, Class<?> targetClass, String source){
        String conversionCode;
        if(sourceClass == targetClass){
            conversionCode =  "<SOURCE>";
        }else{
            conversionCode =  ConversionFactory.getConversion(sourceClass, targetClass)
                    .map(BaseConversion::getConversionCode).orElse(null);
        }
        if(conversionCode != null){
            conversionCode = conversionCode.replace("<SOURCE>",source);
        }
        return conversionCode;
    }




}
