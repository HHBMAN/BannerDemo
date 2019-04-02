package com.tsao.yvan.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.tsao.yvan.retrofittest.API.ZhihuApi;
import com.tsao.yvan.retrofittest.model.Story;
import com.tsao.yvan.retrofittest.model.TopStory;
import com.tsao.yvan.retrofittest.model.Zhihu;

import java.util.List;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://news-at.zhihu.com/";

    ConvenientBanner convenientBanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convenientBanner = (ConvenientBanner)findViewById(R.id.convenientBanner);

        Retrofit retrofit = new Retrofit.Builder()//获取Retrofit对象
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//采用链式结构绑定Base url
                .build();//执行操作
        //获取API接口的实现类的实例对象
        ZhihuApi service = retrofit.create(ZhihuApi.class);
        Call<Zhihu> call =  service.getZhihuNewsLatest();
        call.enqueue(new Callback<Zhihu>() {
            @Override
            public void onResponse(Call<Zhihu> call, Response<Zhihu> response) {

                List<Story> storiesList = response.body().getStories();

                List<TopStory> top_storiesList = response.body().getTopStories();

                //将相关数据显示在轮播控件上
                convenientBanner.setPages(new CBViewHolderCreator<BannerHolderView>() {
                    @Override
                    public BannerHolderView createHolder() {
                        return new BannerHolderView();
                    }
                },top_storiesList)
                .setPageIndicator(new int[]{R.mipmap.dot_blur,R.mipmap.dot_focus})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
                convenientBanner.setScrollDuration(1000);
                convenientBanner.startTurning(5000);

            }

            @Override
            public void onFailure(Call<Zhihu> call, Throwable t) {
                System.out.println("onFailure========================================");
            }
        });




    }


}
