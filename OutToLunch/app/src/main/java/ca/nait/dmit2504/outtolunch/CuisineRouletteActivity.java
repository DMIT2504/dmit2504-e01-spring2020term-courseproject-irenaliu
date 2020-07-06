package ca.nait.dmit2504.outtolunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class CuisineRouletteActivity extends AppCompatActivity implements Animation.AnimationListener {

    private EditText mSearchEdit;
    private Button mFindBtn;
    private boolean mBtnRotation = true;
    private int mNum = 7;
    private long mDegrees = 0;

    ImageView mSelectedImgView, mWheelImgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_roulette);

        mSearchEdit = findViewById(R.id.act_roulette_search_edit);
        mFindBtn = findViewById(R.id.act_roulette_search_btn);
        mSelectedImgView = findViewById(R.id.act_roulette_selected_img);
        mWheelImgView = findViewById(R.id.act_roulette_wheel_img);

        mWheelImgView.setOnClickListener(v -> {
            int random = new Random().nextInt(360) + 3600;
            RotateAnimation rotateAnimation = new RotateAnimation((float) mDegrees, (float) (mDegrees + ((long) random)),1, 0.5f, 1, 0.5f);
            mDegrees = mDegrees + (long)random % 360;
            rotateAnimation.setDuration((long) random);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setAnimationListener(this);
            mWheelImgView.setAnimation(rotateAnimation);
            mWheelImgView.startAnimation(rotateAnimation);
        });

        mFindBtn.setOnClickListener(v -> {
            String searchString = mSearchEdit.getText().toString();

            if(!searchString.equals("")) {
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("search", searchString);
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mBtnRotation = false;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
//        String selectedCuisine = String.valueOf( (int) (double)mNum) - Math.floor((double)mDegrees) / (360.0d / ((double)mNum);
//        Toast toast = Toast.makeText(this, selectedCuisine, Toast.LENGTH_SHORT);
//        toast.setGravity(49, 0,0);
//        toast.show();
        mBtnRotation = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}