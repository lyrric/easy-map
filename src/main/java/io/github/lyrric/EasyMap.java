package io.github.lyrric;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.util.ClassTypeUtil;
import io.github.lyrric.util.JavaCodeFormattingUtil;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangxiaodong
 */
public class EasyMap {


    private static final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();



    public static <T> T mapSingleton(Object source, Class<T> targetClass) {
        ClassInfo classInfo = new ClassGenerator().convertObject(source.getClass(), targetClass);

        return null;
    }

    public static <T> List<T> mapList(List<?> sourceList, Class<T> targetClass) throws IOException, ClassNotFoundException {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }
        ClassInfo classInfo = new ClassGenerator().convertObject(
                ClassTypeUtil.wrapType(List.class, null, sourceList.get(0).getClass()),
                ClassTypeUtil.wrapType(List.class, null, targetClass));
        String javaSourceString = classInfo.toJavaSourceString();
        System.out.println(JavaCodeFormattingUtil.tryFormat(javaSourceString));
        long start;
        long end;
        start = System.currentTimeMillis();
        ClassTypeUtil.hftCompile(classInfo.getPackageStr()+"."+classInfo.getClassName(), javaSourceString);
        end = System.currentTimeMillis();
        System.out.println("hft编译耗时：" + (end - start));
        return null;
    }




}
