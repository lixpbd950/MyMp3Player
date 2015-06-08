package net.xiaopingli.mymp3player.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {
    protected static final String TAG = "MainActivity";
    private View currentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        ImageButton btn_one = (ImageButton) findViewById(R.id.buttom_one);
        ImageButton btn_two = (ImageButton) findViewById(R.id.buttom_two);

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Mp3ListFragment mp3ListFragment = new Mp3ListFragment();
                //TestFragment testFragment = new TestFragment();
                ft.replace(R.id.fl_content, mp3ListFragment, MainActivity.TAG);
                ft.commit();
                setButton(v);

            }
        });

        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                //Mp3ListFragment mp3ListFragment = new Mp3ListFragment();
                TestFragment testFragment = new TestFragment();
                ft.replace(R.id.fl_content, testFragment, MainActivity.TAG);
                ft.commit();
                setButton(v);

            }
        });


        /**
         * 默认第一个按钮点击
         */
        btn_one.performClick();

    }

    private void setButton(View v) {
        if (currentButton != null && currentButton.getId() != v.getId()) {
            currentButton.setEnabled(true);
        }
        v.setEnabled(false);
        currentButton = v;
    }
}
