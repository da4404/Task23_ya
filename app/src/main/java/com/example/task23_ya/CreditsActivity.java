package com.example.task23_ya;

import android.os.Bundle;

/**
 * @author darya
 * @version 1.0
 * @since 10/05/2026
 * Credits activity showing app credits
 */
public class CreditsActivity extends BaseActivity {
    /**
     * Called when the activity is starting.
     * <p>
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }
}
