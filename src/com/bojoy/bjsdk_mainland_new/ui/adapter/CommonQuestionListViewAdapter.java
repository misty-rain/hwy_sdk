package com.bojoy.bjsdk_mainland_new.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bojoy.bjsdk_mainland_new.model.entity.CommonQuestionListViewBean;
import com.bojoy.bjsdk_mainland_new.utils.ReflectResourceId;
import com.bojoy.bjsdk_mainland_new.utils.Resource;

import java.util.ArrayList;

/**
 * 常见问题页面adapter
 *
 * @author shenliuyong
 */
public class CommonQuestionListViewAdapter extends BaseAdapter {
    private ArrayList<CommonQuestionListViewBean> mDatas;
    private Context mContext;
    private LayoutInflater inflater;
    private int[] attr;// 控制隐藏控件的显示

    private ListView mListView;

    public CommonQuestionListViewAdapter(Context context,
                                         ArrayList<CommonQuestionListViewBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.inflater = LayoutInflater.from(context);
        attr = new int[mDatas.size()];
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(ReflectResourceId.getLayoutId(mContext,
                    Resource.layout.bjmgf_sdk_common_questions_item), null);
            viewHolder.titleTextView = (TextView) convertView.findViewById(ReflectResourceId
                    .getViewId(mContext, Resource.id.bjmgf_sdk_common_text_title));
            viewHolder.imageButton = (ImageButton) convertView.findViewById(ReflectResourceId
                    .getViewId(mContext, Resource.id.bjmgf_sdk_questions_put_down));
            viewHolder.imageButton.setFocusable(false);

            viewHolder.answerTextView = (TextView) convertView.findViewById(ReflectResourceId
                    .getViewId(mContext, Resource.id.bjmgf_sdk_common_text_answer));
            viewHolder.titleLayout = convertView.findViewById(ReflectResourceId.getViewId(mContext,
                    Resource.id.bjmgf_sdk_id_questions_title_layout));

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleTextView.setText(mDatas.get(position).getID() + "、"
                + mDatas.get(position).getQuestion());

        if (attr[position] == 1) {
            viewHolder.answerTextView.setVisibility(View.VISIBLE);
            viewHolder.imageButton.setBackgroundResource(ReflectResourceId.getDrawableId(
                    mContext, Resource.drawable.bjmgf_sdk_btn_down));
            viewHolder.answerTextView.setText(mDatas.get(position).getAnswer());
        } else {
            viewHolder.imageButton.setBackgroundResource(ReflectResourceId.getDrawableId(
                    mContext, Resource.drawable.bjmgf_sdk_forward));
            viewHolder.answerTextView.setVisibility(View.GONE);
        }

        // 点击文本所在布局layout，显现隐藏的文本
        viewHolder.titleLayout.setOnClickListener(new MyClickLinster(viewHolder, position));

        return convertView;
    }

    public void setListView(ListView listView) {
        this.mListView = listView;

    }

    class ViewHolder {
        TextView titleTextView;
        TextView answerTextView;
        ImageButton imageButton;
        View titleLayout;
    }

    class MyClickLinster implements OnClickListener {
        private ViewHolder mViewHolder;
        private int mPosition;
        private String mNoticeString;

        public MyClickLinster(ViewHolder viewHolder, int position) {
            this.mViewHolder = viewHolder;
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {

            if (attr[mPosition] == 1) {
                attr[mPosition] = 0;
            } else {
                attr[mPosition] = 1;
            }

            // 为了使notifyDataSetChanged生效
            if (mDatas != null && mDatas.size() > 0) {
                mNoticeString = mDatas.get(0).getAnswer();
                mDatas.get(0).setAnswer("");
                mDatas.get(0).setAnswer(mNoticeString);
                notifyDataSetChanged();
            }

            if (mPosition == (getCount() - 2) || mPosition == (getCount() - 1)) {
                if (mListView != null) {
                    mListView.setSelection(ListView.FOCUS_DOWN);
                }
            }
        }
    }

}
