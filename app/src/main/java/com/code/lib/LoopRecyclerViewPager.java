package com.code.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;

public class LoopRecyclerViewPager extends RecyclerViewPager {

    public LoopRecyclerViewPager(Context context) {
        this(context, null);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopRecyclerViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        super.scrollToPosition(getMiddlePosition());
    }

    @Override
    @NonNull
    protected RecyclerViewPagerAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {
        return (adapter instanceof LoopRecyclerViewPagerAdapter)
                ? (LoopRecyclerViewPagerAdapter) adapter
                : new LoopRecyclerViewPagerAdapter(this, adapter);
    }

    /**
     * Starts a smooth scroll to an adapter position.
     * if position < adapter.getActualCount,
     * position will be transform to right position.
     *
     * @param position target position
     */
    @Override
    public void smoothScrollToPosition(int position) {
        int transformedPosition = transformInnerPositionIfNeed(position);
        super.smoothScrollToPosition(transformedPosition);
        Log.e("test", "transformedPosition:" + transformedPosition);
    }

    /**
     * Starts a scroll to an adapter position.
     * if position < adapter.getActualCount,
     * position will be transform to right position.
     *
     * @param position target position
     */
    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(transformInnerPositionIfNeed(position));
    }

    /**
     * get actual current position in actual adapter.
     */
    public int getActualCurrentPosition() {
        int position = getCurrentPosition();
        return transformToActualPosition(position);
    }

    /**
     * Transform adapter position to actual position.
     *
     * @param position adapter position
     * @return actual position
     */
    public int transformToActualPosition(int position) {
        if (getAdapter() == null || getAdapter().getItemCount() < 0) {
            return 0;
        }
        return position % getActualItemCountFromAdapter();
    }

    private int getActualItemCountFromAdapter() {
        return ((LoopRecyclerViewPagerAdapter) getWrapperAdapter()).getActualItemCount();
    }

    private int transformInnerPositionIfNeed(int position) {
        final int actualItemCount = getActualItemCountFromAdapter();
        if (actualItemCount == 0)
            return actualItemCount;
        final int actualCurrentPosition = getCurrentPosition() % actualItemCount;
        int bakPosition1 = getCurrentPosition()
                - actualCurrentPosition
                + position % actualItemCount;
        int bakPosition2 = getCurrentPosition()
                - actualCurrentPosition
                - actualItemCount
                + position % actualItemCount;
        int bakPosition3 = getCurrentPosition()
                - actualCurrentPosition
                + actualItemCount
                + position % actualItemCount;
        Log.e("test", bakPosition1 + "/" + bakPosition2 + "/" + bakPosition3 + "/" + getCurrentPosition());
        // get position which is closer to current position
        if (Math.abs(bakPosition1 - getCurrentPosition()) > Math.abs(bakPosition2 -
                getCurrentPosition())) {
            if (Math.abs(bakPosition2 -
                    getCurrentPosition()) > Math.abs(bakPosition3 -
                    getCurrentPosition())) {
                return bakPosition3;
            }
            return bakPosition2;
        } else {
            if (Math.abs(bakPosition1 -
                    getCurrentPosition()) > Math.abs(bakPosition3 -
                    getCurrentPosition())) {
                return bakPosition3;
            }
            return bakPosition1;
        }
    }

    private int getMiddlePosition() {
        int middlePosition = Integer.MAX_VALUE / 2;
        final int actualItemCount = getActualItemCountFromAdapter();
        if (actualItemCount > 0 && middlePosition % actualItemCount != 0) {
            middlePosition = middlePosition - middlePosition % actualItemCount;
        }
        return middlePosition;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        //当拦截触摸事件到达此位置的时候，返回true，
//        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
//        return true;
//    }
//
//    /**
//     * 触摸时按下的点
//     **/
//    PointF downP = new PointF();
//    /**
//     * 触摸时当前的点
//     **/
//    PointF curP = new PointF();
//
//    @Override
//    public boolean onTouchEvent(MotionEvent arg0) {
//        curP.x = arg0.getX();
//        curP.y = arg0.getY();
//        ViewParent view = getParent().getParent().getParent().getParent().getParent();
//        if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
//            //记录按下时候的坐标
//            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//            downP.x = arg0.getX();
//            downP.y = arg0.getY();
//            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            view.requestDisallowInterceptTouchEvent(true);
//        }
//        if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
//            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            view.requestDisallowInterceptTouchEvent(true);
//        }
//        if (arg0.getAction() == MotionEvent.ACTION_UP || arg0.getAction() == MotionEvent.ACTION_CANCEL) {
//            //在up时判断是否按下和松手的坐标为一个点
//            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
////            view.requestDisallowInterceptTouchEvent(true);
////            if(downP.x==curP.x && downP.y==curP.y){
////                return true;
////            }
//            if (downP.x == curP.x && downP.y == curP.y) {
//                for (int i = 0; i < getChildCount(); i++) {
//                    Rect rect = new Rect();
//                    getChildAt(i).getHitRect(rect);
//                    if (rect.contains((int) downP.x, (int) downP.y)) {
//                        if (listener != null) {
//                            listener.click(getChildAt(i), i);
//                            getChildAt(i).performClick();
//                            return true;
//                        }
//                    }
//                }
//                return true;
//            }
//        }
//        return super.onTouchEvent(arg0);
//    }

//    private OnItemClickListener listener;
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnItemClickListener {
//        void click(View view, int position);
//    }
}
