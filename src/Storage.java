import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage {
  private static int whileCounter=0;
  private static int currentWhile=0;
  private static HashMap<String, Integer> variableDb = new HashMap<String, Integer>();
  private static List<Loop> whileList = new ArrayList<>();

  public static void clearVar(String var){
    variableDb.put(var,0);
  }
  public static void incrVar(String var){
    variableDb.put(var,variableDb.get(var)+1);
  }
  public static void decrVar(String var){
    variableDb.put(var,variableDb.get(var)-1);
  }
  public static int getVarVal(String var) {
    int value;
    value = variableDb.get(var);
    return value;
  }
  public static int getCrtWhile(){
    return currentWhile;
  }
  public static void incrCrtWhile(){
    currentWhile++;
  }
  public static void incrWhileCounter(){
    whileCounter++;
  }
  public static int getWhileCounter(){
    return whileCounter;
  }
  public static void decrCrtWhile(){
    currentWhile--;
  }
  public static Loop getLoop(int index){
    return whileList.get(index);
  }
  public static void backOneLoop(){
    currentWhile=whileList.get(currentWhile).parentLoop;
  }

  public static void addInstruction(Instruction newInstr){
    Loop initLoop;
    if(newInstr.type==3) {
      initLoop = new Loop();
      initLoop.parentLoop = currentWhile;
      initLoop.targetValue = newInstr.targetValue;
      initLoop.targetVar = newInstr.targetVar;
      whileList.add(initLoop);
      whileCounter++;
      whileList.get(currentWhile).opList.add(newInstr);
      whileList.get(currentWhile).instructionCount++;
      currentWhile=whileCounter;

    }
    else{
      initLoop = whileList.get(currentWhile);
      initLoop.opList.add(newInstr);
      initLoop.instructionCount++;
    }
  }

  public static void initDb(){
    Loop initLoop = new Loop();
    initLoop.targetValue = -1;
    initLoop.targetVar = "defaultVar";
    clearVar("defaultVar");
    whileList.add(initLoop);

  }

  public static void endProgram(){
    Instruction endInstr = new Instruction();
    Loop defLoop = whileList.get(0);
    endInstr.type = 4;
    defLoop.opList.add(endInstr);
    defLoop.instructionCount++;
  }

  public static void listInstructionSet(){
    for(int i = 0; i<=whileCounter; i++){
      System.out.println("This is while "+i);
      for(int j = 0; j<whileList.get(i).instructionCount; j++){
        System.out.println("--- Operation code "+whileList.get(i).opList.get(j).type);
      }
    }
  }

  public static void listVarSet(){
    variableDb.forEach((k,v) -> {
      System.out.println(k+" has value "+v.toString());
    });
  }

}
