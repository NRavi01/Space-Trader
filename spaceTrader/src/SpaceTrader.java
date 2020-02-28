import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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

    private boolean singleFire = true;

    private final int regionNumber = 10;
    private final LongProperty lastUpdateTime = new SimpleLongProperty();
    private int shipVelocity = 0;
    private Ship s2;
    private int timeCount = 0;
    private Timeline timeline;

    private boolean isShooting = false;

    private Region currentSystem;
    private final String[] governments = {"Democratic", "Fascist", "Communist", "Separatist"};
    private ArrayList<String> names = new ArrayList<String>();

    private Region[] regions = new Region[regionNumber];

    private ArrayList<Region> visitedRegions = new ArrayList<Region>();

    private ArrayList<Shot> shots = new ArrayList<>();

    private Stage window;

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
        currentSystem = new Region(names.get(currRegionName), (int) (Math.random() * 300),
                (int) (Math.random() * 300), regionSize, techLevel, government, policePresence);
        currentSystem.createMarket();
        currentSystem.getMarket().populateMarket();

        names.remove(currRegionName);
        visitedRegions.add(currentSystem);

        regions[0] = currentSystem;

        System.out.println(currentSystem.getName());

        for (int i = 1; i < regions.length; i++) {
            Region newSystem;
            int regionSize2 = (int) (Math.random() * 20 + 10);
            int techLevel2 = (int) (Math.random() * 3 + 1);
            String government2 = governments[(int) (Math.random() * governments.length)];
            int policePresence2 = (int) (Math.random() * 3 + 1);
            int sysName = (int) (Math.random() * names.size());

            int x = (int) (Math.random() * 300);
            int y = (int) (Math.random() * 300);
            boolean insideBounds = true;
            while (insideBounds) {
                insideBounds = false;
                for (int j = 0; j < i; j++) {
                    if (Math.abs(x - regions[j].getUniX()) <= 20
                            || Math.abs(y - regions[j].getUniY()) <= 20) {
                        x = (int) (Math.random() * 300);
                        y = (int) (Math.random() * 300);
                        insideBounds = true;
                        break;
                    }
                }
            }

            newSystem = new Region(names.get(sysName), x, y, regionSize2, techLevel2,
                    government2, policePresence2);
            newSystem.createMarket();
            newSystem.getMarket().populateMarket();
            regions[i] = newSystem;
            names.remove(sysName);
        }
    }

    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        //SCENE 1
        ImageView mv = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);
        mv.setStyle("-fx-background-color:transparent");
        Text t = new Text(100, 200, "SPACE TRADER");
        t.setFont(new Font(60));
        t.setFill(Color.YELLOW);

        Button playButton = createButton(250, 350, 100, 100, Color.YELLOW, "Play");
        playButton.setFont(new Font(30));
        playButton.setOnMouseEntered(e -> playButton.setTextFill(Color.RED));
        playButton.setOnMouseExited(e -> playButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(mv, t, playButton);

        Scene scene1 = new Scene(grp, 600, 600);

        //SCENE TWO
        ImageView mv2 = createImage("SpaceTraderBackground.jpg", 0, 0, 600, 600);
        mv2.setStyle("-fx-background-color:transparent");
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
        Slider pilotSlider = createSlider(0, 10, 0, 400, true, 1, 0);
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
        Slider fighterSlider = createSlider(0, 10, 0, 400, true, 1, 0);
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
        Slider engineerSlider = createSlider(0, 10, 0, 400, true, 1, 0);
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
        Slider traderSlider = createSlider(0, 10, 0, 400, true, 1, 0);
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

        Label choiceBoxDescription = createLabel("Difficulty Level: ", 100, 250, 20,
                Color.YELLOW, 150);
        choiceBoxDescription.setPrefHeight(50);

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

        Button startButton = createButton(225, 550, 150, 35, Color.YELLOW, "Start Game");
        startButton.setOnMouseEntered(e -> startButton.setTextFill(Color.RED));
        startButton.setOnMouseExited(e -> startButton.setTextFill(Color.YELLOW));

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

        Line tabLine = new Line(0, 500, 600, 500);
        tabLine.setStroke(Color.YELLOW);

        Line vertLine1 = new Line(150, 500, 150, 600);
        vertLine1.setStroke(Color.YELLOW);

        Button travelChart = createButton(0, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChart.setOnMouseEntered(e -> travelChart.setTextFill(Color.RED));
        travelChart.setOnMouseExited(e -> travelChart.setTextFill(Color.YELLOW));

        Group grp3 = new Group();
        grp3.getChildren().addAll(mv3, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
        grp3.getChildren().addAll(t16, ship, tabLine, travelChart, vertLine1);

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

        Circle currRegion = new Circle(currentSystem.getSubX(), currentSystem.getSubY(),
                currentSystem.getSize(), Color.RED);
        currRegion.setFill(new ImagePattern(new Image("\\resources\\planetImage.png")));
        System.out.println(currentSystem.getName());
        int xco = currentSystem.getSubX() - currentSystem.getSize() - 10;
        int yco = currentSystem.getSubY() + currentSystem.getSize();
        Label currRegionLabel = createLabel(currentSystem.getName(), xco, yco, 10, Color.BLUE, 60);

        ArrayList<Circle> systems = new ArrayList<Circle>();
        ArrayList<Label> systemLabels = new ArrayList<Label>();
        ArrayList<Button> systemButtons = new ArrayList<Button>();
        for (int i = 0; i < regions.length; i++) {
            if (!regions[i].getName().equals(currentSystem.getName())) {
                regions[i].setSubX(regions[i].getUniX() + xShift);
                regions[i].setSubY(regions[i].getUniY() + yShift);
                Circle c = new Circle(regions[i].getSubX(), regions[i].getSubY(),
                        regions[i].getSize(), Color.GREEN);
                c.setFill(new ImagePattern(new Image("\\resources\\planetImage.png")));
                systems.add(c);
                int xlabel = regions[i].getSubX() - regions[i].getSize();
                int ylabel = regions[i].getSubY() + regions[i].getSize();
                systemLabels.add(createLabel(regions[i].getName(), xlabel, ylabel, 10,
                        Color.YELLOW, 60));
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
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName(), 210, 0, 25, Color.YELLOW, 180);
        planetName.setAlignment(Pos.CENTER);

        Label coordinates = createLabel("(" + region.getUniX() + ", "
                + (600 - region.getUniY()) + ")", 210, 30, 15, Color.YELLOW, 180);
        coordinates.setAlignment(Pos.CENTER);

        int dist = (int) Math.sqrt((currentSystem.getUniX() - region.getUniX())
                * (currentSystem.getUniX() - region.getUniX())
                + (currentSystem.getUniY() - region.getUniY())
                * (currentSystem.getUniY() - region.getUniY()));
        Label distanceLabel = createLabel("Distance to System: " + Integer.toString(dist),
                210, 60, 15, Color.YELLOW, 180);
        distanceLabel.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Tech Level: ", 0, 100, 20, Color.YELLOW, 125);

        Label t3 = createLabel("Government Type: ", 0, 150, 20, Color.YELLOW, 175);

        Label t5 = createLabel("Police Presence: ", 0, 200, 20, Color.YELLOW, 175);

        Label t2;
        Label t4;
        Label t6;
        if (visitedRegions.contains(region)) {
            t2 = createLabel(Integer.toString(region.getTechLevel()), 125, 100, 20, Color.RED, 50);
            t4 = createLabel(region.getGovernment(), 175, 150, 20, Color.RED, 175);
            t6 = createLabel(Integer.toString(region.getPolicePresence()),
                    175, 200, 20, Color.RED, 50);
        } else {
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

        Button shipyardButton = createButton(300, 525, 150, 50, Color.YELLOW, "Shipyard");
        shipyardButton.setOnMouseEntered(e -> shipyardButton.setTextFill(Color.RED));
        shipyardButton.setOnMouseExited(e -> shipyardButton.setTextFill(Color.YELLOW));

        Button tm;
        if (currentSystem == region) {
            tm = createButton(450, 525, 150, 50, Color.YELLOW, "Market");
            tm.setOnMouseEntered(e -> tm.setTextFill(Color.RED));
            tm.setOnMouseExited(e -> tm.setTextFill(Color.YELLOW));
        } else {
            tm = createButton(450, 525, 150, 50, Color.YELLOW, "Travel");
            tm.setOnMouseEntered(e -> tm.setTextFill(Color.RED));
            tm.setOnMouseExited(e -> tm.setTextFill(Color.YELLOW));
        }

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, distanceLabel, coordinates, t1, t2, t3, t4,
                t5, t6, tabLine, vertLine1, vertLine2, vertLine3, backButton, commandButton,
                shipyardButton, tm);

        backButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        shipyardButton.setOnAction(e -> {
            if  (region == currentSystem) {
                window.setScene(createShipyard(window, region));
            } else {
                System.out.println("Can't buy from region you are not in!");
            }
        });

        commandButton.setOnAction(e -> {
            Group grp4 = getCommanderStatus(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        tm.setOnAction(e -> {
            if (currentSystem != region) {
                int distance = (int) (getDistance(region.getSubX(), region.getSubY(), 300, 300));
                if (distance <= player.getFuel()) {
                    if ((int) (Math.random() * 3) == 2) {
                        s2 = new Ship("Gnat", 15, 1, 1, 500);
                        s2.setSubX(500);
                        s2.setSubY(300);
                        player.getShip().setSubX(25);
                        player.getShip().setSubY(300);
                        shipAnimation.start();
                        shootAnimation.start();
                        window.setScene(createFightChoice(window));
                        player.changeFuel((-1) * distance);
                        setCurrRegion(region);
                        resetPoints();
                        visitedRegions.add(region);
                    } else {
                        player.changeFuel((-1) * distance);
                        setCurrRegion(region);
                        resetPoints();
                        visitedRegions.add(region);
                        Group grp5 = getTravelChart(window);
                        Scene s5 = new Scene(grp5, 600, 600);
                        window.setScene(s5);
                    }
                } else {
                    System.out.println("Outside fuel range!");
                }
            } else {
                Scene s4 = createMarket(window, region);
                window.setScene(s4);
            }
        });
        return new Scene(grp, 600, 600);
    }

    private Scene createShipyard(Stage window, Region region) {
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Shipyard", 175, 0, 25,
                Color.YELLOW, 250);
        planetName.setAlignment(Pos.CENTER);

        Label t1 = createLabel("Current Ship Type: ", 0, 50, 20, Color.YELLOW, 250);
        Label t2 = createLabel(player.getShip().getType(), 250, 50, 20, Color.RED, 100);
        Label w1 = createLabel("Current Weapon Level: ", 0, 100, 20, Color.YELLOW, 250);
        Label w2 = createLabel(String.valueOf(player.getShip().getWeaponLevel()), 250, 100, 20,
                Color.RED, 100);
        Label w3 = createLabel("Current Cargo Holds: ", 0, 150, 20, Color.YELLOW, 250);
        Label w4 = createLabel(String.valueOf(player.getShip().getCargoHolds()), 250, 150, 20,
                Color.RED, 100);
        Label t3 = createLabel("Current Shield Level: ", 0, 200, 20, Color.YELLOW, 250);
        Label t4 = createLabel(String.valueOf(player.getShip().getShieldLevel()), 250, 200, 20,
                Color.RED, 175);
        Label t5 = createLabel("Current Fuel Level: ", 0, 250, 20, Color.YELLOW, 250);
        Label t6 = createLabel(String.valueOf(player.getFuel()), 250, 250, 20, Color.RED, 50);
        Label t7 = createLabel("Current Credits: ", 0, 300, 20, Color.YELLOW, 250);
        Label t8 = createLabel(String.valueOf(player.getCredits()), 250, 300, 20, Color.RED, 50);

        Label fuel = createLabel("Increase fuel by: ", 0, 350, 20, Color.BLUE, 150);
        Slider fuelSlider = createSlider(0, 300, 0, 200, true, 30, 20);
        fuelSlider.setLayoutX(150);
        fuelSlider.setLayoutY(360);
        fuelSlider.setBlockIncrement(100);
        fuelSlider.setSnapToTicks(true);

        Label fuelLabel = createLabel("0", 350, 350, 20, Color.BLUE, 35);
        Button addFuel = createButton(385, 340, 215, 50, Color.BLUE, "PURCHASE FOR 0");
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
            } else {
                System.out.println("Not enough money");
                fuelSlider.setValue(0);
            }
        });

        Label shield = createLabel("Increase shield: ", 0, 400, 20, Color.BLUE, 150);
        Slider shieldSlider = createSlider(0, 5, 0, 200, true, 1, 0);
        shieldSlider.setLayoutX(150);
        shieldSlider.setLayoutY(410);
        shieldSlider.setBlockIncrement(1);
        shieldSlider.setSnapToTicks(true);
        Label shieldLabel = createLabel("0", 350, 400, 20, Color.BLUE, 35);
        Button addShield = createButton(385, 390, 215, 50, Color.BLUE, "PURCHASE FOR 0");
        //addFuel.setAlignment(Pos.BASELINE_LEFT);
        addShield.setOnMouseEntered(e -> addShield.setTextFill(Color.RED));
        addShield.setOnMouseExited(e -> addShield.setTextFill(Color.BLUE));
        shieldSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                shieldLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = t1.intValue() - number.intValue();
                points[1].setValue(points[1].getValue() + difference);
                addShield.setText("PURCHASE FOR " + t1.intValue() * 100);
            }
        });
        addShield.setOnAction(e -> {
            int amountShieldAdded = (int) (shieldSlider.getValue());
            if (player.getCredits() > amountShieldAdded * 100) {
                player.changeShield(amountShieldAdded);
                player.setCredits(player.getCredits() - amountShieldAdded * 100);
                window.setScene(createShipyard(window, region));
            } else {
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
        Button buyShipButton = createButton(300, 525, 150, 50, Color.YELLOW, "Buy Ship");
        buyShipButton.setOnMouseEntered(e -> buyShipButton.setTextFill(Color.RED));
        buyShipButton.setOnMouseExited(e -> buyShipButton.setTextFill(Color.YELLOW));
        Button travelChartButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChartButton.setOnMouseEntered(e -> travelChartButton.setTextFill(Color.RED));
        travelChartButton.setOnMouseExited(e -> travelChartButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, w1, w2, w3, w4, t3, t4,
                t5, t6, t7, t8, tabLine, fuel, fuelSlider,
                fuelLabel, addFuel, shield, shieldSlider, shieldLabel, addShield,
                vertLine1, vertLine2, vertLine3, backButton, commandButton, buyShipButton,
                travelChartButton);

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
            } else {
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
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Shipyard", 175, 0, 25,
                Color.YELLOW, 250);
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
                vertLine1, vertLine2, vertLine3, backButton, purchaseButton, cost1, cost2, cost3,
                cost4, shipBox, shipBoxDescription, shipyardButton, travelChartButton);

        backButton.setOnAction(e -> {
            window.setScene(createShipyard(window, region));
        });

        shipyardButton.setOnAction(e -> {
            window.setScene(createShipyard(window, region));
        });

        purchaseButton.setOnAction(e -> {
            String shipName = shipBox.getValue();
            for (int i = 0; i < player.getShips().size(); i++) {
                if (shipName.equals(player.getShips().get(i).getType())
                        && !shipName.equals(player.getShip().getType())) {
                    if (player.getCredits() > player.getShips().get(i).getPrice()) {
                        player.setShip(player.getShips().get(i));
                        player.setCredits(player.getCredits()
                                - player.getShips().get(i).getPrice());
                        window.setScene(createShipyard(window, region));
                    }
                }
            }
        });

        travelChartButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        return new Scene(grp, 600, 600);
    }

    public Scene createFightChoice(Stage window) {
        ImageView background = createImage("lightSpeed.jpg", 0, 0, 600, 600);

        Label pirateInfo = createLabel("You have run into a pirate!", 50, 250, 40, Color.RED, 500);
        pirateInfo.setAlignment(Pos.CENTER);

        Button fleeButton = createButton(150, 450, 100, 50, Color.RED, "FLEE");
        fleeButton.setOnMouseEntered(e -> fleeButton.setTextFill(Color.YELLOW));
        fleeButton.setOnMouseExited(e -> fleeButton.setTextFill(Color.RED));

        Button fightButton = createButton(350, 450, 100, 50, Color.RED, "FIGHT");
        fightButton.setOnMouseEntered(e -> fightButton.setTextFill(Color.YELLOW));
        fightButton.setOnMouseExited(e -> fightButton.setTextFill(Color.RED));
        fightButton.setFocusTraversable(false);

        fleeButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        fightButton.setOnAction(e -> {
            System.out.println("x" + player.getShip().getSubY());
            startFight(window, player.getShip(), s2);
        });

        Group grp = new Group();
        grp.getChildren().addAll(background, pirateInfo, fleeButton, fightButton);
        Scene scene = new Scene(grp, 600, 600);
        window.setScene(scene);
        return scene;
    }

    private final AnimationTimer shipAnimation = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if (lastUpdateTime.get() > 0) {
                final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
                Ship s1 = SpaceTrader.this.getPlayer().getShip();
                s1.setSubY(s1.getSubY() + shipVelocity);
            }
            lastUpdateTime.set(timestamp);
        }
    };

    private final AnimationTimer shootAnimation = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if (lastUpdateTime.get() > 0) {
                if (isShooting) {
                    Ship s1 = SpaceTrader.this.getPlayer().getShip();
                    int xc = s1.getSubX() + s1.getSize();
                    int yc = s1.getSubY() + s1.getSize() / 2 - 10;
                    int speed = 50;
                    int power = player.getShip().getWeaponLevel() * 3;
                    shots.add(new Shot(xc, yc, speed, 0, power));
                    s1.changeAmmo(-1);
                }
                final double elapsedSeconds = (timestamp - lastUpdateTime.get()) / 1_000_000_000.0;
            }
            lastUpdateTime.set(timestamp);
        }
    };

    public void startFight(Stage stage, Ship s1, Ship s2) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.12), ev -> {
            if (checkBothHealth(s1, s2)) {
                boolean hit1 = false;
                boolean hit2 = false;
                ImageView explosion = null;
                ImageView explosion2 = null;
                Label toggle = null;
                ArrayList<ImageView> stageShots = new ArrayList<>();
                Label fight = createLabel("FIGHT", 175, 0, 30, Color.RED, 150);
                fight.setAlignment(Pos.CENTER);
                if (singleFire) {
                    toggle = createLabel("Press z to toggle autofire", 150, 75, 25, Color.RED, 300);
                } else {
                    toggle = createLabel("Press z to toggle singlefire",
                        150, 75, 25, Color.RED, 300);
                }
                fight.setAlignment(Pos.CENTER);
                ImageView background = createImage("plainFightBlack.jpg", 0, 0, 600, 600);
                ImageView ship1 = createImage(s1.getImage(), s1.getSubX(), s1.getSubY(),
                    s1.getSize(), s1.getSize());
                ship1.setRotate(90);
                ImageView ship2 = createImage(s2.getImage(), s2.getSubX(), s2.getSubY(),
                    s2.getSize(), s2.getSize());
                ship2.setRotate(270);
                Label t1 = createLabel("Player Health: ", 0, 0, 20, Color.YELLOW, 150);
                Label t2 = createLabel(String.valueOf(s1.getHealth()), 150, 0, 20, Color.RED, 50);
                Label t3 = createLabel("Opponent Health: ", 390, 0, 20, Color.YELLOW, 170);
                Label t4 = createLabel(String.valueOf(s2.getHealth()), 560, 0, 20, Color.RED, 40);
                Label t5 = createLabel("Player Ammo: ", 0, 50, 20, Color.YELLOW, 150);
                Label t6 = createLabel(String.valueOf(s1.getAmmo()), 150, 50, 20, Color.RED, 50);

                Button fleeButton = createButton(250, 550, 100, 50, Color.RED, "FLEE");
                fleeButton.setOnMouseEntered(e -> fleeButton.setTextFill(Color.YELLOW));
                fleeButton.setOnMouseExited(e -> fleeButton.setTextFill(Color.RED));
                fleeButton.setFocusTraversable(false);

                fleeButton.setOnAction(e -> {
                    System.out.println("hi");
                    Group grp4 = getTravelChart(window);
                    Scene s4 = new Scene(grp4, 600, 600);
                    shootAnimation.stop();
                    shipAnimation.stop();
                    timeline.stop();
                    window.setScene(s4);
                });

                for (int i = 0; i < shots.size(); i++) {
                    int xc = shots.get(i).getX();
                    int yc = shots.get(i).getY();
                    ImageView shotImage = createImage("spaceBullet.png", xc, yc, 50, 30);
                    if (shots.get(i).getDirection() == 1) {
                        shotImage.setRotate(180);
                    }
                    stageShots.add(shotImage);
                    if (shots.get(i).getDirection() == 1) {
                        if (withinRange(shots.get(i).getX(), s1.getSubX(), s1.getSize())
                                && withinRange(shots.get(i).getY(), s1.getSubY() + 30,
                                    s1.getSize() / 2)) {
                            s1.changeHealth(-(shots.get(i).getPower()));
                            explosion = createImage("shipExplosion.png", s1.getSubX(), s1.getSubY(),
                                s1.getSize(), 50);
                            shots.remove(i);
                            hit1 = true;
                            System.out.println("s1: " + s1.getHealth());
                        }
                    } else {
                        if (withinRange(shots.get(i).getX(), s2.getSubX(), s2.getSize())
                                && withinRange(shots.get(i).getY(), s2.getSubY() + 30,
                                    s2.getSize() / 2)) {
                            s2.changeHealth(-(shots.get(i).getPower()));
                            explosion2 = createImage("shipExplosion.png", s2.getSubX(),
                                s2.getSubY(), s2.getSize(), 50);
                            shots.remove(i);
                            hit2 = true;
                            System.out.println("s2: " + s2.getHealth());
                        }
                    }
                }
                Group grp = new Group();
                grp.getChildren().addAll(background, ship1, ship2, fight, toggle,
                        fleeButton, t1, t2, t3, t4, t5, t6);
                for (int i = 0; i < stageShots.size(); i++) {
                    grp.getChildren().add(stageShots.get(i));
                }
                if (hit1) {
                    grp.getChildren().add(explosion);
                }
                if (hit2) {
                    grp.getChildren().add(explosion2);
                }
                Scene scene = new Scene(grp, 600, 600);
                scene.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.W) {
                        shipVelocity = -5;
                    }
                    if (e.getCode() == KeyCode.S) {
                        shipVelocity = 5;
                    }
                    if (e.getCode() == KeyCode.Z) {
                        singleFire = !singleFire;
                    }
                    if (e.getCode() == KeyCode.SPACE) {
                        if (singleFire) {
                            if (timeCount % 2 == 0) {
                                System.out.println(s1.getAmmo());
                                if (s1.getAmmo() > 0) {
                                    int xc = s1.getSubX() + s1.getSize();
                                    int yc = s1.getSubY() + s1.getSize() / 2 - 10;
                                    int speed = 40;
                                    int power = player.getShip().getWeaponLevel() * 3;
                                    shots.add(new Shot(xc, yc, speed, 0, power));
                                    s1.changeAmmo(-1);
                                }
                            }
                        } else {
                            if (s1.getAmmo() > 0) {
                                isShooting = true;
                            }
                        }
                    }
                });

                scene.setOnKeyReleased(e -> {
                    if (e.getCode() == KeyCode.W) {
                        shipVelocity = 0;
                    }
                    if (e.getCode() == KeyCode.S) {
                        shipVelocity = 0;
                    }
                    if (e.getCode() == KeyCode.SPACE) {
                        isShooting = false;
                    }
                });
                stage.setScene(scene);
                //System.out.println("Shot");
                for (int i = 0; i < shots.size(); i++) {
                    if (shots.get(i).getX() > 600 || shots.get(i).getX() < 0) {
                        shots.remove(i);
                    }
                    if (shots.size() > i) {
                        //System.out.println(shots.get(i).getX());
                        //System.out.println(shots.get(i).getY());
                        if (shots.get(i).getDirection() == 0) {
                            shots.get(i).setX(shots.get(i).getX() + shots.get(i).getSpeed());
                        } else {
                            shots.get(i).setX(shots.get(i).getX() - shots.get(i).getSpeed());
                        }
                    }
                }
                if (SpaceTrader.this.getPlayer().getShip().getSubY() < s2.getSubY()) {
                    s2.setSubY(s2.getSubY() - 5);
                }
                if (SpaceTrader.this.getPlayer().getShip().getSubY() > s2.getSubY()) {
                    s2.setSubY(s2.getSubY() + 5);
                }
                if (timeCount % 5 == 0) {
                    int xc = s2.getSubX() - s2.getSize();
                    int yc = s2.getSubY() + s2.getSize() / 2 - 10;
                    int speed = 40;
                    int power = player.getShip().getWeaponLevel() * 3;
                    shots.add(new Shot(xc, yc, speed, 1, power));
                }
                timeCount++;
            } else {
                timeline.stop();
                shootAnimation.stop();
                shipVelocity = 0;
                isShooting = false;
                shipAnimation.stop();
                ImageView background = createImage("plainFightBlack.jpg", 0, 0, 600, 600);
                Label won = createLabel("YOU WON", 175, 0, 25, Color.YELLOW, 250);
                Label lost = createLabel("YOU LOST", 175, 0, 25, Color.YELLOW, 250);
                won.setAlignment(Pos.CENTER);
                lost.setAlignment(Pos.CENTER);

                Button travelButton = createButton(370, 525, 230, 50,
                    Color.YELLOW, "Continue Traveling!");
                travelButton.setOnMouseEntered(e -> travelButton.setTextFill(Color.RED));
                travelButton.setOnMouseExited(e -> travelButton.setTextFill(Color.YELLOW));
                travelButton.setOnAction(e -> {
                    Group grp5 = getTravelChart(window);
                    Scene s5 = new Scene(grp5, 600, 600);
                    window.setScene(s5);
                });
                Group fightFinished = new Group();
                fightFinished.getChildren().add(background);
                if (s1.getHealth() > 0) {
                    shots.clear();
                    s1.setSubX(0);
                    s1.setSubY(0);
                    shipAnimation.stop();
                    shootAnimation.stop();
                    timeline.stop();
                    fightFinished.getChildren().addAll(won, travelButton);
                } else {
                    fightFinished.getChildren().add(lost);
                }
                Scene finalScene = new Scene(fightFinished, 600, 600);
                stage.setScene(finalScene);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private boolean checkBothHealth(Ship s1, Ship s2) {
        if (s1.getHealth() > 0 && s2.getHealth() > 0) {
            return true;
        }
        return false;
    }

    private double getDistance(int x1, int y1, int x2, int y2) {
        double distance = Math.abs((double) ((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
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

    private Scene createMarket(Stage window, Region region) {
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Buy Market", 150, 0, 25,
            Color.YELLOW, 250);
        planetName.setAlignment(Pos.CENTER);

        Label credits = createLabel("Credits: " + player.getCredits(), 450, 5, 20,
            Color.GREEN, 250);

        Market market = region.getMarket();
        int tickUnit;
        Label purchase = createLabel("(0)", 205, 560, 20, Color.YELLOW, 150);

        Label t1 = createLabel("Products", 20, 50, 20, Color.BLUE, 100);
        t1.setStyle("-fx-underline: true");
        Label t2 = createLabel("Available", 150, 50, 20, Color.BLUE, 100);
        t2.setStyle("-fx-underline: true");
        Label z1 = createLabel("Price", 270, 50, 20, Color.BLUE, 50);
        z1.setStyle("-fx-underline: true");
        Label z2 = createLabel("Select Amount to Purchase", 350, 50, 20, Color.BLUE, 270);
        z2.setStyle("-fx-underline: true");

        Label w1 = createLabel("Water", 20, 100, 20, Color.YELLOW, 100);
        Label w2 = createLabel(String.valueOf(market.getQuantity("Water")),
                160, 100, 20, Color.RED, 100);
        Label w3 = createLabel(String.valueOf((int) market.getPrice("Water")),
                280, 100, 20, Color.RED, 100);
        Slider waterSlider = new Slider(0, market.getQuantity("Water"), 0);
        waterSlider.setLayoutX(350);
        waterSlider.setLayoutY(110);
        waterSlider.setPrefWidth(200);
        waterSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Water") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        waterSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        waterSlider.setSnapToTicks(true);
        waterSlider.setStyle("-fx-control-inner-background: red;");
        Label waterLabel = createLabel("0", 550, 100, 20, Color.RED, 35);
        waterSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                waterLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue()
                    - number.intValue()) * (int) market.getPrice("Water");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });


        Label fur1 = createLabel("Furs", 20, 150, 20, Color.YELLOW, 100);
        Label fur2 = createLabel(String.valueOf(market.getQuantity("Furs")),
                160, 150, 20, Color.RED, 100);
        Label fur3 = createLabel(String.valueOf((int) market.getPrice("Furs")),
                280, 150, 20, Color.RED, 100);
        Slider furSlider = new Slider(0, market.getQuantity("Furs"), 0);
        furSlider.setLayoutX(350);
        furSlider.setLayoutY(160);
        furSlider.setPrefWidth(200);
        furSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Furs") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        furSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        furSlider.setSnapToTicks(true);
        furSlider.setStyle("-fx-control-inner-background: red;");
        Label furLabel = createLabel("0", 550, 150, 20, Color.RED, 35);
        furSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                furLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Furs");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label food1 = createLabel("Food", 20, 200, 20, Color.YELLOW, 100);
        Label food2 = createLabel(String.valueOf(market.getQuantity("Food")),
                160, 200, 20, Color.RED, 100);
        Label food3 = createLabel(String.valueOf((int) market.getPrice("Food")),
                280, 200, 20, Color.RED, 100);
        Slider foodSlider = new Slider(0, market.getQuantity("Food"), 0);
        foodSlider.setLayoutX(350);
        foodSlider.setLayoutY(210);
        foodSlider.setPrefWidth(200);
        foodSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Food") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        foodSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        foodSlider.setSnapToTicks(true);
        foodSlider.setStyle("-fx-control-inner-background: red;");
        Label foodLabel = createLabel("0", 550, 200, 20, Color.RED, 35);
        foodSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                foodLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Food");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label o1 = createLabel("Ore", 20, 250, 20, Color.YELLOW, 100);
        Label o2 = createLabel(String.valueOf(market.getQuantity("Ore")),
                160, 250, 20, Color.RED, 100);
        Label o3 = createLabel(String.valueOf((int) market.getPrice("Ore")),
                280, 250, 20, Color.RED, 100);
        Slider oreSlider = new Slider(0, market.getQuantity("Ore"), 0);
        oreSlider.setLayoutX(350);
        oreSlider.setLayoutY(260);
        oreSlider.setPrefWidth(200);
        oreSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Ore") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        oreSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        oreSlider.setSnapToTicks(true);
        oreSlider.setStyle("-fx-control-inner-background: red;");
        Label oreLabel = createLabel("0", 550, 250, 20, Color.RED, 35);
        oreSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                oreLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue()) * (int) market.getPrice("Ore");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label g1 = createLabel("Games", 20, 300, 20, Color.YELLOW, 100);
        Label g2 = createLabel(String.valueOf(market.getQuantity("Games")),
                160, 300, 20, Color.RED, 100);
        Label g3 = createLabel(String.valueOf((int) market.getPrice("Games")),
                280, 300, 20, Color.RED, 100);
        Slider gameSlider = new Slider(0, market.getQuantity("Games"), 0);
        gameSlider.setLayoutX(350);
        gameSlider.setLayoutY(310);
        gameSlider.setPrefWidth(200);
        gameSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Games") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        gameSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        gameSlider.setSnapToTicks(true);
        gameSlider.setStyle("-fx-control-inner-background: red;");
        Label gameLabel = createLabel("0", 550, 300, 20, Color.RED, 35);
        gameSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                gameLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Games");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label fire1 = createLabel("Firearms", 20, 350, 20, Color.YELLOW, 100);
        Label fire2 = createLabel(String.valueOf(market.getQuantity("Firearms")),
                160, 350, 20, Color.RED, 100);
        Label fire3 = createLabel(String.valueOf((int) market.getPrice("Firearms")),
                280, 350, 20, Color.RED, 100);
        Slider fireSlider = new Slider(0, market.getQuantity("Firearms"), 0);
        fireSlider.setLayoutX(350);
        fireSlider.setLayoutY(360);
        fireSlider.setPrefWidth(200);
        fireSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Firearms") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        fireSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        fireSlider.setSnapToTicks(true);
        fireSlider.setStyle("-fx-control-inner-background: red;");
        Label fireLabel = createLabel("0", 550, 350, 20, Color.RED, 35);
        fireSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                fireLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Firearms");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label drug1 = createLabel("Narcotics", 20, 400, 20, Color.YELLOW, 100);
        Label drug2 = createLabel(String.valueOf(market.getQuantity("Narcotics")),
                160, 400, 20, Color.RED, 100);
        Label drug3 = createLabel(String.valueOf((int) market.getPrice("Narcotics")),
                280, 400, 20, Color.RED, 100);
        Slider drugSlider = new Slider(0, market.getQuantity("Narcotics"), 0);
        drugSlider.setLayoutX(350);
        drugSlider.setLayoutY(410);
        drugSlider.setPrefWidth(200);
        drugSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Narcotics") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        drugSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        drugSlider.setSnapToTicks(true);
        drugSlider.setStyle("-fx-control-inner-background: red;");
        Label drugLabel = createLabel("0", 550, 400, 20, Color.RED, 35);
        drugSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                drugLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Narcotics");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
            }
        });

        Label r1 = createLabel("Robots", 20, 450, 20, Color.YELLOW, 100);
        Label r2 = createLabel(String.valueOf(market.getQuantity("Robots")),
                160, 450, 20, Color.RED, 100);
        Label r3 = createLabel(String.valueOf((int) market.getPrice("Robots")),
                280, 450, 20, Color.RED, 100);
        Slider robotSlider = new Slider(0, market.getQuantity("Robots"), 0);
        robotSlider.setLayoutX(350);
        robotSlider.setLayoutY(460);
        robotSlider.setPrefWidth(200);
        robotSlider.setShowTickMarks(false);
        tickUnit = market.getQuantity("Robots") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        robotSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        robotSlider.setSnapToTicks(true);
        robotSlider.setStyle("-fx-control-inner-background: red;");
        Label robotLabel = createLabel("0", 550, 450, 20, Color.RED, 35);
        robotSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                robotLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Robots");
                difference = (int) (difference * (1 - (.02 * points[3].getValue())));
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
                if (removeQuote(purchase.getText()) < 20) {
                    purchase.textProperty().setValue("(0)");
                }
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

        Button sellButton = createButton(300, 525, 150, 50, Color.YELLOW, "Sell Market");
        sellButton.setOnMouseEntered(e -> sellButton.setTextFill(Color.RED));
        sellButton.setOnMouseExited(e -> sellButton.setTextFill(Color.YELLOW));

        Button travelChartButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChartButton.setOnMouseEntered(e -> travelChartButton.setTextFill(Color.RED));
        travelChartButton.setOnMouseExited(e -> travelChartButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, w1, w2, w3, waterSlider,
                waterLabel, z1, z2, fur1, fur2, fur3, furSlider, furLabel,
                food1, food2, food3, foodSlider, foodLabel, o1, o2, o3, oreSlider, oreLabel,
                g1, g2, g3, gameSlider, gameLabel, fire1, fire2, fire3, fireSlider, fireLabel,
                drug1, drug2, drug3, drugSlider, drugLabel, r1, r2, r3, robotSlider, robotLabel,
                tabLine, vertLine1, vertLine2, vertLine3, purchase, credits, backButton,
                commandButton, sellButton, travelChartButton);

        backButton.setOnAction(e -> {
            window.setScene(createRegionScene(window, region));
        });

        sellButton.setOnAction(e -> {
            window.setScene(createSellMarket(window, region));
        });

        commandButton.setOnAction(e -> {
            int amountWaterAdded = (int) (waterSlider.getValue());
            int amountFurAdded = (int) (furSlider.getValue());
            System.out.println(amountFurAdded);
            int amountOreAdded = (int) (oreSlider.getValue());
            int amountFoodAdded = (int) (foodSlider.getValue());
            int amountGameAdded = (int) (gameSlider.getValue());
            int amountFireAdded = (int) (fireSlider.getValue());
            int amountDrugAdded = (int) (drugSlider.getValue());
            int amountRobotAdded = (int) (robotSlider.getValue());
            int totalAdd = amountWaterAdded + amountFurAdded + amountOreAdded + amountFoodAdded
                    + amountGameAdded + amountFireAdded + amountDrugAdded + amountRobotAdded;
            int currentCapacity = player.getShip().getCurrentCapacity();
            if (player.getCredits() > removeQuote(purchase.getText())
                    && currentCapacity >= totalAdd) {
                player.getShip().changeProductQuantity("Water", amountWaterAdded);
                market.changeProductQuantity("Water", -amountWaterAdded);
                player.getShip().changeProductQuantity("Furs", amountFurAdded);
                market.changeProductQuantity("Furs", -amountFurAdded);
                player.getShip().changeProductQuantity("Ore", amountOreAdded);
                market.changeProductQuantity("Ore", -amountOreAdded);
                player.getShip().changeProductQuantity("Food", amountFoodAdded);
                market.changeProductQuantity("Food", -amountFoodAdded);
                player.getShip().changeProductQuantity("Games", amountGameAdded);
                market.changeProductQuantity("Games", -amountGameAdded);
                player.getShip().changeProductQuantity("Firearms", amountFireAdded);
                market.changeProductQuantity("Firearms", -amountFireAdded);
                player.getShip().changeProductQuantity("Narcotics", amountDrugAdded);
                market.changeProductQuantity("Narcotics", -amountDrugAdded);
                player.getShip().changeProductQuantity("Robots", amountRobotAdded);
                market.changeProductQuantity("Robots", -amountRobotAdded);
                player.changeCredits(-removeQuote(purchase.getText()));
                window.setScene(createMarket(window, region));
            } else if (player.getCredits() < removeQuote(purchase.getText())) {
                System.out.println("Not enough money");
                waterSlider.setValue(0);
                furSlider.setValue(0);
                oreSlider.setValue(0);
                foodSlider.setValue(0);
                gameSlider.setValue(0);
                fireSlider.setValue(0);
                drugSlider.setValue(0);
                robotSlider.setValue(0);
            } else {
                System.out.println("Not enough space in cargo holds, you can only add: "
                    + player.getShip().getCurrentCapacity());
                waterSlider.setValue(0);
                furSlider.setValue(0);
                oreSlider.setValue(0);
                foodSlider.setValue(0);
                gameSlider.setValue(0);
                fireSlider.setValue(0);
                drugSlider.setValue(0);
                robotSlider.setValue(0);
            }
        });


        travelChartButton.setOnAction(e -> {
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        return new Scene(grp, 600, 600);
    }

    private Scene createSellMarket(Stage window, Region region) {
        ImageView background = createImage("regionBackground.jpg", 0, 0, 600, 600);

        Label planetName = createLabel(region.getName() + " Sell Market", 150, 0, 25,
            Color.YELLOW, 250);
        planetName.setAlignment(Pos.CENTER);

        Label credits = createLabel("Credits: " + player.getCredits(), 450, 5, 20,
            Color.GREEN, 250);

        Market market = region.getMarket();
        market.makeSell();

        int tickUnit;
        Label purchase = createLabel("(0)", 355, 560, 20, Color.YELLOW, 150);

        Label t1 = createLabel("Products", 20, 50, 20, Color.BLUE, 100);
        t1.setStyle("-fx-underline: true");
        Label t2 = createLabel("Cargo", 140, 50, 20, Color.BLUE, 60);
        t2.setStyle("-fx-underline: true");
        Label z1 = createLabel("Sell Price", 250, 50, 20, Color.BLUE, 100);
        z1.setStyle("-fx-underline: true");
        Label z2 = createLabel("Select Amount to Sell", 350, 50, 20, Color.BLUE, 270);
        z2.setStyle("-fx-underline: true");

        Label w1 = createLabel("Water", 20, 100, 20, Color.YELLOW, 100);
        Label w2 = createLabel(String.valueOf(player.getShip().getQuantity("Water")),
                160, 100, 20, Color.RED, 100);
        Label w3 = createLabel(String.valueOf((int) market.getPrice("Water")),
                280, 100, 20, Color.RED, 100);
        Slider waterSlider = new Slider(0, player.getShip().getQuantity("Water"), 0);
        waterSlider.setLayoutX(350);
        waterSlider.setLayoutY(110);
        waterSlider.setPrefWidth(200);
        waterSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Water") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        waterSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        waterSlider.setSnapToTicks(true);
        waterSlider.setStyle("-fx-control-inner-background: red;");
        Label waterLabel = createLabel("0", 550, 100, 20, Color.RED, 35);
        waterSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                waterLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Water");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });


        Label fur1 = createLabel("Furs", 20, 150, 20, Color.YELLOW, 100);
        Label fur2 = createLabel(String.valueOf(player.getShip().getQuantity("Furs")),
                160, 150, 20, Color.RED, 100);
        Label fur3 = createLabel(String.valueOf((int) market.getPrice("Furs")),
                280, 150, 20, Color.RED, 100);
        Slider furSlider = new Slider(0, player.getShip().getQuantity("Furs"), 0);
        furSlider.setLayoutX(350);
        furSlider.setLayoutY(160);
        furSlider.setPrefWidth(200);
        furSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Furs") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        furSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        furSlider.setSnapToTicks(true);
        furSlider.setStyle("-fx-control-inner-background: red;");
        Label furLabel = createLabel("0", 550, 150, 20, Color.RED, 35);
        furSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                furLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Furs");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label food1 = createLabel("Food", 20, 200, 20, Color.YELLOW, 100);
        Label food2 = createLabel(String.valueOf(player.getShip().getQuantity("Food")),
                160, 200, 20, Color.RED, 100);
        Label food3 = createLabel(String.valueOf((int) market.getPrice("Food")),
                280, 200, 20, Color.RED, 100);
        Slider foodSlider = new Slider(0, player.getShip().getQuantity("Food"), 0);
        foodSlider.setLayoutX(350);
        foodSlider.setLayoutY(210);
        foodSlider.setPrefWidth(200);
        foodSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Food") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        foodSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        foodSlider.setSnapToTicks(true);
        foodSlider.setStyle("-fx-control-inner-background: red;");
        Label foodLabel = createLabel("0", 550, 200, 20, Color.RED, 35);
        foodSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                foodLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Food");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label o1 = createLabel("Ore", 20, 250, 20, Color.YELLOW, 100);
        Label o2 = createLabel(String.valueOf(player.getShip().getQuantity("Ore")),
                160, 250, 20, Color.RED, 100);
        Label o3 = createLabel(String.valueOf((int) market.getPrice("Ore")),
                280, 250, 20, Color.RED, 100);
        Slider oreSlider = new Slider(0, player.getShip().getQuantity("Ore"), 0);
        oreSlider.setLayoutX(350);
        oreSlider.setLayoutY(260);
        oreSlider.setPrefWidth(200);
        oreSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Ore") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        oreSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        oreSlider.setSnapToTicks(true);
        oreSlider.setStyle("-fx-control-inner-background: red;");
        Label oreLabel = createLabel("0", 550, 250, 20, Color.RED, 35);
        oreSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                oreLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Ore");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label g1 = createLabel("Games", 20, 300, 20, Color.YELLOW, 100);
        Label g2 = createLabel(String.valueOf(player.getShip().getQuantity("Games")),
                160, 300, 20, Color.RED, 100);
        System.out.println(player.getShip().getQuantity("Games"));
        Label g3 = createLabel(String.valueOf((int) player.getShip().getPrice("Games")),
                280, 300, 20, Color.RED, 100);
        Slider gameSlider = new Slider(0, player.getShip().getQuantity("Games"), 0);
        gameSlider.setLayoutX(350);
        gameSlider.setLayoutY(310);
        gameSlider.setPrefWidth(200);
        gameSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Games") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        gameSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        gameSlider.setSnapToTicks(true);
        gameSlider.setStyle("-fx-control-inner-background: red;");
        Label gameLabel = createLabel("0", 550, 300, 20, Color.RED, 35);
        gameSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                gameLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Games");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label fire1 = createLabel("Firearms", 20, 350, 20, Color.YELLOW, 100);
        Label fire2 = createLabel(String.valueOf(player.getShip().getQuantity("Firearms")),
                160, 350, 20, Color.RED, 100);
        Label fire3 = createLabel(String.valueOf((int) market.getPrice("Firearms")),
                280, 350, 20, Color.RED, 100);
        Slider fireSlider = new Slider(0, player.getShip().getQuantity("Firearms"), 0);
        fireSlider.setLayoutX(350);
        fireSlider.setLayoutY(360);
        fireSlider.setPrefWidth(200);
        fireSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Firearms") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        fireSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        fireSlider.setSnapToTicks(true);
        fireSlider.setStyle("-fx-control-inner-background: red;");
        Label fireLabel = createLabel("0", 550, 350, 20, Color.RED, 35);
        fireSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                fireLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Firearms");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label drug1 = createLabel("Narcotics", 20, 400, 20, Color.YELLOW, 100);
        Label drug2 = createLabel(String.valueOf(player.getShip().getQuantity("Narcotics")),
                160, 400, 20, Color.RED, 100);
        Label drug3 = createLabel(String.valueOf((int) market.getPrice("Narcotics")),
                280, 400, 20, Color.RED, 100);
        Slider drugSlider = new Slider(0, player.getShip().getQuantity("Narcotics"), 0);
        drugSlider.setLayoutX(350);
        drugSlider.setLayoutY(410);
        drugSlider.setPrefWidth(200);
        drugSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Narcotics") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        drugSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        drugSlider.setSnapToTicks(true);
        drugSlider.setStyle("-fx-control-inner-background: red;");
        Label drugLabel = createLabel("0", 550, 400, 20, Color.RED, 35);
        drugSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                drugLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Narcotics");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
            }
        });

        Label r1 = createLabel("Robots", 20, 450, 20, Color.YELLOW, 100);
        Label r2 = createLabel(String.valueOf(player.getShip().getQuantity("Robots")),
                160, 450, 20, Color.RED, 100);
        Label r3 = createLabel(String.valueOf((int) market.getPrice("Robots")),
                280, 450, 20, Color.RED, 100);
        Slider robotSlider = new Slider(0, player.getShip().getQuantity("Robots"), 0);
        robotSlider.setLayoutX(350);
        robotSlider.setLayoutY(460);
        robotSlider.setPrefWidth(200);
        robotSlider.setShowTickMarks(false);
        tickUnit = player.getShip().getQuantity("Robots") / 10;
        if (tickUnit <= 0) {
            tickUnit = 1;
        }
        robotSlider.setMajorTickUnit(tickUnit);
        //waterSlider.setMinorTickCount(tickUnitd);
        //waterSlider.setBlockIncremenst(100);
        robotSlider.setSnapToTicks(true);
        robotSlider.setStyle("-fx-control-inner-background: red;");
        Label robotLabel = createLabel("0", 550, 450, 20, Color.RED, 35);
        robotSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue,
                                Number number, Number t1) {
                //System.out.println("hi");
                robotLabel.textProperty().setValue(String.valueOf(t1.intValue()));
                int difference = (t1.intValue() - number.intValue())
                    * (int) market.getPrice("Robots");
                purchase.textProperty().setValue("(" + (removeQuote(purchase.getText())
                    + difference) + ")");
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

        Button commandButton = createButton(150, 525, 150, 50, Color.YELLOW, "Buy Market");
        commandButton.setOnMouseEntered(e -> commandButton.setTextFill(Color.RED));
        commandButton.setOnMouseExited(e -> commandButton.setTextFill(Color.YELLOW));

        Button sellButton = createButton(300, 525, 150, 50, Color.YELLOW, "SELL");
        sellButton.setOnMouseEntered(e -> sellButton.setTextFill(Color.RED));
        sellButton.setOnMouseExited(e -> sellButton.setTextFill(Color.YELLOW));

        Button travelChartButton = createButton(450, 525, 150, 50, Color.YELLOW, "Travel Chart");
        travelChartButton.setOnMouseEntered(e -> travelChartButton.setTextFill(Color.RED));
        travelChartButton.setOnMouseExited(e -> travelChartButton.setTextFill(Color.YELLOW));

        Group grp = new Group();
        grp.getChildren().addAll(background, planetName, t1, t2, w1, w2, w3, waterSlider,
                waterLabel, z1, z2, fur1, fur2, fur3, furSlider, furLabel,
                food1, food2, food3, foodSlider, foodLabel, o1, o2, o3, oreSlider, oreLabel,
                g1, g2, g3, gameSlider, gameLabel, fire1, fire2, fire3, fireSlider, fireLabel,
                drug1, drug2, drug3, drugSlider, drugLabel, r1, r2, r3, robotSlider, robotLabel,
                tabLine, vertLine1, vertLine2, vertLine3, purchase, credits, backButton,
                commandButton, sellButton, travelChartButton);

        backButton.setOnAction(e -> {
            market.resetBuy();
            window.setScene(createRegionScene(window, region));
        });

        sellButton.setOnAction(e -> {
            int amountWaterSold = (int) (waterSlider.getValue());
            int amountFurSold = (int) (furSlider.getValue());
            int amountOreSold = (int) (oreSlider.getValue());
            int amountFoodSold = (int) (foodSlider.getValue());
            int amountGameSold = (int) (gameSlider.getValue());
            int amountFireSold = (int) (fireSlider.getValue());
            int amountDrugSold = (int) (drugSlider.getValue());
            int amountRobotSold = (int) (robotSlider.getValue());
            player.getShip().changeProductQuantity("Water", -amountWaterSold);
            market.changeProductQuantity("Water", amountWaterSold);
            player.getShip().changeProductQuantity("Furs", -amountFurSold);
            market.changeProductQuantity("Furs", amountFurSold);
            player.getShip().changeProductQuantity("Ore", -amountOreSold);
            market.changeProductQuantity("Ore", amountOreSold);
            player.getShip().changeProductQuantity("Food", -amountFoodSold);
            market.changeProductQuantity("Food", amountFoodSold);
            player.getShip().changeProductQuantity("Games", -amountGameSold);
            market.changeProductQuantity("Games", amountGameSold);
            player.getShip().changeProductQuantity("Firearms", -amountFireSold);
            market.changeProductQuantity("Firearms", amountFireSold);
            player.getShip().changeProductQuantity("Narcotics", -amountDrugSold);
            market.changeProductQuantity("Narcotics", amountDrugSold);
            player.getShip().changeProductQuantity("Robots", -amountRobotSold);
            market.changeProductQuantity("Robots", amountRobotSold);
            player.changeCredits(removeQuote(purchase.getText()));
            market.resetBuy();
            window.setScene(createSellMarket(window, region));
        });

        commandButton.setOnAction(e -> {
            market.resetBuy();
            window.setScene(createMarket(window, region));
        });


        travelChartButton.setOnAction(e -> {
            market.resetBuy();
            Group grp4 = getTravelChart(window);
            Scene s4 = new Scene(grp4, 600, 600);
            window.setScene(s4);
        });

        return new Scene(grp, 600, 600);
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

    public Slider createSlider(int min, int max, int val, int width, boolean ticks, int majorTick,
                               int minorTick) {
        Slider slider = new Slider(min, max, val);
        slider.setPrefWidth(width);
        slider.setShowTickMarks(ticks);
        slider.setMajorTickUnit(majorTick);
        slider.setMinorTickCount(minorTick);
        return slider;
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

    private int getLevel() {
        return value;
    }

    private void setSkillPoints() {
        skillPoints = 20 - 4 * difficultyLevel;
    }

    private void setSkillPoints(int points) {
        skillPoints = points;
    }

    private Player getPlayer() {
        return player;
    }

    private boolean withinRange(int n1, int n2, int range) {
        if (Math.abs(n1 - n2) <= range) {
            return true;
        }
        return false;
    }

    private int removeQuote(String s) {
        String newString = s.substring(1, s.length() - 1);
        return Integer.parseInt(newString);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
