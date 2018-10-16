import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {
  public void startCompiler (String fileName) {
    readFile(fileName);
    Storage.initDb();
    executeWhile(Storage.getLoop(0));
    Storage.listVarSet();
  }

  public void readFile (String fileName) {
    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      Storage.initDb();
      scanner.useDelimiter(";");
      while (scanner.hasNext()) {
          processLine(scanner.next());
      }
      scanner.close();
    }catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Storage.endProgram();
  }

  public void processLine(String line) {
    String[] commands = {
      "(while) (\\w) (not) (\\w)",
      "(clear) (\\w)",
      "(incr) (\\w)",
      "(decr) (\\w)",
      "(end)"
    };
    String command,varName;

    for(int i=0; i<=4; i++) {
      Instruction newInstr = new Instruction();
      Pattern pattern = Pattern.compile(commands[i]);
      Matcher matcher = pattern.matcher(line);
      if(matcher.find()) {
        command = matcher.group(1);
        if (command.equals("end")) {
          newInstr.type=4;
          Storage.addInstruction(newInstr);
          Storage.backOneLoop();
        }
        else if (command.equals("while")){
          newInstr.type=3;
          newInstr.targetVar=matcher.group(2);
          newInstr.targetValue=Integer.parseInt(matcher.group(4));
          newInstr.loopNumber=Storage.getWhileCounter()+1;
          Storage.addInstruction(newInstr);
        }
        else if (command.equals("clear")){
          newInstr.type=0;
          newInstr.targetVar=matcher.group(2);
          Storage.addInstruction(newInstr);
        }
        else if (command.equals("incr")){
          newInstr.type=1;
          newInstr.targetVar=matcher.group(2);
          Storage.addInstruction(newInstr);
        }
        else if (command.equals("decr")) {
          newInstr.type = 2;
          newInstr.targetVar = matcher.group(2);
          Storage.addInstruction(newInstr);
        }
      }
    }
  }

  private void executeWhile(Loop loop){
    List instructionList = loop.opList;
    int exitCode = 0;
    loop.currInstruction=0;
    while(exitCode!=1){
      for(int i = loop.currInstruction; i<loop.instructionCount;i++){
        Instruction op = (Instruction)instructionList.get(i);
        //System.out.println("I'm running op number "+i+" with type "+op.type+" in while with target var "+loop.targetVar);
        if(op.type==0){
          Storage.clearVar(op.targetVar);
        }
        else if(op.type==1){
          Storage.incrVar(op.targetVar);
        }
        else if(op.type==2){
          Storage.decrVar(op.targetVar);
        }
        else if(op.type==3){
          executeWhile(Storage.getLoop(op.loopNumber));
        }
        else if(op.type==4){
          if(Storage.getVarVal(loop.targetVar)==loop.targetValue||loop.targetVar=="defaultVar")exitCode = 1;
          else {loop.currInstruction=0;}
        }
      }
    }
  }
}

