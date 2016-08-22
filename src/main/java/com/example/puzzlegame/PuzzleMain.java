package com.example.puzzlegame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by liuxian on 2016/8/21.
 */

public class PuzzleMain extends AppCompatActivity implements View.OnClickListener {

    private Bitmap picSelected;

    public static Bitmap lastBitmap;

    private RecyclerView recyclerView_detail;
    private RecyclerView.LayoutManager mLayoutManager;
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
    public static int type = 2;
    private boolean isShowImg;
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
        handlerImage(picSelectedTemp);
        //对图片的处理
        initViews();
        //初始化view
        generateGame();
        //生成游戏游戏数据
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(GameUtil.isMoveable(position)){
                    GameUtil.swapItem(GameUtil.itemBeams.get(position), GameUtil.blankItemBean);
                    recreateData();
                    adapter.notifyDataSetChanged();
                    countIndex++;
                    tv_counts.setText("" + countIndex);
                    if(GameUtil.isSuccess()){
                        recreateData();
                        bitmapItemLists.remove(type * type - 1);
                        bitmapItemLists.add(lastBitmap);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(PuzzleMain.this , "拼图成功" , Toast.LENGTH_LONG).show();
                        recyclerView_detail.setEnabled(false);
                        timer.cancel();
                        timerTask.cancel();
                    }
                }
            }
        });
        bt_back.setOnClickListener(this);
        bt_Image.setOnClickListener(this);
        bt_Restart.setOnClickListener(this);
    }
    /**
     *
     * button点击事件
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_puzzle_main_back:
                PuzzleMain.this.finish();
                break;
            case R.id.bt_puzzle_main_img:
                if(isShowImg){
                    imageView.setVisibility(View.GONE);
                    isShowImg = false;
                }else{
                    imageView.setVisibility(View.VISIBLE);
                    isShowImg = true;
                }
                break;
            case R.id.bt_puzzle_main_restart:
                cleanConfig();
                generateGame();
                recreateData();
                tv_counts.setText("" + countIndex);
                adapter.notifyDataSetChanged();
                recyclerView_detail.setEnabled(true);
                break;
            default:
                break;
        }
    }

    /**
     * 生成游戏数据
     */
    private void generateGame(){
        new ImageUtil().createInitBitmap(type , picSelected , PuzzleMain.this);
        GameUtil.getPuzzleGenerator();
        for(ItemBeam temp : GameUtil.itemBeams){
            bitmapItemLists.add(temp.getBitmap());
        }

        adapter = new RecyclerAdapter(bitmapItemLists);
        recyclerView_detail.setAdapter(adapter);
        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(timerTask , 0 , 1000);//每1000ms执行 0延迟
    }

    /**
     * 对图片进行处理 自适应大小
     */
    private void handlerImage(Bitmap bitmap){
        //将图片放大到固定的大小
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeight = ScreenUtil.getScreenSize(this).heightPixels;
        picSelected = new ImageUtil().resizeBitmap(screenWidth * 0.8f , screenHeight * 0.6f , bitmap);
    }

    /**
     * 初始化view
     */
    private void initViews(){
        //Button
        bt_back = (Button) findViewById(R.id.bt_puzzle_main_back);
        bt_Image = (Button) findViewById(R.id.bt_puzzle_main_img);
        bt_Restart = (Button) findViewById(R.id.bt_puzzle_main_restart);
        //是否显示原图
        isShowImg = false;
        //RecyclerView
        recyclerView_detail = (RecyclerView) findViewById(R.id.rec_puzzle_main_detail);
        mLayoutManager = new GridLayoutManager(PuzzleMain.this , type);
        recyclerView_detail.setLayoutManager(mLayoutManager);
        RelativeLayout.LayoutParams recycleParams = new RelativeLayout.LayoutParams(picSelected.getWidth() , picSelected.getHeight());
        recycleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        recycleParams.addRule(RelativeLayout.BELOW , R.id.ll_puzzle_main_spinner);
        recyclerView_detail.setLayoutParams(recycleParams);
        //TextView
        tv_counts = (TextView) findViewById(R.id.tv_puzzle_main_counts);
        tv_counts.setText("" + countIndex);
        tv_time = (TextView)findViewById(R.id.tv_puzzle_main_time);
        tv_time.setText("0秒");
        //添加显示原图的View
        addImageView();
    }

    private void addImageView(){
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_puzzle_main_layout);
        imageView = new ImageView(PuzzleMain.this);
        imageView.setImageBitmap(picSelected);
        int x = (int) (picSelected.getWidth() * 0.9F);
        int y = (int) (picSelected.getHeight() * 0.9F);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x,y);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);
        relativeLayout.addView(imageView);
        imageView.setVisibility(View.GONE);
    }
    /**
     * 返回时调用
     */
    protected void onStop(){
        super.onStop();
        cleanConfig();
        this.finish();
    }
    /**
     * 清空相关参数设置
     */
    private void cleanConfig(){
        GameUtil.itemBeams.clear();
        timer.cancel();
        timerTask.cancel();
        countIndex = 0;
        timeIndex = 0;
        if(picPath != null){
            File file = new File(MainActivity.TEMP_IMAGE_PATH);
            if(file.exists()){
                file.delete();
            }
        }
    }
    /**
     * 重新获取图片
     */
    private void recreateData(){
        bitmapItemLists.clear();
        for(ItemBeam temp : GameUtil.itemBeams){
            bitmapItemLists.add(temp.getBitmap());
        }
    }
    /**
     *
     */

}
