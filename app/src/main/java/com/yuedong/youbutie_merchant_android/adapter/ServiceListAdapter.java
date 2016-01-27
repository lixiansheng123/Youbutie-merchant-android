package com.yuedong.youbutie_merchant_android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.R;
import com.yuedong.youbutie_merchant_android.bean.ServiceInfoDetailBean;
import com.yuedong.youbutie_merchant_android.framework.BaseAdapter;
import com.yuedong.youbutie_merchant_android.framework.BaseDialog;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class ServiceListAdapter extends BaseAdapter<ServiceInfoDetailBean> {
    private List<ServiceInfoDetailBean> alreadyAddServices;

    /**
     * 增加上架的服务
     *
     * @param bean
     */
    private void addPutawayService(ServiceInfoDetailBean bean) {
        if (alreadyAddServices == null) alreadyAddServices = new ArrayList<ServiceInfoDetailBean>();
        if (!alreadyAddServices.contains(bean))
            alreadyAddServices.add(bean);
    }

    /**
     * 删除上架服务
     *
     * @param bean
     */
    private void delPutawayService(ServiceInfoDetailBean bean) {
        if (alreadyAddServices == null) alreadyAddServices = new ArrayList<ServiceInfoDetailBean>();
        if (alreadyAddServices.contains(bean))
            alreadyAddServices.remove(bean);
    }


    public void setAlreadyAddServices(List<ServiceInfoDetailBean> alreadyAddServices) {
        this.alreadyAddServices = alreadyAddServices;
    }

    public List<ServiceInfoDetailBean> getConfirmPutawayServices() {
        return alreadyAddServices;
    }

    public ServiceListAdapter(Context con, List<ServiceInfoDetailBean> data) {
        super(con, data, R.layout.item_service_list);

    }

    @Override
    public void convert(ViewHolder viewHolder, ServiceInfoDetailBean serviceInfo, final int position, View convertView) {
        TextView serviceNmae = viewHolder.getIdByView(R.id.id_service_name);
        TextView servicePrice = viewHolder.getIdByView(R.id.id_service_price);
        TextView settingPriceText = viewHolder.getIdByView(R.id.id_service_price_setting_name);
        final CheckBox gougou = viewHolder.getIdByView(R.id.id_cb_select);
        String serviceNameStr = serviceInfo.name;
        serviceNmae.setText(serviceNameStr);
        ServiceInfoDetailBean alreadyAddService = getAlreadyAddService(serviceNameStr);
        if (alreadyAddService != null) {
            serviceInfo = alreadyAddService;
            gougou.setChecked(true);

        } else {
            gougou.setChecked(false);

        }
        if (serviceInfo.price != 0) {
            servicePrice.setText(StringUtil.setDoubleValue(serviceInfo.price) + "元/次");
            ViewUtils.showLayout(servicePrice);
            ViewUtils.hideLayout(settingPriceText);

        } else {
            if (alreadyAddServices.contains(serviceInfo)) {
                alreadyAddServices.remove(serviceInfo);
                gougou.setChecked(false);
            }
            ViewUtils.showLayout(settingPriceText);
            ViewUtils.hideLayout(servicePrice);
        }


        final ServiceInfoDetailBean finalServiceInfo = serviceInfo;
        gougou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                L.d("gougou------------------------");
                if (isChecked) {
                    if (finalServiceInfo.price == 0) {
                        gougou.setChecked(false);
                        T.showShort(mCon, "请先输入价格!");
                        return;
                    }
                    addPutawayService(finalServiceInfo);
                } else {
                    delPutawayService(finalServiceInfo);
                }
            }
        });
        final BaseDialog.Builder inputDialogBuilder = new BaseDialog.Builder(mCon);
        final BaseDialog inputDialog = inputDialogBuilder.createAppInputDialog();
        viewHolder.getIdByView(R.id.id_service_price_setting_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    inputDialog.show();
            }
        });
        EditText inputBox = inputDialog.getInputBox();
        inputBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputDialog
                .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputDialog.dismiss();
                    }
                });
        inputDialog.setPositiveButton(R.string.str_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText inputBox = inputDialog.getInputBox();
                if (!StringUtil.isNotEmpty(inputBox.getText().toString())) {
                    T.showShort(mCon, "请输入价格!");
                    return;
                }
                double price = Double.parseDouble(inputBox.getText().toString());
                inputDialog.dismiss();
                finalServiceInfo.price = price;
                inputBox.setText("");
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 根据服务名获取已经上架的服务
     *
     * @param serviceName
     * @return
     */
    public ServiceInfoDetailBean getAlreadyAddService(String serviceName) {
        if (alreadyAddServices != null) {

            L.d("serviceName:" + serviceName + "===getAlreadyAddService" + alreadyAddServices.toString());
            for (ServiceInfoDetailBean bean : alreadyAddServices) {
                if (bean.name.trim().equals(serviceName.trim()))
                    return bean;
            }
        }
        return null;
    }
}
