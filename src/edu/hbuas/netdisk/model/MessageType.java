package edu.hbuas.netdisk.model;

import java.io.Serializable;

/**
 * 封装一个网盘消息类型
 * @author Lenovo
 *
 */
public enum MessageType  implements Serializable {

	UPLOAD,
	DOWNLOAD,
	LOADFILES,
	DELETE
}
