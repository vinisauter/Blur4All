package vas.com.blur4all;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RemoteViews;

/**
 * Created by Vinicius on 26/09/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@RemoteViews.RemoteView
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class BlurredImageView extends android.support.v7.widget.AppCompatImageView {
    private int blurRadius = 10;
    private float scale = 1;

    public BlurredImageView(Context context) {
        super(context);
    }

    public BlurredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs, 0);
    }

    public BlurredImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs, defStyleAttr);
    }

    void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BlurredImageView, defStyleAttr, 0);
        scale = a.getFloat(R.styleable.BlurredImageView_downScale, 0);
        blurRadius = a.getInteger(R.styleable.BlurredImageView_blurRadius, 10);
        Drawable srcTest = a.getDrawable(R.styleable.BlurredImageView_srcTest);
        a.recycle();
        if (isInEditMode() && srcTest != null) {
            setImageDrawable(srcTest);
        } else setImageDrawable(getDrawable());
    }

    public void setScale(int scale) {
        this.scale = scale;
        invalidate();
        requestLayout();
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
        invalidate();
        requestLayout();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (blurRadius <= 0.0F) {
            return;
        }
        if (blurRadius > 25.0F) {
            blurRadius = 25;
        }

        if (isInEditMode()) BlurredImageView.super.setImageDrawable(doBlur(drawable));
        else new BlurTask().execute(drawable);
    }

    private Drawable doBlur(Drawable drawable) {
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            int maxWidth = getMaxWidth();
            if (width < maxWidth)
                maxWidth = width;
            int maxHeight = getMaxHeight();
            if (height < maxHeight)
                maxHeight = height;

            if (width > height) {
                // landscape
                float ratio = (float) width / maxWidth;
                width = maxWidth;
                height = (int) (height / ratio);
            } else if (height > width) {
                // portrait
                float ratio = (float) height / maxHeight;
                height = maxHeight;
                width = (int) (width / ratio);
            } else {
                // square
                height = maxHeight;
                width = maxWidth;
            }
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(((BitmapDrawable) drawable).getBitmap(), width / 3, height / 3);
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if (!isInEditMode())
                if (0 < scale && scale < 1) {
                    int dstWidth = (int) (drawable.getIntrinsicWidth() * scale);
                    int dstHeight = (int) (drawable.getIntrinsicHeight() * scale);
                    bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, false);
                }
            Bitmap blured = Blur.apply(getContext(), bitmap, blurRadius);
            drawable = new BitmapDrawable(getResources(), blured);
        }
        return drawable;
    }

    @SuppressLint("StaticFieldLeak")
    private class BlurTask extends AsyncTask<Drawable, Integer, Drawable> {
        Drawable drawable_old;

        @Override
        protected Drawable doInBackground(Drawable... drawables) {
            Drawable drawable = drawables[0];
            drawable_old = drawable;
            return doBlur(drawable);
        }

        @Override
        protected void onPostExecute(Drawable drawable_new) {
            if (drawable_old != null && drawable_new != null) {
//            BlurredImageView.super.setImageDrawable(drawable);
                Drawable[] layers = new Drawable[2];
                layers[0] = drawable_old;
                layers[1] = drawable_new;
                TransitionDrawable transition = new TransitionDrawable(layers);
                BlurredImageView.super.setImageDrawable(transition);
                transition.startTransition(800);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
