package vas.com.blur4all;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

/**
 * Created Vinicius on 22/02/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class BlurActionBarDrawerToggle extends ActionBarDrawerToggle {

    private static final int DEFAULT_RADIUS = 10;
    private static final int DEFAULT_DOWN_SAMPLING = 3;

    private Activity mActivity;

    private View mContainer;
    private ImageView mBlurImage;

    private int mBlurRadius;
    private int mDownSampling;

    public BlurActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
                                     int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
        init(activity);
    }

    public BlurActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar,
                                     int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        init(activity);
    }

    public void setBlurImage() {
        mBlurImage.setImageBitmap(null);
        mBlurImage.setVisibility(View.VISIBLE);
        // do the downscaling for faster processing
        Bitmap downScaled = Util.drawViewToBitmap(mContainer, mContainer.getWidth(), mContainer.getHeight(), mDownSampling);
        // apply the blur using the renderscript
        Bitmap blurred = Blur.apply(mActivity, downScaled, mBlurRadius);
        mBlurImage.setImageBitmap(blurred);
        downScaled.recycle();
    }

    public void clearBlurImage() {
        mBlurImage.setVisibility(View.GONE);
        mBlurImage.setImageBitmap(null);
    }

    public void setBlurRadius(int blurRadius) {
        if (0 < blurRadius && blurRadius <= 25) {
            mBlurRadius = blurRadius;
        }
    }

    public void setDownSampling(int downSampling) {
        mDownSampling = downSampling;
    }

    private void init(Activity activity) {
        mActivity = activity;
        mBlurRadius = DEFAULT_RADIUS;
        mDownSampling = DEFAULT_DOWN_SAMPLING;

        mContainer = activity.findViewById(R.id.blur_container);
        mBlurImage = activity.findViewById(R.id.blur_view);
    }

    private void setBlurAlpha(float slideOffset) {
        if (mBlurImage.getVisibility() != View.VISIBLE) {
            setBlurImage();
        }
        Util.setAlpha(mBlurImage, slideOffset);
    }

    @Override
    public void onDrawerSlide(final View drawerView, final float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (slideOffset > 0f) {
            setBlurAlpha(slideOffset);
        } else {
            clearBlurImage();
        }
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        clearBlurImage();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        setBlurAlpha(1);
    }
}
