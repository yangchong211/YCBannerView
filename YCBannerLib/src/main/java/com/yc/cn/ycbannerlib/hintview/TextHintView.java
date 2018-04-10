package com.yc.cn.ycbannerlib.hintview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.yc.cn.ycbannerlib.inter.BaseHintView;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/3/18
 *     desc  : 文本
 *     revise:
 * </pre>
 */
public class TextHintView extends AppCompatTextView implements BaseHintView {

	private int length;
	public TextHintView(Context context){
		super(context);
	}
	
	public TextHintView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void initView(int length, int gravity) {
		this.length = length;
		setTextColor(Color.WHITE);
		switch (gravity) {
			case 0:
				setGravity(Gravity.START| Gravity.CENTER_VERTICAL);
				break;
			case 1:
				setGravity(Gravity.CENTER);
				break;
			case 2:
				setGravity(Gravity.END| Gravity.CENTER_VERTICAL);
				break;
			default:
				break;
		}

        setCurrent(0);
	}

	@SuppressLint("SetTextI18n")
	@Override
	public void setCurrent(int current) {
		setText(current+1+"/"+ length);
	}

}
