/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: userInterface/Viewer.java 2015-03-11 buixuan.
 * ******************************************************/
package userInterface;

import data.ia.Mob;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import specifications.PhantomService;
import specifications.ReadService;
import specifications.RequireReadService;
import specifications.ViewerService;
import tools.Parameters;

import java.io.InputStream;
import java.util.ArrayList;

public class Viewer implements ViewerService, RequireReadService {
    private static final double defaultMainWidth = Parameters.defaultWidth,
            defaultMainHeight = Parameters.defaultHeight;
    private ReadService data;
    private Image fondDeMap;
    private Image lavaTexture;
    private Image icons;
    private Image heartSpriteSheet;
    private Image gameOver;
    private Image bubble;
    private Image vortex;
    private Image slowdown;
    private Image skullSpriteSheet;
    private double xShrink, yShrink, shrink, xModifier, yModifier;
    private Player player;
    private Enemy enemy;

    public Viewer() {
    }

    @Override
    public void bindReadService(ReadService service) {
        data = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void init() {
        xShrink = 1;
        yShrink = 1;
        xModifier = 1000;
        yModifier = 0;

        InputStream iconImage = getClass().getResourceAsStream("/images/icons.png");
        assert iconImage != null;
        icons = new Image(iconImage);

        InputStream heartBonusImage = getClass().getResourceAsStream("/images/heart.gif");
        assert heartBonusImage != null;
        heartSpriteSheet = new Image(heartBonusImage);

        InputStream skullImage = getClass().getResourceAsStream("/images/skull.gif");
        assert skullImage != null;
        skullSpriteSheet = new Image(skullImage);

        InputStream gameOverImage = getClass().getResourceAsStream("/images/game over.png");
        assert gameOverImage != null;
        gameOver = new Image(gameOverImage);

        InputStream bubbleImage = getClass().getResourceAsStream("/images/bubble.gif");
        assert bubbleImage != null;
        bubble = new Image(bubbleImage);

        InputStream vortexImage = getClass().getResourceAsStream("/images/vortex.gif");
        assert vortexImage != null;
        vortex = new Image(vortexImage);

        InputStream slowdownImage = getClass().getResourceAsStream("/images/slowdown.gif");
        assert slowdownImage != null;
        slowdown = new Image(slowdownImage);

        enemy = new Enemy();

    }


    @Override
    public Parent getPanel() {

        shrink = Math.min(xShrink, yShrink);
        xModifier = .01 * shrink * defaultMainHeight;
        yModifier = .01 * shrink * defaultMainHeight;


//        Circle crl = new Circle(shrink * data.getHeroesPosition().x +
//                shrink * xModifier, shrink * data.getHeroesPosition().y +
//                shrink * yModifier, data.getHeroesWidth() / 2);
//        Rectangle rect = new Rectangle(0, 0, shrink * data.getHeroesWidth(), shrink * data.getHeroesHeight());
//        rect.setTranslateX(shrink * data.getHeroesPosition().x + shrink * xModifier);
//        rect.setTranslateY(shrink * data.getHeroesPosition().y + shrink * yModifier);

        Ellipse oval = new Ellipse(shrink * data.getHeroesPosition().x + shrink * xModifier,
                shrink * data.getHeroesPosition().y + shrink * yModifier,
                data.getHeroesWidth() / 2, data.getHeroesHeight() / 2);


        ImageView surfAvatar = player.getSurfAvatar(shrink, xModifier, yModifier);
        ImageView heroAvatar = player.getHeroAvatar(shrink, xModifier, yModifier);

        Group panel = new Group();

        ArrayList<PhantomService> phantoms = data.getObjects();

        for (PhantomService p : phantoms) {

            if (p.getPhantomType() == Parameters.phantomType.roundObstacle) {

                ImageView obstacleAvatar = p.getAvatar().getObstacleAvatar();
                obstacleAvatar.setTranslateX(p.getPosition().x - 32);
                obstacleAvatar.setTranslateY(p.getPosition().y - 32);

                panel.getChildren().add(obstacleAvatar);
                
            }

            if (p.getPhantomType() == Parameters.phantomType.slowdownObstacle) {

                ImageView slowdownAvatar = new ImageView(slowdown);
                slowdownAvatar.setTranslateX(shrink * p.getPosition().x + shrink * xModifier);
                slowdownAvatar.setTranslateY(shrink * p.getPosition().y + shrink * yModifier);

                panel.getChildren().addAll(slowdownAvatar);
            }
            if (p.getPhantomType() == Parameters.phantomType.dash) {

                ImageView dashAvatar = new ImageView(skullSpriteSheet);
                dashAvatar.setPreserveRatio(true);
                dashAvatar.setFitWidth(shrink * Parameters.hearthWidth);
                dashAvatar.setFitHeight(shrink * Parameters.hearthHeight);
                dashAvatar.setTranslateX(shrink * p.getPosition().x + shrink * xModifier - shrink * 32);
                dashAvatar.setTranslateY(shrink * p.getPosition().y + shrink * yModifier - shrink * 32);
                
                panel.getChildren().add(dashAvatar);
            }
            if (p.getPhantomType() == Parameters.phantomType.heart) {

                ImageView hearthAvatar = new ImageView(heartSpriteSheet);
                hearthAvatar.setPreserveRatio(true);
                hearthAvatar.setFitWidth(shrink * Parameters.hearthWidth);
                hearthAvatar.setFitHeight(shrink * Parameters.hearthHeight);

                hearthAvatar.setTranslateX(shrink * p.getPosition().x + shrink * xModifier - shrink * 32);
                hearthAvatar.setTranslateY(shrink * p.getPosition().y + shrink * yModifier - shrink * 20);

                panel.getChildren().add(hearthAvatar);

            }
            if (p.getPhantomType() == Parameters.phantomType.vortexTexture) {

                ImageView vortexAvatar = new ImageView(vortex);
                vortexAvatar.setTranslateX(shrink * p.getPosition().x + shrink * xModifier - vortexAvatar.getLayoutBounds().getWidth() / 2);
                vortexAvatar.setTranslateY(shrink * p.getPosition().y + shrink * yModifier - vortexAvatar.getLayoutBounds().getHeight() / 2);

                panel.getChildren().add(vortexAvatar);

            }
            if (p.getPhantomType() == Parameters.phantomType.bubble) {
                ImageView bubbleAvatar = new ImageView(bubble);
                bubbleAvatar.setPreserveRatio(true);
                bubbleAvatar.setFitWidth(0.5 * Parameters.hearthWidth);
                bubbleAvatar.setFitHeight(0.5 * Parameters.hearthHeight);
                bubbleAvatar.setTranslateX(shrink * p.getPosition().x + shrink * xModifier - shrink * 32);
                bubbleAvatar.setTranslateY(shrink * p.getPosition().y + shrink * yModifier - shrink * 20);
                panel.getChildren().add(bubbleAvatar);
            }

        }

        for (PhantomService p1 : phantoms) {
            if (p1.getPhantomType() == Parameters.phantomType.mob) {
                ImageView mobAvatar = ((Mob) p1).getTexture().getEnemyAvatar();

                mobAvatar.setTranslateX(shrink * p1.getPosition().x + shrink * xModifier - 0.5 * mobAvatar.getViewport().getWidth());
                mobAvatar.setTranslateY(shrink * p1.getPosition().y + shrink * yModifier - 0.5 * mobAvatar.getViewport().getHeight());
                panel.getChildren().add(mobAvatar);
            }
        }

        for (int i = 0; i < 3 - data.getLp(); i++) {
            ImageView emptyHeart = new ImageView(icons);
            emptyHeart.setViewport(new Rectangle2D(0, 32, 32, 32));

            emptyHeart.setLayoutX(defaultMainWidth * 0.5 - 160 + 64 - i * 32);
            emptyHeart.setLayoutY(16);
            panel.getChildren().add(emptyHeart);
        }

        for (int i = 0; i < data.getLp(); i++) {
            ImageView heart = new ImageView(icons);
            heart.setViewport(new Rectangle2D(0, 0, 32, 32));

            heart.setLayoutX(defaultMainWidth * 0.5 - 160 + i * 32);
            heart.setLayoutY(16);
            panel.getChildren().add(heart);
        }

        for (int i = 0; i < 3 - data.getHeroDashBar(); i++) {
            ImageView emptyDash = new ImageView(icons);
            emptyDash.setViewport(new Rectangle2D(32, 32, 32, 32));
            emptyDash.setLayoutX(defaultMainWidth * 0.5 + 64 + 64 - i * 32);
            emptyDash.setLayoutY(16);
            panel.getChildren().add(emptyDash);
        }

        for (int i = 0; i < data.getHeroDashBar(); i++) {
            ImageView dash = new ImageView(icons);
            dash.setViewport(new Rectangle2D(32, 0, 32, 32));
            dash.setLayoutX(defaultMainWidth * 0.5 + 64 + i * 32);
            dash.setLayoutY(16);
            panel.getChildren().add(dash);
        }

        Text score = new Text(data.getScore() + " m");
        Font font = new Font("Arial", 20);
        score.setFont(font);
        score.setTranslateX(shrink * defaultMainWidth / 2 - score.getLayoutBounds().getWidth() / 2);
        score.setTranslateY(40);
        panel.getChildren().add(score);


        Text storedScore = new Text("Record score: " + data.getStoredScore() + " m");
        font = new Font("Arial", 18);
        storedScore.setFont(font);
        storedScore.setTranslateX(shrink * defaultMainWidth - 50 - storedScore.getLayoutBounds().getWidth());
        storedScore.setTranslateY(40);
        panel.getChildren().add(storedScore);

        if (data.isFinish()) {
            panel.setEffect(new GaussianBlur());
        }

        panel.getChildren().addAll(surfAvatar, heroAvatar /*, oval*/);

        return panel;
    }

    public ImageView getGameOverImage() {
        ImageView endGame = new ImageView(gameOver);
        endGame.setLayoutX(defaultMainWidth * 0.5 - endGame.getLayoutBounds().getWidth() / 2);
        endGame.setLayoutY(defaultMainHeight * 0.5 - endGame.getLayoutBounds().getHeight() / 2);

        return endGame;
    }

    @Override
    public void setMainWindowWidth(double width) {
        xShrink = width / defaultMainWidth;
    }

    @Override
    public void setMainWindowHeight(double height) {
        yShrink = height / defaultMainHeight;
    }
}
