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
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AudioRecord audioRecorder;
    private Handler handler = new Handler();
    private int bufferSize;
    private boolean signalHighFlag = false;
    private int count = 0;
    private TextView message;
    private View dot, dash;
    String messageText = "";
    String messageWord = "";

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView start = findViewById(R.id.start);
        message = findViewById(R.id.message);
        dot = findViewById(R.id.dot);
        dash = findViewById(R.id.dash);

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
                    handler.postDelayed(this, 10);
                }
            }
        }
    };

    private void checkForHigh(short[] data) {
        if (Math.abs(data[0]) > 100) {
            Log.e(TAG, "signal detected");
            if (signalHighFlag) {
                count++;
            } else {
                Log.e(TAG, Integer.toString(count));


                signalHighFlag = true;
                count = 1;
            }
        } else {
            dot.setVisibility(View.GONE);
            dash.setVisibility(View.GONE);
            if (!signalHighFlag) {
                count++;
                determineTypeOfSilence(count);
                messageWord = "";
            } else {
                Log.d(TAG, Integer.toString(count));
                determineTypeOfSignal(count);
                signalHighFlag = false;
                count = 1;
            }
        }
    }

    private void determineTypeOfSilence(int count) {
        if (count <= 4) {
            //next dot/dash
            Log.e(TAG, "next dot/dash");
        } else if (count <= 10) {
            //next letter
            Log.e(TAG, "decode");
            decodeLetter(messageWord);
            message.setText(messageText);
            messageWord = "";
        } else {
            //next word
            messageText += " ";
        }
    }

    private void determineTypeOfSignal(int count) {
        if (count <= 5) {
            //dot
            dot.setVisibility(View.VISIBLE);
            messageWord += ".";
        } else {
            //dash
            dash.setVisibility(View.VISIBLE);
            messageWord += "-";
        }
    }

    private void decodeLetter(String word) {
        switch (word) {
            case ".-":
                messageText += "A";
                break;
            case "-...":
                messageText += "B";
                break;
            case "-.-.":
                messageText += "C";
                break;
            case "-..":
                messageText += "D";
                break;
            case ".":
                messageText += "E";
                break;
            case "..-.":
                messageText += "F";
                break;
            case "--.":
                messageText += "G";
                break;
            case "....":
                messageText += "H";
                break;
            case "..":
                messageText += "I";
                break;
            case ".---":
                messageText += "J";
                break;
            case "-.-":
                messageText += "K";
                break;
            case ".-..":
                messageText += "L";
                break;
            case "--":
                messageText += "M";
                break;
            case "-.":
                messageText += "N";
                break;
            case "---":
                messageText += "O";
                break;
            case ".--.":
                messageText += "P";
                break;
            case "--.-":
                messageText += "Q";
                break;
            case ".-.":
                messageText += "R";
                break;
            case "...":
                messageText += "S";
                break;
            case "-":
                messageText += "T";
                break;
            case "..-":
                messageText += "U";
                break;
            case "...-":
                messageText += "V";
                break;
            case ".--":
                messageText += "W";
                break;
            case "-..-":
                messageText += "X";
                break;
            case "-.--":
                messageText += "Y";
                break;
            case "--..":
                messageText += "Z";
                break;
            case ".----":
                messageText += "1";
                break;
            case "..---":
                messageText += "2";
                break;
            case "...--":
                messageText += "3";
            case "....-":
                messageText += "4";
                break;
            case ".....":
                messageText += "5";
                break;
            case "-....":
                messageText += "6";
                break;
            case "--...":
                messageText += "7";
                break;
            case "---..":
                messageText += "8";
                break;
            case "----.":
                messageText += "9";
                break;
            case "-----":
                messageText += "0";
                break;
        }
        Log.e(TAG, word);
    }

    private void printRecording(short[] data) {
        String text = "";
        for (int i = 0; i < data.length; i++) {
            text = text + " " + Short.toString(data[i]);
        }
        Log.d(TAG, text);
    }
}
