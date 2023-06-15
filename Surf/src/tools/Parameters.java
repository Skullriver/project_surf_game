/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: tools/HardCodedParameters.java 2015-03-11 buixuan.
 * ******************************************************/
package tools;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.InputStream;

public class Parameters {

    //---PARAMETERS---//

    public static final int roundObstacleWidth = 64, roundObstacleHeight = 64;
    public static final int vortexWidth = 64, vortexHeight = 64;
    public static final int dashWidth = 64, dashHeight = 64;
    public static final int hearthWidth = 64, hearthHeight = 64;
    public static final int mobWidth = 32, mobHeight = 64;
    public static final int enginePaceMillis = 200,
            spriteSlowDownRate = 10;
    public static final double friction = 0.30;
    public static final int bigPhantomWidth = 60, bigPhantomHeight = 60;
    public static final Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    public static double screenWidth = screenBounds.getWidth();
    public static final int defaultWidth = (int) screenBounds.getWidth(),
            defaultHeight = (int) screenBounds.getHeight(),
            heroesWidth = 18, heroesHeight = 48,
            heroesStartX = defaultWidth / 2, heroesStartY = defaultHeight / 2 - 100,
            phantomWidth = 30, phantomHeight = 30, velocity = 2;
    public static double screenHeight = screenBounds.getHeight();
    public static String defaultParamFileName = "in.parameters";

    public static <T> T instantiate(final String className, final Class<T> type) {
        try {
            return type.cast(Class.forName(className).newInstance());
        } catch (final InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static enum phantomType {
        roundObstacle, bigObstacle, vortex,
        slowdownObstacle, dash, heart, bubble, vortexTexture,mob
    }




    public static final Position uperRight = new Position(0, defaultWidth);


    public static final Position downRight = new Position(defaultHeight, defaultWidth);


}
