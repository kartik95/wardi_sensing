package com.kviation.sample.orientation;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

//  private Orientation mOrientation;
//  private AttitudeIndicator mAttitudeIndicator;

//    private WindowManager mWindowManager;
//    private static final int SENSOR_DELAY_MICROS = 50 * 1000;
//private Orientation.Listener mListener;

  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  private Sensor mGyroscope;

  private int mLastAccuracy;
  private Button start, stop, save;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

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

      }
    });
  }


  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }

//  @Override
//  protected void onStart() {
//    super.onStart();
//    mOrientation.startListening(this);
//  }
//
//  @Override
//  protected void onStop() {
//    super.onStop();
//    mOrientation.stopListening();
//  }

//  @Override
//  public void onOrientationChanged(float pitch, float roll) {
//    mAttitudeIndicator.setAttitude(pitch, roll);
//  }

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
