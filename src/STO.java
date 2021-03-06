//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

abstract class STO
{
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	protected int scopeLevel;
	protected boolean isGlobal;
    protected boolean isStatic;
    protected String offset;
	protected String newUpdate;
	protected boolean UpdateFlag = false ;
	private boolean thisflag = false;
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO(String strName)
	{
		this(strName, null);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO(String strName, Type typ)
	{
		setName(strName);
		setType(typ);
		setIsAddressable(false);
		setIsModifiable(false);
        this.offset = "";
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public String getName()
	{
		return m_strName;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setName(String str)
	{
		m_strName = str;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Type getType()
	{
		return m_type;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	private void setType(Type type)
	{
		m_type = type;
	}

	//----------------------------------------------------------------
	// Addressable refers to if the object has an address. Variables
	// and declared constants have an address, whereas results from 
	// expression like (x + y) and literal constants like 77 do not 
	// have an address.
	//----------------------------------------------------------------
	public boolean getIsAddressable()
	{
		return m_isAddressable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setIsAddressable(boolean addressable)
	{
		m_isAddressable = addressable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean getIsModifiable()
	{
		return m_isModifiable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setIsModifiable(boolean modifiable)
	{
		m_isModifiable = modifiable;
	}

	//----------------------------------------------------------------
	// A modifiable L-value is an object that is both addressable and
	// modifiable. Objects like constants are not modifiable, so they 
	// are not modifiable L-values.
	//----------------------------------------------------------------
	public boolean isModLValue()
	{
		return getIsModifiable() && getIsAddressable();
	}

	//----------------------------------------------------------------
	//	It will be helpful to ask a STO what specific STO it is.
	//	The Java operator instanceof will do this, but these methods 
	//	will allow more flexibility (ErrorSTO is an example of the
	//	flexibility needed).
	//----------------------------------------------------------------
	public boolean isVar() { return false; }
	public boolean isConst() { return false; }
	public boolean isExpr() { return false; }
	public boolean isFunc() { return false; }
	public boolean isStructdef() { return false; }
	public boolean isError() { return false; }

	public void setScopeLevel (int level)
	{
		this.scopeLevel = level;
	}

	public int getScopeLevel()
	{
		return this.scopeLevel;
	}

    public void setStatic(boolean optStatic)
    {
        this.isStatic = optStatic;
    }

    public boolean getStatic()
    {
        return this.isStatic;
    }

    public void setGlobal(boolean isGlobal)
    {
        this.isGlobal = isGlobal;
    }

    public boolean getGlobal()
    {
        return this.isGlobal;
    }

    public void setOffset(String offset)
    {
        this.offset = offset;
    }
    public String getOffset()
    {
        return this.offset;
    }

	public void setUpdateOffset(String offset){
		this.newUpdate = offset;
	}

	public String getUpdatedOffset(){
		return this.newUpdate;
	}

	public void setUpdateFlag(boolean a){
		this.UpdateFlag = a;
	}

	public boolean GetUpdateFlag(){
		return this.UpdateFlag;
	}

	public void SetThis(){ this.thisflag = true;}
	public boolean ifthis(){return this.thisflag;}
}
