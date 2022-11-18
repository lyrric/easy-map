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



    @Data
    public static class SubSourceItem{
        private Integer subId;

    }
}
