package io.github.lyrric.util;

import io.github.lyrric.model.generate.ClassInfo;

/**
 * @author wangxiaodong
 */
public class EasyClassLoader extends ClassLoader{

    public EasyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> loadClass(String className, byte[] cLassBytes){
        return defineClass(className,
                cLassBytes, 0, cLassBytes.length);
    }
}
