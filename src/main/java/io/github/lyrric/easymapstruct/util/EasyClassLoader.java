package io.github.lyrric.easymapstruct.util;

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
