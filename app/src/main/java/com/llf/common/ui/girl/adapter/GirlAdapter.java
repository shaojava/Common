package com.llf.common.ui.girl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.commonwidget.CircleImageView;
import com.llf.basemodel.recycleview.BaseViewHolder;
import com.llf.basemodel.utils.ImageLoaderUtils;
import com.llf.common.R;
import com.llf.common.entity.JcodeEntity;

import java.util.List;

/**
 * Created by llf on 2017/4/19.
 * 发现的适配器，分为两种样式
 */

public class GirlAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final int TYPE_FOOTER = 0;
    private static final int ITEM_NOIMAGE = 1;
    private static final int ITEM_HASIMAGE = 2;

    private List<JcodeEntity> data;
    private Context mContext;
    private int viewFooter;
    private View footerView;
    private OnItemClickListener mOnItemClickListener;
    private static final String HOST = "http://www.jcodecraeer.com";

    public GirlAdapter(List<JcodeEntity> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    public void replaceAll(List<JcodeEntity> elements) {
        if (data.size() > 0) {
            data.clear();
        }
        data.addAll(elements);
        notifyDataSetChanged();
    }

    public void addAll(List<JcodeEntity> elements) {
        data.addAll(elements);
        notifyDataSetChanged();
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_NOIMAGE) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_jcode_no_image, parent, false));
        } else if (viewType == ITEM_HASIMAGE) {
            return new BaseViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_jcode_has_image, parent, false));
        } else {
            footerView = LayoutInflater.from(mContext).inflate(viewFooter, parent, false);
            return new BaseViewHolder(footerView);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (!(viewFooter != 0 && position == getItemCount() - 1)) {
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(position);
                    }
                });
            }

            int type = getItemViewType(position);
            final JcodeEntity item = data.get(position);
            if (type == ITEM_HASIMAGE) {
                ImageLoaderUtils.loadingImg(mContext, (ImageView) holder.getView(R.id.cover), HOST + item.getImgUrl());
            }
            CircleImageView avatar = holder.getView(R.id.avatar);
            holder.setText(R.id.title, item.getTitle());
            holder.setText(R.id.content, item.getContent());
            holder.setText(R.id.author, item.getAuthor());
            holder.setText(R.id.seeNum, item.getWatch());
            holder.setText(R.id.commentNum, item.getComments());
            holder.setText(R.id.loveNum, item.getLike());
            ImageLoaderUtils.loadingImg(mContext, avatar, HOST + item.getAuthorImg());
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.lanuch(mContext, HOST + "/member/index.php?uid" + item.getAuthor());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if (viewFooter != 0) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int type = ITEM_HASIMAGE;
        if (viewFooter != 0 && position == getItemCount() - 1) {
            type = TYPE_FOOTER;
            return type;
        }
        if (TextUtils.isEmpty(data.get(position).getImgUrl())) {
            type = ITEM_NOIMAGE;
        } else {
            type = ITEM_HASIMAGE;
        }
        return type;
    }

    public void addFooterView(int footerView) {
        this.viewFooter = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setFooterVisible(int visible) {
        footerView.setVisibility(visible);
    }

    //设置点击事件
    public void setOnItemClickListener(OnItemClickListener mListener) {
        mOnItemClickListener = mListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
