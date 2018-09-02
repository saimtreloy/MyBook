package kooxda.saim.com.mybook.Utility;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.File;

/**
 * Created by NREL on 7/1/18.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        //return new HttpProxyCacheServer(this);
        return  new HttpProxyCacheServer.Builder(this).maxCacheSize(1024*1024*1024).maxCacheFilesCount(1000).cacheDirectory(new File(getCacheDir().getAbsolutePath())).build();
    }
}