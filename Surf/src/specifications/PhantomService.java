/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/PhantomService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Parameters;
import tools.Position;
import userInterface.Obstacle;

public interface PhantomService {
    public Position getPosition();

    public void setPosition(Position p);

    public Position.COMMAND getAction();

    public void setAction(Position.COMMAND c);

    public double getPhantomWidth();

    public void setPhantomWidth(double width);

    public double getPhantomHeight();

    public void setPhantomHeight(double height);

    public Parameters.phantomType getPhantomType();
    public void setPhantomType(Parameters.phantomType type);

    public Object getPhantomForm();
    public void setPhantomForm(Object form);
    public Obstacle getAvatar();
}
