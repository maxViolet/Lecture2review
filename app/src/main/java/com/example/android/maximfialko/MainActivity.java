package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.example.android.maximfialko.Utils.Navigator;

public class MainActivity extends AppCompatActivity /*implements NewsListFragment.DetailFragmentListener*/ {

    private boolean isTwoPanel;
//    public Fragment mContent;

    static final String TAG_LIST_FRAGMENT = "mainList_fragment";
    static final String TAG_DETAIL_FRAGMENT = "detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        Log.d("lifecycle", "MainActivity: onCreate");
        isTwoPanel = findViewById(R.id.frame_detail) != null;
//        Log.d("TABLET MODE", isTwoPanel);
//        mContent = new Fragment();

        /*if (savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "savedNewsListFragment_Instance");
        }*/

        if (savedInstanceState == null) {
            openNewsListFragment();
            if (isTwoPanel) {
                openDetailFragment(0);
            }
        }
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

    public void openNewsListFragment() {
        NewsListFragment newsList_Fragment = NewsListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, newsList_Fragment, TAG_LIST_FRAGMENT)
                .addToBackStack(TAG_LIST_FRAGMENT)
                .commit();
    }

    public void openDetailFragment(int id) {
        NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(id);
        int frameId = isTwoPanel ? R.id.frame_detail : R.id.frame_list;

        getSupportFragmentManager().beginTransaction()
                .replace(frameId, newsDetailFragment, TAG_DETAIL_FRAGMENT)
                .addToBackStack(TAG_DETAIL_FRAGMENT)
                .commit();
    }

    public boolean getIsTwoPanel() {
        return isTwoPanel;
    }
}
