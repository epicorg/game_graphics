package login.call;

import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;

import java.net.InetAddress;
import java.net.UnknownHostException;

import login.communication.ServerCommunicationThread;


/**
 * Classe che permette la gestione degli eventi chiamata
 *
 * @author Luca
 * @since 28/03/2015.
 */
public class Call {

    private AudioStream audioStream;

    public Call(AudioStream audioStream) {
        this.audioStream = audioStream;
    }

    /**
     * Metodo che connette un audiostream all'host remoto avviando la comunicazione audio tramite RTP
     * in maniera bidirezionale ed automatica
     *
     * @param port
     * @throws UnknownHostException
     */
    public void attachAudioStream(int port) throws UnknownHostException {

        audioStream.associate(InetAddress.getByName(ServerCommunicationThread.SERVER_ADDRESS), port);

    }

    public void end() {
        audioStream.join(null);
        audioStream.release();
    }

    public void micOff() {
        audioStream.setMode(RtpStream.MODE_RECEIVE_ONLY);
    }

    public void micOn() {
        audioStream.setMode(RtpStream.MODE_NORMAL);
    }
}
