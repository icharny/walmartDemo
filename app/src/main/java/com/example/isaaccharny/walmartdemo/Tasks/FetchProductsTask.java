package com.example.isaaccharny.walmartdemo.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.isaaccharny.walmartdemo.Adapters.ProductAdapterInterface;
import com.example.isaaccharny.walmartdemo.model.Product;
import com.example.isaaccharny.walmartdemo.model.ProductData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchProductsTask extends AsyncTask<Void, Void, List<Product>> {
    private static final String API_ENDPOINT_FORMAT = "https://walmartlabs-test.appspot.com/_ah/api/walmart/v1/walmartproducts/%s/%d/%d";
    private static final String API_KEY = "e245c0de-1d6b-471c-9181-322b6b374a5b";
    public static final int PAGE_SIZE = 30;

    private final int mPage;
    private final ProductAdapterInterface mAdapter;
    private final Context mContext;
private OnFetchProductsListener mOnFetchProductsListener;

    public FetchProductsTask(int mPage, ProductAdapterInterface mAdapter, Context mContext) {
        this.mPage = mPage;
        this.mAdapter = mAdapter;
        this.mContext = mContext;
    }

    public FetchProductsTask(int mPage, ProductAdapterInterface mAdapter, Context mContext, OnFetchProductsListener onFetchProductsListener) {
        this.mPage = mPage;
        this.mAdapter = mAdapter;
        this.mContext = mContext;
        this.mOnFetchProductsListener = onFetchProductsListener;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        List<Product> products;
        JSONObject reponseJson = fetchProducts();
        if (reponseJson != null) {
            products = parseJsonIntoProducts(reponseJson);
        } else {
            products = new ArrayList<>();
        }

        return products;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        super.onPostExecute(products);

        for (Product p : products) {
            ProductData.PRODUCTS.add(p);
        }

        mAdapter.notifyDataSetChanged();

        if (mOnFetchProductsListener != null) {
            mOnFetchProductsListener.onFetchProducts();
        }
    }

    @Nullable
    private JSONObject fetchProducts() {
        JSONObject returnJson = null;

        try {
            URL url = new URL(String.format(API_ENDPOINT_FORMAT, API_KEY, mPage, PAGE_SIZE));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder resp = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                resp.append(line);
            }

            returnJson = new JSONObject(resp.toString());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return returnJson;
    }

    private List<Product> parseJsonIntoProducts(@NonNull final JSONObject responseJson) {
        List<Product> products = new ArrayList<>();
        try {
            JSONArray productsJson = responseJson.getJSONArray("products");
            for (int i = 0; i < productsJson.length(); i++) {
                JSONObject productJson = productsJson.getJSONObject(i);
                Product product = new Product();
                product.setId(productJson.getString("productId"));
                if (productJson.has("productName")) {
                    product.setName(productJson.getString("productName"));
                }
                if (productJson.has("shortDescription")) {
                    product.setShortDescription(productJson.getString("shortDescription"));
                }
                if (productJson.has("longDescription")) {
                    product.setLongDescription(productJson.getString("longDescription"));
                }
                if (productJson.has("price")) {
                    product.setPrice(productJson.getString("price"));
                }
                if (productJson.has("productImage")) {
                    String imgUrl = productJson.getString("productImage");
                    product.setProductImage(imgUrl);
                    if (!TextUtils.isEmpty(imgUrl) && !ProductData.IMAGES.containsKey(imgUrl)) {
                        new FetchImageTask(mAdapter, mContext, imgUrl).execute();
                    }
                }
                if (productJson.has("reviewRating")) {
                    product.setReviewRating(productJson.getDouble("reviewRating"));
                }
                if (productJson.has("reviewCount")) {
                    product.setReviewCount(productJson.getLong("reviewCount"));
                }
                if (productJson.has("inStock")) {
                    product.setInStock(productJson.getBoolean("inStock"));
                }
                products.add(product);
                Log.v("Product fetched", product.toString());
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        try {
            long totalProducts = responseJson.getLong("totalProducts");
            if (totalProducts <= PAGE_SIZE * mPage) {
                mAdapter.notifyNoMoreData();
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return products;
    }

    public interface OnFetchProductsListener {
        void onFetchProducts();
    }
}
