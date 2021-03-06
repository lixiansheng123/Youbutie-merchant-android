package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.app.Config;
import com.yuedong.youbutie_merchant_android.app.Constants;
import com.yuedong.youbutie_merchant_android.bean.InviteMemberListBean;
import com.yuedong.youbutie_merchant_android.bean.PhoneAddressBookBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.model.MerchantEvent;
import com.yuedong.youbutie_merchant_android.model.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.model.UserEvent;
import com.yuedong.youbutie_merchant_android.model.VipEvent;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Merchant;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Messages;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.model.bmob.bean.Vips;
import com.yuedong.youbutie_merchant_android.model.listener.ObtainSecretKeyListener;
import com.yuedong.youbutie_merchant_android.utils.AppUtils;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.DimenUtils;
import com.yuedong.youbutie_merchant_android.utils.DisplayImageByVolleyUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.LaunchWithExitUtils;
import com.yuedong.youbutie_merchant_android.utils.RequestYDHelper;
import com.yuedong.youbutie_merchant_android.utils.SPUtils;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.SystemUtils;
import com.yuedong.youbutie_merchant_android.utils.T;
import com.yuedong.youbutie_merchant_android.utils.ViewUtils;
import com.yuedong.youbutie_merchant_android.view.RoundImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 邀请会员
 */
public class InviteMemberActivity extends BaseActivity {
    private ExpandableListView listView;
    private List<String> groups = new ArrayList<String>();
    private List<List<InviteMemberListBean>> childs = new ArrayList<List<InviteMemberListBean>>();
    private List<PhoneAddressBookBean> phoneContacts;
    private MyAdapter myAdapter;
    private String mSecretKey;
    private Merchant merchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi(new TitleViewHelper().createDefaultTitleView3("邀请加入会员"),//
                false, false, false, R.layout.activity_invite_member);

    }

    @Override
    protected void initViews() {
        listView = fvById(R.id.id_list);
        // 去掉箭头
        listView.setGroupIndicator(null);
        myAdapter = new MyAdapter();
    }

    @Override
    protected void initEvents() {
        fvById(R.id.id_input_box).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.KEY_LIST, (ArrayList) phoneContacts);
                LaunchWithExitUtils.startActivity(activity, SearchAddressBookFriendActivity.class, bundle);
            }
        });
    }

    List<User> totalRegistUser = new ArrayList<User>();

    @Override
    protected void ui() {
        MerchantEvent.getInstance().findMeMetchant(App.getInstance().getUser().getObjectId(), new FindListener<Merchant>() {
            @Override
            public void onStart() {
                dialogStatus(true);
            }

            @Override
            public void onSuccess(List<Merchant> list) {
                dialogStatus(true);
                merchant = list.get(0);
                // 获取店铺vip用户
                VipEvent.getInstance().findVipByMerchant(merchant.getObjectId(), new FindListener<Vips>() {
                    @Override
                    public void onSuccess(List<Vips> list) {
                        L.d("findVipByMerchant succeed:" + list.toString());
                        myAdapter.setVipsList(list);
                        // 获取secretkey
                        App.getInstance().getYdApiSecretKey(new ObtainSecretKeyListener() {
                            @Override
                            public void start() {
                                dialogStatus(true);
                            }

                            @Override
                            public void end() {
                            }

                            @Override
                            public void succeed(String secretKey) {
                                mSecretKey = secretKey;
                                // 获取手机的通讯录信息
                                App.getInstance().getExecutor().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        phoneContacts = SystemUtils.getPhoneContacts(context);
                                        // 预留不正确手机的去除
                                        Iterator<PhoneAddressBookBean> phoneAddressBookBeanIterator = phoneContacts.iterator();
                                        while (phoneAddressBookBeanIterator.hasNext()) {
                                            PhoneAddressBookBean bookBean = phoneAddressBookBeanIterator.next();
                                            // 不是正确的手机号码
                                            if (!bookBean.getPhoneNumber().matches(Config.REGEX_TEL))
                                                phoneAddressBookBeanIterator.remove();
                                        }
                                        // 获取手机通讯录信息完毕
                                        List<String> mobiles = new ArrayList<String>();
                                        for (PhoneAddressBookBean bookBean : phoneContacts) {
                                            if (StringUtil.isNotEmpty(bookBean.getPhoneNumber())) {
                                                mobiles.add(bookBean.getPhoneNumber());
                                            }
                                        }
                                        final List<String> tempMobiles = mobiles;
                                        if (CommonUtils.listIsNotNull(tempMobiles)) {
                                            mainHandle.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // 查询号码超过50个bmob会异常 所以得分次来查询 恶心。。。
                                                    if (tempMobiles.size() > 50) {
                                                        // 全部已经注册的用户
                                                        double countD = tempMobiles.size() * 1.0f / 50;
                                                        int count = (int) countD;
                                                        if (countD != count) {
                                                            //查多少次
                                                            count++;
                                                        }
                                                        for (int i = 0; i < count; i++) {
                                                            int startIndex = i * 50;
                                                            int endIndex = (i + 1) * 50;
                                                            if (endIndex > tempMobiles.size())
                                                                endIndex = tempMobiles.size();
                                                            // subList 包头不包尾
                                                            List<String> partMobiles = tempMobiles.subList(startIndex, endIndex);
                                                            L.d("部分手机通讯录:" + partMobiles.size());
                                                            final int tempCount = count;
                                                            final int tempI = i;
                                                            UserEvent.getInstance().findUserByMobiles(partMobiles, new FindListener<User>() {
                                                                @Override
                                                                public void onSuccess(List<User> list) {
                                                                    if (CommonUtils.listIsNotNull(list)) {
                                                                        totalRegistUser.addAll(list);
                                                                    }
                                                                    if (tempI == (tempCount - 1)) {
                                                                        // 最后一次
                                                                        updateList(totalRegistUser);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onError(int i, String s) {
                                                                    error(i);
                                                                    dialogStatus(false);
                                                                    return;
                                                                }
                                                            });
                                                        }
                                                    } else {
                                                        // 根据手机号集合获取已经在油补贴注册过的用户
                                                        UserEvent.getInstance().findUserByMobiles(tempMobiles, new FindListener<User>() {

                                                            @Override
                                                            public void onSuccess(final List<User> list) {
                                                                totalRegistUser = list;
                                                                updateList(list);
                                                            }

                                                            @Override
                                                            public void onError(int i, String s) {
                                                                dialogStatus(false);
                                                                error(i);
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        } else {
                                            mainHandle.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialogStatus(false);
                                                    T.showShort(context, "您的手机通讯录不存在信息");
                                                }
                                            });

                                        }
                                    }
                                });
                            }

                            @Override
                            public void fail(int code, String error) {
                                T.showShort(context, error + code);
                                dialogStatus(false);
                            }
                        });


                    }

                    @Override
                    public void onError(int i, String s) {
                        error(i);
                        dialogStatus(false);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                error(i);
                dialogStatus(false);
            }
        });


    }

    /**
     * 更新列表
     *
     * @param list
     */
    private void updateList(List<User> list) {
        L.d("已经注册的通讯录好友:" + list.toString());
        App.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                // 操作大量数据放到异步去做
                initListData(totalRegistUser);
                mainHandle.post(new Runnable() {
                    @Override
                    public void run() {
                        dialogStatus(false);
                        listView.setAdapter(myAdapter);
                        // 展开所有组
                        for (int i = 0; i < myAdapter.getGroupCount(); i++) {
                            listView.expandGroup(i);
                        }
                    }
                });
            }
        });
    }

    /**
     * 初始化列表数组
     *
     * @param list
     */
    private void initListData(List<User> list) {
        groups.add("已加入油补贴");
        groups.add("未加入油补贴");
        if (CommonUtils.listIsNotNull(list)) {
            List<InviteMemberListBean> alreadayRegistList = new ArrayList<InviteMemberListBean>();
            List<InviteMemberListBean> notRegistList = new ArrayList<InviteMemberListBean>();
            Iterator<User> userIterator = list.iterator();
            while (userIterator.hasNext()) {
                User user = userIterator.next();
                for (int j = 0; j < phoneContacts.size(); j++) {
                    PhoneAddressBookBean bookBean = phoneContacts.get(j);
                    InviteMemberListBean bean = new InviteMemberListBean();
                    bean.remark = bookBean.getContactName();
                    bean.setPhoto(user.getPhoto());
                    bean.bg = AppUtils.randomGetAddressBookUnRegistFriendHead();
                    bean.setMobilePhoneNumber(bookBean.getPhoneNumber());
                    bean.setCreatedAt(user.getCreatedAt());
                    if (user.getMobilePhoneNumber().equals(bookBean.getPhoneNumber())) {
                        // 对应上了 为user增加备注
                        bean.regist = true;
                        bean.setObjectId(user.getObjectId());
                        alreadayRegistList.add(bean);

                    } else {
                        // 对应不上的增加到未注册里面去
                        bean.regist = false;
                        notRegistList.add(bean);
                    }
                }
            }
            childs.add(alreadayRegistList);
            childs.add(notRegistList);
        } else {
            // 把通讯录的数据填写进入
            List<InviteMemberListBean> alreadayRegistList = new ArrayList<InviteMemberListBean>();
            List<InviteMemberListBean> notRegistList = new ArrayList<InviteMemberListBean>();
            for (PhoneAddressBookBean bookBean : phoneContacts) {
                InviteMemberListBean bean = new InviteMemberListBean();
                bean.remark = bookBean.getContactName();
                bean.regist = false;
                bean.bg = AppUtils.randomGetAddressBookUnRegistFriendHead();
                bean.setMobilePhoneNumber(bookBean.getPhoneNumber());
                notRegistList.add(bean);
            }
            childs.add(alreadayRegistList);
            childs.add(notRegistList);
        }

        L.d("子列表数据:" + childs.toString());
    }


    public class MyAdapter extends BaseExpandableListAdapter {
        // 已经成为该店铺会员的用户
        private List<Vips> vipsList;

        public void setVipsList(List<Vips> vipsList) {
            this.vipsList = vipsList;
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childs.get(groupPosition).size();
        }

        //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childs.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, R.layout.item_invite_member_group, groupPosition);
            viewHolder.setText(R.id.id_text, groups.get(groupPosition));
            return viewHolder.getConvertView();
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = ViewHolder.get(context, convertView, parent, R.layout.item_invite_member_child, childPosition);
            RoundImageView pic = viewHolder.getIdByView(R.id.id_user_pic);
            TextView name = viewHolder.getIdByView(R.id.id_user_name);
            TextView time = viewHolder.getIdByView(R.id.id_time);
            TextView picText = viewHolder.getIdByView(R.id.id_user_pic_text);
            Button btnInvite = viewHolder.getIdByView(R.id.id_btn_invite);
            View line = viewHolder.getIdByView(R.id.id_line);
            final InviteMemberListBean bean = childs.get(groupPosition).get(childPosition);
            String remarkName = bean.remark;
            name.setText(remarkName);
            ViewUtils.showLayout(btnInvite);
            if (bean.regist) {
                // 判断是否用户已经是会员了
                if (isMerchantMember(bean.getObjectId()))
                    ViewUtils.hideLayout(btnInvite);
                final RequestYDHelper requestYDHelper = new RequestYDHelper();
                requestYDHelper.setAppSecretkey(mSecretKey);
                final String registInviteNum = (String) SPUtils.get(context, Constants.SP_INVITE_ADD_MEMBER, "");
                btnStatus(btnInvite, bean, registInviteNum);
                // 已经注册
                DisplayImageByVolleyUtils.loadImage(bean.getPhoto(), pic);
                pic.setType(RoundImageView.TYPE_ROUND);
                pic.setRaduisAngle(DimenUtils.dip2px(context, 3));
                pic.setBackgroundDrawable(null);
                time.setText(bean.getCreatedAt());
                ViewUtils.showLayout(time);
                ViewUtils.hideLayout(picText);
                if (btnInvite.getText().toString().equals(getString(R.string.str_invite)))
                    btnInvite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogStatus(true);
                            // 消息表插入记录
                            List<String> targets = new ArrayList<String>();
                            targets.add(bean.getObjectId());
                            Messages messages = new Messages();
                            messages.setType(3);
                            messages.setTargets(targets);
                            messages.setMerchant(merchant);
                            messages.setSender(App.getInstance().getUser());
                            messages.setTitle(getString(R.string.str_push_merchant_invite_member_title));
                            messages.setContent(String.format(getString(R.string.str_push_merchant_invite_member_content), merchant.getName()));
                            messages.save(context, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    dialogStatus(false);
                                    // 发送推送
                                    requestYDHelper.requestPushSingle(getString(R.string.str_push_merchant_invite_member_title),//
                                            String.format(getString(R.string.str_push_merchant_invite_member_content),//
                                                    merchant.getName()), bean.getObjectId(), //
                                            RequestYDHelper.PUSH_TYPE_MERCHANT_INVITE_MEMBER, "", bean.getObjectId());
                                    SPUtils.put(context, Constants.SP_INVITE_ADD_MEMBER, registInviteNum + bean.getMobilePhoneNumber() + ",");
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    error(i);
                                    dialogStatus(false);
                                }
                            });

                        }
                    });
                else
                    btnInvite.setOnClickListener(null);

            } else {
                final String unRegistInviteNum = (String) SPUtils.get(context, Constants.SP_INVITE_REGIST, "");
                L.d("未注册已经验证过的号码:" + unRegistInviteNum);
                btnStatus(btnInvite, bean, unRegistInviteNum);
                final String unRegistUserMobile = bean.getMobilePhoneNumber();
                L.d("未注册的用户手机号码:" + unRegistUserMobile);
                if (btnInvite.getText().toString().equals(getString(R.string.str_invite)))
                    btnInvite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 未注册邀请操作
                            SystemUtils.sms(context, unRegistUserMobile, getString(R.string.str_sms_moudle));
                            SPUtils.put(context, Constants.SP_INVITE_REGIST, unRegistInviteNum + unRegistUserMobile + ",");
                            notifyDataSetChanged();
                        }
                    });
                else
                    btnInvite.setOnClickListener(null);
                // 未注册
                ViewUtils.showLayout(picText);
                ViewUtils.hideLayout(time);
                pic.setImageDrawable(null);
                pic.setBackgroundResource(bean.bg);
                if (StringUtil.isNotEmpty(remarkName))
                    picText.setText(remarkName.substring(0, 1));
            }

            if (isLastChild)
                ViewUtils.hideLayout(line);
            else
                ViewUtils.showLayout(line);

            return viewHolder.getConvertView();

        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        /**
         * 是否已经是该店铺会员
         *
         * @return
         */
        public boolean isMerchantMember(String userObjectId) {
            if (vipsList != null) {
                for (Vips vips : vipsList) {
                    User user = vips.getUser();
                    if (userObjectId.equals(user.getObjectId()))
                        return true;
                }
            }
            return false;
        }
    }

    private void btnStatus(Button btnInvite, InviteMemberListBean bean, String inviteNums) {
        String[] split = inviteNums.split(",");
        boolean has = false;
        for (String inviteNum : split) {
            if (inviteNum.equals(bean.getMobilePhoneNumber())) {
                has = true;
            }
        }
        if (has) {
            btnInvite.setBackgroundResource(R.drawable.bg_round_grey);
            btnInvite.setText(getString(R.string.str_already_invite));
        } else {
            btnInvite.setBackgroundResource(R.drawable.bg_round_yellow);
            btnInvite.setText(getString(R.string.str_invite));
        }
    }
    /*本想在这个页面的时候被拒绝会刷新状态 因为数据并没有发生变化所以刷新也没有用 不会有效果的*/
//    @Override
//    protected void notifyMsg(int msgType, Map<String, Object> data, JSONObject jsonObject) {
//        super.notifyMsg(msgType, data, jsonObject);
//        L.d("notifyMsg-child-start");
//        if (msgType == RequestYDHelper.PUSH_TYPE_MERCHANT_INVITE_MEMBER) {
//            L.d("notifyMsg-child-end");
//            if (myAdapter != null) {
//                myAdapter.notifyDataSetChanged();
//            }
//        }
//    }
}
