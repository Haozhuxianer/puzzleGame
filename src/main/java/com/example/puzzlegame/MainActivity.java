package com.example.puzzlegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.DialogPreference;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView pic_select;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;
    private int[] mResPicId;//图片资源ID
    private List<Bitmap> mPicList = new ArrayList<Bitmap>();

    private Button loacl_pic;
    private Button diffi_cho;
    private PopupWindow mPopupWindow;
    private TextView popup_item1;
    private TextView popup_item2;
    private TextView popup_item3;

    private static final int RESULT_IMAGE = 100;//返回本地图库
    private static final int RESULT_CAMARA = 200;//返回相机
    private static String TEMP_IMAGE_PATH;//Temp照片路劲
    private static final String IMAGE_TYPE = "image/*";
    private int type = 2;
    private String[] customItem = new String[]{"本地图册", "相册拍照"};
    protected int ITEM_LEFT_TO_LOAD_MORE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_start_layout);
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ttt.jpg";
        initView();
        diffi_cho = (Button) findViewById(R.id.difficulty_choice);
        diffi_cho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupShow();
            }
        });
        loacl_pic = (Button) findViewById(R.id.local_pic);
        loacl_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCustom();
            }
        });
    }

    public void initView(){
        //初始化RecyclerView
        pic_select = (RecyclerView) findViewById(R.id.pic_selected_recycler);
        int spacingInPixels = 4;
        pic_select.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        pic_select.setHasFixedSize(true);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        pic_select.setLayoutManager(mLayoutManager);
        //数据初始化
        mResPicId = new int[]{
                R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6
               ,R.drawable.pic9,R.drawable.pic12,R.drawable.pic1,R.drawable.pic2
        };
        Bitmap[] bitmaps = new Bitmap[mResPicId.length];
        for(int i = 0 ; i <bitmaps.length ; i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }
        mAdapter = new RecyclerAdapter(mPicList);
        pic_select.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this , "OK" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void popupShow(){
        View convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.popup_item , null);
        mPopupWindow = new PopupWindow(convertView, LinearLayoutCompat.LayoutParams.WRAP_CONTENT , LinearLayoutCompat.LayoutParams.WRAP_CONTENT , true);
        mPopupWindow.setContentView(convertView);
        popup_item1 = (TextView) convertView.findViewById(R.id.popup_item1);
        popup_item2 = (TextView) convertView.findViewById(R.id.popup_item2);
        popup_item3 = (TextView) convertView.findViewById(R.id.popup_item3);
        popup_item1.setOnClickListener(this);
        popup_item2.setOnClickListener(this);
        popup_item3.setOnClickListener(this);
        View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.main_start_layout , null);
        mPopupWindow.showAtLocation(rootView , Gravity.BOTTOM , 0 , 0);
    }
    public void onClick(View view){
       int id = view.getId();
        switch (id){
            case R.id.popup_item1:
                diffi_cho.setText("2 X 2");
                mPopupWindow.dismiss();
                break;
            case R.id.popup_item2:
                diffi_cho.setText("3 X 3");
                mPopupWindow.dismiss();
                break;
            case R.id.popup_item3:
                diffi_cho.setText("4 X 4");
                mPopupWindow.dismiss();
                break;
        }
    }

    private void showDialogCustom(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择： ");
        builder.setItems(customItem, new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which){
                if(0 == which){
                    //本地相册
                    Intent intent = new Intent(Intent.ACTION_PICK , null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , IMAGE_TYPE);
                    startActivityForResult(intent , RESULT_IMAGE);
                }else if( 1 == which){
                    //启动相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT , photoUri);
                    startActivityForResult(intent , RESULT_CAMARA);
                }
            }
        });
        builder.create().show();
    }
    /*
    *调用图库相机回掉方法
     */

    protected void onActivityResult(int requestCode , int resultCode , Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT_IMAGE && data != null){
                //相册
                Cursor cursor = this.getContentResolver().query(data.getData(),null,null,null,null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
            }else if (requestCode == RESULT_CAMARA){

            }
        }
    }

}
