package com.evanhoe.tango.objs;

public class UserLoginResult {
	
	public boolean isServiceAvailable() {
		return isServiceAvailable;
	}
	public void setServiceAvailable(boolean isServiceAvailable) {
		this.isServiceAvailable = isServiceAvailable;
	}

	private boolean isServiceAvailable;
	private User user;
	private boolean isOnline;
	private String token;

	public boolean isOnline() {
		return isOnline;
	}
	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setToken(String token) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public UserLoginResult(User user,boolean isServiceAvailable,boolean isOnline) {
		super();
		this.isServiceAvailable = isServiceAvailable;
		this.isOnline = isOnline;
		this.user = user;
	}
	public UserLoginResult(User user,boolean isServiceAvailable,boolean isOnline,String token) {
		super();
		this.isServiceAvailable = isServiceAvailable;
		this.isOnline = isOnline;
		this.user = user;
		this.token=token;
	}
	public UserLoginResult() {
		super();
	}
	

}
