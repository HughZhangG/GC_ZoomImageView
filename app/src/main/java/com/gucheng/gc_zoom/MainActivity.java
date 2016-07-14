package com.gucheng.gc_zoom;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.gucheng.gc_zoom.okhttp.OkHttpUtil;
import com.gucheng.gc_zoom.view.ZoomImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<DataBean.ResultsBean> mResultsBeanList;
    private ViewPager mViewPager;

//http://gank.io/api/data/福利/10/1

    private List<ImageView> mImageViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mImageViews = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.id_view_pager);


        initData();
    }

    private void initData() {
        new GetDataAsyncTask().execute("http://gank.io/api/data/福利/10/1");
    }


    private class GetDataAsyncTask extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return OkHttpUtil.get("http://gank.io/api/data/福利/10/1");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {

            if (!TextUtils.isEmpty(s)){
                Gson gson = new Gson();

                DataBean dataBean = gson.fromJson(s, DataBean.class);

                mResultsBeanList = dataBean.getResults();


                mViewPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return mResultsBeanList.size();
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {

                        ZoomImageView imageView = new ZoomImageView(MainActivity.this);

                        Picasso.with(getApplicationContext()).load(mResultsBeanList.get(position).getUrl()).into(imageView);

                        mImageViews.add(position,imageView);

                        container.addView(imageView);

                        return imageView;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        container.removeView(mImageViews.get(position));
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }
                });
            }

            super.onPostExecute(s);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageViews.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
