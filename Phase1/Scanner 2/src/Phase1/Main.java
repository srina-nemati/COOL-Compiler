package Phase1;

import java.io.*;

public class Main {
    static String inputFile = "E:\\SBU\\Term6\\Compiler\\Project\\Phase1\\Scanner 2\\src\\Phase1\\test\\sample2.txt";
    static String outputFile = "E:\\SBU\\Term6\\Compiler\\Project\\Phase1\\Scanner 2\\src\\Phase1\\test\\sample2_out.html";
    static Scanner.Symbol symbol = null;

    public static String coloring(String currentLineHtml){
        switch (symbol.getType()) {
            case "WhiteSpace" -> currentLineHtml += symbol.getVal();
            case "Undefined" -> currentLineHtml += "<p3 style=\"color:  #FF1818 ;\">" + symbol.getVal() + "</p3>";
            case "String" -> currentLineHtml += "<p3 style=\"color:  #00C897;\">" + symbol.getVal() + "</p3>";
            case "ReservedKeywords" -> currentLineHtml += "<p3 style=\"color:  #B667F1 ;\">" + symbol.getVal() + "</p3>";
            case "Identifiers" -> currentLineHtml += "<p3 style=\"color:  #FFFFFF ;\">" + symbol.getVal() + "</p3>";
            case "RealNumber" -> currentLineHtml += "<em><p3 style=\"color:  #FFC300 ;\">" + symbol.getVal() + "</p3></em>";
            case "IntegerNumber" -> currentLineHtml += "<p3 style=\"color:  #FFC300 ;\">" + symbol.getVal() + "</p3>";
            case "SpecialCharacters" -> currentLineHtml += "<em><p3 style=\"color:  #B8FFF9;\">" + symbol.getVal() + "</p3></em>";
            case "Operators" -> currentLineHtml += "<p3 style=\"color:  #90E0EF ;\">" + symbol.getVal() + "</p3>";
            case "Comment" -> currentLineHtml += "<p3 style=\"color:  #69676C;\">" + symbol.getVal() + "</p3>";
        }
        return currentLineHtml;
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(new FileReader(inputFile));
            File outFile = new File(outputFile);
            FileWriter fileWriter = new FileWriter(outFile);

            int lastLine = -1;
            String currentLineHtml = "";

            fileWriter.write("""
                <html lang="en">
                  <head>
                <style>body {padding: 40px;background-color: #222222}</style>    <meta charset="UTF-8" />
                    <meta/>
                  </head>
                  <body>
                """);
            fileWriter.flush();

            while (true) {
                try {
                    symbol = scanner.nextToken();
                    if (scanner.yyatEOF()) {
                        currentLineHtml += "</pre></div>\n";
                        fileWriter.write(currentLineHtml);
                        fileWriter.flush();
                        break;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                int currLine = symbol.getLineNum();
//                System.out.println(currLine);

                if (currLine > lastLine) {

                    if (lastLine != -1) {
                        fileWriter.write(currentLineHtml);
                        fileWriter.flush();
                    }

                    lastLine = currLine;
                    currentLineHtml = "<div><pre><b><p3 style=\"color:  #4863A0;\">" + (currLine+1) + "</p3></b>";

                }

//                System.out.println(symbol.getType() + ": " + symbol.getVal());
                currentLineHtml = coloring(currentLineHtml);

            }

            fileWriter.write("  </body>\n" +
                    "</html>");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
