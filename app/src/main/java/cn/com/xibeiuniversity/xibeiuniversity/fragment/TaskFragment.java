package cn.com.xibeiuniversity.xibeiuniversity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xibeiuniversity.xibeiuniversity.R;
import cn.com.xibeiuniversity.xibeiuniversity.activity.task.TaskSearchActivity;
import cn.com.xibeiuniversity.xibeiuniversity.adapter.task.TaskAdapter;
import cn.com.xibeiuniversity.xibeiuniversity.adapter.task.TaskStateAdapter;
import cn.com.xibeiuniversity.xibeiuniversity.base.BaseFragment;
import cn.com.xibeiuniversity.xibeiuniversity.bean.task.TaskBean;
import cn.com.xibeiuniversity.xibeiuniversity.config.UrlConfig;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.TaskListInterface;
import cn.com.xibeiuniversity.xibeiuniversity.utils.MyRequest;
import cn.com.xibeiuniversity.xibeiuniversity.utils.MyUtils;
import cn.com.xibeiuniversity.xibeiuniversity.utils.SharedUtil;
import cn.com.xibeiuniversity.xibeiuniversity.utils.ToastUtil;
import cn.com.xibeiuniversity.xibeiuniversity.weight.HorizontalListView;

/**
 * 文件名：TaskFragment
 * 描    述：任务界面
 * 作    者：stt
 * 时    间：2016.12.30
 * 版    本：V1.0.0
 */

public class TaskFragment extends BaseFragment implements View.OnClickListener, TaskListInterface {
    private Context context;
    private TextView titleName;
    private LinearLayout searchLayout;
    //刷新控件
    private PullToRefreshListView mPullRefreshListView;
    private TaskAdapter taskAdapter;
    private HorizontalListView horizontalListView;
    private TaskStateAdapter taskStateAdapter;
    private int state = 0;//状态
    private int pageindex = 1;//页码数
    private List<TaskBean.RowsBean> rowsBeanList = new ArrayList();

    @Override
    protected View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_task, null);
        return view;
    }

    @Override
    protected void setDate() {
        requestData(pageindex, state);
    }


    @Override
    protected void init(View rootView) {
        titleName = (TextView) rootView.findViewById(R.id.title_name);
        titleName.setText("任务");
        searchLayout = (LinearLayout) rootView.findViewById(R.id.title_search);
        searchLayout.setVisibility(View.INVISIBLE);
        searchLayout.setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.task_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        horizontalListView = (HorizontalListView) rootView.findViewById(R.id.task_horizontalListView);
        taskStateAdapter = new TaskStateAdapter(context, getTypeData());
        horizontalListView.setAdapter(taskStateAdapter);
        taskStateAdapter.setSelectIndex(0);
        taskStateAdapter.notifyDataSetChanged();
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state = position;
                pageindex = 1;
                requestData(pageindex, position);
                taskStateAdapter.setSelectIndex(position);
                taskStateAdapter.notifyDataSetChanged();
            }
        });
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh");
                // 这里写下拉刷新的任务
                pageindex = 1;
                requestData(pageindex, state);
                taskAdapter.notifyDataSetChanged();
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh");
                // 这里写上拉加载更多的任务
                pageindex++;
                requestData(pageindex, state);
                taskAdapter.notifyDataSetChanged();
                mPullRefreshListView.onRefreshComplete();
            }
        });

    }

    private void requestData(int pageindex, int state) {
        MyRequest.tasIssuedListRequest(context, this, pageindex, "2", state);
    }

    private List<String> getTypeData() {
        List<String> data = new ArrayList<String>();
        data.add("全部");
        data.add("未处理");
        data.add("处理中");
        data.add("已完成");
        data.add("未完成");
        return data;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_search:
                Intent intent = new Intent(getActivity(), TaskSearchActivity.class);
                startActivityForResult(intent, 200);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 || requestCode == getActivity().RESULT_OK) {
//            ToastUtil.show(context, data.getStringExtra("username"));
        }
    }

    @Override
    public void showTaskList(TaskBean taskBean) {
        if (pageindex == 1) {
            rowsBeanList = taskBean.getRows();
            taskAdapter = new TaskAdapter(context, rowsBeanList);
            mPullRefreshListView.setAdapter(taskAdapter);
        } else if (pageindex > 1 && taskBean.getRows().size() != 0) {
            rowsBeanList.addAll(taskBean.getRows());
            taskAdapter = new TaskAdapter(context, rowsBeanList);
            mPullRefreshListView.setAdapter(taskAdapter);
        } else if (pageindex > 1 && taskBean.getRows().size() == 0) {
            ToastUtil.show(context, "没有更多数据了");
        }
    }
}