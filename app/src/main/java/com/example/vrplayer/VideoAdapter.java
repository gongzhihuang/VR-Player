package com.example.vrplayer;

/**
 * Created by Administrator on 2017/6/2.
 */
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ListView适配器
 */
public class VideoAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<VideoInfo> videoRows;
    LayoutInflater inflater;
    public VideoAdapter(Context context,
                               ArrayList<VideoInfo> videoRows) {
        this.context = context;
        this.videoRows = videoRows;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return videoRows.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View videoRow = inflater.inflate(R.layout.video_listview_item, null);
        ImageView videoThumb = (ImageView) videoRow.findViewById(R.id.imageView);
        if (videoRows.get(position).thumbPath != null) {
            videoThumb.setImageURI(Uri.parse(videoRows.get(position).thumbPath));
        }
        TextView videoTitle = (TextView) videoRow.findViewById(R.id.textView);
        videoTitle.setText(videoRows.get(position).title);
        return videoRow;
    }

}
