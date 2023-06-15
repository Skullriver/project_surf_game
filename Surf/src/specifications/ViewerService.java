/* ******************************************************
 * Project alpha - Composants logiciels 2015.
 * Copyright (C) 2015 <Binh-Minh.Bui-Xuan@ens-lyon.org>.
 * GPL version>=3 <http://www.gnu.org/licenses/>.
 * $Id: specifications/ViewerService.java 2015-03-11 buixuan.
 * ******************************************************/
package specifications;

import javafx.scene.Parent;
import javafx.scene.image.ImageView;

public interface ViewerService{
  public void init();
  public Parent getPanel();
  public void setMainWindowWidth(double w);
  public void setMainWindowHeight(double h);
  public ImageView getGameOverImage();
}
