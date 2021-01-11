/*
 Jorge Vera Garcia
 Imad El Ddine Ghandour
 Theory of Programming Languages
 CS 7843. Section 01
 Dr. Jose Garrido
 */

import java.io.*;
import java.util.Scanner;

/*
The following program is a lexical analyzer (implemented as a scanner) for programming language scl. The program takes in a .scl file and scans each string inside of it, categorizing
lexemes. This program is just the beginning step of a language processor. 
 */

public class CS7843_01_JorgeVeraGarcia_ImadGhandour_LexicalAnalyzerSCL {

    // Driver method
    public static void main(String[] args) throws FileNotFoundException {
        int extractCount = 0;
        int lineCount = 0;
        boolean str = false;
        boolean dbl = false;
        String stringLiteral = "";

        // Symbols table containing keywords and operators
        String[][] symbolTable = {
                {"keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "keyword", "MulOrDivOp", "MulOrDivOp", "addOrSubOp", "addOrSubOp", "leftParenthesis", "rightParenthesis", "assignmentOp"},
                {"import", "double", "of", "is", "type", "variables", "implementations", "function", "main", "integer", "begin", "endfun", "define", "set", "display", "description", "*/", "exit", "*", "/", "+", "-", "(", ")", "="},
        };

        Scanner userInput = new Scanner(System.in);

        System.out.print("Enter file name: ");
        String fileName = userInput.nextLine();

        Scanner input = new Scanner(new File(fileName)); // Scanner object that holds .scl file to be scanned

        // this while loop goes through the entire file, scanning each string up until a space and categorizing them based on whether they are identifiers, keywords or operators
        while (input.hasNextLine()) {
            lineCount++;
            String line = input.nextLine();

            Scanner nextLine = new Scanner(line);
            while (nextLine.hasNext()) {
                dbl = false;
                String token = ""; // variable will store the token for the particular lexeme being analyzed at the time
                String extract = nextLine.next(); // variable that stores lexemes to be analyzed
                extractCount = extractCount + extract.length();

                if (getChar(extract, 0) == '\"') {
                    str = true;
                }

                if (!str) {
                    char currentChar = getChar(extract, 0); // currentChar variable will store a character of the lexeme in order to use it in the conditions of if-statements below

                    // following are if statements meant to categorize the lexemes of the SCL source file
                    if (extract.equals("*/")) {
                        token = "keyword";
                    } else if (currentChar == '0' || currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4' || currentChar == '5' || currentChar == '6' || currentChar == '7' || currentChar == '8' || currentChar == '9') {
                        for (int i = 0; i < extract.length(); i++) {
                            if (getChar(extract, i) == '0' || getChar(extract, i) == '1' || getChar(extract, i) == '2' || getChar(extract, i) == '3' || getChar(extract, i) == '4' || getChar(extract, i) == '5' || getChar(extract, i) == '6' || getChar(extract, i) == '7' || getChar(extract, i) == '8' || getChar(extract, i) == '9' || getChar(extract, i) == '.') {
                                if (getChar(extract, i) =='.' && dbl) {
                                    token = "Error. Does Not Exist";
                                    break; //this will break out of the .next() loop when we have >1 '.' in the number
                                }
                                if (getChar(extract, i) == '.') {
                                    dbl = true; // boolean that becomes true when one decimal point is present in the number
                                }
                                token = "Numeric Literal";
                            } else {
                                if (i != extract.length() - 1) {
                                    token = "Error. Does Not Exist";
                                    break;
                                } else {
                                    if (extract.charAt(extract.length() - 1) == ',' || extract.charAt(extract.length() - 1) == '.') {
                                        extract = extract.substring(0, extract.length() - 1);
                                        token = "Identifier";
                                        break;
                                    } else {
                                        token = "Error. Does Not Exist.";
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (currentChar == '*' || currentChar == '/' || currentChar == '+' || currentChar == '-' || currentChar == '(' || currentChar == ')') {
                        if (extract.length () == 1) { // checks if the operator length is equal to 1
                            token = lookupChar(currentChar, symbolTable);
                        } else {
                            token = "Error. Does Not Exist";
                            continue;
                        }
                    } else if (extract.equals(":")) {
                        continue;
                    } else {
                        token = lookupLexeme(extract, symbolTable);
                    }

                    System.out.println(token + ", symbol: \"" + extract + "\" line: " + lineCount);

                } else {
                    stringLiteral = stringLiteral + extract + " ";

                    if (getChar(extract, extract.length() - 1) == '\"') {
                        str = false;
                        token = "String Literal";
                        System.out.println(token + ", symbol: " + stringLiteral + " line: " + lineCount);

                        stringLiteral = "";
                    }
                }
            }
        }
    }

    //===========================================================================================================================================================================================================================
    // geChar() takes in a lexeme and returns a character in the lexeme to determine appropriate token
    static char getChar(String lexeme, int index) {
        char nextChar = lexeme.charAt(index);
        return nextChar;
    }

    //===========================================================================================================================================================================================================================
    // method grabs a lexeme and categorizes it based on the symbols table
    static String lookupLexeme(String lexeme, String symbolTable[][]) {
        String token = "identifier";

        for (int i = 0; i < symbolTable[0].length; i++) { // for loop takes the lexeme (a string) and compares it against all elements of the sysmbols tables
            if (symbolTable[1][i].equals(lexeme)) {
                token = symbolTable[0][i];
                break;
            }
        }

        return token; // returns token based on the symbols table
    }

    //===========================================================================================================================================================================================================================
    // lookupChar() takes in character - operators - and looks them up in the symbols table
    static String lookupChar(char ch, String[][] symbolTable) {
        String token = "";
        String character = "" + ch;

        for (int i = 0; i < symbolTable[0].length; i++) { // for loop goes through the symbols table until it finds the operator
            if (symbolTable[1][i].equals(character)) {
                token = symbolTable[0][i];
                break;
            }
        }

        return token; // returns the token based on the ch variable if it finds one in the symbols table
    }
}
