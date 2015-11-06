/**
 * Created by Sabryna on 10/6/15.
 */
abstract class TypeNumeric extends TypeBasic{

    public TypeNumeric(String strName, int size) {
        super(strName, size);
    }

    /*Overriden Type Checking Method*/

    @Override
    public boolean isNumeric()
    {
        return true;
    }
}
