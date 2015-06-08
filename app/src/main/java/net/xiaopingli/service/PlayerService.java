package net.xiaopingli.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import net.xiaopingli.Constants.Constants;
import net.xiaopingli.model.Mp3Info;

import java.io.File;
import java.io.IOException;

public class PlayerService extends Service {

    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isReleased = false;
    MediaPlayer mediaPlayer;

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("---------------PlayerService onStartCommand");
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        int MSG = intent.getIntExtra("MSG",0);

        switch (MSG){
            case Constants.PlayerMsg.MSG_PLAY:
                play(mp3Info);
                break;
            case Constants.PlayerMsg.MSG_PAUSE:
                pause();
                break;
            case Constants.PlayerMsg.MSG_STOP:
                stop();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stop() {
        System.out.println("---------------service stop");
        if(mediaPlayer!=null){
            if(isPlaying){
                if(!isReleased){
                    mediaPlayer.stop();
                    //mediaPlayer.release();
                    isReleased = true;
                }
                isPlaying = false;
            }
        }
    }

    private void pause() {
        System.out.println("---------------service pause");
        if(mediaPlayer!=null){
            if(!isReleased){
                if(!isPaused){
                    mediaPlayer.pause();
                    isPaused=true;
                    isPlaying=true;
                }else {
                    mediaPlayer.start();
                    isPaused=false;
                }
            }
        }
    }

    private void play(Mp3Info mp3Info){
        Uri uri = Uri.parse("file://" + getMp3Path(mp3Info));
        mediaPlayer = MediaPlayer.create(PlayerService.this,uri);
        System.out.println("---------------service play");
        if(!isPlaying && mediaPlayer!=null){
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            isPlaying = true;
            isReleased = false;
        }
    }

    private String getMp3Path(Mp3Info mp3Info){
        String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = SDPATH+ File.separator+"mp3"+File.separator+mp3Info.getMp3Name();
        return path;
    }
}
