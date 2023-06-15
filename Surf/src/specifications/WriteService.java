/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/WriteService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Parameters;
import tools.Position;
import tools.Position.COMMAND;

import java.util.ArrayList;

public interface WriteService {


    public void setStepNumber(int n);

    public void addObject(Position p, COMMAND lastCmd, double width, double height, Parameters.phantomType type, Object form);
    public void addMob(Position p, PhantomService mob);

    public void setObjects(ArrayList<PhantomService> obstacles);

    public void addScore(int score);

    public void setLp(int toRemove);

    public void setFinish(boolean f);

    public void removeDashScore();

    public void setClignote(boolean c);

    public void addDashScore();

    public void addHearthScore();

    public void setPhantomCommand(Position.COMMAND c);
}
