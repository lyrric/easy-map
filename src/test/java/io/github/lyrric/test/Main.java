package io.github.lyrric.test;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) {
        Class<SourcePerson> sourcePersonClass = SourcePerson.class;
        Method[] declaredMethods = sourcePersonClass.getDeclaredMethods();
        Type genericReturnType = declaredMethods[0].getGenericReturnType();
        Type[] genericParameterTypes = declaredMethods[0].getGenericParameterTypes();
        System.out.println();
//        ClassInfo generate = new ClassGenerator().generate(SourcePerson.class, TargetPerson.class);
//        System.out.println(generate.toJavaSourceString());
    }
}
