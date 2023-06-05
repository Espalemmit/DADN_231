# Setup your project DADN_Adafruit

### MQTTHelper.java

```
public class MQTTHelper {
    public MqttAndroidClient mqttAndroidClient;
    public final String[] arrayTopics = {
      "nathan0793/feeds/adc-temperature", 
      "nathan0793/feeds/adc-intensity",
      "nathan0793/feeds/adc-humidity",
      "nathan0793/feeds/btnled",
      "nathan0793/feeds/btnbump",
      "nathan0793/feeds/ackled"
    };
    
    final String clientId = "12342678"; // Set a random number -- Must change if more than 1 person use in same Local Network
    final String username = "nathan0793"; // Setup follow your adafruit server --> Check the picture as below
    final String password = "aio_NiAy21S9CrBFkhvy5DkKgyjWIa8Q"; // Setup follow your adafruit server --> Check the picture as below


//...}

```
![Adafruit Configuration](https://github.com/Hastash/DADN_Adafruit/assets/50420777/6c04fb0a-6f87-4a53-829f-85303aca2acb)
# Here is your result when setup completed
| UI | Features | 
|:-------------------------:|:-------------------------:|
|<img width="45%" alt="Dashboard" src="https://github.com/Hastash/DADN_Adafruit/assets/50420777/b035db53-a29c-4dfe-b5a5-8c3de5cc818c"> <br/> Dashboard| <img width="45%" alt="Dashboard Error Controller Button" src="https://github.com/Hastash/DADN_Adafruit/assets/50420777/e91c4b85-aa04-4013-8461-819f75643e0d"> <br/>Dashboard Error Controller Button |
|<img width="45%" alt="Chart UI" src="https://github.com/Hastash/DADN_Adafruit/assets/50420777/93ef327e-10db-4128-9925-d9696723f7b7"> <br/> Chart UI|  <img width="45%" alt="Realtime Chart" src="https://github.com/Hastash/DADN_Adafruit/assets/50420777/1c0eb44c-b62b-4d82-b7cb-c80d9451210b"> <br/> Realtime Chart UI|
