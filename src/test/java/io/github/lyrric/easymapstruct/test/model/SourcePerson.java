package io.github.lyrric.easymapstruct.test.model;

import lombok.Data;

import java.util.List;

/**
 * @author wangxiaodong
 */
@Data
public class SourcePerson {

    private Integer id ;
    private Long uuid ;

    private SubSourceItem subItem;

    private Integer[] subIds;

    private SubSourceItem[] subItems;

    private List<SubSourceItem> SubItemList;


    @Data
    public static class SubSourceItem{
        private Integer subId;

    }
}
