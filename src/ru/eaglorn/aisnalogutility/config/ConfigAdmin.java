package ru.eaglorn.aisnalogutility.config;

public class ConfigAdmin {
	public String LOGIN = "";
	public String PASSWORD = "";

	public ConfigAdmin(String login, char[] password) {
		this.LOGIN = login;
		this.PASSWORD = String.valueOf(password);
	}
}
