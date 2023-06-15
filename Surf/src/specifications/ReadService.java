/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ReadService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import tools.Position;

import java.util.ArrayList;

public interface ReadService {
  public Position getHeroesPosition();
  public double getHeroesWidth();
  public double getHeroesHeight();
  public boolean isClignote();
  public int getStepNumber();
  public int getScore();
  public int getStoredScore();
  public ArrayList<PhantomService> getObjects();
  public Integer getLp();
  public boolean isFinish();
  public int getHeroDashBar();
}
