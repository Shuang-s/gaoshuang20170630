package com.example.gaoshuang20170630;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * name:高爽
 * date:2017/6/30.
 * desc:
 */
public class BannerImageLoader extends com.youth.banner.loader.ImageLoader{
    private final DisplayImageOptions options;
    private final ImageLoader imageloader;

    public BannerImageLoader(Context context){
        File file=new File(Environment.getExternalStorageDirectory(),"image");
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .diskCache(new UnlimitedDiscCache(file))
                .build();

        //将configuration配置到imageloader中
        imageloader = ImageLoader.getInstance();
        imageloader.init(configuration);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageloader.displayImage((String) path,imageView,options);
    }
}
