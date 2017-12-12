# YCBanner
轮播图

## 目录介绍
- 1.使用说明
- 2.功能说明
- 3.图片展示
- 4.其他介绍

## 1.使用说明
- 1.1 直接在项目build文件中添加库即可：compile 'cn.yc:YCBannerLib:1.2'
-     关于具体的使用方法，可以直接参考代码
- 1.2 在布局中写，可以设置选择的属性值
```
<com.yc.cn.ycbannerlib.first.BannerView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:hint_color="@color/colorAccent"
        app:hint_gravity="center"
        app:hint_mode="point"
        app:play_delay="2000"/>
```
- 1.3 在代码中，自定义adapter适配器，继承自己合适的adapter。目前支持继承动态管理adapter，静态管理adapter，和无限轮播adapter
```
private void initBanner() {
        banner = (BannerView) findViewById(R.id.banner);
        banner.setAdapter(new ImageNormalAdapter());
        banner.setOnBannerClickListener(new BannerView.OnBannerClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(SecondActivity.this,position+"被点击呢",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class ImageNormalAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }
```
- 1.4 关于轮播图属性
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

## 2.功能说明
- 2.1 自定义轮播图，可以设置轮播红点或者轮播数字
- 2.2 支持多种轮播图适配器，无限轮播adapter，静态管理adapter，和动态管理adapter。支持多种场合使用。
- 2.3 支持自定义hintView，十分灵活，拓展性强

## 3.图片展示
- 3.1 轮播图截图
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/1.png)
- ![image](https://github.com/yangchong211/YCBanner/blob/master/image/2.png)

## 4.其他介绍
**4.1版本更新说明**
- v1.0 最简单的轮播图，无限轮播
- v1.1 9月2日  添加了轮播图点击事件，添加了动态管理adapter，和静态管理adapter，模拟多种场景轮播图
- v1.2 12月12日 添加了暂停，开始轮播的功能；如果设置轮播图控件宽高都是wrap_content，那么则默认宽是match_parent，高是200dp。修改了handler内存泄漏

**4.2本人写的综合案例**
- [案例](https://github.com/yangchong211/LifeHelper)
- [说明及截图](https://github.com/yangchong211/LifeHelper/blob/master/README.md)
- 模块：新闻，音乐，视频，图片，唐诗宋词，快递，天气，记事本，阅读器等等
- 接口：七牛，阿里云，天行，干货集中营，极速数据，追书神器等等
- [持续更新目录说明](http://www.jianshu.com/p/53017c3fc75d)

**4.3其他**
- 知乎：https://www.zhihu.com/people/yang-chong-69-24/pins/posts
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- github：https://github.com/yangchong211
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
