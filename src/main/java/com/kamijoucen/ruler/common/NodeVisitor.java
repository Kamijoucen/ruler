package com.kamijoucen.ruler.common;

import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.DefaultParamValNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.ast.expression.InfixDefinitionNode;
import com.kamijoucen.ruler.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.ast.factor.ArrayNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.ast.factor.BoolNode;
import com.kamijoucen.ruler.ast.factor.BreakNode;
import com.kamijoucen.ruler.ast.factor.ContinueNode;
import com.kamijoucen.ruler.ast.factor.DoubleNode;
import com.kamijoucen.ruler.ast.factor.IntegerNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.NullNode;
import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.ast.factor.ReturnNode;
import com.kamijoucen.ruler.ast.factor.RsonNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.ast.factor.ThisNode;
import com.kamijoucen.ruler.ast.factor.TypeOfNode;
import com.kamijoucen.ruler.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.ast.factor.VirtualNode;
import com.kamijoucen.ruler.value.BaseValue;

public interface NodeVisitor {

    BaseValue eval(NameNode node);

    BaseValue eval(OutNameNode node);

    BaseValue eval(IntegerNode node);

    BaseValue eval(DoubleNode node);

    BaseValue eval(BoolNode node);

    BaseValue eval(StringNode node);

    BaseValue eval(BinaryOperationNode node);

    BaseValue eval(UnaryOperationNode node);

    BaseValue eval(ArrayNode node);

    BaseValue eval(NullNode node);

    BaseValue eval(RsonNode node);

    BaseValue eval(ThisNode node);

    BaseValue eval(TypeOfNode node);

    BaseValue eval(BlockNode node);

    BaseValue eval(IfStatementNode node);

    BaseValue eval(AssignNode node);

    BaseValue eval(WhileStatementNode node);

    BaseValue eval(ForEachStatementNode node);

    BaseValue eval(BreakNode node);

    BaseValue eval(ContinueNode node);

    BaseValue eval(CallNode node);

    BaseValue eval(IndexNode node);

    BaseValue eval(DotNode node);

    BaseValue eval(ClosureDefineNode node);

    BaseValue eval(ReturnNode node);

    BaseValue eval(VariableDefineNode node);

    BaseValue eval(ImportNode node);

    BaseValue eval(RuleStatementNode node);

    BaseValue eval(InfixDefinitionNode node);

    BaseValue eval(DefaultParamValNode node);

    BaseValue eval(VirtualNode node);

}
