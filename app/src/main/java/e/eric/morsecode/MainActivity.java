package e.eric.morsecode;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AudioRecord audioRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView start = findViewById(R.id.start);

        audioRecorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    Log.d(TAG, "test 1");
//                    audioRecorder.startRecording();
                    short[] audioData;
                    audioRecorder.read(audioData, );
                    Log.d(TAG, "test 2");
                }
            }
        });

    }
}
