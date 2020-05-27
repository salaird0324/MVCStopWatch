package mynewstopwatch;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Spencer Laird
 * use Z-index to get the layout of the buttons to work properly.
 */
public class AnalogStopWatch {
    Thread t;
    int hourCount=0, minCount = 0, secCount = 0, miliCount = 0;
//    String str="", nstr="",mstr="";
    int count1 = 0;
    
    public String time = "0" +hourCount %24 + ":" + minCount % 60 + ":" + secCount %60+ "." + miliCount % 60;
    public double lapCount=0;
    public double counter =0;
    public double lapSaver = 0.0;
    public double lapSeconds=0;
    Label test = new Label("");
    
    
    
    
    
     public Timeline timeline;
     public KeyFrame keyFrame;
     public Timeline digTimeline;
     public KeyFrame digKeyFrame;
    ImageView dialImageView;
    ImageView handImageView;
    
    
    public double secondsElapsed = 0.0;
    public double tickTimeInSeconds = 0.01;  //how to change the resolution 
    public double angleDeltaPerSeconds = 6.0;
    
    public boolean isRunning = false;
    public boolean tickPressed = false;
    
    private StackPane rootContainer;
    private Image dialImage;
    private Image handImage;
    Label Clock = new Label("00:00:00");
    
    
    public AnalogStopWatch(){
        
        
        
        setUpUI();
        setUpTimer();
        
    
    }
    
 
    
    public void setUpUI(){
        rootContainer = new StackPane();
        dialImageView = new ImageView();
        handImageView = new ImageView();
        
        
         dialImage = new Image(getClass().getResourceAsStream("clockface.png"));
         handImage = new Image(getClass().getResourceAsStream("hand.png"));
         
         dialImageView.setImage(dialImage);
         handImageView.setImage(handImage);
         
        VBox controls = new VBox();
        controls.setMaxHeight(Double.MAX_VALUE);
        ToggleButton startStop = new ToggleButton("Stop");
        startStop.setDisable(false);
        Button record = new Button("Record");
        startStop.setMaxWidth(Double.MAX_VALUE);
        record.setMaxWidth(Double.MAX_VALUE);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.BOTTOM_CENTER);
       
        
        Clock.setFont(Font.font("Cambria", 32));
        vbox.setPadding(new Insets(15, 15, 0, 15));
        vbox.getChildren().addAll(Clock);
        
        
         VBox records = new VBox();
        records.setAlignment(Pos.BOTTOM_CENTER);
      
        ScrollPane s1 = new ScrollPane();
        s1.setContent(records);
        s1.setMaxHeight(188);
        controls.getChildren().addAll(s1);       
      
       
        
       
      //  records.getChildren().addAll(lap1, lap2, lap3);
        

        controls.setAlignment(Pos.BOTTOM_CENTER);
        controls.setSpacing(10);
        controls.setPadding(new Insets(30, 30, 30, 30));
        controls.getChildren().addAll(vbox);
        controls.getChildren().addAll(record, startStop);
        controls.getChildren().addAll(records);
        controls.toFront();

   
        
        record.setOnAction((ActionEvent event)-> {
            if(isRunning()){
                count1++;
                Label test = new Label("Lap " + count1 + ": " + time);
                
               records.getChildren().addAll(test);
               
             
//            lapSeconds = secondsElapsed/100;
//            lapCount = lapSeconds - lapSaver;
//          //  mili = 60/lapCount;
//           Label test = new Label((int)lapCount + "."+ (int)miliCount %60);
//           lapSaver += lapCount;
//           records.getChildren().addAll(test);
            
            }
           if(!(isRunning())) {
           count1 = 0;
           secondsElapsed = 0;
          
           lapSeconds=0;
           lapSaver=0;
           miliCount=0;
         //  tickTimeInSeconds=0;
           handImageView.setRotate(0);
           time = "0:0:0.0";
           secCount=0;
           minCount=0;
           hourCount=0;
           Clock.setText(time);
           records.getChildren().clear();
           
           
       }
                

               
                
        });
        
        startStop.setOnAction((ActionEvent)-> {
             
            if(isRunning()){
               
                timeline.stop();
                startStop.setText("Start");
                record.setText("Reset");

            }
            else{
                
                timeline.play();
                startStop.setText("Stop");
                record.setText("Record");
                updateStopWatch();
                 
                
            }
      });
        
        rootContainer.getChildren().addAll(dialImageView, handImageView, controls);
    }

    public void setUpTimer(){
        if(isRunning()){
            timeline.stop();
        }
        
        keyFrame = new KeyFrame(Duration.millis(tickTimeInSeconds * 1000),(ActionEvent) -> {
            updateStopWatch();
        });
        
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        
       
    }
    
    private void updateStopWatch(){
       
      
        secondsElapsed += tickTimeInSeconds;
        double rotation =  secondsElapsed * angleDeltaPerSeconds;
         handImageView.setRotate(rotation);       
                //handImageView.getRotate();
        //handImageView.setRotate(rotation + 6);
        
            miliCount++;
            if(miliCount == 100){
                miliCount = 0;
                secCount++;
            }
            if(secCount == 60){
                secCount = 0;
                minCount++;
            }
            if(minCount == 60){
                minCount = 0;
                hourCount++;
            } 
             time = (long)hourCount % 24 + ":" + (long)minCount % 60 + ":" + (long)secCount % 60 + "." + (long)miliCount % 100;
             Clock.setText(time);
        
    }
    
    public void start(){
        timeline.play();
    }
    
    public Double getWidth(){
        if(dialImage != null){
            return dialImage.getWidth();
        }
        else {
            return 0.0;
        }
    }
    
    public double getHeight(){
        if(dialImage != null) return dialImage.getHeight();
        else return 0.0;
            
    }
    
    public Parent getRootContainer(){
        return rootContainer;
    }
    
    public void setTickTime(double tickTimeInSeconds){
        this.tickTimeInSeconds = tickTimeInSeconds;
        setUpTimer();
        
         if(!isRunning()){
            timeline.play();
        }
    }
    
    public boolean isRunning(){
        if(timeline != null){
            if(timeline.getStatus() == Animation.Status.RUNNING){
                return true;
            
            }
        }
        return false;
    }
    
  
    
    

}
