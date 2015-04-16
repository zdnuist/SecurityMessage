package me.zdnuist.securitymessage.activity;

import me.zdnuist.securitymessage.fragment.LoadingFragment;
import me.zdnuist.securitymessage.util.AppUtils;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    protected Context context;
    
    protected LoadingFragment loading;

    protected void onCreate(Bundle savedInstanceState, int layoutResID) {
        super.onCreate(savedInstanceState);
        setContentView(layoutResID);

        loading = new LoadingFragment();
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
    
    protected void showLoading(){
		loading.show(getFragmentManager(), "loading");
    }
    
    protected void dimissLoading(){
    		loading.dismissAllowingStateLoss();
    }
}
