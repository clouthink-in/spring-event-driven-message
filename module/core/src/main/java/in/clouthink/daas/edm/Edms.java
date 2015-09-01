package in.clouthink.daas.edm;

import java.util.Iterator;
import java.util.ServiceLoader;

public class Edms {
    
    private static ServiceLoader<Edm> codecSetLoader = ServiceLoader.load(Edm.class);
    
    public static Edm getEdm() {
        Iterator<Edm> iterator = codecSetLoader.iterator();
        
        if (iterator.hasNext()) {
            return iterator.next();
        }
        
        return null;
    }
    
}
