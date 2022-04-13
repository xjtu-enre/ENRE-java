package visitor;

import org.eclipse.jdt.core.dom.*;
import util.Configure;
import util.SingleCollect;

public class CKVisitor extends ASTVisitor {

    public SingleCollect singleCollect = SingleCollect.getSingleCollectInstance();

    @Override
    public boolean visit(TryStatement node) {
        //ck
        singleCollect.addCk(Configure.TRY_CATCHES, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(NumberLiteral node){
        //ck
        singleCollect.addCk(Configure.NUMBER, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(InfixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(PrefixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }

    @Override
    public boolean visit(PostfixExpression node){
        //ck
        singleCollect.addCk(Configure.MATH_OPERATIONS, 1);

        return super.visit(node);
    }
}
