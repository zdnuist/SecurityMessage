package me.zdnuist.securitymessage.bean;


public class MessageBean {
	
	public final static int RECIEVE_MSG = 1;
	public final static int SEND_MSG = 2;

	private int id;
	private String phoneNum;
	private String messageContent;
	private String contactName;
	private boolean isEncrypt; // 是否加密
	private String datetime;
	private int type; // 短信类型
	private String postMessageContent; //加密后的内容
	
	private int threadId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPostMessageContent() {
		return postMessageContent;
	}

	public void setPostMessageContent(String postMessageContent) {
		this.postMessageContent = postMessageContent;
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
	

}
