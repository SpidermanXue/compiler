//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.*;

class SymbolTable
{
	private Stack<Scope> m_stkScopes;
	private int m_nLevel;
	private Scope m_scopeGlobal;
	private FuncSTO m_func = null;
	private StructdefSTO m_struct = null;
    
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public SymbolTable()
	{
		m_nLevel = 0;
		m_stkScopes = new Stack<Scope>();
		m_scopeGlobal = null;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void insert(STO sto)
	{
		Scope scope = m_stkScopes.peek();
		scope.InsertLocal(sto);
	}

    public void insertGlobal(STO sto)
    {
        m_scopeGlobal.InsertLocal(sto);
    }

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO accessGlobal(String strName)
	{
		return m_scopeGlobal.access(strName);
	}

	public Vector<STO> accessFunc(String strName){ //return function group
		return m_scopeGlobal.findfunc(strName);
	}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO accessLocal(String strName)
	{
		Scope scope = m_stkScopes.peek();
		return scope.accessLocal(strName);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO access(String strName)
	{ //return function sto
		Stack stk = new Stack();
		Scope scope;
		STO stoReturn = null;
//		int length = m_stkScopes.size();
//		int i = 0;
//		while(!m_stkScopes.empty()){
//			scope = m_stkScopes.pop();
//			if ((stoReturn = scope.access(strName)) != null)
//				return stoReturn;
//
//
//		}
//		return null;

		//sabryna
		Object[] scopeArray = m_stkScopes.toArray();
		int scopeSize = m_stkScopes.size();

		while (scopeSize != 0)
		{
			scope = (Scope) scopeArray[scopeSize-1];
			if ((stoReturn = scope.access(strName)) != null){
				return stoReturn;
			}
			scopeSize --;
		}

//		for (Enumeration<Scope> e = m_stkScopes.elements(); e.hasMoreElements();)
//		{
//			scope = e.nextElement();
//			if ((stoReturn = scope.access(strName)) != null)
//				return stoReturn;
//		}

		return null;
	}

	public Scope accessLevel(int level)
	{
		return this.m_stkScopes.elementAt(level);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void openScope()
	{
		Scope scope = new Scope();

		// The first scope created will be the global scope.
		if (m_scopeGlobal == null)
			m_scopeGlobal = scope;

        if(!m_stkScopes.empty()){
            if(this.getWhile()){
                scope.setWhile();
            }

            if(this.getForEach()){
                scope.setForEach();
            }
        }

		m_stkScopes.push(scope);
		m_nLevel++;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void closeScope()
	{
	//	System.out.println("level # " + m_nLevel);
		m_stkScopes.pop();
		m_nLevel--;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int getLevel()
	{
		return m_nLevel;
	}


	//----------------------------------------------------------------
	//	This is the function currently being parsed.
	//----------------------------------------------------------------
	public FuncSTO getFunc() { return m_func; }

	public void setFunc(FuncSTO sto) { m_func = sto; }

	public StructdefSTO getStruct() { return m_struct; }

	public void setStruct(StructdefSTO sto) { m_struct = sto; }

	public void setForEach()
	{
		m_stkScopes.peek().setForEach();
	}

	public void setWhile()
	{
		m_stkScopes.peek().setForEach();
	}

	public void setIf(){m_stkScopes.peek().setIf();}

	public boolean getForEach()
	{
		return m_stkScopes.peek().getForEach();
	}

	public boolean getWhile()
	{
		return m_stkScopes.peek().getWhile();
	}

	public boolean getIf(){return m_stkScopes.peek().getIf();}
}
