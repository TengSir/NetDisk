package edu.hbuas.netdisk.util;

import java.util.HashMap;

import javafx.fxml.Initializable;
import javafx.scene.control.Control;

/**
 * 设计一个简单的公共集合，用来将所有控制器放入同一个空间，方便控制器之间共享数据
 * @author Lenovo
 *
 */
public class ControllData {
	
	public final static HashMap<String, Initializable>  allControllers=new HashMap<String, Initializable>();

}
