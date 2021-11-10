package io.github.lyrric.conversion;

import io.github.lyrric.constant.Constant;
import io.github.lyrric.model.FieldConversionResult;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author wangxiaodong
 */
public class ConversionUtil {


    public static FieldConversionResult convert(Method sourceMethod, Method targetMethod){
        Class<?> sourceClass = sourceMethod.getReturnType();
        Class<?> targetClass = targetMethod.getParameterTypes()[0];

        Optional<String> conversionCode;

        if(sourceClass == targetClass){
            conversionCode = Optional.of("<SOURCE>");
        }else{
            conversionCode = ConversionFactory.getConversion(sourceClass, targetClass)
                    .map(BaseConversion::getConversionCode);
        }
        conversionCode = conversionCode.map(str -> str.replace("<SOURCE>", getterCode(sourceMethod)))
                .map(str -> setterCode(targetMethod, str));

        if(!sourceClass.isPrimitive() && conversionCode.isPresent()){
            return FieldConversionResult.ofSingleCode(conversionCode.get()).addCheckNull(getterCode(sourceMethod));
        }
        return null;
    }


    private static String getterCode(Method sourceMethod){
        return Constant.SOURCE + "." + sourceMethod.getName() + "()";
    }

    private static String setterCode(Method targetMethod, String conversionCode){
        return Constant.TARGET + "." + targetMethod.getName() + "(" + conversionCode + ");";
    }

}
