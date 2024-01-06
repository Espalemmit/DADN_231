package hm.iot.iotnote10plus;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.angads25.toggle.widget.LabeledSwitch;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.time.LocalTime;
import java.time.ZoneId;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemperature, txtHumidity, txtIntensity;
    LabeledSwitch btnLED, btnPUMP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTemperature = findViewById(R.id.txtTemperature);
        txtIntensity = findViewById(R.id.txtIntensity);
        txtHumidity = findViewById(R.id.txtHumidity);
        btnLED = findViewById(R.id.btnLED);
        btnPUMP = findViewById(R.id.btnPump);

        txtTemperature.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this,PlotActivity.class);
                intent.putExtra("cbTemperature", true);
                startActivity(intent);

            });
        txtIntensity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,PlotActivity.class);
            intent.putExtra("cbIntensity", true);
            startActivity(intent);
        });
        txtHumidity.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,PlotActivity.class);
            intent.putExtra("cbHumidity", true);
            startActivity(intent);
        });
        btnLED.setOnToggledListener((toggleableView, isOn) -> {
            if(isOn){
                sendDataMQTT("datxichma/feeds/nutnhan1", "1",0);
            } else{
                sendDataMQTT("datxichma/feeds/nutnhan1", "0",0);
            }
        });
        btnPUMP.setOnToggledListener((toggleableView, isOn) -> {
            if(isOn){
                sendDataMQTT("datxichma/feeds/nutnhan2", "1",0);
            } else{
                sendDataMQTT("datxichma/feeds/nutnhan2", "0",0);
            }
        });
        startMQTT();
    }
    public void sendDataMQTT(String topic, String value, int numberOfRetries){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);
        byte[] b = value.getBytes(StandardCharsets.UTF_8);
        msg.setPayload(b);


        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-iot-note10plus-13f47-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference();
                Date date = new Date();
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Log.d("TEST", topic + "***" + message.toString() +"***"+ inputFormat.format(date));
                LocalTime present = LocalTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                LocalDate presentDay = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
                int gio = present.getHour();
                int phut = present.getMinute();
                int giay = present.getSecond();
                int ngay = presentDay.getDayOfMonth();
                int thang = presentDay.getMonthValue();
                int nam = presentDay.getYear();

                String node = ngay + "-" + thang + "-" + nam + " " + gio + ":" + phut + ":" + giay;
                if(topic.contains("cambien1")) {
                    int tempValue = Integer.parseInt(message.toString());
                    String text = message + "°C";
                    txtTemperature.setText(text);
                    myRef.child(node).child("Nhiệt độ").setValue(tempValue);
                }
                else if (topic.contains("cambien3")) {
                    int humidValue = Integer.parseInt(message.toString());
                    String text = message + "%";
                    txtHumidity.setText(text);
                    myRef.child(node).child("Độ ẩm").setValue(humidValue);
                    if(humidValue >= 50) {
                        btnPUMP.setOn(false);
                    }
                    if(humidValue < 20) {
                        btnPUMP.setOn(true);
                    }
                }
                else if (topic.contains("cambien2")) {
                    int intensityValue = Integer.parseInt(message.toString());
                    String text = message + "Lux";
                    txtIntensity.setText(text);
                    myRef.child(node).child("Ánh sáng").setValue(intensityValue);
                    if (intensityValue > 400) {
                        btnLED.setOn(false); // Turn off the LED button
                    }
                    if (intensityValue < 50) {
                        btnLED.setOn(true); // Turn off the LED button
                    }
                }
                else if (topic.contains("ai")){
                    if (!btnLED.isEnabled()) {
                        if (message.toString().equals("1")) {
                            btnLED.setEnabled(true);
                        } else {
                            btnLED.setOn(!btnLED.isOn());
                        }
                        btnLED.setEnabled(true);
                    }
                }
                else if (topic.equals("datxichma/feeds/nutnhan2")){
                    btnPUMP.setOn(message.toString().equals("1"));
                }
                else if (topic.contains("datxichma/feeds/nutnhan1")){
                    btnLED.setOn(message.toString().equals("1"));
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}