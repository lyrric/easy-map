package io.github.lyrric;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.util.ClassTypeUtil;
import io.github.lyrric.util.EasyClassLoader;
import io.github.lyrric.util.JavaCodeFormattingUtil;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
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
        EasyClassLoader classLoader = new EasyClassLoader(EasyMap.class.getClassLoader());
        long start;
        long end;
  /*      start = System.currentTimeMillis();
        ClassTypeUtil.hftCompile(classInfo.getPackageStr()+"."+classInfo.getClassName(), javaSourceString);
        end = System.currentTimeMillis();
        System.out.println("hft编译耗时：" + (end - start));
*/

        start = System.currentTimeMillis();
        byte[] bytes = ClassTypeUtil.compile(classInfo.getClassName()+".java", javaSourceString);
        end = System.currentTimeMillis();
        System.out.println("javac编译耗时：" + (end - start));
        //Class<?> loadClass = classLoader.loadClass(classInfo.getPackageStr()+"."+classInfo.getClassName(), bytes);

        return null;
    }




}
