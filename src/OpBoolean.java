/**
 * Created by Sabryna on 10/8/15.
 */
public class OpBoolean extends OpBinary {

    public OpBoolean(String op, String opType) {
        super(op, opType);
    }

    @Override
    public STO checkOperands(STO a, STO b) {
        Type one = a.getType();
        Type two = b.getType();
        int intResult = 0;

        // Type Error Checking
        if(!(one instanceof TypeBool)){
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1w_Expr, one.getName(), this.getOp(), "bool"));
        }
        if(!(two instanceof TypeBool)){
            return new ErrorSTO(b.getName(), Formatter.toString(
                    ErrorMsg.error1w_Expr, two.getName(), this.getOp(), "bool"));
        }

        // Constant Folding
        if ((a instanceof ConstSTO) && (b instanceof ConstSTO)) {
            int aVal = ((ConstSTO) a).getIntValue();
            int bVal = ((ConstSTO) b).getIntValue();

            switch (this.getOp()) {
                case "&&":
                    intResult = (aVal == 1 && bVal == 1)?1:0;
                    break;
                case "||":
                    intResult = (aVal == bVal)?aVal:1;
                    break;
            }
            return new ConstSTO("bool", new TypeBool("bool", 4), intResult);
        }

        return new ExprSTO("bool", new TypeBool("bool", 4));
    }
}
