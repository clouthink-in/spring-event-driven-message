package in.clouthink.daas.edm.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The message definition.
 */
public class PushMessage implements Serializable {

	/**
	 * id to track the message
	 *
	 * @since 1.0.1
	 */
	private String id;

	/**
	 * @since 1.0.1
	 */
	private PushWay pushWay;

	private PushType pushType;

	private String title;

	private String content;

	private String[] groups;

	/**
	 * @since 1.0.1
	 */
	private String[] tags;

	/**
	 * @since 1.0.1
	 */
	private String[] alias;

	private String[] devices;

	private Map<String,Object> attributes = new HashMap<String,Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PushWay getPushWay() {
		return pushWay;
	}

	public void setPushWay(PushWay pushWay) {
		this.pushWay = pushWay;
	}

	public PushType getPushType() {
		return pushType;
	}

	public void setPushType(PushType pushType) {
		this.pushType = pushType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getGroups() {
		return groups;
	}

	public void setGroups(String[] groups) {
		this.groups = groups;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String[] getDevices() {
		return devices;
	}

	public void setDevices(String[] devices) {
		this.devices = devices;
	}

	public String[] getAlias() {
		return alias;
	}

	public void setAlias(String[] alias) {
		this.alias = alias;
	}

	public Map<String,Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String,Object> attributes) {
		this.attributes = attributes;
	}

}
