package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BasePhotoCropActivity;
import com.yuedong.youbutie_merchant_android.model.Callback;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.listener.UploadListener;
import com.yuedong.youbutie_merchant_android.utils.ApiUtils;
import com.yuedong.youbutie_merchant_android.utils.CropHelper;
import com.yuedong.youbutie_merchant_android.utils.CropParams;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.ImageZoomUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.view.SelectPicPop;
import com.yuedong.youbutie_merchant_android.view.TimeSelectPop;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class EditMerchantActivity extends BasePhotoCropActivity implements View.OnClickListener {
    private ImageView merchantPic;
    private TextView merchantName, locationText, businessStartTime, businessEndTime, telText, startTimeText, endTimeText;
    private Merchant merchant;
    private int textColor = Color.parseColor("#82706e");
    private SelectPicPop selectPicPop;
    private CropParams mCropParams = new CropParams();
    private TimeSelectPop startTimeSelectPop, endTimeSelectPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("编辑门店"), false, false, false, R.layout.activity_edit_merchant);
    }

    @Override
    protected void initViews() {
        startTimeSelectPop = new TimeSelectPop(context);
        endTimeSelectPop = new TimeSelectPop(context);
        startTimeSelectPop.setSelectTimeDesc(getString(R.string.str_select_start_time));
        endTimeSelectPop.setSelectTimeDesc(getString(R.string.str_select_end_time));

        loadDialog.setMessage("上传信息中");
        selectPicPop = new SelectPicPop(context);
        startTimeText = fvById(R.id.id_select_start_time);
        endTimeText = fvById(R.id.id_select_end_time);
        merchantName = fvById(R.id.id_merchant_name);
        locationText = fvById(R.id.id_merchant_location);
        businessStartTime = fvById(R.id.id_select_start_time);
        businessEndTime = fvById(R.id.id_select_end_time);
        telText = fvById(R.id.id_merchant_tel);
        merchantPic = fvById(R.id.id_merchant_pic);
    }

    @Override
    protected void initEvents() {
        startTimeSelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackHM(final String hour, final String minute) {
                String fullStartTime = DateUtils.getCurYMD();
                // yyyy-MM-dd HH:mm:ss
                fullStartTime = fullStartTime + " " + hour + ":" + minute + ":00";
                L.d("fullStartTime" + fullStartTime);

                // 更新门店开始时间
                dialogStatus(true);
                Merchant updateMerchant = new Merchant();
                updateMerchant.setStartTime(new BmobDate(new Date(BmobDate.getTimeStamp(fullStartTime))));
                updateMerchant.update(context, merchant.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dialogStatus(false);
                        App.getInstance().meMerchantInfoChange = true;
                        startTimeText.setText(hour + ":" + minute);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        error(s);
                        dialogStatus(false);
                    }
                });
            }
        });
        endTimeSelectPop.setOnCallbcak(new Callback() {
            @Override
            public void callbackHM(final String hour, final String minute) {
                String fullEndTime = DateUtils.getCurYMD();
                fullEndTime = fullEndTime + " " + hour + ":" + minute + ":00";
                dialogStatus(true);

                // 更新门店结束时间
                Merchant updateMerchant = new Merchant();
                updateMerchant.setEndTime(new BmobDate(new Date(BmobDate.getTimeStamp(fullEndTime))));
                updateMerchant.update(context, merchant.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dialogStatus(false);
                        App.getInstance().meMerchantInfoChange = true;
                        endTimeText.setText(hour + ":" + minute);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        dialogStatus(false);
                        error(s);
                    }
                });


                L.d("fullEndTime" + fullEndTime);
            }
        });
        selectPicPop.setOnSelectPicPopCallback(new SelectPicPop.OnSelectPicPopCallback() {
            @Override
            public void onTakePic() {
                mCropParams.updateUri();
                Intent intent = CropHelper.buildCaptureIntent(mCropParams.uri);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
            }

            @Override
            public void onGetPic() {
                mCropParams.updateUri();
                startActivityForResult(CropHelper.buildCropFromGalleryIntent(mCropParams), CropHelper.REQUEST_CROP);
            }
        });
        fvById(R.id.id_edit_merchant_name_layout).setOnClickListener(this);
        fvById(R.id.id_edit_merchant_location_layout).setOnClickListener(this);
        fvById(R.id.id_edit_merchant_tel_layout).setOnClickListener(this);
        fvById(R.id.id_select_end_time_layout).setOnClickListener(this);
        fvById(R.id.id_select_start_time_layout).setOnClickListener(this);
        merchantPic.setOnClickListener(this);
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        final String picPath = uri.getPath();
        // 上传头像到服务器
        ApiUtils.uplodaFile(context, picPath, new UploadListener() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(String s, String s1, BmobFile bmobFile) {
                L.i("上传文件成功：url=" + bmobFile.getUrl());
                if (StringUtil.isEmpty(bmobFile.getUrl())) {
                    T.showShort(context, "上传文件返回url为null");
                    return;
                }
                Merchant updateMerchant = new Merchant();
                updateMerchant.setPhoto(bmobFile.getUrl());
                updateMerchant.update(context, merchant.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        dialogStatus(false);
                        App.getInstance().meMerchantInfoChange = true;
                        Bitmap bm = ImageZoomUtils.decodeSampleBitmapFromPath(picPath, 300, 200);
                        merchantPic.setImageBitmap(bm);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        error(s);
                        dialogStatus(false);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                error(s);
                dialogStatus(false);

            }
        });
    }

    @Override
    protected void ui() {
        merchant = (Merchant) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        if (merchant.getPhoto() != null) {
            DisplayImageByVolleyUtils.loadImage(merchant.getPhoto(), merchantPic);
        }

        //  营业开始时间
        BmobDate startTime = merchant.getStartTime();
        if (startTime != null) {
            long startTimeL = BmobDate.getTimeStamp(startTime.getDate());
            String startTimeStr = DateUtils.formatDate(new Date(startTimeL), "HH:mm");
            businessStartTime.setTextColor(textColor);
            businessStartTime.setText(startTimeStr);
        }

        // 营业结束时间
        BmobDate endTime = merchant.getEndTime();
        if (endTime != null) {
            long endTimeL = BmobDate.getTimeStamp(endTime.getDate());
            String endTimeStr = DateUtils.formatDate(new Date(endTimeL), "HH:mm");
            businessEndTime.setTextColor(textColor);
            businessEndTime.setText(endTimeStr);
        }
        setInfo(merchantName, textColor, merchant.getName());
        setInfo(locationText, textColor, merchant.getAddress());
        setInfo(telText, textColor, merchant.getTelephone());

    }

    private void setInfo(TextView textView, int color, String text) {
        if (StringUtil.isNotEmpty(text)) {
            textView.setTextColor(color);
            textView.setText(text);
        }
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_select_start_time_layout:
                startTimeSelectPop.show();
                break;

            case R.id.id_select_end_time_layout:
                endTimeSelectPop.show();
                break;
            case R.id.id_merchant_pic:
                selectPicPop.show();
                break;

            case R.id.id_edit_merchant_name_layout:
                editMerchantInfo("门店名字", InfoEditActivity.ACTION_INPUT_MEMBER_NAME, Constants.REQUESTCODE_MERCHANT_NAME, merchantName.getText().toString());

                break;

            case R.id.id_edit_merchant_location_layout:
                editMerchantInfo("门店位置", InfoEditActivity.ACTION_INPUT_MEMBER_LOCATION, Constants.REQUESTCODE_MERCHANT_LOCATION, locationText.getText().toString());
                break;

            case R.id.id_edit_merchant_tel_layout:
                editMerchantInfo("门店电话", InfoEditActivity.ACTION_INPUT_MEMBER_TEL, Constants.REQUESTCODE_MERCHANT_TEL, telText.getText().toString());
                break;


        }
    }

    private void editMerchantInfo(String title, int action, int requestCode, String content) {
        Intent intent = new Intent(activity, InfoEditActivity.class);
        Bundle params = new Bundle();
        params.putString(Constants.KEY_TEXT, title);
        params.putInt(Constants.KEY_ACTION, action);
        params.putSerializable(Constants.KEY_BEAN, merchant);
        params.putString(Constants.KEY_TEXT2, content);
        intent.putExtras(params);
        LaunchWithExitUtils.startActivityForResult(activity, intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        String result = data.getStringExtra(Constants.KEY_TEXT);
        if (requestCode == Constants.REQUESTCODE_MERCHANT_NAME && resultCode == Constants.RESULT_MERCHANT_NAME && data != null) {
            setInfo(merchantName, textColor, result);
        } else if (requestCode == Constants.REQUESTCODE_MERCHANT_LOCATION && resultCode == Constants.RESULT_MERCHANT_LOCATION && data != null) {
            setInfo(locationText, textColor, result);
        } else if (requestCode == Constants.REQUESTCODE_MERCHANT_TEL && resultCode == Constants.RESULT_MERCHANT_TEL && data != null) {
            setInfo(telText, textColor, result);
        }
    }
}
