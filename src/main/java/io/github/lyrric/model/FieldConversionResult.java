package io.github.lyrric.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 一个字段转换成另一个字段的代码
 *
 * @author wangxiaodong
 */
public class FieldConversionResult {

    /**
     * 需要引入的类
     */
    private final Set<String> importLines;
    /**
     * 转换的代码
     */
    private final List<String> codes;


    private FieldConversionResult(Set<String> importLines, List<String> codes) {
        this.importLines = importLines;
        this.codes = codes;
    }


    /**
     * 添加if（<SOURCE> != null）{
     *
     * }
     */
    public FieldConversionResult addCheckNull(String source){
        codes.add(0, "if(" + source + " != null){");
        codes.add("}");
        return this;
    }

    public static FieldConversionResult of(Set<String> importLines, List<String> codes) {
        return new FieldConversionResult(importLines, codes);
    }

    public static FieldConversionResult of(List<String> codes) {
        return new FieldConversionResult(Collections.emptySet(), codes);
    }

    public static FieldConversionResult ofSingleCode(String code){
        return new FieldConversionResult(Collections.emptySet(), new ArrayList<>(Collections.singletonList(code)));
    }

    public static FieldConversionResult empty() {
        return new FieldConversionResult(Collections.emptySet(), Collections.emptyList());
    }

    public Set<String> getImportLines() {
        return importLines;
    }

    public List<String> getCodes() {
        return codes;
    }

}
