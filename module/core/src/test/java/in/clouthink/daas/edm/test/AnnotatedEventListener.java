package in.clouthink.daas.edm.test;

import in.clouthink.daas.edm.Listenable;

public class AnnotatedEventListener {
    
    @Listenable
    public void execute(String message) {
        System.out.println("Annotated " + message);
    }
    
}
