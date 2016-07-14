package com.example.isaaccharny.walmartdemo.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.isaaccharny.walmartdemo.Tasks.FetchProductsTask;
import com.example.isaaccharny.walmartdemo.ProductDetailFragment;
import com.example.isaaccharny.walmartdemo.model.ProductData;

public class ProductDetailPagerAdapter extends FragmentPagerAdapter implements ProductAdapterInterface {
    private boolean mCanFetchMore = true;
    private final Context mContext;

    public ProductDetailPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == getCount() - 10 && mCanFetchMore) {
            FetchProductsTask task = new FetchProductsTask((getCount() / FetchProductsTask.PAGE_SIZE) + 1, this, mContext);
            task.execute();
        }

        Bundle arguments = new Bundle();
        arguments.putInt(ProductDetailFragment.PRODUCT_INDEX,
                position);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return ProductData.PRODUCTS.size();
    }

    @Override
    public void notifyNoMoreData() {
        mCanFetchMore = false;
    }
}
