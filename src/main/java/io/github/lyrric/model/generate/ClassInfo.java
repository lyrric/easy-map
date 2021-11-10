package io.github.lyrric.model.generate;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 需要引入的包
     */
    private Set<String> importClasses;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法列表
     */
    private List<MethodInfo> methodInfos;

    public ClassInfo() {
        methodInfos = new ArrayList<>();
    }


    public void setImportClasses(Set<String> importClasses) {
        this.importClasses = importClasses;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public void addMethods(List<MethodInfo> methodInfos){
        this.methodInfos.addAll(methodInfos);
    }
    public void addMethod(MethodInfo methodInfo){
        this.methodInfos.add(methodInfo);
    }


    public String toJavaSourceString(){
        StringBuilder code = new StringBuilder();
        code.append("package " + packageStr + ";")
                .append("\n")
                .append(String.join("\n", importClasses.stream().map(str -> "import " + str + ";").collect(Collectors.toList())))
                .append("\n")
                .append("public class ").append(className).append(" {").append("\n");
        for (MethodInfo methodInfo : methodInfos) {
            String modifier = methodInfo.getModifier().equals(Modifier.PUBLIC) ? "public" : "private";
            code.append("\n")
            .append(String.format("%s static %s %s(%s source){",
                    modifier, methodInfo.getReturnType().getCanonicalName(), methodInfo.getMethodName(), methodInfo.getArgType().getCanonicalName()))
            .append("\n")
            .append(String.join("\n", methodInfo.getCodes()))
                    .append("\n")
            .append("}");
        }
        code.append("\n")
                .append("}");
        return code.toString();
    }
}
