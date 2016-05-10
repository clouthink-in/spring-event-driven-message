package in.clouthink.daas.edm;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The <code>Edm</code> factory based on Java <code>ServiceLoader</code>.
 */
public class Edms {
    
    private static Edm defaultInstance;
    
    private static ConcurrentHashMap<String, Edm> namedInstances = new ConcurrentHashMap<String, Edm>();
    
    static {
        ServiceLoader<Edm> codecSetLoader = ServiceLoader.load(Edm.class);
        Iterator<Edm> iterator = codecSetLoader.iterator();
        if (iterator.hasNext()) {
            defaultInstance = iterator.next();
        }
    }
    
    public static Edm getEdm() {
        return defaultInstance;
    }
    
    public static Edm getEdm(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The null name is not allowed.");
        }
        Edm edm = namedInstances.get(name);
        if (edm != null) {
            return edm;
        }
        
        if (defaultInstance == null) {
            return null;
        }
        
        if (!(defaultInstance instanceof EdmInstanceFactory)) {
            throw new UnsupportedOperationException();
        }
        edm = ((EdmInstanceFactory) defaultInstance).newInstance();
        edm = namedInstances.putIfAbsent(name, edm);
        if (edm != null) {
            return edm;
        }
        return namedInstances.get(name);
    }
    
}
