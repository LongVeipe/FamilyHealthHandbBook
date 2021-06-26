package com.example.familyhealthhandbook.View.GroupProfile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.View.Groups.GroupsFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditGroupNameDialog extends Dialog {

    private GroupProfileActivity activity;

    private TextInputEditText name_edt;
    private MaterialButton cancel_btn;
    private MaterialButton save_btn;

    public EditGroupNameDialog(@NonNull Context context, GroupProfileActivity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_edit_group_name);
        Window window = this.getWindow();
        if(window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        this.setCancelable(true);

        mapping();
        init();
    }

    private void mapping() {
        name_edt = findViewById(R.id.name_edt);
        cancel_btn = findViewById(R.id.cancel_btn);
        save_btn = findViewById(R.id.save_btn);
    }

    void init()
    {
        initSubmit_btn();
        initName_edt();
        initCancel_btn();
    }

    private void initName_edt()
    {
        String name = activity.getName_tv().getText().toString();
        this.name_edt.setText(name);
    }
    private void initSubmit_btn() {

    }

    private void initCancel_btn()
    {
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
