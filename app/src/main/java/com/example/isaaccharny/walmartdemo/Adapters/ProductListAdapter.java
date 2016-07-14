package com.example.isaaccharny.walmartdemo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.isaaccharny.walmartdemo.ProductDetailActivity;
import com.example.isaaccharny.walmartdemo.ProductDetailFragment;
import com.example.isaaccharny.walmartdemo.R;
import com.example.isaaccharny.walmartdemo.Tasks.FetchImageTask;
import com.example.isaaccharny.walmartdemo.Tasks.FetchProductsTask;
import com.example.isaaccharny.walmartdemo.Tasks.LoadImageTask;
import com.example.isaaccharny.walmartdemo.model.Product;
import com.example.isaaccharny.walmartdemo.model.ProductData;

public class ProductListAdapter extends BaseAdapter implements ProductAdapterInterface {
    private boolean mCanFetchMore = true;
    private final int mLayoutResourceId;
    private final Context mContext;

    public ProductListAdapter(Context context, int layoutResourceId) {
        super();
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        return ProductData.PRODUCTS.size();
    }

    @Override
    public Product getItem(int position) {
        return ProductData.PRODUCTS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (position == getCount() - 10 && mCanFetchMore) {
            FetchProductsTask task = new FetchProductsTask((getCount() / FetchProductsTask.PAGE_SIZE) + 1, this, mContext);
            task.execute();
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutResourceId, parent, false);

            ImageView iv = (ImageView) convertView.findViewById(R.id.product_list_productImage);
            TextView tv = (TextView) convertView.findViewById(R.id.product_list_productName);
            WebView wv = (WebView) convertView.findViewById(R.id.product_list_productShortDescription);

            convertView.setTag(new ViewHolder(iv, tv, wv));
        }

        Product product = getItem(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        String imgUrl = product.getProductImage();
        String imgPath = ProductData.IMAGES.get(imgUrl);
        if (!TextUtils.isEmpty(imgPath)) {
            if (mLayoutResourceId == R.layout.product_grid_content) {
                // needed for funky gridview scrolling behavior
                new LoadImageTask(viewHolder.productImageView, imgUrl).execute();
            } else {
                Bitmap imgBitmap = BitmapFactory.decodeFile(imgPath);
                viewHolder.productImageView.setImageBitmap(imgBitmap);
            }
        } else {
            new FetchImageTask(this, mContext, imgUrl).execute();
        }
        viewHolder.productNameTextView.setText(product.getName());
        if (viewHolder.productShortDescriptionWebView != null) {
            String shortDescription = product.getShortDescription();
            if (!TextUtils.isEmpty(shortDescription)) {
                viewHolder.productShortDescriptionWebView.setVisibility(View.VISIBLE);
                viewHolder.productShortDescriptionWebView.loadData(shortDescription, "text/html", "utf-8");
                // needed because webview doesn't pass though click events to the underlying listview
                viewHolder.productShortDescriptionWebView.setOnTouchListener(new View.OnTouchListener() {
                    public final static int FINGER_RELEASED = 0;
                    public final static int FINGER_TOUCHED = 1;
                    public final static int FINGER_DRAGGING = 2;
                    public final static int FINGER_UNDEFINED = 3;

                    private int fingerState = FINGER_RELEASED;

                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (fingerState == FINGER_RELEASED) {
                                    fingerState = FINGER_TOUCHED;
                                } else {
                                    fingerState = FINGER_UNDEFINED;
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                if (fingerState != FINGER_DRAGGING) {
                                    fingerState = FINGER_RELEASED;
                                    showProductDetails(position);
                                } else if (fingerState == FINGER_DRAGGING) {
                                    fingerState = FINGER_RELEASED;
                                } else {
                                    fingerState = FINGER_UNDEFINED;
                                }
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if ((fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) && (motionEvent.getEventTime() - motionEvent.getDownTime() > 75)) {
                                    fingerState = FINGER_DRAGGING;
                                } else {
                                    fingerState = FINGER_UNDEFINED;
                                }
                                break;
                            default:
                                fingerState = FINGER_UNDEFINED;
                        }

                        return true;
                    }
                });
            } else {
                viewHolder.productShortDescriptionWebView.setVisibility(View.GONE);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProductDetails(position);
            }
        });

        return convertView;
    }

    @Override
    public void notifyNoMoreData() {
        mCanFetchMore = false;
    }

    private static class ViewHolder {
        public final ImageView productImageView;
        public final TextView productNameTextView;
        public final WebView productShortDescriptionWebView;

        private ViewHolder(ImageView iv, TextView tv, WebView wv) {
            this.productImageView = iv;
            this.productNameTextView = tv;
            this.productShortDescriptionWebView = wv;
        }
    }

    @UiThread
    private void showProductDetails(int position) {
        Intent intent = new Intent(mContext, ProductDetailActivity.class);
        intent.putExtra(ProductDetailFragment.PRODUCT_INDEX, position);
        mContext.startActivity(intent);
    }
}
