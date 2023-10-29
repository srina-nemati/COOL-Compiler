import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String inputCoolFilePath = "";
        String tablePath = "E:\\SBU\\Term6\\Compiler\\Project\\Phase3\\Code\\table.npt";

        MyScanner scanner = new MyScanner(new FileReader(inputCoolFilePath));
        CodeGeneratorImpl codeGenerator = new CodeGeneratorImpl(scanner);
        Parser parser = new Parser(scanner, codeGenerator, tablePath, true);

        try {
            parser.parse();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }
}