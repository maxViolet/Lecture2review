package com.example.android.maximfialko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.example.android.maximfialko.Utils.Navigator;

public class MainActivity extends AppCompatActivity implements NewsListFragment.DetailFragmentListener {

    private boolean isTwoPanel;
//    private NewsListFragment newsList_Fragment;
//    private NewsDetailFragment detailNews_fragment;

    static final String TAG_LIST_FRAGMENT = "recycler_fragment";
    static final String TAG_DETAIL_FRAGMENT = "detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Log.d("lifecycle", "main_____________onCREATE");

        isTwoPanel = findViewById(R.id.frame_detail) != null;
        Log.d("TABLET MODE", "main: " + isTwoPanel);
//        Log.d("TABLET MODE", " " + (int) (getResources().getDisplayMetrics().density * 160f));

        if (savedInstanceState == null) {
            NewsListFragment newsList_Fragment = NewsListFragment.newInstance();

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_list, newsList_Fragment, TAG_LIST_FRAGMENT)
                    .commit();

            if (isTwoPanel) {
//                int id = newsList_Fragment.getId();
                openDetailFragment(0);
//                NewsDetailFragment detailNews_fragment = NewsDetailFragment.newInstance(id);
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.frame_detail, detailNews_fragment, TAG_DETAIL_FRAGMENT)
//                        .commit();
            }
        }
    }

    @Override
    public void onStart() {
        Log.d("lifecycle", "main_____________onSTART");
        super.onStart();
//        if (isTwoPanel) {
//            int id = newsList_Fragment.getFirstItemId();
//            openDetailFragment(id);
//        }
    }

    @Override
    public void onResume() {
        Log.d("lifecycle", "main_____________onRESUME");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("lifecycle", "main_____________onPAUSE");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("lifecycle", "main_____________onSTOP");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("lifecycle", "main_____________onDESTROY");
        super.onDestroy();
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

    public boolean getIsTwoPanel() {
        return isTwoPanel;
    }

    @Override
    public void openDetailFragment(int id) {
        NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(id);
        int frameId = isTwoPanel ? R.id.frame_detail : R.id.frame_list;
        getFragmentManager().beginTransaction()
                .replace(frameId, newsDetailFragment, TAG_DETAIL_FRAGMENT)
                .addToBackStack(TAG_DETAIL_FRAGMENT)
                .commit();
    }

//    @Override
//    public  getContextFrom(Activity activity) {
//        return activity.getApplicationContext();
//    }
}
