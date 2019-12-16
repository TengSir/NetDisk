package edu.hbuas.netdisk.control;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import edu.hbuas.netdisk.model.Message;
import edu.hbuas.netdisk.model.MessageType;
import edu.hbuas.netdisk.model.User;
import edu.hbuas.netdisk.util.ControllData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class MainControl implements Initializable {
	
	@FXML
	private Button uploadFileButton;
	
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ControllData.allControllers.put("Main", this);//在控制器初始化的代码中将当前控制器存入到一个公共的集合中，方便其他控制器如果想使用当前控制器对象时直接可以从集合里找出来
    	System.out.println("Main的控制器初始化代码");
    	uploadFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Socket  client=null;
				ObjectOutputStream  out=null;
				System.out.println("点击上传按钮");
				//1.弹出一个文件选择框，让用户选择文件
				FileChooser  fc=new FileChooser();
				File  selectFile=fc.showOpenDialog(uploadFileButton.getScene().getWindow());
				System.out.println(selectFile.getName()+"\r\n"+selectFile.getAbsolutePath());
				
				//2.在执行上传之前先建立底层的socket链接
				try {
					  client=new Socket("172.19.4.45",9999);
					  out=new ObjectOutputStream(client.getOutputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//3.上传之前先封装一个消息对象，发送给服务器通知他我要干嘛， 让他准备执行对应的代码应对
				Message  uploadMessage=new Message();
				User user=((LoginControl)ControllData.allControllers.get("Login")).getUser();//通过公共的集合获取LoginControl对象中的user属性
				uploadMessage.setFromUser(user.getUsername());
				uploadMessage.setType(MessageType.UPLOAD);
				uploadMessage.setFilename(selectFile.getName());
				uploadMessage.setFileLength(selectFile.length());
				
				//4.先将当前的消息对象发给服务器，让服务器准备接受我上传文件的数据
				try {
					out.writeObject(uploadMessage);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//5.消息对象发给服务器之后，就开始上传文件数据
				try {
					FileInputStream  fileIn=new FileInputStream(selectFile);
					byte[] bs=new byte[1024];
					int length=-1;
					while((length=fileIn.read(bs))!=-1) {
						out.write(bs,0,length);
						out.flush();
					}
					Alert  a=new Alert(Alert.AlertType.INFORMATION);
					a.setContentText("文件上传成功");
					a.show();
					out.close();
					client.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

    }
    
}
