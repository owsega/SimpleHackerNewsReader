package com.owsega.hackernews.util;

import android.support.design.widget.Snackbar;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.owsega.hackernews.R;
import com.owsega.hackernews.view.activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.owsega.hackernews.util.ContextUtils.createActionSnackbar;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class ContextUtilsTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        ContextUtils utils = new ContextUtils();
    }

    @Test
    public void testSnackbarCalls() {
        String message = "test message";
        String action = "test action";
        MainActivity activity = activityRule.getActivity();
        View rootView = activity.findViewById(R.id.content);
        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClicked");
            }
        };

        Snackbar snackbar = createActionSnackbar(rootView, message, action, listener);
        snackbar.show();
        View snackbarLayout = snackbar.getView();
        TextView snackbarTextView = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
        Button button = (Button) snackbarLayout.findViewById(R.id.snackbar_action);

        assertNotNull(snackbarTextView);
        assertEquals(message, snackbarTextView.getText().toString());
        assertNotNull(button);
        assertEquals(true, button.hasOnClickListeners());
        assertEquals(action, button.getText().toString());
        assertEquals(View.VISIBLE, button.getVisibility());
    }
}
