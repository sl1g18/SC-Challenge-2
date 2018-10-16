import java.util.ArrayList;
import java.util.List;

public class Loop {
  String targetVar;
  int targetValue;
  int parentLoop;
  int instructionCount;
  int currInstruction;
  List<Instruction> opList = new ArrayList<>();
  Loop(){
    targetVar="";
    targetValue=0;
    currInstruction=0;
    instructionCount=0;
  }
}
