package io.github.lyrric.easymapstruct.test.model;

import lombok.Data;

import java.util.List;

/**
 * @author wangxiaodong
 */
@Data
public class TargetPerson {

    public long id ;
    private String uuid ;

    private SubTargetItem subItem;

    private long[] subIds;

    private SubTargetItem[] subItems;

    private List<SubTargetItem> subItemList;
    @Data
    public static class SubTargetItem{
        private String subId;

    }
}
