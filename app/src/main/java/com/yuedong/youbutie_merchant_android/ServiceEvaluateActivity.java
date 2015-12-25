package com.yuedong.youbutie_merchant_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.mouble.IUserEvaluateEvent;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Appraise;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.Order;
import com.yuedong.youbutie_merchant_android.mouble.bmob.impl.UserEvaluateEventImpl;
import com.yuedong.youbutie_merchant_android.mouble.bmob.listener.BmobSaveResultObjListener;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;

/**
 * Created by Administrator on 2015/12/19.
 */
public class ServiceEvaluateActivity extends BaseActivity {
    private RatingBar ratingBar;
    private EditText inputBox;
    private Button submitBtn;
    private Order order;
    private IUserEvaluateEvent<Appraise> userEvaluateEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("服务评价"));
        setShowContentView(R.layout.activity_service_evaluate);
        order = (Order) getIntent().getExtras().getSerializable(Constants.KEY_BEAN);
        userEvaluateEvent = new UserEvaluateEventImpl();
        loadDialog.setMessage("提交数据中..");
    }

    @Override
    protected void initViews() {
        ratingBar = fvById(R.id.id_grade);
        inputBox = fvById(R.id.id_input_box);
        submitBtn = fvById(R.id.id_submit);
    }

    @Override
    protected void initEvents() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEvaluateEvent.addOrderEvaluate(order, App.getInstance().getUser(), inputBox.getText().toString(), ratingBar.getRating(), new BmobSaveResultObjListener<Appraise>() {
                    @Override
                    public void onResult(Appraise result) {
                        dialogStatus(false);
                        Intent intent = new Intent();
                        intent.putExtra(Constants.KEY_BEAN, result);
                        setResult(Constants.RESULT_ORDER_EVALUATE, intent);
                        defaultFinished();
                    }

                    @Override
                    public void onStart() {
                        dialogStatus(true);
                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        error(s);
                        dialogStatus(false);
                    }
                });
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                btnStatus();
            }
        });
        inputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnStatus();
            }
        });
    }

    @Override
    protected void ui() {

    }

    private void btnStatus() {
        if (ratingBar.getRating() != 0 && StringUtil.isNotEmpty(inputBox.getText().toString())) {
            submitBtn.setBackgroundResource(R.color.yellowf0c010);
            submitBtn.setClickable(true);
        } else {
            submitBtn.setBackgroundResource(R.color.grey938381);
            submitBtn.setClickable(false);
        }
    }
}
