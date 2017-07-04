package com.example.gaoshuang20170630;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.List;

/**
 * name:高爽
 * date:2017/6/30.
 * desc:
 */
public class MyAdapter extends BaseAdapter {
    private String urlpath = "http://qhb.2dyt.com/Bwei/news?page=1&type=5&postkey=1503d";
    private Context context;
    private List<Data.ListBean> list;
    private int TYPE0=0;
    private int TYPE1=1;
    private final DisplayImageOptions options;
    private final ImageLoader imageloader;

    public MyAdapter(Context context, List<Data.ListBean> list) {
        this.context = context;
        this.list = list;
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
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getPic().startsWith("http")?TYPE0:TYPE1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type=getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            if (type==TYPE0){
                convertView = View.inflate(context,R.layout.item1, null);
                holder.image1 = (ImageView) convertView.findViewById(R.id.image1);
                holder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            }
            if (type==TYPE1){
                convertView = View.inflate(context,R.layout.item2, null);
                holder.image1 = (ImageView) convertView.findViewById(R.id.image1);
                holder.image2 = (ImageView) convertView.findViewById(R.id.image2);
                holder.image3 = (ImageView) convertView.findViewById(R.id.image3);
                holder.image4 = (ImageView) convertView.findViewById(R.id.image4);
            }
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            Data.ListBean data=list.get(position);
            holder.title.setText(data.getTitle());
        }
        if (type==TYPE0){
            String[] str=list.get(position).getPic().split("\\|");
            imageloader.displayImage(str[0],holder.image1,options);
            imageloader.displayImage(str[1],holder.image2,options);
        }
        if (type==TYPE1){
            String[] str=list.get(position).getPic().split("\\|");
            imageloader.displayImage(str[0],holder.image1,options);
            imageloader.displayImage(str[1],holder.image2,options);
            imageloader.displayImage(str[2],holder.image3,options);
            imageloader.displayImage(str[3],holder.image4,options);
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView image1,image2,image3,image4;
    }
}
