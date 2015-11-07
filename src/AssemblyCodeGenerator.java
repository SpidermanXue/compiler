import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

/**
 * Created by Sabryna on 11/3/15.
 */
public class AssemblyCodeGenerator {
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
            "/*\n" +
                    " * Generated %s\n" +
                    " */\n\n";
    // 5
    private static final String SEPARATOR = "\t";
    // 6
    private static final String SET_OP = "set";
    private static final String SAVE_OP = "save";
    private static final String ADD_OP = "add";
    private static final String LOAD_OP = "ld";
    private static final String CALL_OP = "call";
    private static final String STORE = "st";
    private static final String Section = ".section";
    private static final String Align = ".align";
    private static final String Global = ".global";
    private static final String BSS = "\".bss\"";
    private static final String SKIP = ".skip";
    private static final String WORD = ".word";
    private static final String TEXT = "\".text\"";
    private static final String DATA = "\".data\"";
    private static final String HEAP = "\".heap\"";
    private static final String STACK = "\".stack\"";

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


    private static final String SP = "%sp";
    private static final String FP = "%fp";

    private static final String GLOBAL1 = "%g1";



    private static final String TWO_PARAM = "%s" + SEPARATOR + "%s, %s\n";
    private static final String TWO_STRING = "%s" + SEPARATOR + "%s \n";
    private static final String STRING_NUM = "%s" + SEPARATOR + SEPARATOR + "%s \n";
    private static final String THREE_STRING = "%s\t" + "%s" + SEPARATOR +" %s \n";
    private static final String FOUR_STRING = "%s\t" + "%s" + SEPARATOR + " %s" + SEPARATOR + " %s +  \n";
    private static final String END_SAVE = "SAVE." + "%s" + "=" + "-(92 + " + "%s" +") & -8 \n\n";


    private FuncSTO currentFunc;

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

    public void DoBasicGlobalDecl (STO To, STO From){ // typename , id , location of var, value
        increaseIndent();
        if(From == null){ // not initialized
            if(To.getStatic() || To.getGlobal()) {
                writeAssembly(TWO_STRING, Section, BSS);
            }
            else{
                writeAssembly(TWO_STRING, Section, STACK);
            }
        }else{ // initialized
            if(To.getStatic() || To.getGlobal()) {
                writeAssembly(TWO_STRING, Section, DATA);
            }
            else{
                writeAssembly(TWO_STRING, Section, STACK);
            }
        }
        writeAssembly(STRING_NUM, Align, String.valueOf(4));

        if(To.getGlobal()){
            writeAssembly(TWO_STRING, Global, To.getName());
        }
        decreaseIndent();
        writeAssembly(To.getName() + ":\n");
        increaseIndent();
        if(From == null) {
            writeAssembly(STRING_NUM, SKIP, String.valueOf(4));
        }else{
            writeAssembly(STRING_NUM, WORD, From.getValue());
        }
        GoBackToText();
    }

    public void DoBasicLocalDecl(STO to, STO from){ // called when each time they declare a local variable
        if(from == null) return;
        writeAssembly("! " + to.getName() + "=" + from.getName());
        writeAssembly(THREE_STRING, SET_OP, String.valueOf(to.getOffset()), OUTPUT0);
        writeAssembly(FOUR_STRING, ADD_OP, FP, OUTPUT1, OUTPUT1);
        if(from instanceof ConstSTO){
            writeAssembly(THREE_STRING, SET_OP, String.valueOf(((ConstSTO) from).getIntValue()), OUTPUT0);

        }else{
            writeAssembly(THREE_STRING, SET_OP, String.valueOf(to.getOffset()), LOCAL7);
            writeAssembly(FOUR_STRING, ADD_OP, FP, LOCAL7, LOCAL7); //reach the address of that variable
            writeAssembly(THREE_STRING, LOAD_OP,"["+LOCAL7+"]" + OUTPUT0);
        }
        writeAssembly(TWO_STRING, OUTPUT0, "["+ OUTPUT1 + "]");

    }

    public void DoReturn(STO RE){
        if(RE.getName().equals("void")){
            writeAssembly("! return;\n");
        }else if(RE instanceof ConstSTO){
            ConstSTO value = (ConstSTO) RE;
            writeAssembly("! return" + value.getIntValue() + ";\n");
            writeAssembly(TWO_STRING, String.valueOf(value.getIntValue()), INPUT1);
        }else if(RE instanceof VarSTO){
            VarSTO value = (VarSTO) RE;
            writeAssembly("! return" + value.getName() + ";\n");
            writeAssembly(THREE_STRING, String.valueOf(value.getOffset()), LOCAL7 );
            writeAssembly(FOUR_STRING, FP, LOCAL7, LOCAL7);
            writeAssembly(THREE_STRING, LOAD_OP, "[" + LOCAL7 + "]", INPUT0);
        }
        PrintEnd();
    }

    public void EndOfFunc(){
        writeAssembly("! End of function " + currentFunc + "\n");
        PrintEnd();
        writeAssembly(END_SAVE, currentFunc, currentFunc.getVarSize());
        decreaseIndent();
        writeAssembly(currentFunc+".fini:\n");
        increaseIndent();
        writeAssembly(FOUR_STRING, SP, String.valueOf(-96), SP);
        writeAssembly("ret\n");
        writeAssembly("restore\n");
    }

    public void PrintEnd(){
        writeAssembly(TWO_STRING, CALL_OP, makeFullFuncName(currentFunc) +".fini");
        writeAssembly("nop\n");
        writeAssembly("ret\n");
        writeAssembly("restore\n");
    }
    public void DoFuncDecl(STO func){ // this part should be called after parameter's part called in this function scope ends
        FuncSTO funcName = (FuncSTO) func;
        currentFunc = funcName;
        increaseIndent();
        writeAssembly(TWO_STRING, Section, TEXT);
        writeAssembly(STRING_NUM, SKIP, String.valueOf(4));
        writeAssembly(TWO_STRING, Global, funcName.getName());
        decreaseIndent();
        writeAssembly(func.getName() + ":\n");
        String FullName = makeFullFuncName(funcName);
        writeAssembly(FullName + ":\n"); // not the return type. linked parameters type
        writeAssembly(THREE_STRING, SET_OP, GLOBAL1 + "\n");
        writeAssembly(FOUR_STRING, SAVE_OP, SP, GLOBAL1, SP + "\n");

        increaseIndent();
        writeAssembly("\n! Store params");
        if(funcName.getParameter() != null){ //spit the parameters
            Vector<STO> params = funcName.getParameter();
            int startNum = 68;
            int i = 0;
            for(STO param : params){
                writeAssembly(THREE_STRING, STORE,"%i" + String.valueOf(i) + "\t[%fp+" + String.valueOf(startNum) + "]");
                i++;
                startNum++;
            }

        }
    }

    public String makeFullFuncName(FuncSTO func){
        String name = func.getName();
        Vector<STO> params = func.getParameter();
        if(params != null){
            for (STO param : params){
                name += "."+param.getName();
            }
        }
        return name;
    }
    public void GoBackToText(){
        writeAssembly("\n\t"+ TWO_STRING, Section, TEXT);
        writeAssembly(STRING_NUM, Align, String.valueOf(4));
    }

    public static void main(String args[]) {
        AssemblyCodeGenerator myAsWriter = new AssemblyCodeGenerator("rc.s");

        myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(TWO_STRING, Section, BSS);
        myAsWriter.writeAssembly(STRING_NUM, Align, String.valueOf(4));
        myAsWriter.writeAssembly(STRING_NUM, Global, args[1]);
        myAsWriter.decreaseIndent();
        myAsWriter.writeAssembly(args[1] + ":\n");
        myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(STRING_NUM, SKIP, String.valueOf(4));


//
//        myAsWriter.increaseIndent();
//        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4095), "%l0");
//        myAsWriter.increaseIndent();
//        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(1024), "%l1");
//        myAsWriter.decreaseIndent();
//
//        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(512), "%l2");

 //       myAsWriter.decreaseIndent();
        myAsWriter.dispose();
    }
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

    public void doBasicDecl(STO var, boolean init)
    {

    }

    public void insertSectionText()
    {
        //inserting just in case section text
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
