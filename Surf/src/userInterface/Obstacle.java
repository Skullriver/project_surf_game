package userInterface;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tools.Parameters;

import java.io.InputStream;
import java.util.Random;

public class Obstacle {

    private ImageView obstacleAvatar;

    public Obstacle() {
        InputStream obstacle1Image = getClass().getResourceAsStream("/images/obstacle-1.png");
        assert obstacle1Image != null;
        Image obstacle1SpriteSheet = new Image(obstacle1Image);
        obstacleAvatar = new ImageView(obstacle1SpriteSheet);
    }
    public void setAvatar() {

        InputStream obstacle9Image = getClass().getResourceAsStream("/images/obstacle-9.png");
        assert obstacle9Image != null;
        Image obstacle9SpriteSheet = new Image(obstacle9Image);
        obstacleAvatar = new ImageView(obstacle9SpriteSheet);

        double r = new Random().nextFloat();
        if(r <= 0.11){
            InputStream obstacle1Image = getClass().getResourceAsStream("/images/obstacle-1.png");
            assert obstacle1Image != null;
            Image obstacle1SpriteSheet = new Image(obstacle1Image);
            obstacleAvatar = new ImageView(obstacle1SpriteSheet);
        }else if (r > 0.11 && r <= 0.22){
            InputStream obstacle2Image = getClass().getResourceAsStream("/images/obstacle-2.png");
            assert obstacle2Image != null;
            Image obstacle2SpriteSheet = new Image(obstacle2Image);
            obstacleAvatar = new ImageView(obstacle2SpriteSheet);
        }else if (r > 0.22 && r <= 0.33){
            InputStream obstacle3Image = getClass().getResourceAsStream("/images/obstacle-3.png");
            assert obstacle3Image != null;
            Image obstacle3SpriteSheet = new Image(obstacle3Image);
            obstacleAvatar = new ImageView(obstacle3SpriteSheet);
        }else if (r > 0.33 && r <= 0.44){
            InputStream obstacle4Image = getClass().getResourceAsStream("/images/obstacle-4.png");
            assert obstacle4Image != null;
            Image obstacle4SpriteSheet = new Image(obstacle4Image);
            obstacleAvatar = new ImageView(obstacle4SpriteSheet);
        }else if (r > 0.44 && r <= 0.55){
            InputStream obstacle5Image = getClass().getResourceAsStream("/images/obstacle-5.png");
            assert obstacle5Image != null;
            Image obstacle5SpriteSheet = new Image(obstacle5Image);
            obstacleAvatar = new ImageView(obstacle5SpriteSheet);
        }else if (r > 0.55 && r <= 0.66) {
            InputStream obstacle6Image = getClass().getResourceAsStream("/images/obstacle-6.png");
            assert obstacle6Image != null;
            Image obstacle6SpriteSheet = new Image(obstacle6Image);
            obstacleAvatar = new ImageView(obstacle6SpriteSheet);
        }else if (r > 0.66 && r <= 0.77) {
            InputStream obstacle7Image = getClass().getResourceAsStream("/images/obstacle-7.png");
            assert obstacle7Image != null;
            Image obstacle7SpriteSheet = new Image(obstacle7Image);
            obstacleAvatar = new ImageView(obstacle7SpriteSheet);
        }else if (r > 0.77 && r <= 0.88) {
            InputStream obstacle8Image = getClass().getResourceAsStream("/images/obstacle-8.png");
            assert obstacle8Image != null;
            Image obstacle8SpriteSheet = new Image(obstacle8Image);
            obstacleAvatar = new ImageView(obstacle8SpriteSheet);
        }
    }

    public ImageView getObstacleAvatar(){
        return this.obstacleAvatar;
    }

}
