package io.github.lyrric.easymapstruct.test.model;

import lombok.Data;

/**
 * @author wangxiaodong
 */
@Data
public class TargetPerson {

    public long id ;
    private String uuid ;

    private SubTargetItem subItem;

    @Data
    public static class SubTargetItem{
        private Integer subId;

    }
}
