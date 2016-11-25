package in.clouthink.daas.edm.sms.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "in.clouthink.daas.edm.sms.webchinese")
public class WebChineseOptions {

	private String server;

	private String uid;

	private String key;

	public WebChineseOptions() {
	}

	public WebChineseOptions(String server, String uid, String key) {
		this.server = server;
		this.uid = uid;
		this.key = key;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
