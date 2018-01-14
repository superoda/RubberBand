package com.koaladynamo.rubberband;

import java.util.TimerTask;
import android.os.Handler;
import android.view.View;

public class RubberTimer extends TimerTask {

	private Handler handler;
	private MainActivity act;

	public RubberTimer(MainActivity act) {
		handler = new Handler();
		this.act = act;
	}

	@Override
	public void run() {
		handler.post(new Runnable() {
			@Override
			public void run() {

				if (act.mode == 1) {
					if (--act.countDown == 0) {

						act.answer = act.animation();
						act.msg.setText("Which ?");
						act.mode = 2;
						act.timer.cancel();

					} else {
						act.msg.setText("" + act.countDown);
					}

				} else if (act.mode == 0) {
					act.msg.setText("Game Over");
					act.animationReset();
					act.buttonStart.setVisibility(View.VISIBLE);
					act.timer.cancel();

				} else {
					act.msg.setText("!! Error !!");
					act.timer.cancel();

				}
			}
		});
	}
}