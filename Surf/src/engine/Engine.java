/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: engine/Engine.java 2015-03-11 buixuan.
 * ******************************************************/
package engine;

import data.ia.Mob;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import specifications.DataService;
import specifications.EngineService;
import specifications.PhantomService;
import specifications.RequireDataService;
import tools.Parameters;
import tools.Position;
import userInterface.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Engine implements EngineService, RequireDataService {
    private static final double friction = Parameters.friction;
    private static double velocity = Parameters.velocity;
    private Timer engineClock;
    private DataService data;
    private Position.COMMAND command;
    private Random gen;
    private Object objects;
    private boolean moveLeft, moveRight, moveUp, moveDown;
    private double heroesVX, heroesVY;
    private Position.COMMAND lastCmd = Position.COMMAND.UP;
    private boolean dash = false;
    private int dashTime = 0;
    private boolean isSlowed;
    private boolean isCollision = false;

    private Timeline timeline;

    private AnimationTimer animationTimer;
    private int stopTime;
    private Player player;
    private long lastTime;


    public Engine() {
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public void bindDataService(DataService service) {
        data = service;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void init() {

        engineClock = new Timer();
        objects = new Object();
        command = Position.COMMAND.UP;
        gen = new Random();
        moveLeft = false;
        moveRight = false;
        moveUp = false;
        moveDown = false;
        isSlowed = false;
        isCollision = false;
        velocity = Parameters.velocity;
        stopTime = 0;

        setDash(false);
        objects.bindDataService(data);
        objects.bindEngineService(this);
        player.setState(3);
        setLastCmd(command);
    }

    public boolean isDash() {

        if (!dash) {
            return false;
        } else {
            if (dashTime <= 0) {
                dash = false;
                return false;
            }
        }
        return true;
    }

    public Position.COMMAND getLastCommand() {
        return lastCmd;
    }

    public double getVelocity() {
        return velocity;
    }

    @Override
    public void start() {

        lastTime = System.nanoTime();
        //Update speed with acceleration
        animationTimer = new AnimationTimer() {
            public void handle(long now) {

                if (!isSlowed && !isCollision) {
                    // Calculate time elapsed since last frame in seconds
                    double deltaTime = (now - lastTime) / 1e9; // 1e9 nanoseconds per second

                    //Raise speed
                    velocity = Math.min(6, velocity + friction * deltaTime);

                    // Store the current time for use in the next frame
                    lastTime = now;
                } else if (!isCollision) {
                    velocity = 1.2;
                }
                if (isCollision) {
                    velocity = 0;
                }
            }
        };

        animationTimer.start();

        engineClock.schedule(new TimerTask() {
            public void run() {
                if (lastCmd != Position.COMMAND.DOWN) {
                    data.addScore((int) velocity);
                }
                isSlowed = false;
                waitStop();


                if (isDash()) {
                    dashTime = dashTime - 1;
                }
                if (stopTime == 0) {
                    //Do not spawn when not moving
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.025 + velocity / 60)
                        objects.spawnPhantom();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.015 + velocity / 80)
                        objects.spawnStructure();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.005 + velocity / 60)
                        objects.spawnVortex();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.005)
                        objects.spawnDash();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.005)
                        objects.spawnHeart();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.005 + velocity / 60)
                        objects.spawnMob();
                    if (lastCmd != Position.COMMAND.DOWN && gen.nextFloat() < 0.3 + velocity / 60)
                        objects.spawnBubble();

                }

            }
        }, 0, Parameters.enginePaceMillis);

        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {

            ArrayList<PhantomService> phantoms = new ArrayList<PhantomService>();

            for (PhantomService p : data.getObjects()) {

                if (p.getAction() == Position.COMMAND.LEFT) moveLeft(p);
                if (p.getAction() == Position.COMMAND.RIGHT) moveRight(p);
                if (p.getAction() == Position.COMMAND.UP) moveUp(p);
                if (p.getAction() == Position.COMMAND.DOWN) moveDown(p);
                if (p.getAction() == Position.COMMAND.DOUBLE_LEFT) moveDoubleLeft(p);
                if (p.getAction() == Position.COMMAND.DOUBLE_RIGHT) moveDoubleRight(p);

                if (p.getPhantomType() == Parameters.phantomType.roundObstacle) {
                    if (collisionHeroesPhantom(p)) {
                        isCollision = true;
                        stopTime = 10;
                        data.setClignote(true);
                        data.setLp(1);
                        this.dash = false;
                        if (data.getLp() == 0) {
                            data.setFinish(true);
                            stop();
                        }
                    } else {
                        if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                    }
                }
                if (p.getPhantomType() == Parameters.phantomType.slowdownObstacle) {
                    if (collisionHeroesRectPhantom(p)) {
                        isSlowed = true;
                    }
                    if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                }
                if (p.getPhantomType() == Parameters.phantomType.dash) {
                    if (collisionHeroesPhantom(p)) {
                        data.addDashScore();
                    } else {
                        if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                    }
                }

                if (p.getPhantomType() == Parameters.phantomType.heart) {
                    if (collisionHeroesPhantom(p)) {
                        data.addHearthScore();
                    } else {
                        if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                    }
                }
                if (p.getPhantomType() == Parameters.phantomType.vortex) {
                    if (collisionHeroesPhantom(p)) {
                        if (lastCmd == Position.COMMAND.DOUBLE_LEFT) {
                            player.setState(1);
                            setLastCmd(Position.COMMAND.DOUBLE_RIGHT);
                            data.setPhantomCommand(Position.COMMAND.DOUBLE_RIGHT);
                        } else if (lastCmd == Position.COMMAND.DOUBLE_RIGHT) {
                            player.setState(5);
                            setLastCmd(Position.COMMAND.DOUBLE_LEFT);
                            data.setPhantomCommand(Position.COMMAND.DOUBLE_LEFT);
                        } else if (lastCmd == Position.COMMAND.LEFT) {
                            player.setState(2);
                            setLastCmd(Position.COMMAND.RIGHT);
                            data.setPhantomCommand(Position.COMMAND.RIGHT);
                        } else if (lastCmd == Position.COMMAND.RIGHT) {
                            player.setState(4);
                            setLastCmd(Position.COMMAND.LEFT);
                            data.setPhantomCommand(Position.COMMAND.LEFT);
                        } else if (lastCmd == Position.COMMAND.UP) {
                            Random r = new Random();
                            if (r.nextFloat() < 0.5) {
                                player.setState(4);
                                setLastCmd(Position.COMMAND.LEFT);
                                data.setPhantomCommand(Position.COMMAND.LEFT);
                            } else {
                                player.setState(2);
                                setLastCmd(Position.COMMAND.RIGHT);
                                data.setPhantomCommand(Position.COMMAND.RIGHT);
                            }
                        }
                    } else {
                        if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                    }

                }
                if (p.getPhantomType() == Parameters.phantomType.vortexTexture) {
                    if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                }
                if (p.getPhantomType() == Parameters.phantomType.bubble) {
                    if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                }
                if (p.getPhantomType() == Parameters.phantomType.mob) {
                    if (collisionHeroesPhantom(p)) {
                        isCollision = true;
                        stopTime = 10;
                        data.setClignote(true);
                        data.setLp(1);
                        this.dash = false;
                        if (data.getLp() == 0) {
                            data.setFinish(true);
                            stop();
                        }

                    } else {
                        Mob b = (Mob) p;

                        Position.COMMAND a = ((Mob) p).getProperAction();
                        Position pos = b.getPosition();
                        if (a == Position.COMMAND.UP) {
                            b.setPosition(new Position(pos.x, pos.y));
                        } else if (a == Position.COMMAND.LEFT) {
                            b.setPosition(new Position(pos.x + 1.5 * Math.sin(Math.PI / 12), pos.y + 1.5));
                        } else if (a == Position.COMMAND.RIGHT) {
                            b.setPosition(new Position(pos.x - 1.5 * Math.sin(Math.PI / 12), pos.y + 1.5));
                        } else if (a == Position.COMMAND.DOUBLE_RIGHT) {
                            b.setPosition(new Position(pos.x - 1.5 * Math.sin(2 * Math.PI / 15), pos.y + 1.5));
                        } else if (a == Position.COMMAND.DOUBLE_LEFT) {
                            b.setPosition(new Position(pos.x + 1.5 * Math.sin(2 * Math.PI / 15), pos.y + 1.5));
                        } else if (a == Position.COMMAND.DOWN) {
                            b.setPosition(new Position(pos.x, pos.y + 1.5));
                        }
                        if (gen.nextFloat() > 0.995) {
                            b.move();
                        }
                        ArrayList<PhantomService> copy = (ArrayList<PhantomService>) data.getObjects().clone();
                        copy.remove(p);
                        boolean flag = true;
                        for (PhantomService p2 : copy) {
                            if (p2.getPhantomType() == Parameters.phantomType.roundObstacle) {
                                if (collisionCircleCircle(p, p2)) {
                                    b.setDead();
                                }
                            }
                            if (p.getPhantomType() == Parameters.phantomType.vortex) {
                                if (collisionCircleCircle(p, p2)) {

                                }
                            }
                        }
                        if (p.getPosition().y + p.getPhantomHeight() > 0) phantoms.add(p);
                    }

                }

            }

            data.setObjects(phantoms);

            data.setStepNumber(data.getStepNumber() + 1);
        }));

        // Set the Timeline to repeat indefinitely
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void waitStop() {
        if (stopTime == 1) {
            velocity = Parameters.velocity;
            stopTime--;
            data.setClignote(false);
            isCollision = false;
        } else if (stopTime > 0) {
            stopTime = Math.max(0, stopTime - 1);
        }
    }

    @Override
    public void stop() {
        animationTimer.stop();
        engineClock.cancel();
        timeline.stop();
        player.setState(6);

        double score = data.getScore();
        try {
            // Le fichier d'entrée
            File file = new File("score.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            line = br.readLine();

            double scoreToSave = score;
            if (line != null) {
                String storedScoreStr = line.split(":")[1];
                double storedScore = Double.parseDouble(storedScoreStr);
                scoreToSave = Math.max(storedScore, score);
            }

            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("score: " + scoreToSave);
            bw.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void moveLeft(PhantomService p) {
        if (isDash()) {
            p.setPosition(new Position(p.getPosition().x - (2 * velocity * Math.sin(Math.PI / 6)), p.getPosition().y - (2 * velocity * Math.cos(Math.PI / 6))));
        } else {
            p.setPosition(new Position(p.getPosition().x - velocity * Math.sin(Math.PI / 6), p.getPosition().y - velocity * Math.cos(Math.PI / 6)));
        }

    }

    private boolean collisionCircleCircle(PhantomService c1, PhantomService c2) {
//        double dist = Math.sqrt(Math.pow(c1.getPosition().x-c2.getPosition().x,2)+Math.pow(c1.getPosition().y-c2.getPosition().y,2));
//        return c1.getPhantomWidth()/2+c2.getPhantomWidth()/2<dist;

        return (
                (c1.getPosition().x - c2.getPosition().x) * (c1.getPosition().x - c2.getPosition().x) +
                        (c1.getPosition().y - c2.getPosition().y) * (c1.getPosition().y - c2.getPosition().y) <
                        0.25 * (c1.getPhantomWidth() + c2.getPhantomWidth()) * (c1.getPhantomWidth() + c2.getPhantomWidth())
        );

    }

    private void moveDoubleLeft(PhantomService p) {
        if (isDash()) {
            p.setPosition(new Position(p.getPosition().x - (2 * velocity * Math.sin(Math.PI / 4)), p.getPosition().y - (2 * velocity * Math.cos(Math.PI / 4))));
        } else {
            p.setPosition(new Position(p.getPosition().x - velocity * Math.sin(Math.PI / 4), p.getPosition().y - velocity * Math.cos(Math.PI / 4)));
        }

    }

    private void moveRight(PhantomService p) {
        if (isDash()) {
            p.setPosition(new Position((p.getPosition().x + 2 * velocity * Math.sin(Math.PI / 6)), (p.getPosition().y - 2 * velocity * Math.cos(Math.PI / 6))));
        } else {
            p.setPosition(new Position((p.getPosition().x + velocity * Math.sin(Math.PI / 6)), (p.getPosition().y - velocity * Math.cos(Math.PI / 6))));

        }
    }

    private void moveDoubleRight(PhantomService p) {
        if (isDash()) {
            p.setPosition(new Position(p.getPosition().x + (2 * velocity * Math.sin(Math.PI / 4)), p.getPosition().y - (2 * velocity * Math.cos(Math.PI / 4))));
        } else {
            p.setPosition(new Position(p.getPosition().x + velocity * Math.sin(Math.PI / 4), p.getPosition().y - velocity * Math.cos(Math.PI / 4)));
        }

    }

    private void moveUp(PhantomService p) {
        if (isDash()) {
            p.setPosition(new Position(p.getPosition().x, p.getPosition().y - 2 * velocity));
        } else {
            p.setPosition(new Position(p.getPosition().x, p.getPosition().y - velocity));
        }
    }

    private void moveDown(PhantomService p) {
        velocity = Parameters.velocity;
        dash = false;
        dashTime = 0;

        p.setPosition(new Position(p.getPosition().x, p.getPosition().y));
    }

    private boolean collisionHeroesPhantom(PhantomService p) {

        //P - circle, Hero - ellipse

        // Assume circle has center (cx, cy) and radius r
        // Assume ellipse has center (ex, ey), width w, and height h

        double dx = Math.abs(p.getPosition().x - data.getHeroesPosition().x);
        double dy = Math.abs(p.getPosition().y - data.getHeroesPosition().y);

        if (dx > data.getHeroesWidth() / 2 + p.getPhantomWidth() / 2 || dy > data.getHeroesHeight() / 2 + p.getPhantomWidth() / 2) {
            // No collision possible
            return false;
        }

        double x1 = data.getHeroesPosition().x - data.getHeroesWidth() / 2;
        double y1 = data.getHeroesPosition().y - data.getHeroesHeight() / 2;
        double x2 = data.getHeroesPosition().x + data.getHeroesWidth() / 2;
        double y2 = data.getHeroesPosition().y + data.getHeroesHeight() / 2;

        double distance;

        if (p.getPosition().x < x1) {
            if (p.getPosition().y < y1) {
                // Upper left corner
                distance = Math.sqrt((p.getPosition().x - x1) * (p.getPosition().x - x1) + (p.getPosition().y - y1) * (p.getPosition().y - y1));
            } else if (p.getPosition().y > y2) {
                // Lower left corner
                distance = Math.sqrt((p.getPosition().x - x1) * (p.getPosition().x - x1) + (p.getPosition().y - y2) * (p.getPosition().y - y2));
            } else {
                // Left edge
                distance = dx - data.getHeroesWidth() / 2;
            }
        } else if (p.getPosition().x > x2) {
            if (p.getPosition().y < y1) {
                // Upper right corner
                distance = Math.sqrt((p.getPosition().x - x2) * (p.getPosition().x - x2) + (p.getPosition().y - y1) * (p.getPosition().y - y1));
            } else if (p.getPosition().y > y2) {
                // Lower right corner
                distance = Math.sqrt((p.getPosition().x - x2) * (p.getPosition().x - x2) + (p.getPosition().y - y2) * (p.getPosition().y - y2));
            } else {
                // Right edge
                distance = dx - data.getHeroesWidth() / 2;
            }
        } else {
            if (p.getPosition().y < y1) {
                // Top edge
                distance = dy - data.getHeroesHeight() / 2;
            } else if (p.getPosition().y > y2) {
                // Bottom edge
                distance = dy - data.getHeroesHeight() / 2;
            } else {
                // Inside ellipse
                distance = 0;
            }
        }

        return distance <= p.getPhantomWidth() / 2;
    }

    private boolean collisionHeroesRectPhantom(PhantomService p) {

        double ellipseCenterX = data.getHeroesPosition().x;
        double ellipseCenterY = data.getHeroesPosition().y;
        double rectCenterX = p.getPosition().x + p.getPhantomWidth() / 2;
        double rectCenterY = p.getPosition().y + p.getPhantomHeight() / 2;

        double deltaX = Math.abs(ellipseCenterX - rectCenterX);
        double deltaY = Math.abs(ellipseCenterY - rectCenterY);

        double radiusX = data.getHeroesWidth() / 2;
        double radiusY = data.getHeroesHeight() / 2;

        if (deltaX > (p.getPhantomWidth() / 2 + radiusX)) {
            return false;
        }
        if (deltaY > (p.getPhantomHeight() / 2 + radiusY)) {
            return false;
        }

        double cornerDistance_sq = Math.pow(deltaX - p.getPhantomWidth() / 2, 2) +
                Math.pow(deltaY - p.getPhantomHeight() / 2, 2);

        return (cornerDistance_sq <= Math.pow(radiusX, 2) ||
                cornerDistance_sq <= Math.pow(radiusY, 2));

//        if ((data.getHeroesPosition().x > p.getPosition().x && data.getHeroesPosition().x < p.getPosition().x + p.getPhantomWidth())
//                && data.getHeroesPosition().y > p.getPosition().y && data.getHeroesPosition().y < p.getPosition().y + p.getPhantomHeight()) {
//            return true;
//        }
//
//        // Find the closest point on the rectangle's perimeter to the circle's center
//        double closestX = clamp(data.getHeroesPosition().x, p.getPosition().x, p.getPosition().x + p.getPhantomWidth());
//        double closestY = clamp(data.getHeroesPosition().y, p.getPosition().y, p.getPosition().y + p.getPhantomHeight());
//
//        // Calculate the distance between the closest point and the circle's center
//        double distance = Math.sqrt(Math.pow(data.getHeroesPosition().x - closestX, 2) + Math.pow(data.getHeroesPosition().y - closestY, 2));
//
//        // Check if the distance is less than or equal to the circle's radius
//        if (distance <= data.getHeroesWidth() / 2) {
//            return true;
//        } else {
//
//            return false;
//        }

    }

    public Position.COMMAND getLastCmd() {
        return lastCmd;
    }

    public void setLastCmd(Position.COMMAND lastCmd) {
        this.lastCmd = lastCmd;
    }

    @Override
    public boolean getDash() {
        return dash;
    }

    @Override
    public void setDash(boolean dash) {
        if (dash) {

            this.dash = dash;
            dashTime = 100;
        } else {
            this.dash = false;
        }

    }
}
