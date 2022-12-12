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

    /* Singleton pattern  */
    public static ShareScore getInstance(){
        if (instance == null)
            instance = new ShareScore();
        return instance;
    }

    // method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grandResults){

        /* if permission save image */
        if(grandResults.length > 0 && grandResults[0] == PackageManager.PERMISSION_GRANTED){
            saveImage();
        }
        else{ /* if not show permission deny */
            Toast.makeText(context,"Permission denied", Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode,permission,grandResults);
    }

    /* this method is about saveImage */
    public File saveImage(){
        if(!checkUserPermission()){
            return null; /* if permission is not allowed return null */
        }
        try{
            String path = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/ShareMyScore"; /* create path */
            System.out.println(path);
            File fileDir = new File(path);  /* new file with path that create */
            if(!fileDir.exists()) {
                fileDir.mkdir();
            }

            String myPath = path+"/Screenshot_"+ new Date().getTime() + ".png"; // define name of picture

            Bitmap bitmap = takeScreenShot(); /* take Screen shot of game result */
            File file = new File(myPath); // define path for generate file output
            FileOutputStream fileOutput = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutput); /* compress picture file */
            fileOutput.flush();
            fileOutput.close();

            Toast.makeText(context,"Image has been saved successfully",Toast.LENGTH_LONG).show();   /* toast it when it success */

            /* success return picture */
            return file;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; /* if error! return null */

    }

    /* this method is about share picture to other app */
    public  void sharePicture(File file){
        Uri uri;
        /* set uri */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            uri = FileProvider.getUriForFile(context,activity.getPackageName(),file);
        }
        else {
            uri = Uri.fromFile(file);
        }

        /* create Intent and put data to putExtra */
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_SUBJECT,"UTA is the best app");
        intent.putExtra(Intent.EXTRA_TEXT, "Come on, Let’s play with me.");
        intent.putExtra(Intent.EXTRA_STREAM,uri);
        Intent chooser = Intent.createChooser(intent,"Share File");
        /* create list */
        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);
        /* for list grantUriPermission */
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        try{ /* try activity */
           activity.startActivity(chooser);
        }catch (ActivityNotFoundException e){  /* if cannot show some text */
            Toast.makeText(context, e.getMessage() ,Toast.LENGTH_LONG).show();
        }
    }

    /* this method is about take screenshot game result */
    private Bitmap takeScreenShot(){
        View v = activity.findViewById(R.id.endGameRootView);  /* find view with id */
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(),v.getHeight(),Bitmap.Config.ARGB_8888); /* map it */
        Canvas canvas = new Canvas(bitmap); /* new canvas */
        v.draw(canvas);
        return bitmap; /* return picture */
    }

    /* this method is about check user permission that this mobile allow this application to screenshot */
    public boolean checkUserPermission(){
        int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        /* if not permission return false */
        if(permission != PackageManager.PERMISSION_GRANTED){ /* if user doesn't get a permission request from line 23*/
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return false;
        }
        return true;
    }

    /* this method is about set context and activity */
    public void setContext(Context context){
        this.context = context;   /* set context */
        this.activity = (Activity) context;  /* set activity */
    }

}
