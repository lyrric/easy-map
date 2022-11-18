package io.github.lyrric.easymapstruct.test.model;

import lombok.Data;

/**
 * @author wangxiaodong
 */
@Data
public class TargetPerson {

    public long id ;
    private String uuid ;

    @Data
    public static class SubTargetItem{
        private Integer subId;

    }
}
