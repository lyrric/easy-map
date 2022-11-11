package io.github.lyrric.easymapstruct.model.generate;

import io.github.lyrric.easymapstruct.util.ClassTypeUtil;
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

    public String toJavaSourceString(){


        /*
         *  ClassTypeUtil.getSelfClass(returnType).getCanonicalName()
         *      对于带有泛型的类来说，如List，会返回“java.util.List”，缺少了泛型信息。
         *      对于其他类型的类来说，是正常的。
         *  returnType.getTypeName()
         *      对于带有泛型的类来说，会返回如“java.util.List<SourcePerson>”的字符串，也是我们需要的格式。
         *      对于内部类来说，会返回如“com.test.model.TargetPerson$SubTargetItem”格式，这个格式是JVM内部认可的格式，
         *          但是无法直接在代码中使用，代码中访问内部类必须是com.test.model.TargetPerson.SubTargetItem
         *  所以这里需要判断类型， 根据不同的类型，用不同的方法。
         */

        String retClassName = ClassTypeUtil.hasGenerics(returnType) ?
                returnType.getTypeName() : ClassTypeUtil.getSelfClass(returnType).getCanonicalName();
        String argClassName = ClassTypeUtil.hasGenerics(argType) ?
                argType.getTypeName() : ClassTypeUtil.getSelfClass(argType).getCanonicalName();

        return "\n" +
                String.format("%s static %s %s(%s source){",
                        modifier.equals(Modifier.PUBLIC) ? "public" : "private",
                        retClassName,
                        methodName,
                        argClassName) +
                "\n" +
                String.join("\n", codes) +
                "\n" +
                "}";
    }
}
