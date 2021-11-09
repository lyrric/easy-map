//包名命命名规则：io.github.lyrric.generated
package io.github.lyrric.test;

import io.github.lyrric.test.model.SourcePerson;
import io.github.lyrric.test.model.TargetPerson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 集合之间相互转换
 * @author wangxiaodong
 */
//类名生成规则：目标类名+$MultiConversion
public class TargetPerson$MultiConversion {


    //方法命名规则：fromMulti+源类名
    //参数命令规则：首字母小写
    public List<TargetPerson> fromMultiSourcePerson(List<io.github.lyrric.test.model.SourcePerson> sourcePersons){
        if(sourcePersons == null){
            return null;
        }
        List<TargetPerson> target = new ArrayList<>(sourcePersons.size());
        for (SourcePerson sourcePerson : sourcePersons) {
            target.add(fromSourcePerson(sourcePerson));
        }
        return target;
    }

    //方法命名规则：from+源类名
    //参数命令规则：首字母小写
    private TargetPerson fromSourcePerson(io.github.lyrric.test.model.SourcePerson sourcePerson){
        TargetPerson target = new TargetPerson();
        //target.setId(sourcePerson.getId());
        //target.setAge(sourcePerson.getAge());
        return target;
    }

}
