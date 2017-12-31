package com.example.vrplayer;
import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class LocalVideoActivity extends Activity implements OnItemClickListener {
    private ListView listView;
    private Cursor cursor;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);

        listView = (ListView) findViewById(R.id.local_video_list);
        /**
         * 从MediaStore.Video.Thumbnail查询中获得的列的列表。
         */
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };
        /**
         * 从MediaStore.Video.Media查询中获得的列的列表。
         */
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };
        /**
         * 在主查询中将选择所有在MediaStore中表示的视频
         */
        cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);
        ArrayList<VideoInfo> videoRows = new ArrayList<VideoInfo>();
        if (cursor.moveToFirst()) {
            do {
                VideoInfo newVVI = new VideoInfo();
                /**
                 * 将使用另一个查询为每个视频提取缩略图，而且这些数据块都将存储在VideoViewInfo对象中。
                 */
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = managedQuery(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    newVVI.thumbPath = thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                }
                newVVI.filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                newVVI.title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                newVVI.mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));

                videoRows.add(newVVI);
            } while (cursor.moveToNext());
        }
        listView.setAdapter(new VideoAdapter(this, videoRows));
        listView.setOnItemClickListener(this);
    }

    /**
     * 这个方法将从Cursor对象中提取所需的数据，点击一个item，将创建一个Intent，以启动播放界面
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (cursor.moveToPosition(position)) {
            int fileColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int mimeColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
            String videoFilePath = cursor.getString(fileColumn);
            String mimeType = cursor.getString(mimeColumn);
            String videopath = videoFilePath.toString();

            Intent intent = new Intent(LocalVideoActivity.this,LocalVideoActivityPlayer.class);
            intent.putExtra("VIDEOURI",videopath);
            startActivity(intent);
        }
    }
}
