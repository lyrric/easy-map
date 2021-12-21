package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxiaodong
 */
public class BasicToStringConversion extends BaseConversion{


    public BasicToStringConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public String getConversionCode() {
        return "String.valueOf(<SOURCE>)";
    }

    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>() {{
            put(new DoubleKey(Byte.class, String.class), new BasicToStringConversion(Byte.class, String.class));
            put(new DoubleKey(byte.class, String.class), new BasicToStringConversion(byte.class, String.class));
            put(new DoubleKey(Boolean.class, String.class), new BasicToStringConversion(Boolean.class, String.class));
            put(new DoubleKey(boolean.class, String.class), new BasicToStringConversion(boolean.class, String.class));
            put(new DoubleKey(Short.class, String.class), new BasicToStringConversion(Short.class, String.class));
            put(new DoubleKey(short.class, String.class), new BasicToStringConversion(short.class, String.class));
            put(new DoubleKey(Integer.class, String.class), new BasicToStringConversion(Integer.class, String.class));
            put(new DoubleKey(int.class, String.class), new BasicToStringConversion(int.class, String.class));
            put(new DoubleKey(Long.class, String.class), new BasicToStringConversion(Long.class, String.class));
            put(new DoubleKey(long.class, String.class), new BasicToStringConversion(long.class, String.class));
            put(new DoubleKey(Float.class, String.class), new BasicToStringConversion(Float.class, String.class));
            put(new DoubleKey(float.class, String.class), new BasicToStringConversion(float.class, String.class));
            put(new DoubleKey(Double.class, String.class), new BasicToStringConversion(Double.class, String.class));
            put(new DoubleKey(double.class, String.class), new BasicToStringConversion(double.class, String.class));
            put(new DoubleKey(Character.class, String.class), new BasicToStringConversion(Character.class, String.class));
            put(new DoubleKey(char.class, String.class), new BasicToStringConversion(char.class, String.class));

        }};
    }


}
