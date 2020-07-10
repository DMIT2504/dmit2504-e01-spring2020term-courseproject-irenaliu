package ca.nait.dmit2504.musicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //logger
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mSongImgView;
    private ImageButton mUploadBtn;
    private MediaPlayer mMediaPlayer;
    private static final int REQUEST_AUDIO_FILE_CODE = 1;
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUploadBtn = findViewById(R.id.act_main_upload_btn);
        mSongImgView = findViewById(R.id.act_main_song_img);

        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        mUploadBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent uploadFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                uploadFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                uploadFileIntent.setType("audio/*");
                                startActivityForResult(Intent.createChooser(uploadFileIntent, "Select an audio file"), REQUEST_AUDIO_FILE_CODE);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            getFileName(uri);
            Log.i(TAG, mFileName);

            // Set background depending on song chosen
            switch (mFileName) {
                case "Sailor Moon - Moonlight Densetsu.mp3":
                    mSongImgView.setImageResource(R.drawable.sailor_moon);
                    break;
                case "Catch You, Catch Me.mp3":
                    mSongImgView.setImageResource(R.drawable.cardcaptors);
                    break;
                case "Spirited Away - Day Of The River.mp3":
                    mSongImgView.setImageResource(R.drawable.spirited_away);
                    break;
            }

            // Set audio properties then save audio to list
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(getApplicationContext(), uri);
            } catch (IllegalArgumentException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (SecurityException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IllegalStateException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
            }
            mMediaPlayer.start();
        }
    }

    // media player will be called with audio here
    private void getFileName(Uri uri){
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                mFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }
    }
}