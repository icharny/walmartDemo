package com.example.isaaccharny.walmartdemo.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.isaaccharny.walmartdemo.Adapters.ProductAdapterInterface;
import com.example.isaaccharny.walmartdemo.model.ProductData;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FetchImageTask extends AsyncTask<Void, Void, String> {
    private final ProductAdapterInterface mAdapter;
    private final Context mContext;
    private final String mImgUrl;

    public FetchImageTask(ProductAdapterInterface mAdapter, Context mContext, String mImgUrl) {
        this.mAdapter = mAdapter;
        this.mContext = mContext;
        this.mImgUrl = mImgUrl;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = new URL(mImgUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            File cacheDirectory = mContext.getCacheDir();
            String imgUuid = UUID.randomUUID().toString();
            File imgFile = new File(cacheDirectory.getPath() + "/wal_img_" + imgUuid + ".png");
            FileOutputStream fOut = new FileOutputStream(imgFile);
            Bitmap b = BitmapFactory.decodeStream(in);
            b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            return imgFile.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String imgPath) {
        super.onPostExecute(imgPath);

        if (TextUtils.isEmpty(imgPath)) {
            ProductData.IMAGES.remove(mImgUrl);
        } else {
            ProductData.IMAGES.put(mImgUrl, imgPath);
        }

        mAdapter.notifyDataSetChanged();
    }
}