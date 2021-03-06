package cn.com.xibeiuniversity.xibeiuniversity.activity.task;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.xibeiuniversity.xibeiuniversity.R;
import cn.com.xibeiuniversity.xibeiuniversity.activity.DetailImageActivity;
import cn.com.xibeiuniversity.xibeiuniversity.activity.InputTextActivity;
import cn.com.xibeiuniversity.xibeiuniversity.adapter.task.TaskDetalPhotoAdapter;
import cn.com.xibeiuniversity.xibeiuniversity.bean.task.TaskChoosePersonBean;
import cn.com.xibeiuniversity.xibeiuniversity.function.takephoto.app.TakePhotoActivity;
import cn.com.xibeiuniversity.xibeiuniversity.function.takephoto.compress.CompressConfig;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.GetPhotoTypeInterface;
import cn.com.xibeiuniversity.xibeiuniversity.interfaces.TaskTypeValuesInterface;
import cn.com.xibeiuniversity.xibeiuniversity.utils.DateTimePickDialogUtil;
import cn.com.xibeiuniversity.xibeiuniversity.utils.DialogUtils;
import cn.com.xibeiuniversity.xibeiuniversity.utils.MyRequest;
import cn.com.xibeiuniversity.xibeiuniversity.utils.ToastUtil;

/**
 * 文件名：AddTaskActivity
 * 描    述：添加任务的类
 * 作    者：stt
 * 时    间：2017.3.17
 * 版    本：V1.0.6
 */

public class AddTaskActivity extends TakePhotoActivity implements View.OnClickListener, TaskTypeValuesInterface, GetPhotoTypeInterface, AdapterView.OnItemClickListener {
    private TextView titleName, textName;
    private LinearLayout back;
    private EditText addressEdit, infoEdit;
    private RelativeLayout typeLayout, priorityLayout, startTimeLayout, endTimeLayout, personLayout, taskNameLayout;
    private ImageView takePhoto;
    private TextView typeText, priorityText, startTimeText, endTimeText, personText;
    private Dialog showPhotoDialog;
    private ArrayList<String> listPath = new ArrayList<String>();
    private ArrayList<Bitmap> list = new ArrayList<Bitmap>();
    private List<File> listFile = new ArrayList<>();
    private Map<String, File> fileMap = new HashMap<>();
    private File mCameraFile;
    private GridView gridView;
    private TaskDetalPhotoAdapter taskDetalPhotoAdapter;
    private Button sendInfoBtn;
    private String inputName, inputTypeName, inputAddress, inputPriorityName, inputStartTime, inputEndTime, inputPerson, inputInfo;
    private String inputPersonId = "", inputType = "", inputPriority = "";

    @Override
    protected void setView() {
        setContentView(R.layout.activity_add_task);
    }

    @Override
    protected void setDate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            list = savedInstanceState.getParcelableArrayList("listBitmap");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listBitmap", list);
    }

    @Override
    protected void init() {
        titleName = (TextView) findViewById(R.id.title_name);
        titleName.setText("添加任务");
        back = (LinearLayout) findViewById(R.id.title_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        textName = (TextView) findViewById(R.id.add_task_name);
        addressEdit = (EditText) findViewById(R.id.add_task_address);
        infoEdit = (EditText) findViewById(R.id.add_task_inputInfo);
        taskNameLayout = (RelativeLayout) findViewById(R.id.task_name_layout);
        taskNameLayout.setOnClickListener(this);
        typeLayout = (RelativeLayout) findViewById(R.id.add_task_typeLayout);
        priorityLayout = (RelativeLayout) findViewById(R.id.add_task_priorityLayout);
        startTimeLayout = (RelativeLayout) findViewById(R.id.add_task_startTimeLayout);
        endTimeLayout = (RelativeLayout) findViewById(R.id.add_task_endTimeLayout);
        personLayout = (RelativeLayout) findViewById(R.id.add_task_personLayout);
        personText = (TextView) findViewById(R.id.add_task_person);
        takePhoto = (ImageView) findViewById(R.id.add_task_takePhoto);
        gridView = (GridView) findViewById(R.id.add_task_gridView);
        typeLayout.setOnClickListener(this);
        priorityLayout.setOnClickListener(this);
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        personLayout.setOnClickListener(this);

        typeText = (TextView) findViewById(R.id.add_task_type);
        priorityText = (TextView) findViewById(R.id.add_task_priority);
        startTimeText = (TextView) findViewById(R.id.add_task_startTime);
        endTimeText = (TextView) findViewById(R.id.add_task_endTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        startTimeText.setText(df.format(new Date()));
        endTimeText.setText(df.format(new Date()));

        gridView = (GridView) findViewById(R.id.add_task_gridView);
        gridView.setOnItemClickListener(this);
        taskDetalPhotoAdapter = new TaskDetalPhotoAdapter(this, list);
        gridView.setAdapter(taskDetalPhotoAdapter);
        taskDetalPhotoAdapter.setList(list);
        sendInfoBtn = (Button) findViewById(R.id.add_task_button);
        sendInfoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                XiBeiApp.finishTop();
                break;
            case R.id.add_task_typeLayout://任务类型
                MyRequest.getTaskTypeRequest(AddTaskActivity.this, typeLayout, 0);
                break;
            case R.id.add_task_priorityLayout://优先级
                MyRequest.getPriorityRequest(AddTaskActivity.this, typeLayout, 1);
                break;
            case R.id.add_task_startTimeLayout://开始时间
                DateTimePickDialogUtil startPickDialog = new DateTimePickDialogUtil(AddTaskActivity.this, "");
                startPickDialog.dateTimePicKDialog(startTimeText);
                break;
            case R.id.add_task_endTimeLayout://结束时间
                DateTimePickDialogUtil endPickDialog = new DateTimePickDialogUtil(AddTaskActivity.this, "");
                endPickDialog.dateTimePicKDialog(endTimeText);
                break;
            case R.id.add_task_takePhoto://拍照
                showPhotoDialog = DialogUtils.showPhotoDialog(this);
                break;
            case R.id.add_task_personLayout://人员下发
                startActivityForResult(new Intent(this, TaskChoosePersonActivity.class), 11);
                break;
            case R.id.task_name_layout: //任务名称
                Intent intent = new Intent(AddTaskActivity.this, InputTextActivity.class);
                inputName = textName.getText().toString().trim();
                if (!TextUtils.isEmpty(inputName)) {
                    intent.putExtra("value", inputName);
                }
                startActivityForResult(intent, 1);
                break;
            case R.id.add_task_button:
                inputTypeName = typeText.getText().toString().trim();
                inputAddress = addressEdit.getText().toString().trim();
                inputPriorityName = priorityText.getText().toString().trim();
                inputStartTime = startTimeText.getText().toString().trim();
                inputEndTime = endTimeText.getText().toString().trim();
                inputPerson = personText.getText().toString().trim();
                inputInfo = infoEdit.getText().toString().trim();
                if (isEmpty()) {
                    inputPersonId = inputPersonId.substring(0, inputPersonId.length() - 1);
//                    Log.i("inputPersonId", "名称:" + inputName + ",类型:" + inputType + ",地点:" + inputAddress + ",优先级:" + inputPriority + ",开始时间:" + inputStartTime + ",结束时间:" + inputEndTime + ",下发人员:" + inputPersonId + ",内容:" + inputInfo + ",图片:" + fileMap.get("AddImage1").getPath());
                    MyRequest.addTaskRequests(this, fileMap, inputName, inputType, inputAddress, inputPriority, inputStartTime, inputEndTime, inputPersonId, inputInfo);//不管有没有图片
                }
                break;
        }
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(inputName)) {
            ToastUtil.show(this, "请输入任务名称");
            return false;
        }
        if (TextUtils.isEmpty(inputTypeName)) {
            ToastUtil.show(this, "请选择任务类型");
            return false;
        }
        if (TextUtils.isEmpty(inputAddress)) {
            ToastUtil.show(this, "请输入任务地点");
            return false;
        }
        if (TextUtils.isEmpty(inputPriorityName)) {
            ToastUtil.show(this, "请选择优先级");
            return false;
        }
        if (TextUtils.isEmpty(inputStartTime)) {
            ToastUtil.show(this, "请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(inputEndTime)) {
            ToastUtil.show(this, "请选择结束时间");
            return false;
        }
        if (TextUtils.isEmpty(inputPerson)) {
            ToastUtil.show(this, "请选择下发人员");
            return false;
        }
        if (TextUtils.isEmpty(inputInfo)) {
            ToastUtil.show(this, "请输入任务描述");
            return false;
        }
        return true;
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(String msg) {
        super.takeFail(msg);
    }

    int imageIndex = 0;

    @Override
    public void takeSuccess(String imagePath) {
        super.takeSuccess(imagePath);
        imageIndex++;
        Log.i("imagePaths", imagePath + "======" + imageIndex);
        listPath.add(imagePath);
        mCameraFile = new File(imagePath);
        fileMap.put(imagePath, mCameraFile);
        listFile.add(mCameraFile);
        showImg(imagePath);
    }

    private void showImg(String imagePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);
        list.add(bitmap);
        taskDetalPhotoAdapter.setList(list);
        if (list.size() == 5) {
            takePhoto.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void getTaskType(String name, String values, int index) {
        if (index == 0) {//任务类型
            typeText.setText(name);
            inputType = values;
        }
        if (index == 1) {//优先级
            priorityText.setText(name);
            inputPriority = values;
        }
    }

    @Override
    public void getPhotoType(int type) {
        File file = new File(Environment.getExternalStorageDirectory(), "/XiBeiTask/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(700).create();//压缩方法实例化就是压缩图片，根据配置参数压缩
        switch (type) {
            case 0://相册获取图片
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromGallery();
                if (showPhotoDialog.isShowing()) {
                    showPhotoDialog.dismiss();
                }
                break;
            case 1://相机获取图片
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromCapture(imageUri);
                if (showPhotoDialog.isShowing()) {
                    showPhotoDialog.dismiss();
                }
                break;
            case 2://文件获取图片
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromDocuments();
                if (showPhotoDialog.isShowing()) {
                    showPhotoDialog.dismiss();
                }
                break;
            case 3://取消
                if (showPhotoDialog.isShowing()) {
                    showPhotoDialog.dismiss();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent in = new Intent(this, DetailImageActivity.class);
        in.putStringArrayListExtra("listPath", listPath);
        startActivity(in);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    inputName = data.getStringExtra("return_value");
                    textName.setText(inputName);
                }
                if (resultCode == RESULT_CANCELED) {
                    inputName = data.getStringExtra("return_value");
                    textName.setText(inputName);
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    List<TaskChoosePersonBean> list = (List<TaskChoosePersonBean>) data.getSerializableExtra("data");
                    String personName = "";
                    inputPersonId = "";
                    if (list.size() != 0) {
                        for (TaskChoosePersonBean bean : list) {
                            personName = personName + bean.getName() + ",";
                            inputPersonId = inputPersonId + bean.getId() + ",";
                        }
                    }
                    personText.setText(TextUtils.isEmpty(personName) ? "" : personName.substring(0, personName.length() - 1));
                }
                break;
        }
    }
}
