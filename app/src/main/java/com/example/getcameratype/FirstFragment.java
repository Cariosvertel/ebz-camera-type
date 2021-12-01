package com.example.getcameratype;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {
    static public String DBG_TAG = "#camera-type";
    TextView firstTextView;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstTextView = getView().findViewById(R.id.textview_first);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isCamera2Device();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isCamera2Device() {
        Activity act =  getActivity();
        CameraManager camMgr = (CameraManager)act.getSystemService(Context.CAMERA_SERVICE);
        boolean camera2Dev = true;
        try {

            String[] cameraIds = camMgr.getCameraIdList();
            Log.i(DBG_TAG, "-------------------- CHECK CAMERA2 AVAILABLE ------------------------");
            Log.i(DBG_TAG, "cameraIds.length = " + cameraIds.length);
            if (cameraIds.length != 0 ) {
                for (String id : cameraIds) {
                    CameraCharacteristics characteristics = camMgr.getCameraCharacteristics(id);
                    int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    int facing      = characteristics.get(CameraCharacteristics.LENS_FACING);
                    Log.i(DBG_TAG, "CAM[" + id + "]");
                    Log.i(DBG_TAG, "  deviceLevel=" + deviceLevel);
                    Log.i(DBG_TAG, "  facing     =" + facing);
                    if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                        camera2Dev =  false;
                        Log.i(DBG_TAG, "  ##This Camera is LEGACY##");
                        firstTextView.setText("IS CAMERA LEGACY");
                        Toast.makeText(act, "IS  CAMERA LEGACY",
                                Toast.LENGTH_LONG).show();

                    }else{
                        firstTextView.setText("IS CAMERA 2");
                        Toast.makeText(act, "IS CAMERA 2 !",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            camera2Dev = false;
        }
        return camera2Dev;
    }
}