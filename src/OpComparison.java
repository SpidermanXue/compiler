/**
 * Created by Sabryna on 10/8/15.
 */
public class OpComparison extends OpBinary {

    public OpComparison (String op, String opType) {
        super(op, opType);
    }

    @Override
    public STO checkOperands(STO a, STO b)
    {
        STO result = null;

        // Type conflict check
        switch (this.getOp())
        {
            case "==":
                result = DoEquality(a, b);
                break;
            case "!=":
                result = DoEquality(a, b);
                break;
            case "<":
                result = DoRelation(a, b);
                break;
            case "<=":
                result = DoRelation(a, b);
                break;
            case ">":
                result = DoRelation(a, b);
                break;
            case ">=":
                result = DoRelation(a, b);
                break;
        }
        if (result != null) {
            return result;
        } else {
            // no need const folding and no error
            return new ExprSTO("bool", new TypeBool("bool", 4));
        }
    }

    /**
    *   Name: DoEquality(STO a, STO b)
    *   Description:
    *       DoEquality handles error or const folding
    *      for == or !=
    * */
    private STO DoEquality(STO a, STO b)
    {
        Type one = a.getType();
        Type two = b.getType();
        boolean flagNumeric = (one instanceof TypeNumeric) && (two instanceof TypeNumeric);
        boolean flagBool = (one instanceof TypeBool) && (two instanceof TypeBool);
        boolean flagPointer = (one instanceof TypePointer) && (two instanceof TypePointer);


        boolean result;
        int intResult = 0;

        // Type Error Checking
        if (!(flagNumeric || flagBool || flagPointer)) {

            if (one instanceof TypePointer || two instanceof TypePointer) {
                return new ErrorSTO(a.getName(), Formatter.toString(
                        ErrorMsg.error17_Expr, this.getOp(), one.getName(), two.getName()));
            }

            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1b_Expr, one.getName(), this.getOp(), two.getName()));
        }

        if (one instanceof TypePointer && two instanceof TypePointer) {
            if (one instanceof TypeNullPointer || two instanceof TypeNullPointer)
            {
                return null;
            }

            if (!one.isAssignableTo(two)) {
                return new ErrorSTO(a.getName(), Formatter.toString(
                        ErrorMsg.error17_Expr, this.getOp(), one.getName(), two.getName()));
            }
            return null;
        }

        // Constant Folding
        if ((a instanceof ConstSTO) && (b instanceof ConstSTO)) {
            if (one instanceof TypeNumeric) {
                if (this.getOp().equals("==")) {
                    result = (((ConstSTO) a).getFloatValue() == ((ConstSTO) b).getFloatValue());
                } else {
                    result = (((ConstSTO) a).getFloatValue() != ((ConstSTO) b).getFloatValue());
                }
            } else {
                if(this.getOp().equals("==")) {
                    result = (((ConstSTO) a).getIntValue() == ((ConstSTO) b).getIntValue());
                } else {
                    result = (((ConstSTO) a).getIntValue() != ((ConstSTO) b).getIntValue());
                }
            }

            if (result) {
                intResult = 1;
            }
            return new ConstSTO("bool", new TypeBool("bool", 4), intResult);
        }

        return null;
    }

    /**
     *   Name: DoEquality(STO a, STO b)
     *   Description:
     *       DoEquality handles error or const folding
     *      for >, <, >=, <=
     * */
    private STO DoRelation(STO a, STO b)
    {
        Type one = a.getType();
        Type two = b.getType();

        boolean result = false;
        int intResult = 0;

        // Type Error Checking
        if (!(one instanceof TypeNumeric)) {
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1n_Expr, one.getName(), this.getOp()));
        }
        if (!(two instanceof TypeNumeric)) {
            return new ErrorSTO(a.getName(), Formatter.toString(
                    ErrorMsg.error1n_Expr, two.getName(), this.getOp()));
        }

        // Constant Folding
        if ((a instanceof ConstSTO) && (b instanceof ConstSTO)) {
            float aVal = ((ConstSTO) a).getFloatValue();
            float bVal = ((ConstSTO) b).getFloatValue();

            switch (this.getOp()){
                case ">":
                    result = aVal > bVal;
                    break;
                case "<":
                    result = aVal < bVal;
                    break;
                case ">=":
                    result = aVal >= bVal;
                    break;
                case "<=":
                    result = aVal <= bVal;
                    break;
            }
            if (result) {
                intResult = 1;
            }
            return new ConstSTO("bool", new TypeBool("bool", 4), intResult);
        }
        return null;
    }
}
