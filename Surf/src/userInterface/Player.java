package userInterface;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import specifications.ReadService;
import specifications.RequireReadService;
import tools.Parameters;

import java.io.InputStream;
import java.util.ArrayList;

public class Player implements RequireReadService {

    private ImageView surfAvatar;
    private ImageView heroAvatar;
    private static final int spriteSlowDownRate = Parameters.spriteSlowDownRate;
    private ArrayList<Rectangle2D> surfAvatarViewports;
    private int surfAvatarViewportIndex;

    private double heroesScale;
    private int state;

    private ReadService data;
    private int compte;

    public Player() {

        InputStream surfImage = getClass().getResourceAsStream("/images/surf.png");
        assert surfImage != null;
        Image spriteSheet = new Image(surfImage);

        InputStream heroImage = getClass().getResourceAsStream("/images/lucifer.png");
        assert heroImage != null;
        Image heroSheet = new Image(heroImage);

        surfAvatar = new ImageView(spriteSheet);
        surfAvatar.setViewport(new Rectangle2D(3*64, 0, 64, 64));

        heroAvatar = new ImageView(heroSheet);
        heroAvatar.setViewport(new Rectangle2D(3*64, 0, 64, 64));

        surfAvatarViewports = new ArrayList<Rectangle2D>();
        surfAvatarViewportIndex = 0;

        surfAvatarViewports.add(new Rectangle2D(3*64, 0, 64, 64));
        surfAvatarViewports.add(new Rectangle2D(3*64, 64, 64, 64));
        surfAvatarViewports.add(new Rectangle2D(3*64, 128, 64, 64));

        state = 0;
    }

    @Override
    public void bindReadService(ReadService service) {
        data = service;
    }

    public void setState(int state) {
        this.state = state;

        int offsetX = state * 64; // Horizontal offset of the first frame for the current state
        surfAvatarViewports = new ArrayList<Rectangle2D>();
        surfAvatarViewportIndex = 0;

        surfAvatarViewports.add(new Rectangle2D(offsetX, 0, 64, 64));
        surfAvatarViewports.add(new Rectangle2D(offsetX, 64, 64, 64));
        surfAvatarViewports.add(new Rectangle2D(offsetX, 128, 64, 64));

        heroAvatar.setViewport(new Rectangle2D(offsetX, 0, 64, 64));
    }


    public int getState() {
        return state;
    }

    public ImageView getSurfAvatar(double shrink, double xModifier, double yModifier)
    {
        compte++;
        int index = surfAvatarViewportIndex / spriteSlowDownRate;
        surfAvatar.setViewport(surfAvatarViewports.get(index));

        surfAvatar.setTranslateX(shrink * data.getHeroesPosition().x +
                shrink * xModifier
                - 0.5 * surfAvatarViewports.get(index).getWidth()
        );
        surfAvatar.setTranslateY(shrink * data.getHeroesPosition().y +
                shrink * yModifier +
                - 0.5 * surfAvatarViewports.get(index).getHeight() - 2
        );

        surfAvatarViewportIndex = (surfAvatarViewportIndex + 1) % (surfAvatarViewports.size() * spriteSlowDownRate);

        if(data.isClignote() && (compte %10 <5)){
            surfAvatar.setOpacity(0);
        }
        else{
            surfAvatar.setOpacity(1);
        }

        return surfAvatar;
    }

    public ImageView getHeroAvatar(double shrink, double xModifier, double yModifier){

        heroAvatar.setTranslateX(shrink * data.getHeroesPosition().x +
                shrink * xModifier
                - 0.5 * heroAvatar.getViewport().getWidth()
        );
        heroAvatar.setTranslateY(shrink * data.getHeroesPosition().y +
                shrink * yModifier
                - 0.5 * heroAvatar.getViewport().getHeight() - 6
        );
        if(data.isClignote() && (compte %10 <5)){
            heroAvatar.setOpacity(0);
        }
        else{
            heroAvatar.setOpacity(1);
        }

        return heroAvatar;
    }
}
