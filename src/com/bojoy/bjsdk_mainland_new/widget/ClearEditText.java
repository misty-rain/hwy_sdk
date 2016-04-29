package com.bojoy.bjsdk_mainland_new.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;
import com.bojoy.bjsdk_mainland_new.utils.StringUtility;
import com.bojoy.bjsdk_mainland_new.utils.Utility;


/**
 * 带清除按钮的单行输入框
 * 增加：
 * 标题
 *
 * @author sunhaoyang
 */
public class ClearEditText extends RelativeLayout implements TextWatcher,
        OnClickListener {

    private final String TAG = ClearEditText.class.getSimpleName();

    private EditText editText;
    private ImageView clearImage;
    private TextView textView;
    private final int PaddingTop = 10;
    private final int PaddingBottom = 16;
    private final int MarginRight = 10;

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        editText = new EditText(context, attrs);
        initWithAttrs(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        editText = new EditText(context, attrs, defStyle);
        initWithAttrs(context, attrs);
    }

    private void initWithAttrs(Context context, AttributeSet attrs) {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(editText, params);
        /**
         * clearedit:clear_src —— 清除按钮图片资源
         * 目前公司在打包时会修改package名，造成编译状态的
         * R文件和打包后的R文件不是同一个，使用默认的删除按钮
         */
        int[] array = ReflectResourceId.getStyleableArray(context,
                Resource.styleable.bjmgf_sdk_clearEdit);
        int drawableId = 0;
        String warnTitle = "";
        if (array != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, array);
            drawableId = a.getResourceId(ReflectResourceId.getStyleable(
                    context, Resource.styleable.bjmgf_sdk_clearEdit_clear_src), 0);
            warnTitle = a.getString(ReflectResourceId.getStyleable(
                    context, Resource.styleable.bjmgf_sdk_clearEdit_warn_title));
            if (warnTitle == null) {
                warnTitle = "";
            }
        } else {
            drawableId = ReflectResourceId.getDrawableId(context, Resource.drawable.bjmgf_sdk_button_gray_close);
        }
        clearImage = new ImageView(context);
        clearImage.setScaleType(ScaleType.FIT_CENTER);
        clearImage.setPadding(0, PaddingTop, 0, PaddingBottom);
        if (warnTitle.equals("")) {
            editText.setPadding((int) ((context.getResources().getDimension(ReflectResourceId.getDimenId(context, Resource.dimen.bjmgf_sdk_margin5)))), 0, 0, 0);
        } else {
            editText.setPadding((int) (warnTitle.length() * (context.getResources().getDimension(ReflectResourceId.getDimenId(context, Resource.dimen.bjmgf_sdk_font_size14)) + context.getResources().getDimension(ReflectResourceId.getDimenId(context, Resource.dimen.bjmgf_sdk_margin15)))), 0, 0, 0);
        }
        if (drawableId > 0) {
            clearImage.setImageResource(drawableId);
        }
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins((int) (context.getResources().getDimension(ReflectResourceId.getDimenId(context, Resource.dimen.bjmgf_sdk_margin15))), 0, 0, 0);
        textView = new TextView(context);
        textView.setText(warnTitle);
        textView.setTextColor(context.getResources().getColor(ReflectResourceId.getColorId(context, Resource.color.bjmgf_sdk_black)));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        addView(textView, params);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.rightMargin = MarginRight;
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_TOP, editText.getId());
        params.addRule(RelativeLayout.ALIGN_BOTTOM, editText.getId());
        params.setMargins(0, 0, (int) context.getResources().getDimension(ReflectResourceId.getDimenId(context, Resource.dimen.bjmgf_sdk_margin10)), 0);
        addView(clearImage, params);
        editText.addTextChangedListener(this);
        clearImage.setOnClickListener(this);
        editText.setText("");
    }

    @Override
    public void onClick(View arg0) {
        editText.setText("");
    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        if (StringUtility.isEmpty(editText.getText().toString())) {
            clearImage.setVisibility(View.GONE);
        } else {
            if (clearImage.getVisibility() != View.VISIBLE) {
                clearImage.setVisibility(View.VISIBLE);
                /** bug 修复1 */
                /** 将clearImage的尺寸和MarginRight的尺寸最为editText的PaddingRight  */
                /**
                 * 获取View的尺寸
                 */
                int[] size = Utility.getViewSize(clearImage);
                int rightPadding = MarginRight * 2 + size[0];
                editText.setPadding(editText.getPaddingLeft(), editText.getPaddingTop(),
                        rightPadding, editText.getPaddingBottom());
//			    LogProxy.i(TAG, "clearImage width = " + size[0]);
            }
        }
    }

    /**
     * 获取ClearEditText的文本内容
     *
     * @return
     */
    public String getEditText() {
        return editText.getText().toString();
    }

    /**
     * 设置ClearEditText的文本内容
     *
     * @return
     */
    public String setEditText(String s) {
        editText.setText(s);
        return null;
    }

    /**
     * 获取editText控件
     *
     * @return
     */
    public EditText getEdit() {
        return editText;
    }

}
