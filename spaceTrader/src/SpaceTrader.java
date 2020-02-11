import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class SpaceTrader extends Application {
    //Player properties
    private Player player;
    private String playerName;
    private int value = 0;
    private int skillPoints = 16;
    private int difficultyLevel = 0;
    private SimpleIntegerProperty[] points = new SimpleIntegerProperty[4];

    private final int regionNumber = 10;

    Region currentSystem;
    private final String[] governments = {"Democratic", "Fascist", "Communist", "Separatist"};
    private ArrayList<String> names = new ArrayList<String>();

    private Region[] regions = new Region[regionNumber];

    private ArrayList<Region> visitedRegions = new ArrayList<Region>();

    private ArrayList<Shot> shots = new ArrayList<>();

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
        String government = governments[(int) (Math.random() * governments.length)];
        int policePresence = (int) (Math.random() * 3 + 1);
        int currRegionName = (int) (Math.random() * regionNumber);
        currentSystem = new Region(names.get(currRegionName), (int) (Math.random() * 600), (int) (Math.random() * 600), regionSize, techLevel, government, policePresence);
        names.remove(currRegionName);
        visitedRegions.add(currentSystem);

        regions[0] = currentSystem;
        for (int i = 1; i < regions.length; i++) {
            Region newSystem;
            int regionSize2 = (int) (Math.random() * 20 + 10);
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


        startButton.setOnAction((e) -> {
            getDifficultyChoice(choiceBox);
            System.out.println(name.getText());
            playerName = name.getText();
            player = new Player(playerName, getDifficultyChoice(choiceBox), points, value);
            try {
                if (playerName == null || playerName.equals("")) {
                    throw new IllegalArgumentException("Name cannot be blank");
                }

                System.out.printf("Game Started! %n%s has entered the game", playerName);
            } catch (IllegalArgumentException var4) {
                System.out.println(var4.getMessage());
            }
            window.setScene(new Scene(getCommanderStatus(window), 600, 600));
        });

        //SCENE FOUR
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

        Label t6 = createLabel(player.getName(), 125, 100, 20, Color.RED, 50);

        Label t7 = createLabel("Number of Credits: ", 0, 150, 20, Color.YELLOW, 175);

        Label t8 = createLabel(String.valueOf(player.getCredits()), 175, 150, 20, Color.RED, 50);

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

        Button travelChart = createButton(0, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChart.setOnMouseEntered(e -> travelChart.setTextFill(Color.RED));
        travelChart.setOnMouseExited(e -> travelChart.setTextFill(Color.YELLOW));

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

        //Label fourLabel = createLabel("SPACE TRADER", 210, 0, 25, Color.YELLOW, 180);

        Group grp4 = new Group();
        grp4.getChildren().add(image4);

        Circle fuelRadius = new Circle(300, 300, player.getFuel());
        fuelRadius.setStroke(Color.YELLOW);

        int xShift = 300 - currentSystem.getUniX();
        int yShift = 300 - currentSystem.getUniY();
        currentSystem.setSubX(300);
        currentSystem.setSubY(300);

        Circle currRegion = new Circle(currentSystem.getSubX(), currentSystem.getSubY(), currentSystem.getSize(), Color.RED);
        currRegion.setFill(new ImagePattern(new Image("\\resources\\planetImage.png")));
        System.out.println(currentSystem.getName());
        int xco = currentSystem.getSubX() - currentSystem.getSize() - 10;
        int yco = currentSystem.getSubY() + currentSystem.getSize();
        Label currRegionLabel = createLabel(currentSystem.getName(), xco, yco, 10, Color.BLUE, 60);

        ArrayList<Circle> systems = new ArrayList<Circle>();
        ArrayList<Label> systemLabels = new ArrayList<Label>();
        ArrayList<Button> systemButtons = new ArrayList<Button>();
        for (int i = 0; i < regions.length; i++) {
            if(!regions[i].getName().equals(currentSystem.getName())) {
                regions[i].setSubX(regions[i].getUniX() + xShift);
                regions[i].setSubY(regions[i].getUniY() + yShift);
                Circle c = new Circle(regions[i].getSubX(), regions[i].getSubY(), regions[i].getSize(), Color.GREEN);
                c.setFill(new ImagePattern(new Image("\\resources\\planetImage.png")));
                systems.add(c);
                int xlabel = regions[i].getSubX() - regions[i].getSize();
                int ylabel = regions[i].getSubY() + regions[i].getSize();
                systemLabels.add(createLabel(regions[i].getName(), xlabel, ylabel, 10, Color.YELLOW, 60));
                int xbut = regions[i].getSubX() - (int) (regions[i].getSize() * 1.5);
                int ybut = regions[i].getSubY() - (int) (regions[i].getSize() * 1.5);
                Button b = createButton(xbut, ybut, regions[i].getSize() * 2,
                        regions[i].getSize() * 2, Color.BLUE, regions[i].getName());
                b.setTextFill(Color.GREEN);
                b.setFont(new Font(0));
                systemButtons.add(b);
            }
        }
        int xbut = currentSystem.getSubX() - (int) (currentSystem.getSize() * 1.5);
        int ybut = currentSystem.getSubY() - (int) (currentSystem.getSize() * 1.5);
        Button currButton = createButton(xbut, ybut, currentSystem.getSize() * 2,
                currentSystem.getSize() * 2, Color.RED, currentSystem.getName());
        currButton.setFont(new Font(0));
        systemButtons.add(currButton);

        grp4.getChildren().addAll(fuelRadius, currRegion, currRegionLabel);

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
        ImageView background = createImage( "regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName(), 210, 0, 25, Color.YELLOW, 180);
        planetName.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Tech Level: ", 0, 100, 20, Color.YELLOW, 125);

        Label t3 = createLabel("Government Type: ", 0, 150, 20, Color.YELLOW, 175);

        Label t5 = createLabel("Police Presence: ", 0, 200, 20, Color.YELLOW, 175);

        Label t2;
        Label t4;
        Label t6;
        if (visitedRegions.contains(region)) {
            t2 = createLabel(Integer.toString(region.getTechLevel()), 125, 100, 20, Color.RED, 50);
            t4 = createLabel(region.getGovernment(), 175, 150, 20, Color.RED, 175);
            t6 = createLabel(Integer.toString(region.getPolicePresence()), 175, 200, 20, Color.RED, 50);
        }
        else {
            t2 = createLabel("Unknown: Region not visited", 180, 100, 20, Color.RED, 300);
            t4 = createLabel("Unknown: Region not visited", 180, 150, 20, Color.RED, 300);
            t6 = createLabel("Unknown: Region not visited", 180, 200, 20, Color.RED, 300);
        }

        Line tabLine = new Line(0, 500, 600, 500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150, 600);
        vertLine1.setStroke(Color.YELLOW);

        Line vertLine2 = new Line(450, 500, 450, 600);
        vertLine2.setStroke(Color.YELLOW);

        Line vertLine3 = new Line(300, 500, 300, 600);
        vertLine3.setStroke(Color.YELLOW);

        Button backButton = createButton(0, 525, 150, 50, Color.YELLOW, "Back");
        backButton.setOnMouseEntered(e -> backButton.setTextFill(Color.RED));
        backButton.setOnMouseExited(e -> backButton.setTextFill(Color.YELLOW));

        Button commandButton = createButton(150, 525, 150, 50, Color.YELLOW, "Status");
        commandButton.setOnMouseEntered(e -> commandButton.setTextFill(Color.RED));
        commandButton.setOnMouseExited(e -> commandButton.setTextFill(Color.YELLOW));

        Button shipyardButton= createButton(300, 525, 150, 50, Color.YELLOW, "Shipyard");
        shipyardButton.setOnMouseEntered(e -> shipyardButton.setTextFill(Color.RED));
        shipyardButton.setOnMouseExited(e -> shipyardButton.setTextFill(Color.YELLOW));

        Button travelButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel");
        travelButton.setOnMouseEntered(e -> travelButton.setTextFill(Color.RED));
        travelButton.setOnMouseExited(e -> travelButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, t3, t4, t5, t6, tabLine,
                vertLine1, vertLine2, vertLine3, backButton, commandButton, shipyardButton, travelButton);

        backButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        shipyardButton.setOnAction(e -> {
            if  (region == currentSystem) {
                window.setScene(createShipyard(window, region));
            }
            else {
                System.out.println("Can't buy from region you are not in!");
            }
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
                visitedRegions.add(region);
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

    private Scene createShipyard(Stage window, Region region) {
        ImageView background = createImage( "regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Shipyard", 175, 0, 25, Color.YELLOW, 250);
        planetName.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Current Ship Type: ", 0, 50, 20, Color.YELLOW, 250);
        Label t2 = createLabel(player.getShip().getType(), 250, 50, 20, Color.RED, 100);
        Label w1 = createLabel("Current Weapon Level: ", 0, 100, 20, Color.YELLOW, 250);
        Label w2 = createLabel(String.valueOf(player.getShip().getWeaponLevel()), 250, 100, 20, Color.RED, 100);
        Label w3 = createLabel("Current Cargo Holds: ", 0, 150, 20, Color.YELLOW, 250);
        Label w4 = createLabel(String.valueOf(player.getShip().getCargoHolds()), 250, 150, 20, Color.RED, 100);
        Label t3 = createLabel("Current Shield Level: ", 0, 200, 20, Color.YELLOW, 250);
        Label t4 = createLabel(String.valueOf(player.getShip().getShieldLevel()), 250, 200, 20, Color.RED, 175);
        Label t5 = createLabel("Current Fuel Level: ", 0, 250, 20, Color.YELLOW, 250);
        Label t6 = createLabel(String.valueOf(player.getFuel()), 250, 250, 20, Color.RED, 50);
        Label t7 = createLabel("Current Credits: ", 0, 300, 20, Color.YELLOW, 250);
        Label t8 = createLabel(String.valueOf(player.getCredits()), 250, 300, 20, Color.RED, 50);

        Label fuel = createLabel("Increase fuel by: ", 0, 350, 20, Color.BLUE, 150);
        Slider fuelSlider = new Slider(0, 300, 0);
        fuelSlider.setLayoutX(150);
        fuelSlider.setLayoutY(360);
        fuelSlider.setPrefWidth(200);
        fuelSlider.setShowTickMarks(true);
        fuelSlider.setMajorTickUnit(30);
        fuelSlider.setMinorTickCount(20);
        fuelSlider.setBlockIncrement(100);
        fuelSlider.setSnapToTicks(true);
        Label fuelLabel = createLabel("0", 350, 350, 20, Color.BLUE, 35);
        Button addFuel = createButton(385, 340, 215, 50, Color.BLUE , "PURCHASE FOR 0");
        //addFuel.setAlignment(Pos.BASELINE_LEFT);
        addFuel.setOnMouseEntered(e -> addFuel.setTextFill(Color.RED));
        addFuel.setOnMouseExited(e -> addFuel.setTextFill(Color.BLUE));
        fuelSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                fuelLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                addFuel.setText("PURCHASE FOR " + t1.intValue());
            }
        });

        addFuel.setOnAction(e -> {
            int amountFuelAdded = (int) (fuelSlider.getValue());
            if (player.getCredits() > amountFuelAdded) {
                player.changeFuel(amountFuelAdded);
                player.setCredits(player.getCredits() - amountFuelAdded * 3);
                window.setScene(createShipyard(window, region));
            }
            else {
                System.out.println("Not enough money");
                fuelSlider.setValue(0);
            }
        });

        Label shield = createLabel("Increase shield: ", 0, 400, 20, Color.BLUE, 150);
        Slider shieldSlider = new Slider(0, 5, 0);
        shieldSlider.setLayoutX(150);
        shieldSlider.setLayoutY(410);
        shieldSlider.setPrefWidth(200);
        shieldSlider.setShowTickMarks(true);
        shieldSlider.setMajorTickUnit(1);
        shieldSlider.setMinorTickCount(0);
        shieldSlider.setBlockIncrement(1);
        shieldSlider.setSnapToTicks(true);
        Label shieldLabel = createLabel("0", 350, 400, 20, Color.BLUE, 35);
        Button addShield = createButton(385, 390, 215, 50, Color.BLUE , "PURCHASE FOR 0");
        //addFuel.setAlignment(Pos.BASELINE_LEFT);
        addShield.setOnMouseEntered(e -> addShield.setTextFill(Color.RED));
        addShield.setOnMouseExited(e -> addShield.setTextFill(Color.BLUE));
        shieldSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                shieldLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                addShield.setText("PURCHASE FOR " + t1.intValue() * 100);
            }
        });

        addShield.setOnAction(e -> {
            int amountShieldAdded = (int) (shieldSlider.getValue());
            if (player.getCredits() > amountShieldAdded * 100) {
                player.changeShield(amountShieldAdded);
                player.setCredits(player.getCredits() - amountShieldAdded * 100);
                window.setScene(createShipyard(window, region));
            }
            else {
                System.out.println("Not enough money");
                shieldSlider.setValue(0);
            }
        });

        Line tabLine = new Line(0, 500, 600, 500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150, 600);
        vertLine1.setStroke(Color.YELLOW);

        Line vertLine2 = new Line(450, 500, 450, 600);
        vertLine2.setStroke(Color.YELLOW);

        Line vertLine3 = new Line(300, 500, 300, 600);
        vertLine3.setStroke(Color.YELLOW);

        Button backButton = createButton(0, 525, 150, 50, Color.YELLOW, "Back");
        backButton.setOnMouseEntered(e -> backButton.setTextFill(Color.RED));
        backButton.setOnMouseExited(e -> backButton.setTextFill(Color.YELLOW));

        Button commandButton = createButton(150, 525, 150, 50, Color.YELLOW, "PURCHASE");
        commandButton.setOnMouseEntered(e -> commandButton.setTextFill(Color.RED));
        commandButton.setOnMouseExited(e -> commandButton.setTextFill(Color.YELLOW));

        Button buyShipButton= createButton(300, 525, 150, 50, Color.YELLOW, "Buy Ship");
        buyShipButton.setOnMouseEntered(e -> buyShipButton.setTextFill(Color.RED));
        buyShipButton.setOnMouseExited(e -> buyShipButton.setTextFill(Color.YELLOW));

        Button travelChartButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChartButton.setOnMouseEntered(e -> travelChartButton.setTextFill(Color.RED));
        travelChartButton.setOnMouseExited(e -> travelChartButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, w1, w2, w3, w4, t3, t4,
                t5, t6, t7, t8, tabLine, fuel, fuelSlider,
                fuelLabel, addFuel, shield, shieldSlider, shieldLabel, addShield,
                vertLine1, vertLine2, vertLine3, backButton, commandButton, buyShipButton, travelChartButton);

        backButton.setOnAction(e -> {
            window.setScene(createRegionScene(window, region));
        });

        buyShipButton.setOnAction(e -> {
            window.setScene(createBuyShip(window, region));
        });

        commandButton.setOnAction(e -> {
            int amountShieldAdded = (int) (shieldSlider.getValue());
            int amountFuelAdded = (int) (fuelSlider.getValue());
            if (player.getCredits() > (amountShieldAdded * 100 + amountFuelAdded)) {
                player.changeShield(amountShieldAdded);
                player.changeFuel(amountFuelAdded);
                player.setCredits(player.getCredits() - amountShieldAdded * 100 - amountFuelAdded);
                window.setScene(createShipyard(window, region));
            }
            else {
                System.out.println("Not enough money");
                shieldSlider.setValue(0);
                fuelSlider.setValue(0);
            }
        });

        travelChartButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        return new Scene(grp, 600, 600);
    }

    public Scene createBuyShip(Stage window, Region region) {
        ImageView background = createImage( "regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Shipyard", 175, 0, 25, Color.YELLOW, 250);
        planetName.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Gnat: ", 0, 100, 20, Color.YELLOW, 150);
        Label t2 = createLabel("15 Cargo, 1 Weapon, 1 Shield", 150, 100, 20, Color.RED, 300);
        Label cost1 = createLabel("500", 450, 100, 20, Color.BLUE, 100);
        Label t3 = createLabel("Firefly: ", 0, 150, 20, Color.YELLOW, 150);
        Label t4 = createLabel("20 Cargo, 1 Weapon, 1 Shield", 150, 150, 20, Color.RED, 300);
        Label cost2 = createLabel("650", 450, 150, 20, Color.BLUE, 100);
        Label t5 = createLabel("Mosquito: ", 0, 200, 20, Color.YELLOW, 150);
        Label t6 = createLabel("15 Cargo, 2 Weapon, 1 Shield", 150, 200, 20, Color.RED, 300);
        Label cost3 = createLabel("800", 450, 200, 20, Color.BLUE, 100);
        Label t7 = createLabel("Bumblebee: ", 0, 250, 20, Color.YELLOW, 150);
        Label t8 = createLabel("15 Cargo, 2 Weapon, 2 Shield", 150, 250, 20, Color.RED, 300);
        Label cost4 = createLabel("950", 450, 250, 20, Color.BLUE, 100);

        Label shipBoxDescription = new Label("Type of Ship to Buy: ");
        shipBoxDescription.setLayoutX(50);
        shipBoxDescription.setLayoutY(400);
        shipBoxDescription.setPrefWidth(200);
        shipBoxDescription.setPrefHeight(50);
        shipBoxDescription.setFont(new Font(20));
        shipBoxDescription.setTextFill(Color.YELLOW);

        ChoiceBox<String> shipBox = new ChoiceBox<>();
        shipBox.getItems().addAll("Gnat", "Firefly", "Mosquito", "Bumblebee");
        shipBox.setValue("Gnat");
        shipBox.setLayoutX(250);
        shipBox.setPrefHeight(50);
        shipBox.setPrefWidth(100);
        shipBox.setLayoutY(400);
        shipBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue,
                                String level, String newLevel) {

            }
        });

        Line tabLine = new Line(0, 500, 600, 500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150, 600);
        vertLine1.setStroke(Color.YELLOW);

        Line vertLine2 = new Line(450, 500, 450, 600);
        vertLine2.setStroke(Color.YELLOW);

        Line vertLine3 = new Line(300, 500, 300, 600);
        vertLine3.setStroke(Color.YELLOW);

        Button backButton = createButton(0, 525, 150, 50, Color.YELLOW, "Back");
        backButton.setOnMouseEntered(e -> backButton.setTextFill(Color.RED));
        backButton.setOnMouseExited(e -> backButton.setTextFill(Color.YELLOW));

        Button purchaseButton = createButton(150, 525, 150, 50, Color.YELLOW, "Purchase");
        purchaseButton.setOnMouseEntered(e -> purchaseButton.setTextFill(Color.RED));
        purchaseButton.setOnMouseExited(e -> purchaseButton.setTextFill(Color.YELLOW));

        Button shipyardButton = createButton(300, 525, 150, 50, Color.YELLOW, "Shipyard");
        shipyardButton.setOnMouseEntered(e -> shipyardButton.setTextFill(Color.RED));
        shipyardButton.setOnMouseExited(e -> shipyardButton.setTextFill(Color.YELLOW));

        Button travelChartButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChartButton.setOnMouseEntered(e -> travelChartButton.setTextFill(Color.RED));
        travelChartButton.setOnMouseExited(e -> travelChartButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, t3, t4, t5, t6, t7, t8, tabLine,
                vertLine1, vertLine2, vertLine3, backButton, purchaseButton, cost1, cost2, cost3, cost4,
                shipBox, shipBoxDescription, shipyardButton, travelChartButton);

        backButton.setOnAction(e -> {
            window.setScene(createShipyard(window, region));
        });

        shipyardButton.setOnAction(e -> {
            window.setScene(createShipyard(window, region));
        });

        purchaseButton.setOnAction(e -> {
            String shipName = shipBox.getValue();
            for (int i = 0; i < player.getShips().size(); i++) {
                if (shipName.equals(player.getShips().get(i).getType()) && !shipName.equals(player.getShip().getType())) {
                    System.out.println("hi");
                    if (player.getCredits() > player.getShips().get(i).getPrice()) {
                        player.setShip(player.getShips().get(i));
                        player.setCredits(player.getCredits() - player.getShips().get(i).getPrice());
                        window.setScene(createShipyard(window, region));
                    }
                }
            }
        });

        travelChartButton.setOnAction(e -> {
            /*
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
             */
            player.getShip().setSubX(25);
            player.getShip().setSubY(300);
            Ship s2 = new Ship("Gnat", 15, 1, 1, 500);
            s2.setSubX(500);
            s2.setSubY(300);
            getFightScene(window, player.getShip(), s2 );
        });

        return new Scene(grp, 600, 600);
    }

    public Ship getFightScene(Stage stage, Ship s1, Ship s2) {

        //while(checkBothHealth(s1, s2)) {
            ArrayList<ImageView> stageShots = new ArrayList<>();
            ImageView background = createImage( "regionBackground.jpg", 0, 0, 600, 600);
            ImageView ship1 = createImage(s1.getImage(), s1.getSubX(), s1.getSubY(), s1.getSize(), s1.getSize());
            ship1.setRotate(90);
            ImageView ship2 = createImage(s2.getImage(), s2.getSubX(), s2.getSubY(), s2.getSize(), s2.getSize());
            ship2.setRotate(270);
            for (int i = 0; i < shots.size(); i++) {
                int xc = shots.get(i).getX();
                int yc = shots.get(i).getY();
                ImageView shotImage = createImage("spaceBullet.jpg", xc, yc, 50, 30);
                if (shots.get(i).getDirection() == 1) {
                    shotImage.setRotate(180);
                }
                stageShots.add(shotImage);
            }
            Group grp = new Group();
            grp.getChildren().addAll(background, ship1, ship2);
            for (int i = 0; i < stageShots.size(); i++) {
                grp.getChildren().add(stageShots.get(i));
            }
            Scene scene = new Scene(grp, 600, 600);
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.W) {
                    s1.setSubY(s1.getSubY() - 8);
                    getFightScene(stage, s1, s2);
                }
                if (e.getCode() == KeyCode.S) {
                    s1.setSubY(s1.getSubY() + 8);
                    getFightScene(stage, s1, s2);
                }
                if (e.getCode() == KeyCode.SPACE) {
                    int xc = s1.getSubX() + s1.getSize();
                    int yc = s1.getSubY() + s1.getSize()/2 - 10;
                    int speed = 5;
                    int power = player.getShip().getWeaponLevel();
                    shots.add(new Shot(xc, yc, speed, 0, power));
                }
                getFightScene(stage, s1, s2);
            });

            stage.setScene(scene);
            System.out.println("hi");
        try {
            Thread.sleep(5000);
            for (int i = 0; i < shots.size(); i++) {
                shots.get(i).setX(shots.get(i).getX() + shots.get(i).getSpeed());
                System.out.println("Hi");
            }
            getFightScene(stage, s1, s2);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //}
        if (s1.getHealth() > 0) {
            return s1;
        }
        return s2;
    }

    private boolean checkBothHealth(Ship s1, Ship s2) {
        if (s1.getHealth() > 0 && s2.getHealth()> 0) {
            return true;
        }
        return false;
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
    private Button createButton(int x, int y, int width, int height, Color c, String name) {
        Button startButton = new Button(name);
        startButton.setFont(new Font(20));
        startButton.setTextFill(c);
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
            value = 1000;
            difficultyLevel = 1;
            setSkillPoints();
        } else if (level.equals("Medium")) {
            value = 500;
            difficultyLevel = 2;
            setSkillPoints();
        } else if (level.equals("Hard")) {
            value = 100;
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
        return value;
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
