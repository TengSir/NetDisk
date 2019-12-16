package edu.hbuas.netdisk.control;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import edu.hbuas.netdisk.model.User;
import edu.hbuas.netdisk.model.UserDAO;
import edu.hbuas.netdisk.util.ControllData;
import edu.hbuas.netdisk.view.Login;
import edu.hbuas.netdisk.view.Main;

public class LoginControl implements Initializable {
	private UserDAO  dao;
	private User user;
	
	public User getUser() {
		return user;
	}
	@FXML
	private TextField  username;
	@FXML
	private PasswordField password;

	@FXML
	public void processLogin(ActionEvent event) {
		
		
		
		//1.获取注册界面上的用户名和密码
		String usernameInput=username.getText();
		String passwordInput=password.getText();
		
		//2.直接调用dao的登录方法查询该用户是否存在
		try {
			User user=dao.login(usernameInput, passwordInput);
			this.user=user;
			if(user==null) {
				Alert  a=new Alert(Alert.AlertType.ERROR);
				a.setContentText("用户名和密码不正确!");
				a.show();
			}else {
				Main  l=new Main();
				l.start(new Stage());
				
				//隐藏当前窗口
				((Button)event.getSource()).getScene().getWindow().hide();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Alert  a=new Alert(Alert.AlertType.ERROR);
			a.setContentText("登录失败!");
			a.show();
		}
	}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ControllData.allControllers.put("Login", this);
    	dao=new UserDAO();
    }
}
