package com.ysj.beans.io;

import java.io.IOException;
import java.io.InputStream;

import com.Aspectj.beans.io.Resource;
import com.Aspectj.beans.io.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;



/**
 * @author yihua.huang@dianping.com
 */
public class ResourceLoaderTest {

	@Test
	public void test() throws IOException {
		ResourceLoader resourceLoader = new ResourceLoader();
		Resource resource = resourceLoader.getResource("tinyioc.xml");
		InputStream inputStream = resource.getInputStream();
		Assert.assertNotNull(inputStream);
	}
}
