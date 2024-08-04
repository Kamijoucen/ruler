package com.kamijoucen.ruler.eval;

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
import com.kamijoucen.ruler.common.AbstractVisitor;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.eval.expression.AssignEval;
import com.kamijoucen.ruler.eval.expression.BlockEval;
import com.kamijoucen.ruler.eval.expression.CallEval;
import com.kamijoucen.ruler.eval.expression.ClosureEval;
import com.kamijoucen.ruler.eval.expression.DefaultParamValEval;
import com.kamijoucen.ruler.eval.expression.DotEval;
import com.kamijoucen.ruler.eval.expression.ForEachStatementEval;
import com.kamijoucen.ruler.eval.expression.IfStatementEval;
import com.kamijoucen.ruler.eval.expression.ImportEval;
import com.kamijoucen.ruler.eval.expression.IndexEval;
import com.kamijoucen.ruler.eval.expression.InfixDefinitionEval;
import com.kamijoucen.ruler.eval.expression.RuleStatementEval;
import com.kamijoucen.ruler.eval.expression.VariableEval;
import com.kamijoucen.ruler.eval.expression.WhileStatementEval;
import com.kamijoucen.ruler.eval.factor.ArrayEval;
import com.kamijoucen.ruler.eval.factor.BinaryOperationEval;
import com.kamijoucen.ruler.eval.factor.BooleanEval;
import com.kamijoucen.ruler.eval.factor.BreakEval;
import com.kamijoucen.ruler.eval.factor.ContinueEval;
import com.kamijoucen.ruler.eval.factor.DoubleEval;
import com.kamijoucen.ruler.eval.factor.IntegerEval;
import com.kamijoucen.ruler.eval.factor.NameEval;
import com.kamijoucen.ruler.eval.factor.NullEval;
import com.kamijoucen.ruler.eval.factor.OutNameEval;
import com.kamijoucen.ruler.eval.factor.ReturnEval;
import com.kamijoucen.ruler.eval.factor.RsonEval;
import com.kamijoucen.ruler.eval.factor.StringEval;
import com.kamijoucen.ruler.eval.factor.ThisEval;
import com.kamijoucen.ruler.eval.factor.TypeOfEval;
import com.kamijoucen.ruler.eval.factor.UnaryOperationEval;
import com.kamijoucen.ruler.runtime.Environment;
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
    private static final DefaultParamValEval defaultParamValEval = new DefaultParamValEval();

    private final RulerConfiguration configuration;
    private final RuntimeContext context;
    private final Environment environment;

    public EvalVisitor(RulerConfiguration configuration) {
        this.configuration = configuration;
        this.context = this.configuration.createDefaultRuntimeContext(null);
        this.environment = new Environment(configuration.getGlobalScope());
    }

    @Override
    public BaseValue eval(NameNode node) {
        return nameEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(OutNameNode node) {
        return outNameEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(IntegerNode node) {
        return integerEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(DoubleNode node) {
        return doubleEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(BoolNode node) {
        return booleanEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(StringNode node) {
        return stringEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(BinaryOperationNode node) {
        return binaryOperationEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(UnaryOperationNode node) {
        return unaryOperationEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ArrayNode node) {
        return arrayEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(NullNode node) {
        return nullEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(RsonNode node) {
        return rsonEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ThisNode node) {
        return thisEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(TypeOfNode node) {
        return typeOfEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(BlockNode node) {
        return blockEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(IfStatementNode node) {
        return ifStatementEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(AssignNode node) {
        return assignEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(WhileStatementNode node) {
        return whileStatementEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ForEachStatementNode node) {
        return forEachStatementEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(BreakNode node) {
        return breakEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ContinueNode node) {
        return continueEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(CallNode node) {
        return callEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(IndexNode node) {
        return indexEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(DotNode node) {
        return dotEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ClosureDefineNode node) {
        return closureEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ReturnNode node) {
        return returnEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(VariableDefineNode node) {
        return varDefineEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(ImportNode node) {
        return importEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(RuleStatementNode node) {
        return ruleStatementEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(InfixDefinitionNode node) {
        return infixDefinitionEval.eval(node, environment, context, this);
    }

    @Override
    public BaseValue eval(DefaultParamValNode node) {
        return defaultParamValEval.eval(node, environment, context, this);
    }
}
