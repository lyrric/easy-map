package io.github.lyrric.easymapstruct.test;


import io.github.lyrric.easymapstruct.EasyMapStruct;
import io.github.lyrric.easymapstruct.test.model.SourcePerson;
import io.github.lyrric.easymapstruct.test.model.TargetPerson;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangxiaodong
 */
public class TestMain {

    @Test
    public void testSingle() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        TargetPerson targetPeople = EasyMapStruct.mapSingleton(sourcePerson, TargetPerson.class);
        SourcePerson result = EasyMapStruct.mapSingleton(targetPeople, SourcePerson.class);
        assert result.equals(sourcePerson);
    }

    @Test
    public void testList() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        List<SourcePerson> sourcePeoples = Collections.singletonList(sourcePerson);
        List<TargetPerson> targetPeoples = EasyMapStruct.mapList(sourcePeoples, TargetPerson.class);
        List<SourcePerson> result = EasyMapStruct.mapList(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }

    @Test
    public void testSet() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        Set<SourcePerson> sourcePeoples = Collections.singleton(sourcePerson);
        Set<TargetPerson> targetPeoples = EasyMapStruct.mapSet(sourcePeoples, TargetPerson.class);
        Set<SourcePerson> result = EasyMapStruct.mapSet(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }



    @Test
    public void t(){
        List<Integer> collect = Collections.singletonList(4)
                .stream().filter(t -> t.equals(4))
                .collect(Collectors.toList());
        System.out.println(collect);
    }
}
