import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class spaceTrader extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Button startButton = new Button();
        startButton.setText("Start Game");
        TextField name = new TextField();
        ObservableList<String> difficulty = FXCollections.observableArrayList(
                "Easy",
                "Medium",
                "Hard"
        );
        ComboBox chooseDifficulty = new ComboBox(difficulty);

        startButton.setOnAction((e) -> {
            String playerName = name.getText();
            name.clear();
            try {
                if (playerName == null || playerName.equals("")) {
                    throw new IllegalArgumentException("Name cannot be blank");
                } else {
                    System.out.printf("Game Started! %n%s has entered the game", playerName);
                }
            } catch(IllegalArgumentException i) {
                System.out.println(i.getMessage());
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(name, chooseDifficulty, startButton);

        Scene scene = new Scene(vbox);

        primaryStage.setTitle("Space Trader");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
