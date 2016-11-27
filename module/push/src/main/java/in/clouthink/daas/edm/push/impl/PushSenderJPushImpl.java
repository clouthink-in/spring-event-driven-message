package in.clouthink.daas.edm.push.impl;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.google.gson.JsonObject;
import in.clouthink.daas.edm.Listenable;
import in.clouthink.daas.edm.push.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * The jpush impl for push sender
 *
 * @author dz
 */
public class PushSenderJPushImpl implements PushSender {

	private static final Log logger = LogFactory.getLog(PushSenderJPushImpl.class);

	private PushResponseReceiver receiver = new PushResponseReceiverImpl();

	private JPushOptions jPushOptions;

	private PushClient pushClient;

	public PushSenderJPushImpl(JPushOptions jPushOptions) {
		this.jPushOptions = jPushOptions;
		this.pushClient = new PushClient(jPushOptions.getAppSecret(),
										 jPushOptions.getAppKey(),
										 jPushOptions.getMaxRetries());
	}

	/**
	 * @param receiver
	 * @since 1.0.1
	 */
	public void setPushResponseReceiver(PushResponseReceiver receiver) {
		if (receiver == null) {
			throw new NullPointerException();
		}
		this.receiver = receiver;
	}

    @Listenable
	@Override
	public void push(PushMessage message) {
		//use the default receiver
		push(message, receiver);
	}

	@Override
	public void push(PushMessage message, PushResponseReceiver responseReceiver) {
		if (message.getPushType() == null) {
			throw new IllegalArgumentException("The push type is required.");
		}
		if (message.getPushWay() == null) {
			//notification by default
			message.setPushWay(PushWay.Notification);
		}

		switch (message.getPushType()) {
			case All: {
				pushToAll(message, responseReceiver);
				return;
			}
			case Group: {
				pushToGroup(message, responseReceiver);
				return;
			}
			case TagOr: {
				pushToTagOr(message, responseReceiver);
			}
			case TagAnd: {
				pushToTagAnd(message, responseReceiver);
			}
			case Alias: {
				pushToAlias(message, responseReceiver);
			}
			case Device: {
				pushToDevice(message, responseReceiver);
			}
		}

		return;
	}

	private void pushToAll(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.all()).build();
		doPush(message, payload, responseReceiver);
	}

	@Deprecated
	private void pushToGroup(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.tag_and(message.getGroups())).build();
		doPush(message, payload, responseReceiver);
	}

	private void pushToTagOr(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.tag(message.getTags())).build();
		doPush(message, payload, responseReceiver);
	}

	private void pushToTagAnd(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.tag_and(message.getTags())).build();
		doPush(message, payload, responseReceiver);
	}

	private void pushToAlias(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.alias(message.getAlias())).build();
		doPush(message, payload, responseReceiver);
	}

	@Deprecated
	private void pushToDevice(PushMessage message, PushResponseReceiver responseReceiver) {
		PushPayload payload = createPayloadBuilder(message).setAudience(Audience.alias(message.getDevices())).build();
		doPush(message, payload, responseReceiver);
	}

	private PushPayload.Builder createPayloadBuilder(PushMessage message) {
		if (message.getPushWay() == PushWay.Notification) {
			IosNotification.Builder iosBuilder = IosNotification.newBuilder().setAlert(message.getTitle()).incrBadge(1);
			processAttributes(message, iosBuilder);
			IosNotification iosNotification = iosBuilder.addExtra("content", message.getContent()).build();

			AndroidNotification.Builder androidBuilder = AndroidNotification.newBuilder().setAlert(message.getTitle());
			processAttributes(message, androidBuilder);
			AndroidNotification androidNotification = androidBuilder.addExtra("content", message.getContent()).build();

			return PushPayload.newBuilder()
							  .setPlatform(Platform.all())
							  .setNotification(Notification.newBuilder()
														   .addPlatformNotification(iosNotification)
														   .addPlatformNotification(androidNotification)
														   .build())
							  .setOptions(Options.newBuilder()
												 .setTimeToLive(jPushOptions.getTimeToLive())
												 .setApnsProduction(jPushOptions.isApnsProduction())
												 .build());
		}
		if (message.getPushWay() == PushWay.Message) {
			Message.Builder msgBuilder = Message.newBuilder()
												.setTitle(message.getTitle())
												.setMsgContent(message.getContent());
			processAttributes(message, msgBuilder);

			return PushPayload.newBuilder()
							  .setPlatform(Platform.all())
							  .setMessage(msgBuilder.build())
							  .setOptions(Options.newBuilder()
												 .setTimeToLive(jPushOptions.getTimeToLive())
												 .setApnsProduction(jPushOptions.isApnsProduction())
												 .build());
		}

		return null;
	}

	private void processAttributes(PushMessage message, Message.Builder iosBuilder) {
		for (Map.Entry<String,Object> entry : message.getAttributes().entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Number) {
				iosBuilder.addExtra(key, (Number) value);
			}
			else if (value instanceof Boolean) {
				iosBuilder.addExtra(key, (Boolean) value);
			}
			else if (value instanceof JsonObject) {
				iosBuilder.addExtra(key, ((JsonObject) value).toString());
			}
			else if (value instanceof String) {
				iosBuilder.addExtra(key, (String) value);
			}
			else {
				iosBuilder.addExtra(key, value.toString());
			}
		}
	}

	private void processAttributes(PushMessage message, IosNotification.Builder iosBuilder) {
		for (Map.Entry<String,Object> entry : message.getAttributes().entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Number) {
				iosBuilder.addExtra(key, (Number) value);
			}
			else if (value instanceof Boolean) {
				iosBuilder.addExtra(key, (Boolean) value);
			}
			else if (value instanceof JsonObject) {
				iosBuilder.addExtra(key, (JsonObject) value);
			}
			else if (value instanceof String) {
				iosBuilder.addExtra(key, (String) value);
			}
			else {
				iosBuilder.addExtra(key, value.toString());
			}
		}
	}

	private void processAttributes(PushMessage message, AndroidNotification.Builder buideer) {
		for (Map.Entry<String,Object> entry : message.getAttributes().entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Number) {
				buideer.addExtra(key, (Number) value);
			}
			else if (value instanceof Boolean) {
				buideer.addExtra(key, (Boolean) value);
			}
			else if (value instanceof JsonObject) {
				buideer.addExtra(key, (JsonObject) value);
			}
			else if (value instanceof String) {
				buideer.addExtra(key, (String) value);
			}
			else {
				buideer.addExtra(key, value.toString());
			}
		}
	}

	private void doPush(PushMessage message, PushPayload payload, PushResponseReceiver responseReceiver) {
		if (payload == null) {
			return;
		}
		PushResponse response = new PushResponse();
		response.setRequest(message);
		try {
			PushResult result = pushClient.sendPush(payload);
			response.setSuccess(result.isResultOK());
		}
		catch (APIConnectionException e) {
			response.setSuccess(false);
			response.setNetworkFailed(true);
			response.setNetworkFailureReason(e.getMessage());
		}
		catch (APIRequestException e) {
			response.setSuccess(false);
			response.setErrorMessage(e.getErrorMessage());
			response.setErrorCode(Integer.toString(e.getErrorCode()));
		}
		finally {
			try {
				if (responseReceiver != null) {
					responseReceiver.onResponse(response);
				}
			}
			catch (Throwable e) {
				//ignore
			}
		}
	}

}
