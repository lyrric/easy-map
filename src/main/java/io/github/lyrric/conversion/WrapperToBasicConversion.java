package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;
import io.github.lyrric.util.ClassTypeUtil;
import io.github.lyrric.model.ConversionContext;
import io.github.lyrric.model.FieldConversionResult;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * 包装类型->（原始类型 或 包装类型）
 *
 * @author wangxiaodong
 */
public class WrapperToBasicConversion extends BaseConversion {


    public WrapperToBasicConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public String getConversionCode() {
        Class<?> targetType = ClassTypeUtil.getPrimitiveType(targetClass);
        Class<?> sourceType = ClassTypeUtil.getPrimitiveType(sourceClass);
        if (sourceType == targetType) {
            return "<SOURCE>";
        }
        return "<SOURCE>." + targetType.getSimpleName() + "Value()";
    }


    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>() {{
            put(new DoubleKey(Byte.class, byte.class), new WrapperToBasicConversion(Byte.class, byte.class));
            put(new DoubleKey(Byte.class, short.class), new WrapperToBasicConversion(Byte.class, short.class));
            put(new DoubleKey(Byte.class, Short.class), new WrapperToBasicConversion(Byte.class, Short.class));
            put(new DoubleKey(Byte.class, int.class), new WrapperToBasicConversion(Byte.class, int.class));
            put(new DoubleKey(Byte.class, Integer.class), new WrapperToBasicConversion(Byte.class, Integer.class));
            put(new DoubleKey(Byte.class, long.class), new WrapperToBasicConversion(Byte.class, long.class));
            put(new DoubleKey(Byte.class, Long.class), new WrapperToBasicConversion(Byte.class, Long.class));
            put(new DoubleKey(Byte.class, float.class), new WrapperToBasicConversion(Byte.class, float.class));
            put(new DoubleKey(Byte.class, Float.class), new WrapperToBasicConversion(Byte.class, Float.class));
            put(new DoubleKey(Byte.class, double.class), new WrapperToBasicConversion(Byte.class, double.class));
            put(new DoubleKey(Byte.class, Double.class), new WrapperToBasicConversion(Byte.class, Double.class));

            put(new DoubleKey(Short.class, int.class), new WrapperToBasicConversion(Short.class, int.class));
            put(new DoubleKey(Short.class, short.class), new WrapperToBasicConversion(Short.class, short.class));
            put(new DoubleKey(Short.class, Integer.class), new WrapperToBasicConversion(Short.class, Integer.class));
            put(new DoubleKey(Short.class, long.class), new WrapperToBasicConversion(Short.class, long.class));
            put(new DoubleKey(Short.class, Long.class), new WrapperToBasicConversion(Short.class, Long.class));
            put(new DoubleKey(Short.class, float.class), new WrapperToBasicConversion(Short.class, float.class));
            put(new DoubleKey(Short.class, Float.class), new WrapperToBasicConversion(Short.class, Float.class));
            put(new DoubleKey(Short.class, double.class), new WrapperToBasicConversion(Short.class, double.class));
            put(new DoubleKey(Short.class, Double.class), new WrapperToBasicConversion(Short.class, Double.class));

            put(new DoubleKey(Integer.class, long.class), new WrapperToBasicConversion(Integer.class, long.class));
            put(new DoubleKey(Integer.class, int.class), new WrapperToBasicConversion(Integer.class, int.class));
            put(new DoubleKey(Integer.class, Long.class), new WrapperToBasicConversion(Integer.class, Long.class));
            put(new DoubleKey(Integer.class, float.class), new WrapperToBasicConversion(Integer.class, float.class));
            put(new DoubleKey(Integer.class, Float.class), new WrapperToBasicConversion(Integer.class, Float.class));
            put(new DoubleKey(Integer.class, double.class), new WrapperToBasicConversion(Integer.class, double.class));
            put(new DoubleKey(Integer.class, Double.class), new WrapperToBasicConversion(Integer.class, Double.class));

            put(new DoubleKey(Long.class, float.class), new WrapperToBasicConversion(Long.class, float.class));
            put(new DoubleKey(Long.class, long.class), new WrapperToBasicConversion(Long.class, long.class));
            put(new DoubleKey(Long.class, Float.class), new WrapperToBasicConversion(Long.class, Float.class));
            put(new DoubleKey(Long.class, double.class), new WrapperToBasicConversion(Long.class, double.class));
            put(new DoubleKey(Long.class, Double.class), new WrapperToBasicConversion(Long.class, Double.class));

            put(new DoubleKey(Float.class, double.class), new WrapperToBasicConversion(Float.class, double.class));
            put(new DoubleKey(Float.class, float.class), new WrapperToBasicConversion(Float.class, float.class));
            put(new DoubleKey(Float.class, Double.class), new WrapperToBasicConversion(Float.class, Double.class));

            put(new DoubleKey(Double.class, double.class), new WrapperToBasicConversion(Double.class, double.class));


        }};
    }
}
