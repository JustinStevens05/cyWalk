package com.example.androidexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * The volley singleton that is used to make requests to the backend
 * */
public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * gets the volley instance being using in the program
     *
     * @param context the context of the current page
     * @return the VolleySingleton being used in the code
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * gets the request queue being used byt hte volley singleton
     *
     * @return the RequestQueue the volley singleton is using
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * adds a request to the end of the current request queue in volley
     *
     * @param req the request you would like to add to the request queue
     * @param <T> I honestly don't know
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * gets the image loader
     *
     * @return this Volley singletons ImageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
