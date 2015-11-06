/**
 * Created by Sabryna on 10/8/15.
 */
abstract class Operator {

    protected String m_op;
    protected String m_opType;


    public Operator (String op, String opType)
    {
        setOp(op);
        setOpType(opType);
    }


    private void setOp(String op)
    {
        this.m_op = op;
    }

    private void setOpType(String opType)
    {
        this.m_opType = opType;
    }

    //get the m_op (i.e +, -, *...)
    public String getOp()
    {
        return m_op;
    }
    //get the m_opType (i.e. ArithmeticOP, BooleanOP...)
    public String getOpType() {
        return m_opType;
    }


    public STO checkOperands(STO a, STO b)
    {
        return null;
    }


}

