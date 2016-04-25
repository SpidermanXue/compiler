/**
 * Created by Sabryna on 10/8/15.
 */
public class OpBitwise extends OpBinary {

    public OpBitwise(String op, String opType) {
        super(op, opType);
    }

    @Override
    public STO checkOperands(STO a, STO b)
    {
        Type one = a.getType();
        Type two = b.getType();
        int intResult = 0;

        // Type Error Checking
        if (!(one instanceof TypeInt)) {
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1w_Expr, one.getName(), this.getOp(), "int"));
        }
        if (!(two instanceof TypeInt)) {
            return new ErrorSTO(b.getName(), Formatter.toString(
                    ErrorMsg.error1w_Expr, two.getName(), this.getOp(), "int"));
        }

        // Constant Folding
        if ((a instanceof ConstSTO) && (b instanceof ConstSTO)) {
            int aVal = ((ConstSTO) a).getIntValue();
            int bVal = ((ConstSTO) b).getIntValue();

            switch (this.getOp()) {
                case "&":
                    intResult = aVal & bVal;
                    break;
                case "^":
                    intResult = aVal ^ bVal;
                    break;
                case "|":
                    intResult = aVal | bVal;
                    break;
            }
            return new ConstSTO("int", new TypeInt("int", 4), intResult);
        }

        return new ExprSTO (a, b, m_op,  new TypeInt("int", 4), "int");

    }
}
