package com.kamijoucen.ruler.eval;

import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.eval.expression.*;
import com.kamijoucen.ruler.eval.facotr.*;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.runtime.Scope;
import com.kamijoucen.ruler.value.BaseValue;

public class EvalVisitor extends AbstractVisitor {

    private static final NameEval nameEval = new NameEval();
    private static final OutNameEval outNameEval = new OutNameEval();
    private static final IntegerEval integerEval = new IntegerEval();
    private static final DoubleEval doubleEval = new DoubleEval();
    private static final BooleanEval booleanEval = new BooleanEval();
    private static final StringEval stringEval = new StringEval();
    private static final BinaryOperationEval binaryOperationEval = new BinaryOperationEval();
    private static final UnaryOperationEval unaryOperationEval = new UnaryOperationEval();
    private static final ArrayEval arrayEval = new ArrayEval();
    private static final NullEval nullEval = new NullEval();
    private static final RsonEval rsonEval = new RsonEval();
    private static final ThisEval thisEval = new ThisEval();
    private static final TypeOfEval typeOfEval = new TypeOfEval();
    private static final BlockEval blockEval = new BlockEval();
    private static final IfStatementEval ifStatementEval = new IfStatementEval();
    private static final AssignEval assignEval = new AssignEval();
    private static final WhileStatementEval whileStatementEval = new WhileStatementEval();
    private static final ForEachStatementEval forEachStatementEval = new ForEachStatementEval();
    private static final BreakEval breakEval = new BreakEval();
    private static final ContinueEval continueEval = new ContinueEval();
    private static final CallEval callEval = new CallEval();
    private static final IndexEval indexEval = new IndexEval();
    private static final DotEval dotEval = new DotEval();
    private static final ClosureEval closureEval = new ClosureEval();
    private static final ReturnEval returnEval = new ReturnEval();
    private static final VariableEval varDefineEval = new VariableEval();
    private static final ImportEval importEval = new ImportEval();
    private static final RuleStatementEval ruleStatementEval = new RuleStatementEval();
    private static final InfixDefinitionEval infixDefinitionEval = new InfixDefinitionEval();

    @Override
    public BaseValue eval(NameNode node, Scope scope, RuntimeContext context) {
        return nameEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(OutNameNode node, Scope scope, RuntimeContext context) {
        return outNameEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IntegerNode node, Scope scope, RuntimeContext context) {
        return integerEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(DoubleNode node, Scope scope, RuntimeContext context) {
        return doubleEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BoolNode node, Scope scope, RuntimeContext context) {
        return booleanEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(StringNode node, Scope scope, RuntimeContext context) {
        return stringEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BinaryOperationNode node, Scope scope, RuntimeContext context) {
        return binaryOperationEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(UnaryOperationNode node, Scope scope, RuntimeContext context) {
        return unaryOperationEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ArrayNode node, Scope scope, RuntimeContext context) {
        return arrayEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(NullNode node, Scope scope, RuntimeContext context) {
        return nullEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(RsonNode node, Scope scope, RuntimeContext context) {
        return rsonEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ThisNode node, Scope scope, RuntimeContext context) {
        return thisEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(TypeOfNode node, Scope scope, RuntimeContext context) {
        return typeOfEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BlockNode node, Scope scope, RuntimeContext context) {
        return blockEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IfStatementNode node, Scope scope, RuntimeContext context) {
        return ifStatementEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(AssignNode node, Scope scope, RuntimeContext context) {
        return assignEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(WhileStatementNode node, Scope scope, RuntimeContext context) {
        return whileStatementEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ForEachStatementNode node, Scope scope, RuntimeContext context) {
        return forEachStatementEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(BreakNode node, Scope scope, RuntimeContext context) {
        return breakEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ContinueNode node, Scope scope, RuntimeContext context) {
        return continueEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(CallNode node, Scope scope, RuntimeContext context) {
        return callEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(IndexNode node, Scope scope, RuntimeContext context) {
        return indexEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(DotNode node, Scope scope, RuntimeContext context) {
        return dotEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ClosureDefineNode node, Scope scope, RuntimeContext context) {
        return closureEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ReturnNode node, Scope scope, RuntimeContext context) {
        return returnEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(VariableDefineNode node, Scope scope, RuntimeContext context) {
        return varDefineEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(ImportNode node, Scope scope, RuntimeContext context) {
        return importEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(RuleStatementNode node, Scope scope, RuntimeContext context) {
        return ruleStatementEval.eval(node, scope, context);
    }

    @Override
    public BaseValue eval(InfixDefinitionNode node, Scope scope, RuntimeContext context) {
        return infixDefinitionEval.eval(node, scope, context);
    }

    @Override
    public BaseValue evel(DefaultParamValueNode node, Scope scope, RuntimeContext context) {

        return super.evel(node, scope, context);
    }
}
