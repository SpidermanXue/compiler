/**
 * Created by Sabryna on 10/10/15.
 */
public class TypeNullPointer extends TypePointer {

    public TypeNullPointer(String strName, int size) {
        super(strName, size);
    }

    @Override
    public boolean isAssignableTo(Type t) {
        //TODO implement method

        return (t instanceof TypePointer);
    }

    @Override
    public boolean isEquivalentTo(Type t) {

        if(t instanceof TypeNullPointer){
            return true;
        }
        //TODO implement method
        return false;
    }


}
