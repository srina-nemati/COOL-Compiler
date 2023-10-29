import java.util.Stack;

public class CodeGeneratorImpl implements CodeGenerator{
    public static Stack<Object> semanticStack = new Stack<>();
    public MyScanner scanner;
    public static Descriptor secondOperand, firstOperand;
    public static String afterIfLabel, afterElseLabel;
    private static int labelIndex = 0;
    public static int variableCount = 0;


    CodeGeneratorImpl(MyScanner scanner){
        this.scanner = scanner;
    }

    public static String generateNewLabel() {
        ++labelIndex;
        return "lbl" + labelIndex;
    }

    public static String GenerateVariable(){
        variableCount++;
        return "var" + (variableCount);
    }

    Type StringToType(String type) {
        return switch (type) {
            case "bool" -> Type.Boolean;
            case "int" -> Type.IntegerNumber;
            case "real" -> Type.RealNumber;
            case "string" -> Type.String;
            default -> null;
        };
    }

    @Override
    public void doSemantic(String sem) {
        try{
            switch (sem) {
                case "Add":
                    System.out.println("CodeGen of Add");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Add");
                    break;
                case "Sub":
                    System.out.println("CodeGen of Sub");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Sub");
                    break;
                case "Divide":
                    System.out.println("CodeGen of Divide");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Divide");
                    break;
                case "Multiply":
                    System.out.println("CodeGen of Multiply");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Multiply");
                    break;
                case "OrLogicalExpr":
                case "OrExpr":
                    System.out.println("CodeGen of Or");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"OR");
                    break;
                case "AndLogicalExpr":
                case "AndExpr":
                    System.out.println("CodeGen of And");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"AND");
                    break;
                case "Not":
                    System.out.println("CodeGen of Not");
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,firstOperand,"NOT");
                    break;
                case "SmallerSymbol":
                    System.out.println("CodeGen of Smaller");
                    secondOperand = (Descriptor) semanticStack.pop();
                    Descriptor firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Smaller");
                    break;
                case "GreaterSymbol":
                    System.out.println("CodeGen of Greater");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    System.out.println("Bigger Computation");
                    Computations.Operate(firstOperand,secondOperand,"Greater");
                    break;
                case "SmallerEqSymbol":
                    System.out.println("CodeGen of Smaller Equal");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"SmallerEqual");
                    break;
                case "GreaterEqSymbol":
                    System.out.println("CodeGen of Greater Equal");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"GreaterEqual");
                    break;
                case "EqualSymbol":
                    System.out.println("CodeGen of Equal");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"Equal");
                    break;
                case "NotEqSymbol":
                    System.out.println("CodeGen of Not Equal");
                    secondOperand = (Descriptor) semanticStack.pop();
                    firstOperand = (Descriptor) semanticStack.pop();
                    Computations.Operate(firstOperand,secondOperand,"NotEqual");
                    break;
                case "IntReader":
                    MIPSWriter.appendComment("Read Integer");
                    MIPSWriter.appendCommandToCode("li", "$v0", "5");
                    MIPSWriter.appendCommandToCode("syscall");
                    MIPSWriter.appendCommandToCode("move", "$t0", "$v0");
                    String variableName = GenerateVariable();
                    MIPSWriter.appendCommandToData(variableName, "word", "0");
                    MIPSWriter.appendCommandToCode("la", "$t1", variableName);
                    MIPSWriter.appendCommandToCode("sw", "$t0", "0($t1)");
                    System.out.println("Read Integer : " + variableName);
                    semanticStack.push(new LocalVarDscp(variableName, Type.IntegerNumber));
                    break;
                case "LineReader":
                    MIPSWriter.appendComment("Read String");
                    MIPSWriter.appendCommandToCode("li", "$v0", "8");
                    MIPSWriter.appendCommandToCode("la", "$a0", "strbuffer");
                    MIPSWriter.appendCommandToCode("li", "$a1", "20");
                    MIPSWriter.appendCommandToCode("move", "$t0", "$a0");
                    MIPSWriter.appendCommandToCode("sw", "$t0", "stradr");
                    String varName = GenerateVariable();
                    MIPSWriter.appendCommandToData(varName, "space", "20");
                    MIPSWriter.appendCommandToCode("sw", "$t0", varName);
                    semanticStack.push(new LocalVarDscp(varName, Type.String));
                    System.out.println("Read String : " + varName);
                    MIPSWriter.appendCommandToCode("syscall");
                    break;
                case "PushType":
                    Type type = StringToType(scanner.next().getType());
                    semanticStack.push(type);
                    System.out.println("PushType: " + type);
                    break;
                case "Print":
                    Descriptor var = (Descriptor) semanticStack.pop();
                    String outputType="";
                    String comment ="";
                    if(var.type == Type.RealNumber){
                        outputType = "2";
                        comment = "Real";
                    }
                    if(var.type == Type.IntegerNumber){
                        outputType = "1";
                        comment = "Integer";
                    }
                    if(var.type == Type.String){
                        outputType = "4";
                        comment = "String";
                    }
                    MIPSWriter.appendComment("Print" + " " + comment + " (" + var.name + ")");
                    MIPSWriter.appendCommandToCode("li", "$v0", outputType);
                    MIPSWriter.appendCommandToCode("la", "$t0", var.name);
                    if (var.type == Type.RealNumber) {
                        MIPSWriter.appendCommandToCode("l.s", "$f0", "0($t0)");
                        MIPSWriter.appendCommandToCode("mov.s", "$f12", "$f0");
                    } else {
                        MIPSWriter.appendCommandToCode("lw", "$t0", "0($t0)");
                        MIPSWriter.appendCommandToCode("move", "$a0", "$t0");
                    }
                    MIPSWriter.appendCommandToCode("syscall");
                    if (var.type == Type.RealNumber || var.type == Type.IntegerNumber) {
                        MIPSWriter.appendComment("new line");
                        MIPSWriter.appendCommandToCode("li", "$v0", "4");
                        MIPSWriter.appendCommandToCode("la", "$a0", "nl");
                        MIPSWriter.appendCommandToCode("syscall");
                    }
                    System.out.println("Print : " + var.name);
                    break;
                case "If":
                    Descriptor fooValueDescriptor = (Descriptor) semanticStack.pop();
                    afterIfLabel = generateNewLabel();
                    afterElseLabel = generateNewLabel();
                    MIPSWriter.appendComment("Start If" + fooValueDescriptor);
                    MIPSWriter.appendCommandToCode("la", "$t0", fooValueDescriptor.name);
                    MIPSWriter.appendCommandToCode("lw", "$t1", "0($t0)");
                    MIPSWriter.appendCommandToCode("beqz", "$t1", afterIfLabel);
                    break;
                case "IfSTM":
                    MIPSWriter.appendComment("completeIf");
                    MIPSWriter.appendCommandToCode("j", afterElseLabel);
                    MIPSWriter.addLabel(afterIfLabel);
                    MIPSWriter.addLabel(afterElseLabel);
                    break;
                case "Else":
                    MIPSWriter.appendComment("elseCode");
                    MIPSWriter.deleteLabel(afterIfLabel);
                    MIPSWriter.addLabel(afterIfLabel);
                    break;
                case "ElseSTM":
                    MIPSWriter.appendComment("completeElse");
                    MIPSWriter.deleteLabel(afterElseLabel);
                    MIPSWriter.addLabel(afterElseLabel);
                    break;
                case "DescriptorAdder":
                case "PushDCL":
                case "PushString":
                case "PushDouble":
                case "PushInt":
                case "PushId":
                case "PushIdDcl":
                case "ArrayDcl":
                case "PopPushTypeArray":
                case "AccessArray":
                case "AccessArrayPop":
                case "ArrayASG":
                case "SetArrayDescriptor":
                case "ReturnSTM":
                case "break":
                case "GetLength":
                case "MinusMinusSymbol":
                case "PlusPlusSymbol":
                case "JumpLink":
                case "Cast":
                case "ASG":
                case "AddASG":
                case "SubASG":
                case "DivideASG":
                case "MultiplyASG":
                case "TrueSymbol":
                case "FalseSymbol":

                case "ForStartCondition":
                case "ForJZ":
                case "ForComplete":

                case "WhileComplete":
                case "WhileJZ":
                case "WhileStartCondition":
            }
        } catch (Exception e) {
//            error
        }
    }
}
