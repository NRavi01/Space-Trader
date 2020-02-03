import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.FileInputStream;


public class SpaceTrader extends Application {
    private String playerName;
    private SimpleIntegerProperty value = new SimpleIntegerProperty(this, "value");
    private int skillPoints = 16;
    private int difficultyLevel = 0;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];

    public SpaceTrader() {
        for (int i = 0; i < 4; i++) {
            points[i] = new SimpleIntegerProperty();
        }
    }

    public void start(Stage primaryStage) throws Exception {
        Stage window = primaryStage;

        //STAGE 1
        Image image = new Image("SpaceTraderBackground.jpg");
        ImageView mv = new ImageView(image);
        mv.setFitHeight(600);
        mv.setFitWidth(600);
        mv.setLayoutX(0);
        mv.setLayoutY(0);

        Text t = new Text(100, 200, "SPACE TRADER");
//        Font transformers_medium = Font.loadFont(new FileInputStream("C:\\Users\\bobby\\Documents\\Year1" +
//                "\\CS2340\\spaceTrader\\resources\\transformers_font.ttf"), 60);
        t.setFont(new Font(60));
        t.setFill(Color.YELLOW);
//        t.setFont(transformers_medium);

        Button playButton = new Button("Play");
        playButton.setTextFill(Color.YELLOW);
//        Font transformers_small = Font.loadFont(new FileInputStream("C:\\Users\\bobby\\Documents\\Year1" +
//                "\\CS2340\\spaceTrader\\resources\\transformers_font.ttf"), 20);
//        playButton.setFont(transformers_small);
        playButton.setFont(new Font(20));
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setLayoutX(250);
        playButton.setLayoutY(350);

        Group grp = new Group();
        grp.getChildren().add(mv);
        grp.getChildren().add(t);
        grp.getChildren().add(playButton);

        Scene scene1 = new Scene(grp, 600, 600);


        //STAGE TWO
        Image image2 = new Image("SpaceTraderBackground.jpg");
        ImageView mv2 = new ImageView(image2);
        mv2.setFitHeight(600);
        mv2.setFitWidth(600);
        mv2.setLayoutX(0);
        mv2.setLayoutY(0);

        Text t2 = new Text(100, 100, "SPACE TRADER");
        t2.setFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t2.setFont(new Font(60));

        TextField name = new TextField();
        name.setPromptText("Enter player name here!");
        name.setLayoutX(200);
        name.setPrefHeight(50);
        name.setPrefWidth(200);
        name.setLayoutY(150);


        Text pointsText = new Text(455, 300, "Points left: ");
        pointsText.setFill(Color.YELLOW);
        pointsText.setFont(new Font(12));
        Label numPoints = new Label("16");
        numPoints.setLayoutX(550);
        numPoints.setLayoutY(287);
        numPoints.setTextFill(Color.YELLOW);

        HBox pilotBox = new HBox();
        pilotBox.setLayoutX(50);
        pilotBox.setLayoutY(350);
        Text pilot = new Text(50, 350, "Pilot  \t");
        pilot.setFill(Color.YELLOW);
        pilot.setFont(new Font(15));
        Slider pilotSlider = new Slider(0, 10, 0);
        pilotSlider.setPrefWidth(400);
        pilotSlider.setShowTickMarks(true);
        pilotSlider.setMajorTickUnit(1);
        pilotSlider.setMinorTickCount(0);
        pilotSlider.setBlockIncrement(10);
        pilotSlider.setSnapToTicks(true);
        Label pilotLabel = new Label("0");
        pilotLabel.setTextFill(Color.YELLOW);
        pilotSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                points[0].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue()) > skillPoints) {
                    points[0].set(points[0].getValue() - 1);
                    pilotSlider.setValue(points[0].getValue());
                }
                else {
                    pilotLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue())));
            }
        });
        pilotBox.getChildren().addAll(pilot, pilotSlider, pilotLabel);

        HBox fighterBox = new HBox();
        fighterBox.setLayoutX(50);
        fighterBox.setLayoutY(400);
        Text fighter = new Text(50, 350, "Fighter \t");
        fighter.setFill(Color.YELLOW);
        fighter.setFont(new Font(15));
        Slider fighterSlider = new Slider(0, 10, 0);
        fighterSlider.setPrefWidth(400);
        fighterSlider.setShowTickMarks(true);
        fighterSlider.setMajorTickUnit(1);
        fighterSlider.setMinorTickCount(0);
        fighterSlider.setBlockIncrement(10);
        fighterSlider.setSnapToTicks(true);
        Label fighterLabel = new Label("0");
        fighterLabel.setTextFill(Color.YELLOW);
        fighterSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                points[1].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue()) > skillPoints) {
                    points[1].set(points[1].getValue() - 1);
                    fighterSlider.setValue(points[1].getValue());
                }
                else {
                    fighterLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue())));
            }
        });
        fighterBox.getChildren().addAll(fighter, fighterSlider, fighterLabel);

        HBox engineerBox = new HBox();
        engineerBox.setLayoutX(50);
        engineerBox.setLayoutY(450);
        Text engineer = new Text(50, 350, "Engineer\t");
        engineer.setFill(Color.YELLOW);
        engineer.setFont(new Font(15));
        Slider engineerSlider = new Slider(0, 10, 0);
        engineerSlider.setPrefWidth(400);
        engineerSlider.setShowTickMarks(true);
        engineerSlider.setMajorTickUnit(1);
        engineerSlider.setMinorTickCount(0);
        engineerSlider.setBlockIncrement(10);
        engineerSlider.setSnapToTicks(true);
        Label engineerLabel = new Label("0");
        engineerLabel.setTextFill(Color.YELLOW);
        engineerSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                points[2].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue()) > skillPoints) {
                    points[2].set(points[2].getValue() - 1);
                    engineerSlider.setValue(points[2].getValue());
                }
                else {
                    engineerLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue())));
            }
        });
        engineerBox.getChildren().addAll(engineer, engineerSlider, engineerLabel);


        HBox traderBox = new HBox();
        traderBox.setLayoutX(50);
        traderBox.setLayoutY(500);
        Text trader = new Text(50, 350, "Trader  \t");
        trader.setFill(Color.YELLOW);
        trader.setFont(new Font(15));
        Slider traderSlider = new Slider(0, 10, 0);
        traderSlider.setPrefWidth(400);
        traderSlider.setShowTickMarks(true);
        traderSlider.setMajorTickUnit(1);
        traderSlider.setMinorTickCount(0);
        traderSlider.setBlockIncrement(10);
        traderSlider.setSnapToTicks(true);
        Label traderLabel = new Label("0");
        traderLabel.setTextFill(Color.YELLOW);
        traderSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1){
                points[3].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue()) > skillPoints) {
                    points[3].set(points[3].getValue() - 1);
                    traderSlider.setValue(points[3].getValue());
                }
                else {
                    traderLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue() + points[1].getValue() + points[2].getValue() + points[3].getValue())));
            }
        });
        traderBox.getChildren().addAll(trader, traderSlider, traderLabel);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Easy", "Medium", "Hard");
        choiceBox.setValue("Please choose a difficulty level!");
        choiceBox.setLayoutX(250);
        choiceBox.setPrefHeight(50);
        choiceBox.setPrefWidth(100);
        choiceBox.setLayoutY(250);
        choiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String level, String newLevel){
                pilotSlider.setValue(0);
                fighterSlider.setValue(0);
                engineerSlider.setValue(0);
                traderSlider.setValue(0);
                if (newLevel.equals("Easy")) {
                    setSkillPoints(16);
                    numPoints.setText("16");
                }
                else if (newLevel.equals("Medium")) {
                    setSkillPoints(12);
                    numPoints.setText("12");
                }
                else {
                    setSkillPoints(8);
                    numPoints.setText("8");
                }
            }
        });


        Button startButton = new Button("Start Game");
        startButton.setTextFill(Color.YELLOW);
//        startButton.setFont(transformers_small);
        startButton.setFont(new Font(20));
        startButton.setStyle("-fx-background-color: transparent;");
        startButton.setPrefWidth(150);
        startButton.setLayoutX(225);
        startButton.setLayoutY(550);

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
        grp2.getChildren().add(pilotBox);
        grp2.getChildren().add(fighterBox);
        grp2.getChildren().add(engineerBox);
        grp2.getChildren().add(traderBox);
        grp2.getChildren().add(numPoints);
        grp2.getChildren().add(pointsText);
        Scene scene2 = new Scene(grp2, 600, 600);
        playButton.setOnAction(e -> window.setScene(scene2));

        //STAGE 3
        Image image3 = new Image("SpaceTraderBackground.jpg");
        ImageView mv3 = new ImageView(image3);
        mv2.setFitHeight(600);
        mv2.setFitWidth(600);
        mv2.setLayoutX(0);
        mv2.setLayoutY(0);

        Text t3 = new Text(100, 50, "SPACE TRADER");
        t3.setFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t3.setFont(new Font(60));

        Text t4 = new Text(175, 100, "Commander Status");
        t4.setFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t4.setFont(new Font(30));

        Label t5 = new Label("Player Name: ");
        t5.setLayoutX(0);
        t5.setLayoutY(100);
        t5.setPrefWidth(125);
        t5.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t5.setFont(new Font(20));

        Label t6 = new Label("Player name");
        t6.setLayoutX(125);
        t6.setLayoutY(100);
        t6.textProperty().bind(name.textProperty());
        System.out.println(t6.textProperty());
        t6.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t6.setFont(new Font(20));

        Label t7 = new Label("Number of Credits: ");
        t7.setLayoutX(0);
        t7.setLayoutY(150);
        t7.setPrefWidth(175);
        t7.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t7.setFont(new Font(20));

        Label t8 = new Label("10");
        t8.setLayoutX(175);
        t8.setLayoutY(150);
        t8.setPrefWidth(50);
        t8.textProperty().bind(Bindings.convert(value));
        t8.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t8.setFont(new Font(20));

        Label t9 = new Label("Pilot Skill Points: ");
        t9.setLayoutX(0);
        t9.setLayoutY(200);
        t9.setPrefWidth(175);
        t9.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t9.setFont(new Font(20));

        Label t10 = new Label("10");
        t10.setLayoutX(200);
        t10.setLayoutY(200);
        t10.setPrefWidth(50);
        t10.textProperty().bind(Bindings.convert(points[0]));
        t10.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t10.setFont(new Font(20));

        Label t11 = new Label("Fighter Skill Points: ");
        t11.setLayoutX(0);
        t11.setLayoutY(250);
        t11.setPrefWidth(175);
        t11.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t11.setFont(new Font(20));

        Label t12 = new Label("10");
        t12.setLayoutX(200);
        t12.setLayoutY(250);
        t12.setPrefWidth(50);
        t12.textProperty().bind(Bindings.convert(points[1]));
        t12.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t12.setFont(new Font(20));

        Label t13 = new Label("Engineer Skill Points: ");
        t13.setLayoutX(0);
        t13.setLayoutY(300);
        t13.setPrefWidth(200);
        t13.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t13.setFont(new Font(20));

        Label t14 = new Label("10");
        t14.setLayoutX(200);
        t14.setLayoutY(300);
        t14.setPrefWidth(50);
        t14.textProperty().bind(Bindings.convert(points[2]));
        t14.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t14.setFont(new Font(20));

        Label t15 = new Label("Trader Skill Points: ");
        t15.setLayoutX(0);
        t15.setLayoutY(350);
        t15.setPrefWidth(175);
        t15.setTextFill(Color.YELLOW);
//        t2.setFont(transformers_medium);
        t15.setFont(new Font(20));

        Label t16 = new Label("10");
        t16.setLayoutX(200);
        t16.setLayoutY(350);
        t16.setPrefWidth(50);
        t16.textProperty().bind(Bindings.convert(points[3]));
        t16.setTextFill(Color.RED);
//        t2.setFont(transformers_medium);
        t16.setFont(new Font(20));

        Group grp3 = new Group();
        grp3.getChildren().add(mv3);
        grp3.getChildren().add(t3);
        grp3.getChildren().add(t4);
        grp3.getChildren().add(t5);
        grp3.getChildren().add(t6);
        grp3.getChildren().add(t7);
        grp3.getChildren().add(t8);
        grp3.getChildren().add(t9);
        grp3.getChildren().add(t10);
        grp3.getChildren().add(t11);
        grp3.getChildren().add(t12);
        grp3.getChildren().add(t13);
        grp3.getChildren().add(t14);
        grp3.getChildren().add(t15);
        grp3.getChildren().add(t16);
        Scene scene3 = new Scene(grp3, 600, 600);
        startButton.setOnAction((e) -> {
            getDifficultyChoice(choiceBox);
            playerName = name.getText();

            try {
                if (playerName == null || playerName.equals("")) {
                    throw new IllegalArgumentException("Name cannot be blank");
                }

                System.out.printf("Game Started! %n%s has entered the game", playerName);
            } catch (IllegalArgumentException var4) {
                System.out.println(var4.getMessage());
            }
            window.setScene(scene3);
        });

        //Starting the demo
        window.setScene(scene1);
        primaryStage.setTitle("Space Trader");
        primaryStage.show();
    }

    private void getDifficultyChoice(ChoiceBox<String> choiceBox) {
        String level = choiceBox.getValue();
        if (level.equals("Easy")) {
            value.set(1000);
            difficultyLevel = 1;
            setSkillPoints();
        }
        else if (level.equals("Medium")) {
            value.set(500);
            difficultyLevel = 2;
            setSkillPoints();
        }
        else if (level.equals("Hard")) {
            value.set(100);
            difficultyLevel = 3;
            setSkillPoints();
        }
        else {
            throw new IllegalArgumentException("Difficulty Level must be selected");
        }
    }

    public int getLevel() {
        return value.getValue();
    }

    private void setSkillPoints() {
        skillPoints = 20 - 4 * difficultyLevel;
    }

    private void setSkillPoints(int points) {
        skillPoints = points;
    }

    public static void main(String[] args) {
        launch(args);
    }
}