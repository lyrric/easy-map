package io.github.lyrric.test.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wangxiaodong
 */
public class SubItem {

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("1.22");
        System.out.println(bigDecimal);
    }

    public static void convertToList(Collection<?> source) {
        for (Object o : source) {
            System.out.println(o.getClass());
        }
    }}
