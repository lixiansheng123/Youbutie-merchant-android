package com.yuedong.youbutie_merchant_android.bean;

/**
 * 统计消费类目bean
 */
public class CountConsumeBean {
    private ServiceInfoDetailBean serviceInfoDetailBean;// 门店服务
    private int typeRatio;// 类目占总数比例
    private int typeNumber;

    public ServiceInfoDetailBean getServiceInfoDetailBean() {
        return serviceInfoDetailBean;
    }

    public void setServiceInfoDetailBean(ServiceInfoDetailBean serviceInfoDetailBean) {
        this.serviceInfoDetailBean = serviceInfoDetailBean;
    }

    public int getTypeRatio() {
        return typeRatio;
    }

    public void setTypeRatio(int typeRatio) {
        this.typeRatio = typeRatio;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }
}
