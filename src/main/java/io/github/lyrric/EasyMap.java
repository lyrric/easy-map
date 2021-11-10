package io.github.lyrric;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class EasyMap {


    public static <T> T mapSingleton(Object source, Class<T> targetClass) {
        ClassInfo classInfo = new ClassGenerator().generate(source.getClass(), targetClass);

        return null;
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }

        new ClassGenerator().convertList(Modifier.PUBLIC, sourceList.get(0).getClass(), targetClass);
        return null;
    }


    public static void main(String[] args) {

    }
}
