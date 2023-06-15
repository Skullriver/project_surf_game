/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/Data.java 2015-03-11 buixuan.
 * ******************************************************/
package data;

import data.ia.Phantom;
import javafx.scene.shape.Rectangle;
import specifications.DataService;
import specifications.PhantomService;
import tools.Parameters;
import tools.Position;
import tools.Position.COMMAND;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Data implements DataService {

    private Position heroesPosition;
    private Integer heroLP;

    private Rectangle hero;
    private boolean clignote = false;
    private int heroDashBar;
    private int stepNumber, score, storedScore;
    private ArrayList<PhantomService> objects;
    private double heroesWidth, heroesHeight,
            bigPhantomWidth, bigPhantomHeight, rectPhantomHeight, rectPhantomWidth;
    private boolean isFinish;

    public Data() {
    }

    @Override
    public void init() {
        //hercules = new Heroes;
        heroesPosition = new Position(Parameters.heroesStartX, Parameters.heroesStartY);
        hero = new Rectangle(Parameters.heroesStartX,
                Parameters.heroesStartY,
                Parameters.heroesWidth, Parameters.heroesHeight);
        heroDashBar = 0;
        objects = new ArrayList<PhantomService>();
        stepNumber = 0;
        score = 0;
        storedScore = readStoredScore();
        // Le fichier d'entrée

        heroesWidth = Parameters.heroesWidth;
        heroesHeight = Parameters.heroesHeight;
        bigPhantomWidth = Parameters.bigPhantomWidth;
        bigPhantomHeight = Parameters.bigPhantomHeight;

        rectPhantomHeight = Parameters.bigPhantomHeight;
        rectPhantomWidth = Parameters.bigPhantomWidth;

        clignote = false;
        heroLP = 3;
        isFinish = false;
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            objects.add(new Phantom(new Position(r.nextInt(Parameters.defaultWidth),
                    r.nextInt(Parameters.defaultHeight)), Position.COMMAND.UP, Parameters.phantomType.bubble, new Rectangle()));
        }
    }

    private int readStoredScore() {
        double storedScore = 0;

        File file = new File("score.txt");
        if (!file.exists()) {
            storedScore = 0;
        }else{
            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                String line;
                line = br.readLine();
                if (line != null) {
                    String storedScoreStr = line.split(":")[1];
                    storedScore = Double.parseDouble(storedScoreStr);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        return (int) storedScore;
    }

    public Rectangle getHeroesObject() {
        return hero;
    }

    public int getHeroDashBar() {
        return heroDashBar;
    }

    @Override
    public Position getHeroesPosition() {
        return heroesPosition;
    }

    @Override
    public double getHeroesWidth() {
        return heroesWidth;
    }

    @Override
    public double getHeroesHeight() {
        return heroesHeight;
    }


    @Override
    public int getStepNumber() {
        return stepNumber;
    }

    @Override
    public void setStepNumber(int n) {
        stepNumber = n;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getStoredScore() {
        return storedScore;
    }

    @Override
    public ArrayList<PhantomService> getObjects() {
        return objects;
    }

    @Override
    public void setObjects(ArrayList<PhantomService> objects) {
        this.objects = objects;
    }

    @Override
    public void addScore(int score) {
        this.score += score;
    }

    @Override
    public void addObject(Position p, COMMAND cmd, double width, double height, Parameters.phantomType type, Object form) {
        objects.add(new Phantom(p, cmd, width, height, type, form));
    }
    @Override
    public void addMob(Position p, PhantomService b){
        objects.add(b);
    }
    public void addDashScore() {
        heroDashBar = Math.min(3, heroDashBar + 1);
    }

    @Override
    public void addHearthScore() {
        heroLP = Math.min(3, heroLP + 1);

    }
    public void removeDashScore() {
        heroDashBar = Math.max(0, heroDashBar - 1);
    }

    @Override
    public boolean isClignote() {
        return clignote;
    }

    @Override
    public void setClignote(boolean c) {
        clignote = c;
    }
    @Override
    public Integer getLp() {
        return heroLP;
    }

    @Override
    public void setLp(int toRemove) {
        heroLP -= toRemove;

    }

    @Override
    public boolean isFinish() {
        return isFinish;
    }

    @Override
    public void setFinish(boolean f) {
        isFinish = f;

    }
    @Override
    public void setPhantomCommand(Position.COMMAND c) {

        for (PhantomService p : objects) {
            p.setAction(c);
        }

    }

}

