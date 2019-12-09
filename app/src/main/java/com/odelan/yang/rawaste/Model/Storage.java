package com.odelan.yang.rawaste.Model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.odelan.yang.rawaste.Util.Interface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    public static void uploadFile(String path, final Interface.OnResult<String> result) {
        Uri file = Uri.fromFile(new File(path));
        String fileName = "image_" + (System.currentTimeMillis() / 1000) + ".jpg";
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("images/" + fileName);
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                result.onSuccess(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                result.onFailure(exception.getMessage());
            }
        });
    }

    static void uploadFiles(final List<String> paths, final List<String> urls, final int index, final Interface.OnResult<List<String>> result) {
        if (index == paths.size()) {
            result.onSuccess(urls);
            return;
        }
        if (paths.get(index).startsWith("http")) {
            urls.add(paths.get(index));
            uploadFiles(paths, urls, index + 1, result);
            return;
        }
        uploadFile(paths.get(index), new Interface.OnResult<String>() {
            @Override
            public void onSuccess(String url) {
                urls.add(url);
                uploadFiles(paths, urls, index + 1, result);
            }
            @Override
            public void onFailure(String error) {
                result.onFailure(error);
            }
        });
    }

    public static void uploadFiles(List<String> paths, Interface.OnResult<List<String>> result) {
        uploadFiles(paths, new ArrayList<String>(), 0, result);
    }
}
