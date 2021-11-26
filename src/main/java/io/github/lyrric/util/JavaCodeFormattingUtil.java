package io.github.lyrric.util;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

import java.util.Map;

/**
 * @author wangxiaodong
 */
@Slf4j
public class JavaCodeFormattingUtil {


    /**
     * 尝试对传入的JavaSourceFile格式化，此操作若成功则将改变传入对象的内容
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String tryFormat(String javaCode) {
        Map m = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
        m.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        m.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        m.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        m.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "160");
        m.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);

        IDocument doc = null;
        try {
            CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(m);
            TextEdit textEdit = codeFormatter.format(CodeFormatter.K_UNKNOWN, javaCode, 0, javaCode.length(), 0, null);
            if (textEdit != null) {
                doc = new Document(javaCode);
                textEdit.apply(doc);
                return doc.get();
            }
        } catch (Exception e) {
            log.error("格式化代码错误: {}", e.getMessage());
        }
        return javaCode;
    }

}
