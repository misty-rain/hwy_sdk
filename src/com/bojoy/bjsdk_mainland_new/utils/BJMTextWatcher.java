package com.bojoy.bjsdk_mainland_new.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 输入框监听类
 * @author qiuzhiyuan
 *
 */
public class BJMTextWatcher implements TextWatcher {
	
	private EditText editText = null;
	
	public BJMTextWatcher(EditText editText) {
		this.editText = editText;
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String editable = editText.getText().toString();
    	String str = Utility.filterUTF8(s.toString());
        if (!editable.equals(str)) {
        	editText.setText(str);
        	editText.setSelection(str.length());
		}
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}
