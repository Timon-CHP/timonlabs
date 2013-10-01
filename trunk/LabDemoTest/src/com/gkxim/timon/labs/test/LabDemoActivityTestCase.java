/**
 * File: LabDemoActivityTestCase.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 01-10-2013
 * 
 */
package com.gkxim.timon.labs.test;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;

import com.gkxim.timon.labs.LabDemoActivity;

public class LabDemoActivityTestCase extends
		ActivityInstrumentationTestCase2<LabDemoActivity> {

	LabDemoActivity mTarget;
	private Button mBtnTestFinger;

	public LabDemoActivityTestCase() {
		// Construting Testcase for LabDemoActivity
		super(LabDemoActivity.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		// Setup environment, resource and initialize values for the TEST
		// would be empty if no need to setup
		mTarget = this.getActivity();
		mBtnTestFinger = (Button) mTarget
				.findViewById(com.gkxim.timon.labs.R.id.btn_open_fingerpaint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.test.ActivityInstrumentationTestCase2#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		// Clean up environment, heap, stored values to avoid of leaks.
		// would be empty if no need to tear down.
		super.tearDown();
	}

	public void testPassLabDemoActivityTestCase_addMethod() {
		Log.i("Testing", "testing LabDemoActivity.addMethodForTest()");
		int expectedValue = 9;
		// test method: addMethodForTest() from LabDemoActivity.
		// the target that need to be tested should be defined as public.
		assertEquals(expectedValue, mTarget.addMethodForTest(4, 5));
	}

	public void testFailLabDemoActivityTestCase_addMethod() {
		Log.i("Testing", "testing fail LabDemoActivity.addMethodForTest()");
		int expectedValue = 10;
		// test method: addMethodForTest() from LabDemoActivity.
		// the target that need to be tested should be defined as public.
		assertFalse((mTarget.addMethodForTest(4, 5) == expectedValue? true
				:false));
	}

	public void testPassLabDemoActivityTestCase_uiValue() {
		String expectedValue = "Finger Paint";
		assertEquals(expectedValue, mBtnTestFinger.getText().toString());
	}

	public void testFailLabDemoActivityTestCase_uiValue() {
		String expectedValue = "finger paint";
		assertFalse(expectedValue.equals(mBtnTestFinger.getText().toString()));
	}
}
