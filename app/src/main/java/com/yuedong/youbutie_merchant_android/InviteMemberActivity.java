package com.yuedong.youbutie_merchant_android;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yuedong.youbutie_merchant_android.app.App;
import com.yuedong.youbutie_merchant_android.bean.InviteMemberListBean;
import com.yuedong.youbutie_merchant_android.bean.PhoneAddressBookBean;
import com.yuedong.youbutie_merchant_android.framework.BaseActivity;
import com.yuedong.youbutie_merchant_android.framework.ViewHolder;
import com.yuedong.youbutie_merchant_android.mouble.TitleViewHelper;
import com.yuedong.youbutie_merchant_android.mouble.UserEvent;
import com.yuedong.youbutie_merchant_android.mouble.bmob.bean.User;
import com.yuedong.youbutie_merchant_android.utils.CommonUtils;
import com.yuedong.youbutie_merchant_android.utils.L;
import com.yuedong.youbutie_merchant_android.utils.StringUtil;
import com.yuedong.youbutie_merchant_android.utils.SystemUtils;
import com.yuedong.youbutie_merchant_android.utils.T;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 邀请会员
 */
public class InviteMemberActivity extends BaseActivity {
    private ExpandableListView listView;
    private List<String> groups = new ArrayList<String>();
    private List<List<InviteMemberListBean>> childs = new ArrayList<List<InviteMemberListBean>>();
    private List<PhoneAddressBookBean> phoneContacts;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleView(new TitleViewHelper().createDefaultTitleView3("邀请加入会员"));
        setShowContentView(R.layout.activity_invite_member);
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

    }

    @Override
    protected void ui() {
        // 获取手机的通讯录信息
        dialogStatus(true);
        App.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                phoneContacts = SystemUtils.getPhoneContacts(context);
                // 获取手机通讯录信息完毕
                List<String> mobiles = new ArrayList<String>();
                for (PhoneAddressBookBean bookBean : phoneContacts) {
                    if (StringUtil.isNotEmpty(bookBean.getPhoneNumber())) {
                        mobiles.add(bookBean.getPhoneNumber());
                    }
                }
                L.d("手机通讯录电话------>>" + mobiles.toString());
                final List<String> tempMobiles = mobiles;
                if (CommonUtils.listIsNotNull(tempMobiles)) {
                    mainHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            // 根据手机号集合获取已经在油补贴注册过的用户
                            UserEvent.getInstance().findUserByMobiles(tempMobiles, new FindListener<User>() {

                                @Override
                                public void onSuccess(final List<User> list) {
                                    L.d("已经注册的通讯录好友:" + list.toString());
                                    App.getInstance().getExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            // 操作大量数据放到异步去做
                                            initListData(list);
                                            mainHandle.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialogStatus(false);
                                                    listView.setAdapter(myAdapter);
                                                }
                                            });
                                        }
                                    });
                                }

                                @Override
                                public void onError(int i, String s) {
                                    dialogStatus(false);
                                    error(s);
                                }
                            });
                        }
                    });
                } else {
                    T.showShort(context, "您的手机通讯录不存在信息");
                }
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
                    bean.setMobilePhoneNumber(user.getMobilePhoneNumber());
                    bean.setCreatedAt(user.getCreatedAt());
                    if (user.getMobilePhoneNumber().equals(bookBean.getPhoneNumber())) {
                        // 对应上了 为user增加备注
                        bean.regist = true;
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
                bean.setMobilePhoneNumber(bookBean.getPhoneNumber());
                notRegistList.add(bean);
            }
            childs.add(alreadayRegistList);
            childs.add(notRegistList);
        }

        L.d("子列表数据:" + childs.toString());
    }


    public class MyAdapter extends BaseExpandableListAdapter {

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
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
