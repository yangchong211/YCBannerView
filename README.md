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

