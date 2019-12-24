package edu.hbuas.netdisk.control;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.sound.midi.Soundbank;

import com.sun.glass.events.MouseEvent;

import edu.hbuas.netdisk.config.NetDiskConfig;
import edu.hbuas.netdisk.model.Message;
import edu.hbuas.netdisk.model.MessageType;
import edu.hbuas.netdisk.model.User;
import edu.hbuas.netdisk.util.ControllData;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
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
			        VBox oneFile = addFileItem(f);//生成一个FileIteam对象，然后添加到网盘右边的文件列表面板中
			        
			      //循环一次，读取到一个文件，然后加载一个文件的Vbox控件
				//然后将这个文件组件加到网盘的右边显示文件列表的pane里面
				filesPane.getChildren().add(oneFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 根据给定的参数加载生成一个文件显示控件的方法
     * @param f
     * @return
     * @throws MalformedURLException
     * @throws IOException
     * @throws FileNotFoundException
     */
	private VBox addFileItem(File f) throws MalformedURLException, IOException, FileNotFoundException {
		URL l =new File("resources/fxml/File.fxml").toURL();
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(l);
		fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
		VBox  oneFile=fxmlLoader.load();
		FileControl  control=fxmlLoader.getController();
		//更新文件图标
		try {
			if(f.isDirectory()){
				  control.getFileImage().setImage(new Image(new FileInputStream("resources/imgs/fileicons/dir.png")));
			}else{
				String fileType=f.getName().substring(f.getName().lastIndexOf(".")+1,f.getName().length());
				 control.getFileImage().setImage(new Image(new FileInputStream("resources/imgs/fileicons/"+fileType+".gif")));
			}
		} catch (Exception e) {
			System.out.println("一个文件无法加载");
		}
		control.getFileName().setText(f.getName());//更新文件名
		oneFile.setOnMouseClicked(new EventHandler<Event>() {
        	@Override
        	public void handle(Event event) {
        		javafx.scene.input.MouseEvent  me=(javafx.scene.input.MouseEvent )event;
        		
        		if(me.getButton()==MouseButton.SECONDARY&&me.getClickCount()==1) {
        			//鼠标邮件单击当前的FIleItem对象时执行的事件代码
        			System.out.println("right one click");
        			// ContextMenu生成弹出菜单
        			ContextMenu  rightMenu=new ContextMenu();
        			MenuItem  item1=new MenuItem("下载");
        			item1.setOnAction(new EventHandler<ActionEvent>() {
        				@Override
        				public void handle(ActionEvent event) {
        						//当用户点击对应文件的下载按钮时应该执行的方法
        					String willDownloadFilename=((Label)oneFile.getChildren().get(1)).getText();//获得用户要想下载的文件名
        					
        					//封装一个Message对象发送给服务器，通知服务器我想要下载
        					Message  downloadMessage=new Message();
        					downloadMessage.setFromUser(user.getUsername());
        					downloadMessage.setType(MessageType.DOWNLOAD);
        					downloadMessage.setFilename(willDownloadFilename);
        					
        					//使用socket
        					try {
								Socket  client=new Socket(NetDiskConfig.netDiskServerIP,NetDiskConfig.netDiskServerPort);
							   ObjectOutputStream  out=new ObjectOutputStream(client.getOutputStream());
							   ObjectInputStream   in=new ObjectInputStream(client.getInputStream());
							   out.writeObject(downloadMessage);
							   out.flush();
							   //在真正传输之前弹出文件选择框，让用户选择文件保存路径
							   FileChooser  fc=new FileChooser();
							   File  savePath=fc.showSaveDialog(oneFile.getScene().getWindow());
							   if(!savePath.exists())savePath.mkdirs();//文件路径不存在，则新建目录
							   //准备一个文件输出流，指向用户要保存的文件路径
							   FileOutputStream  fileOut=new FileOutputStream(new File(savePath,willDownloadFilename));
							   //把Message发送给服务器之后就应该使用输入流等待服务器给我回发对应的文件数据
							   byte[]  bs=new byte[1024];
							   int length=-1;
							   while((length=in.read(bs))!=-1) {
								   fileOut.write(bs,0,length);
							   }
							   //文件传输完毕，关闭资源，关闭socket通道
							   fileOut.flush();
							   fileOut.close();
							   client.close();
							   //文件传输完毕后弹框提示
								Alert  a=new Alert(Alert.AlertType.INFORMATION);
								a.setContentText("文件下载成功");
								a.show();
        					} catch (Exception e){
								e.printStackTrace();
							}
        				}
					});
        			MenuItem  item2=new MenuItem("删除");
        			rightMenu.getItems().add(item1);
        			rightMenu.getItems().add(item2);
        			rightMenu.setPrefSize(100, 60);
        			rightMenu.show(oneFile, Side.BOTTOM, 0, 10);
        			
        		}else if(me.getButton()==MouseButton.PRIMARY&&me.getClickCount()==2) {
        			//当鼠标左键双击当时候执行的事件代码
        			System.out.println("left double click");
        		}
        	}
	});
//		oneFile.setOnMouseClicked(new EventHandler<Event>() {
//			@Override
//			public void handle(Event e) {
//				ContextMenu  menu=new ContextMenu();
//				menu.getItems().add(new MenuItem("download"));
//				menu.setPrefSize(100, 40);
//				menu.setX(10);
//				menu.show(oneFile, Side.BOTTOM, -20, -20);
//				menu.show(oneFile.getScene().getWindow());
//			}
//		});
		
		
		return oneFile;
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
			
			//上传成功后调用本地方法，将新上传的文件对象添加到右边的文件列表面板里
			VBox newFile=addFileItem(selectFile);
			filesPane.getChildren().add(newFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
