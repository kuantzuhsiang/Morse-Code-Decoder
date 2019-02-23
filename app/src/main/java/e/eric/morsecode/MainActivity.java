package e.eric.morsecode;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AudioRecord audioRecorder;
    private static short[] audioData;
    private boolean recording = false;
    private Timer timer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView start = findViewById(R.id.start);

        final int bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "pressed");
                        recording = true;
                        start.setImageResource(R.drawable.mic_pressed);
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "released");
                        recording = false;
                        start.setImageResource(R.drawable.mic);
                        return true;
                }
                return false;
            }
        });

//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                start.setImageResource(R.drawable.mic_pressed);
//
//                if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
//                    audioData = new short[bufferSize];
//                    audioRecorder.startRecording();
//                    if (audioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
//                        Log.d(TAG, "test");
//                    }
//                    Thread recordingThread = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            while(true) {
//                                audioRecorder.read(audioData, 0, bufferSize);
//                                printRecording(audioData);
//                            }
//                        }
//                    });
//                    recordingThread.start();
//                }
//            }
//        });

    }

    private void printRecording(short[] data) {
        for (int i = 0; i < data.length; i++) {
            Log.d(TAG, i + ": " + Short.toString(data[i]));
        }
    }
}
