package com.example.puzzlegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * Created by liuxian on 2016/8/21.
 */

public class PuzzleMain extends AppCompatActivity implements View.OnClickListener{

    private Bitmap picSelected;

    public static Bitmap lastBitmap;

    private RecyclerView recyclerView_detail;
    private int resId;
    private String picPath;
    private ImageView imageView;

    private Button bt_back;
    private Button bt_Image;
    private Button bt_Restart;

    private TextView tv_counts;
    private TextView tv_time;

    private List<Bitmap> bitmapItemLists = new ArrayList<Bitmap>();

    private RecyclerAdapter adapter;
    private static int type = 2;
    private static int countIndex = 0;
    private static int timeIndex = 0;
    private Timer timer;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    timeIndex++;
                    tv_time.setText("" + timeIndex);
                    break;
                default:
                    break;
            }
        }
    } ;

    private TimerTask timerTask;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_main);
        Bitmap picSelectedTemp;
        resId = getIntent().getExtras().getInt("picSelectedID");
        picPath = getIntent().getExtras().getString("picPath");
        if(resId != 0){
            picSelectedTemp = BitmapFactory.decodeResource(getResources() , resId);
        }else{
            picSelectedTemp = BitmapFactory.decodeFile(picPath);
        }
        type = getIntent().getExtras().getInt("type" , 2);
        handlerImage();
    }
}
