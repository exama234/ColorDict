package jp.co.fexd.color;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyListener implements View.OnTouchListener{

	public boolean onTouch(View v, MotionEvent event) {
		Log.d(this.getClass().getName(), "�^�b�`�C�x���g");
		return false;
	}

}
