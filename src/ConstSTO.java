//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.math.BigDecimal;

class ConstSTO extends STO
{
    //----------------------------------------------------------------
    //	Constants have a value, so you should store them here.
    //	Note: We suggest using Java's BigDecimal class, which can hold
    //	floats and ints. You can then do .floatValue() or 
    //	.intValue() to get the corresponding value based on the
    //	type. Booleans/Ptrs can easily be handled by ints.
    //	Feel free to change this if you don't like it!
    //----------------------------------------------------------------
    public BigDecimal		m_value;

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public ConstSTO(String strName)
	{
		super(strName);
		m_value = null; // fix this
		//this.setIsAddressable(true);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ)
	{
		super(strName, typ);
		m_value = null; // fix this
		//this.setIsAddressable(true);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ, int val)
	{
		super(strName, typ);
		//System.out.println("input value:::" + val);
		m_value = new BigDecimal(val);
		//this.setIsAddressable(true);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ, double val)
	{
		super(strName, typ);
		m_value = new BigDecimal(val);
		//this.setIsAddressable(true);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

    public ConstSTO(String strName, Type typ, float val){
        super(strName, typ);
        m_value = new BigDecimal(val);
        //this.setIsAddressable(true);
    }

	public ConstSTO(String strName, Type typ, BigDecimal val){
		super(strName, typ);
		m_value = val;
		//this.setIsAddressable(true);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isConst() 
	{
		return true;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public BigDecimal getValue() 
	{
		return m_value;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int getIntValue() 
	{
		return m_value.intValue();
	}

	public void unaryValue(){

		this.m_value = this.m_value.negate();
	}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public float getFloatValue() 
	{
		return m_value.floatValue();
	}

    /* Get Float as a string in the format use by rc.s file (ex: 1.78 becomes 0r1.78)*/
	public String getFloatValue_As()
	{
		return "0r"+Float.toString(m_value.floatValue());
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean getBoolValue() 
	{
		return !BigDecimal.ZERO.equals(m_value);
	}

    public String getBoolValue_As()
    {
        int tmp = this.getBoolValue()?1:0;
        return String.valueOf(tmp);
    }


}
