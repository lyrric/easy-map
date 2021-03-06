package io.github.lyrric.model.generate;

import lombok.Getter;
import lombok.Setter;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 生产的方法
 * @author wangxiaodong
 */
@Getter
@Setter
public class MethodInfo {

    /**
     * 修饰符private or public
     */
    private Modifier modifier;
    /**
     * 返回类型
     */
    private Type returnType;
    /**pe
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型，only one
     */
    private Type argType;
    /**
     * 每一行的代码
     */
    private List<String> codes;

    public MethodInfo(Modifier modifier, Type returnType, String methodName, Type argType) {
        this.modifier = modifier;
        this.returnType = returnType;
        this.methodName = methodName;
        this.argType = argType;
        codes = new ArrayList<>();
    }




    public void addCodes(List<String> codes) {
        this.codes.addAll(codes);
    }
    public void addCode(String code) {
        this.codes.add(code);
    }
}
