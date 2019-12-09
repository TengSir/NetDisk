package edu.hbuas.netdisk.control;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import edu.hbuas.netdisk.model.User;
import edu.hbuas.netdisk.model.UserDAO;
import edu.hbuas.netdisk.view.Login;

public class RegisterControl implements Initializable {
	private UserDAO  dao;
	
	@FXML
	private TextField  username;
	
	@FXML
	private PasswordField  password;
	@FXML
	private  RadioButton male;
	@FXML
	private TextField email;
	@FXML
	private  ChoiceBox<Integer> age;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	dao=new UserDAO();
    	System.out.println("控制器初始化代码");
    }
    
    
    @FXML
    public void processLogin(ActionEvent  event) {
    	System.out.println("点击了注册按钮");
    	//1.先获取用户在ui上填写的注册信息
    	String usernameInput=username.getText().trim();
    	String passwordInput=password.getText().trim();
    	String emailInput=email.getText().trim();
    	String sexInput=male.isSelected()?"男":"女";
    	
    	//2.做表单验证
    	
    	//3.将数据封装成一个javabean
    	User  user=new User();
    	user.setUsername(usernameInput);
    	user.setPassword(passwordInput);
    	user.setAge(20);
    	user.setSex(sexInput);
    	user.setEmail(emailInput);
    	System.out.println(user);
    	
    	//4.调用userdao的注册用户的方法执行注册功能
    	try {
			boolean result=dao.registerUser(user);
			
			//5.dao方法执行完毕之后根据dao的返回值做后续处理
			if(result) {
				Alert  a=new Alert(Alert.AlertType.INFORMATION);
				a.setContentText("注册成功!");
				a.show();
				Login  l=new Login();
				l.start(new Stage());
				
				//隐藏当前窗口
				((Button)event.getSource()).getScene().getWindow().hide();
				
			}else {
				Alert  a=new Alert(Alert.AlertType.ERROR);
				a.setContentText("注册失败!");
				a.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Alert  a=new Alert(Alert.AlertType.ERROR);
			a.setContentText("注册失败!");
			a.show();
		}
    }
}
