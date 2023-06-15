/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: data/ia/MoveLeftPhantom.java 2015-03-11 buixuan.
 * ******************************************************/
package data.ia;

import specifications.PhantomService;
import tools.Parameters;
import tools.Position;
import userInterface.Enemy;
import userInterface.Obstacle;

public class Phantom implements PhantomService {
    private Position position;
    private Position.COMMAND action;
    private double phantomWidth, phantomHeight;
    private Parameters.phantomType phantomType;
    private Object phantomForm;

    private final Obstacle texture;

    public Phantom(Position p, Position.COMMAND m, Parameters.phantomType type, Object form) {
        position = p;
        action = m;

        phantomType = type;
        phantomForm = form;

        phantomWidth = Parameters.phantomWidth;
        phantomHeight = Parameters.phantomHeight;

        texture = new Obstacle();
        if(type == Parameters.phantomType.roundObstacle)
            texture.setAvatar();
    }

    public Phantom(Position p, Position.COMMAND m, double width, double height, Parameters.phantomType type, Object form) {
        position = p;
        action = m;

        phantomType = type;
        phantomForm = form;

        phantomWidth = width;
        phantomHeight = height;


        texture = new Obstacle();
        if(type == Parameters.phantomType.roundObstacle)
            texture.setAvatar();
    }

    public Obstacle getAvatar() {
        return texture;
    }
    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position p) {
        position = p;
    }

    @Override
    public Position.COMMAND getAction() {
        return this.action;
    }

    public void setAction(Position.COMMAND c) {
        action = c;
    }

    @Override
    public double getPhantomWidth() {
        return phantomWidth;
    }

    @Override
    public void setPhantomWidth(double width) {
        phantomWidth = width;
    }

    @Override
    public double getPhantomHeight() {
        return phantomHeight;
    }

    @Override
    public void setPhantomHeight(double height) {
        phantomHeight = height;
    }

    @Override
    public Parameters.phantomType getPhantomType() {
        return phantomType;
    }

    @Override
    public void setPhantomType(Parameters.phantomType type) {
        phantomType = type;
    }

    @Override
    public Object getPhantomForm() {
        return phantomForm;
    }

    @Override
    public void setPhantomForm(Object form) {
        phantomForm = form;
    }
}
