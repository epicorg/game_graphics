package login.audio;

import android.content.Context;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import login.communication.ServerCommunicationThread;

/**
 * Created by Luca on 30/03/2015.
 */
public class AudioCallManager {

    public static final String LOG_TAG = "AudioCallManager";

    private static AudioCallManager instance = new AudioCallManager();
    private AudioGroup audioGroup;
    private AudioStream audioStream;
    private Context context;

    private InetAddress serverIp;
    private int serverPort;

    private AudioCallManager() {
        initAudioGroup();
    }

    public static AudioCallManager getInstance() {
        return instance;
    }

    public void initAudioGroup() {
        audioGroup = new AudioGroup();
        audioGroup.setMode(AudioGroup.MODE_ECHO_SUPPRESSION);
    }

    public int newAudioStream() throws UnknownHostException, SocketException {
        InetAddress localIpAddress = InetAddress.getByName(ServerCommunicationThread.getLocalIpAddress());
        audioStream = new AudioStream(localIpAddress);
        audioStream.setCodec(AudioCodec.PCMU);
        audioStream.setMode(RtpStream.MODE_NORMAL);
        int localPort = audioStream.getLocalPort();
        return localPort;
    }

    public void associateStream() {
        audioStream.associate(serverIp, serverPort);
        audioStream.join(audioGroup);
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_IN_COMMUNICATION);

        if (Audio.isWiredHeadsetOn())
            Audio.setSpeakerphoneOn(false);
    }

    public void releaseResources() {
        audioStream.join(null);
        audioStream.release();
        audioStream = null;
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Audio.setMode(AudioManager.MODE_NORMAL);
        Audio.setSpeakerphoneOn(false);
        Audio.setMicrophoneMute(false);
    }

    public void muteUnMute(){
        AudioManager Audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(Audio.isMicrophoneMute())
            Audio.setMicrophoneMute(false);
        else
            Audio.setMicrophoneMute(true);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }


}
