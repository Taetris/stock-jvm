package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StockApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent main = FXMLLoader.load(StockApplication.class.getResource("../../resources/stock-overview.fxml"));
        Scene scene = new Scene(main, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
