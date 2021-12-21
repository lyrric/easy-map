package io.github.lyrric.test;

import io.github.lyrric.EasyMap;
import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;
import io.github.lyrric.util.ClassTypeUtil;
import org.junit.Test;

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

    @Test
    public void test(){
        Long l = Long.MAX_VALUE;
        System.out.println(String.valueOf(l));

    }

    @Test
    public void testSingle() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        List<SourcePerson> sourcePeoples = Collections.singletonList(sourcePerson);
        List<TargetPerson> targetPeoples = EasyMap.mapList(sourcePeoples, TargetPerson.class);
        List<SourcePerson> result = EasyMap.mapList(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }

    @Test
    public void testList() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        TargetPerson targetPeople = EasyMap.mapSingleton(sourcePerson, TargetPerson.class);
        SourcePerson result = EasyMap.mapSingleton(targetPeople, SourcePerson.class);
        assert result.equals(sourcePerson);
    }
}
