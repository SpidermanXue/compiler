/**
 * Created by Sabryna on 10/8/15.
 */
public class OpUnary extends Operator {
    public OpUnary(String op, String opType) {
        super(op, opType);
    }

    @Override
    public STO checkOperands(STO a, STO b)
    {
        return null;
    }

    public STO checkOperands(STO a)
    {
        switch (this.getOp())
        {
            case "++":
                return this.doIncDec(a);
            case "--":
                return this.doIncDec(a);
            case "!":
                return this.doNot(a);
        }
        return new ErrorSTO(a.getName(), "The author of the compiler did something wrong in rc.cup");
    }

    private STO doIncDec(STO a)
    {
        Type aType = a.getType();

        // Type Error Checking


        if (!(aType instanceof TypeNumeric || aType instanceof TypePointer)) {
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error2_Type, aType.getName(), this.getOp()));
        }

        if(!a.isModLValue()){
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error2_Lval, this.getOp()));
        }

        // Constant Folding
        if (a instanceof ConstSTO) {
            int result1 = ((ConstSTO) a).getIntValue();
            float result2 = ((ConstSTO) a).getFloatValue();

            if (this.getOp().equals("++")) {
                result1++;
                result2++;
            } else {
                result1--;
                result2--;
            }

            if (aType instanceof TypeInt) {
                return new ConstSTO(a.getName(), aType, result1);
            } else {
                return new ConstSTO(a.getName(), aType, result2);
            }
        }
        //should be an R value
        return new ExprSTO(a.getName(), aType);
    }

    private STO doNot(STO a)
    {
        Type aType = a.getType();

        // Type Error Checking
        if (!(aType instanceof TypeBool)) {
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1u_Expr, a.getType().getName(), this.getOp(), "bool"));
        }

        // Constant Folding
        if (a instanceof ConstSTO) {
            int aVal = ((ConstSTO) a).getIntValue();
            aVal = (aVal == 1)?0:1;

            return new ConstSTO(a.getName(), new TypeBool("bool", 4), aVal);
        }

        return new ExprSTO(a.getName(), new TypeBool("bool", 4));
    }
}
