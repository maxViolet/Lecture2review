package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.maximfialko.Utils.DetailNews_Fragment;
import com.example.android.maximfialko.Utils.Navigator;

public class MainActivity extends AppCompatActivity {

    NewsList_Fragment newsList_Fragment;

    static final String TAG_LIST_FRAGMENT = "recycler_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_acitivity);

        if (savedInstanceState == null) {
            newsList_Fragment = NewsList_Fragment.newInstance();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.main_frame, newsList_Fragment, TAG_LIST_FRAGMENT);
//        transaction.addToBackStack(null);
            transaction.commit();
        } else { }

//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.main_frame, newsList_Fragment, TAG_LIST_FRAGMENT).addToBackStack(TAG_LIST_FRAGMENT).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_switch:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



//    @Override
//    public  getContextFrom(Activity activity) {
//        return activity.getApplicationContext();
//    }
}
