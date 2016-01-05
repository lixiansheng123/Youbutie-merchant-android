package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BasePhotoCropActivity;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.mouble.bmob.listener.UploadListener;
import com.yuedong.youbutie_merchant_android.utils.ApiUtils;
import com.yuedong.youbutie_merchant_android.utils.CropHelper;
import com.yuedong.youbutie_merchant_android.utils.CropParams;
import com.yuedong.youbutie_merchant_android.utils.DateUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.ImageZoomUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.view.SelectPicPop;

import java.util.Date;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;

public class EditMerchantActivity extends BasePhotoCropActivity implements View.OnClickListener {
    private ImageView merchantPic;
    private TextView merchantName, locationText, businessStartTime, businessEndTime, telText;
    private Merchant merchant;
    private int textColor = Color.parseColor("#82706e");
    private SelectPicPop selectPicPop;
    private CropParams mCropParams = new CropParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("编辑门店"));
        setShowContentView(R.layout.activity_edit_merchant);
    }

    @Override
    protected void initViews() {
        selectPicPop = new SelectPicPop(context);
        merchantName = fvById(R.id.id_merchant_name);
        locationText = fvById(R.id.id_merchant_location);
        businessStartTime = fvById(R.id.id_select_start_time);
        businessEndTime = fvById(R.id.id_select_end_time);
        telText = fvById(R.id.id_merchant_tel);
        merchantPic = fvById(R.id.id_merchant_pic);
    }

    @Override
    protected void initEvents() {
        merchantPic.setOnClickListener(this);
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
        loadDialog.setMessage("上传信息中");
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
            case R.id.id_merchant_pic:
                selectPicPop.show();
                break;


        }
    }
}
