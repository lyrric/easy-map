package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;
import io.github.lyrric.util.ClassTypeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 原始类型转成包装类型，列如int->integer
 * @author wangxiaodong
 */
public class PrimitiveToWrapperConversion extends BaseConversion{


    public PrimitiveToWrapperConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public String getConversionCode(){
        Class<?> targetType = ClassTypeUtil.getPrimitiveType(targetClass);
        return "(" + targetType.getName() + ") <SOURCE>";
    }

    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>(){{
            put(new DoubleKey(byte.class, Byte.class), new PrimitiveToWrapperConversion(byte.class, Byte.class));
            put( new DoubleKey(byte.class, Short.class), new PrimitiveToWrapperConversion(byte.class, Short.class));
            put( new DoubleKey(byte.class, Integer.class), new PrimitiveToWrapperConversion(byte.class, Integer.class) );
            put( new DoubleKey(byte.class, Long.class), new PrimitiveToWrapperConversion(byte.class, Long.class ));
            put( new DoubleKey(byte.class, Float.class), new PrimitiveToWrapperConversion(byte.class, Float.class));
            put( new DoubleKey(byte.class, Double.class), new PrimitiveToWrapperConversion(byte.class, Double.class));

            put( new DoubleKey(short.class, Short.class), new PrimitiveToWrapperConversion(short.class, Short.class));
            put( new DoubleKey(short.class, Integer.class), new PrimitiveToWrapperConversion(short.class, Integer.class));
            put( new DoubleKey(short.class, Long.class), new PrimitiveToWrapperConversion(short.class, Long.class));
            put( new DoubleKey(short.class, Float.class), new PrimitiveToWrapperConversion(short.class, Float.class));
            put( new DoubleKey(short.class, Double.class), new PrimitiveToWrapperConversion(short.class, Double.class));

            put( new DoubleKey(int.class, Integer.class), new PrimitiveToWrapperConversion(int.class, Integer.class));
            put( new DoubleKey(int.class, Long.class ), new PrimitiveToWrapperConversion(int.class, Long.class));
            put( new DoubleKey(int.class, Float.class ), new PrimitiveToWrapperConversion(int.class, Float.class));
            put( new DoubleKey(int.class, Double.class ), new PrimitiveToWrapperConversion(int.class, Double.class));

            put( new DoubleKey(long.class, Long.class ), new PrimitiveToWrapperConversion(long.class, Long.class));
            put( new DoubleKey(long.class, Integer.class ), new PrimitiveToWrapperConversion(long.class, Integer.class));
            put( new DoubleKey(long.class, Float.class ), new PrimitiveToWrapperConversion(long.class, Float.class ));
            put( new DoubleKey(long.class, Double.class ), new PrimitiveToWrapperConversion(long.class, Double.class));

            put( new DoubleKey(float.class, Float.class ), new PrimitiveToWrapperConversion(float.class, Float.class ));
            put( new DoubleKey(float.class, Double.class ), new PrimitiveToWrapperConversion(float.class, Double.class));

            put( new DoubleKey(double.class, Double.class ), new PrimitiveToWrapperConversion(double.class, Double.class));
            put( new DoubleKey(double.class, Float.class ), new PrimitiveToWrapperConversion(double.class, Float.class));
            put( new DoubleKey(boolean.class, Boolean.class ), new PrimitiveToWrapperConversion(boolean.class, Boolean.class ));
            put( new DoubleKey(char.class, Character.class ), new PrimitiveToWrapperConversion(char.class, Character.class ));
        }};
    }
}
