package userInterface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import specifications.EngineService;
import specifications.RequireEngineService;
import tools.Parameters;
import tools.Position;

import java.io.InputStream;

public class Background implements RequireEngineService {

    private final ImageView backgroundImage;
    private final ImageView backgroundImage2;
    private final ImageView backgroundImage3;
    private final ImageView backgroundImage4;
    private final ImageView backgroundImage5;
    private final ImageView backgroundImage6;
    private Timeline timeline;
    private EngineService engine;

    public Background() {

        ////////////////////////
        //  5  //  1   //  3  //
        ////////////////////////
        //  6  //  2  //  4   //
        ////////////////////////

        InputStream bgImage = getClass().getResourceAsStream("/images/bg3.png");
        assert bgImage != null;
        Image lavaTexture = new Image(bgImage);
        // Create two ImageViews to display the ocean texture image side by side
        backgroundImage = new ImageView(lavaTexture);
        backgroundImage2 = new ImageView(lavaTexture);
        backgroundImage3 = new ImageView(lavaTexture);
        backgroundImage4 = new ImageView(lavaTexture);
        backgroundImage5 = new ImageView(lavaTexture);
        backgroundImage6 = new ImageView(lavaTexture);

//        backgroundImage.setOpacity(0.9);
//        backgroundImage2.setOpacity(0.9);
//        backgroundImage3.setOpacity(0.9);
//        backgroundImage4.setOpacity(0.9);
//        backgroundImage5.setOpacity(0.9);
//        backgroundImage6.setOpacity(0.9);

        // Set the fitWidth and fitHeight properties of the ImageView to the screen size
        backgroundImage.setFitWidth(Parameters.screenWidth);
        backgroundImage.setFitHeight(Parameters.screenHeight);

        backgroundImage2.setFitWidth(Parameters.screenWidth);
        backgroundImage2.setFitHeight(Parameters.screenHeight);

        backgroundImage3.setFitWidth(Parameters.screenWidth);
        backgroundImage3.setFitHeight(Parameters.screenHeight);

        backgroundImage4.setFitWidth(Parameters.screenWidth);
        backgroundImage4.setFitHeight(Parameters.screenHeight);

        backgroundImage5.setFitWidth(Parameters.screenWidth);
        backgroundImage5.setFitHeight(Parameters.screenHeight);

        backgroundImage6.setFitWidth(Parameters.screenWidth);
        backgroundImage6.setFitHeight(Parameters.screenHeight);

        // Set the positions of the ocean ImageViews
        backgroundImage.setY(0);
        backgroundImage3.setY(0);
        backgroundImage5.setY(0);

        backgroundImage.setX(0);
        backgroundImage3.setX(Parameters.screenWidth);
        backgroundImage5.setX(-Parameters.screenWidth);

        backgroundImage2.setY(Parameters.screenHeight);
        backgroundImage4.setY(Parameters.screenHeight);
        backgroundImage6.setY(Parameters.screenHeight);

        backgroundImage2.setX(0);
        backgroundImage4.setX(Parameters.screenWidth);
        backgroundImage6.setX(-Parameters.screenWidth);


        // Create a Timeline to update the positions of the ImageViews
        timeline = new Timeline(new KeyFrame(Duration.millis(20), event -> {

            double velocity = engine.getVelocity();

            double newY1 = backgroundImage.getY() - velocity;
            double newY2 = backgroundImage2.getY() - velocity;
            double newY3 = backgroundImage3.getY() - velocity;
            double newY4 = backgroundImage4.getY() - velocity;
            double newY5 = backgroundImage5.getY() - velocity;
            double newY6 = backgroundImage6.getY() - velocity;

            // Calculate the new positions of the ImageViews
            double newX1 = backgroundImage.getX();
            double newX2 = backgroundImage2.getX();
            double newX3 = backgroundImage3.getX();
            double newX4 = backgroundImage4.getX();
            double newX5 = backgroundImage5.getX();
            double newX6 = backgroundImage6.getX();

            if (engine.getLastCommand() == Position.COMMAND.LEFT) {
                newX1 = backgroundImage.getX() - velocity * Math.sin(Math.PI / 6);
                newX2 = backgroundImage2.getX() - velocity * Math.sin(Math.PI / 6);
                newX3 = backgroundImage3.getX() - velocity * Math.sin(Math.PI / 6);
                newX4 = backgroundImage4.getX() - velocity * Math.sin(Math.PI / 6);
                newX5 = backgroundImage5.getX() - velocity * Math.sin(Math.PI / 6);
                newX6 = backgroundImage6.getX() - velocity * Math.sin(Math.PI / 6);

                newY1 = backgroundImage.getY() - velocity * Math.cos(Math.PI / 6);
                newY2 = backgroundImage2.getY() - velocity * Math.cos(Math.PI / 6);
                newY3 = backgroundImage3.getY() - velocity * Math.cos(Math.PI / 6);
                newY4 = backgroundImage4.getY() - velocity * Math.cos(Math.PI / 6);
                newY5 = backgroundImage5.getY() - velocity * Math.cos(Math.PI / 6);
                newY6 = backgroundImage6.getY() - velocity * Math.cos(Math.PI / 6);
            }
            if (engine.getLastCommand() == Position.COMMAND.RIGHT) {
                newX1 = backgroundImage.getX() + velocity * Math.sin(Math.PI / 6);
                newX2 = backgroundImage2.getX() + velocity * Math.sin(Math.PI / 6);
                newX3 = backgroundImage3.getX() + velocity * Math.sin(Math.PI / 6);
                newX4 = backgroundImage4.getX() + velocity * Math.sin(Math.PI / 6);
                newX5 = backgroundImage5.getX() + velocity * Math.sin(Math.PI / 6);
                newX6 = backgroundImage6.getX() + velocity * Math.sin(Math.PI / 6);

                newY1 = backgroundImage.getY() - velocity * Math.cos(Math.PI / 6);
                newY2 = backgroundImage2.getY() - velocity * Math.cos(Math.PI / 6);
                newY3 = backgroundImage3.getY() - velocity * Math.cos(Math.PI / 6);
                newY4 = backgroundImage4.getY() - velocity * Math.cos(Math.PI / 6);
                newY5 = backgroundImage5.getY() - velocity * Math.cos(Math.PI / 6);
                newY6 = backgroundImage6.getY() - velocity * Math.cos(Math.PI / 6);
            }

            if (engine.getLastCommand() == Position.COMMAND.DOUBLE_LEFT) {
                newX1 = backgroundImage.getX() - velocity * Math.sin(Math.PI / 4);
                newX2 = backgroundImage2.getX() - velocity * Math.sin(Math.PI / 4);
                newX3 = backgroundImage3.getX() - velocity * Math.sin(Math.PI / 4);
                newX4 = backgroundImage4.getX() - velocity * Math.sin(Math.PI / 4);
                newX5 = backgroundImage5.getX() - velocity * Math.sin(Math.PI / 4);
                newX6 = backgroundImage6.getX() - velocity * Math.sin(Math.PI / 4);

                newY1 = backgroundImage.getY() - velocity * Math.cos(Math.PI / 4);
                newY2 = backgroundImage2.getY() - velocity * Math.cos(Math.PI / 4);
                newY3 = backgroundImage3.getY() - velocity * Math.cos(Math.PI / 4);
                newY4 = backgroundImage4.getY() - velocity * Math.cos(Math.PI / 4);
                newY5 = backgroundImage5.getY() - velocity * Math.cos(Math.PI / 4);
                newY6 = backgroundImage6.getY() - velocity * Math.cos(Math.PI / 4);
            }
            if (engine.getLastCommand() == Position.COMMAND.DOUBLE_RIGHT) {
                newX1 = backgroundImage.getX() + velocity * Math.sin(Math.PI / 4);
                newX2 = backgroundImage2.getX() + velocity * Math.sin(Math.PI / 4);
                newX3 = backgroundImage3.getX() + velocity * Math.sin(Math.PI / 4);
                newX4 = backgroundImage4.getX() + velocity * Math.sin(Math.PI / 4);
                newX5 = backgroundImage5.getX() + velocity * Math.sin(Math.PI / 4);
                newX6 = backgroundImage6.getX() + velocity * Math.sin(Math.PI / 4);

                newY1 = backgroundImage.getY() - velocity * Math.cos(Math.PI / 4);
                newY2 = backgroundImage2.getY() - velocity * Math.cos(Math.PI / 4);
                newY3 = backgroundImage3.getY() - velocity * Math.cos(Math.PI / 4);
                newY4 = backgroundImage4.getY() - velocity * Math.cos(Math.PI / 4);
                newY5 = backgroundImage5.getY() - velocity * Math.cos(Math.PI / 4);
                newY6 = backgroundImage6.getY() - velocity * Math.cos(Math.PI / 4);
            }
            if (engine.getLastCommand() == Position.COMMAND.DOWN) {
                newY1 = backgroundImage.getY();
                newY2 = backgroundImage2.getY();
                newY3 = backgroundImage3.getY();
                newY4 = backgroundImage4.getY();
                newY5 = backgroundImage5.getY();
                newY6 = backgroundImage6.getY();
            }


            //If an ImageView goes off the screen to the left, move it to the right of the other ImageView
            if (newY1 < -Parameters.screenHeight) {
                backgroundImage.setY(backgroundImage2.getY() + Parameters.screenHeight);
            } else if (newY2 < -Parameters.screenHeight) {
                backgroundImage2.setY(backgroundImage.getY() + Parameters.screenHeight);
            } else {
                // Otherwise, update the positions of both ImageViews
                backgroundImage.setY(newY1);
                backgroundImage2.setY(newY2);
            }

            if (newY3 < -Parameters.screenHeight) {
                backgroundImage3.setY(backgroundImage4.getY() + Parameters.screenHeight);
            } else if (newY4 < -Parameters.screenHeight) {
                backgroundImage4.setY(backgroundImage3.getY() + Parameters.screenHeight);
            } else {
                // Otherwise, update the positions of both ImageViews
                backgroundImage3.setY(newY3);
                backgroundImage4.setY(newY4);
            }

            if (newY5 < -Parameters.screenHeight) {
                backgroundImage5.setY(backgroundImage6.getY() + Parameters.screenHeight);
            } else if (newY6 < -Parameters.screenHeight) {
                backgroundImage6.setY(backgroundImage5.getY() + Parameters.screenHeight);
            } else {
                // Otherwise, update the positions of both ImageViews
                backgroundImage5.setY(newY5);
                backgroundImage6.setY(newY6);
            }

            if(newX5 < -2*Parameters.screenWidth){
                backgroundImage5.setX(backgroundImage3.getX() + Parameters.screenWidth);
                backgroundImage.setX(backgroundImage5.getX() + Parameters.screenWidth);
            }else if(newX3 < -2*Parameters.screenWidth){
                backgroundImage3.setX(backgroundImage.getX() + Parameters.screenWidth);
                backgroundImage5.setX(backgroundImage3.getX() + Parameters.screenWidth);
            }else if(newX1 < -2*Parameters.screenWidth){
                backgroundImage.setX(backgroundImage5.getX() + Parameters.screenWidth);
                backgroundImage3.setX(backgroundImage.getX() + Parameters.screenWidth);
            }else if(newX3 > 2*Parameters.screenWidth){
                backgroundImage3.setX(backgroundImage5.getX() - Parameters.screenWidth);
                backgroundImage.setX(backgroundImage3.getX() - Parameters.screenWidth);
            }else if(newX5 > 2*Parameters.screenWidth){
                backgroundImage5.setX(backgroundImage.getX() - Parameters.screenWidth);
                backgroundImage3.setX(backgroundImage5.getX() - Parameters.screenWidth);
            }else if(newX1 > 2*Parameters.screenWidth){
                backgroundImage.setX(backgroundImage3.getX() - Parameters.screenWidth);
                backgroundImage5.setX(backgroundImage.getX() - Parameters.screenWidth);
            }else{
                backgroundImage.setX(newX1);
                backgroundImage3.setX(newX3);
                backgroundImage5.setX(newX5);
            }

            if(newX6 < -2*Parameters.screenWidth){
                backgroundImage6.setX(backgroundImage4.getX() + Parameters.screenWidth);
                backgroundImage2.setX(backgroundImage6.getX() + Parameters.screenWidth);
            }else if(newX4 < -2*Parameters.screenWidth){
                backgroundImage4.setX(backgroundImage2.getX() + Parameters.screenWidth);
                backgroundImage6.setX(backgroundImage4.getX() + Parameters.screenWidth);
            }else if(newX2 < -2*Parameters.screenWidth){
                backgroundImage2.setX(backgroundImage6.getX() + Parameters.screenWidth);
                backgroundImage4.setX(backgroundImage2.getX() + Parameters.screenWidth);
            }else if(newX4 > 2*Parameters.screenWidth){
                backgroundImage4.setX(backgroundImage6.getX() - Parameters.screenWidth);
                backgroundImage2.setX(backgroundImage4.getX() - Parameters.screenWidth);
            }else if(newX6 > 2*Parameters.screenWidth){
                backgroundImage6.setX(backgroundImage2.getX() - Parameters.screenWidth);
                backgroundImage4.setX(backgroundImage6.getX() - Parameters.screenWidth);
            }else if(newX2 > 2*Parameters.screenWidth){
                backgroundImage2.setX(backgroundImage4.getX() - Parameters.screenWidth);
                backgroundImage6.setX(backgroundImage2.getX() - Parameters.screenWidth);
            }else{
                backgroundImage2.setX(newX2);
                backgroundImage4.setX(newX4);
                backgroundImage6.setX(newX6);
            }

        }));

        // Set the Timeline to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void bindEngineService(EngineService service) {
        engine = service;
    }

    public void start() {
        timeline.play();
    }

    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    public ImageView getBackgroundImage2() {
        return backgroundImage2;
    }

    public ImageView getBackgroundImage3() {
        return backgroundImage3;
    }

    public ImageView getBackgroundImage4() {
        return backgroundImage4;
    }

    public ImageView getBackgroundImage5() {
        return backgroundImage5;
    }

    public ImageView getBackgroundImage6() {
        return backgroundImage6;
    }

    public void stop() {
        timeline.stop();
    }


}
