package com.lin.mobilesafe.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lin.mobilesafe.R;
import com.lin.mobilesafe.dao.BlackNameDao;
import com.lin.mobilesafe.domain.BlackNameBean;

import java.util.ArrayList;
import java.util.List;

public class TelSmsSafeActivity extends AppCompatActivity {

    private static final int LODDING = 1;
    private static final int FINISH = 2;
    private List<BlackNameBean> blackNameBeanList;
    private MyAdapter mAdapter;

    private ListView mListView;
    private ProgressBar pb_load;
    private TextView tv_noData;

    private BlackNameDao blackNameDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

        initEvent();
    }

    private void initEvent() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            /**
             * The view is not scrolling. Note navigating the list using the trackball counts as
             * being in the idle state since these transitions are not animated.
             */
            //public static int SCROLL_STATE_IDLE = 0;

            /**
             * The user is scrolling using touch, and their finger is still on the screen
             */
            //public static int SCROLL_STATE_TOUCH_SCROLL = 1;

            /**
             * The user had previously been scrolling using touch and had performed a fling. The
             * animation is now coasting to a stop
             */
            //public static int SCROLL_STATE_FLING = 2;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                SCROLL_STATE_FLING

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initData() {
        blackNameDao = new BlackNameDao(getApplicationContext());
        loadData();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

    }

    private void initView() {
        setContentView(R.layout.activity_tel_sms_safe);
        mListView = (ListView) findViewById(android.R.id.list);
        pb_load = (ProgressBar) findViewById(R.id.pb_safe_load);
        tv_noData = (TextView) findViewById(R.id.tv_safe_nodata);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LODDING:
                    pb_load.setVisibility(View.VISIBLE);
                    tv_noData.setVisibility(View.GONE);
                    mListView.setVisibility(View.GONE);
                    break;
                case FINISH:
                    pb_load.setVisibility(View.GONE);
                    if (blackNameBeanList.size() == 0) {
                        tv_noData.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    } else {
                        tv_noData.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
                default:
                    break;
            }

        }
    };

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = LODDING;
                handler.sendMessage(msg);
                blackNameBeanList = blackNameDao.queryDBAll();
                msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            TelSmsSafeItemView telSmsSafeItemView;
            if (convertView == null) {

                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_safe_listview_layout, null);
                telSmsSafeItemView = (TelSmsSafeItemView) convertView.findViewById(R.id.siv_item);
                convertView.setTag(telSmsSafeItemView);
            } else {
                telSmsSafeItemView = (TelSmsSafeItemView) convertView.getTag();
            }

            telSmsSafeItemView.setText(blackNameBeanList.get(position));
            telSmsSafeItemView.setDeleteOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    blackNameBeanList.remove(position);
                    notifyDataSetChanged();
                }
            });


            return convertView;
        }

        @Override
        public int getCount() {
            if (blackNameBeanList == null) {
                return 0;
            } else {
                return blackNameBeanList.size();
            }

        }

        @Override
        public Object getItem(int position) {
            if (blackNameBeanList == null) {
                return null;
            }
            if (position > blackNameBeanList.size() - 1) {
                return null;
            }

            return blackNameBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.add(0, 0, 0, "增加数据");
        menu.add(0, 1, 0, "删除数据");
        menu.add(0, 2, 0, "更新数据");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
