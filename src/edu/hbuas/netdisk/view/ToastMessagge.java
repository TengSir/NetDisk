package edu.hbuas.netdisk.view;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ToastMessagge {
	private static  Stage  stage=new Stage();
	private static Label  label=new Label();
	public static void main(String[] args) {
		ToastMessagge.toast("test", 500,null); 
	}
   static {
	   stage.setAlwaysOnTop(true);
	   stage.initStyle(StageStyle.TRANSPARENT);
   }
   /**
    * Toast指定时间消失消息
    * @param msg
    * @param time
    */
   public static void toast(String msg, int time,Window  w) {
       label.setText(msg);
       TimerTask task= new TimerTask() {
           @Override
           public void run() {
               Platform.runLater(()->stage.close());
           }
       };
       init(msg);
       Timer timer=new Timer();
       timer.schedule(task,time);
       stage.setX(w.getX()+350);
       stage.setY(w.getY()+250);
       stage.show();
   }

   //设置消息
   private static void init(String msg) {
	   Label label=new Label(msg);//默认信息
       label.setStyle("-fx-background: rgba(56,56,56,0.8);-fx-border-radius: 4;-fx-background-radius: 4");//label透明,圆角
       label.setTextFill(Color.rgb(225,255,226));//消息字体颜色
       label.setPrefHeight(30);
       label.setPadding(new Insets(5,10,5,10));
       label.setAlignment(Pos.CENTER);//居中
       label.setFont(new Font(20));//字体大小
       Scene scene=new Scene(label);
       scene.setFill(null);//场景透明
       stage.setScene(scene);
   }
}
