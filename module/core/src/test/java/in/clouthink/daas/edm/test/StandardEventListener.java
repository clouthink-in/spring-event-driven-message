package in.clouthink.daas.edm.test;

import in.clouthink.daas.edm.EventListener;

public class StandardEventListener implements EventListener<String> {
    
    @Override
    public void onEvent(String message) {
        System.out.println("Standard " + message);
    }
    
}
