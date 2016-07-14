package com.example.isaaccharny.walmartdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isaaccharny.walmartdemo.model.Product;
import com.example.isaaccharny.walmartdemo.model.ProductData;

public class ProductDetailFragment extends Fragment {
    public static final String PRODUCT_INDEX = "productIndex";

    private Product mProduct;

    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProduct = ProductData.PRODUCTS.get(getArguments().getInt(PRODUCT_INDEX));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        String imgUrl = mProduct.getProductImage();
        String imgPath = ProductData.IMAGES.get(imgUrl);
        ImageView imgView = (ImageView) rootView.findViewById(R.id.product_detail_productImage);
        if (!TextUtils.isEmpty(imgPath)) {
            //new LoadImageTask(imgView, imgUrl).execute();
            Bitmap imgBitmap = BitmapFactory.decodeFile(imgPath);
            imgView.setImageBitmap(imgBitmap);
        }
        ((TextView) rootView.findViewById(R.id.product_detail_productName)).setText(mProduct.getName());
        String longDescription = mProduct.getLongDescription();
        WebView wv = (WebView) rootView.findViewById(R.id.product_detail_productLongDescription);
        if (!TextUtils.isEmpty(longDescription)) {
            wv.setVisibility(View.VISIBLE);
            wv.loadData(longDescription, "text/html", "utf-8");
        } else {
            wv.setVisibility(View.GONE);
        }

        double aveRating = mProduct.getReviewRating();
        setStarImage(rootView, aveRating, R.id.starImageView1, 1);
        setStarImage(rootView, aveRating, R.id.starImageView2, 2);
        setStarImage(rootView, aveRating, R.id.starImageView3, 3);
        setStarImage(rootView, aveRating, R.id.starImageView4, 4);
        setStarImage(rootView, aveRating, R.id.starImageView5, 5);

        ((TextView) rootView.findViewById(R.id.ratingTextView)).setText(String.format("Average rating of %d review%s is %.2f", mProduct.getReviewCount(), mProduct.getReviewCount() != 1 ? "s" : "", aveRating));

        return rootView;
    }

    private void setStarImage(View rootView, double rating, int imageViewResourceId, double max) {
        ImageView starImgView = (ImageView) rootView.findViewById(imageViewResourceId);
        if (rating >= max) {
            // Image Credit: http://findicons.com/icon/465610/star_rating_full?id=466004
            starImgView.setImageDrawable(getResources().getDrawable(R.drawable.star_rating_full));
        } else if (rating >= (max - .5)) {
            // Image Credit: http://findicons.com/icon/465411/star_rating_half?id=465411
            starImgView.setImageDrawable(getResources().getDrawable(R.drawable.star_rating_half));
        } else {
            // Image Credit: http://findicons.com/icon/465644/star_rating_empty?id=466076
            starImgView.setImageDrawable(getResources().getDrawable(R.drawable.star_rating_empty));
        }
    }
}
