package io.github.lyrric.easymapstruct.model.generate;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产的类信息
 * @author wangxiaodong
 */
public class ClassInfo {

    /**
     * 包名
     */
    private final String packageStr = "io.github.lyrric.generated";
    /**
     * 类名
     */
    private String className;
    /**
     * 方法列表
     */
    private final List<MethodInfo> methodInfos;
    /**
     * 实例
     */
    private Object instance;
    /**
     *
     */
    private Class<?> clazz;

    public ClassInfo() {
        methodInfos = new ArrayList<>();
    }



    public void setClassName(String className) {
        this.className = className;
    }


    public void addMethods(List<MethodInfo> methodInfos){
        this.methodInfos.addAll(methodInfos);
    }

    public String getClassName() {
        return className;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public String toJavaSourceString(){
        StringBuilder code = new StringBuilder();
        code.append("package " + packageStr + ";")
                .append("\n")
                .append("public class ").append(className).append(" {").append("\n");
        for (MethodInfo methodInfo : methodInfos) {
           code.append(methodInfo.toJavaSourceString());
        }
        code.append("\n")
                .append("}");
        return code.toString();
    }


    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }


    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
