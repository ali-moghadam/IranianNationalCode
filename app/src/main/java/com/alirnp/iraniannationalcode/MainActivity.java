package com.alirnp.iraniannationalcode;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.hanks.htextview.base.HTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean doubleBackToExitPressedOnce = false;
    private ConstraintLayout mConstraintLayoutRoot;
    private View mViewGenerate;
    private View mViewValidate;
    private HTextView mTextViewCode;
    private ImageView mImageViewRepeat;
    private EditText mEditTextCode;
    private ImageView mImageViewGenerate;
    private ImageView mImageViewCopy;
    private ConstraintLayout mImageViewCheckValidate;

    private State state = State.DEFAULT;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);

        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_state_default);

        findViews();

        setListeners();
    }


    private void setListeners() {

        mConstraintLayoutRoot.setOnClickListener(this);

        mViewValidate.setOnClickListener(this);
        mViewGenerate.setOnClickListener(this);

        mImageViewGenerate.setOnClickListener(this);
        mImageViewCopy.setOnClickListener(this);
        mImageViewCheckValidate.setOnClickListener(this);
        mImageViewRepeat.setOnClickListener(this);

    }

    private void findViews() {

        mConstraintLayoutRoot = findViewById(R.id.activity_main_state_constraintLayoutRoot);
        mViewGenerate = findViewById(R.id.activity_main_state_view_generate);
        mViewValidate = findViewById(R.id.activity_main_state_view_validation);

        mTextViewCode = findViewById(R.id.textView_code);
        mEditTextCode = findViewById(R.id.editText_code);
        mImageViewGenerate = findViewById(R.id.imageView_generateCode);
        mImageViewCopy = findViewById(R.id.imageView_copyCode);
        mImageViewCheckValidate = findViewById(R.id.imageView_checkValidateCode);
        mImageViewRepeat = findViewById(R.id.imageView_clear);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        state = State.DEFAULT;
        changeLayout();

        this.doubleBackToExitPressedOnce = true;

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void changeLayout() {
        ConstraintSet constraintSetRoot = new ConstraintSet();

        switch (state) {
            case VALIDATE:
                constraintSetRoot.clone(this, R.layout.activity_main_state_validate);
                break;
            case GENERATE:
                constraintSetRoot.clone(this, R.layout.activity_main_state_generate);

                break;
            case DEFAULT:
                constraintSetRoot.clone(this, R.layout.activity_main_state_default);
                break;
        }


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
            TransitionManager.beginDelayedTransition(mConstraintLayoutRoot, new AutoTransition());


        constraintSetRoot.applyTo(mConstraintLayoutRoot);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.activity_main_state_view_generate) {
            setState(State.GENERATE);

        } else if (v.getId() == R.id.activity_main_state_view_validation) {
            setState(State.VALIDATE);

        } else if (v.getId() == R.id.activity_main_state_constraintLayoutRoot) {
            setState(State.DEFAULT);

        } else if (v.getId() == R.id.imageView_generateCode) {
            mTextViewCode.animateText(spaceText(NationalCode.generateCode()));

        } else if (v.getId() == R.id.imageView_copyCode) {
            copyToClipBoard();

        } else if (v.getId() == R.id.imageView_checkValidateCode) {
            validateInputCode();

        } else if (v.getId() == R.id.imageView_clear) {
            mEditTextCode.setText("");

        } else {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        changeLayout();
    }

    private void validateInputCode() {
        if (mEditTextCode.getText() == null) return;

        hideKeyboard(this);

        String text = mEditTextCode.getText().toString();
        if (text.length() != 10) {
            String error = getResources().getString(R.string.national_code_length_is_wrong);
            showResultDialog(false, error);
            return;
        }
        boolean validate = NationalCode.validationNationalCode(text);

        checkStatusCode(validate);
    }

    private void setState(State state) {
        this.state = state;
    }

    private void copyToClipBoard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("national code", mTextViewCode.getText().toString().replaceAll("\\s+", ""));

        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "کد ملی در حافظه موقت ذخیره شد", Toast.LENGTH_SHORT).show();
        }
    }

    private String spaceText(Long generatedCode) {
        String code = String.valueOf(generatedCode);
        StringBuilder spacedCode = new StringBuilder();

        for (int i = 0; i < code.length(); i++) {
            spacedCode.append(code.charAt(i));

            if (i != (code.length() - 1))
                spacedCode.append(" ");
        }
        return spacedCode.toString();
    }

    private void checkStatusCode(boolean validate) {

        int text = validate ? R.string.national_code_true : R.string.national_code_false;
        showResultDialog(validate, getResources().getText(text).toString());
        // Toast.makeText(this, getResources().getText(text), Toast.LENGTH_SHORT).show();
    }

    private void showResultDialog(boolean isCorrect, String text) {
        ResultFragment resultFragment = ResultFragment.newInstance(isCorrect, text);
        resultFragment.show(getSupportFragmentManager(), null);
    }


    private enum State {DEFAULT, GENERATE, VALIDATE}
}
