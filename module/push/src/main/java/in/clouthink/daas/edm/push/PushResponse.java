package in.clouthink.daas.edm.push;

import java.io.Serializable;

/**
 * @since 1.0.1
 */
public class PushResponse implements Serializable {
    
    private PushMessage request;
    
    private boolean success = false;
    
    private boolean networkFailed = false;
    
    private String networkFailureReason;
    
    private String errorCode;
    
    private String errorMessage;
    
    public PushMessage getRequest() {
        return request;
    }
    
    public void setRequest(PushMessage request) {
        this.request = request;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public boolean isNetworkFailed() {
        return networkFailed;
    }
    
    public void setNetworkFailed(boolean networkFailed) {
        this.networkFailed = networkFailed;
    }
    
    public String getNetworkFailureReason() {
        return networkFailureReason;
    }
    
    public void setNetworkFailureReason(String networkFailureReason) {
        this.networkFailureReason = networkFailureReason;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PushResponse{");
        sb.append("request=").append(request);
        sb.append(", success=").append(success);
        sb.append(", networkFailed=").append(networkFailed);
        sb.append(", networkFailureReason='")
          .append(networkFailureReason)
          .append('\'');
        sb.append(", errorCode='").append(errorCode).append('\'');
        sb.append(", errorMessage='").append(errorMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
