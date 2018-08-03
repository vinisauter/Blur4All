package vas.com.blur4all.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import vas.com.blur4all.BluredActivity;
import vas.com.blur4all.R;

/**
 * Created by user on 03/08/2018.
 */

public class BluredActivitySample extends BluredActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blured);
        //...
    }

    public static IntentBuilder intent(Context context) {
        return new IntentBuilder(context, BluredActivitySample.class);
    }

    public static IntentBuilder intent(android.app.Fragment fragment) {
        return new IntentBuilder(fragment, BluredActivitySample.class);
    }

    public static IntentBuilder intent(android.support.v4.app.Fragment supportFragment) {
        return new IntentBuilder(supportFragment, BluredActivitySample.class);
    }
}
