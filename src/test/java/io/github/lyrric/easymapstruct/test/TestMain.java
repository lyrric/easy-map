package io.github.lyrric.easymapstruct.test;


import io.github.lyrric.easymapstruct.EasyMapStruct;
import io.github.lyrric.easymapstruct.test.model.SourcePerson;
import io.github.lyrric.easymapstruct.test.model.TargetPerson;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wangxiaodong
 */
public class TestMain {

    @Test
    public void testSingle()  {
        SourcePerson sourcePerson = getSourcePerson();
        TargetPerson targetPeople = EasyMapStruct.mapSingleton(sourcePerson, TargetPerson.class);
        SourcePerson result = EasyMapStruct.mapSingleton(targetPeople, SourcePerson.class);
        assert result.equals(sourcePerson);
    }

    @Test
    public void testList() {
        List<SourcePerson> sourcePeoples = Collections.singletonList(getSourcePerson());
        List<TargetPerson> targetPeoples = EasyMapStruct.mapList(sourcePeoples, TargetPerson.class);
        List<SourcePerson> result = EasyMapStruct.mapList(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }

    @Test
    public void testSet()  {
        Set<SourcePerson> sourcePeoples = Collections.singleton(getSourcePerson());
        Set<TargetPerson> targetPeoples = EasyMapStruct.mapSet(sourcePeoples, TargetPerson.class);
        Set<SourcePerson> result = EasyMapStruct.mapSet(targetPeoples, SourcePerson.class);
        assert result.equals(sourcePeoples);
    }



    @Test
    public void testArray(){
        SourcePerson[] source = new SourcePerson[]{getSourcePerson()};
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
    public void test() {
      
    }


    private SourcePerson getSourcePerson(){
        SourcePerson sourcePerson = new SourcePerson();
        sourcePerson.setId(55);
        sourcePerson.setUuid(999L);
        sourcePerson.setSubIds(new Integer[]{1,2,3,4,5});
        SourcePerson.SubSourceItem subSourceItem = new SourcePerson.SubSourceItem();
        subSourceItem.setSubId(99);
        sourcePerson.setSubItem(subSourceItem);
        sourcePerson.setSubItems(new SourcePerson.SubSourceItem[]{subSourceItem});
        sourcePerson.setSubItemList(Collections.singletonList(subSourceItem));
        return sourcePerson;
    }
}
