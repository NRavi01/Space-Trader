import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;


public class SpaceTrader extends Application {
    //Player properties
    private Player player;
    private String playerName;
    private SimpleIntegerProperty value = new SimpleIntegerProperty(this, "value");
    private int skillPoints = 16;
    private int difficultyLevel = 0;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];

    private final int regionNumber = 10;

    Region currentSystem;
    private final String[] governments = {"Democratic", "Fascist", "Communist", "Separatist"};
    private ArrayList<String> names = new ArrayList<String>();

    private Region[] regions = new Region[regionNumber];

    public SpaceTrader() {
        names.add("NishSystem");
        names.add("BobSystem");
        names.add("Undorix");
        names.add("Bubriri");
        names.add("Menereth");
        names.add("Peccora");
        names.add("Sestrion");
        names.add("Eimia");
        names.add("Vautov");
        names.add("Bathenope");
        for (int i = 0; i < 4; i++) {
            points[i] = new SimpleIntegerProperty();
        }
        int regionSize = (int) (Math.random() * 10 + 10);
        int techLevel = (int) (Math.random() * 3 + 1);
        System.out.println("tech: " + techLevel);
        String government = governments[(int) (Math.random() * governments.length)];
        int policePresence = (int) (Math.random() * 3 + 1);
        int currRegionName = (int) (Math.random() * regionNumber);
        currentSystem = new Region(names.get(currRegionName), (int) (Math.random() * 600), (int) (Math.random() * 600), regionSize, techLevel, government, policePresence);
        names.remove(currRegionName);

        regions[0] = currentSystem;
        for (int i = 1; i < regions.length; i++) {
            Region newSystem;
            int regionSize2 = (int) (Math.random() * 10 + 10);
            int techLevel2 = (int) (Math.random() * 3 + 1);
            String government2 = governments[(int) (Math.random() * governments.length)];
            int policePresence2 = (int) (Math.random() * 3 +  1);
            int sysName = (int) (Math.random() * names.size());
            newSystem = new Region(names.get(sysName), (int) (Math.random() * 600), (int) (Math.random() * 600), regionSize2, techLevel2, government2, policePresence2);
            regions[i] = newSystem;
            names.remove(sysName);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        /*
        AudioClip music = new AudioClip(this.getClass().getResource("StarWarsMusic.mp3").toString());
        music.play();
        */
        Stage window = primaryStage;

        //SCENE 1
        ImageView mv = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);

        Text t = new Text(100, 200, "SPACE TRADER");
        t.setFont(new Font(60));
        t.setFill(Color.YELLOW);

        Button playButton = new Button("Play");
        playButton.setTextFill(Color.YELLOW);
        playButton.setFont(new Font(30));
        playButton.setStyle("-fx-background-color: transparent;");
        playButton.setOnMouseEntered(e -> playButton.setTextFill(Color.RED));
        playButton.setOnMouseExited(e -> playButton.setTextFill(Color.YELLOW));
        playButton.setLayoutX(250);
        playButton.setLayoutY(350);

        Group grp = new Group();
        grp.getChildren().addAll(mv, t, playButton);

        Scene scene1 = new Scene(grp, 600, 600);


        //SCENE TWO
        ImageView mv2 = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);

        Text t2 = new Text(100, 100, "SPACE TRADER");
        t2.setFill(Color.YELLOW);
        t2.setFont(new Font(60));

        TextField name = new TextField();
        name.setPromptText("Enter player name here!");
        name.setLayoutX(200);
        name.setPrefHeight(50);
        name.setPrefWidth(200);
        name.setLayoutY(150);


        Label pointsText = createLabel("Skill points left: ", 400, 250, 20, Color.YELLOW, 150);
        pointsText.setPrefHeight(50);

        Label numPoints = createLabel("16", 550, 250, 20, Color.YELLOW, 50);
        numPoints.setPrefHeight(50);

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
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                points[0].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue()
                        + points[2].getValue() + points[3].getValue()) > skillPoints) {
                    points[0].set(points[0].getValue() - 1);
                    pilotSlider.setValue(points[0].getValue());
                } else {
                    pilotLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue()
                        + points[1].getValue() + points[2].getValue() + points[3].getValue())));
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
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                points[1].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue()
                        + points[3].getValue()) > skillPoints) {
                    points[1].set(points[1].getValue() - 1);
                    fighterSlider.setValue(points[1].getValue());
                } else {
                    fighterLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue()
                        + points[1].getValue() + points[2].getValue() + points[3].getValue())));
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
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                points[2].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue()
                        + points[3].getValue()) > skillPoints) {
                    points[2].set(points[2].getValue() - 1);
                    engineerSlider.setValue(points[2].getValue());
                } else {
                    engineerLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue()
                        + points[1].getValue() + points[2].getValue() + points[3].getValue())));
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
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                points[3].set(t1.intValue());
                if ((points[0].getValue() + points[1].getValue() + points[2].getValue()
                        + points[3].getValue()) > skillPoints) {
                    points[3].set(points[3].getValue() - 1);
                    traderSlider.setValue(points[3].getValue());
                } else {
                    traderLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                }
                numPoints.setText(Integer.toString(skillPoints - (points[0].getValue()
                        + points[1].getValue() + points[2].getValue() + points[3].getValue())));
            }
        });
        traderBox.getChildren().addAll(trader, traderSlider, traderLabel);

        Label choiceBoxDescription = new Label("Difficulty Level: ");
        choiceBoxDescription.setLayoutX(100);
        choiceBoxDescription.setLayoutY(250);
        choiceBoxDescription.setPrefWidth(150);
        choiceBoxDescription.setPrefHeight(50);
        choiceBoxDescription.setFont(new Font(20));
        choiceBoxDescription.setTextFill(Color.YELLOW);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Easy", "Medium", "Hard");
        choiceBox.setValue("Easy");
        choiceBox.setLayoutX(250);
        choiceBox.setPrefHeight(50);
        choiceBox.setPrefWidth(100);
        choiceBox.setLayoutY(250);
        choiceBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String level, String newLevel) {
                pilotSlider.setValue(0);
                fighterSlider.setValue(0);
                engineerSlider.setValue(0);
                traderSlider.setValue(0);
                if (newLevel.equals("Easy")) {
                    setSkillPoints(16);
                    numPoints.setText("16");
                } else if (newLevel.equals("Medium")) {
                    setSkillPoints(12);
                    numPoints.setText("12");
                } else {
                    setSkillPoints(8);
                    numPoints.setText("8");
                }
            }
        });


        Button startButton = new Button("Start Game");
        startButton.setTextFill(Color.YELLOW);
        startButton.setFont(new Font(20));
        startButton.setStyle("-fx-background-color: transparent;");
        startButton.setOnMouseEntered(e -> startButton.setTextFill(Color.RED));
        startButton.setOnMouseExited(e -> startButton.setTextFill(Color.YELLOW));
        startButton.setPrefWidth(150);
        startButton.setLayoutX(225);
        startButton.setLayoutY(550);

        Group grp2 = new Group();
        grp2.getChildren().addAll(mv2, name, choiceBox, choiceBoxDescription, startButton, t2);
        grp2.getChildren().addAll(pilotBox, fighterBox, engineerBox, traderBox, numPoints);
        grp2.getChildren().add(pointsText);
        Scene scene2 = new Scene(grp2, 600, 600);
        playButton.setOnAction(e -> window.setScene(scene2));

        //SCENE 3
        ImageView mv3 = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);

        ImageView ship = createImage("SpaceTraderShip.png", 350, 125, 200, 325);

        Text t3 = new Text(100, 50, "SPACE TRADER");
        t3.setFill(Color.YELLOW);
        t3.setFont(new Font(60));

        Text t4 = new Text(175, 100, "Commander Status");
        t4.setFill(Color.YELLOW);
        t4.setFont(new Font(30));

        Label t5 = createLabel("Player Name: ", 0, 100, 20, Color.YELLOW, 125);

        Label t6 = createLabel("Player name", 125, 100, 20, Color.RED, 50);
        t6.textProperty().bind(name.textProperty());

        Label t7 = createLabel("Number of Credits: ", 0, 150, 20, Color.YELLOW, 175);

        Label t8 = createLabel("10", 175, 150, 20, Color.RED, 50);
        t8.textProperty().bind(Bindings.convert(value));

        Label t9 = createLabel("Pilot Skill Points: ", 0, 200, 20, Color.YELLOW, 175);

        Label t10 = createLabel("10", 200, 200, 20, Color.RED, 50);
        t10.textProperty().bind(Bindings.convert(points[0]));

        Label t11 = createLabel("Fighter Skill Points: ", 0, 250, 20, Color.YELLOW, 175);

        Label t12 = createLabel("10", 200, 250, 20, Color.RED, 50);
        t12.textProperty().bind(Bindings.convert(points[1]));

        Label t13 = createLabel("Engineer Skill Points: ", 0, 300, 20, Color.YELLOW, 200);

        Label t14 = createLabel("10", 200, 300, 20, Color.RED, 50);
        t14.textProperty().bind(Bindings.convert(points[2]));

        Label t15 = createLabel("Trader Skill Points: ", 0, 350, 20, Color.YELLOW, 175);

        Label t16 = createLabel("10", 200, 350, 20, Color.RED, 50);
        t16.textProperty().bind(Bindings.convert(points[3]));

        Line tabLine = new Line(0, 500, 600,500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150,600);
        vertLine1.setStroke(Color.YELLOW);

        Button travelChart = new Button("Travel Chart");
        travelChart.setTextFill(Color.YELLOW);
        travelChart.setFont(new Font(20));
        travelChart.setStyle("-fx-background-color: transparent;");
        travelChart.setOnMouseEntered(e -> travelChart.setTextFill(Color.RED));
        travelChart.setOnMouseExited(e -> travelChart.setTextFill(Color.YELLOW));
        travelChart.setPrefWidth(150);
        travelChart.setLayoutX(0);
        travelChart.setLayoutY(525);

        Group grp3 = new Group();
        grp3.getChildren().addAll(mv3, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
        grp3.getChildren().addAll(ship, tabLine, travelChart, vertLine1);
        Scene scene3 = new Scene(grp3, 600, 600);
        startButton.setOnAction((e) -> {
            getDifficultyChoice(choiceBox);
            playerName = name.getText();

            try {
                if (playerName == null || playerName.equals("")) {
                    throw new IllegalArgumentException("Name cannot be blank");
                }

                //System.out.printf("Game Started! %n%s has entered the game", playerName);
            } catch (IllegalArgumentException var4) {
                System.out.println(var4.getMessage());
            }
            window.setScene(scene3);
        });

        //SCENE FOUR
        player = new Player(playerName, getDifficultyChoice(choiceBox), points);

        travelChart.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        //Starting the demo
        window.setScene(scene1);
        primaryStage.setTitle("Space Trader");
        primaryStage.show();
    }

    private Group getCommanderStatus(Stage window) {
        ImageView mv3 = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);

        ImageView ship = createImage("SpaceTraderShip.png", 350, 125, 200, 325);

        Text t3 = new Text(100, 50, "SPACE TRADER");
        t3.setFill(Color.YELLOW);
        t3.setFont(new Font(60));

        Text t4 = new Text(175, 100, "Commander Status");
        t4.setFill(Color.YELLOW);
        t4.setFont(new Font(30));

        Label t5 = createLabel("Player Name: ", 0, 100, 20, Color.YELLOW, 125);

        Label t6 = createLabel("Player name", 125, 100, 20, Color.RED, 50);
        //HOW TF ARE WE GONNA DO THIS
        //t6.textProperty().bind(name.textProperty());

        Label t7 = createLabel("Number of Credits: ", 0, 150, 20, Color.YELLOW, 175);

        Label t8 = createLabel("10", 175, 150, 20, Color.RED, 50);
        t8.textProperty().bind(Bindings.convert(value));

        Label t9 = createLabel("Pilot Skill Points: ", 0, 200, 20, Color.YELLOW, 175);

        Label t10 = createLabel("10", 200, 200, 20, Color.RED, 50);
        t10.textProperty().bind(Bindings.convert(points[0]));

        Label t11 = createLabel("Fighter Skill Points: ", 0, 250, 20, Color.YELLOW, 175);

        Label t12 = createLabel("10", 200, 250, 20, Color.RED, 50);
        t12.textProperty().bind(Bindings.convert(points[1]));

        Label t13 = createLabel("Engineer Skill Points: ", 0, 300, 20, Color.YELLOW, 200);

        Label t14 = createLabel("10", 200, 300, 20, Color.RED, 50);
        t14.textProperty().bind(Bindings.convert(points[2]));

        Label t15 = createLabel("Trader Skill Points: ", 0, 350, 20, Color.YELLOW, 175);

        Label t16 = createLabel("10", 200, 350, 20, Color.RED, 50);
        t16.textProperty().bind(Bindings.convert(points[3]));

        Line tabLine = new Line(0, 500, 600,500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150,600);
        vertLine1.setStroke(Color.YELLOW);

        Button travelChart = new Button("Travel Chart");
        travelChart.setTextFill(Color.YELLOW);
        travelChart.setFont(new Font(20));
        travelChart.setStyle("-fx-background-color: transparent;");
        travelChart.setOnMouseEntered(e -> travelChart.setTextFill(Color.RED));
        travelChart.setOnMouseExited(e -> travelChart.setTextFill(Color.YELLOW));
        travelChart.setPrefWidth(150);
        travelChart.setLayoutX(0);
        travelChart.setLayoutY(525);

        Group grp3 = new Group();
        grp3.getChildren().addAll(mv3, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
        grp3.getChildren().addAll(ship, tabLine, travelChart, vertLine1);

        travelChart.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        return grp3;
    }

    //private Group getTravelChart(Stage window, Scene homeScene) {
    private Group getTravelChart(Stage window) {
        ImageView image4 = createImage("travelChartBackground.jpg", 0, 0, 600, 600);

        Label fourLabel = createLabel("SPACE TRADER", 210, 0, 25, Color.YELLOW, 180);

        Group grp4 = new Group();
        grp4.getChildren().add(image4);

        Circle fuelRadius = new Circle(300, 300, player.getFuel());
        fuelRadius.setStroke(Color.YELLOW);

        int xShift = 300 - currentSystem.getUniX();
        int yShift = 300 - currentSystem.getUniY();
        currentSystem.setSubX(300);
        currentSystem.setSubY(300);

        Circle currRegion = new Circle(currentSystem.getSubX(), currentSystem.getSubY(), currentSystem.getSize(), Color.RED);
        currRegion.setStroke(Color.BLUE);
        System.out.println(currentSystem.getName());
        int xco = currentSystem.getSubX() - currentSystem.getSize() - 10;
        int yco = currentSystem.getSubY() + currentSystem.getSize();
        Label currRegionLabel = createLabel(currentSystem.getName(), xco, yco, 10, Color.GREEN, 60);

        ArrayList<Circle> systems = new ArrayList<Circle>();
        ArrayList<Label> systemLabels = new ArrayList<Label>();
        ArrayList<Button> systemButtons = new ArrayList<Button>();
        for (int i = 0; i < regions.length; i++) {
            if(!regions[i].getName().equals(currentSystem.getName())) {
                regions[i].setSubX(regions[i].getUniX() + xShift);
                regions[i].setSubY(regions[i].getUniY() + yShift);
                systems.add(new Circle(regions[i].getSubX(), regions[i].getSubY(), regions[i].getSize(), Color.GREEN));
                int xlabel = regions[i].getSubX() - regions[i].getSize() - 10;
                int ylabel = regions[i].getSubY() + regions[i].getSize();
                systemLabels.add(createLabel(regions[i].getName(), xlabel, ylabel, 10, Color.YELLOW, 60));
                int xbut = regions[i].getSubX() - (int) (regions[i].getSize() * 1.5);
                int ybut = regions[i].getSubY() - (int) (regions[i].getSize() * 1.5);
                systemButtons.add(createButton(xbut, ybut, regions[i].getSize(), regions[i].getSize(), regions[i].getName()));
            }
        }
        int xbut = currentSystem.getSubX() - (int) (currentSystem.getSize() * 1.5);
        int ybut = currentSystem.getSubY() - (int) (currentSystem.getSize() * 1.5);
        systemButtons.add(createButton(xbut, ybut, currentSystem.getSize(),
            currentSystem.getSize(), currentSystem.getName()));

        grp4.getChildren().addAll(fourLabel, fuelRadius, currRegion, currRegionLabel);

        for (int i = 0; i < systems.size(); i++) {
            grp4.getChildren().add(systems.get(i));
        }
        for (int i = 0; i < systemLabels.size(); i++) {
            grp4.getChildren().add(systemLabels.get(i));
        }

        for (int i = 0; i < systemButtons.size(); i++) {
            int index = i;
            systemButtons.get(index).setOnAction((e) -> {
                for (int j = 0; j < regions.length; j++) {
                    if (systemButtons.get(index).getText().equals(regions[j].getName())) {
                        window.setScene(createRegionScene(window, regions[j]));
                    }
                }
            });
            grp4.getChildren().add(systemButtons.get(i));
        }

        return grp4;
    }

    private Scene createRegionScene(Stage window, Region region) {
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName(), 210, 0, 25, Color.YELLOW, 180);
        planetName.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Tech Level: ", 0, 100, 20, Color.YELLOW, 125);

        Label t2 = createLabel(Integer.toString(region.getTechLevel()), 125, 100, 20, Color.RED, 50);

        Label t3 = createLabel("Government Type: ", 0, 150, 20, Color.YELLOW, 175);

        Label t4 = createLabel(region.getGovernment(), 175, 150, 20, Color.RED, 175);

        Label t5 = createLabel("Police Presence: ", 0, 200, 20, Color.YELLOW, 175);

        Label t6 = createLabel(Integer.toString(region.getPolicePresence()), 175, 200, 20, Color.RED, 50);

        Line tabLine = new Line(0, 500, 600, 500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150, 600);
        vertLine1.setStroke(Color.YELLOW);

        Line vertLine2 = new Line(450, 500, 450, 600);
        vertLine2.setStroke(Color.YELLOW);

        Line vertLine3 = new Line(300, 500, 300, 600);
        vertLine3.setStroke(Color.YELLOW);

        Button backButton = new Button("Back");
        backButton.setTextFill(Color.YELLOW);
        backButton.setFont(new Font(20));
        backButton.setStyle("-fx-background-color: transparent;");
        backButton.setOnMouseEntered(e -> backButton.setTextFill(Color.RED));
        backButton.setOnMouseExited(e -> backButton.setTextFill(Color.YELLOW));
        backButton.setPrefWidth(150);
        backButton.setLayoutX(0);
        backButton.setLayoutY(525);

        Button commandButton = new Button("Status");
        commandButton.setTextFill(Color.YELLOW);
        commandButton.setFont(new Font(20));
        commandButton.setStyle("-fx-background-color: transparent;");
        commandButton.setOnMouseEntered(e -> commandButton.setTextFill(Color.RED));
        commandButton.setOnMouseExited(e -> commandButton.setTextFill(Color.YELLOW));
        commandButton.setPrefWidth(150);
        commandButton.setLayoutX(150);
        commandButton.setLayoutY(525);

        Button travelButton = new Button("Travel");
        travelButton.setTextFill(Color.YELLOW);
        travelButton.setFont(new Font(20));
        travelButton.setStyle("-fx-background-color: transparent;");
        travelButton.setOnMouseEntered(e -> travelButton.setTextFill(Color.RED));
        travelButton.setOnMouseExited(e -> travelButton.setTextFill(Color.YELLOW));
        travelButton.setPrefWidth(150);
        travelButton.setLayoutX(450);
        travelButton.setLayoutY(525);

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, t3, t4, t5, t6, tabLine,
            vertLine1, vertLine2, vertLine3, backButton, commandButton, travelButton);

        backButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        commandButton.setOnAction(e -> {
            Group grp4 = getCommanderStatus(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        travelButton.setOnAction(e -> {
            int distance = (int) (getDistance(region.getSubX(), region.getSubY(), 300, 300));
            if (distance <= player.getFuel()) {
                player.changeFuel((-1) * distance);
                setCurrRegion(region);
                resetPoints();
            }
            else {
                System.out.println("Outside fuel range!");
            }

            Group grp5 = getTravelChart(window);
            Scene s5 = new Scene(grp5, 600, 600);
            window.setScene(s5);
        });

        return new Scene(grp, 600, 600);
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        double distance = Math.abs((double) ( (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) ));
        return Math.sqrt(distance);
    }
    private void resetPoints() {
        for (int i = 0; i < regions.length; i++) {
            regions[i].setSubX(regions[i].getUniX());
            regions[i].setSubY(regions[i].getUniY());
        }
    }

    private void setCurrRegion(Region reg) {
        currentSystem = reg;
    }
    private Button createButton(int x, int y, int width, int height, String name) {
        Button startButton = new Button(name);
        startButton.setFont(new Font(20));
        startButton.setTextFill(Color.BLUE);
        startButton.setStyle("-fx-background-color: transparent;");
        startButton.setPrefWidth(width);
        startButton.setPrefHeight(height);
        startButton.setLayoutX(x);
        startButton.setLayoutY(y);
        return startButton;
    }

    //provide different difficulty levels(easy, medium, hard)
    private int getDifficultyChoice(ChoiceBox<String> choiceBox) {
        String level = choiceBox.getValue();
        if (level.equals("Easy")) {
            value.set(1000);
            difficultyLevel = 1;
            setSkillPoints();
        } else if (level.equals("Medium")) {
            value.set(500);
            difficultyLevel = 2;
            setSkillPoints();
        } else if (level.equals("Hard")) {
            value.set(100);
            difficultyLevel = 3;
            setSkillPoints();
        } else {
            throw new IllegalArgumentException("Difficulty Level must be selected");
        }
        return difficultyLevel;
    }

    public Label createLabel(String text, int x, int y, int font, Color c, int width) {
        Label newLabel = new Label(text);
        newLabel.setTextFill(c);
        newLabel.setFont(new Font(font));
        newLabel.setLayoutX(x);
        newLabel.setLayoutY(y);
        newLabel.setPrefWidth(width);
        return newLabel;
    }
    public ImageView createImage(String path, int x, int y, int width, int height) {
        Image newImage = new Image("\\resources\\" + path);
        ImageView image = new ImageView(newImage);
        image.setFitHeight(height);
        image.setFitWidth(width);
        image.setLayoutX(x);
        image.setLayoutY(y);
        return image;
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
