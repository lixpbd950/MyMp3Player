package net.xiaopingli.mymp3player.app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import net.xiaopingli.model.Mp3Info;

import java.io.File;
import java.io.IOException;


public class PlayerActivity extends ActionBarActivity {

    private ImageButton playButton;
    private ImageButton pauseButton;
    private ImageButton stopButton;

    private MediaPlayer mediaPlayer;
    private Mp3Info mp3Info;

    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isReleased = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        System.out.println("mp3infor------------->"+mp3Info);

        playButton = (ImageButton) findViewById(R.id.player_play);
        pauseButton = (ImageButton) findViewById(R.id.player_pause);
        stopButton = (ImageButton) findViewById(R.id.player_stop);


        Uri uri = Uri.parse("file://"+getMp3Path(mp3Info));
        mediaPlayer = MediaPlayer.create(PlayerActivity.this,uri);

        playButton.setOnClickListener(new PlayListener());
        pauseButton.setOnClickListener(new PauseListener());
        stopButton.setOnClickListener(new StopListener());
    }

    public class PlayListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
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
    }

    public class PauseListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(mediaPlayer!=null){
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

    public class StopListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(mediaPlayer!=null){
                if(isPlaying){
                    if(!isReleased){
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        isReleased = true;
                    }
                    isPlaying = false;
                }
            }
        }
    }

    private String getMp3Path(Mp3Info mp3Info){
        String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
        String path = SDPATH+File.separator+"mp3"+File.separator+mp3Info.getMp3Name();
        return path;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
