/**
 * Created by Sabryna on 10/10/15.
 */
public class TypePointer extends TypeComposite {

    Type m_PointerType;
    STO pointerElement;

    public TypePointer(String strName, int size)
    {
        super(strName, size);
    }

    public TypePointer(String strName, int size, Type lastType, int starNum)
    {
        super(strName, size);

        int tempNum = starNum;
        StringBuilder a = new StringBuilder();
        a.append(lastType.getName());
        while (tempNum>0) {
            a.append("*");
            tempNum--;
        }
        this.setName(a.toString());

        if (starNum > 1) {
            starNum--;

            this.setPointerType(new TypePointer(strName, 4, lastType, starNum));

            this.pointerElement = new VarSTO(
                    this.getPointerType().getName(), this.getPointerType());
            this.pointerElement.setIsAddressable(true);
            this.pointerElement.setIsModifiable(true);

        } else {
            this.setPointerType(lastType);
            this.pointerElement = new VarSTO(
                    this.getPointerType().getName(), this.getPointerType());
            this.pointerElement.setIsAddressable(true);
            this.pointerElement.setIsModifiable(true);
        }
    }

    public TypePointer(String strName, int size, Type lastType, STO lastSTO, int starNum)
    {
        super(strName, size);

        int tempNum = starNum;
        StringBuilder a = new StringBuilder();
        a.append(lastType.getName());
        while (tempNum>0) {
            a.append("*");
            tempNum--;
        }
        this.setName(a.toString());

        if (starNum > 1) {
            starNum--;

            this.setPointerType(new TypePointer(strName, 4, lastType, lastSTO, starNum));

            this.pointerElement = new VarSTO(
                    this.getPointerType().getName(), this.getPointerType());
            this.pointerElement.setIsAddressable(true);
            this.pointerElement.setIsModifiable(true);

        } else {
            this.setPointerType(lastType);
            this.pointerElement = lastSTO;
            this.pointerElement.setIsAddressable(true);
            this.pointerElement.setIsModifiable(true);
        }
    }


    @Override
    public boolean isAssignableTo(Type t) {

        if (t instanceof TypePointer) {
            return this.getPointerType().isEquivalentTo(((TypePointer) t).getPointerType());
        }

        //TODO implement method
        return false;
    }

    @Override
    public boolean isEquivalentTo(Type t) {

        if(t instanceof TypePointer){
            return this.getPointerType().isEquivalentTo(((TypePointer) t).getPointerType());
        }

        //TODO implement method
        return false;
    }

    public void setPointerType(Type t)
    {
        this.m_PointerType = t;
    }

    public Type getPointerType()
    {
        return this.m_PointerType;
    }

    public STO getPointerElement()
    {
        return this.pointerElement;
    }
}
