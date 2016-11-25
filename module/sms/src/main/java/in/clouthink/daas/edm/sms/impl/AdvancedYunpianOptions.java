package in.clouthink.daas.edm.sms.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author dz
 */
@ConfigurationProperties(prefix = "cn.com.bbt.sms")
public class AdvancedYunpianOptions extends YunpianOptions {

	private String registerTemplateId;

	private String forgetPasswordTemplateId;

	public String getRegisterTemplateId() {
		return registerTemplateId;
	}

	public void setRegisterTemplateId(String registerTemplateId) {
		this.registerTemplateId = registerTemplateId;
	}

	public String getForgetPasswordTemplateId() {
		return forgetPasswordTemplateId;
	}

	public void setForgetPasswordTemplateId(String forgetPasswordTemplateId) {
		this.forgetPasswordTemplateId = forgetPasswordTemplateId;
	}

}
