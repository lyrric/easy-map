package io.github.lyrric;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.util.ClassTypeUtil;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class EasyMap {


    public static <T> T mapSingleton(Object source, Class<T> targetClass) {
        ClassInfo classInfo = new ClassGenerator().convertObject(source.getClass(), targetClass);

        return null;
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
//        Type type = ClassTypeUtil.wrapType(List.class, sourceList.get(0).getClass(), List.class);
//        Method[] declaredMethods = SourcePerson.class.getDeclaredMethods();
        ClassInfo classInfo = new ClassGenerator().convertObject(
                ClassTypeUtil.wrapType(List.class, null, sourceList.get(0).getClass()),
                ClassTypeUtil.wrapType(ArrayList.class, null, targetClass));
        System.out.println(classInfo.toJavaSourceString());
        return null;
    }




}
