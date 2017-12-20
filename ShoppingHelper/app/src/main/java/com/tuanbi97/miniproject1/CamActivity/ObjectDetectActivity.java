/*
*  Copyright (C) 2015 TzuTaLin
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.tuanbi97.miniproject1.CamActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BigImageCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.tuanbi97.miniproject1.Data;
import com.tuanbi97.miniproject1.DataTransfer;
import com.tuanbi97.miniproject1.MainActivity;
import com.tuanbi97.miniproject1.MapActivity.MapsActivity;
import com.tzutalin.vision.visionrecognition.ObjectDetector;
import com.tuanbi97.miniproject1.R;
import com.tzutalin.vision.visionrecognition.VisionClassifierCreator;
import com.tzutalin.vision.visionrecognition.VisionDetRet;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ObjectDetectActivity extends Activity {
    private final static String TAG = "ObjectDetectActivity";
    private ObjectDetector mObjectDet;
    private int dw, dh;
    private View.OnClickListener objectClickListener;
    // UI
    LinearLayout mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_object_detect);
        dw = getResources().getDisplayMetrics().widthPixels;
        dh = getResources().getDisplayMetrics().widthPixels;
        mListView = (LinearLayout) findViewById(R.id.material_listview);
        final String key = Camera2BasicFragment.KEY_IMGPATH;
        String imgPath = getIntent().getExtras().getString(key);
        Log.d("Objectdetection", imgPath);
        if (!new File(imgPath).exists()) {
            Toast.makeText(this, "No file path", Toast.LENGTH_SHORT).show();
            this.finish();
            return;
        }
        objectClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = (String) ((TextView) v.findViewById(R.id.objlabel)).getText();
                if (label.equals("tvmonitor")){
                    Toast.makeText(ObjectDetectActivity.this, label, Toast.LENGTH_SHORT).show();
                    DataTransfer.places = Data.listTV;
                    Intent intentmap = new Intent(ObjectDetectActivity.this, MapsActivity.class);
                    startActivity(intentmap);
                }
                else
                if (label.equals("bottle")){
                    Toast.makeText(ObjectDetectActivity.this, label, Toast.LENGTH_SHORT).show();
                    DataTransfer.places = Data.listBottle;
                    Intent intentmap = new Intent(ObjectDetectActivity.this, MapsActivity.class);
                    startActivity(intentmap);
                }
                else{
                    Toast.makeText(ObjectDetectActivity.this, "Object is not available", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DetectTask task = new DetectTask();
        task.execute(imgPath);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_object_detect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ==========================================================
    // Tasks inner class
    // ==========================================================
    private class DetectTask extends AsyncTask<String, Void, List<VisionDetRet>> {
        private ProgressDialog mmDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mmDialog = ProgressDialog.show(ObjectDetectActivity.this, getString(R.string.dialog_wait),getString(R.string.dialog_object_decscription), true);
        }

        @Override
        protected List<VisionDetRet> doInBackground(String... strings) {
            final String filePath = strings[0];
            long startTime;
            long endTime;
            Log.d(TAG, "DetectTask filePath:" + filePath);
            if (mObjectDet == null) {
                try {
                    mObjectDet = VisionClassifierCreator.createObjectDetector(getApplicationContext());
                    // TODO: Get Image's height and width
                    mObjectDet.init(0, 0);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            List<VisionDetRet> ret = new ArrayList<>();
            if (mObjectDet != null) {
                startTime = System.currentTimeMillis();
                Log.d(TAG, "Start objDetect");
                ret.addAll(mObjectDet.classifyByPath(filePath));
                Log.d(TAG, "end objDetect");
                endTime = System.currentTimeMillis();
                final double diffTime = (double) (endTime - startTime) / 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ObjectDetectActivity.this, "Take " + diffTime + " second", Toast.LENGTH_LONG).show();
                    }
                });
            }
            File beDeletedFile = new File(filePath);
            if (beDeletedFile.exists()) {
                beDeletedFile.delete();
            } else {
                Log.d(TAG, "file does not exist " + filePath);
            }

            mObjectDet.deInit();
            return ret;
        }

        @Override
        protected void onPostExecute(List<VisionDetRet> rets) {
            super.onPostExecute(rets);
            if (mmDialog != null) {
                mmDialog.dismiss();
            }
            // TODO: Remvoe it
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            String retImgPath = "/sdcard/temp.jpg";
            Bitmap bitmap = BitmapFactory.decodeFile(retImgPath, options);

            ImageView objview = (ImageView) mListView.findViewById(R.id.objectview);
            objview.setImageBitmap(bitmap);

            for (VisionDetRet item : rets) {
                if (!item.getLabel().equalsIgnoreCase("background")) {
                    View card = LayoutInflater.from(ObjectDetectActivity.this).inflate(R.layout.objectdetected, null);
                    card.setOnClickListener(objectClickListener);
                    ((TextView) card.findViewById(R.id.objlabel)).setText(item.getLabel());
                    ((TextView) card.findViewById(R.id.objprob)).setText(Double.toString(item.getConfidence()));
                    mListView.addView(card);
                }
                Log.d("Objectdetection", item.getLabel() + " " + Double.toString(item.getConfidence()));
                /*StringBuilder sb = new StringBuilder();
                sb.append(item.getLabel())
                        .append(", Prob:").append(item.getConfidence())
                        .append(" [")
                        .append(item.getLeft()).append(',')
                        .append(item.getTop()).append(',')
                        .append(item.getRight()).append(',')
                        .append(item.getBottom())
                        .append(']');
                Log.d(TAG, sb.toString());

                if (!item.getLabel().equalsIgnoreCase("background")) {
                    card = new Card.Builder(ObjectDetectActivity.this)
                            .withProvider(BigImageCardProvider.class)
                            .setTitle("Detect Result")
                            .setDescription(sb.toString())
                            .endConfig()
                            .build();
                    mListView.add(card);
                }*/
            }

            File beDeletedFile = new File(retImgPath);
            if (beDeletedFile.exists()) {
                beDeletedFile.delete();
            }

        }
    }
}
