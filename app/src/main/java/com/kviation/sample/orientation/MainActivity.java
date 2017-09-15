package com.kviation.sample.orientation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  private Sensor mGyroscope;

    private ArrayList<String> accelerometer = new ArrayList<>();
    private ArrayList<String> gyroscope = new ArrayList<>();

  private int mLastAccuracy;
  private Button start, stop, save;
  final int REQUEST_CODE_ASK_PERMISSIONS = 123;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
          requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
      }

    start = (Button) findViewById(R.id.start);
    stop = (Button) findViewById(R.id.stop);
    save = (Button) findViewById(R.id.save);

    mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


    start.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(mAccelerometer == null) {
          LogUtil.w("Accelerometer sensor not available; will not provide orientation data.");
        }
        if(mGyroscope == null) {
          LogUtil.w("Gyroscope sensor not available; will not provide orientation data.");
        }

        mSensorManager.registerListener(MainActivity.this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(MainActivity.this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
      }
    });


    stop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        mSensorManager.unregisterListener(MainActivity.this);
      }
    });


    save.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          File memoryData = Environment.getExternalStorageDirectory();
          File dir = new File(memoryData.getAbsolutePath());
          dir.mkdir();
          File file = new File(dir, "Accelerometer.csv");
          FileOutputStream os = null;
          try {
              os = new FileOutputStream(file);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }
          try {
              for (String s : accelerometer)
                  os.write(s.getBytes());

          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
              os.close();
          } catch (IOException e) {
              e.printStackTrace();
          }

          File file1 = new File(dir, "Gyroscope.csv");
          try {
              os = new FileOutputStream(file1);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }
          try {
              for (String s : gyroscope)
                  os.write(s.getBytes());

          } catch (IOException e) {
              e.printStackTrace();
          }
          try {
              os.close();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
    });
  }

  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }


  @Override
  public void onSensorChanged(SensorEvent event) {

    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

      TextView tvX= (TextView)findViewById(R.id.x_axis);
      TextView tvY= (TextView)findViewById(R.id.y_axis);
      TextView tvZ= (TextView)findViewById(R.id.z_axis);

      float x = event.values[0];
      float y = event.values[1];
      float z = event.values[2];

      tvX.setText(String.valueOf(x));
      tvY.setText(String.valueOf(y));
      tvZ.setText(String.valueOf(z));

        accelerometer.add(" X: " + x + ", Y: " + y + ", Z: " + z);

      Log.v("Acc Data X : ", Float.toString(x));
      Log.v("Acc Data Y : ", Float.toString(y));
      Log.v("Acc Data Z : ", Float.toString(z));
    }
    else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

      TextView tvX= (TextView)findViewById(R.id.x_axis1);
      TextView tvY= (TextView)findViewById(R.id.y_axis1);
      TextView tvZ= (TextView)findViewById(R.id.z_axis1);

      float x = event.values[0];
      float y = event.values[1];
      float z = event.values[2];

      tvX.setText(String.valueOf(x));
      tvY.setText(String.valueOf(y));
      tvZ.setText(String.valueOf(z));

        gyroscope.add(" X: " + x + ", Y: " + y + ", Z: " + z);
      Log.v("Gyr Data X : ", Float.toString(x));
      Log.v("Gyr Data Y : ", Float.toString(y));
      Log.v("Gyr Data Z : ", Float.toString(z));
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

    if (mLastAccuracy != accuracy) {
      mLastAccuracy = accuracy;
    }
  }
}
