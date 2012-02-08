package org.tensin.wmq.common;

import org.junit.Assert;
import org.junit.Test;

public class WMQEnhancedExceptionTestCase {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	@Test
	public void testMQErrorsReading() throws Exception {
		MQErrors mqErrors = WMQEnhancedException.getMQErrorsMessages();
		Assert.assertTrue(mqErrors != null);
		Assert.assertEquals(781, mqErrors.getErrorsCount());
		System.out.println(mqErrors.toString());
	}

}
