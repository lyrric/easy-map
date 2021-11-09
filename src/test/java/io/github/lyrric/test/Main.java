package io.github.lyrric.test;

import io.github.lyrric.generator.ClassGenerator;
import io.github.lyrric.model.generate.ClassInfo;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) {
        ClassInfo generate = new ClassGenerator().generate(SourcePerson.class, TargetPerson.class);
        System.out.println(generate.toJavaSourceString());
    }
}
