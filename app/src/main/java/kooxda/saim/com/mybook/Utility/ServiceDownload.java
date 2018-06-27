package kooxda.saim.com.mybook.Utility;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class ServiceDownload extends Service {

    public ServiceDownload() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    BroadcastReceiver onDownloadCompleteS = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(getApplicationContext(),  "Download Complete", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int onStartCommand(Intent intenta, int flags, int startId) {
        return super.onStartCommand(intenta, flags, startId);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(onDownloadCompleteS, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadCompleteS);
    }
}
