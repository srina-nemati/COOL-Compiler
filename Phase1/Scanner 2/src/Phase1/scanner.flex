// user code
package Phase1;

%%

%class Scanner
%public
%unicode
%line
%column
%function nextToken
%type Symbol
%state STRING


%{
    public class Symbol{
                  String type;
                  Object val;
                  int lineNum;
                  public Symbol(String type, Object val, int lineNum){
                          this.type = type;
                          this.val = val;
                          this.lineNum = lineNum;
                  }
                  public String getType() {
                      return type;
                  }
                  public Object getVal() {
                    return val;
                  }
                  public int getLineNum() {
                    return lineNum;
                  }
  }

%}

//////////////////////////////////////////Reserved Keywords////////////////////////////////////////


ReservedKeywords = "let" | "static" | "while" | "new" | "void" | "class" | "break" | "func" | 
                    "int" | "for" | "continue" | "return" | "real" | "len" | "if" | "inputStr" | 
                    "bool" | "loop" | "range" | "inputInt" | "string" | "print" | "else" | "in"


//////////////////////////////////////////Identifier///////////////////////////////////////////////


Digits = [0-9]
Letters = [A-Za-z]
Underscore = "_"
Identifier = ({Letters})({Underscore} | {Digits} | {Letters}){0,30}


//////////////////////////////////////////Numbers//////////////////////////////////////////////////


DecimalInteger = {Digits}+
Hexadecimal = ("0x" | "0X")([A-Fa-f] | {Digits})+
RealNumbers = ({Digits})+ "." ({Digits})*
ScientificNotation = ({DecimalInteger} | {RealNumbers}) ("E" | "e") ("-" | "+") {DecimalInteger}


//////////////////////////////////////////Comment//////////////////////////////////////////////////


InputCharecters = [^\r\n]
LineTerminators = \r|\n|\r\n
SingleLine = "//" {InputCharecters}* {LineTerminators}?
MultipleLine = "/*" [^*] ~"*/" | "/*"~"*/"
Comment = {SingleLine} | {MultipleLine}


//////////////////////////////////////////Operators & Punctuation///////////////////////////////////


Add = "+"
UnaryMinus = "-"
production = "*"
division = "/"
AdditionAssignment = "+="
SubtractionAssignment = "-="
ProductionAssignment = "*="
DivisionAssignment = "/="
Increment = "++"
decrement = "--"
Less = "<"
LessEqual = "<="
Greater = "<"
GreaterEqual= ">="
NotEqual = "!="
Equal = "=="
Assignment= "="
mod = "%"
LogicalAnd = "&&"
LogicalOr = "||"
BitwiseAnd = "&"
BitwiseOr = "|"
BitwiseXor = "^"
StringLiteral = "\""
Not = "!"
Dot = "."
Colon = ","
Semicolon = ";"
OpeningBraces = "["
ClosingBraces = "]"
OpeningParenthesis = "("
ClosingParenthesis = ")"
OpeningCurlyBraces = "{"
ClosingCurlyBraces = "}"

Operators = {Add} | {UnaryMinus} | {production} | {division} | {AdditionAssignment} |
            {SubtractionAssignment} | {ProductionAssignment} | {DivisionAssignment} |
            {Increment} | {decrement} | {Less} | {LessEqual} | {Greater} | {GreaterEqual} |
            {NotEqual} | {Equal} | {Assignment} | {mod} | {LogicalAnd} | {LogicalOr} | {BitwiseAnd} |
            {BitwiseOr} | {StringLiteral} | {BitwiseXor} | {Not} | {Dot} | {Colon} | {Semicolon} |
            {OpeningBraces} | {ClosingBraces} | {OpeningParenthesis} | {ClosingParenthesis} |
            {OpeningCurlyBraces} | {ClosingCurlyBraces}


//////////////////////////////////////////Special Charecters////////////////////////////////////////


WhiteSpace = \r|\n|\r\n|" "|\f|\t
SpecialCharacters = "\\n"|"\\t"|"\\r"|"\\\""|"\\\'"|"\\\\"
NormalString = [^\r\n\t\"\'\\]+


%%

<YYINITIAL> {
    {StringLiteral} {
        yybegin(STRING);
        return new Symbol("String", yytext(), yyline);
    }
    {ReservedKeywords} {
        return new Symbol("ReservedKeywords",yytext() , yyline);
    }
    {Identifier} {
        return new Symbol("Identifiers",yytext() , yyline);
    }
    {Comment} {
        return new Symbol("Comment",yytext() , yyline);
    }
    {ScientificNotation} {
        return new Symbol("IntegerNumber",yytext(), yyline);
    }
    {Hexadecimal} {
        return new Symbol("IntegerNumber",yytext(), yyline);
    }
    {DecimalInteger} {
        return new Symbol("IntegerNumber",yytext(), yyline);
    }
    {RealNumbers} {
        return new Symbol("RealNumber", yytext(), yyline);
    }
    {Operators} {
        return new Symbol("Operators",yytext(), yyline);
    }
    {WhiteSpace} {
        return new Symbol("WhiteSpace",yytext(), yyline);
    }
   [^] {
        System.out.println("Error at line: "+yyline + "index: "+ yycolumn + "character = "+ yytext());
        return new Symbol("Undefined", yytext() , yyline) ;
    }
}

<STRING> {
     {StringLiteral}  {
         yybegin(YYINITIAL);
         return new Symbol("String",yytext(), yyline);
     }
    {NormalString} {
        return new Symbol("String",yytext(), yyline);
    }
    {SpecialCharacters} {
        return new Symbol("SpecialCharacters",yytext(), yyline);
    }
    {WhiteSpace} {
        return new Symbol("WhiteSpace",yytext(), yyline);
    }
    [^] {
        System.out.println("Error at line: "+yyline + "index: "+ yycolumn + "character = "+ yytext());
        return new Symbol("Undefined", yytext() , yyline) ;
    }
}

[^] {
     System.out.println("Error at line: "+yyline + "; index: "+ yycolumn + "; character = "+ yytext());
     return new Symbol("Undefined", yytext() , yyline) ;
    }