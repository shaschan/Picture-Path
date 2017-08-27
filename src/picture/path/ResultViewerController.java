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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author Dell
 */
public class ResultViewerController{

    @FXML
    private Pane cnv;
    @FXML
    private Label rdText;
    @FXML
    private TextField rd;
    @FXML
    private Label mdText;
    @FXML
    private TextField md;
    @FXML
    private Label pdText;
    @FXML
    private TextField pd;
    @FXML
    private Button clear;
    @FXML
    private Button expExl;
    @FXML
    private Pane imgPane;
    @FXML
    private ChoiceBox gender;
    @FXML
    private Label genderText;
    @FXML
    private TextField ageSt;
    @FXML
    private TextField idnMark;
    @FXML
    private ChoiceBox drsTyp;
    @FXML
    private Label drsTypText;
    @FXML
    private TextField topCol;
    @FXML
    private TextField botCol;
    @FXML
    private RadioButton beardYes;
    @FXML
    private RadioButton beardNo;
    @FXML
    private Label beard;
    @FXML
    private Label Spectacles;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private RadioButton SpectaclesNo;
    @FXML
    private RadioButton SpectaclesYes;
    @FXML
    private TextField spcMark;
    @FXML
    private TextField entTimeSt;
    @FXML
    private TextField ageEn;
    @FXML
    private TextField entTimeEn;
    @FXML
    private TextField extTimeEn;
    @FXML
    private TextField extTimeSt;
    @FXML
    private Button go;
    @FXML
    private Button submit;
    @FXML
    private Label message;
    @FXML
    private Label selCnt;
    @FXML
    private Label aggCnt;
    @FXML
    private Button reset;
    @FXML
    private ChoiceBox imgName;
    @FXML
    private Label imgNameText;

    private HashMap<ContextMenu, ArrayList<Point>> allCircles = new HashMap<ContextMenu, ArrayList<Point>>();
    private HashMap<Point, ContextMenu> pointstoCntMenu = new HashMap<Point, ContextMenu>();

    private ArrayList<Point>[] stopPoints =(ArrayList<Point>[])new ArrayList[1];
    private ArrayList<Point>[] startPoints =(ArrayList<Point>[])new ArrayList[1];
    private ArrayList<Point>[] endPoints =(ArrayList<Point>[])new ArrayList[1];
    private ArrayList<Long>[] stopPointsDurationInMicSecs =(ArrayList<Long>[])new ArrayList[1];

    private ToggleGroup beards = new ToggleGroup();
    private ToggleGroup spects = new ToggleGroup();
    private String currImg = null;
    private File currFile = null;

    private Connection connection;
    
    
    public class idBean {
        private ArrayList <String> column = new ArrayList();

        public String getColumn(int i){
         return this.column.get(i);
        }

        public void setColumn(String params, int i){
         this.column.add(i, params);
        }   

        void setColumn1(String string) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
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
    public void generateExcelHere(ArrayList results) {
        HSSFWorkbook l_workBook_out = new HSSFWorkbook();
        HSSFSheet sheet = l_workBook_out.createSheet("Records");

        Iterator Itr = results.iterator();
        HSSFRow row;
        int rowCounter = 0;
        sheet.setDefaultRowHeightInPoints(5000);

        sheet.setColumnWidth((short)1, (short)4000);
        sheet.setColumnWidth((short)2, (short)4000);
        sheet.setColumnWidth((short)3, (short)4000);
        sheet.setColumnWidth((short)4, (short)4000);
        sheet.setColumnWidth((short)5, (short)4000);
        sheet.setColumnWidth((short)6, (short)4000);
        sheet.setColumnWidth((short)7, (short)4000);
        sheet.setColumnWidth((short)8, (short)4000);
        sheet.setColumnWidth((short)9, (short)4000);
        sheet.setColumnWidth((short)10, (short)4000);
        sheet.setColumnWidth((short)11, (short)4000);
        sheet.setColumnWidth((short)12, (short)4000);
        sheet.setColumnWidth((short)13, (short)4000);
        sheet.setColumnWidth((short)14, (short)4000);
        sheet.setColumnWidth((short)15, (short)4000);
        sheet.setColumnWidth((short)16, (short)4000);
        sheet.setColumnWidth((short)17, (short)4000);
        sheet.setColumnWidth((short)18, (short)4000);
        sheet.setColumnWidth((short)19, (short)4000);
        sheet.setColumnWidth((short)20, (short)4000);
        sheet.setColumnWidth((short)21, (short)4000);
        sheet.setColumnWidth((short)22, (short)4000);
        sheet.setColumnWidth((short)23, (short)4000);
        sheet.setColumnWidth((short)24, (short)4000);

        HSSFCellStyle cellStyle = l_workBook_out.createCellStyle();
        cellStyle.setWrapText(true);

        //Create Font
        HSSFFont l_font = l_workBook_out.createFont();
        l_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        row = sheet.createRow(rowCounter);
        row.setHeightInPoints((float)38.25);
        int celloutCounter = 0;
        HSSFCell cellout;
//1
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Image File Name");
//2
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Video File Name");
//3
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Project ID");
//4
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Map ID");  
//5
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Respondent ID");
//6
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Saved Date");
//7        
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Entry Time (24 Hrs Format)");
//8
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Exit Time (24 Hrs Format)");  
//9
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Gender");
//10
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Age (in Years)");
//11        
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Identification Markers");
//12
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Dress Type");  
//13
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Top Color");
//14
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Bottom Color");
//15       
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Special Marker");
//16
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Beard ?");  
//17
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Spectacles ?");
//18
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("X Cord Set of Stop Points");
//19        
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Y Cord Set of Stop Points");
//20
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Start Point (x,y)");  
//21
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("End Point (x,y)");
//22
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Stop Point Duration Set (in milisecs)");
//23        
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Created Date");
//24        
        celloutCounter++;
        cellout = row.createCell((short) celloutCounter);
        cellout.setCellValue("Efficiency (in milisecs)");

        rowCounter++;
        
        while(Itr.hasNext()) {

         idBean idB = (idBean)Itr.next();

         if(results == null) {
          System.out.println("No results for the given query");
          continue;
         }
         row = sheet.createRow(rowCounter);
         row.setHeightInPoints((float)38.25);
         int cellCounter = 0;
         HSSFCell cell;

         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(0));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(1));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(2));   

         cellCounter++;
         cell = row.createCell((short) cellCounter);   
         cell.setCellValue(idB.getColumn(3));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(4));
         
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(5));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(6));   

         cellCounter++;
         cell = row.createCell((short) cellCounter);   
         cell.setCellValue(idB.getColumn(7));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(8));
         
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(9));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(10));   

         cellCounter++;
         cell = row.createCell((short) cellCounter);   
         cell.setCellValue(idB.getColumn(11));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(12));
         
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(13));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(14));   

         cellCounter++;
         cell = row.createCell((short) cellCounter);   
         cell.setCellValue(idB.getColumn(15));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(16));
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(17));   

         cellCounter++;
         cell = row.createCell((short) cellCounter);   
         cell.setCellValue(idB.getColumn(18));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(19));
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(20));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(21));
         
         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(22));

         cellCounter++;
         cell = row.createCell((short) cellCounter);
         cell.setCellValue(idB.getColumn(23));
         
         rowCounter++;
        }
        writeExcel(l_workBook_out); 
   } 
  
    private void writeExcel(HSSFWorkbook l_workBook_out) {
        String l_str_file_out = "D:/Exported_Picture_Path_Data_"+System.currentTimeMillis()+".xls"; //Give the location suitable to your requirement
        FileOutputStream fileOut;
        try {
         fileOut = new FileOutputStream(l_str_file_out);
         l_workBook_out.write(fileOut);
         fileOut.close();
         message.setText("Excel exported: "+l_str_file_out);
        } catch (FileNotFoundException e) {
         e.printStackTrace();
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
    public ArrayList<Point> nearByPoints(int x, int y){
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

        return ptsNearBy;
    }
    
    public boolean checkPointInside(double x,double y, ArrayList<Point> circle){
        int intersections = 0;
        double prevX = circle.get(circle.size()-1).getX();
        double prevY = circle.get(circle.size()-1).getY();
        for (int i=0; i<circle.size(); i++) {
            if ((prevY <= y && y < circle.get(i).getY()) || (prevY >= y && y > circle.get(i).getY())) {
                double dy = circle.get(i).getY() - prevY;
                double dx = circle.get(i).getX() - prevX;
                double xper = (y - prevY) / dy * dx + prevX;
                
                if (xper > x) {
                    intersections++;
                }
            }
            prevX = circle.get(i).getX();
            prevY = circle.get(i).getY();
        }
        return intersections % 2 == 1;
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
 
        beardYes.setToggleGroup(beards);
        beardNo.setToggleGroup(beards);
        
        SpectaclesYes.setToggleGroup(spects);
        SpectaclesNo.setToggleGroup(spects);

        ArrayList <String> imgNamesAll = new ArrayList();
        try {
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata");
            Boolean temp = rs.next();
            while (temp) {
                imgNamesAll.add("Project="+rs.getString("projectID")+", Map="+rs.getString("mapID")+", Respondent="+rs.getString("resID")+", "+rs.getString("imgFileName"));
                temp = rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        imgName.setItems(FXCollections.observableArrayList(imgNamesAll));

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
                Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);
        });
        
        clear.setOnAction(q -> {
            allCircles.clear();
            pointstoCntMenu.clear();
            if(stopPoints[0] != null)stopPoints[0].clear();
            stopPoints[0]= null;
            if(startPoints[0] != null)startPoints[0].clear();
            startPoints[0]= null;
            if(endPoints[0] != null)endPoints[0].clear();
            endPoints[0]= null;
            if(stopPointsDurationInMicSecs[0] != null)stopPointsDurationInMicSecs[0].clear();
            stopPointsDurationInMicSecs[0]= null;
            
            entTimeSt.setText("");
            entTimeEn.setText("");
            fromDate.setValue(null);
            toDate.setValue(null);
            extTimeSt.setText("");
            extTimeEn.setText("");
            ageSt.setText("");
            ageEn.setText("");
            idnMark.setText("");
            botCol.setText("");
            topCol.setText("");
            spcMark.setText("");
            gender.getSelectionModel().select(null);
            drsTyp.getSelectionModel().select(null);
            genderText.setVisible(true);
            drsTypText.setVisible(true);
            spects.selectToggle(null);
            beards.selectToggle(null);
            selCnt.setText("NA");
            aggCnt.setText("");
            
            if(currFile != null && currImg != null)
                drawStart(currFile, currImg);
        });
        
        submit.setOnAction(q ->{
            allCircles.clear();
            pointstoCntMenu.clear();
            if(stopPoints[0] != null)stopPoints[0].clear();
            stopPoints[0]= null;
            if(startPoints[0] != null)startPoints[0].clear();
            startPoints[0]= null;
            if(endPoints[0] != null)endPoints[0].clear();
            endPoints[0]= null;
            if(stopPointsDurationInMicSecs[0] != null)stopPointsDurationInMicSecs[0].clear();
            stopPointsDurationInMicSecs[0]= null;
            
            if(currFile != null && currImg != null)
                drawStart(currFile, currImg);
        });
        go.setDisable(true);
        imgPane.setDisable(true);
       // expExl.setDisable(true);
        
        imgName.setOnAction((q) -> { 
            if(imgName.getSelectionModel().getSelectedItem() != null){
                go.setDisable(false);
            }else{
                go.setDisable(true);
            }
        });

        
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
        
        rd.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    rd.setText(newValue.replaceAll("[^\\d]", ""));
                }
                imgName.getSelectionModel().clearSelection();
                ArrayList <String> imgNames = new ArrayList();
                try {
                    String pdSQL = "";
                    if(pd.getText() != null && !pd.getText().equals("")){
                        pdSQL = " AND projectID = "+pd.getText()+" ";
                    }
                    String mdSQL = "";
                    if(md.getText() != null && !md.getText().equals("")){
                        mdSQL = " AND mapID = "+md.getText()+" ";
                    }
                    String rdSQL = "";
                    if(rd.getText() != null && !rd.getText().equals("")){
                        rdSQL = " AND resID = "+rd.getText()+" ";
                    }
                    Statement stmnt = connection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata WHERE 1=1 "+pdSQL+mdSQL+rdSQL);
                    Boolean temp = rs.next();
                    while (temp) {
                        imgNames.add("Project="+rs.getString("projectID")+", Map="+rs.getString("mapID")+", Respondent="+rs.getString("resID")+", "+rs.getString("imgFileName"));
                        temp = rs.next();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                imgName.setItems(FXCollections.observableArrayList(imgNames));
                if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                    go.setDisable(true);
                }else{
                    go.setDisable(false);
                   
                }
            }
        );
        md.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    md.setText(newValue.replaceAll("[^\\d]", ""));
                }
                imgName.getSelectionModel().clearSelection();
                ArrayList <String> imgNames = new ArrayList();
                try {
                    String pdSQL = "";
                    if(pd.getText() != null && !pd.getText().equals("")){
                        pdSQL = " AND projectID = "+pd.getText()+" ";
                    }
                    String mdSQL = "";
                    if(md.getText() != null && !md.getText().equals("")){
                        mdSQL = " AND mapID = "+md.getText()+" ";
                    }
                    String rdSQL = "";
                    if(rd.getText() != null && !rd.getText().equals("")){
                        rdSQL = " AND resID = "+rd.getText()+" ";
                    }
                    Statement stmnt = connection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata WHERE 1=1 "+pdSQL+mdSQL+rdSQL);
                    Boolean temp = rs.next();
                    while (temp) {
                        imgNames.add("Project="+rs.getString("projectID")+", Map="+rs.getString("mapID")+", Respondent="+rs.getString("resID")+", "+rs.getString("imgFileName"));
                        temp = rs.next();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                imgName.setItems(FXCollections.observableArrayList(imgNames));
                if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                    go.setDisable(true);
                }else{
                    go.setDisable(false);
                    
                }
            }
        );
        entTimeEn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                entTimeEn.setText(oldValue);
            }
        });
        entTimeSt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                entTimeSt.setText(oldValue);
            }
        });
        extTimeEn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                extTimeEn.setText(oldValue);
            }
        });
        extTimeSt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!matcheTime(newValue)) {
                extTimeSt.setText(oldValue);
            }
        });
        ageSt.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    ageSt.setText(newValue.replaceAll("[^\\d]", ""));
                }
        });
        ageEn.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    ageSt.setText(newValue.replaceAll("[^\\d]", ""));
                }
        });
        pd.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    pd.setText(newValue.replaceAll("[^\\d]", ""));
                }
                imgName.getSelectionModel().clearSelection();
                ArrayList <String> imgNames = new ArrayList();
                try {
                    String pdSQL = "";
                    if(pd.getText() != null && !pd.getText().equals("")){
                        pdSQL = " AND projectID = "+pd.getText()+" ";
                    }
                    String mdSQL = "";
                    if(md.getText() != null && !md.getText().equals("")){
                        mdSQL = " AND mapID = "+md.getText()+" ";
                    }
                    String rdSQL = "";
                    if(rd.getText() != null && !rd.getText().equals("")){
                        rdSQL = " AND resID = "+rd.getText()+" ";
                    }
                    Statement stmnt = connection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata WHERE 1=1 "+pdSQL+mdSQL+rdSQL);
                    Boolean temp = rs.next();
                    while (temp) {
                        imgNames.add("Project="+rs.getString("projectID")+", Map="+rs.getString("mapID")+", Respondent="+rs.getString("resID")+", "+rs.getString("imgFileName"));
                        temp = rs.next();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                imgName.setItems(FXCollections.observableArrayList(imgNames));
                if(md.getText() == null || md.getText().equals("") || rd.getText() == null || rd.getText().equals("") || pd.getText() == null || pd.getText().equals("")){
                    go.setDisable(true);
                }else{
                    go.setDisable(false);
                }
            }
        );
        expExl.setOnAction(q-> {
            String imgFileSQL = "";
            if(currImg != null && currImg.length() > 0){
                imgFileSQL = " AND imgFileName = '"+currImg+"' ";
            }
            
            String pdSQL = "";
            if(pd.getText() != null && !pd.getText().equals("")){
                pdSQL = " AND projectID = '"+pd.getText()+"' ";
            }else{
                message.setText("Warning::: Project ID not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }
            
            String mdSQL = "";
            if(md.getText() != null && !md.getText().equals("")){
                mdSQL = " AND mapID = '"+md.getText()+"' ";
            }else{
                message.setText("Warning::: Map ID not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }
            
            String rdSQL = "";
            if(rd.getText() != null && !rd.getText().equals("")){
                rdSQL = " AND resID = '"+rd.getText()+"' ";
            }else{
                message.setText("Warning::: Respondent ID not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }
            
            String ageStSQL = "";
            if(ageSt.getText() != null && !ageSt.getText().equals("")){
                ageStSQL = " AND age >= "+ageSt.getText()+" ";
            }else{
                message.setText("Warning::: Age Start not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String ageEnSQL = "";
            if(ageEn.getText() != null && !ageEn.getText().trim().equals("")){
                ageEnSQL = " AND age <= "+ageEn.getText()+" ";
            }else{
                message.setText("Warning::: Age End not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String fromDateSQL = "";
            if(fromDate.getValue() != null){
                String dateInYYYMMDD = String.valueOf(fromDate.getValue().getYear())+"-"+String.valueOf(fromDate.getValue().getMonthValue())+"-"+String.valueOf(fromDate.getValue().getDayOfMonth());
                fromDateSQL = " AND buildingDate >= '"+dateInYYYMMDD+"' ";
            }else{
                message.setText("Warning::: From-Date not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String toDateSQL = "";
            if(toDate.getValue() != null){
                String dateInYYYMMDD = String.valueOf(toDate.getValue().getYear())+"-"+String.valueOf(toDate.getValue().getMonthValue())+"-"+String.valueOf(toDate.getValue().getDayOfMonth());
                toDateSQL = " AND buildingDate <= '"+dateInYYYMMDD+"' ";
            }else{
                message.setText("Warning::: To-Date not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }
            
            String entTimeStSQL = "";
            if(entTimeSt.getText() != null && !entTimeSt.getText().trim().equals("") && (entTimeSt.getText().length() == 8)){
                entTimeStSQL = " AND entryTime >= '"+entTimeSt.getText().trim()+"' ";
            }else{
                message.setText("Warning::: Entry Time Start not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String entTimeEnSQL = "";
            if(entTimeEn.getText() != null && !entTimeEn.getText().trim().equals("") && (entTimeEn.getText().length() == 8)){
                entTimeEnSQL = " AND entryTime <= '"+entTimeEn.getText().trim()+"' ";
            }else{
                message.setText("Warning::: Entry Time End not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String extTimeEnSQL = "";
            if(extTimeEn.getText() != null && !extTimeEn.getText().trim().equals("") && (extTimeEn.getText().length() == 8)){
                extTimeEnSQL = " AND exitTime <= '"+extTimeEn.getText().trim()+"' ";
            }else{
                message.setText("Warning::: Exit Time End not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String extTimeStSQL = "";
            if(extTimeSt.getText() != null && !extTimeSt.getText().equals("") && (extTimeSt.getText().length() == 8)){
                extTimeStSQL = " AND exitTime >= '"+extTimeSt.getText().trim()+"' ";
            }else{
                message.setText("Warning::: Exit Time Start not properly set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String idnMarkSQL = "";
            if(idnMark.getText() != null && !idnMark.getText().trim().equals("")){
                idnMarkSQL = " AND identMarkers in (";
                for(int i=0; i< idnMark.getText().split(",").length; i++){
                    if(i == 0)
                        idnMarkSQL = idnMarkSQL + " '"+ idnMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                    else
                        idnMarkSQL = idnMarkSQL + ", '"+ idnMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                }
                idnMarkSQL = idnMarkSQL + ") ";
            }else{
                message.setText("Warning::: Identification Marker not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String topColSQL = "";
            if(topCol.getText() != null && !topCol.getText().trim().equals("")){
                topColSQL = " AND topColor in (";
                for(int i=0; i< topCol.getText().split(",").length; i++){
                    if(i == 0)
                        topColSQL = topColSQL + " '"+ topCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                    else
                        topColSQL = topColSQL + ", '"+ topCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                }
                topColSQL = topColSQL + ") ";
            }else{
                message.setText("Warning::: Top Color not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String botColSQL = "";
            if(botCol.getText() != null && !botCol.getText().trim().equals("")){
                botColSQL = " AND bottomColor in (";
                for(int i=0; i< botCol.getText().split(",").length; i++){
                    if(i == 0)
                        botColSQL = botColSQL + " '"+ botCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                    else
                        botColSQL = botColSQL + ", '"+ botCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                }
                botColSQL = botColSQL + ") ";
            }else{
                message.setText("Warning::: Bottom Color not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String spcMarkSQL = "";
            if(spcMark.getText() != null && !spcMark.getText().trim().equals("")){
                spcMarkSQL = " AND specsMarker in (";
                for(int i=0; i< spcMark.getText().split(",").length; i++){
                    if(i == 0)
                        spcMarkSQL = spcMarkSQL + " '"+ spcMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                    else
                        spcMarkSQL = spcMarkSQL + ", '"+ spcMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                }
                spcMarkSQL = spcMarkSQL + ") ";
            }else{
                message.setText("Warning::: Special Maker not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String genderSQL = "";
            if(gender.getSelectionModel().getSelectedItem() != null && !gender.getSelectionModel().getSelectedItem().toString().equals("All")){
                genderSQL = " AND gender = '"+gender.getSelectionModel().getSelectedItem().toString()+"' ";
            }else if(!(gender.getSelectionModel().getSelectedItem() != null && gender.getSelectionModel().getSelectedItem().toString().equals("All"))){
                message.setText("Warning::: Gender not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String drsTypSQL = "";
            if(drsTyp.getSelectionModel().getSelectedItem() != null && !drsTyp.getSelectionModel().getSelectedItem().toString().equals("All")){
                drsTypSQL = " AND dressType = '"+drsTyp.getSelectionModel().getSelectedItem().toString()+"' ";
            }else if(!(drsTyp.getSelectionModel().getSelectedItem() != null && drsTyp.getSelectionModel().getSelectedItem().toString().equals("All"))){
                message.setText("Warning::: Dress Type not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String beardSQL = "";
            if(beards.getSelectedToggle() != null){
                if(beards.getSelectedToggle().equals(beardYes))
                    beardSQL =" And beard = 'YES' ";
                else
                    beardSQL =" And beard = 'NO' ";
            }else{
                message.setText("Warning::: Beard Type not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            String spectsSQL = "";
            if(spects.getSelectedToggle() != null){
                if(spects.getSelectedToggle().equals(SpectaclesYes))
                    spectsSQL =" And specs = 'YES' ";
                else
                    spectsSQL =" And specs = 'NO' ";
            }else{
                message.setText("Warning::: Spectacles type not set!!!");
                message.setStyle("-fx-background-color:#e0fb13;");
            }

            try {
                    Statement stmnt = connection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata WHERE 1=1 "+imgFileSQL+pdSQL+mdSQL+rdSQL+ageStSQL+ageEnSQL+entTimeStSQL+entTimeEnSQL+fromDateSQL+toDateSQL+extTimeStSQL+extTimeEnSQL+idnMarkSQL+topColSQL+botColSQL+spcMarkSQL+genderSQL+drsTypSQL+beardSQL+spectsSQL);
                    Boolean temp = rs.next();
                    ArrayList list = new ArrayList();
                    while (temp) {
                        idBean idB = new idBean();
                        idB.setColumn(rs.getString("imgFileName"),0);
                        idB.setColumn(rs.getString("vidFileName"),1);
                        idB.setColumn(rs.getString("projectID"),2);
                        idB.setColumn(rs.getString("mapID"),3);
                        idB.setColumn(rs.getString("resID"),4);
                        idB.setColumn(rs.getString("createdDate"),5);
                        idB.setColumn(rs.getString("entryTime"),6);
                        idB.setColumn(rs.getString("exitTime"),7);
                        idB.setColumn(rs.getString("gender"),8);
                        idB.setColumn(rs.getString("age"),9);
                        idB.setColumn(rs.getString("identMarkers"),10);
                        idB.setColumn(rs.getString("dressType"),11);
                        idB.setColumn(rs.getString("topColor"),12);
                        idB.setColumn(rs.getString("bottomColor"),13);
                        idB.setColumn(rs.getString("specsMarker"),14);
                        idB.setColumn(rs.getString("beard"),15);
                        idB.setColumn(rs.getString("specs"),16);
                        idB.setColumn(rs.getString("stopXCordSet"),17);
                        idB.setColumn(rs.getString("stopYCordSet"),18);
                        idB.setColumn(rs.getString("startPoint"),19);
                        idB.setColumn(rs.getString("endPoint"),20);

                        ArrayList<Long> durationSet = new ArrayList<Long>();
                        String StopEntryTimeSet = rs.getString("stopEntryTimeSet");
                        String StopExitTimeSet = rs.getString("stopExitTimeSet");
                        if(StopEntryTimeSet.length() != 0 && StopExitTimeSet.length() != 0){
                            String[] tempStopEntryTimeSet = StopEntryTimeSet.substring(1, StopEntryTimeSet.length()-1).split(", ");
                            String[] tempStopExitTimeSet = StopExitTimeSet.substring(1, StopExitTimeSet.length()-1).split(", ");

                            if(tempStopEntryTimeSet.length == tempStopExitTimeSet.length){
                                for(int tempi =0; tempi < tempStopExitTimeSet.length; tempi++){
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    Date dateEnt = sdf.parse(tempStopEntryTimeSet[tempi]);
                                    Date dateExit = sdf.parse(tempStopExitTimeSet[tempi]);
                                    durationSet.add(dateExit.getTime() - dateEnt.getTime());
                                }
                            }
                        }

                        idB.setColumn(durationSet.toString(),21);
                        idB.setColumn(rs.getString("buildingDate"),22);
                        idB.setColumn(rs.getString("efficiencyTime"),23);

                        list.add(idB);
                        temp = rs.next();
                    }
                    generateExcelHere(list);
                } catch (SQLException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        go.setOnAction(q -> {
            if(md.getText() != null && !md.getText().equals("") && rd.getText() != null && !rd.getText().equals("") && pd.getText() != null && !pd.getText().equals("")){
                String path = "outputs/"+pd.getText()+"/"+md.getText()+"/image/";
                String file = rd.getText();
                startEverything(path, file);
            }else if(imgName.getSelectionModel().getSelectedItem() != null){
                String name = imgName.getSelectionModel().getSelectedItem().toString();
                String path = "outputs/"+name.split(",")[0].split("=")[1]+"/"+name.split(",")[1].split("=")[1]+"/image/";
                String file = name.split(",")[2].split("=")[1];
                startEverything(path, file);
            }
        });
    }    
    public void startEverything(String path, String file){
        imgName.setDisable(true);
        md.setDisable(true);
        pd.setDisable(true);
        rd.setDisable(true);
        go.setDisable(true);
      //  expExl.setDisable(false);
        imgPane.setDisable(false);

        File fileMain = null;
            File directory = new File(path);
            if (directory.exists()){
                File[] directoryListing = directory.listFiles();
                if (directoryListing != null) {
                    for (File child : directoryListing) {
                        if(child.getName().split("_")[1].equals(file)){
                            fileMain = child;
                        }
                    }
                }
            }
        if(fileMain != null){
            currFile = fileMain;
            String[] imgFileNameArr = fileMain.getName().split("_");
            String  imgFileNameHere = "";
            for(int i=0; i < imgFileNameArr.length; i++){
                if(i>1 && i != (imgFileNameArr.length-1))
                    imgFileNameHere = imgFileNameHere.concat(imgFileNameArr[i]).concat("_");
                else if(i == (imgFileNameArr.length-1))
                    imgFileNameHere = imgFileNameHere.concat(imgFileNameArr[i]);
            }
            drawStart(fileMain, imgFileNameHere);
        }
    }
    
    public void drawStart(File file, String imgFileName){
        if(!imgFileName.equals("") && imgFileName != null){
            try {
                final Image image = new Image(new FileInputStream(file));
                ArrayList<Point>[] circlePts =(ArrayList<Point>[])new ArrayList[1];
                int[] counter = {0};
                currImg = imgFileName;
                Canvas cnvLocal = new Canvas();
                cnvLocal.setHeight(image.getHeight());
                cnvLocal.setWidth(image.getWidth());
                GraphicsContext gc = cnvLocal.getGraphicsContext2D();

                double canvasWidth = gc.getCanvas().getWidth();
                double canvasHeight = gc.getCanvas().getHeight();

                gc.setLineWidth(1);
                gc.drawImage(image, 0, 0, image.getWidth(),image.getHeight());

                try {
                    String imgFileSQL = " AND imgFileName = '"+imgFileName+"' ";
                    String ageStSQL = "";
                    if(ageSt.getText() != null && !ageSt.getText().equals("")){
                        ageStSQL = " AND age >= "+ageSt.getText()+" ";
                    }else{
                        message.setText("Warning::: Age Start not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String ageEnSQL = "";
                    if(ageEn.getText() != null && !ageEn.getText().trim().equals("")){
                        ageEnSQL = " AND age <= "+ageEn.getText()+" ";
                    }else{
                        message.setText("Warning::: Age End not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                    
                    String fromDateSQL = "";
                    if(fromDate.getValue() != null){
                        String dateInYYYMMDD = String.valueOf(fromDate.getValue().getYear())+"-"+String.valueOf(fromDate.getValue().getMonthValue())+"-"+String.valueOf(fromDate.getValue().getDayOfMonth());
                        fromDateSQL = " AND buildingDate >= '"+dateInYYYMMDD+"' ";
                    }else{
                        message.setText("Warning::: From-Date not properly set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String toDateSQL = "";
                    if(toDate.getValue() != null){
                        String dateInYYYMMDD = String.valueOf(toDate.getValue().getYear())+"-"+String.valueOf(toDate.getValue().getMonthValue())+"-"+String.valueOf(toDate.getValue().getDayOfMonth());
                        toDateSQL = " AND buildingDate <= '"+dateInYYYMMDD+"' ";
                    }else{
                        message.setText("Warning::: To-Date not properly set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                    
                    String entTimeStSQL = "";
                    if(entTimeSt.getText() != null && !entTimeSt.getText().trim().equals("") && (entTimeSt.getText().length() == 8)){
                        entTimeStSQL = " AND entryTime >= '"+entTimeSt.getText().trim()+"' ";
                    }else{
                        message.setText("Warning::: Entry Time Start not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String entTimeEnSQL = "";
                    if(entTimeEn.getText() != null && !entTimeEn.getText().trim().equals("") && (entTimeEn.getText().length() == 8)){
                        entTimeEnSQL = " AND entryTime <= '"+entTimeEn.getText().trim()+"' ";
                    }else{
                        message.setText("Warning::: Entry Time End not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                    
                    String extTimeEnSQL = "";
                    if(extTimeEn.getText() != null && !extTimeEn.getText().trim().equals("") && (extTimeEn.getText().length() == 8)){
                        extTimeEnSQL = " AND exitTime <= '"+extTimeEn.getText().trim()+"' ";
                    }else{
                        message.setText("Warning::: Exit Time End not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                    
                    String extTimeStSQL = "";
                    if(extTimeSt.getText() != null && !extTimeSt.getText().equals("") && (extTimeSt.getText().length() == 8)){
                        extTimeStSQL = " AND exitTime >= '"+extTimeSt.getText().trim()+"' ";
                    }else{
                        message.setText("Warning::: Exit Time Start not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String idnMarkSQL = "";
                    if(idnMark.getText() != null && !idnMark.getText().trim().equals("")){
                        idnMarkSQL = " AND identMarkers in (";
                        for(int i=0; i< idnMark.getText().split(",").length; i++){
                            if(i == 0)
                                idnMarkSQL = idnMarkSQL + " '"+ idnMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                            else
                                idnMarkSQL = idnMarkSQL + ", '"+ idnMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                        }
                        idnMarkSQL = idnMarkSQL + ") ";
                    }else{
                        message.setText("Warning::: Identification Marker not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String topColSQL = "";
                    if(topCol.getText() != null && !topCol.getText().trim().equals("")){
                        topColSQL = " AND topColor in (";
                        for(int i=0; i< topCol.getText().split(",").length; i++){
                            if(i == 0)
                                topColSQL = topColSQL + " '"+ topCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                            else
                                topColSQL = topColSQL + ", '"+ topCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                        }
                        topColSQL = topColSQL + ") ";
                    }else{
                        message.setText("Warning::: Top Color not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }
                    
                    String botColSQL = "";
                    if(botCol.getText() != null && !botCol.getText().trim().equals("")){
                        botColSQL = " AND bottomColor in (";
                        for(int i=0; i< botCol.getText().split(",").length; i++){
                            if(i == 0)
                                botColSQL = botColSQL + " '"+ botCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                            else
                                botColSQL = botColSQL + ", '"+ botCol.getText().split(",")[i].trim().toLowerCase()+"' ";
                        }
                        botColSQL = botColSQL + ") ";
                    }else{
                        message.setText("Warning::: Bottom Color not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String spcMarkSQL = "";
                    if(spcMark.getText() != null && !spcMark.getText().trim().equals("")){
                        spcMarkSQL = " AND specsMarker in (";
                        for(int i=0; i< spcMark.getText().split(",").length; i++){
                            if(i == 0)
                                spcMarkSQL = spcMarkSQL + " '"+ spcMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                            else
                                spcMarkSQL = spcMarkSQL + ", '"+ spcMark.getText().split(",")[i].trim().toLowerCase()+"' ";
                        }
                        spcMarkSQL = spcMarkSQL + ") ";
                    }else{
                        message.setText("Warning::: Special Maker not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String genderSQL = "";
                    if(gender.getSelectionModel().getSelectedItem() != null && !gender.getSelectionModel().getSelectedItem().toString().equals("All")){
                        genderSQL = " AND gender = '"+gender.getSelectionModel().getSelectedItem().toString()+"' ";
                    }else if(!(gender.getSelectionModel().getSelectedItem() != null && gender.getSelectionModel().getSelectedItem().toString().equals("All"))){
                        message.setText("Warning::: Gender not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String drsTypSQL = "";
                    if(drsTyp.getSelectionModel().getSelectedItem() != null && !drsTyp.getSelectionModel().getSelectedItem().toString().equals("All")){
                        drsTypSQL = " AND dressType = '"+drsTyp.getSelectionModel().getSelectedItem().toString()+"' ";
                    }else if(!(drsTyp.getSelectionModel().getSelectedItem() != null && drsTyp.getSelectionModel().getSelectedItem().toString().equals("All"))){
                        message.setText("Warning::: Dress Type not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String beardSQL = "";
                    if(beards.getSelectedToggle() != null){
                        if(beards.getSelectedToggle().equals(beardYes))
                            beardSQL =" And beard = 'YES' ";
                        else
                            beardSQL =" And beard = 'NO' ";
                    }else{
                        message.setText("Warning::: Beard Type not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    String spectsSQL = "";
                    if(spects.getSelectedToggle() != null){
                        if(spects.getSelectedToggle().equals(SpectaclesYes))
                            spectsSQL =" And specs = 'YES' ";
                        else
                            spectsSQL =" And specs = 'NO' ";
                    }else{
                        message.setText("Warning::: Spectacles type not set!!!");
                        message.setStyle("-fx-background-color:#e0fb13;");
                    }

                    Statement stmnt = connection.createStatement();
                    ResultSet rs = stmnt.executeQuery("SELECT * FROM maindata WHERE 1=1 "+imgFileSQL+ageStSQL+ageEnSQL+entTimeStSQL+entTimeEnSQL+fromDateSQL+toDateSQL+extTimeStSQL+extTimeEnSQL+idnMarkSQL+topColSQL+botColSQL+spcMarkSQL+genderSQL+drsTypSQL+beardSQL+spectsSQL);
                    System.out.println("SELECT * FROM maindata WHERE 1=1 "+imgFileSQL+ageStSQL+ageEnSQL+entTimeStSQL+entTimeEnSQL+fromDateSQL+toDateSQL+extTimeStSQL+extTimeEnSQL+idnMarkSQL+topColSQL+botColSQL+spcMarkSQL+genderSQL+drsTypSQL+beardSQL+spectsSQL);
                    Boolean temp = rs.next();
                    Random r = new Random();
                    while (temp) {
                        int colIdx1 = r.nextInt(255);
                        int colIdx2 = r.nextInt(255);
                        int colIdx3 = r.nextInt(255);
                        String xSet = rs.getString("xCordSet");
                        String ySet = rs.getString("yCordSet");
                        String[] tempX = xSet.substring(1, xSet.length()-1).split(", ");
                        String[] tempY = ySet.substring(1, ySet.length()-1).split(", ");
                        for(int tempi = 0; tempi < tempX.length; tempi++){
                            if(tempi>0){
                                gc.setStroke(Color.rgb(colIdx1, colIdx2, colIdx3));
                                gc.setFill(Color.rgb(colIdx1, colIdx2, colIdx3));
                                gc.setLineWidth(1.7);
                                gc.strokeLine(Double.parseDouble(tempX[tempi]), Double.parseDouble(tempY[tempi]), Double.parseDouble(tempX[tempi-1]), Double.parseDouble(tempY[tempi-1]));
                            }
                        }
                        String xStopSet = rs.getString("stopXCordSet");
                        String yStopSet = rs.getString("stopYCordSet");
                        if(xStopSet.length() != 0 && yStopSet.length() != 0){
                            String[] tempStopX = xStopSet.substring(1, xStopSet.length()-1).split(", ");
                            String[] tempStopY = yStopSet.substring(1, yStopSet.length()-1).split(", ");
                            for(int tempi =0; tempi < tempStopX.length; tempi++){
                                gc.setFill(Color.LIGHTGRAY);
                                gc.setStroke(Color.DARKBLUE);
                                gc.setLineWidth(4);
                                gc.fill();
                                gc.strokeRect(
                                    Double.parseDouble(tempStopX[tempi]), //x of the upper left corner
                                    Double.parseDouble(tempStopY[tempi]), //y of the upper left corner
                                    2,  //width of the rectangle
                                    2  //height of the rectangle
                                );
                                if(stopPoints[0] == null) stopPoints[0] = new ArrayList<Point>();
                                stopPoints[0].add(new Point(Integer.parseInt(tempStopX[tempi]), Integer.parseInt(tempStopY[tempi])));
                            }
                        }
                        
                        String StopEntryTimeSet = rs.getString("stopEntryTimeSet");
                        String StopExitTimeSet = rs.getString("stopExitTimeSet");
                        if(StopEntryTimeSet.length() != 0 && StopExitTimeSet.length() != 0){
                            String[] tempStopEntryTimeSet = StopEntryTimeSet.substring(1, StopEntryTimeSet.length()-1).split(", ");
                            String[] tempStopExitTimeSet = StopExitTimeSet.substring(1, StopExitTimeSet.length()-1).split(", ");

                            if(tempStopEntryTimeSet.length == tempStopExitTimeSet.length){
                                for(int tempi =0; tempi < tempStopExitTimeSet.length; tempi++){
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    Date dateEnt = sdf.parse(tempStopEntryTimeSet[tempi]);
                                    Date dateExit = sdf.parse(tempStopExitTimeSet[tempi]);
                                    long duration = dateExit.getTime() - dateEnt.getTime();
                                    if(stopPointsDurationInMicSecs[0] == null) stopPointsDurationInMicSecs[0] = new ArrayList<Long>();
                                                                    System.out.println("duration::"+duration);
                                    stopPointsDurationInMicSecs[0].add(duration);
                                }
                            }
                        }
                        if(rs.getString("startPoint").split(",").length == 2){
                            if(startPoints[0] == null) startPoints[0] = new ArrayList<Point>();
                            startPoints[0].add( new Point(Integer.parseInt(rs.getString("startPoint").split(",")[0]),Integer.parseInt(rs.getString("startPoint").split(",")[1])));
                            gc.setFill(Color.CORAL);
                            gc.setStroke(Color.CORAL);
                            gc.setLineWidth(4);
                            gc.fill();
                            gc.strokeRect(
                                Double.parseDouble(rs.getString("startPoint").split(",")[0]), //x of the upper left corner
                                Double.parseDouble(rs.getString("startPoint").split(",")[1]), //y of the upper left corner
                                2,  //width of the rectangle
                                2  //height of the rectangle
                            );
                        }
                        if(rs.getString("endPoint").split(",").length == 2){
                            if(endPoints[0] == null) endPoints[0] = new ArrayList<Point>();
                            endPoints[0].add( new Point(Integer.parseInt(rs.getString("endPoint").split(",")[0]),Integer.parseInt(rs.getString("endPoint").split(",")[1])));
                            gc.setFill(Color.TEAL);
                            gc.setStroke(Color.TEAL);
                            gc.setLineWidth(4);
                            gc.fill();
                            gc.strokeRect(
                                Double.parseDouble(rs.getString("endPoint").split(",")[0]), //x of the upper left corner
                                Double.parseDouble(rs.getString("endPoint").split(",")[1]), //y of the upper left corner
                                2,  //width of the rectangle
                                2  //height of the rectangle
                            );
                        }
                        temp = rs.next();
                    }
                    if(stopPoints[0] != null) aggCnt.setText(String.valueOf(stopPoints[0].size()));
                } catch (SQLException ex) {
                    Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                cnvLocal.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("GO:::::X::"+event.getX()+",Y::"+event.getY());
                        if(event.isSecondaryButtonDown()){ //for a point
                            boolean found = false;
                            Point foundKey = null;
                            for(Point key : pointstoCntMenu.keySet()){
                                if(pointstoCntMenu.get(key) != null && nearByPoints(key.x, key.y).contains(new Point((int)event.getX(), (int)event.getY()))){
                                    found = true;
                                    foundKey = key;
                                }
                            }
                            if(found){
                                pointstoCntMenu.get(foundKey).show(cnvLocal,event.getScreenX(), event.getScreenY());
                            }else{
                                MenuItem type = new MenuItem("Not Available");
                                MenuItem dur = new MenuItem("Not Available");
                                ContextMenu cntmn = new ContextMenu();
                                boolean foundApoint = false;
                                if(stopPoints[0] != null  && stopPointsDurationInMicSecs[0] != null){
                                    for(int i=0; i < stopPoints[0].size(); i++){
                                        if(nearByPoints(stopPoints[0].get(i).x, stopPoints[0].get(i).y).contains(new Point((int)event.getX(), (int)event.getY()))){
                                            type.setText("Stop Point");
                                            System.out.println("Stop Point::"+stopPoints[0].get(i)+", at i ="+i);
                                            System.out.println("stopPointsDurationInMicSecs::"+stopPointsDurationInMicSecs[0].get(i));
                                            dur.setText("Duration - "+String.valueOf(stopPointsDurationInMicSecs[0].get(i))+" milisecs");
                                            foundApoint = true;
                                            break;
                                        }
                                    }
                                }
                                if(startPoints[0] != null){
                                    if(!foundApoint){
                                        for(int i=0; i < startPoints[0].size(); i++){
                                            if(nearByPoints(startPoints[0].get(i).x, startPoints[0].get(i).y).contains(new Point((int)event.getX(), (int)event.getY()))){
                                                type.setText("Start Point");
                                                dur.setText("-");
                                                foundApoint = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(endPoints[0] != null){
                                    if(!foundApoint){
                                        for(int i=0; i < endPoints[0].size(); i++){
                                            if(nearByPoints(endPoints[0].get(i).x, endPoints[0].get(i).y).contains(new Point((int)event.getX(), (int)event.getY()))){
                                                type.setText("End Point");
                                                dur.setText("-");
                                                foundApoint = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                cntmn.getItems().addAll(type,dur);
                                pointstoCntMenu.put(new Point((int)event.getX(), (int)event.getY()), cntmn);
                                cntmn.show(cnvLocal,event.getScreenX(), event.getScreenY());
                            }
                        }else if (event.getClickCount() == 1){ //for inside a circle
                            for(ContextMenu key : allCircles.keySet()){
                                if(checkPointInside(event.getX(), event.getY(), allCircles.get(key))){
                                    key.show(cnvLocal,event.getScreenX(), event.getScreenY());
                                    selCnt.setText(key.getItems().get(0).getText().split(" - ")[1]);
                                }
                            }
                        }else if (event.getClickCount() >= 1){ // hide if double click
                            for(Point key : pointstoCntMenu.keySet()){
                                pointstoCntMenu.get(key).hide();
                            }
                            for(ContextMenu key : allCircles.keySet()){
                                key.hide();
                            }
                        }
                    }
                });

                cnvLocal.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if(circlePts[0] == null){
                            circlePts[0] = new ArrayList<Point>();
                            circlePts[0].add(new Point((int) event.getX(), (int) event.getY()));

                            gc.setStroke(Color.RED);
                            gc.setFill(Color.RED);
                            gc.setLineWidth(3);
                            gc.strokeLine(circlePts[0].get(counter[0]).getX(), circlePts[0].get(counter[0]).getY(), event.getX(), event.getY());
                        }else{
                            circlePts[0].add(new Point((int) event.getX(), (int) event.getY()));

                            gc.setStroke(Color.RED);
                            gc.setFill(Color.RED);
                            gc.setLineWidth(3);
                            gc.strokeLine(circlePts[0].get(counter[0]-1).getX(), circlePts[0].get(counter[0]-1).getY(), event.getX(), event.getY());
                        }
                        counter[0]++;
                    }
                });
                
                cnvLocal.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if(circlePts[0] != null){
                            MenuItem St = new MenuItem("Aggr Stop Point Count - 0");
                            MenuItem En = new MenuItem("Aggr Entry Point Count - 0");
                            MenuItem Ex = new MenuItem("Aggr Exit Point Count - 0");
                            MenuItem dur = new MenuItem("Aggr Duration of Stop Points - 0 milisecs");
                            int cntSt = 0;
                            int cntEn = 0;
                            int cntEx = 0;
                            long dr = 0;
                            ContextMenu cntmn = new ContextMenu();
                            if(stopPoints[0] != null && stopPointsDurationInMicSecs[0] != null){
                                for(Point stopPts: stopPoints[0]){
                                    if(checkPointInside(stopPts.getX(), stopPts.getY(), circlePts[0])){
                                        cntSt++;
                                        dr += stopPointsDurationInMicSecs[0].get(stopPoints[0].indexOf(stopPts));
                                    }
                                }
                            }
                            if(startPoints[0] != null){
                                for(Point startPts: startPoints[0]){
                                    if(checkPointInside(startPts.getX(), startPts.getY(), circlePts[0])){
                                        cntEn++;
                                    }
                                }
                            }
                            if(endPoints[0] != null){
                                for(Point endPts: endPoints[0]){
                                    if(checkPointInside(endPts.getX(), endPts.getY(), circlePts[0])){
                                        cntEx++;
                                    }
                                }
                            }
                            St.setText("Aggr Stop Point Count - "+String.valueOf(cntSt));
                            En.setText("Aggr Entry Point Count - "+String.valueOf(cntEn));
                            Ex.setText("Aggr Exit Point Count - "+String.valueOf(cntEx));
                            dur.setText("Aggr Duration of Stop Points - "+String.valueOf(dr)+" milisecs");
                            cntmn.getItems().addAll(St,En,Ex,dur);
                            allCircles.put(cntmn, circlePts[0]);

                            circlePts[0] = null;
                            counter[0]= 0;
                        }
                    }
                });

                cnv.getChildren().clear();
                cnv.getChildren().add(cnvLocal);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ResultViewerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            message.setText("Error::: File Data not found!!!");
            message.setStyle("-fx-background-color:#d41616;");
        }
    }
}
