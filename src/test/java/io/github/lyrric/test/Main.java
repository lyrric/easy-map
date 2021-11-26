package io.github.lyrric.test;

import io.github.lyrric.EasyMap;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;
import io.github.lyrric.util.ClassTypeUtil;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        //System.out.println(ClassTypeUtil.wrapType(List.class, null, SourcePerson.class).getTypeName());
        EasyMap.mapList(Collections.singletonList(new SourcePerson()), TargetPerson.class);
        Method[] s = SourcePerson.class.getDeclaredMethods();
        Method[] t = TargetPerson.class.getDeclaredMethods();
        boolean equals = s[4].getGenericReturnType().equals(t[5].getGenericParameterTypes()[0]);
        System.out.println();
    }
}
