package in.clouthink.daas.edm.push;

/**
 * @since 1.0.1
 */
public class PushResponse {

	private boolean success = false;

	private boolean networkFailed = false;

	private String networkFailureReason;

	private String errorCode;

	private String errorMessage;

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
}
