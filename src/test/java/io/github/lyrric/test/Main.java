package io.github.lyrric.test;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        Type getUuid = SourcePerson.class.getDeclaredMethod("getUuid").getGenericReturnType();
        Type getUuid1 = SourcePerson.class.getDeclaredMethod("getUuid").getGenericReturnType();
        System.out.println(getUuid.hashCode());
        System.out.println(getUuid1.hashCode());
        System.out.println();
    }
}
