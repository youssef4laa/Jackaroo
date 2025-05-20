package view;

import controller.BoardController;      // Import the BoardController
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

// <-- import your popup helper
import view.ExceptionPopup;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu(primaryStage);
        startMenu.show(playerName -> {
            try {
                buildGameUI(primaryStage, playerName);
            } catch (IOException e) {
                // this really is “cannot continue” — show error then exit
                ExceptionPopup.showError("Startup Error",
                                         "Failed to start game: " + e.getMessage());
                Platform.exit();
                System.exit(1);
            } catch (Exception e) {
                // any other unexpected error at startup: show popup, but let window stay open
                ExceptionPopup.showException(e);
            }
        });
    }

    private void buildGameUI(Stage stage, String playerName) throws IOException {
        // instantiate the controller (which builds model+view)
        BoardController boardController = new BoardController(playerName);

        // grab its root pane and put it on the stage
        BorderPane root = boardController.getGameView();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Jackaroo — Welcome, " + playerName);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
