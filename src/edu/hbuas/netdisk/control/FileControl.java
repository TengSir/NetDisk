package edu.hbuas.netdisk.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class FileControl implements Initializable {

	@FXML
	private ImageView fileImage;
	@FXML
	private Label  fileName;
	public ImageView getFileImage() {
		return fileImage;
	}
	public void setFileImage(ImageView fileImage) {
		this.fileImage = fileImage;
	}
	public Label getFileName() {
		return fileName;
	}
	public void setFileName(Label fileName) {
		this.fileName = fileName;
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
