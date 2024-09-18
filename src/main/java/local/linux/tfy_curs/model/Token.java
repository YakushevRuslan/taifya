package local.linux.tfy_curs.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Token {
    public enum TokenType {
        BEGIN, END, INTEGER, IDENTIFIER, NUMBER, COMMA, SEMICOLON, REAL, DOUBLE, TRUE, FALSE, POINT,
        EQUAL, IF, ELSE, PLUS, MINUS, MULT, DIV, LESS, EQUALS, COLON, MORE, ASSIGNMENT,THEN, VAR, ENTER
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private TokenType type;
    private String value;


    public Token(TokenType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return String.format("%s, %s ", type, value);
    }


    static TokenType[] Delimiters = new TokenType[]{
            TokenType.PLUS, TokenType.MINUS, TokenType.MULT,
            TokenType.DIV, TokenType.EQUAL, TokenType.MORE,
            TokenType.LESS, TokenType.COMMA
    };


    public static boolean isDelimiter(Token token) {
        return Arrays.asList(Delimiters).contains(token.type);
    }


    public static Map<String, TokenType> SpecialWords = new HashMap<String, TokenType>() {{
        put("begin", TokenType.BEGIN);
        put("integer", TokenType.INTEGER);
        put("if", TokenType.IF);
        put("else", TokenType.ELSE);
        put("==", TokenType.EQUALS);
        put("real", TokenType.REAL);
        put("double", TokenType.DOUBLE);
        put(":=",TokenType.ASSIGNMENT);
        put("var", TokenType.VAR);
        put("then", TokenType.THEN);
        put("end", TokenType.END);
        put("true", TokenType.TRUE);
        put("false", TokenType.FALSE);
    }};


    public static boolean isSpecialWord(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }
        return SpecialWords.containsKey(word);
    }


    public static Map<Character, TokenType> SpecialSymbols = new HashMap<Character, TokenType>() {{
        put(';', TokenType.SEMICOLON);
        put(',', TokenType.COMMA);
        put('.', TokenType.POINT);
        put('+', TokenType.PLUS);
        put('=', TokenType.EQUAL);
        put('>', TokenType.MORE);
        put('-', TokenType.MINUS);
        put('*', TokenType.MULT);
        put('/', TokenType.DIV);
        put('<', TokenType.LESS);
        put(':', TokenType.COLON);
        put('#', TokenType.ENTER);
    }};


     public static boolean isSpecialSymbol(char ch) {
        return SpecialSymbols.containsKey(ch);
    }

}
