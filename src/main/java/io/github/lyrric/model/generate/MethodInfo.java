package io.github.lyrric.model.generate;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产的方法
 * @author wangxiaodong
 */
public class MethodInfo {

    /**
     * 修饰符private or public
     */
    private Modifier modifier;
    /**
     * 返回类型
     */
    private Class<?> returnType;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型，only one
     */
    private Class<?> argType;
    /**
     * 每一行的代码
     */
    private List<String> codes;

    public MethodInfo(Modifier modifier, Class<?> returnType, String methodName, Class<?> argType) {
        this.modifier = modifier;
        this.returnType = returnType;
        this.methodName = methodName;
        this.argType = argType;
        codes = new ArrayList<>();
    }


    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?> getArgType() {
        return argType;
    }

    public void setArgType(Class<?> argType) {
        this.argType = argType;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void addCodes(List<String> codes) {
        this.codes.addAll(codes);
    }
    public void addCode(String code) {
        this.codes.add(code);
    }
}
