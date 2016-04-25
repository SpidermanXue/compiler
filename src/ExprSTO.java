//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.Vector;

class ExprSTO extends STO
{
//	private Vector <String> Operation = new Vector<String> ();
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO one;
	public STO two;
	public String operator;
	public Boolean funccall = false;
	public ExprSTO(String strName)
	{
		super(strName);
        // You may want to change the isModifiable and isAddressable
        // fields as necessary
	}

	public ExprSTO(String strName, Type typ)
	{
		super(strName, typ);
        // You may want to change the isModifiable and isAddressable
        // fields as necessary
	}

	public ExprSTO(STO one, STO two, String operator, Type typ, String strName){
		super(strName, typ);
		saveOne(one);
		saveTwo(two);
		saveOperator(operator);

	}
//	public ExprSTO(String strName, Type typ, Vector<String> op){
//		super(strName, typ);
//		//setOp(op);
//	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isExpr()
	{
		return true;
	}
	public void saveOne(STO a){
		this.one = a;
	}
	public void saveTwo(STO a){
		this.two = a;
	}
	public void saveOperator(String op){
		this.operator = op;
	}

	public STO getOne(){
		return this.one;
	}
	public STO getTwo(){
		return this.two;
	}
	public String getOperator(){
		return this.operator;
	}
	public Boolean IfFunCall(){
		return this.funccall;
	}
	public void SetFunCall(){
		this.funccall = true;
	}

}
