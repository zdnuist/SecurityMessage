package me.zdnuist.securitymessage.activity;

import me.zdnuist.securitymessage.util.AppUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    protected Context context;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        context = getApplicationContext();
        AppUtils.initActionBar(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
