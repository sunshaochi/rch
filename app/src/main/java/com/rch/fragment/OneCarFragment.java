package com.rch.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.activity.NewBrandAct;
import com.rch.adatper.CarlibAdapter;
import com.rch.adatper.StandarAdapter;
import com.rch.base.BaseFrag;
import com.rch.common.Config;
import com.rch.common.EditInputFilter;
import com.rch.common.GeneralUtils;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.HomeLable;
import com.rch.custom.LoadingView;
import com.rch.custom.SortModel;
import com.rch.entity.CarEntity;
import com.rch.entity.LableEntity;
import com.rch.entity.SearchEntity;
import com.rch.entity.StandarBean;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/10/9.
 */

public class OneCarFragment extends BaseFrag{
    @ViewInject(R.id.home_title_one_layout)
    private LinearLayout oneLayout;
    @ViewInject(R.id.home_title_two_layout)
    private LinearLayout twoLayout;
    @ViewInject(R.id.home_title_three_layout)
    private LinearLayout threeLayout;
    @ViewInject(R.id.home_title_four_layout)
    private LinearLayout fourLayout;
    @ViewInject(R.id.home_title_one_text)
    private TextView tvOne;
    @ViewInject(R.id.home_title_two_text)
    private TextView tvTwo;
    @ViewInject(R.id.home_title_three_text)
    private TextView tvThree;
    @ViewInject(R.id.home_title_four_text)
    private TextView tvFour;
    @ViewInject(R.id.home_title_one_img)
    private ImageView ivOne;
    @ViewInject(R.id.home_title_two_img)
    private ImageView ivTwo;
    @ViewInject(R.id.home_title_three_img)
    private ImageView ivThree;
    @ViewInject(R.id.home_title_four_img)
    private ImageView ivFour;
    @ViewInject(R.id.home_sort_layout)
    private LinearLayout sortLayout;
    @ViewInject(R.id.home_money_layout)
    private LinearLayout moneyLayout;
    @ViewInject(R.id.home_age_layout)
    private LinearLayout ageLayout;
    @ViewInject(R.id.ll_carg)
    private LinearLayout ll_carg;
    @ViewInject(R.id.lv_carg)
    private ListView lv_carg;
    @ViewInject(R.id.tv_carg)
    private TextView tv_carg;
    @ViewInject(R.id.iv_carg)
    private ImageView iv_carg;
    @ViewInject(R.id.home_carg_layout)
    private LinearLayout home_carg_layout;

    @ViewInject(R.id.sort_one)
    private TextView sortOne;
    @ViewInject(R.id.sort_two)
    private TextView sortTwo;
    @ViewInject(R.id.sort_three)
    private TextView sortThree;

    @ViewInject(R.id.money_zero)
    private TextView moneyZero;
    @ViewInject(R.id.money_one)
    private TextView moneyOne;
    @ViewInject(R.id.money_two)
    private TextView moneyTwo;
    @ViewInject(R.id.money_three)
    private TextView moneyThree;
    @ViewInject(R.id.money_four)
    private TextView moneyFour;
    @ViewInject(R.id.money_five)
    private TextView moneyFive;

    @ViewInject(R.id.age_zero)
    private TextView ageZero;
    @ViewInject(R.id.age_one)
    private TextView ageOne;
    @ViewInject(R.id.age_two)
    private TextView ageTwo;
    @ViewInject(R.id.age_three)
    private TextView ageThree;
    @ViewInject(R.id.age_four)
    private TextView ageFour;
    @ViewInject(R.id.age_five)
    private TextView ageFive;
    @ViewInject(R.id.age_six)
    private TextView ageSix;
    @ViewInject(R.id.age_seven)
    private TextView ageSeven;
    @ViewInject(R.id.et_custom_age_one)
    private EditText customAgeOne;
    @ViewInject(R.id.et_custom_age_two)
    private EditText customAgeTwo;
    @ViewInject(R.id.et_custom_money_one)
    private EditText customMoneyOne;
    @ViewInject(R.id.et_custom_money_two)
    private EditText customMoneyTwo;
    @ViewInject(R.id.home_lable_container)
    private HomeLable lable;
    @ViewInject(R.id.home_recyclerView)
    private PullToRefreshRecyclerView pullToRefreshRecyclerView;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    @ViewInject(R.id.tv_totle)
    private TextView tv_totle;


    private RecyclerView recyclerView;
    int page=1;
    int pageSize=10;
    String gpsCity="";
    String priceType="0";//:1,  价格显示类型   0-普通用户，1-企业分销商 显示企业价，2-个人分销商 显示分销价
    String minAge="",maxAge="",sotrMode="1",minPrice="", maxPrice="",brandId="";
    List<CarEntity> tempList;
    List<CarEntity> shopList=new ArrayList<>();
    List<LableEntity> list=new ArrayList<>();//标签list

    CarlibAdapter adapter;
    boolean sotrFilter=false,moneyFilter=false,ageFilter=false,carrulesFilter=false;

    List<View> selMoneyColorList=new ArrayList<>();
    List<View> selOrderColorList =new ArrayList<>();
    List<View> selAgeColorList=new ArrayList<>();


    private String totalSize;
    private Gson gson;
    private String isRecommend="";
    private List<StandarBean>stanlist;
    private StandarAdapter standaradpter;
    private String vehicleStandard;


    @Override
    public View initView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.car_fragment,null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        gson=new Gson();
        ll_carg.setVisibility(View.VISIBLE);
        selOrderColorList.add(sortOne);
        selMoneyColorList.add(moneyZero);
        selAgeColorList.add(ageZero);
        //以上三个控制颜色

        recyclerView=pullToRefreshRecyclerView.getRefreshableView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                onRefresh();
            }
        });

        pullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page=1;
                requestShopListData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                page++;
                requestShopListData();
            }
        });

        lable.setOnTtemListener(new HomeLable.OnTtemListener() {
            @Override
            public void onDel(List<LableEntity> delList) {
                list=delList;
                List<String>lablelist=new ArrayList<>();
                lablelist.clear();
//                lable.initData(list,width);
                lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                if(list.size()>0)
                {
                    for(int i=0;i<list.size();i++){
                        lablelist.add(list.get(i).getType());
                    }

                    if(!lablelist.contains("4")){//没有车碾
                        //清空自定义数据
                        customAgeOne.setText("");
                        customAgeTwo.setText("");
                        maxAge="";
                        minAge="";
                    }
                    if(!lablelist.contains("3")){//没有金额
                        //清空自定义数据
                        customMoneyOne.setText("");
                        customMoneyTwo.setText("");
                        minPrice="";
                        maxPrice="";

                        //没有金额后把金额的弹出框选中第一个
                        if(selMoneyColorList.size()>0) {
                            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                        }
                        selMoneyColorList.clear();
                        moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
                        moneyZero.setBackgroundResource(R.drawable.select_price);
                        selMoneyColorList.add(moneyZero);

                    }
                    if(!lablelist.contains("1")&&lablelist.contains("5")){//没有品牌,有标签
                        brandId=onlycxid;
                    }else if(lablelist.contains("1")&&!lablelist.contains("5")){//有品牌,没标签
                        brandId=onlybrandid;
                    }else if(!lablelist.contains("1")&&!lablelist.contains("5")){//没有品牌,没有标签
                        brandId="";
                    }
                } else {
                    minPrice="";
                    maxPrice="";
                    brandId="";
                    minAge="";
                    maxAge="";
                }

                onRefresh();
            }

            @Override
            public void onReset() {
                list.clear();
//                lable.initData(list,width);
                lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                minPrice="";
                maxPrice="";
                brandId="";
                minAge="";
                maxAge="";
                onRefresh();
                //当重置后把价格筛选框里面不限选中
                if(selMoneyColorList.size()>0) {
                    ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
                    ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
                }
                selMoneyColorList.clear();
                moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
                moneyZero.setBackgroundResource(R.drawable.select_price);
                selMoneyColorList.add(moneyZero);
            }

        });
        gpsCity=SpUtils.getCityid(getActivity());
        onRefresh();

        InputFilter[] filters = {new EditInputFilter(99999,2)};
        //自定义价格
        if (customMoneyOne!=null) {
            customMoneyOne.setFilters(filters);
        }
        if (customMoneyTwo!=null) {
            customMoneyTwo.setFilters(filters);
        }

        lv_carg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                standaradpter.setCurrentItem(i);
                carrulesFilter=false;
                tv_carg.setTextColor(getResources().getColor(R.color.black_1));
                if(i==0) {
                    tv_carg.setText("规格");
                }else {
                    tv_carg.setText(stanlist.get(i).getVehicleStandardName());
                }
                iv_carg.setImageResource(R.mipmap.back_xb);
                home_carg_layout.setVisibility(View.GONE);
                vehicleStandard=stanlist.get(i).getVehicleStandard();
                setPullEnable();
                onRefresh();
            }
        });
    }

    private void onRefresh()
    {
          page=1;
          pullToRefreshRecyclerView.setVisibility(View.VISIBLE);//这里是打开筛选条件时候不让点击item,关闭后可以点击
          requestShopListData();
          load_view.loading();
    }

    public void requestShopListData()
    {
        RequestParam param = new RequestParam();
        param.add("currentPage",String.valueOf(page));
        param.add("pageSize",String.valueOf(pageSize));
        if(!TextUtils.isEmpty(gpsCity)) {
            param.add("city", gpsCity);
        }
        param.add("brandId", brandId);
        param.add("salesMinPrice", minPrice);
        param.add("salesMaxPrice", maxPrice);
        param.add("vehicleMinYear", minAge);
        param.add("vehicleMaxYear", maxAge);
        param.add("orderType", sotrMode);
        param.add("ifNew","1");
        if(!TextUtils.isEmpty(isRecommend)) {
            param.add("isRecommend", isRecommend);
        }
        if(!TextUtils.isEmpty(vehicleStandard)){
            param.add("vehicleStandard", vehicleStandard);
        }
        OkHttpUtils.post(Config.CARLIST, param, new OkHttpCallBack(getActivity(),"加载中...") {

            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                pullToRefreshRecyclerView.onRefreshComplete();
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    if(!result.isNull("totalSize")){
                        totalSize=result.getString("totalSize");
                    }
                    JSONArray array=result.getJSONArray("list");
                    tempList=gson.fromJson(array.toString(),new TypeToken<List<CarEntity>>(){}.getType());
                    if ( tempList!= null && tempList.size() > 0)
                    {
                        if(page==1){
                            shopList.clear();
                        }
                        shopList.addAll(tempList);
                        if(adapter==null){
                            adapter=new CarlibAdapter(getActivity(),shopList,0);
                            recyclerView.setAdapter(adapter);
                        }else {
                            adapter.updateShopListData(shopList);
                        }
//                    adapter.updateShopListData(shopList,priceType,"");
                        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                    }else {
                        if(page==1){
                            load_view.noContent();
                            load_view.setNoContentIco(R.mipmap.no_car_data);
                            load_view.setNoContentTxt("暂无数据");
                            pullToRefreshRecyclerView.setVisibility(View.GONE);
                        }else {
                            pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    if(page==1){//只需要下拉时候才显示
                        if(tv_totle.getVisibility()==View.GONE) {
                            tv_totle.setVisibility(View.VISIBLE);
                        }
                        i=3;
                        tv_totle.setText("共为您找到" + totalSize + "辆车");
                        timer = new Timer();
                        myTask = new MyTimerTask();
                        timer.schedule(myTask, 0, 1000);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onError(int code, String error) {
                load_view.loadError();
                pullToRefreshRecyclerView.onRefreshComplete();
                pullToRefreshRecyclerView.setVisibility(View.GONE);

            }
        });
    }

    private int i = 3;
    private Timer timer;
    private MyTimerTask myTask;




    /**
     * 倒计时
     *
     * @author wangbin
     *
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(i--);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                tv_btyzm.setEnabled(true);
//                tv_btyzm.setText("重新发送");
                tv_totle.setVisibility(View.GONE);
                timer.cancel();
                myTask.cancel();
            }
        }

    };


    @OnClick({R.id.home_title_one_layout,R.id.home_title_two_layout,R.id.home_title_three_layout,R.id.home_title_four_layout
    ,R.id.sort_one,R.id.sort_two,R.id.sort_three,R.id.money_zero,R.id.money_one,R.id.money_two,R.id.money_three,R.id.money_four
    ,R.id.money_five,R.id.age_zero,R.id.age_one,R.id.age_two,R.id.age_three,R.id.age_four,R.id.age_five,R.id.age_six,R.id.age_seven
    ,R.id.custom_age_filter,R.id.custom_money_filter,R.id.ll_carg})
     public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.home_title_one_layout:
                sortPanel(0);
                break;
            case R.id.home_title_two_layout:
                sortPanel(1);
                break;
            case R.id.home_title_three_layout:
                sortPanel(2);
                break;
            case R.id.home_title_four_layout:
                sortPanel(3);
                break;
            case R.id.ll_carg://车规
                sortPanel(4);
                break;
            case R.id.sort_one:
            case R.id.sort_two:
            case R.id.sort_three:
                isRecommend="";
                setSortMode(v);
                break;
            case R.id.money_zero:
            case R.id.money_one:
            case R.id.money_two:
            case R.id.money_three:
            case R.id.money_four:
            case R.id.money_five:
                isRecommend="";
                setMoneyData(v);
                //清空自定义数据
                customMoneyOne.setText("");
                customMoneyTwo.setText("");
                break;

            case R.id.age_zero:
            case R.id.age_one:
            case R.id.age_two:
            case R.id.age_three:
            case R.id.age_four:
            case R.id.age_five:
            case R.id.age_six:
            case R.id.age_seven:
                isRecommend="";
                setAgeData(v);
                //清空自定义数据
                customAgeOne.setText("");
                customAgeTwo.setText("");
                break;

            case R.id.custom_money_filter:
                isRecommend="";
                customFilter();
                break;


            case R.id.custom_age_filter:
                isRecommend="";
                customAgeFilter();
                break;



        }
    }

    private void customFilter() {
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
//        selMoneyColorList.clear();
//        moneyZero.setTextColor(getResources().getColor(R.color.orange_2));
//        moneyZero.setBackgroundResource(R.drawable.select_price);
//        selMoneyColorList.add(moneyZero);

        String min=customMoneyOne.getText().toString().trim();
        String max=customMoneyTwo.getText().toString().trim();
        if(min.isEmpty()||max.isEmpty()) {
            ToastTool.show(getActivity(), "自定义价格不能为空！");
        }
        else if(min.equals("0")&&max.equals("0")) {
            ToastTool.show(getActivity(), "自定义价格不能全为0！");
        }
        else if(max.equals("0")) {
            ToastTool.show(getActivity(), "自定义价格不能为0！");
        }
        else if(Double.valueOf(min)>= Double.valueOf(max)) {
            ToastTool.show(getActivity(), "自定义第二个价格不能小于等于第一个价格！");
        } else {
            minPrice = min;
            maxPrice = max;
            String text = min + "-" + max + "万";
            addLableEntity("", text, "3");
            moneyFilter=false;
            moneyLayout.setVisibility(View.GONE);
            tvThree.setTextColor(getResources().getColor(R.color.black_1));
            ivThree.setImageResource(R.mipmap.back_xb);
            onRefresh();
        }
    }

    private void customAgeFilter() {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.black_1));
        }
        selMoneyColorList.clear();

        minAge=customAgeOne.getText().toString().trim();
        maxAge=customAgeTwo.getText().toString().trim();
        if (TextUtils.isEmpty(minAge) || TextUtils.isEmpty(maxAge)){
            ToastTool.show(getActivity(),"自定义车龄不能为空！");
            return;
        }else {
            int iMinAge=Integer.parseInt(minAge);
            int iMaxAge=Integer.parseInt(maxAge);
            if(iMinAge>=iMaxAge) {
                ToastTool.show(getActivity(), "自定义第二个车龄不能小于等于第一个车龄的值！");
                return;
            }
        }

        String text=minAge+"-"+maxAge+"年";
        addLableEntity("",text,"4");
        ageFilter=false;
        ageLayout.setVisibility(View.GONE);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        onRefresh();
    }

    private void setAgeData(View v) {
        if(selAgeColorList.size()>0) {
            ((TextView) selAgeColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
        }
        selAgeColorList.clear();
        selAgeColorList.add(v);
        TextView textView= (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        String text=textView.getText().toString().trim();
        String tag=String.valueOf(textView.getTag());
        if(tag.equals("0")){
            maxAge = "";
            minAge = "";
        }else if(tag.equals("1-in")){
            maxAge = "1";
            minAge = "";
        }else if(tag.equals("2-in")){
            maxAge = "2";
            minAge = "";
        }else if(tag.equals("3-in")){
            maxAge = "3";
            minAge = "";
        }else if(tag.equals("4-in")){
            maxAge = "4";
            minAge = "";
        }else if(tag.equals("5-in")){
            maxAge = "5";
            minAge = "";
        }else if(tag.equals("6-in")){
            maxAge = "6";
            minAge = "";
        }else if(tag.equals("6-on")){
            maxAge = "";
            minAge = "6";
        }

        ageFilter=false;
        ageLayout.setVisibility(View.GONE);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        if(!text.equals("不限")){
            addLableEntity("", text, "4");
        }else {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("4")) {
                        list.remove(i);
                        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                    }
                }
            }
        }
        onRefresh();

    }


    private void setMoneyData(View v) {
        if(selMoneyColorList.size()>0) {
            ((TextView) selMoneyColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((TextView) selMoneyColorList.get(0)).setBackgroundResource(R.drawable.noselect_price);
        }
        selMoneyColorList.clear();
        selMoneyColorList.add(v);
        TextView textView= (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundResource(R.drawable.select_price);
        String text=textView.getText().toString().trim();
        String moneyMode=String.valueOf(textView.getTag());
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if(moneyMode.equals("0")){
            minPrice="";
            maxPrice="";
        }else if(moneyMode.equals("1")){
            minPrice="0";
            maxPrice="30";
        }else if(moneyMode.equals("2")){
            minPrice="30";
            maxPrice="50";
        }else if(moneyMode.equals("3")){
            minPrice="50";
            maxPrice="70";
        }else if(moneyMode.equals("4")){
            minPrice="70";
            maxPrice="100";
        }else if(moneyMode.equals("5")){
            minPrice="100";
            maxPrice="";
        }
        moneyFilter=false;
        moneyLayout.setVisibility(View.GONE);
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        if(!text.equals("不限")){
            addLableEntity("", text, "3");
        }else {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    LableEntity lableEntity = list.get(i);
                    if (lableEntity.getType().equals("3")) {
                        list.remove(i);
                        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
                    }
                }
            }
        }
        onRefresh();
    }

    private void setSortMode(View v) {
        if(selOrderColorList.size()>0) {
            ((TextView) selOrderColorList.get(0)).setTextColor(getResources().getColor(R.color.gray_3));
            ((View)selOrderColorList.get(0)).setBackgroundColor(getResources().getColor(R.color.white));
            ((TextView) selOrderColorList.get(0)).setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

        }
        selOrderColorList.clear();
        selOrderColorList.add(v);

        TextView textView= (TextView) v;
        textView.setTextColor(getResources().getColor(R.color.orange_2));
        textView.setBackgroundColor(getResources().getColor(R.color.gray_2));
        Drawable drawableright = getResources().getDrawable(
                R.mipmap.city_checked);
        textView.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableright,null);
        textView.setPadding(GeneralUtils.dip2px(getActivity(),15),0,GeneralUtils.dip2px(getActivity(),15),0);

        sotrMode=String.valueOf(textView.getTag());
        tvOne.setText(textView.getText().toString().trim());
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        sortLayout.setVisibility(View.GONE);
        sotrFilter=false;

        isRecommend="";
        onRefresh();
    }

    int BRANDREQUESTCODE = 10101;//品牌
    private void sortPanel(int i) {
        tvOne.setTextColor(getResources().getColor(R.color.black_1));
        ivOne.setImageResource(R.mipmap.back_xb);
        tvThree.setTextColor(getResources().getColor(R.color.black_1));
        ivThree.setImageResource(R.mipmap.back_xb);
        tvFour.setTextColor(getResources().getColor(R.color.black_1));
        ivFour.setImageResource(R.mipmap.back_xb);
        tv_carg.setTextColor(getResources().getColor(R.color.black_1));
        iv_carg.setImageResource(R.mipmap.back_xb);
        sortLayout.setVisibility(View.GONE);
        moneyLayout.setVisibility(View.GONE);
        ageLayout.setVisibility(View.GONE);
        home_carg_layout.setVisibility(View.GONE);
//        pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
        switch (i){
            case 0:
                moneyFilter=false;
                ageFilter=false;
                carrulesFilter=false;
                if(!sotrFilter) {
                    sotrFilter=true;
                    tvOne.setTextColor(getResources().getColor(R.color.orange_2));
                    ivOne.setImageResource(R.mipmap.sel_subscript);
                    sortLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    sotrFilter=false;
                    tvOne.setTextColor(getResources().getColor(R.color.black_1));
                    ivOne.setImageResource(R.mipmap.back_xb);
                    sortLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;
            case 1:
                sotrFilter=false;
                moneyFilter=false;
                ageFilter=false;
                carrulesFilter=false;
                startActivityForResult(new Intent(getActivity(), NewBrandAct.class).putExtra("type", 1), BRANDREQUESTCODE);
                pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
                break;
            case 2:
                sotrFilter=false;
                ageFilter=false;
                carrulesFilter=false;
                if(!moneyFilter) {
                    moneyFilter=true;
                    tvThree.setTextColor(getResources().getColor(R.color.orange_2));
                    ivThree.setImageResource(R.mipmap.sel_subscript);
                    moneyLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    moneyFilter=false;
                    tvThree.setTextColor(getResources().getColor(R.color.black_1));
                    ivThree.setImageResource(R.mipmap.back_xb);
                    moneyLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;

            case 3:
                sotrFilter=false;
                moneyFilter=false;
                carrulesFilter=false;
                if(!ageFilter) {
                    ageFilter=true;
                    tvFour.setTextColor(getResources().getColor(R.color.orange_2));
                    ivFour.setImageResource(R.mipmap.sel_subscript);
                    ageLayout.setVisibility(View.VISIBLE);
                    setPullEnable();
                } else {
                    ageFilter=false;
                    tvFour.setTextColor(getResources().getColor(R.color.black_1));
                    ivFour.setImageResource(R.mipmap.back_xb);
                    ageLayout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;

            case 4://车规
                sotrFilter=false;
                moneyFilter=false;
                ageFilter=false;
                if(!carrulesFilter) {
                    carrulesFilter=true;
                    tv_carg.setTextColor(getResources().getColor(R.color.orange_2));
                    iv_carg.setImageResource(R.mipmap.sel_subscript);
                    home_carg_layout.setVisibility(View.VISIBLE);
                    getStandardList();//获取车规定列表
                    setPullEnable();
                } else {
                    carrulesFilter=false;
                    tv_carg.setTextColor(getResources().getColor(R.color.black_1));
                    iv_carg.setImageResource(R.mipmap.back_xb);
                    home_carg_layout.setVisibility(View.GONE);
                    setPullEnable();
                }
                break;
        }

    }

    /**
     * 获取车规
     */
    private void getStandardList() {
        RequestParam param=new RequestParam();
        OkHttpUtils.post(Config.STANDARDLIST_URL, param, new OkHttpCallBack(mActivity,"") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONArray array=object.getJSONArray("result");
                    stanlist=gson.fromJson(array.toString(),new TypeToken<List<StandarBean>>(){}.getType());
                    if(stanlist.size()>0){
                        StandarBean bean=new StandarBean();
                        bean.setVehicleStandard("");
                        bean.setVehicleStandardName("全部规格");
                        stanlist.add(0,bean);
                        standaradpter=new StandarAdapter(mActivity,stanlist,tv_carg.getText().toString());
                        lv_carg.setAdapter(standaradpter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
                carrulesFilter=false;
                tv_carg.setTextColor(getResources().getColor(R.color.black_1));
                iv_carg.setImageResource(R.mipmap.back_xb);
                home_carg_layout.setVisibility(View.GONE);
                setPullEnable();
            }
        });
    }

    /**
     * 设置下拉刷新是否可以点击item因为弹出的选择条件布局挡不住
     */
    private void setPullEnable() {
        if(sortLayout.getVisibility()==View.GONE&&moneyLayout.getVisibility()==View.GONE&&ageLayout.getVisibility()==View.GONE&&home_carg_layout.getVisibility()==View.GONE){
            pullToRefreshRecyclerView.setVisibility(View.VISIBLE);
        }else {
            pullToRefreshRecyclerView.setVisibility(View.GONE);

        }
    }


    //type- 1:品牌，3-价格，4--车龄,5--车系（只有从搜索框里面来的）只要筛选了条件就不要推荐
    private void addLableEntity(String id,String text,String type)
    {
        LableEntity lableEntity=new LableEntity();
        lableEntity.setId(id);
        lableEntity.setLable(text);
        lableEntity.setType(type);
        if(list.size()>0) {
            for (int i=0;i<list.size();i++)
            {
                LableEntity entity=list.get(i);
                if(entity.getType().equals(type))
                    list.remove(i);
            }

        }
        list.add(lableEntity);
        lable.removeAllViews();
        lable.initData(list,SpUtils.getScreenWidth(getActivity()));

        isRecommend="";//只要筛选了条件就不要推荐
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == BRANDREQUESTCODE) {

                SortModel brand = (SortModel) data.getSerializableExtra("sortmodel");
                brandId = brand.getBrandId();
                if (!brand.getName().equals("不限品牌")) {
                    addLableEntity(brand.getBrandId(), brand.getName(), "1");
                }else {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LableEntity lableEntity = list.get(i);
                            if (lableEntity.getType().equals("1")) {
                                list.remove(i);
                                lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                            }
                        }
                    }
                }


                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        LableEntity lableEntity = list.get(i);
                        if (lableEntity.getType().equals("5")) {
                            list.remove(i);
                            lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                        }
                    }
                }

                onRefresh();


            }
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){

        }else {
            gpsCity=SpUtils.getCityid(getActivity());
            onRefresh();
        }
    }

    public void upDateUi(){
//        gpsCity=SpUtils.getLoacationCity(getActivity());
        gpsCity=SpUtils.getCityid(getActivity());
        onRefresh();
    }

    private String onlybrandid="";
    private String onlycxid="";

    /**
     * 从搜索也过来带车系的
     * @param entity
     */
    public void upTable(SearchEntity entity) {//从外面送进来的品牌
      if(entity!=null){
          if(!TextUtils.isEmpty(entity.getBrandName())){
              addLableEntity(entity.getSearchId(), entity.getBrandName(), "1");
              onlybrandid=entity.getBrandId();
          }else {
              if (list.size() > 0) {
                  for (int i = 0; i < list.size(); i++) {
                      LableEntity lableEntity = list.get(i);
                      if (lableEntity.getType().equals("1")) {
                          list.remove(i);
                          lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                      }
                  }
              }
          }

          if(!TextUtils.isEmpty(entity.getSeriesName())){
              addLableEntity(entity.getSeriesId(), entity.getSeriesName(), "5");
              onlycxid=","+entity.getSeriesId();
//               onlycxid=entity.getSeriesId();
          }else {
              if (list.size() > 0) {
                  for (int i = 0; i < list.size(); i++) {
                      LableEntity lableEntity = list.get(i);
                      if (lableEntity.getType().equals("5")) {
                          list.remove(i);
                          lable.initData(list,SpUtils.getScreenWidth(getActivity()));
                      }
                  }
              }
          }

          if(!TextUtils.isEmpty(entity.getSearchId())){
              brandId=entity.getSearchId();
          }

          onRefresh();
      }
    }

    /**
     * 从推荐直接进来所有已经的筛选条件都关掉
     */
    public void upDateIsRec() {
        list.clear();
        lable.initData(list, SpUtils.getScreenWidth(getActivity()));
        minPrice="";
        maxPrice="";
        brandId="";
        minAge="";
        maxAge="";
//        isRecommend="1";
        isRecommend="";
        onRefresh();
    }
}
