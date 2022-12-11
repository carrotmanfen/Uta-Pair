package com.example.utapair;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NonNls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ShareScore extends AppCompatActivity {
    // attribute
    private static ShareScore instance;
    private Context context;
    private Activity activity;

    public static ShareScore getInstance(){
        if (instance == null)
            instance = new ShareScore();
        return instance;
    }

    // method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grandResults){

        if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
            saveImage();
        }
        else{
            Toast.makeText(context,"Permission denied", Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode,permission,grandResults);
    }

    public File saveImage(){
        if(!checkUserPermission()){
            return null; /* if permission is not allowed return null */
        }

        try{
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/ShareMyScore"; // ***
            System.out.println(path);
            File fileDir = new File(path);
            if(!fileDir.exists()) {
                fileDir.mkdir();
            }

            String mpath = path+"/Screenshot_"+ new Date().getTime() + ".png"; // define name of picture

            Bitmap bitmap = takeScreenShot();
            File file = new File(mpath); // define path for generate file output
            FileOutputStream fileOutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
            fileOutput.flush();
            fileOutput.close();

            Toast.makeText(context,"Image has been saved successfully",Toast.LENGTH_LONG).show();

            /* success return picture */
            return file;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; /* if error! return null */

    }

    public  void sharePicture(File file){
        Uri uri;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(context,activity.getPackageName(),file);
        }
        else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_SUBJECT,"UTA is the best app");
        intent.putExtra(Intent.EXTRA_TEXT, "Come on, Let’s play with me.");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        String Body = "UTA is the best app";
//        String sub = "Application Link here !!"; /* link of webpage app */
//        intent.putExtra(Intent.EXTRA_SUBJECT,Body);
//        intent.putExtra(Intent.EXTRA_TEXT,sub);
//        startActivity(Intent.createChooser(intent,"Share using"));
        Intent chooser = Intent.createChooser(intent,"Share File");

        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }


        try{
           activity.startActivity(chooser);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, e.getMessage() ,Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap takeScreenShot(){
        View v = activity.findViewById(R.id.endGameRootView);
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public boolean checkUserPermission(){
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){ /* if user doesn't get a permission request from line 23*/
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }
    public void setContext(Context context){
        this.context = context;
        this.activity = (Activity) context;
    }

}
