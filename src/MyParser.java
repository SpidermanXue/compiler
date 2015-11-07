//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java_cup.runtime.*;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

class MyParser extends parser
{
    private Lexer m_lexer;
    private ErrorPrinter m_errors;
    private boolean m_debugMode;
    private int m_nNumErrors;
    private String m_strLastLexeme;
    private boolean m_bSyntaxError = true;
    private int m_nSavedLineNum;
    private SymbolTable m_symtab;
    private AssemblyCodeGenerator MyWriter;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public MyParser(Lexer lexer, ErrorPrinter errors, boolean debugMode)
    {
        m_lexer = lexer;
        m_symtab = new SymbolTable();
        m_errors = errors;
        m_debugMode = debugMode;
        m_nNumErrors = 0;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public boolean Ok()
    {
        return m_nNumErrors == 0;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public Symbol scan()
    {
        Token t = m_lexer.GetToken();

        //	We'll save the last token read for error messages.
        //	Sometimes, the token is lost reading for the next
        //	token which can be null.
        m_strLastLexeme = t.GetLexeme();

        switch (t.GetCode())
        {
            case sym.T_ID:
            case sym.T_ID_U:
            case sym.T_STR_LITERAL:
            case sym.T_FLOAT_LITERAL:
            case sym.T_INT_LITERAL:
                return new Symbol(t.GetCode(), t.GetLexeme());
            default:
                return new Symbol(t.GetCode());
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void syntax_error(Symbol s)
    {
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void report_fatal_error(Symbol s)
    {
        m_nNumErrors++;
        if (m_bSyntaxError)
        {
            m_nNumErrors++;
            //	It is possible that the error was detected
            //	at the end of a line - in which case, s will
            //	be null.  Instead, we saved the last token
            //	read in to give a more meaningful error
            //	message.
            m_errors.print(Formatter.toString(ErrorMsg.syntax_error, m_strLastLexeme));
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void unrecovered_syntax_error(Symbol s)
    {
        report_fatal_error(s);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void DisableSyntaxError()
    {
        m_bSyntaxError = false;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void EnableSyntaxError()
    {
        m_bSyntaxError = true;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String GetFile()
    {
        return m_lexer.getEPFilename();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int GetLineNum()
    {
        return m_lexer.getLineNumber();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void SaveLineNum()
    {
        m_nSavedLineNum = m_lexer.getLineNumber();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int GetSavedLineNum()
    {
        return m_nSavedLineNum;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoProgramStart()
    {
        m_symtab.openScope();
        MyWriter = new AssemblyCodeGenerator("rc.s");
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoProgramEnd()
    {
        m_symtab.closeScope();
        MyWriter.dispose();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------

    // Check 8a: Constant/Variable initialization
    void DoVarDecl(Boolean optStatic, String id, Type t, STO s, Vector <STO> array) {//SET STO, DECLARE VARIABLE, a[5] = 10; call here
        //int x , id x, t.getname = int , int x = y, y is s, v is for array
        STO sto;
        Vector<Integer> domain = new Vector<Integer>();
        boolean errFlag = false;
        if (optStatic == null) {
            optStatic = false;
        }
        // TypeArray
        if (array != null) {
            for (STO index : array) {
                if (index instanceof ErrorSTO) {
                    errFlag = true;
                    break;
                }
                if (index.getType() instanceof TypeInt) {
                    if (!(index instanceof ConstSTO)) {
                        m_nNumErrors++;
                        m_errors.print(ErrorMsg.error10c_Array);
                        errFlag = true;
                        break;
                    }
                    int range = ((ConstSTO) index).getIntValue();
                    if (range <= 0) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error10z_Array, range));
                        errFlag = true;
                        break;
                    }
                    domain.addElement(range);
                } else {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(
                            ErrorMsg.error10i_Array, index.getType().getName()));
                    errFlag = true;
                    break;
                }
            }
            if (errFlag) {
                sto = new ErrorSTO(id);
            } else {
                TypeArray newType = new TypeArray(t.getName(), domain.firstElement(), t, domain);
                sto = new VarSTO(id, newType);
                sto.setIsAddressable(true);
            }
            if (m_symtab.accessLocal(id) != null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
                return;
            }

            sto.setStatic(optStatic);
            sto.setGlobal(this.m_symtab.getLevel()==1);

            m_symtab.insert(sto);
            MyWriter.DoDecl(sto,s);
            return;
        } //end of TypeArray part

        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
        }

        sto = new VarSTO(id, t);
        if (t instanceof TypeInt || t instanceof TypeFloat || t instanceof TypeBool ||
                (t instanceof TypePointer && !(t instanceof TypeNullPointer)) ||
                t instanceof TypeStruct) {
            sto.setIsModifiable(true);
            sto.setIsAddressable(true);
        }
        //check initialization
        if (s != null) {
            if (!(s instanceof ErrorSTO)) {
                if (!s.getType().isAssignableTo(t)) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(
                            ErrorMsg.error8_Assign, s.getType().getName(), t.getName()));
                }
            }

        }

        sto.setStatic(optStatic);
        sto.setGlobal(this.m_symtab.getLevel()==1);
        m_symtab.insert(sto);
        MyWriter.DoDecl(sto, s);

    }

    //DoVarDecl helper function for pointer, called in rc.cup
    Type DoDecoratedBasicType(Type type, int pointerCount)
    {
        if (pointerCount == 0)
        {
            return type;
        } else {

            return new TypePointer("", 4, type, pointerCount);
        }
    }
    Type DoDecoratedStructType (Type type, int pointerCount)
    {
        if (pointerCount == 0)
        {
            return type;
        } else {
            StructdefSTO struct = (StructdefSTO) m_symtab.accessGlobal(type.getName());

            return new TypePointer("", 4, type, struct, pointerCount);
        }
    }
    // Check 8b : Const/variable initialization using auto keyword
    void doAutoDecl(Boolean optStatic, String id, STO exp){
        Type t = exp.getType();

        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        VarSTO sto = new VarSTO(id, t);
        if(t instanceof TypeInt || t instanceof TypeFloat || t instanceof TypeBool ||
                (t instanceof TypePointer && !(t instanceof TypeNullPointer))){
            sto.setIsModifiable(true);
            sto.setIsAddressable(true);
        }else{
            sto.setIsModifiable(false);
        }
        sto.setStatic(optStatic);
        sto.setGlobal(m_symtab.getLevel()==1);
        m_symtab.insert(sto);
    }


    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoExternDecl (String id)
    {
        if (m_symtab.accessLocal(id) != null)
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        VarSTO sto = new VarSTO(id);
        m_symtab.insert(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    // Check 8b
    void DoConstDecl (Boolean optStatic, Type t, String id, STO s)
    {
        // Check ErrorSTO
        if (s instanceof ErrorSTO) {
            return;
        }

        // Check Constant
        if (!(s instanceof ConstSTO)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error8_CompileTime, id));
            return;
        }

        // Handle Auto declaration
        if (t != null) {
            if(!s.getType().isAssignableTo(t)){
                m_nNumErrors++;
                m_errors.print(Formatter.toString(
                        ErrorMsg.error8_Assign, s.getType().getName(), t.getName()));
                return;
            }

        } else {
            if (m_symtab.accessLocal(id) != null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
                return;
            }

            ConstSTO temp = (ConstSTO) s;
            ConstSTO toInsert = null;

            if (temp.getType() instanceof TypeInt) {
                toInsert = new ConstSTO(id, temp.getType(), temp.getIntValue());
                toInsert.setIsAddressable(true);
            } else if (temp.getType() instanceof TypeFloat) {
                toInsert = new ConstSTO(id, temp.getType(), temp.getFloatValue());
                toInsert.setIsAddressable(true);
            } else if (temp.getType() instanceof TypeBool) {
                int i = 0;
                if (temp.getBoolValue()) {
                    i = 1;
                }

                toInsert = new ConstSTO (id, temp.getType(), i);
                toInsert.setIsAddressable(true);
            }

            m_symtab.insert(toInsert);
            return;
        }

        // Check Previous Declare
        if (m_symtab.accessLocal(id) != null)
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
        }

        ConstSTO m = (ConstSTO) s;
        ConstSTO sto = null;
        if (m.getType() instanceof TypeInt) {
             sto = new ConstSTO(id, t, m.getIntValue());
             sto.setIsAddressable(true);
        } else if (m.getType() instanceof  TypeFloat) {
             sto = new ConstSTO(id, t, m.getFloatValue());
             sto.setIsAddressable(true);
        } else if (m.getType() instanceof  TypeBool) {
            int i = 0;
            if(m.getBoolValue()){
                i = 1;
            }

            sto = new ConstSTO (id, t, i);
            sto.setIsAddressable(true);
        }

        sto.setStatic(optStatic);
        sto.setGlobal(m_symtab.getLevel()==1);
        m_symtab.insert(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoFuncDecl_1(String id, Type returntype, String reference)
    {
        FuncSTO sto = new FuncSTO(id, returntype);
        if (m_symtab.accessGlobal(id) != null)
        {
            sto.setCondidate(true);
        }
        sto.setReturnType(returntype);

        if(reference != null){ // check 6b2
            sto.setamper();
        }

        sto.setreturnflag();
        sto.setScopeLevel(m_symtab.getLevel()+1); //SET funcSTO vars level
        sto.setIsModifiable(false);
        sto.setIsAddressable(true);
        m_symtab.insertGlobal(sto); // save FUNCSTO TO global
        m_symtab.openScope();  //open one scope for those vars
        m_symtab.setFunc(sto); // save current funcSTO
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoFuncDecl_2()
    {
        m_symtab.closeScope();
        m_symtab.setFunc(null);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoFormalParams(Vector<STO> params)
    {
        Boolean flag = false;
        Vector<STO> funcGroup;
        FuncSTO currentFunc = m_symtab.getFunc();
        if(currentFunc == null) return;
        FuncSTO funtemp;
        int oneSize = 0;
        int twoSize = 0;
        Vector<STO> args;
        if (currentFunc == null) {
            m_nNumErrors++;
            m_errors.print ("internal: DoFormalParams says no proc!");
        }

        funcGroup = m_symtab.accessFunc(currentFunc.getName()); //get function group

        if(funcGroup.size() > 1) {
            for (STO fun : funcGroup) {

                if (!(fun instanceof FuncSTO)) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, fun.getName()));
                    return;
                }
                funtemp = (FuncSTO) fun;
                if (!funtemp.getCondidate()) { //pick those not candidate
                    args = funtemp.getParameter();
                    if (params == null && args == null) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, currentFunc.getName()));
                        return;
                    }

                    if (params != null ) {
                        oneSize = params.size();
                    }else{
                        oneSize = 0;
                    }

                    if(args !=null) {
                        twoSize = args.size();
                    }else{
                        twoSize = 0;
                    }
                    if ((oneSize == twoSize) && oneSize!=0 ) {
                        Iterator<STO> itrSto = params.iterator();
                        Iterator<STO> itrSto1 = args.iterator();
                        while (itrSto.hasNext()) {
                            flag = false;
                            VarSTO b = (VarSTO) itrSto.next(); //second one
                            STO a = itrSto1.next(); //input

                            if (!(a.getType().isEquivalentTo(b.getType()))) {
                                flag = true;
                                break;
                            }
                        }
                    } else {
                        flag = true;
                    }

                    if (!flag) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, currentFunc.getName()));
                        return;
                    }
                }
            }
        }

        // when just one or on same one
        currentFunc.setCondidate(false);
        m_symtab.getFunc().setParameter(params); // save to funcST
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoBlockOpen()
    {
        m_symtab.openScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoBlockClose()
    {
        m_symtab.closeScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoAssignExpr(STO to, STO from)
    {
        //Sabryna
        if (to instanceof ErrorSTO || from instanceof ErrorSTO) {
            return new ErrorSTO(to.getName());
        }

        if (!to.isModLValue())
        {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error3a_Assign);
            return new ErrorSTO(to.getName());
        } else {
            if (!from.getType().isAssignableTo(to.getType())) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(
                        ErrorMsg.error3b_Assign, from.getType().getName(), to.getType().getName()));
                return new ErrorSTO(to.getName());
            }
        }
        return to;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoFuncCall(STO sto, Vector<STO> args)
    {
        Vector<STO> params;
        boolean flag = true;
        Vector<STO> funcGroup = new Vector<STO>();
        STO result;

        STO toReturn;

        if(sto instanceof ErrorSTO) return sto;

        if(sto instanceof StructdefSTO) {
            Vector<STO> func = new Vector<STO>();
            StructdefSTO struct = (StructdefSTO) m_symtab.accessGlobal( sto.getType().getName() );
            Vector<STO> container = struct.getVar();
            for(STO a : container){
                if(a.getName().equals(sto.getName())){
                    func.add(a);
                }
            }
            funcGroup = func;
        } else if (!(sto instanceof FuncSTO)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.not_function, sto.getName()));
            return new ErrorSTO(sto.getName());
        } else if (((FuncSTO) sto).isStructFunc()) {
            Vector<STO> funcContainer = new Vector<STO>();
            Vector<STO> allVarContainer = ((FuncSTO) sto).getStruct().getVar();

            for (STO a : allVarContainer) {
                if (a.getName().equals(sto.getName()) && a instanceof FuncSTO ){
                    funcContainer.add(a);
                }
            }
            funcGroup = funcContainer;
        } else {
            funcGroup = m_symtab.accessFunc(sto.getName());
        }

        if (funcGroup.size() == 1) {
            return DoFuncCall1(sto, args);
        } else {
            for (STO fun : funcGroup) {
                flag = true;
                FuncSTO Func = (FuncSTO) fun; //function call
                params = Func.getParameter();

                if ((params != null) && (args != null)) {
                    if ((args.size() != params.size()) ) {
                        flag = false; // not match
                        continue;
                    } else { //same size
                        Iterator<STO> itrSto = params.iterator();
                        Iterator<STO> itrSto1 = args.iterator();

                        while (itrSto.hasNext()) {
                            VarSTO b = (VarSTO) itrSto.next();
                            STO a = itrSto1.next(); //input
                            if (b.getamper()) { //if has &
                                if (!a.getType().isEquivalentTo(b.getType())) {
                                    flag = false; //not match;
                                    break;
                                } else if (!a.getIsModifiable()) {
                                    if (!(a.getType() instanceof TypeArray)) {
                                        flag = false; //not match;
                                        break;
                                    }
                                }
                            } else {
                                if (!a.getType().isEquivalentTo(b.getType())) {
                                    flag = false; //not match;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if ((params != null && args == null) ||  (params == null && args != null)) {
                        //argument number doesn't match up
                        flag = false; //not match
                        //break;
                    }
                }

                if(flag) {
                    Type returnType = ((FuncSTO) fun).getReturnType();
                    toReturn = new ExprSTO(fun.getName(), returnType);
                    if(((FuncSTO) fun).getamper()){
                        toReturn.setIsAddressable(true);
                        toReturn.setIsModifiable(true);
                    }
                    return toReturn;
                    //break; //found, jump out for loop
                }
            }

//            if (!flag) {
//                m_nNumErrors++;
//                m_errors.print(Formatter.toString(ErrorMsg.error9_Illegal, sto.getName()));
//                return new ErrorSTO(sto.getName());
//            }

            //Type returnType = ((FuncSTO) sto).getReturnType();
            //STO toReturn2 = new ExprSTO(sto.getName(), returnType);
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error9_Illegal, sto.getName()));
            return new ErrorSTO(sto.getName());
            //return toReturn;
        }
    }

//check 5
    STO DoFuncCall1(STO sto, Vector<STO> args)
    {
        int oneSize, twoSize;
        Scope funcScope;
        Vector<STO> params = new Vector<STO>();
        if(sto instanceof ErrorSTO) return sto;

        FuncSTO currentFunc;
        currentFunc = (FuncSTO) m_symtab.accessGlobal(sto.getName());
        if(currentFunc != null) {
            params = currentFunc.getParameter();
        }

        if(sto instanceof ErrorSTO) return sto;

        if (!(sto instanceof FuncSTO))
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.not_function, sto.getName()));
            return new ErrorSTO(sto.getName());
        }

        params = ((FuncSTO) sto).getParameter();
        if (args != null) {
            twoSize = args.size();
        } else {
            twoSize = 0;
        }
        if (params != null) {
            oneSize = params.size();
        } else {
            oneSize = 0;
        }
        if (args != null) {
            if (twoSize != oneSize) {
                //argument number doesn't match up

                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, args.size(), oneSize));
                return new ErrorSTO(sto.getName());
            } else if (params != null ){
                boolean flag = false;
                Iterator<STO> itrSto = params.iterator();
                Iterator<STO> itrSto1 = args.iterator();

                while (itrSto.hasNext()) {
                    VarSTO b = (VarSTO) itrSto.next();
                    STO a = itrSto1.next(); //input

                    if (a instanceof ErrorSTO) {
                        continue;
                    }

                    if (b.getamper()) {
                        if (!a.getType().isEquivalentTo(b.getType())) {
                            flag = true;
                            m_nNumErrors++;
                            m_errors.print(Formatter.toString(ErrorMsg.error5r_Call, a.getType().getName(), b.getName(), b.getType().getName()));
                        } else if(!a.getIsModifiable()) {
                            if (!(a.getType() instanceof TypeArray)) {
                                flag = true;
                                m_nNumErrors++;
                                m_errors.print(Formatter.toString(ErrorMsg.error5c_Call, b.getName(),b.getType().getName()));
                            }
                        }
                    } else {
                        if (!a.getType().isAssignableTo(b.getType())) {
                            flag = true;
                            m_nNumErrors++;
                            m_errors.print(Formatter.toString(ErrorMsg.error5a_Call, a.getType().getName(), b.getName(), b.getType().getName()));
                        }
                    }
                }
                if(flag) return new ErrorSTO(sto.getName());
            }
        } else {
            if (oneSize!= 0) {
                //argument number doesn't match up
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, 0, oneSize));
                return new ErrorSTO(sto.getName());
            }
        }

        Type returnType = ((FuncSTO) sto).getReturnType();

        STO toReturn = new ExprSTO(sto.getName(), returnType);

        if(((FuncSTO) sto).getamper()){
            toReturn.setIsAddressable(true);
            toReturn.setIsModifiable(true);
        }
        return toReturn;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoDesignator2_Dot(STO sto, String strID)
    {
        StructdefSTO struct = null;

        boolean found = false;
        boolean flagThis = false;

        if (sto.getType() instanceof ErrorType) {
            return new ErrorSTO(sto.getName());
        }
        if(!(sto.getType() instanceof TypeStruct)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error14t_StructExp, sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        }
        if (sto.getName().equals("this")) {
            struct = m_symtab.getStruct();
            flagThis = true;
        } else{
            struct = (StructdefSTO) m_symtab.accessGlobal(sto.getType().getName());
        }
        Vector <STO> structVar = struct.getVar(); //get varlist

        for(STO a : structVar){
            if(strID.equals(a.getName())){
                found = true;
                //Sabryna
                if (a instanceof FuncSTO) {
                    ((FuncSTO) a).setIsStructFunc(true);
                    ((FuncSTO) a).setStruct(struct);
                    return a;
                }
                return a;
            }
        }

        if (flagThis) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error14c_StructExpThis, strID));
            return new ErrorSTO(sto.getName());
        } else {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error14f_StructExp, strID, sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    // Check 11: Array Usage
    STO DoDesignator2_Array(STO sto, STO number)
    {
        // return Error if previous error already occurred
        if (sto instanceof ErrorSTO || number instanceof ErrorSTO) {
            return new ErrorSTO(sto.getName());
        }

        // Check if id is TypeArray or TypePointer
        if ((!(sto.getType() instanceof TypeArray)) &&
                (!(sto.getType() instanceof TypePointer))) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error11t_ArrExp, sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        }

        // Check if index is int
        if (!(number.getType() instanceof TypeInt)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error11i_ArrExp, number.getType().getName()));
            return new ErrorSTO(sto.getName());
        }

        // No need to check boundary if TypePointer
        if (sto.getType() instanceof TypePointer) {

            if (sto.getType() instanceof TypeNullPointer) {
                m_nNumErrors++;
                m_errors.print(ErrorMsg.error15_Nullptr);
                return new ErrorSTO(sto.getName());
            }

            STO toReturn = new VarSTO(sto.getName(),
                                      ((TypePointer) sto.getType()).getPointerType());
            toReturn.setIsAddressable(true);
            toReturn.setIsModifiable(true);
            return toReturn;
        }

        // Check if out of bounds for ConstSTO
        if (number instanceof ConstSTO) {
            int userIndex = ((ConstSTO) number).getIntValue();
            int maxIndex = ((TypeArray)sto.getType()).getIndexSize();

            if (((maxIndex - 1) < userIndex) || (userIndex < 0)) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(
                        ErrorMsg.error11b_ArrExp, userIndex, maxIndex));
                return new ErrorSTO(sto.getName());
            }
        }

        return ((TypeArray)sto.getType()).getElement();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoDesignator3_ID(String strID)
    {
        STO sto;

        if ((sto = m_symtab.accessLocal(strID)) == null) {
            if((sto = m_symtab.accessGlobal(strID)) == null) {
                if((sto = m_symtab.access(strID)) == null) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
                    sto = new ErrorSTO(strID);
                }
            }
        }

        return sto;
    }

    STO DoDesignator4_ID(String strID)
    {  //asscess global
        STO sto;
        if ((sto = m_symtab.accessGlobal(strID)) == null)
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error0g_Scope, strID));
            sto = new ErrorSTO(strID);
        }
        return sto;
    }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------


    // Check 14
    Type DoStructType_ID(String strID)
    {
        STO sto;

        if ((sto = m_symtab.accessGlobal(strID)) == null)
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
            return new ErrorType();
        }

        if (!sto.isStructdef())
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.not_type, sto.getName()));
            return new ErrorType();
        }

        return sto.getType();
    }

    // Check 1
    //Sabryna
    STO DoBinaryExpr(STO a, STO b, Operator o)
    {
        if (a instanceof ErrorSTO || b instanceof ErrorSTO) {
            return new ErrorSTO(a.getName());
        }

        STO result = o.checkOperands(a, b);

        if(result.isError()){
            m_nNumErrors++;
            m_errors.print(((ErrorSTO)result).getErrMsg());
        }

        return result;
    }

    // Check 2
    //Sabryna
    STO DoUnaryExpr(STO a, Operator o)
    {
        if (a instanceof ErrorSTO) {
            return a;
        }

        STO result = ((OpUnary)o).checkOperands(a);

        if (result instanceof ErrorSTO) {
            m_nNumErrors++;
            m_errors.print(((ErrorSTO)result).getErrMsg());
            return result;
        }
        return result;
    }

    STO DoUnaryValue(STO a)
    {
        STO toReturn;
        if (a instanceof ConstSTO) {
            toReturn = new ConstSTO(a.getName(), a.getType(), ((ConstSTO) a).getValue());
            ((ConstSTO)toReturn).unaryValue();
            return toReturn;
        }

        return a;
    }

    // Check 4
    void checkIfExpr(STO a){
        if (a instanceof ErrorSTO) {
            return;
        }

        if(!(a.getType() instanceof TypeBool)){
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error4_Test, a.getType().getName()));
        }
    }

    // Check 5
    void checkReturnType(STO o){ //check 6a
        FuncSTO sto = m_symtab.getFunc();
        if (o instanceof ErrorSTO) {
            sto.setreturnflag(false);
            return;
        }
        Type s = o.getType();
        //struct case
        if (sto == null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error6a_Return_type, s.getName(), "void"));
            return;
        }

        boolean refer = sto.getamper();
        if(!refer){
            if (!(sto.getReturnType() instanceof TypeVoid)) {
                if (s instanceof TypeVoid) {
                    m_nNumErrors++;
                    m_errors.print(ErrorMsg.error6a_Return_expr);
                } else if (!s.isAssignableTo(sto.getReturnType())) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error6a_Return_type, s.getName(), sto.getReturnType().getName()));
                }
            } else if (!(s instanceof TypeVoid)){
                m_nNumErrors++;
                m_errors.print(
                        Formatter.toString(ErrorMsg.error6a_Return_type, s.getName(), sto.getReturnType().getName()));
            }
        }else if(!s.isEquivalentTo(sto.getReturnType())){
            m_nNumErrors++;
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error6b_Return_equiv, s.getName(), sto.getReturnType().getName()));
        }else if(!o.getIsModifiable()){
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error6b_Return_modlval);
        }
        if(m_symtab.getWhile() || m_symtab.getForEach() || m_symtab.getIf()){

        }else {
            sto.setreturnflag(false);
        }
    }

    /*check 5*/
    STO makeVarSto(String id, Type t, String ref, Vector<STO> array)
    {
        STO sto;
        Vector<Integer> domain = new Vector<Integer>();
        boolean errFlag = false;

        if (array != null) {
            for (STO index : array) {
                if (index instanceof ErrorSTO) {
                    errFlag = true;
                    break;
                }
                if (index.getType() instanceof TypeInt) {
                    if (!(index instanceof ConstSTO)) {
                        m_nNumErrors++;
                        m_errors.print(ErrorMsg.error10c_Array);
                        errFlag = true;
                        break;
                    }
                    int range = ((ConstSTO)index).getIntValue();
                    if (range <= 0) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error10z_Array, range));
                        errFlag = true;
                        break;
                    }
                    domain.addElement(range);
                } else {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(
                            ErrorMsg.error10i_Array, index.getType().getName()));
                    errFlag = true;
                    break;
                }
            }
            if (errFlag) {
                sto = new ErrorSTO(id);
            } else {
                TypeArray newType = new TypeArray(t.getName(), domain.firstElement(), t, domain);
                sto = new VarSTO(id, newType);
                if (ref !=null){
                    ((VarSTO)sto).setamper();
                }
                sto.setIsAddressable(true);
            }

            if (m_symtab.accessLocal(id) != null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            }
            m_symtab.insert(sto);
            return sto;
        } //end of TypeArray part

        sto = new VarSTO(id, t);
        if(ref != null){ //sabryna
            ((VarSTO) sto).setamper();
        }
        if (m_symtab.accessLocal(id)!=null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }
        sto.setIsAddressable(true);
        sto.setIsModifiable(true);

        m_symtab.insert(sto);

        return sto;
    }

    /*check 6c*/
    void noReturn (){
        FuncSTO sto = m_symtab.getFunc();
        Type t = sto.getReturnType();
        if(!(t instanceof TypeVoid) && (sto.getreturnflag())){
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error6c_Return_missing);
        }
    }

    /* Check 7*/
    void DoExit(STO a)
    {
        if (a instanceof ErrorSTO) {
            return;
        }
        Type t = new TypeInt("int", 4);
        if (! a.getType().isAssignableTo(t)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error7_Exit, a.getType().getName()));
        }
    }

    /*check 12a*/
    void setForEachFlag(){
        this.m_symtab.setForEach();
    }

    void setWhileFlag(){
        this.m_symtab.setWhile();
    }

    void setIfFlag(){this.m_symtab.setIf();}

    void doForEach(Type t, String ref, String id,  STO s)
    {
        Type ex = s.getType();
        if(!(ex instanceof TypeArray)){
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error12a_Foreach);
            return;
        }
        Type x = ((TypeArray)ex).getContainerType();

        if(ref == null){
           if(!(x.isAssignableTo(t))){
               m_nNumErrors++;
               m_errors.print(Formatter.toString(ErrorMsg.error12v_Foreach, x.getName(), id, t.getName()));
               return;
           }
        }else{
            if(!(x.isEquivalentTo(t))){
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error12r_Foreach, x.getName(), id, t.getName()));
                return;
            }
        }
        DoVarDecl(false, id, t, null, null);
        this.m_symtab.setForEach();
    }

    /* Check 12b*/
    void doBreak()
    {
        if(!(this.m_symtab.getForEach() || this.m_symtab.getWhile())) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error12_Break);
        }
    }
    void doContinue()
    {
        if( !(this.m_symtab.getForEach() || this.m_symtab.getWhile())) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error12_Continue);
        }
    }

    /*check 13*/
    void DoStructdefDecl(String id)
    {
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
        }
        Type t = new TypeStruct(id, 0);
        StructdefSTO sto = new StructdefSTO(id, t);
        m_symtab.insertGlobal(sto);
        m_symtab.setStruct(sto); // save current setStruct
    }

    // check13a : Struct declaration, duplication identifiers
    // Do struct variables declaration
    void DoStructVarDecl(String id, Type t, Vector <STO> v)
    {
        STO sto;
        Vector<Integer> domain = new Vector<Integer>();
        boolean errFlag = false;

        // TypeArray Member variables
        if(v != null){
            for(STO index : v){
                if (index instanceof ErrorSTO) {
                    errFlag = true;
                    break;
                }
                if(index.getType() instanceof TypeInt){
                    if(!(index instanceof ConstSTO)){
                        m_nNumErrors++;
                        m_errors.print(ErrorMsg.error10c_Array);
                        errFlag = true;
                        break;
                    }
                    int range = ((ConstSTO)index).getIntValue();
                    if(range <= 0){
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error10z_Array, range));
                        errFlag = true;
                        break;
                    }
                    domain.addElement(range);
                }else{
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error10i_Array, index.getType().getName()));
                    errFlag = true;
                    break;
                }
            }
            if (errFlag) {
                sto = new ErrorSTO(id);
            } else {
                TypeArray newType = new TypeArray(t.getName(), domain.firstElement(), t, domain);
                sto = new VarSTO(id, newType);
                sto.setIsAddressable(true);
            }
            // Check for duplicate declaration
            if (m_symtab.accessLocal(id) != null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error13a_Struct, id));
            }

            StructdefSTO curStruct = m_symtab.getStruct();
            curStruct.addVar(sto);
            m_symtab.insert(sto);
            curStruct.getType().addSize(sto.getType().getSize());
            return;
        } // End of array var


        sto = new VarSTO(id, t);
        if(t instanceof TypeInt || t instanceof TypeFloat ||
                t instanceof TypeBool || t instanceof TypePointer){
            sto.setIsModifiable(true);
            sto.setIsAddressable(true);
        }

        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error13a_Struct, id));
        }

        StructdefSTO curStruct = m_symtab.getStruct();
        curStruct.addVar(sto);
        m_symtab.insert(sto);

        curStruct.getType().addSize(sto.getType().getSize());
    }
    //----------------------------------------------------------------
    // Do Struct Constructor or Function declaration
    //----------------------------------------------------------------
    void DoFuncDeclStr(String id, Type returntype, String reference)
    {
        if(returntype == null){ // for constructor
             String curStructName = m_symtab.getStruct().getName();
             if(!curStructName.equals(id)){
                 m_nNumErrors++;
                 m_errors.print(Formatter.toString(ErrorMsg.error13b_Ctor, id, curStructName));
                 m_symtab.openScope();  //open one scope, cuz it has to close later
                 //return;
             }else{
                 FuncSTO func = new FuncSTO(id, new TypeVoid("void", 0));
                 StructdefSTO curStruct = m_symtab.getStruct();
                 func.setCondidate(true); // set candidate
                 curStruct.addVar(func); //add into structure
                 m_symtab.insert(func); // insert into struct scope
                 m_symtab.openScope();  //open one scope for those vars
                 m_symtab.setFunc(func);
                 curStruct.setConstruct(true);
             }
        }else {
            FuncSTO sto = new FuncSTO(id, returntype);
            if (m_symtab.accessLocal(id) != null) {

                if (!(m_symtab.accessLocal(id) instanceof FuncSTO)) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error13a_Struct, id));
                }
                    sto.setCondidate(true);
            }

            sto.setReturnType(returntype);
            if (reference != null) { // check 6b2
                sto.setamper();
            }

            sto.setreturnflag();
            sto.setScopeLevel(m_symtab.getLevel() + 1); //SET funcSTO vars level

            StructdefSTO curStruct = m_symtab.getStruct();
            curStruct.addVar(sto); //save var and func into structdefSTO

            m_symtab.insert(sto); // insert into struct scope
            m_symtab.openScope();  //open one scope for those vars
            m_symtab.setFunc(sto); // save current funcSTO
        }
    }

    /*check 13a*/
    void DoFormalParamsStr(Vector<STO> params)
    {
        StructdefSTO curStruct = m_symtab.getStruct();
        Boolean flag = false;
        Vector<STO> funcGroup;
        FuncSTO currentFunc = m_symtab.getFunc();
        if (currentFunc == null) return;
        FuncSTO funtemp;
        int oneSize = 0;
        int twoSize = 0;
        Vector<STO> args;

        if (currentFunc == null) {
        }

        int level = m_symtab.getLevel() - 2; // level of Struct
        Scope s = m_symtab.accessLevel(1); // get Struct scope
        funcGroup = new Vector<STO>(s.findfuncStr(currentFunc.getName())); //get function group

        if (funcGroup.size() > 1) {
            for (STO fun : funcGroup) {
                funtemp = (FuncSTO) fun;
                if (!funtemp.getCondidate()) { //pick those not candidate
                    args = funtemp.getParameter();
                    if (params == null && args == null) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, currentFunc.getName()));
                        return;
                    }
                    if (params != null ){
                        oneSize = params.size();
                    }else {
                        oneSize = 0;
                    }
                    if(args != null) {
                        twoSize = args.size();
                    }else{
                        twoSize = 0;
                    }
                    if ((oneSize == twoSize) && (oneSize != 0)) {
                        Iterator<STO> itrSto = params.iterator();
                        Iterator<STO> itrSto1 = args.iterator();

                        while (itrSto.hasNext()) {
                            VarSTO b = (VarSTO) itrSto.next(); //second one
                            STO a = itrSto1.next(); //input
                            if (!a.getType().isEquivalentTo(b.getType())) {
                                flag = true; //true, not same
                                break; //check next
                            }else{
                                flag = false;
                            }
                        }
                    } else if ((oneSize == 0) && (twoSize == 0)) { //dou shi null
                        flag = false; // same
                    } else { //size bu tong
                        flag = true;
                    }
                    if (!flag) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, currentFunc.getName()));
                        return;
                    }
                }
            }
        }
        currentFunc.setCondidate(false);
        m_symtab.getFunc().setParameter(params);

    }

    void DoDestructor(String id) {
        StructdefSTO ss = m_symtab.getStruct();
        m_symtab.openScope();  //open one scope for those vars
        if(id.equals(ss.getName())){
            if(ss.getdestruct()){ // if set already
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, "~"+id));
                return;
            }else{
                ss.setdestruct(true);
            }
        }else{
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error13b_Dtor, "~"+id, ss.getName()));
            return;
        }
    }

    // Check 15a
    STO DoDereference_Star(STO sto)
    {
        if (sto instanceof ErrorSTO) {
            return sto;
        }

        if (sto.getType() instanceof TypeNullPointer) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error15_Nullptr);
            return new ErrorSTO(sto.getName());
        }

        if (!(sto.getType() instanceof TypePointer)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error15_Receiver, sto.getType().getName()));

            return new ErrorSTO(sto.getName());
        }

        return ((TypePointer) sto.getType()).getPointerElement();
    }

    STO DoDereference_Arrow (STO sto, String id )
    {
        if (sto instanceof ErrorSTO) {
            return sto;
        }
        // check nullptr
        if (sto.getType() instanceof TypeNullPointer) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error15_Nullptr);
            return new ErrorSTO(sto.getName());
        }
        // check if pointer to struct
        if (!(sto.getType() instanceof TypePointer)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error15_ReceiverArrow, sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        }
        if (!(((TypePointer)sto.getType()).getPointerType() instanceof TypeStruct)){
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error15_ReceiverArrow, sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        } else {


            StructdefSTO struct;

            struct = (StructdefSTO) m_symtab.accessGlobal(((TypePointer) sto.getType()).getPointerType().getName());
            Vector<STO> structVar = struct.getVar();


            for(STO a : structVar){
                if(id.equals(a.getName())){
                    if (a instanceof FuncSTO) {
                        ((FuncSTO) a).setIsStructFunc(true);
                        ((FuncSTO) a).setStruct(struct);
                        return a;
                    }
                    return a;
                }
            }

            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error14f_StructExp, id, ((TypePointer) sto.getType()).getPointerType().getName()));
            return new ErrorSTO(sto.getName());
        }
    }

    // Check 16a New/delete statement
    void checkNewStmt(STO sto, Vector<STO> args)
    {
        STO struct;
        if (sto instanceof ErrorSTO) {
            return;
        }

        if (!sto.isModLValue()) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error16_New_var);
            return;
        }

        if (! (sto.getType() instanceof TypePointer)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error16_New, sto.getType().getName()));
            return;
        }

        if (args != null) {
            if (!(((TypePointer) sto.getType()).getPointerType() instanceof TypeStruct)) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error16b_NonStructCtorCall,
                        ((TypePointer) sto.getType()).getPointerType().getName()));
                return;
            } else {
                String structName = ((TypePointer) sto.getType()).getPointerType().getName();
                struct = m_symtab.accessGlobal(structName);
                if (struct instanceof StructdefSTO) {
                    Vector<STO> localvar = ((StructdefSTO) struct).getVar();
                    Vector<STO> structor = new Vector<STO>();

                    for (STO a : localvar) {
                        if (a.getName().equals(structName)) {
                            structor.addElement(a);
                        }
                    }

                    if (structor.size() == 0) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, args.size(), 0));
                    }
                }
            }
        }

        if (args == null && (((TypePointer) sto.getType()).getPointerType() instanceof TypeStruct)) {
            String structName = ((TypePointer) sto.getType()).getPointerType().getName();
            struct = m_symtab.accessGlobal(structName);
            if (struct instanceof StructdefSTO) {
                Vector<STO> localvar = ((StructdefSTO)struct).getVar();
                Vector<STO> structor = new Vector<STO>();

                for (STO a : localvar) {
                    if (a.getName().equals(structName)) {
                        structor.addElement(a);
                    }
                }

                if (structor.size() == 1){
                    STO func = structor.firstElement();
                    int paramCount = 0;

                    if (func instanceof FuncSTO) {
                        if (((FuncSTO) func).getParameter() != null) {
                            paramCount = ((FuncSTO) func).getParameter().size();
                        }
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, 0, paramCount));
                    }
                } else if (structor.size() > 1) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error9_Illegal, structName ));
                }
            }
        }
    }

    void checkDeleteStmt(STO sto)
    {
        if (sto instanceof ErrorSTO) {
            return;
        }

        if (!sto.isModLValue()) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error16_Delete_var);
            return;
        }

        if (! (sto.getType() instanceof TypePointer)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(
                    ErrorMsg.error16_Delete, sto.getType().getName()));
        }
    }

    // Check 17


    // Check 18 Address-of operator
    STO DoDesignator_Ampersand(STO sto)
    {
        if (sto instanceof ErrorSTO) {
            return sto;
        }

        if (!sto.getIsAddressable()) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error18_AddressOf,
                    sto.getType().getName()));
            return new ErrorSTO(sto.getName());
        }

        return new ExprSTO(sto.getName(),
                new TypePointer(sto.getName(), 4, sto.getType(), sto, 1));
    }

    // Check 19
    STO DoDesignator_sizeOf(STO sto)
    {
        if (sto instanceof ErrorSTO) {
            return new ErrorSTO("sizeofErr");
        }

        if (sto instanceof FuncSTO) {
            if (((FuncSTO) sto).getamper()) {
                return new ConstSTO("sizeof(" + sto.getName() + ")",
                                    new TypeInt("int", 4), ((FuncSTO) sto).getReturnType().getSize());
            } else {
                m_nNumErrors++;
                m_errors.print(ErrorMsg.error19_Sizeof);
                return new ErrorSTO("sizeofErr");
            }
        }

        if (!sto.getIsAddressable()) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error19_Sizeof);
            return new ErrorSTO("sizeofErr");
        }

        return new ConstSTO("sizeof("+sto.getName() + ")",
                            new TypeInt("int", 4), sto.getType().getSize());
    }
    STO DoDesignator_sizeOf_type (Type type, Vector<STO> array)
    {
        if (type instanceof ErrorType) {
            return new ErrorSTO("sizeofErrType");
        }

        if (array != null) {
            int arraySize = type.getSize();

            for (STO index : array) {
                if (index instanceof ErrorSTO) {
                    return new ErrorSTO("sizeofErr");
                }
                if (index.getType() instanceof TypeInt) {
                    if (!(index instanceof ConstSTO)) {
                        m_nNumErrors++;
                        m_errors.print(ErrorMsg.error10c_Array);
                        return new ErrorSTO("sizeofErr");
                    }

                    int range = ((ConstSTO)index).getIntValue();
                    if (range < 0) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error10z_Array, range));
                        return new ErrorSTO("sizeofErr");
                    }
                    arraySize = arraySize*range;
                } else {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(
                            ErrorMsg.error10i_Array, index.getType().getName()));
                    return new ErrorSTO("sizeofErr");
                }
            }
            return new ConstSTO("sizeOf "+type.getName()+"array",
                                            new TypeInt("int", 4), arraySize);
        }
        return new ConstSTO(type.getName(), new TypeInt("int", 4), type.getSize());
    }

    // Check 20
    STO DoDesignator_typeCast(Type resultType, STO sto)
    {
        if (sto instanceof ErrorSTO) {
            return sto;
        }

        if (sto.getType() instanceof TypeStruct || sto.getType() instanceof TypeArray ||
            sto.getType() instanceof TypeNullPointer) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error20_Cast,
                    sto.getType().getName(), resultType.getName()));
            return new ErrorSTO(sto.getName());
        }

        if (sto instanceof ConstSTO) {
            if (sto.getType() instanceof TypeBool) {
                if (resultType instanceof TypeNumeric) {
                    if (((ConstSTO) sto).getBoolValue()) {
                        return new ConstSTO(sto.getName(), resultType, 1);
                    }
                        return new ConstSTO(sto.getName(), resultType, 0);
                }

                if (resultType instanceof TypePointer) {
                    return new ExprSTO(sto.getName(), resultType);
                }

                return sto;
            } else if (sto.getType() instanceof TypeFloat) {
                if (resultType instanceof TypeBool) {
                    if ((((ConstSTO) sto).getValue().compareTo(BigDecimal.ZERO)) == 0) {
                        return new ConstSTO(sto.getName(), resultType, 0);
                    }
                    return new ConstSTO(sto.getName(), resultType, 1);
                }
                if (resultType instanceof TypeInt) {
                    return new ConstSTO(sto.getName(), resultType,
                                                      ((ConstSTO) sto).getIntValue());
                }
                if (resultType instanceof TypePointer) {
                    return new ExprSTO(sto.getName(), resultType);
                }

                return sto;
            } else if (sto.getType() instanceof TypeInt) {
                if (resultType instanceof TypeBool) {
                    if(((ConstSTO) sto).getIntValue() == 0) {
                        return new ConstSTO(sto.getName(), resultType, 0);
                    }
                    return new ConstSTO(sto.getName(), resultType, 1);
                }
                if (resultType instanceof TypeFloat) {
                    return new ConstSTO(sto.getName(), resultType,
                            ((ConstSTO) sto).getFloatValue());
                }
                if (resultType instanceof TypePointer) {
                    return new ExprSTO(sto.getName(), resultType);
                }

                return sto;
            }
        } else {
            return new ExprSTO(sto.getName(), resultType);
        }

        return new ConstSTO(sto.getName(), resultType);
    }

    void StructCall(Type t, String s, Vector<STO> array, Vector<STO> args) {
        FuncSTO func;
        STO sto;
        Boolean found = false;
        Boolean found1 = false;
        int size = 0;
        int size2 = 0;
        Vector<STO> para;
        Boolean flag = false;
        StructdefSTO findstruct = (StructdefSTO) m_symtab.accessGlobal(t.getName()); // find the struct from scope

        if (args == null) {
            size2 = 0;
        } else {
            size2 = args.size();
        }
        if (((sto = m_symtab.accessLocal(s)) != null)) { //find if there is same
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, s));
        }else{
            STO in = new VarSTO(s, t);
            in.setIsModifiable(true);
            in.setIsAddressable(true);
            m_symtab.insert(in); // save to symble table peek
        }

        if(findstruct == null) return;

        if(!findstruct.getConstruct()){
            if(args != null){
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, args.size(), 0));
                return;
            }
        }
        if (array != null) { //make a new type, int [][], with size
            Vector<Integer> domain = new Vector<Integer>();
            for (STO a : array) {
                if (a.getType() instanceof TypeInt) {
                    if (a instanceof VarSTO) {
                        m_nNumErrors++;
                        m_errors.print(ErrorMsg.error10c_Array);
                        findstruct.setError();
                        return;
                    }
                    int range = ((ConstSTO) a).getIntValue();
                    if (range < 0) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error10z_Array, range));
                        findstruct.setError();
                        return;
                    }
                    domain.addElement(range);
                } else {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error10i_Array, a.getType().getName()));
                    findstruct.setError();
                    return;
                }
            }

            TypeArray m = new TypeArray(t.getName(), 32); //set Arraytype
            m.setDomain(domain);
            m.setContainerType(t);
            t = m;
        }
        if(findstruct == null){
            m_nNumErrors++;
            return;
        }
        Vector<STO> localvar = findstruct.getVar(); //get the var and function and construction of this STRUCTION
        Vector<STO> structor = new Vector<STO>();

        for (STO a : localvar) { //get construction
            if (a.getName().equals(t.getName())) {
                structor.addElement(a);
            }
        }

        if(structor.size() == 0){
            if(args == null){
                found1 = true;
            }else{
                found1 = false;
            }
        }else if(structor.size() == 1){
            STO fe = structor.firstElement();
            func = (FuncSTO) fe;
            para = func.getParameter();
            if (args != null) {
                if (args.size() != para.size()) {
                    //argument number doesn't match up
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, args.size(), para.size()));
                    return;
                } else {
                    Iterator<STO> itrSto = para.iterator();
                    Iterator<STO> itrSto1 = args.iterator();
                    while (itrSto.hasNext()) {
                        VarSTO b = (VarSTO) itrSto.next();
                        STO a = itrSto1.next(); //input
                        if (b.getamper()) {
                            if (!a.getType().isEquivalentTo(b.getType())) {
                                m_nNumErrors++;
                                m_errors.print(Formatter.toString(ErrorMsg.error5r_Call, a.getType().getName(), b.getName(), b.getType().getName()));
                            } else if(!a.getIsModifiable()) {
                                m_nNumErrors++;
                                m_errors.print(Formatter.toString(ErrorMsg.error5c_Call, b.getName(),b.getType().getName()));
                            }
                        } else {
                            if (!a.getType().isAssignableTo(b.getType())) {
                                m_nNumErrors++;
                                m_errors.print(Formatter.toString(ErrorMsg.error5a_Call, a.getType().getName(), b.getName(), b.getType().getName()));
                            }
                        }
                    }
                    return;
                }
            }else {
                if (para.size()!=0) {
                    //argument number doesn't match up
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, 0, para.size()));
                    return;
                }
            }
        }
        else { // when overloaded
            for (STO fun : structor) {
                flag = true;
                FuncSTO Func = (FuncSTO) fun; //function call
                para = Func.getParameter();

                if ((para != null) && (args != null)) {
                    if ((args.size() != para.size()) ) {
                        flag = false; // not match
                        continue;
                    } else { //same size
                        Iterator<STO> itrSto = para.iterator();
                        Iterator<STO> itrSto1 = args.iterator();

                        while (itrSto.hasNext()) {
                            VarSTO b = (VarSTO) itrSto.next();
                            STO a = itrSto1.next(); //input
                            if (b.getamper()) { //if has &
                                if (!a.getType().isEquivalentTo(b.getType())) {
                                    flag = false; //not match;
                                    break;
                                } else if (!a.getIsModifiable()) {
                                    flag = false; //not match;
                                    break;
                                }
                            } else {
                                if (!a.getType().isEquivalentTo(b.getType())) {
                                    flag = false; //not match;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    if ( (para != null && args == null) ||  (para == null && args != null)) {
                        //argument number doesn't match up
                        flag = false; //not match
                        // break;
                    }
                }
                if(flag) {
                    break; //found, jump out for loop
                }
            }
            if (!flag) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error9_Illegal, t.getName()));
                return;
            }
        }
    }


    STO MakeThisExpr(){
        StructdefSTO struct = m_symtab.getStruct();
        return new ExprSTO("this", new TypeStruct(struct.getName(), struct.getType().getSize()));
    }

}
