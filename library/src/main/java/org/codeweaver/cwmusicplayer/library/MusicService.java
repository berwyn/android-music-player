package org.codeweaver.cwmusicplayer.library;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    public static final String ACTION_PLAY = "org.codeweaver.cwmusicplayer.action.play";

    private static final String TAG = "CWMusicService";

    private MediaPlayer player;
    private WifiManager.WifiLock wifiLock;

    public MusicService() {
    }

    @Override
    public void onDestroy() {
        stopPlayback();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_PLAY)) {
            // TODO: We're playing a track, grab metadata and prepare here

        }
        return START_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO: Actually handle errors, this is just generated for now
        return false;
    }

    private void constructPlayer() {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
    }

    private void stopPlayback() {
        if(player != null) {
            player.stop();
            player.release();
            player = null;
        }

        if(wifiLock != null) {
            wifiLock.release();
            wifiLock = null;
        }
    }

    private void playLocalResource(Uri uri) {
        constructPlayer();
        player.setDataSource(getApplicationContext(), uri);
        player.prepareAsync();
    }

    private void playRemoteResource(String url) {
        constructPlayer();
        player.setDataSource(url);
        player.prepareAsync();

        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "cwmusicservice");
    }
}
