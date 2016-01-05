package com.yuedong.youbutie_merchant_android.framework;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.utils.WindowUtils;


/**
 * 基本的Dialog 实现标题然后提供内容給子类覆盖
 */
public class BaseDialog extends Dialog {
    private Context mContext;
    private boolean mCanCanceledOnTouchOutSide;
    /**
     * 允许回退消失
     */
    private boolean mCancelable;
    // 信息框
    private TextView mMessageView = null;
    // 标题
    private TextView mTitleView = null;
    // 加载图标
    public ImageView mLoaderPic = null;
    private Button mBtnNegative;

    private static BaseDialog mDialog;

    public BaseDialog(Context context) {
        this(context, R.style.dialog);
        mCanCanceledOnTouchOutSide = false;
        mCancelable = true;
        this.mContext = context;
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
        mCanCanceledOnTouchOutSide = false;
        mCancelable = true;
        this.mContext = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mCanCanceledOnTouchOutSide)
            return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_CANCEL:
                dismiss();
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !mCancelable) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void setDialogCancelable(boolean cancelable) {
        mDialog.setCancelable(cancelable);
        mCancelable = cancelable;
    }

    public void setCanCanceledOnTouchOutSide(boolean bFlag) {
        mCanCanceledOnTouchOutSide = bFlag;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
        }

    };

    public void dismissAfterDelay(long time) {
        mHandler.sendEmptyMessageDelayed(0, time);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleView.setText(title);
    }

    public void setMessage(CharSequence message) {
        mMessageView.setText(message);
    }

    public String getMessage() {
        return mMessageView.getText().toString();
    }

    public static class Builder {
        BaseDialog dialog;
        // private Button mBtnNegative;
        private Button mBtnPositive;
        private ImageView mImgViewIcon;
        private Context mContext;
        private String mMessage;
        private String mTitle;
        private ProgressBar progressBar;
        private Integer[] phoneWh;
        private EditText inputBox;


        public Builder(Context context) {
            mContext = context;
            phoneWh = WindowUtils.getPhoneWH(context);
        }

        /**
         * @return 创建带两个按钮的提示对话框
         */
        public BaseDialog createTitleMessage2BtnDialog() {
            LayoutInflater layoutinflater = LayoutInflater.from(getContext());
            dialog = new BaseDialog(mContext);
            View view = layoutinflater.inflate(R.layout.layout_dialog_title_message_2btn, null);
            dialog.mTitleView = (TextView) view.findViewById(R.id.tv_dialog_title);
            dialog.mMessageView = (TextView) view.findViewById(R.id.tv_dialog_content);
            dialog.mBtnNegative = (Button) view.findViewById(R.id.btn_left_id);
            mBtnPositive = (Button) view.findViewById(R.id.btn_right_id);
            dialog.setContentView(view);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.transparentpic);
            mDialog = dialog;
            return dialog;
        }

        public BaseDialog createMessage1BtnDialog() {
            LayoutInflater layoutinflater = LayoutInflater.from(getContext());
            dialog = new BaseDialog(mContext);
            View view = layoutinflater.inflate(R.layout.layout_dialog_title_message_2btn, null);
            view.findViewById(R.id.btn_left_id).setVisibility(View.GONE);
            view.findViewById(R.id.tv_dialog_title).setVisibility(View.GONE);
            dialog.mMessageView = (TextView) view.findViewById(R.id.tv_dialog_content);
            mBtnPositive = (Button) view.findViewById(R.id.btn_right_id);
            dialog.setContentView(view);
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(R.drawable.transparentpic);
            mDialog = dialog;
            return dialog;
        }

        /**
         * @return 创建带银行卡绑定提示对话框
         */
        public BaseDialog createTitleBindBank2BtnDialog(View view) {
            dialog = new BaseDialog(mContext);
            // View view =
            // layoutinflater.inflate(R.layout.layout_dialog_title_message_2btn,
            // null);
            // dialog.mTitleView = (TextView)
            // view.findViewById(R.id.tv_dialog_title);
            // dialog.mMessageView = (TextView)
            // view.findViewById(R.id.tv_dialog_content);
            // dialog.mBtnNegative = (Button)
            // view.findViewById(R.id.btn_left_id);
            // mBtnPositive = (Button) view.findViewById(R.id.btn_right_id);
            dialog.setContentView(view);
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.drawable.transparentpic);
            mDialog = dialog;
            return dialog;
        }

//        /**
//         * @return 创建下载对话框
//         */
//        public BaseDialog createDownloadingDialog() {
//            LayoutInflater layoutinflater = LayoutInflater.from(getContext());
//            dialog = new BaseDialog(mContext);
//            View view = layoutinflater.inflate(R.layout.layout_dialog_upgrading, null);
//            dialog.mTitleView = (TextView) view.findViewById(R.id.upgrading_dialog_title);
//            dialog.mBtnNegative = (Button) view.findViewById(R.id.btn_ugrade_cancel);
//            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
//            dialog.setContentView(view);
//            dialog.setCanCanceledOnTouchOutSide(false);
//            mDialog = dialog;
//            return dialog;
//        }

        /**
         * @return 创建网络加载对话框
         */
        public BaseDialog createNetLoadingDialog() {
            LayoutInflater layoutinflater = LayoutInflater.from(mContext);
            View view = layoutinflater.inflate(R.layout.dialog_load, null);
            Integer[] phoneWh = App.getInstance().getPhoneWh();
            dialog = new BaseDialog(mContext);
            dialog.mMessageView = (TextView) view.findViewById(R.id.id_dialog_msg);
            dialog.mLoaderPic = (ImageView) view.findViewById(R.id.id_load_pic);
            dialog.setContentView(view);
            mDialog = dialog;
            dialog.setCanCanceledOnTouchOutSide(false);
            dialog.setDialogCancelable(false);
            ViewGroup.LayoutParams rootParams = view.getLayoutParams();
            int windowW1_3 = phoneWh[0] / 3;
            rootParams.width = rootParams.height = windowW1_3;
            ViewGroup.LayoutParams picLayouParams = dialog.mLoaderPic.getLayoutParams();
            picLayouParams.width = picLayouParams.height = windowW1_3 * 5 / 12;
            return dialog;
        }

        /**
         * @return 创建提示对话框
         */
        public BaseDialog createMessageDialog() {
            LayoutInflater layoutinflater = LayoutInflater.from(getContext());
            dialog = new BaseDialog(mContext);
            View view = layoutinflater.inflate(R.layout.layout_dialog_message, null);
            dialog.mMessageView = (TextView) view.findViewById(R.id.tv_message);
            dialog.mTitleView = (TextView) view.findViewById(R.id.tv_dialog_title);
            dialog.setContentView(view);
            dialog.setCanCanceledOnTouchOutSide(false);
            dialog.setCancelable(false);
            mDialog = dialog;
            return dialog;
        }

        /**
         * 创建一个输入框
         *
         * @return
         */
        public BaseDialog createInputDialog() {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            dialog = new BaseDialog(mContext);
            View view = layoutInflater.inflate(R.layout.dialog_inputbox, null);
            dialog.mTitleView = (TextView) view.findViewById(R.id.id_title);
            mBtnPositive = (Button) view.findViewById(R.id.id_btn_confirm);
            dialog.mBtnNegative = (Button) view.findViewById(R.id.id_btn_cancle);
            inputBox = (EditText) view.findViewById(R.id.id_input_box);
            dialog.setContentView(view);
            dialog.setCanCanceledOnTouchOutSide(false);
            dialog.setCancelable(false);
            mDialog = dialog;
            ViewGroup.LayoutParams rootParams = view.getLayoutParams();
            int windowW2_3 = phoneWh[0] * 2 / 3;
            rootParams.width = windowW2_3;
            rootParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            return dialog;
        }

        public Context getContext() {
            return mContext;
        }

        public EditText getInputBox() {
            return inputBox;
        }

        public Builder setMessage(String s) {
            mMessage = s;
            mDialog.mMessageView.setText(mMessage);
            return this;
        }

        public Builder setIconResource(int iconID) {
            if (mImgViewIcon != null) {
                mImgViewIcon.setBackgroundResource(iconID);
            }
            return this;
        }

        public Builder setNegativeButton(int text, OnClickListener onclicklistener) {
            dialog.mBtnNegative.setText(text);
            final OnClickListener listener = onclicklistener;
            dialog.mBtnNegative.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });
            return this;
        }

        public Builder setPositiveButton(int text, OnClickListener onclicklistener) {
            mBtnPositive.setText(text);
            final OnClickListener listener = onclicklistener;
            mBtnPositive.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            });
            return this;
        }

        public Builder setTitle(int title) {
            mTitle = (String) mContext.getText(title);
            mDialog.mTitleView.setText(mTitle);
            return this;
        }

        public Builder setTitle(String s) {
            mTitle = s;
            mDialog.mTitleView.setText(mTitle);
            return this;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }
    }
}
