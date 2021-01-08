package com.alirnp.iraniannationalcode;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.DialogFragment;


public class ResultFragment extends DialogFragment {

    private static final String ARG_IS_CORRECT = "isCorrect";
    private static final String ARG_TEXT = "text";

    private TextView mTextViewStatus;
    private ImageView mImageViewStatus;
    private ConstraintLayout mConstraintLayout;
    private Context mContext;
    private boolean isCorrect;
    private String mText;

    public ResultFragment() {
        // Required empty public constructor
    }

    public static ResultFragment newInstance(boolean isCorrect, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_CORRECT, isCorrect);
        args.putString(ARG_TEXT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("deprecation")
    public static int getScreenWidth(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.widthPixels;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isCorrect = getArguments().getBoolean(ARG_IS_CORRECT);
            mText = getArguments().getString(ARG_TEXT);
        }
    }

    private void setBackgroundForDialog(Dialog dialog) {
        if (dialog != null)
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(dialog.getContext(), R.drawable.shape_white_border));
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
    }

    private void setWidth() {
        int width = getScreenWidth(getActivity());

        Window window = getDialog().getWindow();
        window.setLayout((int) (width * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    @Override
    public void onResume() {
        super.onResume();
        setWidth();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setBackgroundForDialog(getDialog());

        initViews(view);
        initValues();
        initListeners();
    }

    private void initListeners() {
        mConstraintLayout.setOnClickListener(view -> dismiss());
    }

    private void initValues() {
        mTextViewStatus.setText(mText);

        if (isCorrect) {
            ImageViewCompat.setImageTintList(mImageViewStatus, getColorState(R.color.green));
            mTextViewStatus.setTextColor(mContext.getResources().getColor(R.color.green));
            mImageViewStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_transparent_green));
            mImageViewStatus.setImageResource(R.drawable.ic_tick);
        } else {
            ImageViewCompat.setImageTintList(mImageViewStatus, getColorState(R.color.red));
            mTextViewStatus.setTextColor(mContext.getResources().getColor(R.color.red));
            mImageViewStatus.setBackground(ContextCompat.getDrawable(mContext, R.drawable.shape_circle_transparent_red));
            mImageViewStatus.setImageResource(R.drawable.ic_cancel);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private void initViews(View view) {
        mConstraintLayout = view.findViewById(R.id.constraintLayout_root);
        mTextViewStatus = view.findViewById(R.id.textView_title);
        mImageViewStatus = view.findViewById(R.id.imageView_status);
    }

    private ColorStateList getColorState(int color) {
        int cl = mContext.getResources().getColor(color);
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{cl, cl, cl, cl};

        return new ColorStateList(states, colors);
    }
}