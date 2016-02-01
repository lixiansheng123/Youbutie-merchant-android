package com.yuedong.youbutie_merchant_android.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.yuedong.youbutie_merchant_android.EditMerchantActivity;
import com.yuedong.youbutie_merchant_android.InfoEditActivity;
import com.yuedong.youbutie_merchant_android.MerchantSettingActivity;
import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.ServiceListActivity;
import com.yuedong.youbutie_merchant_android.adapter.MerchantServiceListAdapter;
import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseFragment;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.view.SupportScrollConflictListView;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2015/12/26.
 * 门店管理fm
 */
public class MerchantManagerFm extends BaseFragment implements View.OnClickListener {
    private NetworkImageView merchantPic;
    private TextView merchantNmae, merchantAddress, merchantTime, merchantPhone, merchantAd;
    private SupportScrollConflictListView serviceList;
    private MerchantServiceListAdapter adapter;
    private Merchant merchant;
    private static final String TAG = "MerchantManagerFm";


    @Override
    public void initViews(Bundle savedInstanceState) {
        buildUi(new TitleViewHelper().createDefaultTitleView7("门店管理", R.drawable.icon_grey_setting, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchWithExitUtils.startActivity(getActivity(), MerchantSettingActivity.class);
            }
        }), false, false, false, R.layout.fragment_merchant_manager);
        adapter = new MerchantServiceListAdapter(getActivity());
        merchantAd = fvById(R.id.id_merchant_ad);
        merchantPic = fvById(R.id.id_merchant_pic);
        merchantNmae = fvById(R.id.id_merchant_name);
        merchantAddress = fvById(R.id.id_merchant_address);
        merchantTime = fvById(R.id.id_merchant_time);
        merchantPhone = fvById(R.id.id_merchant_phone);
        serviceList = fvById(R.id.id_merchant_service_list);
        serviceList.setAdapter(adapter);
        ui();
    }

    public void ui() {
        MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {

            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onFinish() {
                dialogStatus(false);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                if (CommonUtils.listIsNotNull(list)) {
                    merchant = list.get(0);
                    merchantNmae.setText(merchant.getName());
                    merchantAddress.setText("    " + merchant.getAddress());
                    merchantPhone.setText("    " + merchant.getTelephone());
                    DisplayImageByVolleyUtils.loadImage(merchantPic, merchant.getPhoto());
                    merchantTime.setText("    " + "早" + merchant.getStartTime() + "-晚" + merchant.getEndTime());
                    String mercantAdText = merchant.getIntroduced();
                    if (StringUtil.isNotEmpty(mercantAdText)) {
                        merchantAd.setGravity(Gravity.CENTER);
                        merchantAd.setText(mercantAdText);
                        merchantAd.setTextColor(Color.parseColor("#938381"));
                    }
                    adapter.setData(merchant.getServiceInfo());
                    adapter.notifyDataSetChanged();


                } else {
                    T.showShort(getActivity(), "您还没有门店");
                }
            }

            @Override
            public void onError(int i, String s) {
                error(s);
            }
        });
    }

    @Override
    public void initEvents() {
        fvById(R.id.id_merchant_ad_layout).setOnClickListener(this);
        fvById(R.id.id_edit_merchant).setOnClickListener(this);
        fvById(R.id.id_edit_service_list_layout).setOnClickListener(this);
        adapter.setOnButtonSwitchListener(new MerchantServiceListAdapter.OnButtonSwitchListener() {
            @Override
            public void bottonSwitch(int state) {
                // 更新洗车状态
                List<ServiceInfoDetailBean> serviceInfo = merchant.getServiceInfo();
                for (ServiceInfoDetailBean bean : serviceInfo) {
                    if (bean.name.equals("洗车"))
                        bean.state = state;
                }
                Merchant updateMercahnt = new Merchant();
                updateMercahnt.setServiceInfo(serviceInfo);
                updateMercahnt.update(getActivity(), merchant.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        App.getInstance().meMerchantInfoChange = true;
                        T.showShort(getActivity(), "修改成功");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        error(s);
                    }
                });
            }
        });
    }


    /**
     * 转换分钟
     *
     * @param calendar
     * @return
     */
    private String parseMinutes(Calendar calendar) {
        int minutes = calendar.get(Calendar.MINUTE);
        if (minutes < 10)
            return "0" + minutes;
        return "" + minutes;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_merchant_ad_layout:
                if (merchant != null) {
                    Intent it = new Intent(getActivity(), InfoEditActivity.class);
                    Bundle b3 = new Bundle();
                    b3.putSerializable(Constants.KEY_BEAN, merchant);
                    b3.putString(Constants.KEY_TEXT, "门店广告");
                    b3.putInt(Constants.KEY_ACTION, InfoEditActivity.ACTION_INPUT_MEMBER_AD);
                    it.putExtras(b3);
                    LaunchWithExitUtils.startActivityForResult(MerchantManagerFm.this, it, Constants.REQUESTCODE_MERCHANT_AD);
                }
                break;
            case R.id.id_edit_merchant:
                if (merchant != null) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable(Constants.KEY_BEAN, merchant);
                    LaunchWithExitUtils.startActivity(getActivity(), EditMerchantActivity.class, bundle2);
                }
                break;


            case R.id.id_edit_service_list_layout:
                if (merchant != null) {
                    Intent intent = new Intent(getActivity(), ServiceListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.KEY_BEAN, merchant);
                    intent.putExtras(bundle);
                    LaunchWithExitUtils.startActivityForResult(MerchantManagerFm.this, intent, Constants.REQEUSTCODE_EDIT_SERVICE_LIST);
                }
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.d(TAG + "--onActivityResult");
        if (requestCode == Constants.REQEUSTCODE_EDIT_SERVICE_LIST && resultCode == Constants.RESULT_EDIT_SERVICE_LIST && data != null) {
            ui();
        } else if (requestCode == Constants.REQUESTCODE_MERCHANT_AD && resultCode == Constants.RESULT_MERCHANT_AD) {
            L.d(TAG + "--REQUESTCODE_MERCHANT_AD");
            ui();
        }
    }
}
