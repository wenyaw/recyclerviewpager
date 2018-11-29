package com.code.recyclerviewpager;

import android.content.Context;
import javax.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ljw. new int[]{R.mipmap.image1, R.mipmap.image2, R.mipmap.image3, R.mipmap.image4, R.mipmap.image5};
 */

public class LoopPagerAdapter extends RecyclerView.Adapter<LoopPagerAdapter.SimpleViewHolder> {
    private static final int DEFAULT_ITEM_COUNT = 3;

    private final Context mContext;
    private List<DataBean> mList = new ArrayList<>();
    private final RecyclerView mRecyclerView;
    private final List<Integer> mItems ;
    private int mCurrentItemId = 0;

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.tv_title)
        TextView title;
        @Nullable
        @BindView(R.id.img_pic)
        ImageView mImgPic;

        public SimpleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public LoopPagerAdapter(Context context, RecyclerView recyclerView, List<DataBean> list) {
        this(context, recyclerView, DEFAULT_ITEM_COUNT,list);
    }

    public LoopPagerAdapter(Context context, RecyclerView recyclerView, int itemCount,List<DataBean> list) {
        mContext = context;
        mList = list;
        mItems = new ArrayList<>(itemCount);
        for (int i = 0; i < list.size(); i++) {
            addItem(i);
        }
        mRecyclerView = recyclerView;
    }

    public void addItem(int position) {
        final int id = mCurrentItemId++;
        mItems.add(position, id);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_item_view, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final DataBean bean = mList.get(position);
        holder.title.setText(bean.getTitle());
        Glide.with(mContext).load(bean.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .centerCrop().into(holder.mImgPic);

        final View itemView = holder.itemView;
        holder.itemView.setTag(bean);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPagerItemClickListener != null){
                    onPagerItemClickListener.onPagerItemClick(bean);
                }
            }
        });
    }


    //热销作品item点击
    public void setOnItemPagerClickLis(OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    private OnPagerItemClickListener onPagerItemClickListener;

    public abstract static class OnPagerItemClickListener {
        public abstract void onPagerItemClick(DataBean bean);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
