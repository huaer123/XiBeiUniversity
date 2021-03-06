package cn.com.xibeiuniversity.xibeiuniversity.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import cn.com.xibeiuniversity.xibeiuniversity.activity.task.TaskPullDialogActivity;
import cn.com.xibeiuniversity.xibeiuniversity.activity.task.QuitPullDialogActivity;
import cn.com.xibeiuniversity.xibeiuniversity.bean.task.TaskInfoBean;

/**
 * 文件名：PullIntentUtil
 * 描    述：推送过来的信息，根据类型做跳转的工具类
 * 作    者：stt
 * 时    间：2017.3.22
 * 版    本：V1.0.7
 */

public class PullIntentUtil {
    public static void intentAvtivity(Context context, CPushMessage cPushMessage) {
        Intent intent = null;
        Bundle bundle = null;
        TaskInfoBean taskInfoList = JSON.parseObject(cPushMessage.getContent(), TaskInfoBean.class);
        if ("ForceQuit".equals(taskInfoList.getType())) {
            intent = new Intent(context, QuitPullDialogActivity.class);
        } else {
            intent = new Intent(context, TaskPullDialogActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        bundle = new Bundle();
        bundle.putSerializable("taskInfoBean", taskInfoList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
