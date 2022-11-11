package io.github.lyrric.easymapstruct.test.model;

import lombok.Data;

/**
 * @author wangxiaodong
 */
@Data
public class SourcePerson {

    private Integer id ;
    private Long uuid ;

    private SubSourceItem subItem;

    @Data
    public static class SubSourceItem{
        private Integer subId;

    }
}
