package com.yuedong.youbutie_merchant_android.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;


/**
 * Created by Administrator on 2015/12/11.
 */
public class SelectPicPop extends Dialog implements View.OnClickListener {


    private View mContentView;
    private OnSelectPicPopCallback mCallback;
    private TextView mTaskPic;
    private TextView mGetPic;

    public void setOnSelectPicPopCallback(OnSelectPicPopCallback callback) {
        this.mCallback = callback;
    }

    public SelectPicPop(Context context) {
        super(context, R.style.style_my_dialog);
        mContentView = getLayoutInflater().from(context).inflate(R.layout.dialog_photo_select, null);
        setContentView(mContentView);
        initView();
    }

    private void initView() {
        findViewById(R.id.view_gap).setOnClickListener(this);
        mGetPic = (TextView) findViewById(R.id.tv_get_pic);
        mTaskPic = (TextView) findViewById(R.id.tv_take_pic);
        mTaskPic.setOnClickListener(this);
        mGetPic.setOnClickListener(this);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
    }

    private View findMyViewById(int res) {
        return mContentView.findViewById(res);
    }

    public interface OnSelectPicPopCallback {

        void onTakePic();

        void onGetPic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_pic:
                if (mCallback != null) {
                    dismiss();
                    mCallback.onTakePic();
                }
                break;

            case R.id.tv_get_pic:
                if (mCallback != null) {
                    dismiss();
                    mCallback.onGetPic();
                }
                break;

            case R.id.tv_cancle:
                dismiss();
                break;
            case R.id.view_gap:
                dismiss();
                break;

            default:
                break;
        }
    }

    public View getTaskItem() {
        return mTaskPic;
    }

    public View getGetPicItem() {
        return mGetPic;
    }

    @Override
    public void show() {
        if (isShowing())
            return;
        super.show();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = (int) (App.getInstance().getPhoneWh()[0]); // 设置宽度
        this.getWindow().setAttributes(lp);
    }
}
