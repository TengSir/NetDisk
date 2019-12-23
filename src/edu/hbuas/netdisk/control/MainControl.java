package edu.hbuas.netdisk.control;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.sun.org.apache.bcel.internal.generic.LALOAD;

import edu.hbuas.netdisk.config.NetDiskConfig;
import edu.hbuas.netdisk.model.Message;
import edu.hbuas.netdisk.model.MessageType;
import edu.hbuas.netdisk.model.User;
import edu.hbuas.netdisk.util.ControllData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class MainControl implements Initializable {
	
	@FXML
	private Button uploadFileButton;
	@FXML
	private Label usernameLabel;
	private User user;
	@FXML
	private FlowPane filesPane;
	
	

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ControllData.allControllers.put("Main", this);//在控制器初始化的代码中将当前控制器存入到一个公共的集合中，方便其他控制器如果想使用当前控制器对象时直接可以从集合里找出来
    	 user=((LoginControl)ControllData.allControllers.get("Login")).getUser();//通过公共的集合获取LoginControl对象中的user属性
    	usernameLabel.setText(user.getUsername());//将登陆时查询的用户信息更新到主窗口的ui上
    	
    	
    	/**
    	 * 这是主窗口加载前必须执行的控制器的初始化方法
    	 * 我们在这里建立一个socket连接，
    	 * 提前将当前用户的文件列表从服务器读取过来，
    	 * 然后更新到当前用户的主窗口列表里
    	 */
    	
    	try {
			Socket  client=new Socket(NetDiskConfig.netDiskServerIP,NetDiskConfig.netDiskServerPort);
			ObjectOutputStream  out=new ObjectOutputStream(client.getOutputStream());
			ObjectInputStream  in=new ObjectInputStream(client.getInputStream());
			
			//封装一个加载文件的消息，通知服务器我要读取我的文件列表
			Message  loadFiles=new  Message();
			loadFiles.setFromUser(user.getUsername());
			loadFiles.setType(MessageType.LOADFILES);
			
			//使用socket的流讲当前消息对象发送给服务器
			out.writeObject(loadFiles);
			out.flush();
			
			//消息发送完毕后，服务期接收到就会处理，然后会回复我们一个结果
			//这里需要使用socket的输入流读取服务器给我的结果
			Message allFiles=(Message)in.readObject();
			System.out.println(allFiles);
			
			
			
			for(File  f:allFiles.getAllFiles()) {
				System.out.println(f.getName());
				//在解析所有文件列表之前应该先加载设计好的单个文件UI控件XML
//				Group  oneFile=FXMLLoader.load(new File("resources/fxml/File.fxml").toURL());
				
				TextField  text=new TextField();
				text.setText(f.getName());
				//循环一次，读取到一个文件，然后加载一个文件的Vbox控件
				//然后将这个文件组件加到网盘的右边显示文件列表的pane里面
				filesPane.getChildren().add(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 上传文件的方法
     * @param event
     */
    @FXML
    public void uploadFile(ActionEvent event) {
    	Socket  client=null;
		ObjectOutputStream  out=null;
		System.out.println("点击上传按钮");
		//1.弹出一个文件选择框，让用户选择文件
		FileChooser  fc=new FileChooser();
		File  selectFile=fc.showOpenDialog(uploadFileButton.getScene().getWindow());
		if(selectFile==null)return ;//判断如果用户没有选择文件，则不执行上传代码
		//2.在执行上传之前先建立底层的socket链接
		try {
			  client=new Socket(NetDiskConfig.netDiskServerIP,NetDiskConfig.netDiskServerPort);
			  out=new ObjectOutputStream(client.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//3.上传之前先封装一个消息对象，发送给服务器通知他我要干嘛， 让他准备执行对应的代码应对
		Message  uploadMessage=new Message();
		
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
			fileIn.close();
			out.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
