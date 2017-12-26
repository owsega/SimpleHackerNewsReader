package com.owsega.hackernews;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        String packageName = appContext.getPackageName();
        assertEquals("com.owsega.hackernews", packageName);

        PackageInfo packageInfo = appContext.getPackageManager().getPackageInfo(packageName, 0);
        int targetSdkVersion = packageInfo.applicationInfo.targetSdkVersion;
        int minSdkVersion = packageInfo.applicationInfo.minSdkVersion;

//        assertEquals(11, minSdkVersion);
        assertEquals(25, targetSdkVersion);

    }
}
