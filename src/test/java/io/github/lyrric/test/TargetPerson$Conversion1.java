//包名命命名规则：io.github.lyrric.generated
package io.github.lyrric.test;

import io.github.lyrric.test.model.TargetPerson;

/**
 * 单个对象之间转换示例
 * @author wangxiaodong
 */
//类名生成规则：目标类名+$Conversion+no，no从1开始
public class TargetPerson$Conversion1 {


    //方法命名规则：convert
    //参数命令规则：source
    public TargetPerson convert(io.github.lyrric.test.model.SourcePerson source){
        TargetPerson target = new TargetPerson();
//        target.setId(source.getId());
//        target.setAge(source.getAge());

        return target;
    }


}
