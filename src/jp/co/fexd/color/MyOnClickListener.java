package jp.co.fexd.color;

import jp.co.fexd.color.data.ColorData;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;

public class MyOnClickListener implements OnClickListener{
	private Activity activity;
	
	public MyOnClickListener(Activity activity) {
		this.activity = activity;
	}
	
	public void onClick(View v) {
		ColorData colorData = (ColorData) v.getTag();
		if (colorData == null) {
			return ;
		}
		
		//�_�C�A���O�̕\��
		AlertDialog.Builder ad = new AlertDialog.Builder(activity);
		ad.setMessage(colorData.getDiscription());
		ad.setPositiveButton("OK",null);
		ad.show();
	}
	
}
