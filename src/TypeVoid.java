/**
 * Created by Sabryna on 10/10/15.
 */
public class TypeVoid extends Type{

    public TypeVoid(String strName, int size) {
        super(strName, size);
    }

    @Override
    public boolean isAssignableTo(Type t) {
        //TODO implement method
        if (t instanceof TypeVoid) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEquivalentTo(Type t) {
        //TODO implement method
        if(t instanceof TypeVoid) {
            return true;
        }
        return false;
    }
}
