package login.call.audio;

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

    private static AudioCallManager instance = new AudioCallManager();
    private AudioGroup audioGroup;

    private AudioCallManager() {
        initAudioGroup();
    }

    public static AudioCallManager getInstance() {
        return instance;
    }

    public void initAudioGroup() {
        audioGroup = new AudioGroup();
        audioGroup.setMode(AudioGroup.MODE_NORMAL);
    }

    public AudioStream newAudioStream() throws SocketException, UnknownHostException {
        AudioStream audioStream = new AudioStream(InetAddress.getByName(
                ServerCommunicationThread.getLocalIpAddress()));
        audioStream.setCodec(AudioCodec.AMR);
        audioStream.setMode(RtpStream.MODE_NORMAL);
        audioStream.join(audioGroup);
        return audioStream;
    }

    public void setAudio() {
        //TODO
    }
}
