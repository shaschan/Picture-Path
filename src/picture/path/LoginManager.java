package picture.path;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/** Manages control flow for logins */
public class LoginManager {
  private Scene scene;

  public LoginManager(Scene scene) {
    this.scene = scene;
  }

  /**
   * Callback method invoked to notify that a user has been authenticated.
   * Will show the main application screen.
     * @param sessionID
   */ 
  public void dataFeederStart() {
    try{ 
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("FXMLDocument.fxml")
      );
      scene.setRoot((Parent) loader.load());
      scene.getWindow().sizeToScene();
      FXMLDocumentController controller = 
        loader.<FXMLDocumentController>getController();
    controller.initialize();} 
    catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }  
    //showMainView(sessionID);
  }

  /**
   * Callback method invoked to notify that a user has logged out of the main application.
   * Will show the login application screen.
   */ 
  public void logout() {
    showLoginScreen();
  }
  
  public void showLoginScreen() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("login.fxml")
      );
      scene.setRoot((Parent) loader.load());
      LoginController controller = 
        loader.<LoginController>getController();
      controller.initManager(this);
    } catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void resultViewerStart() {
    try {
      FXMLLoader loader = new FXMLLoader(
        getClass().getResource("resultViewer.fxml")
      );
      scene.setRoot((Pane) loader.load());
      scene.getWindow().sizeToScene();
      ResultViewerController controller = 
        loader.<ResultViewerController>getController();
      controller.initialize();
    } catch (IOException ex) {
      Logger.getLogger(LoginManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
}
