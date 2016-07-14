package com.example.isaaccharny.walmartdemo.Tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.isaaccharny.walmartdemo.model.ProductData;

public class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private final ImageView mImageView;
    private final String imgUrl;
    private String imgPath;

    public LoadImageTask(ImageView mImageView, String imgUrl) {
        this.mImageView = mImageView;
        this.imgUrl = imgUrl;
    }

    @Override
    protected void onPreExecute() {
        imgPath = ProductData.IMAGES.get(imgUrl);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap imgBitmap = BitmapFactory.decodeFile(imgPath);

        return imgBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap img) {
        super.onPostExecute(img);

        mImageView.setImageBitmap(img);
    }
}
