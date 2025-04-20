package com.kamijoucen.ruler.compiler.parser;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.compiler.TokenStream;

/**
 * 原子解析器接口，每个实现类负责解析特定的语法结构
 */
public interface AtomParser {

    /**
     * 判断是否支持解析当前输入流
     * @param tokenStream 输入流，允许查看多个token来做决策
     * @return 是否支持
     */
    boolean support(TokenStream tokenStream);

    /**
     * 解析当前语法结构
     * @param manager 解析器管理器，提供解析上下文和工具
     * @return 解析结果节点
     */
    BaseNode parse(AtomParserManager manager);
}