package ca.nait.dmit2504.outtolunch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class CuisineRouletteActivity extends AppCompatActivity implements Animation.AnimationListener {

    private TextView mCuisineTxt, mStartTxt;
    private Button mFindBtn;
    private boolean mBtnRotation = true;
    private int mNum = 7;
    private int mDegrees = 0, mDegreesOld = 0;
    private Random mRandom;
    //7 sectors on the wheel = 51.42857 degrees each
    //1st sector starts from half the previous sector, 51.42857/ 2 = 25.714285, ends in 25.714285 + 51.42857 = 77.1429
    // 77.1429 is 3x 25.714285, so 25.714285 = factor
    private static final float FACTOR = 25.714285f; //use this to calculate sectors
    private static final String[] mCuisines = {"Chinese", "Vegetarian", "Indian", "Japanese", "Viet", "American", "Italian"};
    private String selectedCuisine = "";


    private ImageView mWheelImgView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuisine_roulette);

        mStartTxt = findViewById(R.id.act_roulette_start_txt);
        mCuisineTxt = findViewById(R.id.act_roulette_cuisine_txt);
        mFindBtn = findViewById(R.id.act_roulette_search_btn);
        mWheelImgView = findViewById(R.id.act_roulette_wheel_img);

        mWheelImgView.setOnClickListener(v -> {
            //create random instance
            mRandom = new Random();
            mDegreesOld = mDegrees % 360;
            mDegrees = mRandom.nextInt(360) + 3600;
            RotateAnimation rotateAnimation = new RotateAnimation(mDegreesOld, mDegrees,RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setDuration(3600);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setAnimationListener(this);
            mWheelImgView.setAnimation(rotateAnimation);
            mWheelImgView.startAnimation(rotateAnimation);
        });

        mFindBtn.setOnClickListener(v -> {
            String searchString = mCuisineTxt.getText().toString();

            if(!searchString.equals("")) {
                Intent mapIntent = new Intent(this, MapsActivity.class);
                mapIntent.putExtra("search", searchString);
                startActivity(mapIntent);
            } else {
                Toast toast = Toast.makeText(this, R.string.please_spin_the_wheel, Toast.LENGTH_SHORT);
                //set toast position
                toast.setGravity(Gravity.TOP, 0,0);
                toast.show();
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
        //clear start text + selected cuisine
        mStartTxt.setText("");
        mCuisineTxt.setText("");
        mBtnRotation = false;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        selectedCuisine = currentCuisine(360 - (mDegrees % 360));
        mCuisineTxt.setText(selectedCuisine);
        mBtnRotation = true;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private String currentCuisine(int degrees) {
        String cuisine = "Chinese";
        if (degrees >= (FACTOR * 1) && degrees < (FACTOR * 3)) {
            cuisine = "Italian";
        }
        if (degrees >= (FACTOR * 3) && degrees < (FACTOR * 5)) {
            cuisine = "American";
        }
        if (degrees >= (FACTOR * 5) && degrees < (FACTOR * 7)) {
            cuisine = "Vietnamese";
        }
        if (degrees >= (FACTOR * 7) && degrees < (FACTOR * 9)) {
            cuisine = "Japanese";
        }
        if (degrees >= (FACTOR * 9) && degrees < (FACTOR * 11)) {
            cuisine = "Indian";
        }
        if (degrees >= (FACTOR * 11) && degrees < (FACTOR * 13)) {
            cuisine = "Vegetarian";
        }
        return cuisine;
    }
}