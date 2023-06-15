package userInterface;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import specifications.PhantomService;
import tools.Parameters;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Enemy {

    private ImageView enemyAvatar;

    private final InputStream enemy1Image = getClass().getResourceAsStream("/images/enemy-1.gif");
    private final InputStream enemy2Image = getClass().getResourceAsStream("/images/enemy-2.gif");
    private final InputStream enemy3Image = getClass().getResourceAsStream("/images/enemy-3.gif");

    private int state;
    public Enemy() {
        state = 2;
    }
    public void setAvatar() {

        double r = new Random().nextFloat();
        if(r <= 0.1){
            assert enemy1Image != null;
            Image enemy1SpriteSheet = new Image(enemy1Image);
            enemyAvatar = new ImageView(enemy1SpriteSheet);
        }else if (r > 0.33 && r <= 0.66){
            assert enemy2Image != null;
            Image enemy2SpriteSheet = new Image(enemy2Image);
            enemyAvatar = new ImageView(enemy2SpriteSheet);
        }else{
            assert enemy3Image != null;
            Image enemy3SpriteSheet = new Image(enemy3Image);
            enemyAvatar = new ImageView(enemy3SpriteSheet);
        }

        enemyAvatar.setViewport(new Rectangle2D(2*64, 0, 64, 64));
    }

    public void setState(int state) {

        this.state = state;

        int offsetX = state * 64; // Horizontal offset of the first frame for the current state

        enemyAvatar.setViewport(new Rectangle2D(offsetX, 0, 64, 64));
    }

    public int getState(){
        return state;
    }

    public ImageView getEnemyAvatar(){

        return enemyAvatar;
    }

}
