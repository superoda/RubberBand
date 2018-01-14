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
	protected int mode;	// 状態管理
	protected int countDown;
	protected int answer;
	protected int score;
	protected int animeSpeed;
	
	// スコアボード用
	private ScoreCenter scoreCenter;
	
	// 広告用ＩＤ
	private static final String  MY_AD_UNIT_ID= "a15122564b15494";
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 乱数発生準備
		long seed = System.currentTimeMillis();
		rand = new Random(seed);
		
		// 画像
		img   = (ImageView)findViewById(R.id.rubber);			// 輪ゴム画像の準備
		hand_L = (ImageView)findViewById(R.id.lhand);			// 左手
		hand_R = (ImageView)findViewById(R.id.rhand);			// 右手
		
		// テキスト領域
		scoreBoard = (TextView)findViewById(R.id.score);		// 点数
		msg   = (TextView)findViewById(R.id.message);			// メッセージ

		// ボタン
		buttonStart = (Button)findViewById(R.id.buttonStart);	// スタートボタン
		
		mode = 0;
		msg.setText("Game Over");

		score=0;
		scoreBoard.setText("score:" + score);
	    
		
		// ****************
		// 以下、スコアボード
		// ****************
		// 4-2. (1) 初期化
		scoreCenter = ScoreCenter.getInstance();
		scoreCenter.initialize(getApplicationContext());		
		
		// 挨拶
		scoreCenter.hello();
		
		
		// ****************
		// 以下、広告
		// ****************
		
		// adView を作成する
	    adView = new AdView(this, AdSize.BANNER, MY_AD_UNIT_ID);

	    // 属性 android:id="@+id/mainLayout" が与えられているものとして
	    // LinearLayout をルックアップする
	    LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

	    // 広告位置の調整
	    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
	    lp.gravity = Gravity.BOTTOM;
	    adView.setLayoutParams(lp);

	    layout.addView(adView);
	    
	    // 一般的なリクエストを行って広告を読み込む  
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
	    	translate = new TranslateAnimation(0, 100, 0, 0); // (0,0)から(100,100)に移動
			translate.setDuration(animeSpeed); // アニメーション
			translate.setFillAfter(true);   //終了後を保持
			translate.setFillEnabled(true);
			img.startAnimation(translate); // アニメーション適用	

		}else{
			translate = new TranslateAnimation(0, -100, 0, 0); // (0,0)から(100,100)に移動
			translate.setDuration(animeSpeed); // アニメーション
			translate.setFillAfter(true);   //終了後を保持
			translate.setFillEnabled(true);
			img.startAnimation(translate); // アニメーション適用	
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
		translate = new TranslateAnimation(0, 0, 0, 0); // (0,0)から(0,0)に移動
		translate.setDuration(0); // 0msかけてアニメーションする
		translate.setFillAfter(true);   //終了後を保持
		translate.setFillEnabled(true);
		img.startAnimation(translate); // アニメーション適用	
		
	}
	
	public void onClickRightHand(View view){
		if (mode ==2)
			judge(RIGHT_HAND);
	}
	
	public void onClickLeftHand(View view){
		if (mode ==2)
			judge(LEFT_HAND);
	}
	
	// 正誤判定
	private void judge(int push){
		if (answer == push) {		// 正解の場合
    		msg.setText("collect!");
			animationReset();
			scoreBoard.setText("score:" + (++score) );

			countDown = 4;
			mode = 1;

			// 両手で輪ゴムを引っ張るアニメ
			int handDist = score / 5;
			
	    	translate = new TranslateAnimation(-20, 0, 0 - handDist, 0); 	// (-50,0)から(0,0)に移動
			translate.setDuration(3000); 						// アニメーション
			translate.setFillAfter(true);   //終了後を保持
			translate.setFillEnabled(true);
			hand_R.startAnimation(translate); // アニメーション適用	

	    	translate = new TranslateAnimation(20, 0, 0 + handDist, 0); 	// (50,0)から(0,0)に移動
			translate.setDuration(3000); 						// アニメーション
			translate.setFillAfter(true);   //終了後を保持
			translate.setFillEnabled(true);
			hand_L.startAnimation(translate); // アニメーション適用	
			
			
			RubberTimer rTimer = new RubberTimer(MainActivity.this);
			timer = new Timer();
	        timer.schedule(rTimer, 1000, 1000);
			
		}else{						// 誤りの場合
    		msg.setText("miss!");
			mode = 0;

			// ネットランキングに整数のスコアを送信
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
