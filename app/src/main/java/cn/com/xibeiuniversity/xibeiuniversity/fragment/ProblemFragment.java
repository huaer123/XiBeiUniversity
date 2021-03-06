package cn.com.xibeiuniversity.xibeiuniversity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.xibeiuniversity.xibeiuniversity.R;
import cn.com.xibeiuniversity.xibeiuniversity.activity.problem.AddProblemActivity;
import cn.com.xibeiuniversity.xibeiuniversity.adapter.problem.ProblemAdapter;
import cn.com.xibeiuniversity.xibeiuniversity.base.BaseFragment;
import cn.com.xibeiuniversity.xibeiuniversity.bean.problem.ProblemBean;
import cn.com.xibeiuniversity.xibeiuniversity.bean.problem.ProblemTypeLeft;
import cn.com.xibeiuniversity.xibeiuniversity.bean.problem.ProblemTypeRight;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.ProblemListInterface;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.ProblemTypeInterface;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.ProblemTypeLeftInterface;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.ProblemTypeRightInterface;
import cn.com.xibeiuniversity.xibeiuniversity.utils.MyRequest;
import cn.com.xibeiuniversity.xibeiuniversity.utils.PopWindowUtils;
import cn.com.xibeiuniversity.xibeiuniversity.utils.ToastUtil;


/**
 * 文件名：ProblemFragment
 * 描    述：问题上报界面
 * 作    者：stt
 * 时    间：2016.12.30
 * 版    本：V1.0.0
 */

public class ProblemFragment extends BaseFragment implements View.OnClickListener, ProblemTypeInterface,
        ProblemListInterface, ProblemTypeLeftInterface, ProblemTypeRightInterface {
    private Context context;
    private TextView titleName;//标题名称
    //刷新控件
    private PullToRefreshListView mPullRefreshListView;
    private ProblemBean problemBean = new ProblemBean();
    private ProblemAdapter problemAdapter;
    private LinearLayout addProblem;
    /**
     * pop的类型，时间，状态
     */
    private LinearLayout typeLayout, timeLayout, stateLayout;
    private TextView typeText, timeText, stateText;
    private ImageView typeImage, timeImage, stateImage;
    private PopupWindow popupWindow;
    private int state = 0;//状态
    private int pageindex = 1;//页码数
    private int searchState = 0;//状态
    private String searchProblemType = "";//类型
    private int searchDate = 0;//时间
    private List<ProblemBean.RowsBean> rowsBeanList = new ArrayList();
    private TextView[] textViewsTit;
    private ImageView[] imageViewTit;
    private RelativeLayout nothing;
    private int ViewHight = 0;


    @Override
    protected View setView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();
        View view = inflater.inflate(R.layout.fragment_problem, null);
        return view;
    }

    @Override
    protected void setDate() {
        requestData(pageindex, searchState, searchProblemType, searchDate);
    }

    private void requestData(int pageindex, int searchState, String searchProblemType, int searchDate) {
        MyRequest.problemListRequest(context, this, pageindex, searchState, searchProblemType, searchDate);
    }

    @Override
    protected void init(View rootView) {
        titleName = (TextView) rootView.findViewById(R.id.title_name);
        titleName.setText("问题上报");
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.problem_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        addProblem = (LinearLayout) rootView.findViewById(R.id.problem_addInfo);
        addProblem.setVisibility(View.VISIBLE);
        addProblem.setOnClickListener(this);

        typeLayout = (LinearLayout) rootView.findViewById(R.id.problem_layout_type);
        timeLayout = (LinearLayout) rootView.findViewById(R.id.problem_layout_time);
        stateLayout = (LinearLayout) rootView.findViewById(R.id.problem_layout_state);
        typeText = (TextView) rootView.findViewById(R.id.problem_layout_type_text);
        timeText = (TextView) rootView.findViewById(R.id.problem_layout_time_text);
        stateText = (TextView) rootView.findViewById(R.id.problem_layout_state_text);
        typeLayout.setOnClickListener(this);
        timeLayout.setOnClickListener(this);
        stateLayout.setOnClickListener(this);
        textViewsTit = new TextView[]{typeText, timeText, stateText};
        typeImage = (ImageView) rootView.findViewById(R.id.problem_layout_type_image);
        timeImage = (ImageView) rootView.findViewById(R.id.problem_layout_time_image);
        stateImage = (ImageView) rootView.findViewById(R.id.problem_layout_state_image);
        nothing = (RelativeLayout) rootView.findViewById(R.id.problem_nothing);
        imageViewTit = new ImageView[]{typeImage, timeImage, stateImage};
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullDownToRefresh");
                // 这里写下拉刷新的任务
                pageindex = 1;
                requestData(pageindex, searchState, searchProblemType, searchDate);
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                Log.e("TAG", "onPullUpToRefresh");
                // 这里写上拉加载更多的任务
                pageindex++;
                requestData(pageindex, searchState, searchProblemType, searchDate);
                mPullRefreshListView.onRefreshComplete();
            }
        });
        final RelativeLayout titleLayout = (RelativeLayout) rootView.findViewById(R.id.title_layout);
        final LinearLayout problemTitleLayout = (LinearLayout) rootView.findViewById(R.id.problem_title_layout);
        ViewTreeObserver titleLayoutVTO = titleLayout.getViewTreeObserver();
        titleLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewHight = titleLayout.getHeight();
                Log.i("ViewHight", ViewHight + "");
            }
        });
        ViewTreeObserver problemTitleLayoutVTO = problemTitleLayout.getViewTreeObserver();
        problemTitleLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewHight = problemTitleLayout.getHeight() * 2 + ViewHight;
            }
        });
    }

    @Override
    public void onClick(View v) {
        List list;
        switch (v.getId()) {
            case R.id.problem_addInfo:
                Intent intent = new Intent(context, AddProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.problem_layout_type:
//                list = new ArrayList();
//                list.add("全部");
//                list.add("部件问题");
//                list.add("事件问题");
//                popupWindow = PopWindowUtils.showProblemPop(getActivity(), this, v, list, 0,ViewHight);
                  MyRequest.getProblemTypeLeft(getActivity(), this);
                  setTextViewColor(typeText);
                break;
            case R.id.problem_layout_time:
                list = new ArrayList();
                list.add("全部");
                list.add("三天");
                list.add("一周");
                list.add("一个月");
                popupWindow = PopWindowUtils.showProblemPop(getActivity(), this, v, list, 1, ViewHight);
                setTextViewColor(timeText);
                break;
            case R.id.problem_layout_state:
                list = new ArrayList();
                list.add("全部");
                list.add("已上报");
                list.add("已回复");
                popupWindow = PopWindowUtils.showProblemPop(getActivity(), this, v, list, 2, ViewHight);
                setTextViewColor(stateText);
                break;
        }
    }

    private void setTextViewColor(TextView textView) {
        if (textView != null) {
            for (int i = 0; i < textViewsTit.length; i++) {
                if (textView.getId() == textViewsTit[i].getId()) {
//                    imageViewTit[i].setImageResource(R.mipmap.search_top);
                    textViewsTit[i].setTextColor(ContextCompat.getColor(context, R.color.blue));
                } else {
//                    imageViewTit[i].setImageResource(R.mipmap.search_bottom);
                    textViewsTit[i].setTextColor(ContextCompat.getColor(context, R.color.black));
                }
            }
        } else {
            for (int i = 0; i < textViewsTit.length; i++) {
//                imageViewTit[i].setImageResource(R.mipmap.search_bottom);
                textViewsTit[i].setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }
    }

    @Override
    public void getProblemType(int type, String typeName) {
        switch (type) {
            case 0:
                searchProblemType = "";
                typeText.setText(typeName);
                setTextViewColor(null);
                break;
            case 1:
                searchDate = "全部".equals(typeName) ? 0 : "三天".equals(typeName) ? 1 : "一周".equals(typeName) ? 2 : "一个月".equals(typeName) ? 3 : 0;
                timeText.setText("时间("+typeName+")");
                setTextViewColor(null);
                break;
            case 2:
                searchState = "全部".equals(typeName) ? 0 : "已上报".equals(typeName) ? 1 : "已回复".equals(typeName) ? 2 : 0;
                stateText.setText("状态("+typeName+")");
                setTextViewColor(null);
                break;
        }
        pageindex = 1;
        requestData(pageindex, searchState, searchProblemType, searchDate);
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    @Override
    public void showTaskList(ProblemBean problemBean) {
        if (pageindex == 1 && problemBean.getRows().size() == 0) {
            mPullRefreshListView.setVisibility(View.GONE);
            nothing.setVisibility(View.VISIBLE);
        } else {
            mPullRefreshListView.setVisibility(View.VISIBLE);
            nothing.setVisibility(View.GONE);
            if (pageindex == 1) {
                rowsBeanList = problemBean.getRows();
                problemAdapter = new ProblemAdapter(context, rowsBeanList);
                mPullRefreshListView.setAdapter(problemAdapter);
                problemAdapter.notifyDataSetChanged();
            } else if (pageindex > 1 && problemBean.getRows().size() != 0) {
                rowsBeanList.addAll(problemBean.getRows());
                problemAdapter = new ProblemAdapter(context, rowsBeanList);
                mPullRefreshListView.setAdapter(problemAdapter);
                problemAdapter.notifyDataSetChanged();
            } else if (pageindex > 1 && problemBean.getRows().size() == 0) {
                ToastUtil.show(context, "没有更多数据了");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData(pageindex, searchState, searchProblemType, searchDate);
    }

    @Override
    public void getTypeLeft(List<ProblemTypeLeft> problemTypeLeftList) {
        popupWindow = PopWindowUtils.showProblemTypePop(context, this, this, typeLayout, ViewHight, (ArrayList<ProblemTypeLeft>) problemTypeLeftList);
    }

    @Override
    public void getTypeRight(ProblemTypeRight problemTypeRight) {
        searchProblemType = problemTypeRight.getCode();
        typeText.setText("类型("+problemTypeRight.getName()+")");
        setTextViewColor(null);
        pageindex = 1;
        requestData(pageindex, searchState, searchProblemType, searchDate);
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
