//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.Vector;

class FuncSTO extends STO
{
	private Type m_returnType;
    private boolean amper = false;
    private boolean returnflag = false;
	private Vector<STO> param;

	private boolean condidate = false;

    private boolean structFuncFlag = false;
    private StructdefSTO struct;
	private int varSize;
	private boolean IfdeclinAss = false;
    //-------
	// --------------------------------------------------------
	//
	//----------------------------------------------------------------
	public FuncSTO(String strName, Type t)
	{
		super (strName);
		setReturnType(t);
		// You may want to change the isModifiable and isAddressable                      
		// fields as necessary
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isFunc() 
	{ 
		return true;
		// You may want to change the isModifiable and isAddressable                      
		// fields as necessary
	}

	//----------------------------------------------------------------
	// This is the return type of the function. This is different from 
	// the function's type (for function pointers - which we are not 
	// testing in this project).
	//----------------------------------------------------------------
	public void setReturnType(Type typ)
	{
		m_returnType = typ;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Type getReturnType ()
	{
		return m_returnType;
	}

    public void setIsStructFunc(boolean v){
        this.structFuncFlag = true;
    }

    public boolean isStructFunc()
    {
        return this.structFuncFlag;
    }

    //Specifiy the struct it belongs to
    public void setStruct(StructdefSTO sto) {

        this.struct = sto;
    }
    public StructdefSTO getStruct()
    {
        return this.struct;
    }


    public void setreturnflag(){ this.returnflag = true; }
	public void setreturnflag(boolean t){ this.returnflag = t; } //require return

	public boolean getreturnflag(){return returnflag;}

    public void setamper(){ this.amper = true; }
    public boolean getamper(){return amper;}

	public void setParameter(Vector<STO> para){
		//this.param = new Vector<STO>(para);
        this.param = para;
    }
	public Vector<STO> getParameter(){return this.param;}

	public void setCondidate(boolean b){this.condidate = b;}
	public boolean getCondidate(){return this.condidate;}

	@Override
	public void setScopeLevel (int level)
	{
		this.scopeLevel = level;
	}

	@Override
	public int getScopeLevel()
	{
		return this.scopeLevel;
	}

//	public int getVarSize()
//	{
//		return this.varSize;
//	}

	public void setFuncVarSize(int a) {
		this.varSize = a;
	}
	public int getFuncVarSize(){
		return this.varSize;
	}

	public void FuncDecled(boolean x){this.IfdeclinAss = x;}
	public boolean getifdecl(){return this.IfdeclinAss;}
}
