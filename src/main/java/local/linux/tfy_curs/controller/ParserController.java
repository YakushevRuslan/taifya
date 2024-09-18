package local.linux.tfy_curs.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import local.linux.tfy_curs.ParserApplication;
import local.linux.tfy_curs.model.Token;
import local.linux.tfy_curs.util.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;


public class ParserController {
    private ParserApplication parserApplication;

    List<String> lexemes = new ArrayList<>();
    List<Token> tokens = new ArrayList<>();


    @FXML
    TextArea textAreaSourceCode;
    @FXML
    TextArea textAreaResult;
    @FXML
    TextArea textAreaLexems;
    @FXML
    TextArea textAreaTokens;

    @FXML
    protected void onStartButtonClick() {
        textAreaTokens.clear();
        lexemeAnalysis();
        createTokens();
        runParser();
        lexemes.clear();
    }

    @FXML
    protected void onDownloadButtonClick() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(new Stage());
        Path filePath = file.toPath(); // Получаем путь к выбранному файлу
        String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8); // Читаем содержимое файла в строку
        textAreaSourceCode.setText(content);

    }

    private void lexemeAnalysis() {
        textAreaLexems.clear();
        lexemes.clear();
        char[] data = textAreaSourceCode.getText().toCharArray();
        for (int i = 0; i < data.length; i++) {
            if (isLetters(data[i])) {
                StringBuilder letter = new StringBuilder();
                while (isLetters(data[i])) {
                    letter.append(data[i]);
                    i++;
                }

                textAreaLexems.appendText(String.format("%s - Идентификатор \n", letter));
                lexemes.add(String.format("%s I", letter));
                textAreaLexems.appendText("\n");

                if (letter.length() > 8) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Предупреждение");
                    alert.setHeaderText("Ошибка. Максимальная длина идентификатора превышает 10 символов.");
                    alert.showAndWait();
                }
            }

            if (isNumbers(data[i])) {
                StringBuilder number = new StringBuilder();
                while (isNumbers(data[i])) {
                    number.append(data[i]);
                    i++;
                }

                textAreaLexems.appendText(String.format("%s - Литерал \n", number));
                lexemes.add(String.format("%s L", number));
                textAreaLexems.appendText("\n");
            }

            if (isSeparator(data[i])) {
                if (data[i] == '\n')
                    continue;
                if (i < data.length + 1) {
                    if (data[i] == ':' && data[i + 1] == '=') {
                        textAreaLexems.appendText(String.format("%s%s - Разделитель \n", data[i], data[i + 1]));
                        lexemes.add(String.format("%s%s D", data[i], data[i + 1]));
                        textAreaLexems.appendText("\n");
                        i++;
                    } else {
                        textAreaLexems.appendText(String.format("%s - Разделитель \n", data[i]));
                        lexemes.add(String.format("%s D", data[i]));
                        textAreaLexems.appendText("\n");

                        }
                    }

            } else {
                if (data[i] == ' ')
                    continue;
                char tmp = data[i];
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Ошибка. Не удается распознать символ : " + tmp);
                alert.showAndWait();
            }
        }
    }

    private void createTokens() {
        textAreaTokens.clear();
        tokens.clear();
        String lexem = "";
        String type = "";

        for (int i = 0; i < lexemes.size(); i++) {

            lexem = lexemes.get(i).split(" ")[0];
            type = lexemes.get(i).split(" ")[1];

            if (type.equals("I")) {       //Identificator
                if (Token.isSpecialWord(lexem)) {
                    Token token = new Token(Token.SpecialWords.get(lexem));
                    tokens.add(token);
                    textAreaTokens.appendText(token.getType().toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                } else {
                    Token token = new Token(Token.TokenType.IDENTIFIER);
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");

                }
            } else if (type.equals("L")) {      //Literal
                Token token = new Token(Token.TokenType.NUMBER);
                token.setValue(lexem);
                tokens.add(token);
                textAreaTokens.appendText(token.toString());
                textAreaTokens.appendText("\n");
                textAreaTokens.appendText("\n");

            } else if (type.equals("D")) { // delimiter
                if(lexem.length() > 1 && lexem.charAt(0) == ':' && lexem.charAt(1) == '='){
                    Token token = new Token(Token.TokenType.ASSIGNMENT);
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                }else {
                    Token token = new Token(Token.SpecialSymbols.get(lexem.charAt(0)));
                    token.setValue(lexem);
                    tokens.add(token);
                    textAreaTokens.appendText(token.toString());
                    textAreaTokens.appendText("\n");
                    textAreaTokens.appendText("\n");
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText(String.format("Ошибка. При формировании токена для %s произошла ошибка.", type));
                alert.showAndWait();
            }
        }
    }

    private char[] separators = {',', '+', '-', '=', '/', '(', ')', '\n', ':', ';', '.', '>', '<'};

    public boolean isLetters(char symbol) {
        String letters = "" + symbol;
        return Pattern.matches("[a-zA-Z]+$", letters);
    }

    public boolean isNumbers(char symbol) {
        String letters = "" + symbol;
        return Pattern.matches("[0-9]+$", letters);
    }

    public boolean isSeparator(char symbol) {
        for (char sep : separators) {
            if (symbol == sep) {
                return true;
            }
        }
        return false;
    }

    public  void runParser(){
        textAreaResult.clear();
        Parser parser = new Parser(tokens);
        Boolean bool = parser.run();
        if (bool){
            textAreaResult.appendText("The code matches the grammar");
            //textAreaResult.appendText(parser.getResult());
        }else {
            textAreaResult.appendText("The code no matches the grammar");
        }
    }
}
