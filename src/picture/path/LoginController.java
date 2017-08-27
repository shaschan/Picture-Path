package picture.path;

import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/** Controls the login screen */
public class LoginController {
  @FXML private TextField user;
  @FXML private TextField password;
  @FXML private ChoiceBox choice;
  @FXML private Button loginButton;
  @FXML private Label choiceText;
  
  public void initialize() {}
  
  public void initManager(final LoginManager loginManager) {
    loginButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override public void handle(ActionEvent event) {
        String sessionID = authorize();
        if (sessionID != null && choice.getSelectionModel().getSelectedItem() != null) {
            if(choice.getSelectionModel().getSelectedItem().toString().equals("Data Feeder"))
                loginManager.dataFeederStart();
            else
                loginManager.resultViewerStart();
        }
      }
    });
    
    choice.setOnAction((q) -> { 
        if(choice.getSelectionModel().getSelectedItem() != null){
            choiceText.setVisible(false);
        }else{
            choiceText.setVisible(true);
        }
    });
  }

  /**
   * Check authorization credentials.
   * 
   * If accepted, return a sessionID for the authorized session
   * otherwise, return null.
   */   
  private String authorize() {
    return 
      "open".equals(user.getText()) && "sesame".equals(password.getText()) 
            ? generateSessionID() 
            : null;
  }
  
  public int sessionID = 0;

  private String generateSessionID() {
    sessionID++;
    return "xyzzy - session " + sessionID;
  }
}
