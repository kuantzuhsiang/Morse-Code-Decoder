package e.eric.morsecode;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AudioRecord audioRecorder;
    private static short[] audioData;
    private Handler handler = new Handler();
    private int bufferSize;

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView start = findViewById(R.id.start);

        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.d(TAG, "buffer size = " + bufferSize);

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
                        start.setImageResource(R.drawable.mic_pressed);
                        recordSound.run();
                        return true;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "released");
                        start.setImageResource(R.drawable.mic);
                        handler.removeCallbacks(recordSound);
                        return true;
                }
                return false;
            }
        });
    }

    private Runnable recordSound = new Runnable() {
        @Override
        public void run() {
            if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                audioData = new short[bufferSize];
                audioRecorder.startRecording();
                if (audioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecorder.read(audioData, 0, bufferSize);
//                    printRecording(audioData);
                    handler.postDelayed(this, 25);
                }
            }
        }
    };

    private void printRecording(short[] data) {
        for (int i = 0; i < data.length; i++) {
            Log.d(TAG, i + ": " + Short.toString(data[i]));
        }
    }
}
