package vas.com.blur4all;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created vinicius on 22/02/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BluredActivity extends Activity {

    private static Bitmap blurred;
    int paddingTop = 0;
    int paddingLeft = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rect visibleFrame = new Rect();
        ViewGroup mRoot = (ViewGroup) getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);
        paddingTop = visibleFrame.top;
        paddingLeft = visibleFrame.left;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRoot.setBackground(new BitmapDrawable(getResources(), blurred));
        } else {
            mRoot.setBackgroundDrawable(new BitmapDrawable(blurred));
        }
    }

    //TODO: class from child
    public static BluredActivity.IntentBuilder intent(Context context) {
        return new BluredActivity.IntentBuilder(context, BluredActivity.class);
    }

    //TODO: class from child
    public static BluredActivity.IntentBuilder intent(android.app.Fragment fragment) {
        return new BluredActivity.IntentBuilder(fragment, BluredActivity.class);
    }

    //TODO: class from child
    public static BluredActivity.IntentBuilder intent(android.support.v4.app.Fragment supportFragment) {
        return new BluredActivity.IntentBuilder(supportFragment, BluredActivity.class);
    }

    private static void setBlurImage(Activity from) {
        Rect visibleFrame = new Rect();
        ViewGroup mRoot = (ViewGroup) from.getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);
        Bitmap bitmap = Util.drawViewToBitmap(mRoot, mRoot.getWidth(), mRoot.getHeight(), 0, 0, 3);
        blurred = Blur.apply(from, bitmap);
        bitmap.recycle();
    }

    public static class IntentBuilder<T extends BluredActivity> {
        private android.app.Fragment fragment;
        private android.support.v4.app.Fragment fragmentSupport;
        final Context context;
        final Intent intent;

        public static Activity getActivity(Context context) {
            if (context == null) return null;
            if (context instanceof Activity) return (Activity) context;
            if (context instanceof ContextWrapper)
                return getActivity(((ContextWrapper) context).getBaseContext());
            return null;
        }

        public IntentBuilder(Context context, Intent intent) {
            this.context = context;
            this.intent = intent;

            Activity activity = getActivity(context);
            if (activity != null)
                setBlurImage(activity);
        }

        public IntentBuilder(Context context, Class<? extends BluredActivity> clazz) {
            this(context, new Intent(context, clazz));
        }

        public IntentBuilder(android.app.Fragment fragment, Class<? extends BluredActivity> clazz) {
            this(fragment.getActivity(), clazz);
            this.fragment = fragment;
        }

        public IntentBuilder(android.support.v4.app.Fragment fragment, Class<? extends BluredActivity> clazz) {
            this(fragment.getActivity(), clazz);
            fragmentSupport = fragment;
        }

        public void start() {
            startForResult(-1);
        }

        public void startForResult(int requestCode) {
            if (fragmentSupport != null) {
                fragmentSupport.startActivityForResult(intent, requestCode);
            } else {
                if (fragment != null) {
                    fragment.startActivityForResult(intent, requestCode);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, new Bundle());
                    } else {
                        context.startActivity(intent, new Bundle());
                    }
                }
            }
        }

        public Context getContext() {
            return context;
        }

        public Intent get() {
            return intent;
        }

        public IntentBuilder flags(int flags) {
            intent.setFlags(flags);
            return this;
        }

        public IntentBuilder action(String action) {
            intent.setAction(action);
            return this;
        }

        public IntentBuilder type(String type) {
            intent.setType(type);
            return this;
        }

        public IntentBuilder category(String category) {
            intent.addCategory(category);
            return this;
        }

        public IntentBuilder data(Uri data) {
            intent.setData(data);
            return this;
        }

        public IntentBuilder extra(String name, boolean value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, byte value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, char value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, short value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, int value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, long value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, float value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, double value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, String value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, CharSequence value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, Parcelable value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, Parcelable[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder parcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
            intent.putParcelableArrayListExtra(name, value);
            return this;
        }

        public IntentBuilder integerArrayListExtra(String name, ArrayList<Integer> value) {
            intent.putIntegerArrayListExtra(name, value);
            return this;
        }

        public IntentBuilder stringArrayListExtra(String name, ArrayList<String> value) {
            intent.putStringArrayListExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, Serializable value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, boolean[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, byte[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, short[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, char[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, int[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, long[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, float[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, double[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, String[] value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extra(String name, Bundle value) {
            intent.putExtra(name, value);
            return this;
        }

        public IntentBuilder extras(Intent src) {
            intent.putExtras(src);
            return this;
        }
    }
}