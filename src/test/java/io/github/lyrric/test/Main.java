package io.github.lyrric.test;

import io.github.lyrric.EasyMap;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;
import io.github.lyrric.util.ClassTypeUtil;

import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException {
        //System.out.println(ClassTypeUtil.wrapType(List.class, null, SourcePerson.class).getTypeName());
        EasyMap.mapList(Collections.singletonList(new SourcePerson()), TargetPerson.class);

    }
}
