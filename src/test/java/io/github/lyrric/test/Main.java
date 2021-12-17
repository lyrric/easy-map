package io.github.lyrric.test;

import io.github.lyrric.EasyMap;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;
import io.github.lyrric.util.ClassTypeUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class Main {

    public static void main(String[] args) throws NoSuchMethodException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        List<TargetPerson> targetPeoples = EasyMap.mapList(Collections.singletonList(sourcePerson), TargetPerson.class);
        TargetPerson targetPeople = EasyMap.mapSingleton(sourcePerson, TargetPerson.class);
        EasyMap.mapSingleton(sourcePerson, TargetPerson.class);
        System.out.println("");
    }
}
