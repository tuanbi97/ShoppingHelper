package com.tuanbi97.miniproject1.ShareActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuanbi97.miniproject1.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ScrollingActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 12345;
    private static final int GALLERY_REQUEST = 12346;

    LinearLayout content;

    ImageView imageView;
    ImageButton camButton;
    ImageButton photoButton;
    ImageButton editButton;
    Dialog editDialog;
    String caption;
    String story;
    ArrayList<View> views;

    int dw, dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        dw = Resources.getSystem().getDisplayMetrics().widthPixels;
        dh = Resources.getSystem().getDisplayMetrics().heightPixels;

        content = (LinearLayout) findViewById(R.id.scrollcontent);
        views = new ArrayList<View>(0);

        initPostButton();
        initButton();
        initListener();
        initContent();
    }

    private void initContent() {
        views.add(createContent(BitmapFactory.decodeResource(getResources(), R.drawable.user1), getString(R.string.user1), getString(R.string.status1), BitmapFactory.decodeResource(getResources(), R.drawable.im1), getString(R.string.text1)));
        views.add(createContent(BitmapFactory.decodeResource(getResources(), R.drawable.user2), getString(R.string.user2), getString(R.string.status2), BitmapFactory.decodeResource(getResources(), R.drawable.im2), getString(R.string.text2)));
        views.add(createContent(BitmapFactory.decodeResource(getResources(), R.drawable.user3), getString(R.string.user3), getString(R.string.status3), BitmapFactory.decodeResource(getResources(), R.drawable.im3), getString(R.string.text3)));
        views.add(createContent(BitmapFactory.decodeResource(getResources(), R.drawable.user4), getString(R.string.user4), getString(R.string.status4), BitmapFactory.decodeResource(getResources(), R.drawable.im4), getString(R.string.text4)));
        views.add(createContent(BitmapFactory.decodeResource(getResources(), R.drawable.user5), getString(R.string.user5), getString(R.string.status5), BitmapFactory.decodeResource(getResources(), R.drawable.im5), getString(R.string.text5)));
        setContent(views);
    }

    private View createContent(Bitmap avatar, String username, String status, Bitmap img, String story) {
        View child = LayoutInflater.from(ScrollingActivity.this).inflate(R.layout.cardlayout, null);
        ((ImageView)child.findViewById(R.id.avatar)).setImageBitmap(avatar);
        ((TextView)child.findViewById(R.id.username)).setText(username);
        ((TextView)child.findViewById(R.id.status)).setText(status);
        ((TextView)child.findViewById(R.id.story)).setText(story);
        ((ImageView)child.findViewById(R.id.image)).setImageBitmap(img);
        return child;
    }

    private void initListener() {
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent cam
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set intent gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST);
            }
        });

        editDialog = new Dialog(this);
        editDialog.setContentView(R.layout.editdialog);
        editDialog.setTitle("Information");
        ((ImageView) editDialog.findViewById(R.id.ava)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tuanava));
        final Button editbtn = (Button) editDialog.findViewById(R.id.ok);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption = ((EditText)editDialog.findViewById(R.id.caption)).getText().toString();
                story = ((EditText)editDialog.findViewById(R.id.brief)).getText().toString();
                editDialog.dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open dialog
                //((EditText) editDialog.findViewById(R.id.caption)).setText("");
                //((EditText) editDialog.findViewById(R.id.brief)).setText("");
                editDialog.show();
            }
        });
    }

    private void initButton() {
        imageView = (ImageView) findViewById(R.id.imageview);
        camButton = (ImageButton) findViewById(R.id.fromcamera);
        photoButton = (ImageButton) findViewById(R.id.fromphoto);
        editButton = (ImageButton) findViewById(R.id.setcontent);
    }

    private void initPostButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View child = LayoutInflater.from(ScrollingActivity.this).inflate(R.layout.cardlayout, null);
                ((ImageView)child.findViewById(R.id.avatar)).setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tuanava));
                ((TextView)child.findViewById(R.id.username)).setText("tuanbi97");
                ((TextView)child.findViewById(R.id.status)).setText(caption);
                ((TextView)child.findViewById(R.id.story)).setText(story);
                ((ImageView)child.findViewById(R.id.image)).setImageBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                views.add(child);
                setContent(views);
                Snackbar.make(view, "Post Success", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
                Bitmap photo = null;
                try {
                    stream = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                photo = BitmapFactory.decodeStream(stream);
                if (photo.getWidth() > dw){
                    photo = Bitmap.createScaledBitmap(photo, dw, (int)(((double)dw)/((double)photo.getWidth())*photo.getHeight()), false);
                }
                if (photo.getHeight() > dh){
                    photo = Bitmap.createScaledBitmap(photo, (int)(((double)dh)/((double)photo.getHeight())*photo.getWidth()), dh, false);
                }
                imageView.setImageBitmap(photo);
            }
            if (requestCode == GALLERY_REQUEST){
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap photo = BitmapFactory.decodeStream(imageStream);
                while (photo.getWidth() > dw){
                    photo = Bitmap.createScaledBitmap(photo, dw, photo.getHeight()* dw/photo.getWidth(), false);
                }
                imageView.setImageBitmap(photo);
            }
        }
    }

    public void setContent(ArrayList<View> views) {
        content.removeAllViews();
        for (int i = views.size() - 1; i >= 0; i--){
            content.addView(views.get(i));
        }
    }
}
