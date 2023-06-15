package data.ia;

import specifications.PhantomService;
import tools.Parameters;
import tools.Position;
import userInterface.Enemy;

import java.util.Random;

public class Mob extends Phantom implements PhantomService {
    private Position.COMMAND action;
    private Enemy texture;

    public Mob(Position p, Position.COMMAND m, Parameters.phantomType type, Object form){

        super(p, m, Parameters.mobWidth, Parameters.mobHeight, type, form);
        action = Position.COMMAND.DOWN;
        texture = new Enemy();
        texture.setAvatar();
    }
    public Position.COMMAND getProperAction()
    {
        return action;
    }

    public Enemy getTexture() {
        return texture;
    }

    public void move(){
        Random r = new Random();
        int n = r.nextInt(6);
        if(n == 0) {
            action = Position.COMMAND.DOUBLE_RIGHT;
            texture.setState(0);
        } else if (n==1) {
            action = Position.COMMAND.DOUBLE_LEFT;
            texture.setState(4);
        } else if (n==2) {
            action = Position.COMMAND.LEFT;
            texture.setState(3);
        }else if (n == 3){
            action = Position.COMMAND.RIGHT;
            texture.setState(1);
        } else{
            action = Position.COMMAND.DOWN;
            texture.setState(2);
        }
    }

    public void setDead()
    {
        action = Position.COMMAND.UP;
        texture.setState(5);
    }

}
