/**
 * Created by Sabryna on 10/10/15.
 */
public class TypeStruct extends TypeComposite {
    public TypeStruct(String strName, int size) {
        super(strName, size);
    }

    @Override
    public boolean isAssignableTo(Type t) {
        if (t instanceof TypeStruct) {
            if (this.getName().equals(t.getName())) {
                return true;
            }
        }
        //TODO implement method
        return false;
    }

    @Override
    public boolean isEquivalentTo(Type t) {
        if (t instanceof TypeStruct) {
            if (this.getName().equals(t.getName())) {
                return true;
            }
        }
        //TODO implement method
        return false;
    }
}
