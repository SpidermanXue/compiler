//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

//---------------------------------------------------------------------
// For structdefs
//---------------------------------------------------------------------

import java.util.Vector;

class StructdefSTO extends STO
{
	private Vector<STO> localVar = new Vector<STO>();
	private boolean constructor = false;
	private boolean destructor = false;
	private boolean finderror = false;

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public StructdefSTO(String strName)
	{
		super(strName);
	}

	public StructdefSTO(String strName, Type typ)
	{
		super(strName, typ);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isStructdef()
	{
		return true;
	}

	public void addVar(STO var){
		localVar.addElement(var);
	}
	public Vector<STO> getVar(){return this.localVar;}

	public void setConstruct(boolean t){this.constructor = t;}
	public boolean getConstruct(){return this.constructor;}


	public void setdestruct(boolean t){this.destructor = t;}
	public boolean getdestruct(){return this.destructor;}

	public void setError(){finderror = true;}
	public boolean getError(){return finderror;}



}
