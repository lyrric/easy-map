package io.github.lyrric.conversion;

/**
 * @author wangxiaodong
 */
public abstract class BaseConversion {
    /**
     *
     */
    protected final Class<?> sourceClass;
    /**
     *
     */
    protected final Class<?> targetClass;

    public BaseConversion(Class<?> sourceClass, Class<?> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
    }
/*
    *//**
     * 获取getter code，不带末尾";"分号
     * 例如"source.getId()"
     * @param
     * @return
     *//*
    protected final String getGetterCode(){
        return Constant.SOURCE + "." + context.getGetterMethodName() + "()";
    }

    *//**
     * 获取setter code，带末尾";"分号
     * 例如"target.setId(source.getId())"
     * @param valueCode set方法中值的代码，例如source.getId()
     * @return
     *//*
    protected final String getSetterCode(String valueCode){
        return Constant.TARGET + "." + context.getSetterMethodName() + "(" + valueCode + ");";
    }*/

    /**
     * convert
     * @return
     */
    public abstract String getConversionCode();

}
