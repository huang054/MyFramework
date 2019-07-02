package com.ysj.beans.xml;

import java.util.Map;

import com.Aspectj.beans.BeanDefinition;
import com.Aspectj.beans.io.ResourceLoader;
import com.Aspectj.beans.xml.XmlBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;



/**
 * @author yihua.huang@dianping.com
 */
public class XmlBeanDefinitionReaderTest {

	@Test
	public void test() throws Exception {
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
		xmlBeanDefinitionReader.loadBeanDefinitions("tinyioc.xml");
		Map<String, BeanDefinition> registry = xmlBeanDefinitionReader.getRegistry();
		Assert.assertTrue(registry.size() > 0);
	}
}
