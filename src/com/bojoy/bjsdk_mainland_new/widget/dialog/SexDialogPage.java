package com.bojoy.bjsdk_mainland_new.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bojoy.bjsdk_mainland_new.app.tools.BJMGFSDKTools;
import com.bojoy.bjsdk_mainland_new.congfig.SysConstant;
import com.bojoy.bjsdk_mainland_new.model.entity.UserData;
import com.bojoy.bjsdk_mainland_new.support.http.HttpUtility;
import com.bojoy.bjsdk_mainland_new.utils.*;

public class SexDialogPage {


    private float Landscape_Width_Percent_Normal = 0.6f;
    private float Portrait_Width_Percent_Normal = 0.9f;
    private final float Width_Percent_Full = 1.0f;

    private final int Size_Min = 300;
    private final int Size_Max = 850;


    private ImageView maleImage;
    private ImageView femaleImage;
    private LinearLayout maleImageLayout;
    private LinearLayout femaleImageLayout;
    private boolean maleCheck;
    private boolean femaleCheck;
    private Button okBtn;

    private static SexDialogPage sexDialogPage;
    Context mContext;

    private SexDialogPage() {
    }

    public static SexDialogPage getInstance() {

        if (sexDialogPage == null) {
            synchronized (HttpUtility.class) {
                if (sexDialogPage == null) {
                    sexDialogPage = new SexDialogPage();
                }
            }
        }
        return sexDialogPage;
    }


    /**
     * 打开选择性别对话框
     *
     * @param context
     * @return
     */

    public Dialog openSexChooiceDialog( final Context context,  final TextView txtSex, final ImageView imgIcon, final UserData userData, final boolean isModifyHead) {
        this.mContext = context;
        final Dialog dialog = new Dialog(context, ReflectResourceId.getStyleId(context, Resource.style.bjmgf_sdk_Dialog));
        dialog.setContentView(ReflectResourceId.getLayoutId(context, Resource.layout.bjmgf_sdk_dock_sex_selecor));
        Window window = dialog.getWindow();

        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = window.getAttributes();
        boolean landscape = BJMGFSDKTools.getInstance().getScreenOrientation() ==
                SysConstant.BJMGF_Screen_Orientation_Landscape;
        params.width = getFitWidth(dm.widthPixels, landscape, false);
        params.height = getFitHeight(dm.heightPixels, landscape, false);
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        maleImage = (ImageView) dialog.findViewById(ReflectResourceId.getViewId(mContext,
                Resource.id.bjmgf_sdk_sex_male_image));
        femaleImage = (ImageView) dialog.findViewById(ReflectResourceId.getViewId(mContext,
                Resource.id.bjmgf_sdk_sex_female_image));
        maleImageLayout = (LinearLayout) dialog.findViewById(ReflectResourceId.getViewId(mContext,
                Resource.id.bjmgf_sdk_sex_selector_male_LayoutId));

        femaleImageLayout = (LinearLayout) dialog.findViewById(ReflectResourceId.getViewId(mContext,
                Resource.id.bjmgf_sdk_sex_selector_female_LayoutId));
        okBtn = (Button) dialog.findViewById(ReflectResourceId.getViewId(mContext,
                Resource.id.bjmgf_sdk_sex_ok_button));
        if (userData.getSex()!=null) {
            if (userData.getSex().equals("1")) {
                maleImage.setImageResource(ReflectResourceId.getDrawableId(mContext,
                        Resource.drawable.bjmgf_sdk_checkbox_bg_selected));
            } else {
                femaleImage.setImageResource(ReflectResourceId.getDrawableId(mContext,
                        Resource.drawable.bjmgf_sdk_checkbox_bg_selected));
            }
        }

        maleImageLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickSex(maleImage);
            }
        });
        femaleImageLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickSex(femaleImage);
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!femaleCheck && !maleCheck) {
                    ToastUtil.showMessage(mContext, mContext.getString(ReflectResourceId.getStringId(mContext, Resource.string.bjmgf_sdk_input_sex_title)));
                    return;
                }
                txtSex.setText(femaleCheck ? mContext.getString(ReflectResourceId.getStringId(mContext,Resource.string.bjmgf_sdk_sex_female)) : mContext.getString(ReflectResourceId.getStringId(mContext,Resource.string.bjmgf_sdk_sex_male)));
                if(!isModifyHead)
                imgIcon.setImageResource(femaleCheck ? ReflectResourceId.getDrawableId(mContext,Resource.drawable.bjmgf_sdk_user_default_icon_female):ReflectResourceId.getDrawableId(mContext,Resource.drawable.bjmgf_sdk_user_default_icon_male));
                txtSex.setTextColor(context.getResources().getColor(
                        ReflectResourceId.getColorId(context,
                                Resource.color.bjmgf_sdk_black)));
                userData.setSex(femaleCheck ? "0":"1");
                dialog.cancel();
            }
        });


        return dialog;

    }


    private void checkSexBox(ImageView sexImage, boolean checked) {
        if (sexImage == null) {
            return;
        }
        sexImage.setImageResource(checked ?
                ReflectResourceId.getDrawableId(mContext,
                        Resource.drawable.bjmgf_sdk_checkbox_bg_selected) :
                ReflectResourceId.getDrawableId(mContext,
                        Resource.drawable.bjmgf_sdk_checkbox_bg_unselected));
    }

    private void clickSex(ImageView sexImage) {
        if (sexImage == null) {
            return;
        }
        if (sexImage.equals(maleImage)) {
            if (maleCheck) {
                return;
            }
            maleCheck = true;
            femaleCheck = false;
        }

        if (sexImage.equals(femaleImage)) {
            if (femaleCheck) {
                return;
            }
            femaleCheck = true;
            maleCheck = false;
        }
        checkSexBox(maleImage, maleCheck);
        checkSexBox(femaleImage, femaleCheck);
    }


    private int getFitWidth(int screenWidth, boolean landscape, boolean full) {
        int width = 0;
        if (full) {
            width = (int) (screenWidth * Width_Percent_Full);
        } else {
            width = (int) (landscape ? screenWidth * Landscape_Width_Percent_Normal
                    : screenWidth * Portrait_Width_Percent_Normal);
        }
        width = checkMax(width);
        width = checkMin(width);
        return width;
    }

    private int getFitHeight(int screenHeight, boolean landscape, boolean full) {
        /** 窗体高度自适应 */
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private int checkMin(int size) {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return size < Size_Min ? Size_Min : size;
    }

    private int checkMax(int size) {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        if (!Utility.isTablet(mContext)) {
            return size;
        }
        return size > Size_Max ? Size_Max : size;
    }


}
