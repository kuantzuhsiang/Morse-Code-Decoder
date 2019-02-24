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
    private Handler handler = new Handler();
    private int bufferSize;
    private boolean signalHighFlag = false;
    private int count = 0;

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
                short[] audioData = new short[bufferSize];
                audioRecorder.startRecording();
                if (audioRecorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    audioRecorder.read(audioData, 0, bufferSize);
                    printRecording(audioData);
                    checkForHigh(audioData);
                    handler.postDelayed(this, 100);
                }
            }
        }
    };

    private void checkForHigh(short[] data) {
        if (Math.abs(data[0]) > 50) {
            if (signalHighFlag) {
                count++;
            } else {
                determineTypeOfSilence(count);
                signalHighFlag = true;
                count = 1;
            }
        } else {
            if (!signalHighFlag) {
                count++;
            } else {
                determineTypeOfSignal(count);
                signalHighFlag = false;
                count = 1;
            }
        }
    }

    private void determineTypeOfSilence(int count) {
        if (count <= 4) {
            //next signal
        } else if (count <= 12) {
            //next letter
        } else {
            //next word
        }
    }

    private void determineTypeOfSignal(int count) {
        if (count <= 4) {
            //dot
        } else {
            //dash
        }
    }

    private void printRecording(short[] data) {
        String text = "";
        for (int i = 0; i < data.length; i++) {
            text = text + " " + Short.toString(data[i]);
        }
        Log.d(TAG, text);
    }
}
