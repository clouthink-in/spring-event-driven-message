package in.clouthink.daas.edm.sms.impl;

import in.clouthink.daas.edm.sms.AdvancedSmsMessage;
import in.clouthink.daas.edm.sms.SmsMessage;
import in.clouthink.daas.edm.sms.SmsSender;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class SmsSenderYunpianImpl implements SmsSender {

	private static final Log logger = LogFactory.getLog(SmsSenderYunpianImpl.class);

	private YunpianOptions yunpianOptions;

	private CloseableHttpClient client;

	public SmsSenderYunpianImpl(YunpianOptions yunpianOptions) {
		this.yunpianOptions = yunpianOptions;
		this.client = HttpClients.createDefault();
	}

	@Override
	public void send(SmsMessage smsMessage) {
		doSend(smsMessage, yunpianOptions);
	}

	@Override
	public <T> void send(AdvancedSmsMessage<T> smsMsg) {
		if (smsMsg.getOptions() == null) {
			doSend(new SmsMessage(smsMsg.getCellphone(), smsMsg.getMessage()), yunpianOptions);
			return;
		}

		doSend(new SmsMessage(smsMsg.getCellphone(), smsMsg.getMessage()), (YunpianOptions) smsMsg.getOptions());
	}

	private void doSend(SmsMessage smsMessage, YunpianOptions options) {
		Map<String,String> params = new HashMap<>();
		params.put("apikey", options.getKey());
		params.put("tpl_id", options.getTemplateId());
		params.put("tpl_value", smsMessage.getMessage());
		params.put("mobile", smsMessage.getCellphone());

		String response = post(options.getEndpoint(), params);
		logger.info("yunpian sms send response:" + response);
	}

	private String post(String url, Map<String,String> paramsMap) {
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (Map.Entry<String,String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity);
				if (!responseText.contains("\"code\":0")) {
					throw new SmsException("发送短信失败,返回结果:" + responseText);
				}
			}
		}
		catch (SmsException e) {
			throw e;
		}
		catch (Exception e) {
			throw new SmsException("发送短信失败,原因:" + e, e);
		}
		finally {
			try {
				response.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}


}
