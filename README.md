## YCBanner轮播图
- 主要引导界面滑动导航 + 大于1页时无限轮播 + 自定义指示器
- 项目地址：https://github.com/yangchong211/YCBanner



### 目录介绍
- 1.功能说明
- 2.轮播图使用说明
- 3.跑马灯使用说明
- 4.图片展示
- 5.其他介绍


### 1.功能说明
- 1.1 自定义轮播图，可以设置轮播红点或者轮播数字，多种指示器，并且灵活设置位置
- 1.2 支持多种轮播图适配器，无限轮播adapter，静态管理adapter，和动态管理adapter。支持多种场合使用。
- 1.3 支持自定义hintView，十分灵活，拓展性强
- 1.4 无限循环自动轮播、手指按下暂停轮播、抬起手指开始轮播
- 1.5 优化：在页面onPause中调用停止轮播，在页面onResume中调用开始轮播
- 1.6 支持监听item点击事件，支持轮播图中ViewPager的滑动监听事件
- 1.7 不仅支持轮播图，还支持引导页面，十分方便


### 2.使用说明
- 2.1 直接在项目build文件中添加库即可：compile 'cn.yc:YCBannerLib:1.3.6'
- 关于具体的使用方法，可以直接参考代码
- 2.2 在布局中写，可以设置选择的属性值

```
<com.yc.cn.ycbannerlib.BannerView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    app:hint_color="@color/colorAccent"
    app:hint_gravity="center"
    app:hint_mode="point"
    app:play_delay="2000"/>
```


- 2.3 在代码中，自定义adapter适配器，继承自己合适的adapter。目前支持继承动态管理adapter，静态管理adapter，和无限轮播adapter

```
private void initBanner() {
    banner = (BannerView) findViewById(R.id.banner);
    //设置轮播时间
    banner.setPlayDelay(2000);
    //设置轮播图适配器，必须
    banner.setAdapter(new ImageNormalAdapter());
    //设置位置
    banner.setHintGravity(1);
    //设置指示器样式
    banner.setHintMode(BannerView.HintMode.TEXT_HINT);
    //判断轮播是否进行
    boolean playing = banner.isPlaying();
    //轮播图点击事件
    banner.setOnBannerClickListener(new OnBannerClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(FirstActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
        }
    });
    //轮播图滑动事件
    banner.setOnPageListener(new OnPageListener() {
        @Override
        public void onPageChange(int position) {

        }
    });
}


private class ImageNormalAdapter extends AbsStaticPagerAdapter {

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        view.setImageResource(imgs[position]);
        return view;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }
}
```


- **2.4 关于轮播图属性**
- 关于暂停和开始轮播方法，建议加上

```
@Override
protected void onPause() {
    super.onPause();
    if(banner!=null){
        //停止轮播
        banner.pause();
    }
}

@Override
protected void onResume() {
    super.onResume();
    if(banner!=null){
        //开始轮播
        banner.resume();
    }
}
```


- **2.5 如何显示红点，文字，自定义icon等**
- **a.设置文本添加代码**：
- banner.setHintView(new TextHintView(this));
- **b.设置自定义icon添加代码：**
-  banner.setHintView(new IconHintView(this,R.drawable.point_focus,R.drawable.point_normal));
-  **c.默认不添加该方法则是显示圆点**
- 请参考代码，已经做出了很详细的注释


### 3.跑马灯使用说明
#### 3.1 在布局中添加代码
```
<!--上下滚动TextView-->
<com.yc.cn.ycbannerlib.marquee.MarqueeView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/marqueeView"
    android:layout_width="match_parent"
    android:layout_marginTop="10dp"
    android:layout_height="40dp"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp"
    android:layout_gravity="center_vertical"
    app:paddingStart="0dp"
    app:mvAnimDuration="1000"
    app:mvInterval="2000"
    app:mvTextColor="#464C4E"
    app:mvTextSize="16sp"
    app:mvSingleLine="true"/>
```

#### 3.2 使用
```
List<CharSequence> list = getMarqueeTitle();
//根据公告字符串列表启动轮播
marqueeView.startWithList(list);
//设置点击事件
marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
    @Override
    public void onItemClick(int position, TextView textView) {

    }
});
```


## 4.图片展示
- 4.1 轮播图截图
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/1.png)
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/2.png)


## 5.其他介绍
**5.1版本更新说明**
- v1.0 16年3月23日，新芽轮播图，最简单的轮播图，无限轮播。
- v1.1 5月2日  添加了动态管理adapter，和静态管理adapter，模拟多种场景轮播图
- v1.2 6月12日 添加了触摸轮播图时暂停，松开手指开始轮播的功能；如果设置轮播图控件宽高都是wrap_content，那么则默认宽是match_parent，高是200dp。修改了handler内存泄漏
- v1.3 17年8月22日 添加了ViewPager滑动监听接口，可以作用于引导页，十分简单
- v1.3.2 17年10月21日 添加跑马灯轮播到该lib库中
- v1.3.6 18年9月15日 同行提议更新API方法说明


#### 关于其他内容介绍
![image](https://upload-images.jianshu.io/upload_images/4432347-7100c8e5a455c3ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)




#### 于LICENSE
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

