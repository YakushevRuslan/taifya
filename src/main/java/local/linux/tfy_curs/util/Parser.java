package local.linux.tfy_curs.util;

import local.linux.tfy_curs.model.Token;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token token;
    private int numberToken = -1;
    private boolean check = true;
    private String result = "";

    public String getResult(){
        return result;
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private void next() {
        if (numberToken + 1 < tokens.size()){
            numberToken++;
            token = tokens.get(numberToken);
        }
    }

    public boolean run() {
        next();
        program();
        return check;
    }

    //программа
    private void program() {
        if (token.getType() == Token.TokenType.VAR) {
            next();
            listDesription();
            if (token.getType() != Token.TokenType.BEGIN)
                check = false;
            next();
            listOperation();
            if (token.getType() == Token.TokenType.END){
                next();
                if (token.getType() != Token.TokenType.POINT)
                    check = false;
            }else check = false;
        } else check = false;
    }

    //спис опис
    private void listDesription(){
        if (token.getType() == Token.TokenType.IDENTIFIER){
            description();
            if(token.getType() == Token.TokenType.SEMICOLON){
                next();
                altDescription();
            }
        }else check = false;
    }

    //опис
    private void description(){
        if (token.getType() == Token.TokenType.IDENTIFIER){
            listVariable();
            if(token.getType() == Token.TokenType.COLON){
                next();
                type();
            }
        }else check = false;
    }

    //спис перем
    private void listVariable(){
        if (token.getType() == Token.TokenType.IDENTIFIER){
            next();
            altListVariable();
        }else check = false;
    }

    //альт спис перем
    private void altListVariable(){
        if (token.getType() == Token.TokenType.COMMA){
            next();
            listVariable();
        }else if (token.getType() != Token.TokenType.COLON){
           check = false;
        }
    }

    //альт опис
    private void altDescription(){
        if(token.getType() == Token.TokenType.IDENTIFIER){
            listDesription();
        }else if (token.getType() != Token.TokenType.BEGIN) {
            check = false;
        }
    }

    //тип
    private void type(){
        if(token.getType() == Token.TokenType.INTEGER || token.getType() == Token.TokenType.REAL || token.getType() == Token.TokenType.DOUBLE){
            next();
        }else check = false;
    }
    // спис_опер
    private void listOperation(){
        if (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.IF) {
            operation();
            // точка с запятой должна быть только после завершения отдельного оператора
            if (token.getType() == Token.TokenType.SEMICOLON) {
                next();
            }
        } else if (token.getType() == Token.TokenType.BEGIN) {
            next();
            operation();
            if (token.getType() == Token.TokenType.END) {
                next();
                if (token.getType() == Token.TokenType.SEMICOLON || token.getType() == Token.TokenType.POINT) {
                    next();
                } else {
                    check = false;  // ожидается ; или . после END
                }
            } else {
                check = false;  // блок BEGIN не закрыт
            }
        } else {
            check = false;
        }

    }
    //опер
    private void operation(){
        if(token.getType() == Token.TokenType.IDENTIFIER){
            appropriation();
        }else if(token.getType() == Token.TokenType.IF) {
            comparison();
        }else check = false;
    }
    //присв
    private void appropriation(){
        if(token.getType() == Token.TokenType.IDENTIFIER){
            next();
            if(token.getType() == Token.TokenType.ASSIGNMENT){
                next();
                operand();
                if (token.getType() == Token.TokenType.SEMICOLON)
                    next();
                altAppropriation();
            }else check = false;
        }else check = false;
    }

    //условн
    private void comparison(){
        if (token.getType() == Token.TokenType.IF){
            next();
            logicalExpression();
            if (token.getType() == Token.TokenType.THEN){
                next();
                blockOperation();
                altBlockOperation();
            }else check = false;
        }else check = false;
    }

    private void altBlockOperation(){
        if (token.getType() == Token.TokenType.ELSE) {
            next();
            blockOperation();
            // Нет необходимости проверять точку с запятой перед END в блоке ELSE
            if (token.getType() != Token.TokenType.END && token.getType() != Token.TokenType.SEMICOLON) {
                check = false;  // Ошибка: недопустимый символ после блока ELSE
            }
        } else if (token.getType() != Token.TokenType.END) {
            check = false;
        }
    }
    private void blockOperation() {
        if (token.getType() == Token.TokenType.IDENTIFIER) {
            appropriation();
        } else if (token.getType() == Token.TokenType.BEGIN) {
            listOperation();
        }else check = false;
    }

    //операнд
    private void operand(){
        if (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.NUMBER)
            next();
        else check = false;
    }
    //альт_присв
    private void altAppropriation(){
        if (token.getType() == Token.TokenType.IDENTIFIER || token.getType() == Token.TokenType.IF){
                operation();
        }else if (token.getType() != Token.TokenType.ELSE && token.getType() != Token.TokenType.END){
            check = false;
        }
    }

    private void logicalExpression(){
        operand();
        compare();
        operand();
    }

    private void compare() {
        if (token.getType() == Token.TokenType.MORE || token.getType() == Token.TokenType.LESS) {
            next();
        }else check = false;
    }
}
