package zentech.myapplication.Entities;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import java.lang.reflect.Method;

/**
 * Created by ad05n on 3/4/2017.
 */

public class ExpandCollapsViewHandler
{
    private static final long SPEED_ANIMATION_TRANSITION = 5;

    public static Animation expand(final View v, final boolean expand) {
        try {
            Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
            m.setAccessible(true);
            m.invoke(
                    v,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(((View)v.getParent()).getMeasuredWidth(), View.MeasureSpec.AT_MOST)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int initialHeight = v.getMeasuredHeight();

        if (expand) {
            v.getLayoutParams().height = 0;
        }
        else {
            v.getLayoutParams().height = initialHeight;
        }
        v.setVisibility(View.VISIBLE);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                int newHeight = 0;
                if (expand) {
                    newHeight = (int) (initialHeight * interpolatedTime);
                } else {
                    newHeight = (int) (initialHeight * (1 - interpolatedTime));
                }
                v.getLayoutParams().height = newHeight;
                v.requestLayout();

                if (interpolatedTime == 1 && !expand)
                    v.setVisibility(View.GONE);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(SPEED_ANIMATION_TRANSITION);
        return a;
    }
}
