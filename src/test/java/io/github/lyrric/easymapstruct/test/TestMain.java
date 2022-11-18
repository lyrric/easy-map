package io.github.lyrric.easymapstruct.test;


import io.github.lyrric.easymapstruct.EasyMapStruct;
import io.github.lyrric.easymapstruct.test.model.SourcePerson;
import io.github.lyrric.easymapstruct.test.model.TargetPerson;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author wangxiaodong
 */
public class TestMain {

    @Test
    public void testSingle()  {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        TargetPerson targetPeople = EasyMapStruct.mapSingleton(sourcePerson, TargetPerson.class);
        SourcePerson result = EasyMapStruct.mapSingleton(targetPeople, SourcePerson.class);
        assert result.equals(sourcePerson);
    }

    @Test
    public void testList() {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        List<SourcePerson> sourcePeoples = Collections.singletonList(sourcePerson);
        List<TargetPerson> targetPeoples = EasyMapStruct.mapList(sourcePeoples, TargetPerson.class);
        List<SourcePerson> result = EasyMapStruct.mapList(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }

    @Test
    public void testSet()  {
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(12345L);
        Set<SourcePerson> sourcePeoples = Collections.singleton(sourcePerson);
        Set<TargetPerson> targetPeoples = EasyMapStruct.mapSet(sourcePeoples, TargetPerson.class);
        Set<SourcePerson> result = EasyMapStruct.mapSet(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }



    @Test
    public void testArray(){
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(1);
        sourcePerson.setUuid(123L);
        SourcePerson[] source = new SourcePerson[]{sourcePerson};
        TargetPerson[] target = EasyMapStruct.mapArray(source, TargetPerson.class);
        SourcePerson[] result = EasyMapStruct.mapArray(target, SourcePerson.class);
        for (int i = 0; i < source.length; i++) {
            assert source[i].equals(result[i]);
        }
    }

    @Test
    public void testArray2(){
        Integer[] source = new Integer[]{1, 2, 3, 4};
        Long[] target = EasyMapStruct.mapArray(source, Long.class);
        for (int i = 0; i < source.length; i++) {
            assert source[i].equals(target[i].intValue());
        }
    }

    @Test
    public void test(){
        SourcePerson[] sourcePeople = new SourcePerson[1];
        Class<? extends SourcePerson[]> aClass = sourcePeople.getClass();
        System.out.println(aClass.getName());
        System.out.println(aClass.getTypeName());
        System.out.println(aClass.getCanonicalName());
        System.out.println(aClass.getSimpleName());
    }
}
