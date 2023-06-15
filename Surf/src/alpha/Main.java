/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: alpha/Main.java 2015-03-11 buixuan.
 * ******************************************************/
package alpha;

import data.Data;
import engine.Engine;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import specifications.DataService;
import specifications.EngineService;
import specifications.ViewerService;
import tools.Position;
import userInterface.Background;
import userInterface.Player;
import userInterface.Viewer;

public class Main extends Application {
    static String[] arg;
    //---HARD-CODED-PARAMETERS---//
    private static String fileName = tools.Parameters.defaultParamFileName;
    //---VARIABLES---//
    private static DataService data;
    private static EngineService engine;
    private static ViewerService viewer;
    private static AnimationTimer timer;

    private static Background background;
    private static Player player;

    //---EXECUTABLE---//
    public static void main(String[] args) {
        //readArguments(args);

        data = new Data();
        engine = new Engine();
        viewer = new Viewer();
        player = new Player();

        background = new Background();
        ((Engine) engine).bindDataService(data);
        ((Viewer) viewer).bindReadService(data);
        ((Viewer) viewer).setPlayer(player);
        ((Engine) engine).setPlayer(player);
        player.bindReadService(data);
        background.bindEngineService(engine);

        data.init();
        engine.init();
        viewer.init();
        launch(args);
    }

    //---ARGUMENTS---//
    private static void readArguments(String[] args) {
        if (args.length > 0 && args[0].charAt(0) != '-') {
            System.err.println("Syntax error: use option -h for help.");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].charAt(0) == '-') {
                if (args[i + 1].charAt(0) == '-') {
                    System.err.println("Option " + args[i] + " expects an argument but received none.");
                    return;
                }
                switch (args[i]) {
                    case "-inFile":
                        fileName = args[i + 1];
                        break;
                    case "-h":
                        System.out.println("Options:");
                        System.out.println(" -inFile FILENAME: (UNUSED AT THE MOMENT) set file name for input parameters. Default name is" + tools.Parameters.defaultParamFileName + ".");
                        break;
                    default:
                        System.err.println("Unknown option " + args[i] + ".");
                        return;
                }
                i++;
            }
        }
    }

    @Override
    public void start(Stage stage) {


        final Scene scene = new Scene(((Viewer) viewer).getPanel());

        scene.setFill(Color.rgb(255, 113, 18));

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.UP) {
                    player.setState(0); // Set animation state for up button press
                    data.setPhantomCommand(Position.COMMAND.DOWN);
                    engine.setLastCmd(Position.COMMAND.DOWN);
                } else if (event.getCode() == KeyCode.LEFT) {

                    if (player.getState() == 2) {
                        player.setState(1); // Set animation state for double left button press
                        data.setPhantomCommand(Position.COMMAND.DOUBLE_RIGHT);
                        engine.setLastCmd(Position.COMMAND.DOUBLE_RIGHT);
                    } else if (player.getState() != 1) {
                        player.setState(2); // Set animation state for single left button press
                        data.setPhantomCommand(Position.COMMAND.RIGHT);
                        engine.setLastCmd(Position.COMMAND.RIGHT);
                    }

                } else if (event.getCode() == KeyCode.DOWN) {
                    data.setPhantomCommand(Position.COMMAND.UP);
                    engine.setLastCmd(Position.COMMAND.UP);

                    player.setState(3); // Set animation state for down button press

                } else if (event.getCode() == KeyCode.RIGHT) {

                    if (player.getState() == 4) {
                        player.setState(5); // Set animation state
                        data.setPhantomCommand(Position.COMMAND.DOUBLE_LEFT);
                        engine.setLastCmd(Position.COMMAND.DOUBLE_LEFT);
                    } else if (player.getState() != 5){
                        player.setState(4); // Set animation state for single right button press
                        data.setPhantomCommand(Position.COMMAND.LEFT);
                        engine.setLastCmd(Position.COMMAND.LEFT);
                    }
                }

                if (event.getCode() == KeyCode.F) {
                    if(data.getHeroDashBar() > 0 && !engine.isDash()){
                        data.removeDashScore();
                        engine.setDash(true);
                    }

                }

                if (event.getCode() == KeyCode.SPACE && data.getLp() == 0) {
                    data.init();
                    engine.init();
                    viewer.init();
                    engine.start();
                    background.start();
                }
                event.consume();
            }
        });

        // Start the Timeline to update the positions of the ocean ImageViews
        background.start();

        stage.setScene(scene);
        stage.setWidth(tools.Parameters.defaultWidth);
        stage.setHeight(tools.Parameters.defaultHeight);
        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                engine.start();
            }
        });
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                engine.stop();
            }
        });
        stage.show();
        stage.getIcons().add(new Image("/images/big-obstacle1.png"));
        stage.setTitle("Inferno Surf: Lucifer's adventure");

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16.67 ), event -> {
            Pane root = new Pane(
                    background.getBackgroundImage(),
                    background.getBackgroundImage2(),
                    background.getBackgroundImage3(),
                    background.getBackgroundImage4(),
                    background.getBackgroundImage5(),
                    background.getBackgroundImage6(),
                    ((Viewer) viewer).getPanel());

            if(data.getLp() == 0){
                background.stop();
                root.getChildren().add(viewer.getGameOverImage());
            }
            scene.setRoot(root);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

//        timer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                Pane root = new Pane(background.getOceanImageView1(), background.getOceanImageView2(), ((Viewer) viewer).getPanel());
//                scene.setRoot(root);
//                if(data.getLp() == 0){
//                    background.stop();
//                }
//            }
//        };
//        timer.start();
    }
}
