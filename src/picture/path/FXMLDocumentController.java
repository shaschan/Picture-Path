/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package picture.path;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.ParseException;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DateTimeStringConverter;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Dell
 */
public class FXMLDocumentController{
    
    @FXML
    private Button selImg;
    @FXML
    private Button selVid;
    @FXML
    private Button go;
    @FXML
    private Button reset;
    @FXML
    private TextField rd;
    @FXML
    private TextField md;
    @FXML
    private TextField pd;
    @FXML
    private Label genderText;
    @FXML
    private Label drsTypText;
    @FXML
    private Label imgText;
    @FXML
    private Label message;
    @FXML
    private Label vdText;
    @FXML
    private Button save;
    @FXML
    private Button clear;
//    @FXML
//    private Button export;
    @FXML
    private Pane imgPane;
    @FXML
    private Pane cnv;
    @FXML
    private TextField entTime;
    @FXML
    private TextField exitTime;
    @FXML
    private TextField age;
    @FXML
    private TextField idnMark;
    @FXML
    private DatePicker dateAt;
    @FXML
    private ChoiceBox gender;
    @FXML
    private ChoiceBox drsTyp;
    @FXML
    private TextField botCol;
    @FXML
    private TextField topCol;
    @FXML
    private Label beard;
    @FXML
    private RadioButton beardYes;
    @FXML
    private RadioButton beardNo;
    @FXML
    private RadioButton SpectaclesNo;
    @FXML
    private RadioButton SpectaclesYes;
    @FXML
    private TextField spcMark;
    @FXML
    private Label imp, imp1, imp2, imp3, imp4, imp5, imp6, imp7, imp8, imp9;
    private Connection connection;
    private Boolean onClearRDEnable = false;
    private Boolean startedFlag = false;
    private Point startPoint = null;
    private Point endPoint = null;
    private Image pubImg = null;
    private File pubImgFile = null;
    private File pubVidFile = null;
    private ArrayList<Double>[] allXs =(ArrayList<Double>[])new ArrayList[1];
    private ArrayList<Double>[] allYs =(ArrayList<Double>[])new ArrayList[1]; 
    private HashMap<Point, String> entMap =new HashMap<Point, String>();
    private HashMap<Point, String> exitMap =new HashMap<Point, String>();
    private HashMap<Integer,ArrayList<Point>> stopsMap =new HashMap<Integer, ArrayList<Point>>();
    private HashMap<Integer, ContextMenu> stopMaptoCntMenu = new HashMap<Integer, ContextMenu>();
    private int[] counter = {0};
    private int[] countKey = {0};
    private long goTime = 0;
    private long saveTime = 0;

    public class contMenu{
        public ContextMenu set(Canvas canv, double x,double y){
            ArrayList<Point> ptsNearBy = new ArrayList<Point>();
            ptsNearBy.add(new Point((int)x,(int)y));
            ptsNearBy.add(new Point((int)x+1,(int)y));
            ptsNearBy.add(new Point((int)x+2,(int)y));ptsNearBy.add(new Point((int)x,(int)y+1));
            ptsNearBy.add(new Point((int)x,(int)y+2));ptsNearBy.add(new Point((int)x+1,(int)y+1));
            ptsNearBy.add(new Point((int)x+2,(int)y+2));ptsNearBy.add(new Point((int)x+1,(int)y+2));
            ptsNearBy.add(new Point((int)x+2,(int)y+1));
            
            ptsNearBy.add(new Point((int)x-1,(int)y));
            ptsNearBy.add(new Point((int)x-2,(int)y));ptsNearBy.add(new Point((int)x,(int)y-1));
            ptsNearBy.add(new Point((int)x,(int)y-2));ptsNearBy.add(new Point((int)x-1,(int)y-1));
            ptsNearBy.add(new Point((int)x-2,(int)y-2));ptsNearBy.add(new Point((int)x-1,(int)y-2));
            ptsNearBy.add(new Point((int)x-2,(int)y-1));
            
            ptsNearBy.add(new Point((int)x+1,(int)y-1));
            ptsNearBy.add(new Point((int)x+2,(int)y-2));ptsNearBy.add(new Point((int)x+1,(int)y-2));
            ptsNearBy.add(new Point((int)x+2,(int)y-1));
            
            ptsNearBy.add(new Point((int)x-1,(int)y+1));
            ptsNearBy.add(new Point((int)x-2,(int)y+2));ptsNearBy.add(new Point((int)x-1,(int)y+2));
            ptsNearBy.add(new Point((int)x-2,(int)y+1));

            TextField ssstt = new TextField();
            ssstt.setPromptText("HH:MM:SS, Press Enter");
            
            TextField eeett = new TextField();
            eeett.setPromptText("HH:MM:SS, Press Enter");
            
            CustomMenuItem markent = new CustomMenuItem(ssstt);
            markent.setHideOnClick(false);
            
            CustomMenuItem markexit = new CustomMenuItem(eeett);
            markexit.setHideOnClick(false);
            
            Menu ent = new Menu("Select Start time of this Stop");
            ent.getItems().add(markent);
            
            Menu ext = new Menu("Select Exit time of this Stop");
            ext.getItems().add(markexit);
            
            MenuItem markEnd = new MenuItem("Click to End Path");
            MenuItem markStart = new MenuItem("Click to Start Path");

            
            ssstt.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!matcheTime(newValue)) {
                    ssstt.setText(oldValue);
                }
            });

            eeett.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!matcheTime(newValue)) {
                    eeett.setText(oldValue);
                }
            });
            
            ssstt.setOnAction((o)->{
                System.out.println("Start Loc::"+x+", "+y);
                if(ssstt.getText().length() == 8){
                    entMap.put(new Point((int)x,(int)y), ssstt.getText().trim());
                    ent.setText("Already Set: "+ssstt.getText());
                    ssstt.setStyle("-fx-background-color:#90EE90;");
                    ent.setStyle("-fx-background-color:#90EE90;");
                }else{
                    ent.setText("Error Setting Time :: Check Format!");
                    ssstt.setStyle("-fx-background-color:#fd0a2a;");
                }
            });
            
            eeett.setOnAction((o)->{
                if(eeett.getText().length() == 8){
                    System.out.println("End Loc::"+x+", "+y);
                    exitMap.put(new Point((int)x,(int)y), eeett.getText().trim());
                    ext.setText("Already Set: "+eeett.getText());
                    eeett.setStyle("-fx-background-color:#90EE90;");
                    ext.setStyle("-fx-background-color:#90EE90;");
                }else{
                    ext.setText("Error Setting Time :: Check Format!");
                    eeett.setStyle("-fx-background-color:#fd0a2a;");
                }
            });
            
            markEnd.setOnAction((o)->{
                startedFlag = false;
                endPoint = new Point((int)x,(int)y);
                for(int l = 0;l < stopMaptoCntMenu.size(); l++){
                    stopMaptoCntMenu.get(l).getItems().get(2).setDisable(startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(3).setDisable(!startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(0).setDisable(!startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(1).setDisable(!startedFlag);
                }
                imgPane.setDisable(false);
            });
            
            markStart.setOnAction((o)->{
                startPoint = new Point((int)x,(int)y);
                startedFlag = true;
                for(int l = 0;l < stopMaptoCntMenu.size(); l++){
                    stopMaptoCntMenu.get(l).getItems().get(2).setDisable(startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(3).setDisable(!startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(0).setDisable(!startedFlag);
                    stopMaptoCntMenu.get(l).getItems().get(1).setDisable(!startedFlag);
                }
            });

            markStart.setDisable(startedFlag);
            markEnd.setDisable(!startedFlag);
            ent.setDisable(!startedFlag);
            ext.setDisable(!startedFlag);

            ContextMenu cntmn = new ContextMenu();
            // Add MenuItem to ContextMenu
            cntmn.getItems().addAll(ent,ext,markStart,markEnd);

            stopsMap.put(countKey[0],ptsNearBy);
            stopMaptoCntMenu.put(countKey[0], cntmn);
            countKey[0]++;
            return cntmn;
        }
    }

//    public void delCtxMn(int i){
//        if(stopMaptoCntMenu.containsKey(i))
//            stopMaptoCntMenu.put(i, null);
//        if(stopsMap.containsKey(i)){
//            for(int k=0; k < stopsMap.get(i).size(); k++){
//                for(ArrayList<Point> key : entMap.keySet()){
//                    int x = stopsMap.get(i).get(k).x;
//                    int y = stopsMap.get(i).get(k).y;
//                    if(key.contains(new Point(x, y))){
//                        entMap.put(key, null);
//                    }
//                }
//                for(ArrayList<Point> key : exitMap.keySet()){
//                    int x = stopsMap.get(i).get(k).x;
//                    int y = stopsMap.get(i).get(k).y;
//                    if(key.contains(new Point(x, y))){
//                        exitMap.put(key, null);
//                    }
//                }
//            }
//            stopsMap.put(i, null);
//        }
//
//    }

    public void delAllCtxMn(){
        stopMaptoCntMenu.clear();
        stopsMap.clear();
        entMap.clear();
        exitMap.clear();
        countKey[0] = 0;
    }
    public boolean matcheTime(String timePart){
        Pattern pattern;
        Matcher matcher;
        if(timePart != null)
        switch(timePart.length()){
            case 0:
                 return true;
            case 1:
                pattern = Pattern.compile("[012]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 2:
                pattern = Pattern.compile("[01][0-9]|2[0-3]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 3:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 4:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 5:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 6:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]:");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 7:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]:[0-5]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            case 8:
                pattern = Pattern.compile("([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]");
                matcher = pattern.matcher(timePart);
                if(matcher.matches()) return true; else return false;
            default:
                return false;
        }
        return false;
    }
    public void initialize() {
        try {
             Class.forName("com.mysql.jdbc.Driver");
            //connection = DriverManager.getConnection("jdbc:mysql://192.168.1.78:3306/test", "root", "mysql5878");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/picpath", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

        go.setDisable(true);
        save.setDisable(true);
        imgPane.setDisable(true);
        selImg.setDisable(true);
        selVid.setDisable(true);
        clear.setDisable(true);

        ToggleGroup beards = new ToggleGroup();
        beardYes.setToggleGroup(beards);
        beardNo.setToggleGroup(beards);
        
        ToggleGroup spects = new ToggleGroup();
        SpectaclesYes.setToggleGroup(spects);
        SpectaclesNo.setToggleGroup(spects);
        
        gender.setOnAction((q) -> { 
            if(gender.getSelectionModel().getSelectedItem() != null){
                genderText.setVisible(false);
                if(gender.getSelectionModel().getSelectedItem().toString().equals("Female")){
                    beard.setText("Beard? N.A.");
                    beardYes.setDisable(true);
                    beardNo.setDisable(true);
                    beards.selectToggle(beardNo);
                }else{
                    beard.setText("Beard?");
                    beardYes.setDisable(false);
                    beardNo.setDisable(false);
                }
            }else{
                genderText.setVisible(true);
            }
        });
        
        drsTyp.setOnAction(q -> {
            if(drsTyp.getSelectionModel().getSelectedItem() != null)
                drsTypText.setVisible(false);
            else
                drsTypText.setVisible(true);
        });
        
        save.setOnAction(q -> {
            if(rd.getText() == null || rd.getText().equals("")){
                message.setText("Error::: Respondent ID not set!!");
                message.setStyle("-fx-background-color:#d41616;");
                return;
            }
            String VidFileNewPath = "outputs/"+pd.getText()+"/"+md.getText()+"/video/File_"+rd.getText()+"_"+pubVidFile.getName();
            String fileNewPathImg = "outputs/"+pd.getText()+"/"+md.getText()+"/image/File_"+rd.getText()+"_"+pubImgFile.getName();

            String parDirName = "outputs/"+pd.getText()+"/";
            File ParDirectory = new File(parDirName);
            if (!ParDirectory.exists()){
                    ParDirectory.mkdir();
            }
            String dirNameInn = "outputs/"+pd.getText()+"/"+md.getText()+"/";
            File directoryInn = new File(dirNameInn);
            if (!directoryInn.exists()){
                    directoryInn.mkdir();
            }
            String dirNameVid = "outputs/"+pd.getText()+"/"+md.getText()+"/video/";
            File directoryVid = new File(dirNameVid);
            if (!directoryVid.exists()){
                    directoryVid.mkdir();
            }
            
            String dirNameImg = "outputs/"+pd.getText()+"/"+md.getText()+"/image/";
            File directoryImg = new File(dirNameImg);
            if (!directoryImg.exists()){
                directoryImg.mkdir();
            }

            File[] directoryListingVid = directoryVid.listFiles();
            if (directoryListingVid != null) {
                for (File child : directoryListingVid) {
                    if(child.getName().split("_")[1].equals(rd.getText())){
                        try {
                            FileDeleteStrategy.FORCE.delete(child);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            try {
                Files.copy(pubVidFile.toPath(),  Paths.get(VidFileNewPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            File[] directoryListingImg = directoryImg.listFiles();
            if (directoryListingImg != null) {
                for (File child : directoryListingImg) {
                    if(child.getName().split("_")[1].equals(rd.getText())){
                        try {
                            FileDeleteStrategy.FORCE.delete(child);
                        } catch (IOException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            try {
                Files.copy(pubImgFile.toPath(),  Paths.get(fileNewPathImg), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            entTime,exitTime,age,idnMark,botCol,topCol,spcMark; TextField
//            gender,drsTyp; ChoiceBox
//            beardYes,beardNo,SpectaclesNo,SpectaclesYes RadioButton
                                                            //1 ---------------2---------3------------4-------5--------6-----------7----------8---------9---------------10--------
            String insertSQL = "INSERT INTO `maindata` (`imgFileName`, `vidFileName`, `projectID`, `mapID`, `resID`, `xCordSet`, `yCordSet`, `createdDate`, `entryTime`, `exitTime`, "
                    + "`gender`, `age`, `identMarkers`, `dressType`, `topColor`, `bottomColor`, `specsMarker`, `beard`, `specs`, `stopXCordSet`, `stopYCordSet`, `startPoint`, "
                    //   -11-------12------13-----------------14---------15----------16--------------17-------------18------19 ------ 20-------------21--------------22-----------
                    + "`endPoint`,`stopEntryTimeSet`, `stopExitTimeSet`, `buildingDate`, `efficiencyTime`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    //----23----------24----------------25--------------------26--------------27--
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

                if((!imgText.getText().equals("No Image Selected")) && (!vdText.getText().equals("No Video Selected"))){
                    preparedStatement.setString(1, imgText.getText());
                    preparedStatement.setString(2, vdText.getText());
                }else{
                    message.setText("Error::: Please Check the Files Uploaded!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                if(!(pd.getText() == null) && (!pd.getText().equals("")) && (pd.getText().matches("^[1-9]\\d*$"))){
                    preparedStatement.setInt(3, Integer.parseInt(pd.getText()));
                }else{
                    message.setText("Error::: Please Check Project ID Set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                if(!(md.getText() == null) && (!md.getText().equals("")) && (md.getText().matches("^[1-9]\\d*$"))){
                    preparedStatement.setInt(4, Integer.parseInt(md.getText()));
                }else{
                    message.setText("Error::: Please Check Map ID Set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                if(!(rd.getText() == null) && (!rd.getText().equals("")) && (rd.getText().matches("^[1-9]\\d*$"))){
                    preparedStatement.setInt(5, Integer.parseInt(rd.getText()));
                }else{
                    message.setText("Error::: Please Check Respondent ID Set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                if(!(allXs[0] == null || allXs[0].isEmpty()) && !(allYs[0] == null || allYs[0].isEmpty())){
                    preparedStatement.setString(6, allXs[0].toString());
                    preparedStatement.setString(7, allYs[0].toString());
                }else{
                    message.setText("Error::: No path mapped to Uploaded!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }

                java.sql.Date sqlDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
                preparedStatement.setDate(8, sqlDate);

                if((exitTime.getText() != null) && (!exitTime.getText().trim().equals("")) && (exitTime.getText().trim().length() == 8) && (entTime.getText().trim().length() == 8) && (entTime.getText() != null) && (!entTime.getText().trim().equals(""))){
                    preparedStatement.setString(9, entTime.getText().trim());
                    preparedStatement.setString(10, exitTime.getText().trim());
                }else{
                    message.setText("Error::: Entry/Exit time properly not set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(gender.getSelectionModel().getSelectedItem() != null && !(gender.getSelectionModel().getSelectedItem().toString()).equals("")){
                    preparedStatement.setString(11, gender.getSelectionModel().getSelectedItem().toString());
                }else{
                    message.setText("Error::: Gender not selected!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(drsTyp.getSelectionModel().getSelectedItem() != null && !(drsTyp.getSelectionModel().getSelectedItem().toString()).equals("")){
                    preparedStatement.setString(14, drsTyp.getSelectionModel().getSelectedItem().toString());
                }else{
                    message.setText("Error::: Dress Type not selected!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if((age.getText() != null) && (!age.getText().trim().equals("")) && (age.getText().trim().matches("^[1-9]\\d*$")) && (idnMark.getText() != null) && (!idnMark.getText().trim().equals(""))
                    && (topCol.getText() != null) && (!topCol.getText().trim().equals("")) && (botCol.getText() != null) && (!botCol.getText().trim().equals(""))     ){
                    preparedStatement.setInt(12, Integer.parseInt(age.getText().trim()));
                    preparedStatement.setString(13, idnMark.getText().trim().toLowerCase());
                    preparedStatement.setString(15, topCol.getText().trim().toLowerCase());
                    preparedStatement.setString(16, botCol.getText().trim().toLowerCase());
                }else{
                    message.setText("Error::: Other infos not set correctly!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(spcMark.getText() != null && !spcMark.getText().trim().equals("")){
                    preparedStatement.setString(17, spcMark.getText().trim().toLowerCase());
                }else{
                    preparedStatement.setString(17, "");
                    message.setText("Warning::: No Special Markers Set!!!");
                    message.setStyle("-fx-background-color:#e0fb13;");
                }
                if(beards.getSelectedToggle() != null){
                    if(beards.getSelectedToggle().equals(beardYes))
                        preparedStatement.setString(18, "YES");
                    else
                        preparedStatement.setString(18, "NO");
                }else{
                    message.setText("Error::: Beard presence not selected!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(spects.getSelectedToggle() != null){
                    if(spects.getSelectedToggle().equals(SpectaclesYes))
                        preparedStatement.setString(19, "YES");
                    else
                        preparedStatement.setString(19, "NO");
                }else{
                    message.setText("Error::: Spectacle presence not selected!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(startPoint != null){
                    preparedStatement.setString(22, String.valueOf(startPoint.x)+","+String.valueOf(startPoint.y));
                }else{
                    message.setText("Error::: Start Point Not Set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(endPoint != null){
                    preparedStatement.setString(23, String.valueOf(endPoint.x)+","+String.valueOf(endPoint.y));
                }else{
                    message.setText("Error::: End Point Not Set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                if(!entMap.isEmpty() && !exitMap.isEmpty()){
                    ArrayList<Integer> stpXset = new ArrayList<Integer>();
                    ArrayList<Integer> stpYset = new ArrayList<Integer>();
                    ArrayList<String> stpEntset = new ArrayList<String>();
                    ArrayList<String> stpExtset = new ArrayList<String>();
                    for(Point pt : entMap.keySet()){
                        if(entMap.get(pt) != null){
                            System.out.println("Entry::: x="+pt.x+", y="+pt.y);
                            stpXset.add(pt.x);
                            stpYset.add(pt.y);
                            stpEntset.add(entMap.get(pt));
                            if(exitMap.containsKey(pt) && exitMap.get(pt) != null)
                                stpExtset.add(exitMap.get(pt));
                            else
                                stpExtset.add("");
                        }
                    }
                    for(Point pt : exitMap.keySet()){
                        if(exitMap.get(pt) != null){
                            System.out.println("Exit::: x="+pt.x+", y="+pt.y);
                            boolean alrSet = false;
                            if(stpXset.contains(pt.x)){
                                int index = stpXset.indexOf(pt.x);
                                if(stpYset.get(index) == pt.y){
                                    alrSet= true;
                                }
                            }
                            if(!alrSet){
                                System.out.println("HOLY SHIT::: x="+pt.x+", y="+pt.y);
                                stpXset.add(pt.x);
                                stpYset.add(pt.y);
                                stpExtset.add(exitMap.get(pt));
                                stpEntset.add("");
                            }
                        }
                    }
                    if(!stpXset.isEmpty() && !stpYset.isEmpty() && !stpEntset.isEmpty() && !stpExtset.isEmpty()){
                        preparedStatement.setString(20, stpXset.toString());
                        preparedStatement.setString(21, stpYset.toString());
                        preparedStatement.setString(24, stpEntset.toString());
                        preparedStatement.setString(25, stpExtset.toString());
                    }else{
                        preparedStatement.setString(20,"");
                        preparedStatement.setString(21,"");
                        preparedStatement.setString(24,"");
                        preparedStatement.setString(25,"");
                        message.setText("Warning::: No stop points detected!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                }else{
                    preparedStatement.setString(20,"");
                    preparedStatement.setString(21,"");
                    preparedStatement.setString(24,"");
                    preparedStatement.setString(25,"");
                    message.setText("Warning::: No stop points detected!!!");
                    message.setStyle("-fx-background-color:#e0fb13;");
                }
                
                if(dateAt.getValue() != null){
                    String dateInYYYMMDD = String.valueOf(dateAt.getValue().getYear())+"-"+String.valueOf(dateAt.getValue().getMonthValue())+"-"+String.valueOf(dateAt.getValue().getDayOfMonth());
                    preparedStatement.setString(26, dateInYYYMMDD);
                }else{
                    message.setText("Error::: Date properly not set!!");
                    message.setStyle("-fx-background-color:#d41616;");
                    return;
                }
                
                saveTime = System.currentTimeMillis();
                preparedStatement.setInt(27, (int)(saveTime-goTime));
                String sqlDel = "DELETE FROM `maindata` WHERE projectID = '"+pd.getText()+"' AND mapID = '"+md.getText()+"' AND resID = '"+rd.getText()+"'";
                try {
                    Statement stmntDel = connection.createStatement();
                    stmntDel.executeUpdate(sqlDel);
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                int ret = preparedStatement.executeUpdate();
                
                if(ret>0){
                    saveTime = goTime;
                    message.setText("SuccessFully Saved to the Database!!!");
                    message.setStyle("-fx-background-color:#43e41d;");
                }else{
                    message.setText("Error conencting database::: Please Check!!");
                    message.setStyle("-fx-background-color:#d41616;");
                }

            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        reset.setOnAction(q -> {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File("dist/Picture Path.jar");

            /* is it a jar file? */
            if(!currentJar.getName().endsWith(".jar"))
              return;

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            try {
                builder.start();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        });
        
        clear.setOnAction(q -> {
            onClearRDEnable = true;
            allYs[0] = null;
            allXs[0] = null;
            counter[0] = 0;
            delAllCtxMn();
            startedFlag = false;
            startPoint = null;
            endPoint = null;
            entTime.setText("");
            dateAt.setValue(null);
            exitTime.setText("");
            age.setText("");
            idnMark.setText("");
            botCol.setText("");
            topCol.setText("");
            spcMark.setText("");
            gender.getSelectionModel().select(null);
            drsTyp.getSelectionModel().select(null);
            spects.selectToggle(null);
            beards.selectToggle(null);
            imgPane.setDisable(true);
            rd.setDisable(false);

            Canvas cnvLocal = new Canvas();
            cnvLocal.setHeight(pubImg.getHeight());
            cnvLocal.setWidth(pubImg.getWidth());
            GraphicsContext gc = cnvLocal.getGraphicsContext2D();
            gc.setLineWidth(1);
            gc.drawImage(pubImg, 0, 0, pubImg.getWidth(),pubImg.getHeight());
            
            cnvLocal.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("GO:::::X::"+event.getX()+",Y::"+event.getY());
                    if (event.getClickCount() >1) {//reset if double clicks
                        gc.clearRect(0, 0, pubImg.getWidth(),pubImg.getHeight());
                        gc.drawImage(pubImg, 0, 0, pubImg.getWidth(),pubImg.getHeight());

                        allYs[0] = null;
                        allXs[0] = null;
                        counter[0] = 0;
                        delAllCtxMn();
                        imgPane.setDisable(true);
                    }else if(event.isSecondaryButtonDown()){
                        boolean found = false;
                        int foundKey = -1;
                        for(int key : stopsMap.keySet()){
                            if(stopsMap.get(key) != null && stopsMap.get(key).contains(new Point((int)event.getX(), (int)event.getY()))){
                                found = true;
                                foundKey = key;
                            }
                        }
                        if(found){
                            stopMaptoCntMenu.get(foundKey).show(cnvLocal,event.getScreenX(), event.getScreenY());
                        }else{
                            gc.setFill(Color.LIGHTGRAY);
                            gc.setStroke(Color.DARKBLUE);
                            gc.setLineWidth(4);

                            gc.fill();
                            gc.strokeRect(
                                event.getX(), //x of the upper left corner
                                event.getY(), //y of the upper left corner
                                2,  //width of the rectangle
                                2  //height of the rectangle
                            );
                            contMenu cM = new contMenu();
                            cM.set(cnvLocal,event.getX(),event.getY()).show(cnvLocal,event.getScreenX(), event.getScreenY());
                        }
                    }else if (event.getClickCount() == 1){
                        for(int key : stopMaptoCntMenu.keySet()){
                            stopMaptoCntMenu.get(key).hide();
                        }
                    }
                }
            });

            cnvLocal.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    if(allXs[0] == null || allYs[0] == null){
                        allXs[0] = new ArrayList<Double>();
                        allXs[0].add(event.getX());
                        allYs[0] = new ArrayList<Double>();
                        allYs[0].add(event.getY());
                        
                        gc.setStroke(Color.RED);
                        gc.setFill(Color.RED);
                        gc.setLineWidth(3);
                        gc.strokeLine(allXs[0].get(counter[0]), allYs[0].get(counter[0]), event.getX(), event.getY());
                    }else{
                        allXs[0].add(event.getX());
                        allYs[0].add(event.getY());
                        
                        gc.setStroke(Color.RED);
                        gc.setFill(Color.RED);
                        gc.setLineWidth(3);
                        gc.strokeLine(allXs[0].get(counter[0]-1), allYs[0].get(counter[0]-1), event.getX(), event.getY());
                    }
                    counter[0]++;
                }
            });
            cnv.getChildren().clear();
            cnv.getChildren().add(cnvLocal);
        });

        exitTime.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                exitTime.setText(oldValue);
            }
        });
        entTime.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                entTime.setText(oldValue);
            }
        });
        age.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    age.setText(newValue.replaceAll("[^\\d]", ""));
                }
        });
        
        rd.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    rd.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(!onClearRDEnable){
                    if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                        selImg.setDisable(true);
                        selVid.setDisable(true);
                        go.setDisable(true);
                        save.setDisable(true);
                    }else{
                        selImg.setDisable(false);
                        selVid.setDisable(false);
                        if((!imgText.getText().equals("No Image Selected")) && (!vdText.getText().equals("No Video Selected"))){
                            go.setDisable(false);
                            save.setDisable(false);
                        }
                    }
                }
            }
        );
        md.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    md.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                    selImg.setDisable(true);
                    selVid.setDisable(true);
                    go.setDisable(true);
                    save.setDisable(true);
                }else{
                    selImg.setDisable(false);
                    selVid.setDisable(false);
                    if((!imgText.getText().equals("No Image Selected")) && (!vdText.getText().equals("No Video Selected"))){
                        go.setDisable(false);
                        save.setDisable(false);
                    }
                }
            }
        );
        pd.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    pd.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                    selImg.setDisable(true);
                    selVid.setDisable(true);
                    go.setDisable(true);
                    save.setDisable(true);
                }else{
                    selImg.setDisable(false);
                    selVid.setDisable(false);
                    if((!imgText.getText().equals("No Image Selected")) && (!vdText.getText().equals("No Video Selected"))){
                        go.setDisable(false);
                        save.setDisable(false);
                    }
                }
            }
        );
        go.setOnAction(q -> {
//            int highestPID = -1,highestMID = -1, highestRID = -1;
//            try {
//                Statement stmnt = connection.createStatement();
//                ResultSet rs = stmnt.executeQuery("select * from maindata");
//                Boolean temp = rs.next();
//                while (temp) {
//                    if(rs.getInt("projectID") > highestPID) highestPID= rs.getInt("projectID");
//                    if(rs.getInt("mapID") > highestMID) highestMID= rs.getInt("mapID");
//                    if(rs.getInt("resID") > highestRID) highestRID= rs.getInt("resID");
//                    temp = rs.next();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            if(rd.getText() == null || rd.getText().equals("")){
//                rd.setText(String.valueOf(highestRID));
//            }
//            if(pd.getText() == null || rd.getText().equals("")){
//                pd.setText(String.valueOf(highestPID));
//            }
//            if(md.getText() == null || rd.getText().equals("")){
//                md.setText(String.valueOf(highestMID));
//            }

            if(pubVidFile != null){
                try {
                    Runtime.getRuntime().exec("cmd /c start \"%programfiles%\\VideoLAN\\VLC\\vlc.exe\" \""+pubVidFile.getAbsoluteFile()+"\"");
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                message.setText("Error::: Video Not Found!!");
                message.setStyle("-fx-background-color:#d41616;");
            }

            if(pubImgFile != null && pubImg != null){
                goTime = System.currentTimeMillis();
                rd.setDisable(true);md.setDisable(true);pd.setDisable(true);clear.setDisable(false);
                selImg.setDisable(true);selVid.setDisable(true);
                go.setDisable(true);
                Canvas cnvLocal = new Canvas();
                cnvLocal.setHeight(pubImg.getHeight());
                cnvLocal.setWidth(pubImg.getWidth());
                GraphicsContext gc = cnvLocal.getGraphicsContext2D();

                double canvasWidth = gc.getCanvas().getWidth();
                double canvasHeight = gc.getCanvas().getHeight();

                gc.setLineWidth(1);
                gc.drawImage(pubImg, 0, 0, pubImg.getWidth(),pubImg.getHeight());

                cnvLocal.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("GO:::::X::"+event.getX()+",Y::"+event.getY());
                        if (event.getClickCount() >1) {//reset if double clicks
                            gc.clearRect(0, 0, pubImg.getWidth(),pubImg.getHeight());
                            gc.drawImage(pubImg, 0, 0, pubImg.getWidth(),pubImg.getHeight());

                            allYs[0] = null;
                            allXs[0] = null;
                            counter[0] = 0;
                            delAllCtxMn();
                        }else if(event.isSecondaryButtonDown()){
                            boolean found = false;
                            int foundKey = -1;
                            for(int key : stopsMap.keySet()){
                                if(stopsMap.get(key) != null && stopsMap.get(key).contains(new Point((int)event.getX(), (int)event.getY()))){
                                    found = true;
                                    foundKey = key;
                                }
                            }
                            if(found){
                                stopMaptoCntMenu.get(foundKey).show(cnvLocal,event.getScreenX(), event.getScreenY());
                            }else{
                                gc.setFill(Color.LIGHTGRAY);
                                gc.setStroke(Color.DARKBLUE);
                                gc.setLineWidth(4);

                                gc.fill();
                                gc.strokeRect(
                                    event.getX(), //x of the upper left corner
                                    event.getY(), //y of the upper left corner
                                    2,  //width of the rectangle
                                    2  //height of the rectangle
                                );
                                contMenu cM = new contMenu();
                                cM.set(cnvLocal,event.getX(),event.getY()).show(cnvLocal,event.getScreenX(), event.getScreenY());
                            }
                        }else if (event.getClickCount() == 1){
                            for(int key : stopMaptoCntMenu.keySet()){
                                stopMaptoCntMenu.get(key).hide();
                            }
                        }
                    }
                });

                cnvLocal.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if(allXs[0] == null || allYs[0] == null){
                            allXs[0] = new ArrayList<Double>();
                            allXs[0].add(event.getX());
                            allYs[0] = new ArrayList<Double>();
                            allYs[0].add(event.getY());

                            gc.setStroke(Color.RED);
                            gc.setFill(Color.RED);
                            gc.setLineWidth(3);
                            gc.strokeLine(allXs[0].get(counter[0]), allYs[0].get(counter[0]), event.getX(), event.getY());
                        }else{
                            allXs[0].add(event.getX());
                            allYs[0].add(event.getY());

                            gc.setStroke(Color.RED);
                            gc.setFill(Color.RED);
                            gc.setLineWidth(3);
                            gc.strokeLine(allXs[0].get(counter[0]-1), allYs[0].get(counter[0]-1), event.getX(), event.getY());
                        }
                        counter[0]++;
                    }
                });
                cnv.getChildren().clear();
                cnv.getChildren().add(cnvLocal);
            }else{
                message.setText("Error::: Image Not Found!!");
                message.setStyle("-fx-background-color:#d41616;");
            }
        });
        
        selImg.setOnAction((e) -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
                File file = fileChooser.showOpenDialog(null);
                if(file != null){
                    try {
                        pubImgFile = file;
                        pubImg = new Image(new FileInputStream(file));
                        imgText.setText(file.getName());
                        if(vdText.getText().equals("No Video Selected") || md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                            go.setDisable(true);
                            save.setDisable(true);
                        }else{
                            go.setDisable(false);
                            save.setDisable(false);
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        });
        
        selVid.setOnAction((e) -> {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilterMp4 = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.MP4");
                FileChooser.ExtensionFilter extFilterAvi = new FileChooser.ExtensionFilter("AVI files (*.avi)", "*.AVI");    
                fileChooser.getExtensionFilters().addAll(extFilterMp4, extFilterAvi);
                File file = fileChooser.showOpenDialog(null);
                if(file != null){
                    pubVidFile = file;
                    vdText.setText(file.getName());
                    if(imgText.getText().equals("No Image Selected") || md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                        go.setDisable(true);
                        save.setDisable(true);
                    }else{
                        go.setDisable(false);
                        save.setDisable(false);
                    }
                }
        });
    }
    
}
