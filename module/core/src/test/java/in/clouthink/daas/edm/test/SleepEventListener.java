package in.clouthink.daas.edm.test;

import in.clouthink.daas.edm.EventListener;

/**
 *
 */
public class SleepEventListener implements EventListener<String> {
    
    private long duration;
    
    public SleepEventListener(long duration) {
        this.duration = duration;
    }
    
    @Override
    public void onEvent(String message) {
        if (duration > 0) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                System.out.println(Thread.currentThread() + ":Start to sleep");
                Thread.sleep(duration);
                System.out.println(Thread.currentThread()
                                   + ":Now wake up after "
                                   + (System.currentTimeMillis()
                                      - currentTimeMillis)
                                   + " millis");
            }
            catch (Exception e) {
            }
        }
        
        System.out.println("Standard " + message);
    }
    
}
