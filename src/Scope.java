//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.Vector;

class Scope
{
	public Vector<STO> m_lstLocals;
	private boolean whileFlag = false;
	private boolean forEachFlag = false;
	private boolean ifFlag = false;

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Scope()
	{
		m_lstLocals = new Vector<STO>();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO access(String strName)
	{
		return accessLocal(strName);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO accessLocal(String strName)
	{
		STO sto = null;

		for (int i = 0; i < m_lstLocals.size(); i++)
		{
			sto = m_lstLocals.elementAt(i);

			if (sto.getName().equals(strName))
				return sto;
		}

		return null;
	}

	public Vector<STO> findfunc(String strName)
	{
		STO sto = null;
		Vector<STO> findFun = new Vector<>();

		for (int i = 0; i < m_lstLocals.size(); i++)
		{
			sto = m_lstLocals.elementAt(i);

			if (sto.getName().equals(strName)){
				findFun.addElement(sto);
			}

		}
		return findFun;
	}

	public Vector<STO> findfuncStr(String strName)
	{
		STO sto = null;
		Vector<STO> findFun = new Vector<>();

		for (int i = 0; i < m_lstLocals.size(); i++)
		{
			sto = m_lstLocals.elementAt(i);

			if (sto.getName().equals(strName) && (sto instanceof FuncSTO)){
				findFun.addElement(sto);
			}

		}
		return findFun;
		//return null;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void InsertLocal(STO sto)
	{
		m_lstLocals.addElement(sto);
	}

	public Vector<STO> getSTO()
	{
		return this.m_lstLocals;
	}

	public void setWhile()
	{
		this.whileFlag = true;
	}

	public void setForEach()
	{
		this.forEachFlag = true;
	}

	public void setIf(){this.ifFlag = true;}

	public boolean getWhile()
	{
		return this.whileFlag;
	}

	public boolean getForEach()
	{
		return this.forEachFlag;
	}

	public boolean getIf(){return this.ifFlag;}

}
