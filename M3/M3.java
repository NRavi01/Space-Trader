
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class M3 extends Application {
    private int difficultyLevel = 0;

    public M3() {
    }

    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;
        //STAGE 1

        Image image = new Image("SpaceTraderBackground.jpeg");
        ImageView mv = new ImageView(image);
        mv.setFitHeight(600);
        mv.setFitWidth(600);
        mv.setLayoutX(0);
        mv.setLayoutY(0);

        Text t = new Text(125, 100, "SPACE TRADER");
        t.setFill(Color.YELLOW);
        t.setFont(new Font(60));

        Button playButton = new Button();
        playButton.setPrefHeight(100);
        playButton.setPrefWidth(100);
        playButton.setText("Play");
        playButton.setTextFill(Color.YELLOW);
        playButton.setStyle("-fx-font-size:90");
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setLayoutX(250);
        playButton.setLayoutY(400);

        Group grp = new Group();
        grp.getChildren().add(mv);
        grp.getChildren().add(t);
        grp.getChildren().add(playButton);

        Scene scene1 = new Scene(grp, 600, 600);


        //STAGE TWO
        Image image2 = new Image("SpaceTraderBackground.jpeg");
        ImageView mv2 = new ImageView(image2);
        mv2.setFitHeight(600);
        mv2.setFitWidth(600);
        mv2.setLayoutX(0);
        mv2.setLayoutY(0);

        Text t2 = new Text(125, 100, "SPACE TRADER");
        t2.setFill(Color.YELLOW);
        t2.setFont(new Font(60));

        TextField name = new TextField();
        name.setPromptText("Enter player name here!");
        name.setLayoutX(200);
        name.setPrefHeight(50);
        name.setPrefWidth(200);
        name.setLayoutY(250);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Easy", "Medium", "Hard");
        choiceBox.setValue("Please choose a difficulty level!");
        choiceBox.setLayoutX(250);
        choiceBox.setPrefHeight(50);
        choiceBox.setPrefWidth(100);
        choiceBox.setLayoutY(350);


        Button startButton = new Button();
        startButton.setText("Start Game");
        startButton.setLayoutX(250);
        startButton.setPrefHeight(50);
        startButton.setPrefWidth(100);
        startButton.setLayoutY(450);

        startButton.setOnAction((e) -> {
            getDifficultyChoice(choiceBox);
            String playerName = name.getText();
            name.clear();

            try {
                if (playerName == null || playerName.equals("")) {
                    throw new IllegalArgumentException("Name cannot be blank");
                }

                System.out.printf("Game Started! %n%s has entered the game", playerName);
            } catch (IllegalArgumentException var4) {
                System.out.println(var4.getMessage());
            }

        });

        Group grp2 = new Group();
        grp2.getChildren().add(mv2);
        grp2.getChildren().add(name);
        grp2.getChildren().add(choiceBox);
        grp2.getChildren().add(startButton);
        grp2.getChildren().add(t2);
        Scene scene2 = new Scene(grp2, 600, 600);
        playButton.setOnAction(e -> window.setScene(scene2));

        //Starting the demo
        window.setScene(scene1);
        primaryStage.setTitle("Space Trader");
        primaryStage.show();
    }

    private void getDifficultyChoice(ChoiceBox<String> choiceBox) {
        String level = choiceBox.getValue();
        if (level.equals("Easy")) {
            difficultyLevel = 1;
        }
        else if (level.equals("Medium")) {
            difficultyLevel = 2;
        }
        else if (level.equals("Hard")) {
            difficultyLevel = 3;
        }
        else {
            throw new IllegalArgumentException("Difficulty Level must be selected");
        }
        System.out.println(difficultyLevel);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
