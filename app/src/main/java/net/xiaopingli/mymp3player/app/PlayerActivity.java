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
import net.xiaopingli.Constants.Constants;
import net.xiaopingli.model.Mp3Info;
import net.xiaopingli.service.PlayerService;

import java.io.File;


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
            Intent serviceIntent = new Intent(PlayerActivity.this,PlayerService.class);
            serviceIntent.putExtra("MSG", Constants.PlayerMsg.MSG_PLAY);
            serviceIntent.putExtra("mp3Info",mp3Info);
            PlayerActivity.this.startService(serviceIntent);
        }
    }

    public class PauseListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent serviceIntent = new Intent(PlayerActivity.this,PlayerService.class);
            serviceIntent.putExtra("MSG", Constants.PlayerMsg.MSG_PAUSE);
            serviceIntent.putExtra("mp3Info",mp3Info);
            getApplicationContext().startService(serviceIntent);
        }
    }

    public class StopListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent serviceIntent = new Intent(PlayerActivity.this,PlayerService.class);
            serviceIntent.putExtra("MSG", Constants.PlayerMsg.MSG_STOP);
            serviceIntent.putExtra("mp3Info",mp3Info);
            getApplicationContext().startService(serviceIntent);
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
