package com.kesetel.mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.kesetel.mobile.helper.HttpHelper;
import com.kesetel.mobile.helper.LogHelper;
import com.kesetel.mobile.droidplugin.PluginApplication;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;


/**
 * Created by Administrator on 2017/4/24.
 */

public class MyApplictaion extends PluginApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DDDD", "MyApplictaion create.");
        LogHelper.init(getAppContext());
    }

    private void initConfig() {
        initImageLoader();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = this;

        new Thread(){
            @Override
            public void run() {
                HttpHelper.init(context);
                initConfig();
            }
        }.start();
    }

    public static Context getAppContext() {
        return context;
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()// 开始构建, 显示的图片的各种格式
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .cacheInMemory(true)// 开启内存缓存
                .cacheOnDisk(true) // 开启硬盘缓存
//                .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少；避免使用RoundedBitmapDisplayer.他会创建新的ARGB_8888格式的Bitmap对象；
//                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .displayer(new SimpleBitmapDisplayer())// 正常显示一张图片　
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型;使用.bitmapConfig(Bitmap.config.RGB_565)代替ARGB_8888;
//                .considerExifParams(true)// 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY)// 缩放级别
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//这两种配置缩放都推荐
                .build();// 构建完成（参数可以不用设置全，根据需要来配置）

        String imageCachePath = context.getExternalCacheDir()+"/image";
        File file = new File(imageCachePath);
        if(!file.exists())
            file.mkdir();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)// 开始构建 ,图片加载配置
                .threadPriority(Thread.NORM_PRIORITY - 2)// 设置线程优先级
                .threadPoolSize(3)// 线程池内加载的数量 ;减少配置之中线程池的大小，(.threadPoolSize).推荐1-5；
                .denyCacheImageMultipleSizesInMemory()// 设置加载的图片有多样的
                .tasksProcessingOrder(QueueProcessingType.LIFO)// 图片加载任务顺序
                .memoryCache(new WeakMemoryCache())//使用.memoryCache(new WeakMemoryCache())，不要使用.cacheInMemory();
                .memoryCacheExtraOptions(480, 800) // 即保存的每个缓存文件的最大长宽
                .memoryCacheSizePercentage(50)// 图片内存占应用的60%；
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())//使用HASHCODE对UIL进行加密命名
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5 加密
                .diskCacheSize(30 * 1024 * 1024) // 缓存设置大小为30 Mb
                .diskCache(new UnlimitedDiskCache(file))// 自定义缓存路径
                .diskCacheFileCount(100) // 缓存的文件数量
                .denyCacheImageMultipleSizesInMemory()// 自动缩放
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .memoryCacheExtraOptions(480, 800)//设置缓存图片时候的宽高最大值，默认为屏幕宽高;保存的每个缓存文件的最大长宽
                .defaultDisplayImageOptions(options)// 如果需要打开缓存机制，需要自己builde一个option,可以是DisplayImageOptions.createSimple()
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }
}
