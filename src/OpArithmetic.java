import java.math.BigDecimal;

/**
 * Created by Sabryna on 10/8/15.
 */
public class OpArithmetic extends OpBinary {
    public OpArithmetic(String op, String opType) {
        super(op, opType);
    }

    @Override
    public STO checkOperands(STO a, STO b)
    {
        Type one = a.getType();
        Type two = b.getType();

        // Type Error Checking
        if (this.m_op.equals("%")) {
            if (!(one instanceof TypeInt)) {
                return new ErrorSTO(one.getName(), Formatter.toString(
                        ErrorMsg.error1w_Expr, one.getName(), this.m_op, "int"));
            }
            if (!(two instanceof TypeInt)) {
                return new ErrorSTO(two.getName(), Formatter.toString(
                        ErrorMsg.error1w_Expr, two.getName(), this.m_op, "int"));
            }
        } else {
            if (!(one instanceof TypeNumeric)) {
                return new ErrorSTO(one.getName(), Formatter.toString(
                        ErrorMsg.error1n_Expr, one.getName(), this.m_op));
            }

            if (!(two instanceof TypeNumeric)) {
                return new ErrorSTO(two.getName(), Formatter.toString(
                        ErrorMsg.error1n_Expr, two.getName(), this.m_op));
            }
        }

        // Constant Folding
        if ((a instanceof ConstSTO) && (b instanceof ConstSTO)) {
            int result1 = 0;
            float result2 = 0;

            int aIntVal = ((ConstSTO) a).getIntValue();
            int bIntVal = ((ConstSTO) b).getIntValue();
            float aFloatVal = ((ConstSTO) a).getFloatValue();
            float bFloatVal = ((ConstSTO) b).getFloatValue();

            switch(m_op){
                case "+":
                    result1 = aIntVal + bIntVal;
                    result2 = aFloatVal + bFloatVal;
                    break;
                case "-":
                    result1 = aIntVal - bIntVal;
                    result2 = aFloatVal - bFloatVal;
                    break;
                case "*":
                    result1 = aIntVal * bIntVal;
                    result2 = aFloatVal * bFloatVal;
                    break;
                case "/":
                    // Check Arithmetic Exception
                    if((((ConstSTO) b).getValue().compareTo(BigDecimal.ZERO)) == 0){
                        return new ErrorSTO(two.getName(), ErrorMsg.error8_Arithmetic);
                    }

                    if (two instanceof TypeInt) {
                        result1 = aIntVal/bIntVal;
                    }

                    result2 = aFloatVal / bFloatVal;
                    break;
                case "%":
                    if((((ConstSTO) b).getValue().compareTo(BigDecimal.ZERO)) == 0){
                        return new ErrorSTO(two.getName(), ErrorMsg.error8_Arithmetic);
                    }

                    result1 = aIntVal % bIntVal;
                    break;
            }

            if(one instanceof TypeInt && two instanceof TypeInt){
                return new ConstSTO ("int",  new TypeInt("int", 32), result1);
            }
            return new ConstSTO ("float", new TypeFloat("float", 32), result2);
        }

        //Implement operands checking logic here
        if (one instanceof TypeInt && two instanceof TypeInt) {
            return new ExprSTO ("int",  new TypeInt("int", 32));
        } else {
            return new ExprSTO ("float", new TypeFloat("float", 32));
        }
    }
}
