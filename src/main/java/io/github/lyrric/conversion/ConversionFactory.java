package io.github.lyrric.conversion;

import io.github.lyrric.conversion.BaseConversion;
import io.github.lyrric.model.DoubleKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangxiaodong
 */
public class ConversionFactory {

    private static final Map<DoubleKey, BaseConversion> CONVERSIONS = new HashMap<>();

    static {
        CONVERSIONS.putAll(PrimitiveToPrimitiveConversion.getSupportedMap());
        CONVERSIONS.putAll(PrimitiveToWrapperConversion.getSupportedMap());
        CONVERSIONS.putAll(WrapperToBasicConversion.getSupportedMap());
        CONVERSIONS.putAll(WrapperToStringConversion.getSupportedMap());
    }

    public static Optional<BaseConversion> getConversion(Class<?> sourceClass, Class<?> targetClass){
        return Optional.ofNullable(CONVERSIONS.get(new DoubleKey(sourceClass, targetClass)));
    }
}
