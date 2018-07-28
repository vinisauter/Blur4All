package vas.com.blur4all;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

/**
 * Created vinicius on 22/02/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BluredActivity extends Activity {

    private static Bitmap blurred;
    int paddingTop = 0;
    int paddingLeft = 0;

    public BluredActivity() {
    }

    public static void startActivity(Activity from, Class<?> clazz) {
        setBlurImage(from);
        Intent intent = new Intent(from, clazz);
        from.startActivity(intent);
    }

    public static void startActivity(Activity from, Intent intent) {
        setBlurImage(from);
        from.startActivity(intent);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz, int requestCode) {
        startActivityForResult(from, clazz, requestCode, null);
    }

    public static void startActivityForResult(Activity from, Class<?> clazz, int requestCode, Bundle bundle) {
        setBlurImage(from);
        Intent intent = new Intent(from, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        from.startActivityForResult(intent, requestCode);
    }

    private static void setBlurImage(Activity from) {
        Rect visibleFrame = new Rect();
        ViewGroup mRoot = (ViewGroup) from.getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);
        Bitmap bitmap = Util.drawViewToBitmap(mRoot, mRoot.getWidth(), mRoot.getHeight(), 0, 0, 3);
        blurred = Blur.apply(from, bitmap);
        bitmap.recycle();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rect visibleFrame = new Rect();
        ViewGroup mRoot = (ViewGroup) getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);
        paddingTop = visibleFrame.top;
        paddingLeft = visibleFrame.left;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRoot.setBackground(new BitmapDrawable(getResources(), blurred));
        } else {
            mRoot.setBackgroundDrawable(new BitmapDrawable(blurred));
        }
    }

    public class BlurActivity<T extends Activity> {
        Class<? extends T> value;

        void dos() {
        }

    }
}