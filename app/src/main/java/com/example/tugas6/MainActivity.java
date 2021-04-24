package com.example.tugas6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView img1, img2, img3;
    private Wheel wheel1, wheel2, wheel3;
    private Button btn;
    private boolean isStarted;
    private Dialog dialog_win;
    private MediaPlayer sound;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img1 = (ImageView) findViewById(R.id.iv_slot1);
        img2 = (ImageView) findViewById(R.id.iv_slot2);
        img3 = (ImageView) findViewById(R.id.iv_slot3);
        btn = (Button) findViewById(R.id.btn_play);

        dialog_win = new Dialog(this);
        sound = MediaPlayer.create(this, R.raw.jackpot);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStarted){
                    wheel1.stopWheel();
                    wheel2.stopWheel();
                    wheel3.stopWheel();

                    if (wheel1.currentIndex == wheel2.currentIndex
                    && wheel2.currentIndex == wheel3.currentIndex) {
                        openDialogWin(500);
                    } else {
                        Toast.makeText(MainActivity.this,"Ooops you lose, try again", Toast.LENGTH_SHORT).show();
                    }

                    btn.setText("Start");
                    isStarted = false;
                    stopSoundEffect();
                } else {
                    wheel1 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img1.setImageResource(img);
                                }
                            });
                        }
                    }, 100, randomLong(0, 200));

                    wheel1.start();

                    wheel2 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img2.setImageResource(img);
                                }
                            });
                        }
                    }, 100, randomLong(200, 450));

                    wheel2.start();

                    wheel3 = new Wheel(new Wheel.WheelListener() {
                        @Override
                        public void newImage(int img) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    img3.setImageResource(img);
                                }
                            });
                        }
                    }, 150, randomLong(200, 450));

                    wheel3.start();

                    btn.setText("Stop");
                    isStarted = true;
                    playSoundEffect();
                }
            }
        });
    }

    private void openDialogWin(int duration) {
        dialog_win.setContentView(R.layout.dialog_win);

        Button btn_play = dialog_win.findViewById(R.id.btn_dialog_play);
        Button btn_share = dialog_win.findViewById(R.id.btn_dialog_share);
        ImageView btn_close = dialog_win.findViewById(R.id.btn_dialog_close);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog_win.getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
                dialog_win.show();

                btn_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_win.dismiss();
                    }
                });

                btn_play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_win.dismiss();
                    }
                });

                btn_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");

                        String body = "Gais gila seneng banget gue abis menang jackpot!";
                        String subject = "";

                        i.putExtra(Intent.EXTRA_TEXT, body);
                        i.putExtra(Intent.EXTRA_SUBJECT, subject);

                        startActivity((Intent.createChooser(i, "Share using")));
                    }
                });
            }
        }, duration);
    }

    private void playSoundEffect() {
        sound.start();
        sound.setLooping(true);
    }

    private void stopSoundEffect() {
        sound.stop();
        sound.prepareAsync();
    }
}