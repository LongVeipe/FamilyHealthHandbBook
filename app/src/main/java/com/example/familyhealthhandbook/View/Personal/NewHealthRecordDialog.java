package com.example.familyhealthhandbook.View.Personal;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyhealthhandbook.Adapter.RecyclerViewHorizontalImageAdapter;
import com.example.familyhealthhandbook.Model.Sickness;
import com.example.familyhealthhandbook.R;
import com.example.familyhealthhandbook.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class NewHealthRecordDialog extends Dialog {

    private FragmentActivity fragmentActivity;

    public NewHealthRecordDialog(@NonNull Context context, FragmentActivity fragmentActivity) {
        super(context);
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_create_health_record, null);
        //this.setContentView(R.layout.dialog_create_health_record);
        setContentView(view);
        Window window = this.getWindow();
        if(window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        this.setCancelable(true);

        TextInputEditText location_edt = this.findViewById(R.id.location_edt);
        AppCompatAutoCompleteTextView sickness_tv = this.findViewById(R.id.sickness_tv);
        initDialogSicknessTv(sickness_tv);

        RecyclerView image_rv = this.findViewById(R.id.image_rv);
        RecyclerViewHorizontalImageAdapter adapter = new RecyclerViewHorizontalImageAdapter(getContext());
        image_rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        image_rv.setHasFixedSize(true);
        image_rv.setAdapter(adapter);

        Button addImg_btn = this.findViewById(R.id.addImg_btn);
        addImg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(adapter);
            }
        });
    }

    void initDialogSicknessTv(AppCompatAutoCompleteTextView sickness_tv)
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            ArrayList<Sickness> sicknesses = Utils.getApi().getAllSickness().execute().body();
            ArrayList<String> sicknessNames = new ArrayList<String>();
            for(Sickness sickness: sicknesses)
            {
                sicknessNames.add(sickness.getName());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sicknessNames);
            sickness_tv.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission(RecyclerViewHorizontalImageAdapter adapter) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                selectImageFromGallery(adapter);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    private void selectImageFromGallery(RecyclerViewHorizontalImageAdapter adapter)
    {
        TedBottomPicker.with(fragmentActivity)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if(uriList != null && !uriList.isEmpty()){
                            adapter.setData(uriList);
                        }
                    }
                });
    }
}
