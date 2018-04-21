package com.sam.smartbutler.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.smartbutler.R;
import com.sam.smartbutler.entity.MyUser;
import com.sam.smartbutler.ui.CourierActivity;
import com.sam.smartbutler.ui.LoginActivity;
import com.sam.smartbutler.ui.PhoneActivity;
import com.sam.smartbutler.utils.L;
import com.sam.smartbutler.utils.ShareUtils;
import com.sam.smartbutler.utils.UtilTools;
import com.sam.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：SmartButler
 * 包名：  com.sam.smartbutler.fragment
 * 文件名：ButlerFragment
 * 创建者：Sam
 * 创建时间：2017/11/13 17:28
 * 描述：个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {

    //个人资料控件
    private EditText et_nickName, et_sex, et_age, et_desc;
    //退出登录，提交资料按钮
    private Button bt_exit, bt_confirm;
    //编辑资料按钮
    private TextView tv_edit;
    //物流查询
    private TextView courier;

    //圆形头像
    CircleImageView profile_image;
    //点击头像后的弹窗
    private CustomDialog dialog;

    //dialog控件
    private Button mCamera, mCancel, mPicture;

    //归属地查询
    TextView tv_phone;

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    File tempFile = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        courier = (TextView) view.findViewById(R.id.courier_user);
        et_nickName = (EditText) view.findViewById(R.id.nickName_user_et);
        et_sex = (EditText) view.findViewById(R.id.sex_user_et);
        et_age = (EditText) view.findViewById(R.id.age_user_et);
        et_desc = (EditText) view.findViewById(R.id.desc_user_et);
        tv_edit = (TextView) view.findViewById(R.id.edit_user_tv);
        bt_exit = (Button) view.findViewById(R.id.exit_user_bt);
        bt_confirm = (Button) view.findViewById(R.id.confirm_user_bt);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0,
                R.layout.dialog_photo,R.style.Theme_dialog,Gravity.BOTTOM, R.style.pop_anim_style);
        mCamera = (Button) dialog.findViewById(R.id.camera_btn);
        mPicture = (Button) dialog.findViewById(R.id.picture_btn);
        mCancel = (Button) dialog.findViewById(R.id.cancel_btn);
        dialog.setCancelable(false);


        bt_exit.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
        courier.setOnClickListener(this);

        mCamera.setOnClickListener(this);
        mPicture.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        tv_phone.setOnClickListener(this);

        //设置编辑框焦点
        setEnable(false);

        //设置编辑框具体值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_nickName.setText(userInfo.getNickName());
        et_sex.setText(userInfo.isSex() ? "男" : "女");
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
        ShareUtils.putString(getActivity(), "nick_name", userInfo.getNickName());

        //从shareutils读取图片
        UtilTools.getImageToShare(getActivity(), profile_image);

    }

    private void setEnable(boolean is) {
        et_nickName.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit_user_bt:
                MyUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user_tv:
                //编辑数据
                bt_confirm.setVisibility(View.VISIBLE);
                setEnable(true);
                break;
            case R.id.confirm_user_bt:
                //提交数据
                confirm();
                break;
            case R.id.profile_image:
                //设置头像
                dialog.show();
                break;
            case R.id.cancel_btn:
                //dialog里面的取消按钮
                dialog.dismiss();
                break;
            case R.id.camera_btn:
                //dialog里面的拍照按钮
                toCamera();
                break;
            case R.id.picture_btn:
                //dialog里面的相片按钮
                toPicture();
                break;
            case R.id.courier_user:
                //物流查询按钮
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                //归属地查询按钮
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;
        }
    }

    //跳转到图库
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转到相机
    private void toCamera() {
        //android6.0动态权限申请
        dynamic();
    }

    //android6.0以上动态获取权限
    private void dynamic() {
        if (Build.VERSION.SDK_INT>22){
            if (ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                //先判断有没有权限，没有就在这里声明
                ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CAMERA},1015);
                L.i("获取权限");
            }else {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //判断内存卡是否可用，可用的话就进行储存
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME
                )));
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }else {
            //这个说明系统在6.0以下，不需要动态获取权限
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断内存卡是否可用，可用的话就进行储存
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                    Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME
            )));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
        dialog.dismiss();
    }

    //获取权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1015:
                L.i("0:"+grantResults[0]);
                L.i("length:"+grantResults.length);
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //判断内存卡是否可用，可用的话就进行储存
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                            Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME
                    )));
                    L.i("跳转相机");
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }else{
                    Toast.makeText(getActivity(), "请手动打开权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //判断data是否为空，用户有可能点击了取消
                    if (data != null) {
                        setImageToView(data);
                        //既然已经设置了图片，原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //设置圆形头像
    private void setImageToView(Intent intent) {
        Bundle bundle = intent.getExtras();
        Bitmap bitmap = bundle.getParcelable("data");
        profile_image.setImageBitmap(bitmap);
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置数据和类型
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //修改资料
    private void confirm() {
        final String nickName = et_nickName.getText().toString();
        String sex = et_sex.getText().toString();
        String age = et_age.getText().toString();
        String desc = et_desc.getText().toString();
        if (!TextUtils.isEmpty(nickName) & !TextUtils.isEmpty(sex) & !TextUtils.isEmpty(age)) {
            MyUser newUser = new MyUser();
            newUser.setNickName(nickName);
            newUser.setAge(Integer.parseInt(age));

            //判断性别
            if (sex.equals("男")) {
                newUser.setSex(true);
            } else {
                newUser.setSex(false);
            }

            //判断简介是否为空
            if (!TextUtils.isEmpty(desc)) {
                newUser.setDesc(desc);
            } else {
                newUser.setDesc("这个人很懒，什么都没有留下");
            }

            BmobUser bmobUser = BmobUser.getCurrentUser();

            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        setEnable(false);
                        bt_confirm.setVisibility(View.GONE);
                        ShareUtils.putString(getActivity(), "nick_name", nickName);
                        Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //存图片到shareUtils里面
        UtilTools.putImageToShare(getActivity(), profile_image);
    }
}
