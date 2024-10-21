package com.contoso.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * StrFormatUtil Tester.
 *
 * @author SeimiMaster contmaster@gmail.com
 * @version 1.0
 */
public class StrFormatUtilTest {

    /**
     * Method: parseCharset(String target)
     */
    @Test
    public void testParseCharset() throws Exception {
        String t = "text/html; charset=utf-16;text/html; charset=utf-8";
        String r = StrFormatUtil.parseCharset(t);
        System.out.println(r);
        Assertions.assertTrue("utf-8".equals(r));
    }
}
