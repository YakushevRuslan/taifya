package local.linux.tfy_curs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParserApplication extends Application {
    private Stage mainStage;

    @Override
    public void start(Stage mainStage) throws IOException {
        this.mainStage = mainStage;
        FXMLLoader fxmlLoader = new FXMLLoader(ParserApplication.class.getResource("parser-application-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1313, 600);
        mainStage.setTitle("Syntactic Analyzer");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public Stage getMainStage(){return this.mainStage;}

    public static void main(String[] args) {
        launch();
    }
}