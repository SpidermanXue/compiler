/**
 * Created by Sabryna on 10/6/15.
 */
public class TypeInt extends TypeNumeric {

    public TypeInt(String strName, int size) {
        super(strName, size);
    }

    @Override
    public boolean isAssignableTo(Type t) {
        //TODO implement method
        if ((t instanceof TypeInt) || (t instanceof TypeFloat)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEquivalentTo(Type t) {
        //TODO implement method
        if (t instanceof TypeInt) {
            return true;
        }
        return false;
    }
/*
    @Override
    public boolean isNumeric()
    {
        return true;
    }
  */
    /*Overriden Type Checking Method*/
    @Override
    public boolean isInt()
    {
        return true;
    }


}
