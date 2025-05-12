package com.readify.readify.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.readify.readify.R;

public class DeleteConfirmDialog extends Dialog {
    private Button yesButton, noButton;
    private OnDeleteConfirmListener yesListener;
    private OnDeleteConfirmListener noListener;

    public interface OnDeleteConfirmListener {
        void onConfirm();
    }

    public DeleteConfirmDialog(Context context, OnDeleteConfirmListener yesListener, OnDeleteConfirmListener noListener) {
        super(context);
        this.yesListener = yesListener;
        this.noListener = noListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_delete);

        yesButton = findViewById(R.id.yes_button);
        noButton = findViewById(R.id.no_button);

        yesButton.setOnClickListener(v -> {
            if (yesListener != null) {
                yesListener.onConfirm();
            }
            dismiss();
        });

        noButton.setOnClickListener(v -> {
            if (noListener != null) {
                noListener.onConfirm();
            }
            dismiss();
        });
    }
}