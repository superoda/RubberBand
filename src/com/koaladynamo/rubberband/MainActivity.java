package com.koaladynamo.rubberband;

import java.util.Random;
import java.util.Timer;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.maru.scorecenter.ScoreCenter;

public class MainActivity extends Activity {

	public static final int RIGHT_HAND = 0;
	public static final int LEFT_HAND = 1;
	
	Timer timer;

	ImageView img;
	ImageView hand_L;
	ImageView hand_R;
	
	TextView  scoreBoard;
	TextView  msg;
	Button buttonStart;
	
	TranslateAnimation translate;
	private Random rand ;
	protected int mode;	// ��ԊǗ�
	protected int countDown;
	protected int answer;
	protected int score;
	protected int animeSpeed;
	
	// �X�R�A�{�[�h�p
	private ScoreCenter scoreCenter;
	
	// �L���p�h�c
	private static final String  MY_AD_UNIT_ID= "a15122564b15494";
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ������������
		long seed = System.currentTimeMillis();
		rand = new Random(seed);
		
		// �摜
		img   = (ImageView)findViewById(R.id.rubber);			// �փS���摜�̏���
		hand_L = (ImageView)findViewById(R.id.lhand);			// ����
		hand_R = (ImageView)findViewById(R.id.rhand);			// �E��
		
		// �e�L�X�g�̈�
		scoreBoard = (TextView)findViewById(R.id.score);		// �_��
		msg   = (TextView)findViewById(R.id.message);			// ���b�Z�[�W

		// �{�^��
		buttonStart = (Button)findViewById(R.id.buttonStart);	// �X�^�[�g�{�^��
		
		mode = 0;
		msg.setText("Game Over");

		score=0;
		scoreBoard.setText("score:" + score);
	    
		
		// ****************
		// �ȉ��A�X�R�A�{�[�h
		// ****************
		// 4-2. (1) ������
		scoreCenter = ScoreCenter.getInstance();
		scoreCenter.initialize(getApplicationContext());		
		
		// ���A
		scoreCenter.hello();
		
		
		// ****************
		// �ȉ��A�L��
		// ****************
		
		// adView ���쐬����
	    adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);

	    // ���� android:id="@+id/mainLayout" ���^�����Ă�����̂Ƃ���
	    // LinearLayout �����b�N�A�b�v����
	    LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

	    // �L���ʒu�̒���
	    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
	    lp.gravity = Gravity.BOTTOM;
	    adView.setLayoutParams(lp);

	    layout.addView(adView);
	    
	    // ��ʓI�ȃ��N�G�X�g���s���čL����ǂݍ���  
	    adView.loadAd(new AdRequest());

	}
	
	public int animation(){
		
    	int num = rand.nextInt(2);
    	
    	if (animeSpeed>60) 
    		animeSpeed-=20;
    	else if (animeSpeed>30)
    		animeSpeed-=5;
    	else if (animeSpeed>15)
    		animeSpeed-=2;
    	else if (animeSpeed>7)
    		animeSpeed--;
    	
    	
		if (num==RIGHT_HAND){
	    	translate = new TranslateAnimation(0, 100, 0, 0); // (0,0)����(100,100)�Ɉړ�
			translate.setDuration(animeSpeed); // �A�j���[�V����
			translate.setFillAfter(true);   //�I�����ێ�
			translate.setFillEnabled(true);
			img.startAnimation(translate); // �A�j���[�V�����K�p	

		}else{
			translate = new TranslateAnimation(0, -100, 0, 0); // (0,0)����(100,100)�Ɉړ�
			translate.setDuration(animeSpeed); // �A�j���[�V����
			translate.setFillAfter(true);   //�I�����ێ�
			translate.setFillEnabled(true);
			img.startAnimation(translate); // �A�j���[�V�����K�p	
    	}
		return num;
	}
	
	public void onClickStart(View view){

    	if (mode == 0){
    		
    		score = 0;
    		animeSpeed = 380;
    		scoreBoard.setText("score:" + score);
    		
    		buttonStart.setVisibility(View.INVISIBLE);
    		mode = 1;
    		countDown = 4;
    		msg.setText("Ready!");
    		
			RubberTimer rTimer = new RubberTimer(MainActivity.this);
			timer = new Timer();
	        timer.schedule(rTimer, 1000, 1000);
    		
    		
    	}
    	
    }
    
	public void animationReset(){
		translate = new TranslateAnimation(0, 0, 0, 0); // (0,0)����(0,0)�Ɉړ�
		translate.setDuration(0); // 0ms�����ăA�j���[�V��������
		translate.setFillAfter(true);   //�I�����ێ�
		translate.setFillEnabled(true);
		img.startAnimation(translate); // �A�j���[�V�����K�p	
		
	}
	
	public void onClickRightHand(View view){
		if (mode ==2)
			judge(RIGHT_HAND);
	}
	
	public void onClickLeftHand(View view){
		if (mode ==2)
			judge(LEFT_HAND);
	}
	
	// ���딻��
	private void judge(int push){
		if (answer == push) {		// �����̏ꍇ
    		msg.setText("collect!");
			animationReset();
			scoreBoard.setText("score:" + (++score) );

			countDown = 4;
			mode = 1;

			// ����ŗփS������������A�j��
			int handDist = score / 5;
			
	    	translate = new TranslateAnimation(-20, 0, 0 - handDist, 0); 	// (-50,0)����(0,0)�Ɉړ�
			translate.setDuration(3000); 						// �A�j���[�V����
			translate.setFillAfter(true);   //�I�����ێ�
			translate.setFillEnabled(true);
			hand_R.startAnimation(translate); // �A�j���[�V�����K�p	

	    	translate = new TranslateAnimation(20, 0, 0 + handDist, 0); 	// (50,0)����(0,0)�Ɉړ�
			translate.setDuration(3000); 						// �A�j���[�V����
			translate.setFillAfter(true);   //�I�����ێ�
			translate.setFillEnabled(true);
			hand_L.startAnimation(translate); // �A�j���[�V�����K�p	
			
			
			RubberTimer rTimer = new RubberTimer(MainActivity.this);
			timer = new Timer();
	        timer.schedule(rTimer, 1000, 1000);
			
		}else{						// ���̏ꍇ
    		msg.setText("miss!");
			mode = 0;

			// �l�b�g�����L���O�ɐ����̃X�R�A�𑗐M
			scoreCenter.postScore("com.koaladynamo.rubberband_scoreboard", java.lang.String.valueOf(score));
			scoreCenter.show("com.koaladynamo.rubberband_scoreboard");
			
			// 
			RubberTimer rTimer = new RubberTimer(MainActivity.this);
			timer = new Timer();
	        timer.schedule(rTimer, 1000, 1000);

		}
		
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
