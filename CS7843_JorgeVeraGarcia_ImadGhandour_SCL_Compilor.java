import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CS7843_JorgeVeraGarcia_ImadGhandour_SCL_Compilor {

    // Variables that support the implementations of the lexical and syntax analyzer within this Java source code file
    static String par = "";
    static String fileName;
    static int lineCount = 1;
    static int lineCountnextToken = 1;
    static Scanner parag1Lex = new Scanner(par);
    static Scanner parag2Lex = new Scanner(par);
    static Scanner parag1Token = new Scanner(par);
    static Scanner parag2Token = new Scanner(par);
    static Scanner translate;
    static String nextToken = "";
    static String lexeme = "";
    static boolean error = false;
    static boolean termFlag = false;
    static int identifierCounter = -1;
    static double op1 = 0;
    static double op2 = 0;
    static double op3 = 0;
    static double op4 = 0;
    static int indexOfExpressionTable = 0;
    static boolean error2 = false;
    static boolean errorException = false;

    // Symbols table containing keywords and operators
    static String[][] keywordTable = {
            {"k_return", "k_import", "k_double", "k_of", "k_is", "k_type", "k_variables", "k_imp", "k_function", "k_main", "k_integer", "k_begin", "k_endfun", "k_define", "k_set", "k_display", "k_description", "k_*/", "k_exit", "MulOrDivOp", "MulOrDivOp", "addOrSubOp", "addOrSubOp", "leftParenthesis", "rightParenthesis", "assignmentOp"},
            {"return", "import", "double", "of", "is", "type", "variables", "implementations", "function", "main", "integer", "begin", "endfun", "define", "set", "display", "description", "*/", "exit", "*", "/", "+", "-", "(", ")", "="},
    };

    // Stores all the declared identifiers, limited to 128 identifiers since static array is being utilized
    static String[] identifierTable = new String[128];
    static String[] expressionEval = new String[10];

    // Driver method
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner userInput = new Scanner(System.in);



        do {
            try {
                System.out.print("Enter file name: ");
                fileName = userInput.nextLine(); // input file stored in fileName
                translate = new Scanner(new File(fileName));
                errorException = false;
            } catch (Exception e1) {
                System.out.println("File Does Not Exist, Please Try Again...");
                errorException = true;
            }
        } while (errorException);
        Scanner inputLexeme = new Scanner(new File(fileName));
        while (inputLexeme.hasNextLine()) { // loop adds a delimiter to the parsing file
            par = par + inputLexeme.nextLine() + "\n ; ";
        }

        parag1Lex = new Scanner(par);
        parag2Lex = new Scanner(par);
        parag1Token = new Scanner(par);
        parag2Token = new Scanner(par);
        Parser();
        System.out.println("Parser Completed Task"); // lets the user executing the parser know that the parser has ended execution
        if (error2 == false) {
            transSCL();
        } else if (error2 == true) {
            System.out.println("---Errors in SCL Source File, Please Review Output to Find and Fix and then Re-Run---");
            System.out.println("----------------------------------Nothing to Run-------------------------------------");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("Terminated at: " + dtf.format(now) + "\n");
        }

    }

    // the following methods are supposed to mimic the extension programs following the grammar description attached to the archive with our solution
    static void Parser() {
        program();
    }

    // method program() implements the derivation of <program> in the BNF description of the SCL language
    static void program() {
        nextToken = nextToken();
        System.out.println("Enter <program>");
        if (nextToken.equals("k_imp")) {
            implementations();
            System.out.println("Exit <Program>");
        } else {
            errorThrown(nextToken);
            implementations();
            System.out.println("Exit <program>");
        }
    }

    // method implementations() implements the derivation of <implementations> in the BNF description of the SCL language
    static void implementations() {
        System.out.println("Enter <implementations>");
        lexeme = lex();
        nextToken = nextToken();
        currentLexmePrnt(lexeme);
        if (nextToken.equals("k_function")) { // if-statement test whether the nextToken is keyword function
            function();
            System.out.println("Exit <implementations>");
        } else {
            errorThrown(nextToken);
            function();
            System.out.println("Exit <implementations>");
        }
    }

    // method function() implements the derivation of <function> in the BNF description of the SCL language
    static void function() {
        lexeme = lex();
        nextToken = nextToken();
        currentLexmePrnt(lexeme);

        /**
         * functionName() call
         */
        if (nextToken.equals("k_main") || nextToken.equals("identifier")) {
            functionName();
        } else {
            errorThrown(nextToken);
            function();
        }

        /**
         * keyword return
         */
        nextToken = nextToken();
        if (nextToken.equals("k_return")) {
            lexeme = lex();
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            lexeme = lex();
            currentLexmePrnt(lexeme);
        }

        /**
         * keyword type
         **/
        nextToken = nextToken();
        if (nextToken.equals("k_type")) {
            lexeme = lex();
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            lexeme = lex();
            currentLexmePrnt(lexeme);
        }

        /**
         * type() call
         */
        nextToken = nextToken();
        if (nextToken.equals("k_double") || nextToken.equals("k_integer")) {
            type();
        } else {
            errorThrown(nextToken);
            type();
        }

        /**
         * keyword is
         */
        nextToken = nextToken();
        if (nextToken.equals("k_is")) {
            lexeme = lex();
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            lexeme = lex();
            currentLexmePrnt(lexeme);
        }
        nextToken = nextToken();

        /**
         * variables call
         */
        if (nextToken.equals("k_variables")) {
            variables();
            error = false;
        } else {
            errorThrown(nextToken);
            variables();
        }

        if (nextToken.equals("k_endfun")) {
            nextToken = nextToken();
            lexeme = lex();
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            nextToken = nextToken();
            lexeme = lex();
            currentLexmePrnt(lexeme);
        }

        if (nextToken.equals("k_main") || nextToken.equals("identifier")) {
            functionName();
        } else {
            errorThrown(nextToken);
            function();
        }

        System.out.println("Exit <function>");
    }

    // method functionName() implements the derivation of <functionName> in the BNF description of the SCL language
    static void functionName() {
        System.out.println("Enter <function_name>");
        lexeme = lex();
        currentLexmePrnt(lexeme);
        System.out.println("Exit <function_name>");
    }

    // method type() implements the derivation of <type> in the BNF description of the SCL language
    static void type() {
        System.out.println("Enter <type>");
        lexeme = lex();
        currentLexmePrnt(lexeme);
        System.out.println("Exit <type>");
    }

    // method variables() implements the derivation of <variables> in the BNF description of the SCL language
    static void variables() {
        System.out.println("Enter <variables>");
        lexeme = lex();
        nextToken = nextToken();
        if (nextToken.equals("k_define")) {
            currentLexmePrnt(lexeme);
            variable_dec();
        } else {
            errorThrown(nextToken);
            currentLexmePrnt(lexeme);
            variable_dec();
        }

        System.out.println("Exit <variables>");
    }

    // method variable_dec() implements the derivation of <varaible_dec> in the BNF description of the SCL language
    static void variable_dec() { // responsible for grammatical specification
        System.out.println("Enter <variable_dec>");
        if (error == false) {
            lexeme = lex();
            nextToken = nextToken();
        }
        error = false;
        if (nextToken.equals("identifier")) {
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            currentLexmePrnt(lexeme);
        }

        lexeme = lex();
        nextToken = nextToken();
        identifierCounter++;
        identifierTable[identifierCounter] = lexeme; // stores the identifier in the identifier table
        if (nextToken.equals("k_of")) {
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            currentLexmePrnt(lexeme);
        }

        lexeme = lex();
        nextToken = nextToken();
        if (nextToken.equals("k_type")) {
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            currentLexmePrnt(lexeme);
        }

        lexeme = lex();
        nextToken = nextToken();
        if (nextToken.equals("k_double") || nextToken.equals("k_integer")) {
            currentLexmePrnt(lexeme);
            type();
        } else {
            currentLexmePrnt(lexeme);
            errorThrown(nextToken);
            type();
        }

        nextToken = nextToken();
        if (nextToken.equals("k_begin")) { // this is where this method gets interesting. variable_dec() needs to check if the next token is either begin, define, or identifier and move accordingly
            begin();
        } else if (nextToken.equals("k_define")) {
            variable_dec();
        } else {
            errorThrown(nextToken);
            lexeme = lex();
            currentLexmePrnt(lexeme);
            error = true; // error variable gets updated in order to ensure
            nextToken = nextToken();
            if (nextToken.equals("identifier")) {
                variable_dec();
            } else if (nextToken.equals("k_display") || nextToken.equals("k_set")) {
                begin();
            }
        }

        System.out.println("Exit <variable_dec>");
    }

    // method begin() implements the derivation of <begin> in the BNF description of the SCL language
    static void begin() { // this method provides the structure of the code
        System.out.println("Enter <begin>");
        if (error == true) { // checks the variable error to either move on with the exuction or adjust the lexeme and nextToken variable
            statement();
        } else {
            lexeme = lex();
            currentLexmePrnt(lexeme);
            nextToken = nextToken();
            statement();
        }

        lexeme = lex();
        nextToken = nextToken();

        currentLexmePrnt(lexeme);
        System.out.println("Exit <begin>");
    }

    // method function() implements the derivation of <function> in the BNF description of the SCL language
    static void statement() {
        System.out.println("Enter <statement>");

        if (nextToken.equals("k_display")) {
            display();
        } else if (nextToken.equals("k_set")) {
            assignment();
        } else { // in the case that the nextToken is not display or set, the program will throw and irrecoverable error and continue executing
            errorThrown(nextToken);
            currentLexmePrnt(lexeme);
            error = true;
            System.out.println("Irrecoverable Error, Unknown if display or assignment, no distinction");
        }

        if (nextToken.equals("k_display")) {
            statement();
        } else if (nextToken.equals("k_set")) {
            statement();
        } else if (nextToken.equals("k_exit")) {

        } else { // in the case that the nextToken is not display or set or exit, the program will throw and irrecoverable error and continue executing
            errorThrown(nextToken);
            lexeme = lex();
            currentLexmePrnt(lexeme);
            nextToken = nextToken();
            System.out.println("Irrecoverable Error, unknown if display or assignment, no distinction");
        }

        System.out.println("Exit <statement>");
    }

    static void display() { // display method is called if the token stored is k_display
        System.out.println("Enter <display>");
        lexeme = lex();
        nextToken = nextToken();

        if (nextToken.equals("string_literal")) {
            currentLexmePrnt(lexeme);
            stringLiteral();
        } else {
            currentLexmePrnt(lexeme);
            field();
        }

        System.out.println("Exit <display>");
    }

    static void field() { // this method retrieves the current lexeme and checks to see if it adheres to the BNF specification
        boolean exists = false;
        System.out.println("Enter <field>");
        lexeme = lex();
        for (int i = 0; i < identifierTable.length; i++) {
            if (lexeme.equals(identifierTable[i])) {
                exists = true;
            }
        }
        currentLexmePrnt(lexeme);
        if (exists == false) { // declaration error being thrown. Exists variable has to be set to false boolean value
            System.out.println("ERROR: Please note that the above variable \"" + lexeme + "\" has NOT been declared");
        }
        nextToken = nextToken();
        System.out.println("Exit <field>");
    }

    static void stringLiteral() { // method processes string literals encountered in code. Essentially passes them to the currentLexmePrnt() method to be printed
        System.out.println("Enter <string_literal>");
        lexeme = lex();
        currentLexmePrnt(lexeme);
        nextToken = nextToken();
        System.out.println("Exit <string_literal>");
    }

    static void assignment() {
        System.out.println("Enter <assignment>");
        lexeme = lex();
        nextToken = nextToken();
        currentLexmePrnt(lexeme);
        lexeme = lex();

        if (nextToken.equals("identifier")) { // the first check in assignment is to see if the nextToken stores identifier
            boolean exists = false;
            nextToken = nextToken();
            for (int i = 0; i < identifierTable.length; i++) {
                if (lexeme.equals(identifierTable[i])) {
                    exists = true;
                }
            }
            currentLexmePrnt(lexeme);
            if (exists == false) { // irrecoverable error stemming from a wrongful variable declaration
                System.out.println("ERROR: Please note that the above variable \"" + lexeme + "\" has NOT been declared");
            }
        } else {
            errorThrown(nextToken());
            nextToken = nextToken();
            currentLexmePrnt(lexeme);
        }

        lexeme = lex();
        if (nextToken.equals("assignmentOp")) { // checks if current token is part of an assignment statement
            nextToken = nextToken();
            currentLexmePrnt(lexeme);
        } else {
            errorThrown(nextToken);
            nextToken = nextToken();
            currentLexmePrnt(lexeme);
        }

        lexeme = lex();
        if (nextToken.equals("Numeric Literal")) { // checks if current token is a numeric literal, calls expr() if it is
            expr();
            if (termFlag == true) {
                System.out.println("The Evaluation of the Mathematical Expression is Equal to \"" + op3 + "\"");
            } else {
                System.out.println("The Evaluation of the Mathematical Expression is Equal to \"" + op4 + "\"");
            }
        } else {
            errorThrown(nextToken);
            nextToken = nextToken();
            currentLexmePrnt(lexeme);
            expr();
            if (termFlag == true) {
                System.out.println("The Evaluation of the Mathematical Expression is Equal to \"" + op3 + "\"");
            } else {
                System.out.println("The Evaluation of the Mathematical Expression is Equal to \"" + op4 + "\"");
            }
        }
        System.out.println("Exit <assignment>");
    }

    static void expr() { // this method constructs a arithmetic epxression that contains either an addition or a subtraction
        System.out.println("Enter <expr>");


        if (termFlag == false) {
            nextToken = nextToken();
            currentLexmePrnt(lexeme);
            expressionEval[indexOfExpressionTable] = lexeme; //Numeric Literal Op1
            indexOfExpressionTable++;
        }

        if (nextToken.equals("addOrSubOp")) {
            lexeme = lex();
            currentLexmePrnt(lexeme);
            nextToken = nextToken();
            if (nextToken.equals("Numeric Literal")) {
                expressionEval[indexOfExpressionTable] = lexeme; //Operation (+ or -)
                indexOfExpressionTable++;
                lexeme = lex();
                expressionEval[indexOfExpressionTable] = lexeme; //Numeric Literal 2
                indexOfExpressionTable++;
                currentLexmePrnt(lexeme);
                nextToken = nextToken();
                if (termFlag == false) {
                    op2 = Double.valueOf(expressionEval[2]);
                    op1 = Double.valueOf(expressionEval[0]);
                    if (expressionEval[1].equals("+")) {
                        op4 = op1 + op2;
                    } else {
                        op4 = op1 - op2;
                    }
                } else {
                    op2 = Double.valueOf(expressionEval[4]);
                    if (expressionEval[3].equals("+")) {
                        op3 = op4 + op2;
                    } else {
                        op3 = op4 - op2;
                    }
                }
                if (nextToken.equals("addOrSubOp")) { // this if statement ensures that only one addition operation is being done per expression
                    System.out.println("You can only add or subtract once");
                    errorThrown(nextToken);
                    nextToken = nextToken();
                    lexeme = lex();
                    currentLexmePrnt(lexeme);
                    op2 = Double.valueOf(expressionEval[2]);
                    op1 = Double.valueOf(expressionEval[0]);
                    if (expressionEval[1].equals("+")) {
                        op4 = op1 + op2;
                    } else {
                        op4 = op1 - op2;
                    }
                    while (!(nextToken.equals("k_display") || nextToken.equals("k_set"))) { // loop that continues updating variables until a display or set keyword comes up
                        errorThrown(nextToken);
                        lexeme = lex();
                        currentLexmePrnt(lexeme);
                        nextToken = nextToken();
                    }
                } else if (nextToken.equals("MulOrDivOp")) { // if the nextToken happens to be MulOrDivOp because the expression contained the lexemes "*" or "/", then it will call the term method
                    term();
                    op3 = Double.valueOf(expressionEval[4]);
                    op2 = Double.valueOf(expressionEval[2]);
                    op1 = Double.valueOf(expressionEval[0]);
                    if (expressionEval[3].equals("*")) {
                        op3 = op2 * op3;
                        if (expressionEval[1].equals("+")) {
                            op3 = op1 + op3;
                        } else {
                            op3 = op1 - op3;
                        }
                    } else {
                        op3 = op2 / op3;
                        if (expressionEval[1].equals("+")) {
                            op3 = op1 + op3;
                        } else {
                            op3 = op1 - op3;
                        }
                    }

                    termFlag = true;
                }
            }
        } else if (nextToken.equals("MulOrDivOp")) { // if the nextToken happens to be MulOrDivOp because the expression contained the lexemes "*" or "/", then it will call the term method
            term();
            op1 = Double.valueOf(expressionEval[0]);
            op2 = Double.valueOf(expressionEval[2]);
            if (expressionEval[1].equals("*")) {
                op4 = op1 * op2;
            } else {
                op4 = op1 / op2;
            }
        } else {
            while (!(nextToken.equals("k_display") || nextToken.equals("k_set"))) { // loop that continues updating variables until a display or set keyword comes up
                errorThrown(nextToken);
                lexeme = lex();
                currentLexmePrnt(lexeme);
                nextToken = nextToken();
            }
        }

        System.out.println("Exit <expr>");
    }

    static void term() {
        System.out.println("Enter <term>");
        lexeme = lex();
        expressionEval[indexOfExpressionTable] = lexeme; //term operation (* or /)
        indexOfExpressionTable++;
        nextToken = nextToken();
        if (nextToken.equals("Numeric Literal") || nextToken.equals("identifier")) { // if the nextToken stores either a numeric literal or an identifier, it will call the factor method
            factor();
        }
        System.out.println("Exit <term>");
    }

    static void factor() { // factor method works just like expr() method, but for multiplication or division
        System.out.println("Enter <factor>");
        lexeme = lex();
        expressionEval[indexOfExpressionTable] = lexeme; //Operand of term()
        indexOfExpressionTable++;
        currentLexmePrnt(lexeme);
        nextToken = nextToken();
        if (nextToken.equals("MulOrDivOp")) {
            System.out.println("You can only multiply or divide once");
            errorThrown(nextToken);
            nextToken = nextToken();
            lexeme = lex();
            currentLexmePrnt(lexeme);
            while (!(nextToken.equals("k_display") || nextToken.equals("k_set"))) {
                errorThrown(nextToken);
                lexeme = lex();
                currentLexmePrnt(lexeme);
                nextToken = nextToken();
            }
        } else if (!(nextToken.equals("k_display") ^ nextToken.equals("k_set"))) {
            termFlag = true;
            op1 = Double.valueOf(expressionEval[0]);
            op2 = Double.valueOf(expressionEval[2]);
            if (expressionEval[1].equals("*")) {
                op4 = op1 * op2;
            } else {
                op4 = op1 / op2;
            }
            expr();
        } else {
            //do nothing
        }
        System.out.println("Exit <factor>");
    }

    static void errorThrown(String nextToken) { // method in charge of sending error message when it is encountered in the program
        System.out.println("ERROR: Next Token should not be \"" + nextToken + "\" in line: " + lineCountnextToken + "; please check the upcoming lexeme");
        error2 = true;
    }

    static void currentLexmePrnt(String lex) { // method in charge of printing the lexeme when it is encountered throughout execution
        System.out.println("Current Lexeme is: \"" + lex + "\" at line: " + lineCount);
    }

    //===========================================================================================================================================================================================================================
    static String lex() { // scanner function that is called by the parser at any point to get the next lexeme
        String lexeme = "";
        String nextLexeme = parag2Lex.next();
        if (nextLexeme.equals(";")) {
            parag1Lex.next();
            parag2Lex.next();
            lineCount++;

        }
        if (nextLexeme.equals(":")) {
            parag1Lex.next();
            parag2Lex.next();
        }
        if (nextLexeme.charAt(0) == '"' && nextLexeme.charAt(nextLexeme.length() - 1) != '"') {
            lexeme = parag1Lex.next();
            //parag2.next();
            while (lexeme.charAt(lexeme.length() - 1) != '"') {
                lexeme = lexeme + " " + parag1Lex.next();
                parag2Lex.next();
            }
            return lexeme;
        }
        lexeme = parag1Lex.next();
        return lexeme;
    }

    //===========================================================================================================================================================================================================================
    // geChar() takes in a lexeme and returns a character in the lexeme to determine appropriate token
    static char getChar(String lexeme, int index) {
        char nextChar = lexeme.charAt(index);
        return nextChar;
    }

    //===========================================================================================================================================================================================================================
    // method grabs a lexeme and categorizes it based on the symbols table
    static String lookupLexeme(String lexeme, String keywordTable[][]) {
        String token = "identifier";

        for (int i = 0; i < keywordTable[0].length; i++) { // for loop takes the lexeme (a string) and compares it against all elements of the sysmbols tables
            if (keywordTable[1][i].equals(lexeme)) {
                token = keywordTable[0][i];
                break;
            }
        }

        return token; // returns token based on the symbols table
    }

    //===========================================================================================================================================================================================================================
    // lookupChar() takes in character - operators - and looks them up in the symbols table
    static String lookupChar(char ch, String[][] keywordTable) {
        String token = "";
        String character = "" + ch;

        for (int i = 0; i < keywordTable[0].length; i++) { // for loop goes through the symbols table until it finds the operator
            if (keywordTable[1][i].equals(character)) {
                token = keywordTable[0][i];
                break;
            }
        }

        return token; // returns the token based on the ch variable if it finds one in the symbols table
    }

    static String getToken() {
        String token = "";
        String nextToken = parag2Token.next();
        if (nextToken.equals(";")) {
            parag1Token.next();
            parag2Token.next();
            lineCountnextToken++;
        }

        if (nextToken.equals(":")) {
            parag1Token.next();
            parag2Token.next();
        }

        if (nextToken.charAt(0) == '"' && nextToken.charAt(nextToken.length() - 1) != '"') {
            token = parag1Token.next();
            while (token.charAt(token.length() - 1) != '"') {
                token = token + " " + parag1Token.next();
                parag2Token.next();
            }
            return token;
        }
        token = parag1Token.next();
        return token;
    }

    static String nextToken() {

        boolean dbl = false;
        String token = ""; // variable will store the token for the particular lexeme being analyzed at the time
        String extract = getToken(); // variable that stores lexemes to be analyzed

        char currentChar = getChar(extract, 0); // currentChar variable will store a character of the lexeme in order to use it in the conditions of if-statements below

        // following are if statements meant to categorize the lexemes of the SCL source file
        if (extract.charAt(0) == '"' && extract.charAt(extract.length() - 1) == '"') {
            token = "string_literal";
        } else if (extract.equals("*/")) {
            token = "keyword";
        } else if (currentChar == '0' || currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4' || currentChar == '5' || currentChar == '6' || currentChar == '7' || currentChar == '8' || currentChar == '9') {
            for (int i = 0; i < extract.length(); i++) {
                if (getChar(extract, i) == '0' || getChar(extract, i) == '1' || getChar(extract, i) == '2' || getChar(extract, i) == '3' || getChar(extract, i) == '4' || getChar(extract, i) == '5' || getChar(extract, i) == '6' || getChar(extract, i) == '7' || getChar(extract, i) == '8' || getChar(extract, i) == '9' || getChar(extract, i) == '.') {
                    if (getChar(extract, i) == '.' && dbl) {
                        token = "Error. Does Not Exist";
                        break;
                    }
                    if (getChar(extract, i) == '.') {
                        dbl = true; // boolean that becomes true when one decimal point is present in the number
                    }
                    token = "Numeric Literal";
                } else {
                    if (i != extract.length() - 1) {
                        token = "Error. Does Not Exist";
                    } else {
                        if (extract.charAt(extract.length() - 1) == ',' || extract.charAt(extract.length() - 1) == '.') {
                            extract = extract.substring(0, extract.length() - 1);
                            token = "Identifier";
                        } else {
                            token = "Error. Does Not Exist.";
                        }
                    }
                }
            }
        } else if (currentChar == '*' || currentChar == '/' || currentChar == '+' || currentChar == '-' || currentChar == '(' || currentChar == ')') {
            if (extract.length() == 1) {
                token = lookupChar(currentChar, keywordTable);
            } else {
                token = "Error. Does Not Exist";

            }
        } else {
            token = lookupLexeme(extract, keywordTable);
        }

        return token;
    }

    // method transSCL() translates the scl source file into C++, the intermediate language
    static void transSCL() throws IOException, InterruptedException {
        // Print statements for better looking execution of translator
        System.out.println("\n-------------------------------------------------------------------------------------\n");
        System.out.print("Getting Ready to Compile File");
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print("done!");

        String newFile = "";
        for (int i = 0; i < fileName.length(); i++) {
            if (fileName.charAt(i) == '.') { // traverses through the file name until it gets to a '.'. Used to add the extension to the file
                break;
            }
            newFile = newFile + fileName.charAt(i);
        }
        newFile = newFile + ".cpp"; // whatever string is stored inside newFile plus the extension '.cpp'
        PrintStream outputFile = new PrintStream(new File("out.cpp")); // output file with executable code, name is out.cpp
        PrintStream consoleStream = new PrintStream(new FileOutputStream(FileDescriptor.out)); // PrintStream object to switch console back
        System.setOut(outputFile); // set out the destination of the stream

        System.out.println("//Name: Imad Ghandour and Jorge Vera Garcia"); // add comments to the .cpp file
        System.out.println("//Professor: Jose Garrido");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("//Date and Time Translated: " + dtf.format(now) + "\n");
        System.out.println("#include<iostream>"); // adds necessary lines of code that ever .cpp file need
        System.out.println("using namespace std;\n"); // same as above
        while (translate.hasNext()) {
            String line = translate.nextLine();
            Scanner scan_line = new Scanner(line);
            String firstWord = scan_line.next();

            // the following if-statements and while loops will look for keywords in the source file, identify what kind of statement is, and translate it to C++ programming language
            if (firstWord.equals("Implementations")) {
                continue;
            } else if (firstWord.equals("function")) { // if the method tupe is a function...
                String mainName = scan_line.next();
                String methodType = scan_line.next();
                while (!(methodType.equals("integer") || methodType.equals("double"))) { // while loop that looks for the method type
                    methodType = scan_line.next();
                }
                if (methodType.equals("integer")) { // if method type is Integer/ Only two possible method types: double and integer
                    methodType = "int";
                }
                System.out.println(methodType + " " + mainName + "() {"); // necessary details to make .cpp file compilable by g++
            } else if (firstWord.equals("define")) { // if the statement in .scl file starts with define, translate to
                String variableName = scan_line.next();
                String type = scan_line.next();
                while (!(type.equals("integer") || type.equals("double"))) { // while the following statements start with either 'integer' or 'double', continue looking for variable definitions
                    type = scan_line.next();
                }
                if (type.equals("integer")) {
                    type = "int";
                }
                System.out.println("\t" + type + " " + variableName + " = 0;"); // need in c++
            } else if (firstWord.equals("begin")) {
                continue;
            } else if (firstWord.equals("set")) { // 'set' is not a keyword in c++ so it can be ignored
                line = line.substring(8);
                System.out.println("\t" + line + ";");
            } else if (firstWord.equals("display")) {
                line = line.substring(11);
                System.out.println("\tcout << " + line + ";");
            } else if (firstWord.equals("exit")) {
                continue;
            } else if (firstWord.equals("endfun")) {
                System.out.println("}");
            }
        }


        System.setOut(consoleStream); // switch the output destination of the stream back to the console
        Runtime runtime = Runtime.getRuntime(); // creates a runtime object used below
        System.out.print("\nCompiling File");
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print(".");
        TimeUnit.SECONDS.sleep(1);
        System.out.print("done!");
        runtime.exec("g++ out.cpp"); // runtime object calls exec() method to execute in terminal the command g++ out.cpp that compiles out.cpp

        String OS = System.getProperty("os.name").toLowerCase(); //to detect OS
        System.out.println("\nOS Detected as \"" + OS.toUpperCase() + "\"");
        System.out.println("Running File with \"" + OS.toUpperCase() + "\" Configuration");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("\n-------------------------------------------------------------------------------------");
        System.out.println("\nOutput of SCL Source Code:");

        Process bashRun; // process object
        if (OS.contains("mac") || OS.contains("linux")) { // check whether the Operating System is either Mac/linux or Windows. Matters because depending on what OS executing the program the bash command is different
            try {
                bashRun = runtime.exec("./a.out");
            } catch (Exception ex) {
                bashRun = runtime.exec("./a.out");
            }
        } else {
            try {
                bashRun = runtime.exec("a.exe");
            } catch (Exception ex) {
                bashRun = runtime.exec("a.exe");
            }
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(bashRun.getInputStream()));
        String line = "";
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("\n-------------------------------------------------------------------------------------");
        System.out.println("\nFinished Running at: " + dtf.format(now));
    }

}



