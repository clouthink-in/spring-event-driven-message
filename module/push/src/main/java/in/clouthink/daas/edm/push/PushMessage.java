package in.clouthink.daas.edm.push;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PushMessage implements Serializable {
    
    private PushType pushType;
    
    private String title;
    
    private String content;
    
    private String[] groups;
    
    private String[] devices;
    
    private Map<String, Object> attributes = new HashMap<String, Object>();
    
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
    
    public String[] getDevices() {
        return devices;
    }
    
    public void setDevices(String[] devices) {
        this.devices = devices;
    }
    
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
