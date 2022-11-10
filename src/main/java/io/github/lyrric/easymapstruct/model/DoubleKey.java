package io.github.lyrric.easymapstruct.model;

import java.util.Objects;

/**
 * @author wangxiaodong
 */
public class DoubleKey {

    private final Class<?> sourceClass;

    private final Class<?> targetClass;

    public DoubleKey(Class<?> sourceClass, Class<?> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }

    @Override
    public String toString() {
        return "Key [sourceType=" + sourceClass + ", targetType="
                + targetClass + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( sourceClass == null ) ? 0 : sourceClass.hashCode() );
        result = prime * result + ( ( targetClass == null ) ? 0 : targetClass.hashCode() );
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        DoubleKey other = (DoubleKey) obj;

        if ( !Objects.equals( sourceClass, other.sourceClass ) ) {
            return false;
        }

        return Objects.equals(targetClass, other.targetClass);
    }
}
