package com.example.recylerviewitemexpand;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PRABHAT on 8/19/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    int listWidth;
    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }


    private Context mContext;

    public RecyclerAdapter(Context context) {
        mContext = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt1, txt2;
        public View rootView, leftView, rightView;
        private ItemClickListener clickListener;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            leftView = (View) v.findViewById(R.id.left_view);
            rightView = (View) v.findViewById(R.id.right_view);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(getActivity(),"click listener1", Toast.LENGTH_SHORT).show();

            clickListener.onClick(v, getPosition(), false);
        }
    }


    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        listWidth = parent.getWidth();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final int expandWidth = (int)(listWidth * 0.7);
        final int collepseWidth = (int) (listWidth * 0.3);



        holder.leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                expand(v, holder.rightView, expandWidth, collepseWidth);
            }
        });

        holder.rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                expand(v, holder.leftView, expandWidth, collepseWidth);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static void expand(final View selectedView, final View otherView, final int expandWidth, final int collepseWidth) {
        //if(selectedView.getMeasuredWidth() >= expandWidth) return;
        if(selectedView.getTag()!= null) {
            if ("Expanded".equalsIgnoreCase(selectedView.getTag().toString())) return;
        }

        selectedView.measure(expandWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        otherView.measure(collepseWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        int sWidth = selectedView.getMeasuredWidth();
        int oWidth = otherView.getMeasuredWidth();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                selectedView.getLayoutParams().width = interpolatedTime == 1
                        ? expandWidth
                        : (int) (expandWidth * interpolatedTime);
                selectedView.requestLayout();

                otherView.getLayoutParams().width = interpolatedTime == 1
                        ? collepseWidth
                        : (int) (collepseWidth * interpolatedTime);
                otherView.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (expandWidth / selectedView.getContext().getResources().getDisplayMetrics().density)*2);
        selectedView.startAnimation(a);

        selectedView.setTag("Expanded");
        otherView.setTag("Collepsed");
    }

}