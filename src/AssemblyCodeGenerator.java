
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Stack;
import java.util.Vector;
import java.util.HashSet;
/**
 * Created by Sabryna on 11/3/15.
 */
public class AssemblyCodeGenerator {
    private boolean FirstFlag = true;
    private int indent_level = 0;
    private static final String ERROR_IO_CLOSE =
            "Unable to close fileWriter";
    private static final String ERROR_IO_CONSTRUCT =
            "Unable to construct FileWriter for file %s";
    private static final String ERROR_IO_WRITE =
            "Unable to write to fileWriter";
    // 3
    private FileWriter fileWriter;
    // 4
    private static final String FILE_HEADER =
            "/*\n" + " * Generated %s\n" + " */\n\n";
    // 5
    private static final String SEPARATOR = "\t";
    // 6
    private static final String SET_OP = "set     ";
    private static final String SAVE_OP = "save    ";
    private static final String ADD_OP = "add     ";
    private static final String SUB_OP = "sub     ";
    private static final String LOAD_OP = "ld      ";
    private static final String CALL_OP = "call    ";
    private static final String CMP_OP = "cmp     ";
    private static final String FCMP_OP = "fcmps   ";
    private static final String STORE = "st      ";
    private static final String BE_OP = "be      "; //==
    private static final String FBE_OP = "fbe     "; //==
    private static final String BL_OP = "bl      "; //>
    private static final String FBL_OP = "fbl     "; //>
    private static final String BNE_OP = "bne     "; // !=
    private static final String FBNE_OP = "fbne    "; // !=
    private static final String BGE_OP = "bge     "; // >=
    private static final String FBGE_OP = "fbge    "; // >=
    private static final String BLE_OP = "ble     "; // <=
    private static final String FBLE_OP = "fble    "; // <=
    private static final String BA_OP = "ba      "; // <=
    private static final String BG_OP = "bg      "; // <=
    private static final String FBG_OP = "fbg     "; // <=



    private static final String AND_OP = "and     ";
    private static final String XOR_OP = "xor     ";
    private static final String OR_OP = "or      ";

    private static final String NEG = "neg     ";
    private static final String FNEG = "fnegs   ";




    private static final String NOP = "nop     ";
    private static final String CALL = "call    ";
    private static final String RET = "ret     ";
    private static final String RESTORE = "restore ";
    private static final String PRINTF = "printf";
    private static final String PRINTFLOAT = "printFloat";

    private static final String MOVE = "mov     ";
    private static final String EXIT = "exit";

    private static final String SECTION = ".section";
    private static final String ALIGN = ".align  ";
    private static final String GLOBAL = ".global ";
    private static final String SKIP = ".skip   ";
    private static final String WORD = ".word   ";
    private static final String SINGLE = ".single ";
    private static final String ASCIZ = ".asciz  ";
    private static final String FITOS = "fitos   ";
    private static final String FADDS = "fadds   ";
    private static final String FMULS = "fmuls   ";
    private static final String FSUBS = "fsubs   ";
    private static final String FDIVS = "fdivs   ";




    private static final String BSS = "\".bss\"";
    private static final String TEXT = "\".text\"";
    private static final String DATA = "\".data\"";
    private static final String HEAP = "\".heap\"";
    private static final String STACK = "\".stack\"";
    private static final String RODATA = "\".rodata\"";

    private static final String INPUT0 = "%i0";
    private static final String INPUT1 = "%i1";
    private static final String INPUT2 = "%i2";
    private static final String INPUT3 = "%i3";
    private static final String INPUT4 = "%i4";
    private static final String INPUT5 = "%i5";

    private static final String OUTPUT0 = "%o0";
    private static final String OUTPUT1 = "%o1";
    private static final String OUTPUT2 = "%o2";
    private static final String OUTPUT3 = "%o3";
    private static final String OUTPUT4 = "%o4";
    private static final String OUTPUT5 = "%o5";

    private static final String LOCAL0 = "%l0";
    private static final String LOCAL1 = "%l1";
    private static final String LOCAL2 = "%l2";
    private static final String LOCAL3 = "%l3";
    private static final String LOCAL4 = "%l4";
    private static final String LOCAL5 = "%l5";
    private static final String LOCAL6 = "%l6";
    private static final String LOCAL7 = "%l7";

    private static final String F0 = "%f0";
    private static final String F1 = "%f1";
    private static final String F2 = "%f2";



    private static final String SP = "%sp";
    private static final String FP = "%fp";

    private static final String GLOBAL0 = "%g0";
    private static final String GLOBAL1 = "%g1";
    private static final String GLOBAL2 = "%g2";
    private static final String GLOBAL3 = "%g3";

    private static final String INC = "inc     ";
    private static final String INTFMT = ".$$.intFmt";
    private static final String STRFMT = ".$$.strFmt";
    private static final String STRTF = ".$$.strTF";
    private static final String STRENDL = ".$$.strEndl";
    private static final String STRARRBOUND = ".$$.strArrBound";
    private static final String STRNULLPTR = ".$$.strNullPtr";
    private static final String PRINTBOOL = ".$$.printBool";
    private static final String PRINTBOOL2 = ".$$.printBool2";
    private static final String ARRCHECK = ".$$.arrCheck";
    private static final String ARRCHECK2 = ".$$.arrCheck2";
    private static final String PTRCHECK = ".$$.ptrCheck";
    private static final String PTRCHECK2 = ".$$.ptrCheck2";

    private static final String CMP_FUNC = ".$$.cmp.";
    private static final String FLOAT_FUC = ".$$.float.";
//    private static final String FLOAT2 = ".$$.float.2";
    private static final String LOOPCHECK = ".$$.loopCheck.";
    private static final String LOOPEND = ".$$.loopEnd.";



    private static final String STRING1 = ".$$.str.";

    private static final String COUTEND = ".$$.strEndl";
    private static final String ELSE_FUC1 = ".$$.else.";
    private static final String END_IF1 = ".$$.endif.";
    private static final String INIT = "\".init\"";
    private static final String AndOrSkip = ".$$.andorSkip.";
    private static final String AndOrEnd = ".$$.andorEnd.";
    private static final String CtorDtor = ".$$.ctorDtor.";






    private static final String ONE_PARAM = "%s" + SEPARATOR + "%s\n";
    private static final String TWO_PARAM = "%s" +  SEPARATOR + "%s, %s\n";
    private static final String THREE_PARAM = "%s" + SEPARATOR + "%s, %s, %s\n";
    private static final String TWO_STRING = "%s" + SEPARATOR + "%s \n";
    private static final String STRING_NUM = "%s"+ SEPARATOR + "%s \n";
 //   private static final String THREE_STRING = "%s\t" + "%s" + SEPARATOR +" %s \n";
//    private static final String FOUR_STRING = "%s"+SEPARATOR + "%s" + SEPARATOR + " %s" + SEPARATOR + " %s"+"\n";
    private static final String END_SAVE = "SAVE." + "%s" + " = " + "-(92 + " + "%s" +") & -8 \n\n";

    private FuncSTO currentFunc;
    private int floatnum = 1; //for func
    private boolean returnflag = false;
    private int OrAnd = 1;
    private int cmpnum = 1;
    private int elsenum = 1;
    private int outnum = 0; // for int
    private int fnum = 0;  // for outnum
    private int ifnum = 1;
    private String call; // pick which register should be used
    private int LOOPCHECKNUM = 1;
    private int StringNum = 1;
    HashSet <String> a = new HashSet <String> ();
    private String structName = null;
    private String peekone = null;

    private Boolean FloatCal = false;

    private Stack<String> conditionstack = new Stack();
    private Stack<String> whilestack = new Stack();
    private Stack<String> whilestack1 = new Stack();


    public AssemblyCodeGenerator (String fileToWrite) {
        try {
            fileWriter = new FileWriter(fileToWrite);
            // 7
            writeAssembly(FILE_HEADER, (new Date()).toString());
        } catch (IOException e) {
            System.err.printf(ERROR_IO_CONSTRUCT, fileToWrite);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void DefineHeader(){
        increaseIndent();
        writeAssembly(TWO_STRING, SECTION, RODATA);
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
        decreaseIndent();
        writeAssembly(INTFMT+":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"%d\"");
        decreaseIndent();
        writeAssembly(STRFMT + ":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"%s\"");
        decreaseIndent();
        writeAssembly(STRTF + ":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"false\\0\\0\\0true\"");
        decreaseIndent();
        writeAssembly(STRENDL + ":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"\\n\"");
        decreaseIndent();
        writeAssembly(STRARRBOUND + ":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"Index value of %d is outside legal range [0, %d).\\n \"");
        decreaseIndent();
        writeAssembly(STRNULLPTR + ":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, "\"Attempt to dereference NULL pointer.\\n  \"" + "\n");

        writeAssembly(TWO_STRING, SECTION, TEXT);
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));

        decreaseIndent();
        writeAssembly(PRINTBOOL + ":\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly(TWO_PARAM, SET_OP, STRTF, OUTPUT0);
        writeAssembly(TWO_PARAM, CMP_OP, GLOBAL0, INPUT0);
        writeAssembly(ONE_PARAM, BE_OP, PRINTBOOL2);
        writeAssembly(NOP + "\n");
        writeAssembly(THREE_PARAM, ADD_OP,  OUTPUT0, String.valueOf(8), OUTPUT0);
        decreaseIndent();

        writeAssembly(PRINTBOOL2+":\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, CALL, PRINTF);
        writeAssembly(NOP+"\n");
        writeAssembly(RET + "\n");
        writeAssembly(RESTORE +"\n");

        decreaseIndent();
        writeAssembly("\n" + ARRCHECK + ":\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly(TWO_PARAM, CMP_OP, INPUT0, GLOBAL0);
        writeAssembly(ONE_PARAM, BL_OP, ARRCHECK2);
        writeAssembly(NOP + "\n");
        writeAssembly(TWO_PARAM, CMP_OP, INPUT0, INPUT1);
        writeAssembly(ONE_PARAM, BGE_OP,  ARRCHECK2);
        writeAssembly(NOP + "\n");
        writeAssembly(RET + "\n");
        writeAssembly(RESTORE +"\n");

        decreaseIndent();
        writeAssembly(ARRCHECK2 + ":\n");
        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, STRARRBOUND, OUTPUT0);
        writeAssembly(TWO_PARAM, MOVE, INPUT0, OUTPUT1);
        writeAssembly(ONE_PARAM, CALL, PRINTF);
        writeAssembly(TWO_PARAM, MOVE, INPUT1, OUTPUT2);
        writeAssembly(ONE_PARAM, CALL, EXIT);
        writeAssembly(TWO_PARAM, MOVE, String.valueOf(1), OUTPUT0);
        writeAssembly(RET + "\n");
        writeAssembly(RESTORE +"\n\n");

        decreaseIndent();
        writeAssembly(PTRCHECK + ":\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly(TWO_PARAM, CMP_OP, INPUT0, GLOBAL0);
        writeAssembly(ONE_PARAM, BNE_OP, PTRCHECK2);
        writeAssembly(NOP+"\n");
        writeAssembly(TWO_PARAM, SET_OP, STRNULLPTR, OUTPUT0);
        writeAssembly(ONE_PARAM, CALL, PRINTF);
        writeAssembly(NOP+"\n");
        writeAssembly(ONE_PARAM, CALL, EXIT);
        writeAssembly(TWO_PARAM, MOVE, String.valueOf(1), OUTPUT0);

        decreaseIndent();
        writeAssembly(PTRCHECK2+ ":\n");
        increaseIndent();
        writeAssembly(RET + "\n");
        writeAssembly(RESTORE +"\n\n");

    }
    // main for testing purposes
    public static void main(String args[]) {
        AssemblyCodeGenerator myAsWriter = new AssemblyCodeGenerator("rc.s");
        myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4095), "%l0");
        //myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(1024), "%l1");
        //myAsWriter.decreaseIndent();
        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(512), "%l2");
        myAsWriter.decreaseIndent();
        myAsWriter.dispose();
    }

    public void PrintCout(STO x){
        ResetIntandFloat();
        if(x.getType() == null) {//string
            writeAssembly("\n\n");
            writeAssembly(TWO_STRING, SECTION, RODATA);
            writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
            decreaseIndent();
            writeAssembly(STRING1 + StringNum + ":\n");
            increaseIndent();
            writeAssembly(TWO_STRING, ASCIZ, "\"" + x.getName() + "\"");
            GoBackToText();
        }
        writeAssembly("\n");
      //  if(x instanceof ConstSTO){
        //    writeAssembly("! print " + ((ConstSTO)x).getIntValue_As() + "\n");

        //}else {
            writeAssembly("! print " + x + "\n");
        //}
        if(x.getType() == null){ //string
            writeAssembly(TWO_PARAM,SET_OP, STRFMT, OUTPUT0);
            writeAssembly(TWO_PARAM, SET_OP, STRING1+StringNum, OUTPUT1);
            writeAssembly(ONE_PARAM, CALL, PRINTF);
            StringNum++;

        }else {
//            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(x.getOffset()), LOCAL7);
//            writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
//            if (x.getType() instanceof TypeInt) {
//                LoadIntorFloat();
//                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT1);
//            } else if (x.getType() instanceof TypeFloat) {
//                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", F0);
//            } else if (x.getType() instanceof TypeBool) {
//                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
//            }

            if (x.getType() instanceof TypeInt) {

                if(!x.GetUpdateFlag()) { //when false, dont have ++
                    writeAssembly(TWO_PARAM, SET_OP, x.getOffset(), LOCAL7);
                }else{
                    writeAssembly(TWO_PARAM, SET_OP, x.getUpdatedOffset(), LOCAL7);
                    x.setUpdateFlag(false);
                }

                if(x.getGlobal()) {
                    writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7);
                }else{
                    writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);

                }
                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT1);
                writeAssembly(TWO_PARAM, SET_OP, INTFMT, OUTPUT0);
            }else{
                LoadIntorFloat(x);
            }

            if (x.getType() instanceof TypeInt) {
                writeAssembly(ONE_PARAM, CALL, PRINTF);
            } else if (x.getType() instanceof TypeFloat) {
                writeAssembly(ONE_PARAM, CALL, PRINTFLOAT);
            } else if (x.getType() instanceof TypeBool) {
                writeAssembly(ONE_PARAM, CALL, PRINTBOOL);
            }
        }
        writeAssembly(NOP + "\n");
        ResetIntandFloat();
    }

    /* K > 1 , do it in calculation, here is if() */
    public void DoIf(STO expr){
        writeAssembly("\n");
        writeAssembly("!" + "if statement "+"\n");
        writeAssembly(TWO_PARAM, SET_OP, expr.getOffset(), LOCAL7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
        writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, GLOBAL0);
        writeAssembly(ONE_PARAM, BE_OP, ELSE_FUC1);
        writeAssembly(NOP + "\n");
        writeAssembly("\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, BA_OP, END_IF1);
        writeAssembly(NOP);
        decreaseIndent();
        writeAssembly("! else");
        writeAssembly(ELSE_FUC1 + ":\n");
        writeAssembly("! endif");
        writeAssembly(END_IF1 + "\n");

    }


    public void DoIfStmt(STO expr){
        writeAssembly("\n");
        writeAssembly("! if statement \n");
        if(expr instanceof ConstSTO){
            writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO)expr).getIntValue_As(), OUTPUT0);
        }else {
            LoadIntorFloat(expr);
        }
        writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, GLOBAL0);
        writeAssembly(ONE_PARAM, BE_OP, ELSE_FUC1 + String.valueOf(ifnum));
        writeAssembly(NOP + "\n");
        increaseIndent();
        conditionstack.push(END_IF1 + String.valueOf(ifnum));
        conditionstack.push(ELSE_FUC1 + String.valueOf(ifnum));
        ifnum++;
    }

    public void DoBa(){

        String tmp = conditionstack.pop(); // tmp has else
        peekone = conditionstack.peek(); // get endif#
//        conditionstack.push(tmp);
        writeAssembly("\n");
        writeAssembly(ONE_PARAM, BA_OP, peekone);
        writeAssembly(NOP + " \n");
        writeAssembly("\n");
        decreaseIndent();
        writeAssembly("!else\n");
        decreaseIndent();
        writeAssembly(tmp + ":\n");
        increaseIndent();
        increaseIndent();

    }

    public void DoElse(){
        writeAssembly("\n");
        decreaseIndent();
//        indent_level=2;
//        writeAssembly("! else\n");
//        decreaseIndent();
   //     String first = conditionstack.pop();
//        writeAssembly(first + ": \n");
//        writeAssembly("\n");
//        increaseIndent();
        indent_level = 2;
        writeAssembly("! endif \n");
        decreaseIndent();
        String second = conditionstack.pop();
        writeAssembly(second + ": \n");
    }

    public void DoWhileStmt(){
        writeAssembly("\n");
        writeAssembly("! while ( ... ) \n");
        decreaseIndent();
        writeAssembly(LOOPCHECK + LOOPCHECKNUM + ":");
        writeAssembly("\n");
        whilestack.push(LOOPCHECK + String.valueOf(LOOPCHECKNUM));
        increaseIndent();
        increaseIndent();
      //  LOOPCHECKNUM++;
    }

    public void CheckWhileCondition(STO expr){
        writeAssembly("\n");
        writeAssembly("! Check loop condition \n");
        LoadIntorFloat(expr);
        writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, GLOBAL0);
        writeAssembly(ONE_PARAM, BE_OP, LOOPEND + String.valueOf(LOOPCHECKNUM));
        writeAssembly(NOP + "\n\n");
        whilestack1.push(LOOPEND + String.valueOf(LOOPCHECKNUM));
        writeAssembly("! Start of loop body");
        increaseIndent();
        writeAssembly("\n");
        LOOPCHECKNUM++;
    }

    public void DoBreak(){
        writeAssembly("\n");
        writeAssembly("! break \n");
        String tmp = whilestack1.peek(); //loopend
        writeAssembly(ONE_PARAM, BA_OP, tmp);
        writeAssembly(NOP + "\n");
    }

    public void DoContinue(){
        writeAssembly("\n");
        writeAssembly("! continue \n");
        String tmp = whilestack.peek(); //loopcheck
        writeAssembly(ONE_PARAM, BA_OP,tmp);
        writeAssembly(NOP + "\n");
    }
    public void EndOfWhile(){
        writeAssembly("\n");
        decreaseIndent();
        writeAssembly("!End of loop body \n");
        String tmp = whilestack.pop(); //loopcheck
        writeAssembly(ONE_PARAM, BA_OP, tmp);
        writeAssembly(NOP + "\n");
        decreaseIndent();
        String tmp1 = whilestack1.pop();
        writeAssembly(tmp1 +":\n");
    }


    public void DoUnary(STO old, STO a){
        ResetIntandFloat();
        writeAssembly("!"+ a.getName() + "\n");
        LoadIntorFloat(old);
        if(old.getType() instanceof TypeFloat){
            writeAssembly(TWO_PARAM, FNEG, F0, F0);
        }else {
            writeAssembly(TWO_PARAM, NEG, OUTPUT0, OUTPUT0);
        }
        writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if (old.getType() instanceof TypeFloat) {
            writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
        }else {
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
        }

    }
    /* for exprSto  int a = b + c, b + c is here*/
    public void DoCalculation(STO result){
        ResetIntandFloat();
        STO a = ((ExprSTO)result).getOne();
        STO b = ((ExprSTO)result).getTwo();
        String Operator = ((ExprSTO)result).getOperator();

        if(currentFunc == null ) return;
        writeAssembly("\n");
        writeAssembly("!" + a.getName() + " Operator " + b.getName() + "\n");
        if(a instanceof ConstSTO && !(a.getIsAddressable())){ //just 1, 1.1 ....
            if(a.getType() instanceof TypeInt) {
                if(! (b instanceof  ConstSTO) ){
                    PickOutnum();
                    writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) a).getIntValue_As(), call);
                    outnum++;
                }else {
                    writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) a).getIntValue_As(), LOCAL7);
                }
                if(b.getType() instanceof TypeFloat){
                    ConvertIntToFloat();
                }

            }else if(a.getType() instanceof TypeFloat){
                DoFloatAssign(a);
            }
        }else{
            if(a.getType() instanceof TypeFloat){
                FloatCal = true;
            }
            LoadIntorFloat(a);
            if(a.getType() instanceof TypeInt && b.getType() instanceof TypeFloat){
                ConvertIntToFloat();
            }
        }

        if(b instanceof ConstSTO && !(b.getIsAddressable())){ // 1.1, 1 ....
            if(b.getType() instanceof  TypeInt) {
                if(a.getType() instanceof TypeFloat){
                    PickOutnum();
                    writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) b).getIntValue_As(), call);
                    outnum++;
                    ConvertIntToFloat();
                }else {
                    PickOutnum();
                    writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) b).getIntValue_As(), call);
                    outnum++;
                }
            }else if(b.getType() instanceof TypeFloat){
                DoFloatAssign(b);
            }else if(b.getType() instanceof TypeBool){
                PickOutnum();
                writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) b).getIntValue_As(), call);
                outnum++;
            }

        }else{ // VAR
            if(b.getType() instanceof TypeFloat){
                FloatCal = true;
            }
            LoadIntorFloat(b);

            if(b.getType() instanceof TypeInt && a.getType() instanceof TypeFloat){
                ConvertIntToFloat();
            }
        }
        Boolean opcheck = false;
        if(Operator.equals("*")){
            opcheck = true;
            if(a.getType() instanceof TypeFloat || b.getType() instanceof TypeFloat){
                writeAssembly(THREE_PARAM, FMULS, F0, F1, F0);
            }else {
                writeAssembly(ONE_PARAM, CALL, ".mul");
                writeAssembly(NOP + "\n");
                writeAssembly(TWO_PARAM, MOVE, OUTPUT0, OUTPUT0);
            }
        }else if(Operator.equals("+")){
            opcheck = true;
            if(a.getType() instanceof TypeFloat || b.getType() instanceof TypeFloat){
                writeAssembly(THREE_PARAM, FADDS, F0, F1, F0);
            }else {
                writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT1, OUTPUT0);
            }
        }else if(Operator.equals("-")){
            opcheck = true;
            if(a.getType() instanceof TypeFloat || b.getType() instanceof TypeFloat){
                writeAssembly(THREE_PARAM, FSUBS, F0, F1, F0);
            }else {
                writeAssembly(THREE_PARAM, SUB_OP, OUTPUT0, OUTPUT1, OUTPUT0);
            }
        }else if(Operator.equals("%")){
            opcheck = true;
            writeAssembly(ONE_PARAM, CALL, ".rem");
            writeAssembly(NOP + "\n");
            writeAssembly(TWO_PARAM, MOVE, OUTPUT0, OUTPUT0);
        }else if(Operator.equals("/")){
            opcheck = true;
            if(a.getType() instanceof TypeFloat || b.getType() instanceof TypeFloat){
                writeAssembly(THREE_PARAM, FDIVS, F0, F1, F0);
            }else {
                writeAssembly(ONE_PARAM, CALL, ".div");
                writeAssembly(NOP + "\n");
                writeAssembly(TWO_PARAM, MOVE, OUTPUT0, OUTPUT0);
            }
        }else if(Operator.equals("&")){
            writeAssembly(THREE_PARAM, AND_OP, OUTPUT0, OUTPUT1, OUTPUT0);
        }else if(Operator.equals("^")){
            writeAssembly(THREE_PARAM, XOR_OP, OUTPUT0, OUTPUT1, OUTPUT0);
        }else if(Operator.equals("|")){
            writeAssembly(THREE_PARAM, OR_OP, OUTPUT0, OUTPUT1, OUTPUT0);
        }else if(Operator.equals(">")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BLE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else if(a.getType() instanceof  TypeFloat || b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBLE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else{
                CompareInt();
                writeAssembly(ONE_PARAM, BLE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }else if(Operator.equals("==")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BNE_OP, CMP_FUNC + String.valueOf(cmpnum));

            }else if(a.getType() instanceof  TypeFloat || b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBNE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else if(a.getType() instanceof  TypeBool && b.getType() instanceof  TypeBool){
                CompareInt();
                writeAssembly(ONE_PARAM, BNE_OP, CMP_FUNC + String.valueOf(cmpnum));

            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }else if(Operator.equals(">=")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BL_OP, CMP_FUNC + String.valueOf(cmpnum));

            }else if(a.getType() instanceof  TypeFloat || b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBL_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else{
                CompareInt();
                writeAssembly(ONE_PARAM, BL_OP, CMP_FUNC + String.valueOf(cmpnum));
            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }else if(Operator.equals("<")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BGE_OP , CMP_FUNC + String.valueOf(cmpnum));

            }else if(a.getType() instanceof  TypeFloat || b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBGE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else{
                CompareInt();
                writeAssembly(ONE_PARAM, BGE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }else if(Operator.equals("<=")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BG_OP , CMP_FUNC + String.valueOf(cmpnum));

            }else if(a.getType() instanceof  TypeFloat || b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBG_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else{
                CompareInt();
                writeAssembly(ONE_PARAM, BG_OP, CMP_FUNC + String.valueOf(cmpnum));
            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }else if(Operator.equals("!=")){
            if(a.getType() instanceof  TypeInt && b.getType() instanceof TypeInt){
                CompareInt();
                writeAssembly(ONE_PARAM, BE_OP , CMP_FUNC + String.valueOf(cmpnum));

            }else if(a.getType() instanceof  TypeFloat && b.getType() instanceof TypeFloat){
                CompareFloat();
                writeAssembly(ONE_PARAM, FBE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }else{
                CompareInt();
                writeAssembly(ONE_PARAM, BE_OP, CMP_FUNC + String.valueOf(cmpnum));
            }
            writeAssembly(TWO_PARAM, MOVE, GLOBAL0, OUTPUT0);
            writeAssembly(ONE_PARAM, INC, OUTPUT0);
            decreaseIndent();
            writeAssembly(CMP_FUNC + String.valueOf(cmpnum) + ":\n");
            cmpnum++;
            increaseIndent();
        }
        int size = -currentFunc.getFuncVarSize() - 4;
        result.setOffset(String.valueOf(size));
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        currentFunc.setFuncVarSize(-size);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if(a.getType() instanceof TypeInt && b.getType() instanceof  TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof TypeFloat || b.getType() instanceof TypeFloat || a.getType() instanceof  TypeBool || b.getType() instanceof TypeBool){
            if(opcheck){
                writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
            }else {
                writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
            }
        }
        ResetIntandFloat();
    }


    public void ResetIntandFloat(){
        outnum = 0;
        fnum = 0;
    }

    public void CompareInt(){
        writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, OUTPUT1);
//        writeAssembly(ONE_PARAM, BLE_OP, CMP_FUNC + String.valueOf(cmpnum));
    }

    public void CompareFloat(){
        writeAssembly(TWO_PARAM, FCMP_OP, F0, F1);
        writeAssembly(NOP + "\n");
//        writeAssembly(ONE_PARAM, FBLE_OP, CMP_FUNC + String.valueOf(cmpnum));

    }


    public void DoLHS(STO a){
        writeAssembly("\n");
        writeAssembly("! Short Circuit LHS \n");
        SetAddLoadCMP(a, "&&");
    }

    public void DoRHS(STO expr){

        STO a = ((ExprSTO)expr).getOne();
        STO b = ((ExprSTO)expr).getTwo();
        String Operator = ((ExprSTO)expr).getOperator();

        writeAssembly("\n");
        writeAssembly("! Short Circuit RHS \n");
        SetAddLoadCMP(b, Operator);
        writeAssembly(ONE_PARAM, BA_OP, AndOrEnd + String.valueOf(OrAnd) );
        writeAssembly(TWO_PARAM, MOVE, String.valueOf(1), GLOBAL0);
        decreaseIndent();
        AndorSkipEnd(expr);
        OrAnd++;


    }
    public void DoOrAnd(STO expr){ // x || y or x && y
        STO a = ((ExprSTO)expr).getOne();
        STO b = ((ExprSTO)expr).getTwo();
        String Operator = ((ExprSTO)expr).getOperator();

        writeAssembly("\n");
        writeAssembly("! Short Circuit LHS \n");

        SetAddLoadCMP(a, Operator);

        writeAssembly("\n");
        writeAssembly("!" + a.getName() + Operator + b.getName() + "\n");
        writeAssembly("\n");
        writeAssembly("! Short Circuit RHS \n ");
        SetAddLoadCMP(b, Operator);
        writeAssembly(ONE_PARAM, BA_OP, AndOrEnd + String.valueOf(OrAnd) );
        if(Operator.equals("||")) {
            writeAssembly(TWO_PARAM, MOVE, String.valueOf(0), OUTPUT0);
        }else{ //&&
            writeAssembly(TWO_PARAM, MOVE, String.valueOf(1), OUTPUT0);
        }
        AndorSkipEnd(expr);
        OrAnd++;
    }

    public void SetAddLoadCMP(STO a, String op){
        if(a instanceof ConstSTO){
            writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO)a).getIntValue_As(), OUTPUT0 );
        }else {
            writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), LOCAL7);
            if (a.getGlobal()) {
                writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7);
            } else {
                writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);

            }
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);

        }
           // writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
            writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, GLOBAL0);
            if(op.equals("||")) {
                writeAssembly(ONE_PARAM, BNE_OP, AndOrSkip + String.valueOf(OrAnd));
            }else{
                writeAssembly(ONE_PARAM, BE_OP, AndOrSkip + String.valueOf(OrAnd));
            }
            writeAssembly(NOP + "\n");

    }

    public void AndorSkipEnd(STO expr){
        writeAssembly(AndOrSkip + String.valueOf((OrAnd)) + ":\n");
        if(((ExprSTO)expr).getOperator().equals("||")) {
            writeAssembly(TWO_PARAM, MOVE, String.valueOf(1), OUTPUT0);
        }else{ // &&
            writeAssembly(TWO_PARAM, MOVE, String.valueOf(0), OUTPUT0);
        }
        writeAssembly(AndOrEnd + String.valueOf((OrAnd)) + ":\n");
        SetFunctionVarSize(expr);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
    }

    public void SetFunctionVarSize(STO expr){
        int size = -currentFunc.getFuncVarSize() - 4;
        expr.setOffset(String.valueOf(size));
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        currentFunc.setFuncVarSize(-size);
    }

    public void DoDecl(STO To, STO From)
    {
        if (To.getGlobal()) {
            DoBasicGlobalDecl(To, From);
        } else {
            DoBasicLocalDecl(To, From);
        }
    }

    public void DoBasicGlobalDecl (STO To, STO From) {

        if(FirstFlag) {
            decreaseIndent();
            FirstFlag = false;
        }
        boolean initConst = false;
        boolean initNonConst = false;
        writeAssembly("\n");
        if(From == null) { // not initialized
            increaseIndent();
            if(To.getStatic() || To.getGlobal()) {
                writeAssembly(TWO_STRING, SECTION, BSS);
            }
            else{
                writeAssembly(TWO_STRING, SECTION, STACK);
            }
        } else { // initialized
            increaseIndent();
            if(To.getStatic() || To.getGlobal()) {
                if (From instanceof ConstSTO) {
                    initConst = true; //initialized
                    writeAssembly(TWO_STRING, SECTION, DATA);
                } else { //expr , nonConst
                    initNonConst = true;
                    writeAssembly(TWO_STRING, SECTION, BSS);
                }
            } else {
                writeAssembly(TWO_STRING, SECTION, STACK);
            }
        }
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));

        if(To.getGlobal() && (!To.getStatic())) {
            writeAssembly(TWO_STRING, GLOBAL, To.getName());
        }

        decreaseIndent();
        writeAssembly(To.getName() + ":\n");
        increaseIndent();

        if (initConst) { //if initialized
            if(From.getType() instanceof TypeInt) {
                if (To.getType() instanceof TypeInt) {
                    writeAssembly(STRING_NUM, WORD, ((ConstSTO) From).getIntValue_As());
                } else {
                    writeAssembly(STRING_NUM, SINGLE, ((ConstSTO) From).getFloatValue_As());
                }
            } else if (From.getType() instanceof TypeFloat) {
                writeAssembly(STRING_NUM, SINGLE, ((ConstSTO) From).getFloatValue_As());
            } else if (From.getType() instanceof TypeBool) {
                writeAssembly(STRING_NUM, WORD, ((ConstSTO) From).getBoolValue_As());
            } else {
                System.out.println("ALERT: Something is wrong with .rc file, or we missed something");
            }
        } else {
            writeAssembly(STRING_NUM, SKIP, String.valueOf(To.getType().getSize()));
        }

        GoBackToText();
        decreaseIndent();

        if (initNonConst) { //full != null , = expr
            String name = ".$.init." + To.getName();
            writeAssembly(name + ":\n");
            writeAssembly(TWO_PARAM, SET_OP, "SAVE."+ name, GLOBAL1);
            writeAssembly(THREE_PARAM, SAVE_OP, SP, GLOBAL1, SP);
            increaseIndent();
            writeAssembly("\n");
            int position = -4;
            From.setOffset(String.valueOf(position));
                currentFunc = new FuncSTO("init", null);
                currentFunc.setFuncVarSize(0);

            DoCalculation(From);
            DoBasicLocalDecl(To, From);
            EndOfFuncForInit(name);
        }

    }


    public void EndOfFuncForInit(String name){
        decreaseIndent();
        decreaseIndent();
        writeAssembly("\n");
        writeAssembly("! End of function " + name+ "\n");
        PrintEndForInit(name);
        writeAssembly(END_SAVE, name, String.valueOf(currentFunc.getFuncVarSize()));
        decreaseIndent();
        writeAssembly(name +".fini:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");
        writeAssembly("\n");
        increaseIndent();
        increaseIndent();
        writeAssembly(TWO_STRING, SECTION, INIT);
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
        writeAssembly(ONE_PARAM, CALL, name);
        writeAssembly(NOP + "\n");
        decreaseIndent();
        GoBackToText();

    }


    public void PrintEndForInit(String name){
        writeAssembly(TWO_STRING, CALL_OP,name +".fini");
        writeAssembly("nop\n");
        writeAssembly("ret\n");
        writeAssembly("restore\n");
    }

    public void DoArray(STO a, int index, STO b){ // a is arraySto, b is varsto
        ResetIntandFloat();
        writeAssembly("\n");
        writeAssembly("!" + a.getName() + "[" + index + "] \n");
        PickOutnum();
        int maxIndex = ((TypeArray)a.getType()).getIndexSize();
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(index), call);
        outnum++;
        PickOutnum(); // remember to outnum++;
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(maxIndex), call);
        outnum++;
        writeAssembly(ONE_PARAM, CALL, ARRCHECK);
        writeAssembly(NOP + "\n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4), OUTPUT1);
        writeAssembly(ONE_PARAM, CALL, ".mul");
        writeAssembly(NOP + "\n");

        String size = a.getOffset();

        writeAssembly(TWO_PARAM, MOVE, OUTPUT0, OUTPUT1);
        if(a.getGlobal()){
            writeAssembly(TWO_PARAM, SET_OP, a.getName(), OUTPUT0);
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, OUTPUT0, OUTPUT0);
        }else {
            writeAssembly(TWO_PARAM, SET_OP, size, OUTPUT0);
            writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);

        }
        writeAssembly(ONE_PARAM, CALL, PTRCHECK);
        writeAssembly(NOP + "\n");
        writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT1, OUTPUT0);
        int offsetposition = -currentFunc.getFuncVarSize() - 4;
        b.setOffset(String.valueOf(offsetposition));
        ((VarSTO)b).SetAsArray();

        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(offsetposition), OUTPUT1);
        currentFunc.setFuncVarSize(-offsetposition);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
    }

    public void LoadArrayTo(STO a, STO b){
        int size = Integer.parseInt(a.getOffset());

        if(b instanceof VarSTO) {
            if (((VarSTO) b).IfIsArray()) {
                size = size + 4;
            }
        }
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1); //get the offset of x
        if(a.getGlobal()){
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, OUTPUT1, OUTPUT1); // go to the offset
        }else {
            writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1); // go to the offset
        }
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT1 + "]", OUTPUT1); // go to the offset

    }
    public void LoadArrayFrom(STO a){
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(a.getOffset()), LOCAL7); //get the offset of x
        if(a.getGlobal()){
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7); // go to the offset
        }else {
            writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7); // go to the offset
        }
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", LOCAL7); // go to the offset

    }

    /* make instance of constuct , make a struct var*/
    public void MakeStructInstant(Type a, String b){
        int size = -currentFunc.getFuncVarSize() - 4;
        currentFunc.setFuncVarSize(-size);
        writeAssembly("\n");
        writeAssembly("!" + b + "." + a.getName() + "(...)" + "\n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size-4), OUTPUT0);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);
        String name = MakeFullStructName(a.getName());
        writeAssembly(ONE_PARAM, CALL, name);
        writeAssembly(NOP + "\n");
        writeAssembly("\n");
        writeAssembly(ONE_PARAM, SECTION, BSS);
        writeAssembly(ONE_PARAM, ALIGN, String.valueOf(4));
        decreaseIndent();
        writeAssembly(CtorDtor + String.valueOf(1) + "\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, SKIP, String.valueOf(4) );
        GoBackToText();
        writeAssembly("\n");
        writeAssembly(TWO_PARAM, SET_OP, CtorDtor + String.valueOf(1), OUTPUT0 );
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size-4), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE, OUTPUT1, "[" + OUTPUT0 + "]");

    }

    public void DoBasicLocalDecl(STO to, STO from)
    {
        ResetIntandFloat();
        Boolean IsArray = false;
        //if(from instanceof  ExprSTO) return;
        if(to.getStatic()){
            writeAssembly("\n");
            to.setOffset(makeFullFuncName(currentFunc) + "." + to.getName());
            if(from != null){
                writeAssembly(TWO_STRING, SECTION, DATA);
                writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
                decreaseIndent();
                writeAssembly(makeFullFuncName(currentFunc)+"."+to.getName()+":\n");
                increaseIndent();
                writeAssembly(STRING_NUM, WORD, String.valueOf(from.getName()));
                GoBackToText();
            }else{
                writeAssembly(TWO_STRING, SECTION, BSS);
                writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
                decreaseIndent();
                writeAssembly(makeFullFuncName(currentFunc)+"."+to.getName()+":\n");
                increaseIndent();
                writeAssembly(STRING_NUM, SKIP, String.valueOf(4));
                GoBackToText();
            }
            return;
        }
        if(from == null) {
            return;
        }
        if(to.ifthis()) { // check has error.
            int size = -currentFunc.getFuncVarSize() - 4;
           // writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
            currentFunc.setFuncVarSize(-size);

            writeAssembly("\n");
            writeAssembly("! this." + to.getName() + "\n");
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(68), OUTPUT0);
            writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);
            writeAssembly(TWO_PARAM, LOAD_OP, "["+OUTPUT0+"]", OUTPUT0);
            writeAssembly(TWO_PARAM,SET_OP, String.valueOf(0), OUTPUT1);
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, OUTPUT1, OUTPUT1);
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, OUTPUT1, OUTPUT0);
            writeAssembly(TWO_PARAM,SET_OP, String.valueOf(size), OUTPUT1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");

            writeAssembly("\n");
            writeAssembly("! this." + to.getName() + " = " + from.getName() + "\n");
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size-4), OUTPUT1); //for the s1.x
            writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT1 + "]", OUTPUT1);
            writeAssembly(TWO_PARAM, SET_OP, from.getName(), OUTPUT0);
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "["+OUTPUT1+"]");
            return;
        }

        writeAssembly("\n");
        writeAssembly("! " + to.getName() + "=" + from.getName() + "\n");



        if(to instanceof VarSTO) {
            if (((VarSTO) to).IfIsArray()) {
                LoadArrayTo(to, from);
                IsArray = true;
            }
        }
        if(!IsArray){
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(to.getOffset()), OUTPUT1); //get the offset of x
            if(to.getGlobal()){
                writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, OUTPUT1, OUTPUT1); // go to the offset
            }else {
                writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1); // go to the offset
            }
        }
        IsArray = false;
        if(from instanceof VarSTO || (from instanceof ConstSTO && from.getIsAddressable())) { // from is var
            if(from instanceof VarSTO){
                if (((VarSTO) from).IfIsArray()) {
                    LoadArrayFrom(from);
                    writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
                    writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
                    IsArray = true;
                }
            }
            if(!IsArray) {
                if (!from.GetUpdateFlag()) { //when false
                    writeAssembly(TWO_PARAM, SET_OP, String.valueOf(from.getOffset()), LOCAL7);  //local's offset is -4,-8..., gloabal's offset is m, y, z....
                } else { //when true
                    writeAssembly(TWO_PARAM, SET_OP, String.valueOf(from.getUpdatedOffset()), LOCAL7);  //local's offset is -4,-8..., gloabal's offset is m, y, z....
                    from.setUpdateFlag(false);
                }
                if (from.getType() instanceof TypeFloat) {
                    if (from.getGlobal()) {
                        writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7); //reach the address of that variable
                    } else {
                        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7); //reach the address of that variable
                    }
                    writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", F0);
                    writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
                } else { // from is int
                    if (from.getGlobal()) {
                        writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7); //reach the address of that variable
                    } else {
                        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7); //reach the address of that variable
                    }
                    if (to.getType() instanceof TypeFloat) {
                        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
                        ConvertIntToFloat();
                        writeAssembly(TWO_PARAM, STORE, "%f0", "[" + OUTPUT1 + "]");

                    } else {
                        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
                        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
                    }
                }
            }
            IsArray = false;
        }else if (from instanceof ExprSTO) {
            writeAssembly(TWO_PARAM, SET_OP, from.getOffset(), LOCAL7);
            writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
            if(from.getType() instanceof TypeFloat){
                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", F0);
            }else {
                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
            }

            if(from.getType() instanceof TypeInt && to.getType() instanceof TypeFloat){
                ConvertIntToFloat();
            }

            if(from.getType() instanceof TypeInt && to.getType() instanceof TypeInt) {
                writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
            }else if(from.getType() instanceof TypeFloat || to.getType() instanceof TypeFloat) {
                writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
            }else if(from.getType() instanceof TypeBool){
                writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
            }


        }else{ // from is const, like 1,2,3, 1.1
            if(to.getType() instanceof TypeFloat){
                    if(from.getType()instanceof TypeFloat) { //convert
                        writeAssembly("\n");
                        writeAssembly(TWO_STRING, SECTION, RODATA);
                        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
                        decreaseIndent();
                        writeAssembly(FLOAT_FUC + String.valueOf(floatnum) + ": \n");
                        increaseIndent();
                        writeAssembly(ONE_PARAM, SINGLE, ((ConstSTO) from).getFloatValue_As());
                        GoBackToText();
                        writeAssembly(TWO_PARAM, SET_OP, FLOAT_FUC + String.valueOf(floatnum), LOCAL7);
                        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", "%f0");
                        writeAssembly(TWO_PARAM, STORE, "%f0", "[" + OUTPUT1 + "]");
                        floatnum++;
                    }else if(from.getType() instanceof TypeInt){
                        writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO) from).getIntValue_As(), OUTPUT0);
                        //convert int to float
                        ConvertIntToFloat();
                        writeAssembly(TWO_PARAM, STORE, "%f0", "[" + OUTPUT1 + "]");

                    }
            }else if(to.getType() instanceof  TypeInt){
//                  if(((VarSTO)to).IfIsArray()){
//                      writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT1 + "]", OUTPUT1);
//                  }
                  writeAssembly(TWO_PARAM, SET_OP, String.valueOf(((ConstSTO) from).getIntValue()), OUTPUT0);
                  writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
            }else if(to.getType() instanceof TypeBool){
                writeAssembly(TWO_PARAM, SET_OP,((ConstSTO) from).getBoolValue_As(), OUTPUT0 );
                writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
            }
        }
    }

    public void CallStructFunc(STO func, STO struct){
        Vector<STO> args;
        writeAssembly("\n");
        writeAssembly("!" + struct.getType().getName() + "." + struct.getName() + "."  + func.getName() + "\n");
        int size = -currentFunc.getFuncVarSize();
        PickOutnum();
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), call);
        writeAssembly(THREE_PARAM, ADD_OP, FP, call, call);
        FuncSTO fun = (FuncSTO) func;
        args = fun.getParameter();
        for(STO a : args){
            writeAssembly("!" + a.getName() + "\n");
        }


    }


    public void DoFuncCall(STO expr, STO func, Vector<STO> args){
        FuncSTO fun = (FuncSTO)func;
        writeAssembly("\n");
        writeAssembly("!" + func.getName() + "(...)\n");
        Vector<STO> params =  fun.getParameter();
        int i = 0;
        if(params != null) {
            for (STO a : params) {
                STO tmp = args.get(i);

                writeAssembly("!" + a.getName() + "<-" + tmp.getName() + "\n");

                if (tmp instanceof VarSTO) {
                    VarSTO t = (VarSTO) tmp;
                    if (((VarSTO) a).getamper()) {
                        PickOutnum();
                        writeAssembly(TWO_PARAM, SET_OP, t.getOffset(), call);
                        writeAssembly(THREE_PARAM, ADD_OP, FP, call, call);
                        outnum++;
                    } else {
                        LoadIntorFloat(t);
                    }

                } else {
                    PickOutnum();
                    writeAssembly(TWO_PARAM, SET_OP, tmp.getName(), call);
                    outnum++;
                }
                i++;
            }
        }
        String name = makeFullFuncName(fun);
        writeAssembly(ONE_PARAM, CALL_OP, name);
        writeAssembly(NOP + "\n");
        int size = -currentFunc.getFuncVarSize() - 4; // for the func, cuz one is exprsto one is funcsto, onthing i can do
        currentFunc.setFuncVarSize(-size);
        expr.setOffset(String.valueOf(size));
        PickOutnum();
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
    }
    public void LoadRegister(STO t){
        writeAssembly(TWO_PARAM, SET_OP, t.getOffset(), LOCAL7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
        writeAssembly(TWO_PARAM, "[" + LOCAL7 + "]");
    }
    public void DoFolding(STO result){
        ConstSTO resu = (ConstSTO) result;
        writeAssembly("\n");
        writeAssembly("! folding \n");
        if(resu.getType() instanceof TypeInt){
            writeAssembly(TWO_PARAM, SET_OP, resu.getIntValue_As(), OUTPUT1);
            writeAssembly(TWO_PARAM, SET_OP, INTFMT, OUTPUT0);
            writeAssembly(ONE_PARAM, CALL, PRINTF);
            writeAssembly(NOP);

        }else if(resu.getType() instanceof  TypeFloat){
            writeAssembly(TWO_STRING, SECTION, RODATA);
            writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
            decreaseIndent();
            writeAssembly(FLOAT_FUC+ floatnum + ":\n");
            increaseIndent();
            writeAssembly(ONE_PARAM, SINGLE, resu.getFloatValue_As());
            GoBackToText();
            writeAssembly(TWO_PARAM, SET_OP, FLOAT_FUC + floatnum, LOCAL7);
            PickFloatnum();
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
            writeAssembly(ONE_PARAM, CALL, PRINTFLOAT);
            writeAssembly(NOP);
            fnum++;
            floatnum++;
        }else if(resu.getType() instanceof  TypeBool){
            writeAssembly(TWO_PARAM, SET_OP, resu.getIntValue_As(), OUTPUT0);
            writeAssembly(ONE_PARAM, CALL, PRINTBOOL);
            writeAssembly(NOP);
        }

    }

    public void DoIncDecOpPre(STO old, STO a, String operation){
        ResetIntandFloat();
        writeAssembly("\n");
        writeAssembly("! " + a.getName() + "\n");
        LoadIntorFloat(old); //f0

        if(a.getType() instanceof  TypeFloat){
            ConstSTO floatbox = new ConstSTO("container", new TypeInt("int", 4), 1.0);
            DoFloatAssign(floatbox); //f1
            if(operation.equals("++")) {
                writeAssembly(THREE_PARAM, FADDS, F0, F1, F2);
            }else{ // --
                writeAssembly(THREE_PARAM, FSUBS, F0, F1, F2);
            }
        }

        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(1), OUTPUT1);
            if(operation.equals("++")){
                writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT1, OUTPUT2);
            }else{
                writeAssembly(THREE_PARAM, SUB_OP, OUTPUT0, OUTPUT1, OUTPUT2);
            }
        }


        writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);

        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT2, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof  TypeFloat){
            writeAssembly(TWO_PARAM, STORE, F2, "[" + OUTPUT1 + "]");
        }

        writeAssembly(TWO_PARAM, SET_OP, old.getOffset() , OUTPUT1 );
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT2, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof  TypeFloat){
            writeAssembly(TWO_PARAM, STORE, F2, "[" + OUTPUT1 + "]");
        }

        return;
    }

    public void DoStore(STO a){
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(a.getOffset()), OUTPUT1 );
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT2, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof  TypeFloat){
            writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
        }

    }
    public void DoIncDecOpPost(STO old, STO a, String operation){
        ResetIntandFloat();
        writeAssembly("\n");
        writeAssembly("! " + a.getName() + "\n");
        LoadIntorFloat(old);

        if(a.getType() instanceof  TypeFloat){
            ConstSTO floatbox = new ConstSTO("container", new TypeInt("int", 4), 1.0);
            DoFloatAssign(floatbox);
            if(operation.equals("++")) {
                writeAssembly(THREE_PARAM, FADDS, F0, F1, F2);
            }else{ // --
                writeAssembly(THREE_PARAM, FSUBS, F0, F1, F2);
            }

        }
        if(a.getType() instanceof TypeInt) {
         //    writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
             writeAssembly(TWO_PARAM, SET_OP, String.valueOf(1), OUTPUT1);
             if(operation.equals("++")){
                writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT1, OUTPUT2);
            }else{
                writeAssembly(THREE_PARAM, SUB_OP, OUTPUT0, OUTPUT1, OUTPUT2);
            }
        }


        writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);


        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof  TypeFloat){
            writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
        }

        writeAssembly(TWO_PARAM, SET_OP, old.getOffset(), OUTPUT1 );
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if(a.getType() instanceof TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT2, "[" + OUTPUT1 + "]");
        }else if(a.getType() instanceof  TypeFloat){
            writeAssembly(TWO_PARAM, STORE, F2, "[" + OUTPUT1 + "]");
        }

        return;
    }

    public void DoNot(STO a, String operation){
        writeAssembly("\n");
        writeAssembly("! " + operation + a.getName() + "\n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(a.getOffset()), LOCAL7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7 );
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
        writeAssembly(THREE_PARAM, XOR_OP, OUTPUT0, String.valueOf(1), OUTPUT0);

        int size = -currentFunc.getFuncVarSize() - 4;
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        currentFunc.setFuncVarSize(-size);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");

        a.setUpdateOffset(String.valueOf(size));
        a.setUpdateFlag(true);//WHEN IT HAS ++ OR --

    }

    public void DoReturn(STO RE){
        writeAssembly("\n");
        Type returnType = currentFunc.getReturnType();
        if(RE.getName().equals("void")){
            writeAssembly("! return;\n");
        }else if(RE instanceof VarSTO || (RE instanceof ConstSTO && RE.getIsAddressable())){ // VAR
            writeAssembly("! return" + RE.getName() + ";\n");
            writeAssembly(TWO_PARAM, SET_OP,String.valueOf(RE.getOffset()), LOCAL7 );
            writeAssembly(THREE_PARAM, ADD_OP,FP, LOCAL7, LOCAL7);
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", INPUT0);
        }else if(RE instanceof ConstSTO ){ //CONST
            ConstSTO value = (ConstSTO) RE;
            writeAssembly("! return" + value.getIntValue() + ";\n");
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(value.getIntValue()), INPUT0);
        }
        if(returnType instanceof TypeFloat && RE.getType() instanceof TypeInt){
            returnflag = true;
            ConvertIntToFloat();
        }
        PrintEnd();
    }

    public void EndOfFunc(){
        indent_level = 1;
        writeAssembly("\n");
        writeAssembly("! End of function " + makeFullFuncName(currentFunc) + "\n");
        PrintEnd();
        writeAssembly(END_SAVE, makeFullFuncName(currentFunc), String.valueOf(currentFunc.getFuncVarSize()));
        decreaseIndent();
        writeAssembly(makeFullFuncName(currentFunc)+".fini:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");
        decreaseIndent();
    }


    public void EndOfFuncStr(){
        String name =  structName + "." + makeFullFuncName(currentFunc);
        decreaseIndent();
        writeAssembly("\n");
        writeAssembly("! End of function " + name + "\n");
        PrintEnd();
        writeAssembly(END_SAVE, name, String.valueOf(currentFunc.getFuncVarSize()));
        decreaseIndent();
        writeAssembly(name +".fini:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM,SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");
        decreaseIndent();
    }


    public void PrintEnd(){
        writeAssembly(TWO_STRING, CALL_OP, makeFullFuncName(currentFunc) +".fini");
        writeAssembly("nop\n");
        writeAssembly("ret\n");
        writeAssembly("restore\n");
    }

    public void CoutEnd(){
        writeAssembly("\n\n");
        writeAssembly("! cout << endl \n");
        writeAssembly(TWO_PARAM, SET_OP, COUTEND, OUTPUT0);
        writeAssembly(ONE_PARAM, CALL_OP, PRINTF);
        writeAssembly(NOP + "\n");

    }

    /*create a new struct */
    public void MakeStruct(String id){
        structName = id;
        decreaseIndent();
        String name = MakeFullStructName(id);
        writeAssembly(name + ":\n");
        writeAssembly(TWO_PARAM, SET_OP, "SAVE."+ name, GLOBAL1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, GLOBAL1, SP);
        writeAssembly("\n");
        increaseIndent();
        writeAssembly("! Store params\n");
        writeAssembly(TWO_PARAM, STORE, INPUT1, "[" + FP + "+68]");
        decreaseIndent();
        writeAssembly("\n");
        increaseIndent();
        writeAssembly("! End of function " + name);
        writeAssembly("\n");
        PrintEndForInit(name);
        writeAssembly("SAVE." + name + "=-(92 + 0) & -8\n");
        writeAssembly("\n");
        decreaseIndent();
        writeAssembly(name + ".fini:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");

        String param = MakeFullStructPara(id);
        decreaseIndent();
        writeAssembly(param + "\n");
        writeAssembly(TWO_PARAM, SET_OP, "SAVE."+ param, GLOBAL1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, GLOBAL1, SP);
        writeAssembly("\n");
        increaseIndent();
        increaseIndent();
        writeAssembly("! Store params\n");
        writeAssembly(TWO_PARAM, STORE, INPUT1, "[" + FP + "+68]");

        decreaseIndent();
        writeAssembly("\n");
        writeAssembly("! End of function " + param);
        writeAssembly("\n");
        PrintEndForInit(name);
        writeAssembly("SAVE." + param + "=-(92 + 0) & -8\n");
        writeAssembly("\n");
        decreaseIndent();
        writeAssembly(param + ".fini:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");


    }

    public String MakeFullStructName(String id){
        return id + "." + id + ".void";
    }
    public String MakeFullStructPara(String id){
        return id + ".$" + id + ".void";
    }

    public void DoFuncDecl(STO func){ // this part should be called after parameter's part called in this function scope ends
        FuncSTO funcName = (FuncSTO) func;
        currentFunc = funcName;
        if(a.add(funcName.getName())) {
            increaseIndent();
            writeAssembly(ONE_PARAM, GLOBAL, funcName.getName());
            decreaseIndent();
            writeAssembly(func.getName() + ":\n");
            funcName.FuncDecled(true);
            decreaseIndent();
        }
        String FullName = makeFullFuncName(funcName);
        writeAssembly(FullName + ":\n"); // not the return type. linked parameters type
        if(indent_level <= 0){
            indent_level = 1;
        }

        writeAssembly(TWO_PARAM, SET_OP, "SAVE."+ FullName, GLOBAL1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, GLOBAL1, SP);
        writeAssembly("\n");
        increaseIndent();
        writeAssembly("! Store params\n");
        if(funcName.getParameter() != null){ //spit the parameters
            Vector<STO> params = funcName.getParameter();
            int startNum = 68;
            int i = 0;
            for(STO param : params){
                param.setOffset(String.valueOf(startNum));
                writeAssembly(TWO_PARAM, STORE, "%i" + String.valueOf(i), "[%fp+" + String.valueOf(startNum) + "]");
                i++;
                startNum += 4;
            }
        }
    }

    public void DoFuncDeclStr(STO func){ // this part should be called after parameter's part called in this function scope ends
        FuncSTO funcName = (FuncSTO) func;
        currentFunc = funcName;
        decreaseIndent();
        String FullName = structName + "." + makeFullFuncName(funcName);
        writeAssembly(FullName + ":\n"); // not the return type. linked parameters type
        if(indent_level <= 0){
            indent_level = 1;
        }

        writeAssembly(TWO_PARAM, SET_OP, "SAVE."+ FullName, GLOBAL1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, GLOBAL1, SP);
        writeAssembly("\n");
        increaseIndent();
        writeAssembly("! Store params\n");
        writeAssembly(TWO_PARAM, STORE, "%i0", "[%fp+68]");
        if(funcName.getParameter() != null){ //spit the parameters
            Vector<STO> params = funcName.getParameter();
            int startNum = 72;
            int i = 1;
            for(STO param : params){
                writeAssembly(TWO_PARAM, STORE, "%i" + String.valueOf(i), "[%fp+" + String.valueOf(startNum) + "]");
                i++;
                startNum += 4;
            }
        }
    }

    public void ConvertIntToFloat(){

        int size = -currentFunc.getFuncVarSize() - 4;
        writeAssembly(TWO_PARAM, SET_OP,String.valueOf(size), LOCAL7);
        currentFunc.setFuncVarSize(-size);
        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
        if(returnflag){
            writeAssembly(TWO_PARAM, STORE, INPUT0, "[" + LOCAL7 + "]");
        }else {
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + LOCAL7 + "]");
        }
        PickFloatnum();
        writeAssembly(TWO_PARAM, LOAD_OP, "["+ LOCAL7 + "]", call);
        writeAssembly(TWO_PARAM, FITOS, call, call);
        fnum++;
        returnflag = false;
    }

    public void DoForEach(STO a){
        writeAssembly("\n");
        int maxIndex = ((TypeArray)a.getType()).getIndexSize() * 4;
        writeAssembly("! foreach(...)\n");
        writeAssembly("! traversal ptr == --array\n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(-maxIndex), OUTPUT0);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);

        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4), OUTPUT1);
        writeAssembly(THREE_PARAM, SUB_OP, OUTPUT0, OUTPUT1, OUTPUT0);
        int size = -currentFunc.getFuncVarSize() - 4;
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        currentFunc.setFuncVarSize(-size);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, STORE,OUTPUT0, "[" + OUTPUT1 + "]");

    }

    public void CheckForEachCondition(STO a){
        int maxIndex = ((TypeArray)a.getType()).getIndexSize() * 4;

        decreaseIndent();
        writeAssembly(LOOPCHECK + LOOPCHECKNUM + ":");
        writeAssembly("\n");
        increaseIndent();
        writeAssembly("! ++traversal ptr\n");
        int size = -currentFunc.getFuncVarSize();
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT1 + "]", OUTPUT0);
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4), OUTPUT2);
        writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT2, OUTPUT0);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");

        writeAssembly("! traversal ptr < array and addr ? \n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(-maxIndex), OUTPUT0);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(maxIndex), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, OUTPUT0, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size), OUTPUT0);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT0, OUTPUT0);
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT0 + "]", OUTPUT0);
        writeAssembly(TWO_PARAM, CMP_OP, OUTPUT0, OUTPUT1);
        writeAssembly(ONE_PARAM, BGE_OP,LOOPEND + String.valueOf(LOOPCHECKNUM));
        writeAssembly(NOP + "\n");


        writeAssembly("! iterVar = currentElem\n");
        writeAssembly(TWO_PARAM, SET_OP, String.valueOf(size + 4), OUTPUT1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + OUTPUT0 + "]", OUTPUT0);
        writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
        writeAssembly("\n");
        writeAssembly("! Start of loop body");
        increaseIndent();
        writeAssembly("\n");

    }

    public void DoCin(STO in){
        writeAssembly("\n");
        writeAssembly("! cin >> " + in.getName() + "\n" );
        String callfunc;
        if(in.getType() instanceof  TypeInt) {
            callfunc = "inputInt";
        }else{
            callfunc = "inputFloat";
        }
        writeAssembly(ONE_PARAM, CALL, callfunc);
        writeAssembly(NOP + "\n");
        if(in instanceof  ConstSTO){
            writeAssembly(TWO_PARAM, SET_OP, String.valueOf(0), OUTPUT1);
        }else{
            writeAssembly(TWO_PARAM, SET_OP, in.getOffset(), OUTPUT1);
        }
        writeAssembly(THREE_PARAM, ADD_OP, FP, OUTPUT1, OUTPUT1);

        if(in.getType() instanceof  TypeInt) {
            writeAssembly(TWO_PARAM, STORE, OUTPUT0, "[" + OUTPUT1 + "]");
        }else{
            writeAssembly(TWO_PARAM, STORE, F0, "[" + OUTPUT1 + "]");
        }
    }
    public void CallExit(STO a){
        writeAssembly("\n");
        writeAssembly("! exit(" + a.getName() + ")\n");
        if(a instanceof ConstSTO){
            writeAssembly(TWO_PARAM, SET_OP, ((ConstSTO)a).getIntValue_As(), OUTPUT0);
        }else {
//            if(a.getGlobal()) {
//                writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), LOCAL7);
//                writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7);
//
//            }else {
//                writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), OUTPUT1);
//                writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);
//            }
//            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", OUTPUT0);
        LoadIntorFloat(a);
        }
        writeAssembly(ONE_PARAM, CALL, EXIT);
        writeAssembly(NOP+"\n");

    }

    public String makeFullFuncName(FuncSTO func){
        String name = func.getName();
        Vector<STO> params = func.getParameter();
        if(params != null){
            for (STO param : params){
                name += "."+param.getType().getName();
            }
        }else{
            name +=".void";
        }
        return name;
    }

    public void GoBackToText(){
        writeAssembly("\n");
        writeAssembly(TWO_STRING, SECTION, TEXT);
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
    }

    public void DoFloatAssign(STO from){
        writeAssembly("\n");
        writeAssembly(TWO_STRING, SECTION, RODATA);
        writeAssembly(STRING_NUM, ALIGN, String.valueOf(4));
        decreaseIndent();
        writeAssembly(FLOAT_FUC + String.valueOf(floatnum) + ": \n");
        increaseIndent();
        writeAssembly(ONE_PARAM, SINGLE, ((ConstSTO) from).getFloatValue_As());
        GoBackToText();
        writeAssembly(TWO_PARAM, SET_OP, FLOAT_FUC + String.valueOf(floatnum), LOCAL7);

        PickFloatnum();
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
        fnum++;
        floatnum++;
    }

    public void PickFloatnum(){
        switch (fnum){
            case 0:
                call = F0;
                break;
            case 1:
                call = F1;
                break;
            case 2:
                call = F2;
                break;
            default:
                call = F2;
        }
    }

    public void PickOutnum(){
        switch (outnum){
            case 0:
                call = OUTPUT0;
                break;
            case 1:
                call = OUTPUT1;
                break;
            case 2:
                call = OUTPUT2;
                break;
            case 3:
                call = OUTPUT3;
                break;
            case 4:
                call = OUTPUT4;
                break;
            case 5:
                call = OUTPUT5;
                break;
            default:
                call = OUTPUT5;
        }
    }

    public void LoadIntorFloat(STO a){
        if(!a.GetUpdateFlag()) { //when false, dont have ++
            writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), LOCAL7);

        }else{
            writeAssembly(TWO_PARAM, SET_OP, a.getUpdatedOffset(), LOCAL7);
            a.setUpdateFlag(false);
        }

        if(a.getGlobal()) {
            writeAssembly(THREE_PARAM, ADD_OP, GLOBAL0, LOCAL7, LOCAL7);
        }else{
            writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7);

        }

        if(a instanceof VarSTO){
            if(((VarSTO)a).getamper()){
                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", LOCAL7);
            }
        }

        if(a.getType() instanceof TypeInt) {
            PickOutnum();
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
            outnum++;
        }
        else if(a.getType() instanceof TypeFloat){
          //  if(FloatCal){
            //    writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", F1);
             //   FloatCal = false;
            //}else {
                PickFloatnum();
                writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
                fnum++;
           // }
        }else if(a.getType() instanceof TypeBool){
            PickOutnum();
            writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
            outnum++;
        }



    }

    public void LoadFloat(STO a){
        writeAssembly(TWO_PARAM, SET_OP, a.getOffset(), LOCAL7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, LOCAL7, LOCAL7); // Global expr need change fp to g0, do it later
        String call;
        switch (fnum){
            case 0:
                call = F0;
                break;
            case 1:
                call = F1;
                break;
            case 2:
                call = F2;
                break;
            default:
                call = F2;
        }
        writeAssembly(TWO_PARAM, LOAD_OP, "[" + LOCAL7 + "]", call);
        fnum++;
    }

    //9
    public void writeAssembly(String template, String ... params) {
        StringBuilder asStmt = new StringBuilder();
        // 10
        for (int i=0; i < indent_level; i++) {
            asStmt.append(SEPARATOR);
        }
        // 11
        asStmt.append(String.format(template, (Object[])params));
        try {
            fileWriter.write(asStmt.toString());
        } catch (IOException e) {
            System.err.println(ERROR_IO_WRITE);
            e.printStackTrace();
        }
    }

    // 8
    public void decreaseIndent() {
        indent_level--;
    }

    public void increaseIndent() {
        indent_level++;
    }

    public void dispose() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println(ERROR_IO_CLOSE);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
