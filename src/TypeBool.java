/**
 * Created by Sabryna on 10/6/15.
 */
public class TypeBool extends TypeBasic {

    public TypeBool(String strName, int size) {
        super(strName, size);
    }

    @Override
    public boolean isAssignableTo(Type t) {
        //TODO implement method
        if(t instanceof TypeBool) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEquivalentTo(Type t) {
        //TODO implement method
        if (t instanceof TypeBool) {
            return true;
        }
        return false;
    }

    /*Overriden Type Checking Method*/
    @Override
    public boolean isBool()
    {
        return true;
    }
}
