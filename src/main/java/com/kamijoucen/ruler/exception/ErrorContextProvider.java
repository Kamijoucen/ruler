package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.TokenLocation;

/**
 * 错误上下文提供器
 * 用于为异常提供源代码上下文信息
 *
 * @author Kamijoucen
 */
public class ErrorContextProvider {

    private final String sourceCode;
    private final String fileName;

    public ErrorContextProvider(String sourceCode, String fileName) {
        this.sourceCode = sourceCode;
        this.fileName = fileName;
    }

    /**
     * 获取指定位置附近的源代码片段
     *
     * @param location 错误位置
     * @param contextLines 上下文行数（前后各显示多少行）
     * @return 格式化的源代码片段
     */
    public String getSourceSnippet(TokenLocation location, int contextLines) {
        if (sourceCode == null || location == null) {
            return null;
        }

        String[] lines = sourceCode.split("\n");
        int errorLine = (int) (location.line - 1); // 转换为0-based索引

        if (errorLine < 0 || errorLine >= lines.length) {
            return null;
        }

        StringBuilder snippet = new StringBuilder();

        // 计算开始和结束行
        int startLine = Math.max(0, errorLine - contextLines);
        int endLine = Math.min(lines.length - 1, errorLine + contextLines);

        // 构建代码片段
        for (int i = startLine; i <= endLine; i++) {
            // 行号
            snippet.append(String.format("%4d | ", i + 1));

            // 代码行
            snippet.append(lines[i]);
            snippet.append("\n");

            // 错误指示器
            if (i == errorLine) {
                snippet.append("     | ");
                // 添加空格到错误列
                int columnIndex = (int) (location.column - 1);
                for (int j = 0; j < columnIndex; j++) {
                    snippet.append(" ");
                }
                snippet.append("^ 这里\n");
            }
        }

        return snippet.toString();
    }

    /**
     * 为异常添加源代码上下文
     *
     * @param exception 要增强的异常
     * @return 增强后的异常
     */
    public <T extends RulerException> T enhance(T exception) {
        if (exception.getLocation() != null) {
            String snippet = getSourceSnippet(exception.getLocation(), 2);
            exception.setSourceSnippet(snippet);
        }
        return exception;
    }

    /**
     * 获取错误行的内容
     *
     * @param location 错误位置
     * @return 错误行的内容，如果无法获取返回null
     */
    public String getErrorLine(TokenLocation location) {
        if (sourceCode == null || location == null) {
            return null;
        }

        String[] lines = sourceCode.split("\n");
        int lineIndex = (int) (location.line - 1);

        if (lineIndex >= 0 && lineIndex < lines.length) {
            return lines[lineIndex];
        }

        return null;
    }
}
