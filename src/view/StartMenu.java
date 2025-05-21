package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.function.Consumer;

public class StartMenu {
    private final Stage stage;
    private MediaPlayer startMusic;

    public StartMenu(Stage stage) {
        this.stage = stage;
    }

    /**
     * Shows the start menu. When the user enters a valid name and clicks
     * Start, onStart.accept(name) is called.
     */
    public void show(Consumer<String> onStart) {
        // --- PLAY BACKGROUND MUSIC ---
        try {
            Media media = new Media(
                getClass().getResource("/audio/startmusic.mp3")
                    .toExternalForm()
            );
            startMusic = new MediaPlayer(media);
            startMusic.setCycleCount(MediaPlayer.INDEFINITE);
            startMusic.setVolume(0.7);
            
            startMusic.play();
        } catch (NullPointerException e) {
            System.err.println("Could not find startmusic.mp3 on the classpath!");
        }

        // --- background image full-screen ---
        Image bgImg = new Image(getClass().getResourceAsStream("/images/jackarooo.png"));
        ImageView bgView = new ImageView(bgImg);
        bgView.setPreserveRatio(true);
        bgView.setSmooth(true);
        bgView.setFitWidth(1024);
        bgView.setFitHeight(1024);
        bgView.setOpacity(0.9);

        // --- label (smaller) ---
        Label welcomeLabel = new Label("WELCOME");
        welcomeLabel.setTextFill(Color.LIGHTGRAY);
        welcomeLabel.setFont(Font.font("Verdana", 24));
        welcomeLabel.setEffect(new DropShadow(5, Color.BLACK));

        // --- input & button (narrower) ---
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your name here");
        nameField.setMaxWidth(180);
        nameField.setStyle(
            "-fx-background-color: rgba(255,255,255,0.9);" +
            "-fx-text-fill: #333333;" +
            "-fx-prompt-text-fill: rgba(51,51,51,0.6);" +
            "-fx-background-radius: 5;"
        );

        Button startBtn = new Button("Start Game");
        startBtn.setFont(Font.font(14));
        startBtn.setPrefWidth(180);
        startBtn.setStyle(
            "-fx-background-color: #e67e22;" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 4 12;"
        );
        startBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Invalid Input");
                warning.setHeaderText(null);
                warning.setContentText("Please enter a valid name.");
                warning.showAndWait();
            } else {
                // stop the music when transitioning
                if (startMusic != null) startMusic.stop();
                onStart.accept(name);
            }
        });

        // --- control panel (compact vertical menu) ---
        VBox controls = new VBox(8,
            welcomeLabel,
            nameField,
            startBtn
        );
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(8));
        controls.setMaxWidth(200);
        controls.setStyle(
            "-fx-background-color: rgba(0,0,0,0.6);" +
            "-fx-background-radius: 8;"
        );
        controls.setEffect(new DropShadow(5, Color.PURPLE));
        controls.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        controls.setTranslateY(60);

        // --- root layout & positioning ---
        StackPane root = new StackPane(bgView, controls);
        StackPane.setAlignment(controls, Pos.TOP_LEFT);
        StackPane.setMargin(controls, new Insets(120, 0, 0, 80));

        Scene scene = new Scene(root, 1024, 1024);
        stage.setScene(scene);
        stage.setTitle("Start Menu");
        stage.setResizable(false);
        stage.show();
    }
}
