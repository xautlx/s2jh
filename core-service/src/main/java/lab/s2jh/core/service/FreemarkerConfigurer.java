package lab.s2jh.core.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import lab.s2jh.core.exception.ServiceException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

@Component
public class FreemarkerConfigurer extends Configuration {

	private final static Logger logger = LoggerFactory.getLogger(FreemarkerConfigurer.class);

	private static StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();

	public FreemarkerConfigurer() {
		this.setDefaultEncoding("UTF-8");
	}

	public String processTemplate(String templateName, Integer version, String templateContents,
			Map<String, Object> dataMap) {
		Assert.notNull(templateName);
		Assert.notNull(version);
		if (StringUtils.isBlank(templateContents)) {
			return null;
		}
		Object templateSource = stringTemplateLoader.findTemplateSource(templateName);
		if (templateSource == null) {
			logger.debug("Init freemarker template: {}", templateName);
			stringTemplateLoader.putTemplate(templateName, templateContents, version.longValue());
		} else {
			long ver = stringTemplateLoader.getLastModified(templateSource);
			if (version.intValue() > ver) {
				logger.debug("Update freemarker template: {}", templateName);
				stringTemplateLoader.putTemplate(templateName, templateContents, version.longValue());
			}
		}
		StringWriter strWriter = new StringWriter();
		try {
			this.getTemplate(templateName).process(dataMap, strWriter);
			strWriter.flush();
		} catch (TemplateException e) {
			throw new ServiceException("error.freemarker.template.process", e);
		} catch (IOException e) {
			throw new ServiceException("error.freemarker.template.process", e);
		}
		return strWriter.toString();
	}

}
