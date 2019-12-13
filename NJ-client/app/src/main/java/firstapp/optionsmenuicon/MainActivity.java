package firstapp.optionsmenuicon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraAccessException;
import org.eclipse.paho.client.mqttv3.MqttException;
import android.view.ViewGroup.LayoutParams;



public class MainActivity extends AppCompatActivity {

    public static final int Time_Limit = 1500;
    private static long backPressed;
    String topic = "NJ app";
    String topic1 = "NJ App";
    int qos = 2;
    String broker = "tcp://192.168.1.4:1883";
    String clientid = "JavaSampleSubscriber";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient sampleClient;
    Vibrator vibrator;
    Ringtone myRingtone;
    TextView subText;
    Button lightBtn;
    Boolean isLightOn = false;
    CameraManager mCameraManager;
    Context mContext;
    String mCameraId;
    RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    String s1 = "Flash_on";
    String s2 = "Ringtone_on";
    String content = "null";


    /////////////////////////////////////////////////
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        final Handler mHandler = new Handler();
        final boolean isRunning = true;





        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRunning) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                displayData();
                            }
                        });
                    } catch (Exception e) {

                    }
                }
            }
        }).start();

        try {

            if (mCameraManager != null) {
                mCameraId = mCameraManager.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //Declare and link your button
        lightBtn = findViewById(R.id.lightBtn);

        //Set button click listener

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLightOn) {

                    isLightOn = false;
                    turnOffLight();
                    lightBtn.setText("TURN ON");
                } else {

                    isLightOn = true;
                    turnOnLight();
                    lightBtn.setText("TURN OFF");
                }
            }
        });
        subText = findViewById(R.id.subText);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //////////////////////////////////////////////
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        myRingtone = RingtoneManager.getRingtone(getApplicationContext(), uri);



        try {

            sampleClient = new MqttClient(broker, clientid, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);

        } catch (MqttException e) {

            e.printStackTrace();


        }


        sampleClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // vibrator.vibrate(500);
                // myRingtone.play();

                if (message.toString().equals(s1)){
                    subText.setText(new String(message.getPayload()));
                    turnOnLight();
                    isLightOn=true;

                }
                if(message.toString().equals(s2)){
                    subText.setText(new String(message.getPayload()));
                   turnOffLight();
                    myRingtone.play();}

                if(message.toString().equals("exit")){

                    subText.setText("");
                    turnOffLight();}

                if (message.toString().equals("EyesOpened")){
                    subText.setText(new String(message.getPayload()));
                    turnOnLight();
                    isLightOn=true;

                }

                if (message.toString().equals("EyesClosed")){
                    subText.setText(new String(message.getPayload()));
                    turnOffLight();

                }

            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }


        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("WrongViewCast")
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.action_settings:

                mContext = getApplicationContext();
                mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_flashlight_sample);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View customView = inflater.inflate(R.layout.custom_layout,null);
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.WRAP_CONTENT,
                         LayoutParams.WRAP_CONTENT

                );
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.imageButton2);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                         mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);
                mPopupWindow.setFocusable(true);
                mPopupWindow.update();
                ImageButton accept_button=(ImageButton)customView.findViewById(R.id.imageButton);
                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText text=(EditText)customView.findViewById(R.id.editText4);
                        broker=text.getText().toString();
                        mPopupWindow.dismiss();
                        try {

                            sampleClient = new MqttClient(broker, clientid, persistence);

                            MqttConnectOptions connOpts = new MqttConnectOptions();

                            connOpts.setCleanSession(true);

                        } catch (MqttException e) {

                            e.printStackTrace();


                        }
                        sampleClient.setCallback(new MqttCallback() {
                            @Override
                            public void connectionLost(Throwable throwable) {
                                System.out.println("connectionLost-----------");
                            }

                            @Override
                            public void messageArrived(String topic, MqttMessage message) throws Exception {
                                // vibrator.vibrate(500);
                                // myRingtone.play();

                                if (message.toString().equals(s1)){
                                    subText.setText(new String(message.getPayload()));
                                    turnOnLight();
                                    isLightOn=true;
                                }
                                if(message.toString().equals(s2)){
                                    subText.setText(new String(message.getPayload()));
                                    turnOffLight();
                                    myRingtone.play();}
                                if(message.toString().equals("exit")){

                                    subText.setText("");
                                    turnOffLight();}
                                if (message.toString().equals("EyesOpened")){
                                    subText.setText(new String(message.getPayload()));
                                    turnOnLight();
                                    isLightOn=true;

                                }

                                if (message.toString().equals("EyesClosed")){
                                    subText.setText(new String(message.getPayload()));
                                    turnOffLight();

                                }



                            }
                            @Override
                            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                            }


                        });


                    }

                });


                break;


            case R.id.action_set_frequency:
                mContext = getApplicationContext();
                mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_flashlight_sample);
                LayoutInflater inflater2 = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                final View customView2 = inflater2.inflate(R.layout.custom_layout2,null);
                mPopupWindow = new PopupWindow(
                        customView2,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT

                );
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }
                mPopupWindow.setFocusable(true);
                mPopupWindow.update();
                ImageButton closeButton2 = (ImageButton) customView2.findViewById(R.id.imageButton3);

                closeButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window

                        EditText text=(EditText)customView2.findViewById(R.id.editText);
                        content=text.getText().toString();
                        Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
                        MqttMessage message = new MqttMessage(content.getBytes());
                        message.setQos(qos);
                        try {
                            sampleClient.publish(topic1, message.getPayload(), 2, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }

                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);


                break;
            case R.id.action_exit:


                System.exit(0);
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onBackPressed() {
        if (Time_Limit + backPressed > System.currentTimeMillis()) {
            super.onBackPressed();

        } else {
            Toast.makeText(getApplicationContext(), "Tap Again to Exit", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();


    }





    public void conn(View v) {


        try {

            vibrator.vibrate(500);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker:" + broker);
            IMqttToken token = sampleClient.connectWithResult(connOpts);
            System.out.println("Connected");
            System.out.println("Subscribing to topic \"" + topic + "\"qos" + qos);
            sampleClient.subscribe(topic, qos);
            System.out.println("Subscribed");


            Toast.makeText(MainActivity.this,"connected",Toast.LENGTH_LONG).show();

        } catch (MqttException me) {
            Toast.makeText(MainActivity.this,"failed to connect",Toast.LENGTH_LONG).show();
            System.out.println("reason" + me.getReasonCode());
            System.out.println("msg" + me.getMessage());
            System.out.println("loc" + me.getLocalizedMessage());
            System.out.println("cause" + me.getCause());
            System.out.println("excep" + me);
            me.printStackTrace();
        }

    }


    public void disconn(View v) throws MqttException {


        try {

            vibrator.vibrate(500);
            System.out.println("Disconnecting from broker:" + broker);
            sampleClient.disconnect();
            System.out.println("Disconnected");


            Toast.makeText(MainActivity.this, "disconnected", Toast.LENGTH_LONG).show();


        } catch (MqttException me) {
            Toast.makeText(MainActivity.this,"failed to disconnect",Toast.LENGTH_LONG).show();
            System.out.println("reason" + me.getReasonCode());
            System.out.println("msg" + me.getMessage());
            System.out.println("loc" + me.getLocalizedMessage());
            System.out.println("cause" + me.getCause());

            System.out.println("excep" + me);
            me.printStackTrace();
        }

    }

        //Turn on flashlight method
    public void turnOnLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Turn off flashlight method
    public void turnOffLight() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayData() {
        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Toast.makeText(this, "Network Available", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(this, "Network Not Available.Please turn on Wifi", Toast.LENGTH_SHORT).show();

        }
    }

}
















