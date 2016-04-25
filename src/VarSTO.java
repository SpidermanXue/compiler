//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

class VarSTO extends STO
{
	private boolean amper = false;
	private boolean array = false;
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public VarSTO(String strName)
	{
		super(strName);
		// You may want to change the isModifiable and isAddressable 
		// fields as necessary
	}

	public VarSTO(String strName, Type typ)
	{
		super(strName, typ);
		// You may want to change the isModifiable and isAddressable 
		// fields as necessary
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isVar() 
	{
		return true;
	}

	public void setamper(){ this.amper = true; }
	public boolean getamper(){return amper;}
	public void SetAsArray(){ this.array = true;}
	public boolean IfIsArray(){return this.array;}
}
