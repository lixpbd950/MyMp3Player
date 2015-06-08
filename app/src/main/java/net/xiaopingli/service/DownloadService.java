package net.xiaopingli.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import net.xiaopingli.download.HttpDownloader;
import net.xiaopingli.model.Mp3Info;

public class DownloadService extends Service {
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("-------------->DownloadService Starts!");
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        DownloadMp3Thread downloadMp3Thread = new DownloadMp3Thread(mp3Info);
        Thread thread = new Thread(downloadMp3Thread);
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    public class DownloadMp3Thread implements Runnable{
        private Mp3Info mp3Info;
        public DownloadMp3Thread(Mp3Info mp3Info){
            this.mp3Info = mp3Info;
        }
        @Override
        public void run() {
            HttpDownloader httpDownloader = new HttpDownloader();
            String mp3Url = "http://xiaopingli.net/mp3/"+mp3Info.getMp3Name();
            int res = httpDownloader.downloadFile("mp3/",mp3Info.getMp3Name(),mp3Url);
        }
    }
}
