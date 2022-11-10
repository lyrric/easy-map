package io.github.lyrric.easymapstruct.conversion;

/**
 * @author wangxiaodong
 */
public abstract class BaseConversion {
    /**
     * sourceClass
     */
    protected final Class<?> sourceClass;
    /**
     * targetClass
     */
    protected final Class<?> targetClass;

    public BaseConversion(Class<?> sourceClass, Class<?> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    /**
     * convert
     * @return
     */
    public abstract String getConversionCode();

}
