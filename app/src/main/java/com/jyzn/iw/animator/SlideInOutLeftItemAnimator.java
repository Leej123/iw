package com.jyzn.iw.animator;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/7/28 0028.
 */
public class SlideInOutLeftItemAnimator extends BaseItemAnimator {
    protected RecyclerView recyclerView;

    public SlideInOutLeftItemAnimator(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    protected void prepareAnimatedAdd(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, -recyclerView.getLayoutManager().getWidth());
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animation.translationX(0)
                .setDuration(getAddDuration())
                .setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        ViewCompat.setTranslationX(view, 0);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        ViewCompat.setTranslationX(view, 0);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
        animation.setDuration(getRemoveDuration())
                .translationX(-recyclerView.getLayoutManager().getWidth())
                .setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        ViewCompat.setTranslationX(view, -recyclerView.getLayoutManager().getWidth());
                        dispatchAddFinished(holder);
                        mRemoveAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }

                    @Override
                    public void onAnimationStart(View view) {
                        dispatchRemoveStarting(holder);
                    }
                }).start();
    }
}
