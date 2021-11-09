package io.github.lyrric.test.model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class SubItem {

    public static void main(String[] args) {
        List<SourcePerson> list = Collections.singletonList(new SourcePerson());
        convertToList(list);
        convertToList(Collections.singletonList(new TargetPerson()));
    }

    public static void convertToList(Collection<?> source) {
        for (Object o : source) {
            System.out.println(o.getClass());
        }
    }}
