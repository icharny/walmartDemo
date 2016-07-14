package com.example.isaaccharny.walmartdemo;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.example.isaaccharny.walmartdemo.Adapters.ProductListAdapter;
import com.example.isaaccharny.walmartdemo.Tasks.FetchProductsTask;
import com.example.isaaccharny.walmartdemo.model.ProductData;

public class ProductListActivity extends AppCompatActivity implements FetchProductsTask.OnFetchProductsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ProductListAdapter productListAdapter;
        if (findViewById(R.id.product_grid) != null) {
            // tablet
            productListAdapter = new ProductListAdapter(this, R.layout.product_grid_content);

            GridView gridView = (GridView) findViewById(R.id.product_grid);
            gridView.setAdapter(productListAdapter);
        } else {
            productListAdapter = new ProductListAdapter(this, R.layout.product_list_content);

            ListView listView = (ListView) findViewById(R.id.product_list);
            listView.setAdapter(productListAdapter);
        }
        FetchProductsTask task = new FetchProductsTask(1, productListAdapter, this, this);
        task.execute();

        if (ProductData.PRODUCTS.size() > 0) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    @Override
    @UiThread
    public void onFetchProducts() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}
