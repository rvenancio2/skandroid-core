package com.samknows.libcore;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.samknows.tests.Param;

@RunWith(RobolectricTestRunner.class)
public class JParam {

	@Test
	public final void test() {
		/* Test with two random string as parameters */
		{
			Param param = null;
			try {
				param = new Param("Ra n dom para meter!", "ranDom VaL ue");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			String sResult = "";
			boolean bResult = false;

			bResult = param.contains("Ra n dom para meter!");
			assertTrue(bResult == true);

			bResult = param.contains("name_1");
			assertTrue(bResult == false);

			sResult = param.getName();
			assertTrue(sResult.equals("ra n dom para meter!"));

			sResult = param.getValue();
			assertTrue(sResult == "ranDom VaL ue");

			bResult = param.isName("RA N dom pARA METER!");
			assertTrue(bResult == true);

			bResult = param.isName("Ra n  dom para meter!");
			assertTrue(bResult == false);

			bResult = param.isValue("ranDom VAL UE");
			assertTrue(bResult == true);

			bResult = param.isValue("ranDom VaL ue ");
			assertTrue(bResult == false);
		}

		{
			/* Test with erroneously supplied nulls as parameters */
			Param param = null;
			try {
				param = new Param(null, null);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			assertTrue( param == null);
		
		}

	}
}
