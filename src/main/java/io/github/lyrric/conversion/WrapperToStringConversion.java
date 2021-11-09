package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;
import io.github.lyrric.model.FieldConversionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangxiaodong
 */
public class WrapperToStringConversion extends BaseConversion{


    public WrapperToStringConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    FieldConversionResult getConversionCode() {
        return FieldConversionResult.ofSingleCode("<SOURCE>.toString()");
    }

    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>() {{
            put(new DoubleKey(Byte.class, String.class), new WrapperToStringConversion(Byte.class, String.class));
            put(new DoubleKey(Boolean.class, String.class), new WrapperToStringConversion(Boolean.class, String.class));
            put(new DoubleKey(Short.class, String.class), new WrapperToStringConversion(Short.class, String.class));
            put(new DoubleKey(Integer.class, String.class), new WrapperToStringConversion(Integer.class, String.class));
            put(new DoubleKey(Long.class, String.class), new WrapperToStringConversion(Long.class, String.class));
            put(new DoubleKey(Float.class, String.class), new WrapperToStringConversion(Float.class, String.class));
            put(new DoubleKey(Double.class, String.class), new WrapperToStringConversion(Double.class, String.class));
            put(new DoubleKey(Character.class, String.class), new WrapperToStringConversion(Character.class, String.class));
        }};
    }


}
