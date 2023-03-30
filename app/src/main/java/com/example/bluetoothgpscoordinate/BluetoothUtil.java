package com.example.bluetoothgpscoordinate;

/*
Copyright (c) 2019 Kai Morich
Based on the work of Kai Morich : https://github.com/kai-morich/SimpleBluetoothTerminal
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

public class BluetoothUtil {

    interface PermissionGrantedCallback {
        void call();
    }

    /**
     * sort by name, then address. sort named devices first
     */
    @SuppressLint("MissingPermission")
    static int compareTo(BluetoothDevice a, BluetoothDevice b) {
        boolean aValid = a.getName()!=null && !a.getName().isEmpty();
        boolean bValid = b.getName()!=null && !b.getName().isEmpty();

        if(aValid && bValid) {
            int ret = a.getName().compareTo(b.getName());
            if (ret != 0) return ret;
            return a.getAddress().compareTo(b.getAddress());
        }
        if(aValid) return -1;
        if(bValid) return +1;
        return a.getAddress().compareTo(b.getAddress());
    }

    /**
     * Android 12 permission handling
     */
    private static void showRationaleDialog(Fragment fragment, DialogInterface.OnClickListener listener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Bluetooth permission");
        builder.setMessage("Bluetooth permission is required by this App. Please grant in next dialog.");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Continue", listener);
        builder.show();
    }

    private static void showSettingsDialog(Fragment fragment) {
        String s = fragment.getResources().getString(fragment.getResources().getIdentifier("@android:string/permgrouplab_nearby_devices", null, null));
        final AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Bluetooth permission");
        builder.setMessage(String.format("Bluetooth permission was permanently denied. You have to enable permission \"%s\" in App settings.", s));
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Settings", (dialog, which) ->
                fragment.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + BuildConfig.APPLICATION_ID))));
        builder.show();
    }

    static boolean hasPermissions(Fragment fragment, ActivityResultLauncher<String> requestPermissionLauncher) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
            return true;
        boolean missingPermissions = fragment.getActivity().checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED;
        boolean showRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT);

        if(missingPermissions) {
            if (showRationale) {
                showRationaleDialog(fragment, (dialog, which) ->
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT));
            } else {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT);
            }
            return false;
        } else {
            return true;
        }
    }

    static void onPermissionsResult(Fragment fragment, boolean granted, PermissionGrantedCallback cb) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
            return;
        boolean showRationale = fragment.shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_CONNECT);
        if (granted) {
            cb.call();
        } else if (showRationale) {
            showRationaleDialog(fragment, (dialog, which) -> cb.call());
        } else {
            showSettingsDialog(fragment);
        }
    }

}
