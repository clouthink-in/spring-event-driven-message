package in.clouthink.daas.edm.push.impl;

import in.clouthink.daas.edm.push.PushResponse;
import in.clouthink.daas.edm.push.PushResponseReceiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default jpush response receiver impl (only log down the response)
 *
 * @since 1.0.1
 */
public class PushResponseReceiverImpl implements PushResponseReceiver {

	private static final Log logger = LogFactory.getLog(PushResponseReceiverImpl.class);

	@Override
	public void onResponse(PushResponse response) {
		if (response.isSuccess()) {
			logger.debug("The message sent success.");
			return;
		}
		if (response.isNetworkFailed()) {
			logger.warn("The message sent failed because the network issue.");
			return;
		}
		logger.warn(String.format("the message sent failed with the error code '%s' and error message '%s' ",
								  response.getErrorCode(),
								  response.getErrorMessage()));
	}

}
