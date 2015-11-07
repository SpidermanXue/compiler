import com.sun.tools.internal.jxc.ap.Const;

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

    private static final String Section = ".section";
    private static final String Align = ".align";
    private static final String Global = ".global";
    private static final String SKIP = ".skip";
    private static final String WORD = ".word";
    private static final String SINGLE = ".single";

    private static final String BSS = "\".bss\"";
    private static final String TEXT = "\".text\"";
    private static final String DATA = "\".data\"";
    private static final String HEAP = "\".heap\"";
    private static final String STACK = "\".stack\"";

    private static final String ONE_PARAM = "%s" + SEPARATOR + "%s\n";
    private static final String TWO_PARAM = "%s" + SEPARATOR + "%s, %s\n";
    private static final String TWO_STRING = "%s" + SEPARATOR + "%s\n";
    private static final String STRING_NUM = "%s" + SEPARATOR + SEPARATOR + "%s \n";

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

    public void DoBasicDecl (STO To, STO From) {
        boolean initConst = false;
        increaseIndent();

        if(From == null) { // not initialized
            if(To.getStatic() || To.getGlobal()) {
                writeAssembly(TWO_STRING, Section, BSS);
            }
            else{
                writeAssembly(TWO_STRING, Section, STACK);
            }
        } else { // initialized
            if(To.getStatic() || To.getGlobal()) {
                if (From instanceof ConstSTO) {
                    initConst = true;
                    writeAssembly(TWO_STRING, Section, DATA);
                } else {
                    writeAssembly(TWO_STRING, Section, BSS);
                }
            }
            else {
                writeAssembly(TWO_STRING, Section, STACK);
            }
        }

        writeAssembly(STRING_NUM, Align, String.valueOf(4));

        if(To.getGlobal()) {
            writeAssembly(TWO_STRING, Global, To.getName());
        }

        decreaseIndent();
        writeAssembly(To.getName() + ":\n");
        increaseIndent();

        if (initConst) {
            if(From.getType() instanceof TypeInt) {
                if (To.getType() instanceof TypeInt) {
                    writeAssembly(STRING_NUM, WORD, String.valueOf(((ConstSTO) From).getIntValue()));
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
    }

    public void GoBackToText(){
        writeAssembly("\n\t"+ TWO_STRING, Section, TEXT);
        writeAssembly(STRING_NUM, Align, String.valueOf(4));
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
