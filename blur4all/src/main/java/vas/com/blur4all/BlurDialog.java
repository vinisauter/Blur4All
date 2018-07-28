package vas.com.blur4all;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created Vinicius on 22/02/2016.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class BlurDialog extends DialogFragment {

    private CharSequence mMessage = "";
    private CharSequence mPositiveButtonText;
    private OnClickListener mPositiveButtonClickListener;
    private CharSequence mNegativeButtonText;
    private OnClickListener mNegativeButtonClickListener;
    private View childView;
    private BlurDialogFragmentHelper mHelper;
    private CustomView customView;

    enum CustomViewType {BEFORE, AFTER, OVERRIDE}

    public interface CustomView {
//        CustomViewType CUSTOM_VIEW_TYPE();

        View onCreateView(BlurDialog dialog, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    }

    public BlurDialog() {
        super();
    }

    public static BlurDialog newInstance() {
        BlurDialog blurDialog = new BlurDialog();
        Bundle args = new Bundle();
//        args.putString("msg", msg);
        blurDialog.setArguments(args);
        return blurDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new BlurDialogFragmentHelper(this);
        mHelper.onCreate();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.customView != null) {
            return this.customView.onCreateView(this, inflater, container, savedInstanceState);
        }
        final View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mMessage.toString())) {
            tv_msg.setVisibility(View.VISIBLE);
            tv_msg.setText(mMessage);
        }
        LinearLayout ll_container = view.findViewById(R.id.ll_container);
        if (childView != null) {
            ll_container.addView(childView, 1);
        }
        Button bt_negative = view.findViewById(R.id.bt_negative);
        bt_negative.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mNegativeButtonText)) {
            bt_negative.setVisibility(View.VISIBLE);
            bt_negative.setText(mNegativeButtonText);
            bt_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeButtonClickListener != null) {
                        mNegativeButtonClickListener.onClick(BlurDialog.this, v);
                    } else
                        dismiss();
                }
            });
        }

        Button bt_positive = view.findViewById(R.id.bt_positive);
        bt_positive.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mPositiveButtonText)) {
            bt_positive.setVisibility(View.VISIBLE);
            bt_positive.setText(mPositiveButtonText);
            bt_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveButtonClickListener != null) {
                        mPositiveButtonClickListener.onClick(BlurDialog.this, v);
                    } else
                        dismiss();
                }
            });
        }
//        ll_container.setClickable(!isCancelable());
        return view;
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onActivityCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mHelper.onDismiss();
        super.onDismiss(dialog);
    }

    public void show(FragmentActivity activity) {
        try {
            super.show(activity.getSupportFragmentManager(), "dialog");
        } catch (Throwable ignore) {
        }
    }

    public BlurDialog setView(View view) {
        childView = view;
        return this;
    }

    public BlurDialog setNegativeButton(CharSequence text, final OnClickListener listener) {
        mNegativeButtonText = text;
        mNegativeButtonClickListener = listener;
        return this;
    }

    public BlurDialog setPositiveButton(CharSequence text, final OnClickListener listener) {
        mPositiveButtonText = text;
        mPositiveButtonClickListener = listener;
        return this;
    }

    public BlurDialog setMessage(CharSequence message) {
        mMessage = message;
        return this;
    }

    public void setCustomView(CustomView customView) {
        this.customView = customView;
    }

    public interface OnClickListener {
        void onClick(DialogFragment dialog, View view);
    }

    public static Builder builder(FragmentActivity activity) {
        return new Builder(activity);
    }

    public static class Builder {
        private View childView = null;
        private CharSequence mNegativeButtonText = null;
        private OnClickListener mNegativeButtonClickListener = null;
        private CharSequence mPositiveButtonText = null;
        private OnClickListener mPositiveButtonClickListener = null;
        private CharSequence mMessage = null;
        private FragmentActivity context;
        private BlurDialog dialog;
        private CustomView customView;

        public Builder(FragmentActivity activity) {
            this.context = activity;
        }

        public Builder setChildView(View view) {
            childView = view;
            return this;
        }

        public Builder setCustomView(CustomView customView) {
            this.customView = customView;
            return this;
        }

        public Builder setNegativeButton(@StringRes int messageId, final OnClickListener listener) {
            setNegativeButton(context.getText(messageId), listener);
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(@StringRes int messageId, final OnClickListener listener) {
            setPositiveButton(context.getText(messageId), listener);
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            mMessage = context.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public BlurDialog build() {
            dialog = BlurDialog.newInstance();
            if (childView != null)
                dialog.setView(childView);
            if (!TextUtils.isEmpty(mMessage))
                dialog.setMessage(mMessage);
            dialog.setPositiveButton(mPositiveButtonText, mPositiveButtonClickListener);
            dialog.setNegativeButton(mNegativeButtonText, mNegativeButtonClickListener);
            dialog.setCustomView(customView);
            return dialog;
        }

        public BlurDialog show() {
            if (dialog == null)
                dialog = build();
            dialog.show(context);
            return dialog;
        }
    }
}
