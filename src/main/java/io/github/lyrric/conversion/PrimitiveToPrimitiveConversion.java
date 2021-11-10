/*
 * Copyright MapStruct Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.lyrric.conversion;

import io.github.lyrric.model.DoubleKey;
import io.github.lyrric.model.FieldConversionResult;

import java.util.HashMap;
import java.util.Map;

/**
 * e.g int -> long
 * @author wangxiaodong
 */
public class PrimitiveToPrimitiveConversion extends BaseConversion {


    public PrimitiveToPrimitiveConversion(Class<?> sourceClass, Class<?> targetClass) {
        super(sourceClass, targetClass);
    }

    @Override
    public String getConversionCode() {
        return"(" + sourceClass.getSimpleName() + ") <SOURCE>";
    }


    public static Map<DoubleKey, BaseConversion> getSupportedMap() {
        return new HashMap<DoubleKey, BaseConversion>(){{
            put( new DoubleKey(byte.class, short.class), new PrimitiveToWrapperConversion(byte.class, short.class));
            put( new DoubleKey(byte.class, int.class), new PrimitiveToWrapperConversion(byte.class, int.class) );
            put( new DoubleKey(byte.class, long.class), new PrimitiveToWrapperConversion(byte.class, long.class ));
            put( new DoubleKey(byte.class, float.class), new PrimitiveToWrapperConversion(byte.class, float.class));
            put( new DoubleKey(byte.class, double.class), new PrimitiveToWrapperConversion(byte.class, double.class));

            put( new DoubleKey(short.class, int.class), new PrimitiveToWrapperConversion(short.class, int.class));
            put( new DoubleKey(short.class, long.class), new PrimitiveToWrapperConversion(short.class, long.class));
            put( new DoubleKey(short.class, float.class), new PrimitiveToWrapperConversion(short.class, float.class));
            put( new DoubleKey(short.class, double.class), new PrimitiveToWrapperConversion(short.class, double.class));

            put( new DoubleKey(int.class, long.class ), new PrimitiveToWrapperConversion(int.class, long.class));
            put( new DoubleKey(int.class, float.class ), new PrimitiveToWrapperConversion(int.class, float.class));
            put( new DoubleKey(int.class, double.class ), new PrimitiveToWrapperConversion(int.class, double.class));

            put( new DoubleKey(long.class, float.class ), new PrimitiveToWrapperConversion(long.class, float.class ));
            put( new DoubleKey(long.class, double.class ), new PrimitiveToWrapperConversion(long.class, double.class));
            put( new DoubleKey(float.class, double.class ), new PrimitiveToWrapperConversion(float.class, double.class));
        }};
    }


}
