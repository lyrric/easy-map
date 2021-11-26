package io.github.lyrric.test.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author wangxiaodong
 */
@Data
public class TargetPerson {

    public long id ;
    private String uuid ;
    private List<TargetPerson> persons ;

}
