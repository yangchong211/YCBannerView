## YCBanner轮播图
- 主要引导界面滑动导航 + 大于1页时无限轮播 + 自定义指示器


### 目录介绍
- 1.功能说明
- 2.使用说明
- 3.图片展示
- 4.其他介绍


### 1.功能说明
- 1.1 自定义轮播图，可以设置轮播红点或者轮播数字，多种指示器，并且灵活设置位置
- 1.2 支持多种轮播图适配器，无限轮播adapter，静态管理adapter，和动态管理adapter。支持多种场合使用。
- 1.3 支持自定义hintView，十分灵活，拓展性强
- 1.4 无限循环自动轮播、手指按下暂停轮播、抬起手指开始轮播
- 1.5 优化：在页面onPause中调用停止轮播，在页面onResume中调用开始轮播
- 1.6 支持监听item点击事件，支持轮播图中ViewPager的滑动监听事件
- 1.7 不仅支持轮播图，还支持引导页面，十分方便


### 2.使用说明
- 2.1 直接在项目build文件中添加库即可：compile 'cn.yc:YCBannerLib:1.3'
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
    banner.setAnimationDuration(1000);
    banner.setHintPadding(0, SizeUtil.dip2px(this,10f),
            SizeUtil.dip2px(this,10f),SizeUtil.dip2px(this,10f));
    banner.setPlayDelay(2000);
    banner.setHintView(new TextHintView(this));
    banner.setAdapter(new ImageNormalAdapter());
    banner.setOnBannerClickListener(new OnBannerClickListener() {
        @Override
        public void onItemClick(int position) {
            Toast.makeText(FourActivity.this,
                    position+"被点击呢",Toast.LENGTH_SHORT).show();
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
- 2.4 关于轮播图属性
- 关于暂停和开始轮播方法，建议加上
```
@Override
protected void onPause() {
    super.onPause();
    if(banner!=null){
        banner.pause();
    }
}

@Override
protected void onResume() {
    super.onResume();
    if(banner!=null){
        banner.resume();
    }
}
```
- 请参考代码，已经做出了很详细的注释



## 3.图片展示
- 3.1 轮播图截图
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/1.png)
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/2.png)

## 4.其他介绍
**4.1版本更新说明**
- v1.0 最简单的轮播图，无限轮播
- v1.1 9月2日  添加了轮播图点击事件，添加了动态管理adapter，和静态管理adapter，模拟多种场景轮播图
- v1.2 12月12日 添加了暂停，开始轮播的功能；如果设置轮播图控件宽高都是wrap_content，那么则默认宽是match_parent，高是200dp。修改了handler内存泄漏
- v1.3 18年3月22日
- 添加了ViewPager滑动监听接口，可以作用于引导页，十分简单


**4.3其他**
-  **开源项目说明**
> **开源综合案例合集**
>
- **如果你感觉还行，请给一个star，如果你觉得哪里有问题，也可以直接把问题提给我，我会修改的。业余的小案例，定期更新，持续更新**
- 代码地址：https://github.com/yangchong211/LifeHelper
- [说明及截图](https://github.com/yangchong211/LifeHelper/blob/master/README.md)
- 模块：新闻，音乐，视频，图片，唐诗宋词，快递，天气，记事本，阅读器等等
- 接口：七牛，阿里云，天行，干货集中营，极速数据，追书神器等等
- 架构：采用MVP+Rx+Retrofit+Desgin+Dagger2+阿里VLayout+腾讯X5等架构模式。




>  **开源视频播放器封装库**
>
- **视频播放器封装库案例，仿照优酷，爱奇艺视频播放器，可以添家视频观看权限，试看模式，类似优酷试看功能。基于ijkplayer，支持网络视频或者本地视频播放，滑动调节亮度或者音量，快进快退，记录播放位置。可以设置边观看变缓存，支持全屏播放，小窗口，正常播放等模式；还支持列表播放，切换分辨率，还可以自定义视频播放器，拓展性强**
- 代码地址：https://github.com/yangchong211/YCVideoPlayer
- [说明及截图](https://github.com/yangchong211/YCVideoPlayer/blob/master/README.md)
- 具体详细的开发说明文档，可以直接查看上面链接说明




>  **开源状态切换管理器封装库**
>
- **状态切换，让View状态的切换和Activity彻底分离开。用builder模式来自由的添加需要的状态View，可以设置有数据，数据为空，加载数据错误，网络错误，加载中等多种状态，并且支持自定义状态的布局。。目前已经用于新芽正式项目中，拓展性强！！**
- 代码地址：https://github.com/yangchong211/YCStateLayout
- [说明及截图](https://github.com/yangchong211/YCStateLayout/blob/master/README.md)
- 具体详细的开发说明文档，可以直接查看上面链接说明



>  **开源状态切换管理器封装库**
>
- **状态切换，让View状态的切换和Activity彻底分离开。用builder模式来自由的添加需要的状态View，可以设置有数据，数据为空，加载数据错误，网络错误，加载中等多种状态，并且支持自定义状态的布局。。目前已经用于新芽正式项目中，拓展性强！！**
- 代码地址：https://github.com/yangchong211/YCStateLayout
- [说明及截图](https://github.com/yangchong211/YCStateLayout/blob/master/README.md)
- 具体详细的开发说明文档，可以直接查看上面链接说明


>  **开源自定义对话框封装库**
>
- **自定义对话框，其中包括：仿ios底部弹窗；自定义Toast；自定义DialogFragment弹窗(功能很强大)，自定义PopupWindow弹窗【采用builder模式，可以自定义位置，背景，自定义布局(支持嵌套recyclerView)等等，拓展性强】，目前已经用于新芽和投资界正式项目中。！**
- 代码地址：https://github.com/yangchong211/YCDialog
- [说明及截图](https://github.com/yangchong211/YCDialog/blob/master/README.md)
- 具体详细的开发说明文档，可以直接查看上面链接说明


>  **开源复合类型封装库**
>
- **自定义支持上拉加载更多，下拉刷新，可以自定义头部和底部，可以添加多个headAdapter，使用一个原生recyclerView就可以搞定复杂界面。支持自由切换状态【加载中，加载成功，加载失败，没网络等状态】的控件，可以自定义状态视图View。！**
- 代码地址：https://github.com/yangchong211/YCRefreshView
- [说明及截图](https://github.com/yangchong211/YCRefreshView/blob/master/README.md)
- 具体详细的开发说明文档，可以直接查看上面链接说明

