package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;
import io.github.lyrric.util.ClassTypeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxiaodong
 */
public class StringToPrimitiveConversion extends BaseConversion {


    public StringToPrimitiveConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public String getConversionCode() {
        Class<?> wrapperClass = targetClass;
        if(targetClass.isPrimitive()){
            wrapperClass = ClassTypeUtil.getWrapperType(targetClass);
        }
        if(wrapperClass.equals(Character.class)){
            return "new Character(<SOURCE>.charAt(1))";
        }else if(wrapperClass.equals(Integer.class)){
            return "Integer.parseInt(<SOURCE>)";
        }else{
            return String.format("%s.parse%s(<SOURCE>)",  wrapperClass.getSimpleName(), wrapperClass.getSimpleName());
        }
    }


    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>(){{
            put( new DoubleKey(String.class, char.class), new StringToPrimitiveConversion(String.class, char.class));
            put( new DoubleKey(String.class, Character.class), new StringToPrimitiveConversion(String.class, Character.class));
            put( new DoubleKey(String.class, byte.class), new StringToPrimitiveConversion(String.class, byte.class));
            put( new DoubleKey(String.class, Byte.class), new StringToPrimitiveConversion(String.class, Byte.class));
            put( new DoubleKey(String.class, boolean.class), new StringToPrimitiveConversion(String.class, boolean.class));
            put( new DoubleKey(String.class, Boolean.class), new StringToPrimitiveConversion(String.class, Boolean.class));
            put( new DoubleKey(String.class, short.class), new StringToPrimitiveConversion(String.class, short.class));
            put( new DoubleKey(String.class, Short.class), new StringToPrimitiveConversion(String.class, Short.class));
            put( new DoubleKey(String.class, int.class), new StringToPrimitiveConversion(String.class, int.class) );
            put( new DoubleKey(String.class, Integer.class), new StringToPrimitiveConversion(String.class, Integer.class) );
            put( new DoubleKey(String.class, long.class), new StringToPrimitiveConversion(String.class, long.class ));
            put( new DoubleKey(String.class, Long.class), new StringToPrimitiveConversion(String.class, Long.class ));
            put( new DoubleKey(String.class, float.class), new StringToPrimitiveConversion(String.class, float.class));
            put( new DoubleKey(String.class, Float.class), new StringToPrimitiveConversion(String.class, Float.class));
            put( new DoubleKey(String.class, double.class), new StringToPrimitiveConversion(String.class, double.class));
            put( new DoubleKey(String.class, Double.class), new StringToPrimitiveConversion(String.class, Double.class));

        }};
    }



}