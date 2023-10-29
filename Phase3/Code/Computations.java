public class Computations {
    public static void Operate(Descriptor firstOperand ,Descriptor  secondOperand,String operation) {
        try {
            Type resultType = firstOperand.type;
            String storeCommand = "sw";
            String loadCommand = "lw";
            String variableName0 = "$f0";
            String variableName1 = "$f1";
            String extention = null;

            switch (resultType) {
                case IntegerNumber -> {
                    extention = "";
                    storeCommand = "sw";
                    loadCommand = "lw";
                    variableName0 = "$t0";
                    variableName1 = "$t1";
                }
                case RealNumber -> {
                    extention = ".s";
                    storeCommand = "s.s";
                    loadCommand = "l.s";
                    variableName0 = "$f0";
                    variableName1 = "$f1";
                }
                default -> resultType = null;
            }

            switch (operation) {
                case "Add":
                    if(firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "add" + extention, storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "Sub":
                    if(firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "sub" + extention, storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "Divide":
                    if(firstOperand.type == secondOperand.type)
                        divide(firstOperand, secondOperand, resultType, "div" + extention, storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "Multiply":
                    if(firstOperand.type == secondOperand.type)
                        multiply(firstOperand, secondOperand, resultType, "mul" + extention, resultType == Type.IntegerNumber ? "sd" : "s.s", loadCommand, variableName0, variableName1);
                    break;
                case "AND":
                    if(firstOperand.type == Type.IntegerNumber)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "and", "sw", "lw", variableName0, variableName1);
                    break;
                case "OR":
                    if(firstOperand.type == Type.IntegerNumber)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "or", "sw", "lw", variableName0, variableName1);
                    break;
                case "NOT":
                    if(firstOperand.type == Type.IntegerNumber)
                        generateNotCommand(firstOperand, resultType, "not");
                    break;
                case "Greater":
                    if (firstOperand.type == Type.RealNumber && firstOperand.type == secondOperand.type) {
                        generate2OperandCommands(secondOperand, firstOperand, resultType, "c.lt.s", storeCommand, loadCommand, variableName0, variableName1);
                    } else if (firstOperand.type == Type.IntegerNumber && firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "sgt", storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "Smaller":
                    if (firstOperand.type == Type.RealNumber && firstOperand.type == secondOperand.type) {
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "c.lt.s", storeCommand, loadCommand, variableName0, variableName1);
                    } else if (firstOperand.type == Type.IntegerNumber && firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "slt", storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "GreaterEqual":
                    if (firstOperand.type == Type.RealNumber && firstOperand.type == secondOperand.type) {
                        generate2OperandCommands(secondOperand, firstOperand, resultType, "c.le.s", storeCommand, loadCommand, variableName0, variableName1);
                    } else if (firstOperand.type == Type.IntegerNumber && firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "sge", storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "SmallerEqual":
                    if (firstOperand.type == Type.RealNumber && firstOperand.type == secondOperand.type) {
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "c.le.s", storeCommand, loadCommand, variableName0, variableName1);
                    } else if (firstOperand.type == Type.IntegerNumber && firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "sle", storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "Equal":
                    if (firstOperand.type == Type.RealNumber && firstOperand.type == secondOperand.type) {
                        String temp = CodeGeneratorImpl.GenerateVariable();
                        String continueLabel = CodeGeneratorImpl.generateNewLabel();
                        MIPSWriter.appendComment("Real Compare");
                        MIPSWriter.appendCommandToCode("la", "$t0", firstOperand.name);
                        MIPSWriter.appendCommandToCode("la", "$t1", secondOperand.name);
                        MIPSWriter.appendCommandToCode("l.s", "$f0", "0($t0)");
                        MIPSWriter.appendCommandToCode("l.s", "$f1", "0($t1)");
                        MIPSWriter.appendCommandToCode("c.eq.s", "$f0", "$f1");
                        MIPSWriter.appendCommandToCode("bc1t", continueLabel);
                        MIPSWriter.appendCommandToCode("li", "$t0", "0");
                        MIPSWriter.addLabel(continueLabel);
                        MIPSWriter.appendCommandToCode("sw", "$t0", temp);
                        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(temp, Type.IntegerNumber));
                    } else if(firstOperand.type == secondOperand.type)
                        generate2OperandCommands(firstOperand, secondOperand, resultType, "seq", storeCommand, loadCommand, variableName0, variableName1);
                    break;
                case "NotEqual":
                    if(firstOperand.type==secondOperand.type) {
                        if(firstOperand.type == Type.RealNumber){
                            String temp = CodeGeneratorImpl.GenerateVariable();
                            String continueLabel = CodeGeneratorImpl.generateNewLabel();
                            MIPSWriter.appendComment("Real Compare");
                            MIPSWriter.appendCommandToCode("la", "$t0", firstOperand.name);
                            MIPSWriter.appendCommandToCode("la", "$t1", secondOperand.name);
                            MIPSWriter.appendCommandToCode("l.s", "$f0", "0($t0)");
                            MIPSWriter.appendCommandToCode("l.s", "$f1", "0($t1)");
                            MIPSWriter.appendCommandToCode("c.eq.s", "$f0", "$f1");
                            MIPSWriter.appendCommandToCode("bc1f", continueLabel);
                            MIPSWriter.appendCommandToCode("li", "$t0", "0");
                            MIPSWriter.addLabel(continueLabel);
                            MIPSWriter.appendCommandToCode("sw", "$t0", temp);
                        }else {
                            generate2OperandCommands(firstOperand, secondOperand, resultType, "sne", "sw", "lw", variableName0, variableName1);
                        }
                    }
                    break;
                case "PlusPlus":
                    if (firstOperand.type == Type.IntegerNumber)
                        generatePlusPlusAfterCommand(firstOperand, resultType, "addi");
                    break;
                case "MinusMinus":
                    if (firstOperand.type == Type.IntegerNumber)
                        generateMinusMinusAfterCommand(firstOperand, resultType, "addi");
                    break;
                default:
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static String loadAndOperate(Descriptor firstOperandDes, Descriptor secondOperandDes, String operationCommand, String storeCommand, String loadCommand, String variableName0, String variableName1) {
        String variableName = CodeGeneratorImpl.GenerateVariable();
        MIPSWriter.appendComment("Command " + operationCommand + firstOperandDes.name + ", " + secondOperandDes.name );
        MIPSWriter.appendCommandToCode("la", "$t0", firstOperandDes.name);
        MIPSWriter.appendCommandToCode("la", "$t1", secondOperandDes.name);
        MIPSWriter.appendCommandToCode(loadCommand, variableName0, "0($t0)");
        MIPSWriter.appendCommandToCode(loadCommand, variableName1, "0($t1)");
        MIPSWriter.appendCommandToCode(operationCommand, variableName0, variableName0, variableName1);
        return variableName;
    }

    private static void generateNotCommand(Descriptor firstOperandDes, Type resultType, String operationCommand) {
        String variableName = CodeGeneratorImpl.GenerateVariable();
        MIPSWriter.appendComment("binary " + operationCommand + " expression of " + firstOperandDes.name);
        MIPSWriter.appendCommandToCode("la", "$t0", firstOperandDes.name);
        MIPSWriter.appendCommandToCode("lw", "$t0", "0($t0)");
        MIPSWriter.appendCommandToCode(operationCommand, "$t0", "$t0");
        MIPSWriter.appendCommandToData(variableName, "word", "0");
        MIPSWriter.appendCommandToCode("sw", "$t0", variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }

    private static void generate2OperandCommands(Descriptor firstOperandDes, Descriptor secondOperandDes, Type resultType, String operationCommand, String storeCommand, String loadCommand, String variableName0, String variableName1) {
        String variableName = loadAndOperate(firstOperandDes, secondOperandDes, operationCommand, storeCommand, loadCommand, variableName0, variableName1);
        MIPSWriter.appendCommandToData(variableName, "word", "0");
        MIPSWriter.appendCommandToCode(storeCommand, variableName0, variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }

    private static void multiply(Descriptor firstOperandDes, Descriptor secondOperandDes, Type resultType, String operationCommand, String storeCommand, String loadCommand, String variableName0, String variableName1) {
        String variableName = loadAndOperate(firstOperandDes, secondOperandDes, operationCommand, storeCommand, loadCommand, variableName0, variableName1);
        MIPSWriter.appendCommandToCode("mfhi", "$t1");
        MIPSWriter.appendCommandToCode("mflo", "$t0");
        MIPSWriter.appendCommandToData(variableName, "space", "64");
        MIPSWriter.appendCommandToCode(storeCommand, variableName0, variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }

    private static void divide(Descriptor firstOperandDes, Descriptor secondOperandDes, Type resultType, String operationCommand, String storeCommand, String loadCommand, String variableName0, String variableName1) {

        String variableName = loadAndOperate(firstOperandDes, secondOperandDes, operationCommand, storeCommand, loadCommand, variableName0, variableName1);
        MIPSWriter.appendCommandToCode("mfhi", "$t1");
        MIPSWriter.appendCommandToCode("mflo", "$t0");
        MIPSWriter.appendCommandToData(variableName, "word", "0");
        MIPSWriter.appendCommandToCode(storeCommand, variableName0, variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }

    private static void generateMinusMinusAfterCommand(Descriptor firstOperandDes, Type resultType, String operationCommand) {
        String variableName = CodeGeneratorImpl.GenerateVariable();
        MIPSWriter.appendComment("MinusMinusAfter" + firstOperandDes.name);
        MIPSWriter.appendCommandToCode("la", "$t0", firstOperandDes.name);
        MIPSWriter.appendCommandToCode("lw", "$t0", "0($t0)");
        MIPSWriter.appendCommandToCode(operationCommand, "$t0", "$t0", "-1");
        MIPSWriter.appendCommandToData(variableName, "word", "0");
        MIPSWriter.appendCommandToCode("sw", "$t0", variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }

    private static void generatePlusPlusAfterCommand(Descriptor firstOperandDes, Type resultType, String operationCommand) {
        String variableName = CodeGeneratorImpl.GenerateVariable();
        MIPSWriter.appendComment("PlusPlusAfter" + firstOperandDes.name);
        MIPSWriter.appendCommandToCode("la", "$t0", firstOperandDes.name);
        MIPSWriter.appendCommandToCode("lw", "$t0", "0($t0)");

        MIPSWriter.appendCommandToCode(operationCommand, "$t0", "$t0", "0x1");
        MIPSWriter.appendCommandToData(variableName, "word", "0");
        MIPSWriter.appendCommandToCode("sw", "$t0", variableName);
        CodeGeneratorImpl.semanticStack.push(new LocalVarDscp(variableName, resultType));
    }
}
