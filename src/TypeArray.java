import java.util.Vector;

/**
 * Created by Sabryna on 10/10/15.
 */
public class TypeArray extends TypeComposite {
    Type m_ContainerType;
    Vector<Integer> domain;
    STO arrayElement;
    int indexSize;

    int level;

    public TypeArray(String strName, int size)
    {
        super(strName, size);
    }

    public TypeArray(String strName, int size, Type lastType, Vector<Integer> indexDomain)
    {
        super(strName, size);
        this.indexSize = size;

        StringBuilder a = new StringBuilder();
        a.append(lastType.getName());
        for (int arraySize: indexDomain) {
            String tempArray = "[" + arraySize + "]";
            a.append(tempArray);
        }
        this.setName(a.toString());

        this.domain = indexDomain;
        int tempSize = indexDomain.size();

        if (tempSize > 1){
            indexDomain.remove(0);
            this.setContainerType(new TypeArray(
                    strName, indexDomain.firstElement(), lastType, indexDomain));
            this.setSize(this.getContainerType().getSize()*this.indexSize);

            this.arrayElement = new VarSTO(
                    this.getContainerType().getName(), this.getContainerType());
            this.arrayElement.setIsAddressable(true);
        } else {
            this.setContainerType(lastType);
            this.setSize(lastType.getSize()*this.indexSize);
            this.arrayElement = new VarSTO(lastType.getName(), lastType );
            this.arrayElement.setIsAddressable(true);
            this.arrayElement.setIsModifiable(true);
        }
    }

    @Override
    public boolean isAssignableTo(Type t) {

        if(!(t instanceof TypeArray)){
            return false;
        }

        if(!(this.getIndexSize() == ((TypeArray) t).getIndexSize())) {
            return false;
        }

        return this.getContainerType().isEquivalentTo(((TypeArray) t).getContainerType());
    }

    @Override
    public boolean isEquivalentTo(Type t) {

        if(!(t instanceof TypeArray)){
            return false;
        }

        if(!(this.getIndexSize() == ((TypeArray) t).getIndexSize())) {
            return false;
        }

        return this.getContainerType().isEquivalentTo(((TypeArray) t).getContainerType());
    }

    public void setDomain(Vector <Integer> v){
        this.domain = v;
    }
    public Vector<Integer> getDomain(){
        return this.domain;
    }

    public void setElement(STO element)
    {
        this.arrayElement = element;
    }

    public STO getElement()
    {
       return this.arrayElement;
    }


    public void setLevel(int a){
        this.level = a;
    }
    public int getLevel(){
        return this.level;
    }

    public void setContainerType(Type t)
    {
        this.m_ContainerType = t;
    }

    public Type getContainerType()
    {
        return this.m_ContainerType;
    }

    public int getIndexSize()
    {
        return this.indexSize;
    }
}
