package engine;

import data.ia.Mob;
import data.ia.Phantom;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import specifications.*;
import tools.Parameters;
import tools.Position;

import java.util.Random;

public class Object implements RequireDataService, RequireEngineService {

    private DataService data;
    private EngineService engine;
    private boolean hasCollision;

    @Override
    public void bindDataService(DataService service) {
        data = service;
    }

    @Override
    public void bindEngineService(EngineService service) {
        engine = service;
    }

    public Position spawnPosition(double width, double height, java.lang.Object form) {

        Random gen = new Random();
        int x = 0;
        int y = (int) (Parameters.screenHeight + 100);
        boolean cont = true;

        while (cont) {
            x = gen.nextInt((int) Parameters.screenWidth);
            PhantomService potentialPhantom =
                    new Phantom(new Position(x - 128, y - 128), null, width + 128, height + 128, null, form);
            if (form instanceof Circle) {
                potentialPhantom =
                        new Phantom(new Position(x, y), null, width + 80, height + 80, null, form);
            }
            cont = false;
            for (PhantomService p : data.getObjects()) {
                if (isOverlap(p, potentialPhantom)) {
                    cont = true;
                    break;
                }
            }
        }
        return new Position(x, y);
    }

    public void spawnPhantom() {
        Position position = spawnPosition(Parameters.roundObstacleWidth, Parameters.roundObstacleHeight, new Circle());
        data.addObject(
                position,
                engine.getLastCommand(),
                Parameters.roundObstacleWidth,
                Parameters.roundObstacleHeight,
                Parameters.phantomType.roundObstacle,
                new Circle()
        );

    }

    public void spawnVortex() {
        Position position = spawnPosition(Parameters.vortexWidth, Parameters.vortexHeight, new Circle());
        data.addObject(
                position,
                engine.getLastCommand(),
                Parameters.vortexWidth,
                Parameters.vortexHeight,
                Parameters.phantomType.vortex,
                new Circle()
        );
        data.addObject(
                position,
                engine.getLastCommand(),
                Parameters.vortexWidth,
                Parameters.vortexHeight,
                Parameters.phantomType.vortexTexture,
                new Circle()
        );
    }

    public void spawnDash() {
        Position position = spawnPosition(Parameters.dashWidth, Parameters.dashHeight, new Circle());
        data.addObject(position,
                engine.getLastCommand(),
                Parameters.dashWidth,
                Parameters.dashHeight,
                Parameters.phantomType.dash,
                new Circle()
        );
    }

    public void spawnHeart() {
        Position position = spawnPosition(Parameters.hearthWidth, Parameters.hearthHeight, new Circle());
        data.addObject(
                position,
                engine.getLastCommand(),
                Parameters.hearthWidth,
                Parameters.hearthHeight,
                Parameters.phantomType.heart,
                new Circle());
    }

    public void spawnMob() {
        Position position = spawnPosition(Parameters.mobWidth, Parameters.mobHeight, new Circle());
        Mob b = new Mob(position, engine.getLastCommand(), Parameters.phantomType.mob, new Circle());
        data.addMob(position, b);
    }

    public void spawnBubble() {
        Position position = spawnPosition(Parameters.mobWidth, Parameters.mobHeight, new Rectangle());
        data.addObject(
                position,
                engine.getLastCommand(),
                Parameters.mobWidth,
                Parameters.mobHeight,
                Parameters.phantomType.bubble,
                new Rectangle());
    }

    public void spawnStructure() {
        Random r = new Random();
        int widthPassage = (int) (r.nextFloat() * 100 + 64);

        //horizontal number of tiles for structure between is 2 and 5
        int scaleNumberWidthLeft = r.nextInt(4) + 2;
        //vertical number of tiles for structure between is 2 and 4
        int scaleNumberHeightLeft = r.nextInt(3) + 2;

        //horizontal number of tiles for structure between is 2 and 5
        int scaleNumberWidthRight = r.nextInt(4) + 2;
        //vertical number of tiles for structure between is 2 and 4
        int scaleNumberHeightRight = r.nextInt(3) + 2;

        //start position
        Position position = spawnPosition(Parameters.roundObstacleWidth * scaleNumberWidthLeft +
                        Parameters.roundObstacleWidth * scaleNumberWidthRight + widthPassage,
                Parameters.roundObstacleHeight * Math.max(scaleNumberHeightRight, scaleNumberHeightLeft),
                new Rectangle());

        int scaleNumberHeight = scaleNumberHeightLeft;
        int scaleNumberWidth = scaleNumberWidthLeft;
        //k = 0 for left part of structure, k = 1 for right part
        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < scaleNumberHeight; i++) {
                for (int j = 0; j < scaleNumberWidth; j++) {
                    //fill the left part of structure with obstacles, slowdown or with empty
                    double choice = r.nextFloat();
                    if (choice <= 0.33) {
                        Position phantomPosition =
                                new Position(position.x + j * Parameters.roundObstacleWidth + Parameters.roundObstacleWidth / 2,
                                        position.y + i * Parameters.roundObstacleHeight + Parameters.roundObstacleHeight / 2);
                        //obstacle
                        data.addObject(phantomPosition,
                                engine.getLastCommand(),
                                Parameters.roundObstacleWidth,
                                Parameters.roundObstacleHeight,
                                Parameters.phantomType.roundObstacle,
                                new Circle());
                    } else if (choice > 0.33 && choice <= 0.66) {
                        Position phantomPosition =
                                new Position(position.x + j * Parameters.roundObstacleWidth,
                                        position.y + i * Parameters.roundObstacleHeight);
                        //slowdown
                        data.addObject(phantomPosition,
                                engine.getLastCommand(),
                                Parameters.roundObstacleWidth,
                                Parameters.roundObstacleHeight,
                                Parameters.phantomType.slowdownObstacle,
                                new Rectangle());
                    } //else empty
                }
            }
            //add dash inside
            if (r.nextFloat() < 0.3 && k == 0) {
                data.addObject(
                        new Position(position.x + scaleNumberWidth * Parameters.roundObstacleWidth + widthPassage / 2, position.y + scaleNumberHeight / 2 * Parameters.roundObstacleHeight),
                        engine.getLastCommand(),
                        Parameters.dashWidth,
                        Parameters.dashHeight,
                        Parameters.phantomType.dash,
                        new Circle());
            }
            //set start position for right part of structure
            position = new Position(position.x + scaleNumberWidth * Parameters.roundObstacleWidth + widthPassage, position.y);
            scaleNumberHeight = scaleNumberHeightRight;
            scaleNumberWidth = scaleNumberWidthRight;
        }
    }

    public boolean collisionRectRect(PhantomService o1, PhantomService o2) {

        double rect1Left = o1.getPosition().x;
        double rect1Right = o1.getPosition().x + o1.getPhantomWidth();
        double rect1Top = o1.getPosition().y;
        double rect1Bottom = o1.getPosition().y + o1.getPhantomHeight();

        double rect2Left = o2.getPosition().x;
        double rect2Right = o2.getPosition().x + o2.getPhantomWidth();
        double rect2Top = o2.getPosition().y;
        double rect2Bottom = o2.getPosition().y + o2.getPhantomHeight();

        if (rect1Left < rect2Right && rect1Right > rect2Left &&
                rect1Top < rect2Bottom && rect1Bottom > rect2Top) {
            // The rectangles intersect
            return true;
        } else {
            // The rectangles do not intersect
            return false;
        }
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private boolean collisionCircleRect(PhantomService circle, PhantomService rect) {

        // Find the closest point on the rectangle's perimeter to the circle's center
        double closestX = clamp(circle.getPosition().x, rect.getPosition().x, rect.getPosition().x + rect.getPhantomWidth());
        double closestY = clamp(circle.getPosition().y, rect.getPosition().y, rect.getPosition().y + rect.getPhantomHeight());

        // Calculate the distance between the closest point and the circle's center
        double distance = Math.sqrt(Math.pow(circle.getPosition().x - closestX, 2) + Math.pow(circle.getPosition().y - closestY, 2));

        // Check if the distance is less than or equal to the circle's radius
        if (distance <= circle.getPhantomWidth() / 2) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isOverlap(PhantomService o1, PhantomService o2) {

        double dist = Math.sqrt(Math.pow(o1.getPosition().x - o2.getPosition().x, 2) + Math.pow(o1.getPosition().y - o2.getPosition().y, 2));

        if (o1.getPhantomForm() instanceof Circle && o2.getPhantomForm() instanceof Circle) {
            return o1.getPhantomWidth() / 2 + o2.getPhantomWidth() / 2 >= dist;
        } else {
            if (o1.getPhantomForm() instanceof Rectangle && o2.getPhantomForm() instanceof Rectangle) {
                return collisionRectRect(o1, o2);
            } else {
                if (o1.getPhantomForm() instanceof Rectangle && o2.getPhantomForm() instanceof Circle) {
                    return collisionCircleRect(o2, o1);
                } else {
                    if (o1.getPhantomForm() instanceof Circle && o2.getPhantomForm() instanceof Rectangle) {
                        return collisionCircleRect(o1, o2);
                    }
                }
            }

        }
        return false;
    }
}
