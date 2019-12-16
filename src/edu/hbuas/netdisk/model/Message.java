package edu.hbuas.netdisk.model;

import java.io.Serializable;

/**
 * 这个类是封装一个网盘消息类型，在每次真正IO动作之前，先发送Message对象，通知服务器我准备执行什么操作
 * @author Lenovo
 *
 */
public class Message  implements Serializable{

	private String fromUser;//发送消息的网盘客户端用户名
	private MessageType  type;//消息类型
	private String filename;//要操作的文件名
	private long fileLength;//文件的长度
	@Override
	public String toString() {
		return "Message [fromUser=" + fromUser + ", type=" + type + ", filename=" + filename + ", fileLength="
				+ fileLength + "]";
	}
	public Message() {
		super();
	}
	public Message(String fromUser, MessageType type, String filename, long fileLength) {
		super();
		this.fromUser = fromUser;
		this.type = type;
		this.filename = filename;
		this.fileLength = fileLength;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
}
