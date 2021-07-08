#include <AntaresESP8266HTTP.h>
#include <JsonListener.h>
#include <JsonStreamingParser.h>
#include <AccuWeatherLibrary.h>
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <time.h>

#define WIFISSID "RumahSini" // SSID WiFi
#define PASSWORD "rumahdepan" // password WiFi

#define ACCESS_KEY_ANTARES "a9b3831c56f6ee30:48b1b952e5490e8f" // Access key akun Antares
#define APPLICATION_NAME "SWAPAS" // Application name Antares
#define DEVICE_NAME_REPORT "Report" // Device name Antares (tabel Report)
#define DEVICE_NAME_S_ULTRASONIK "S_Ultrasonik" // Device name Antares (tabel S_Ultrasonik)
#define DEVICE_NAME_SETTING "Settings" // Device name Antares (tabel Settings)

#define JUMLAH_KEY_WEATHER 5
#define JUMLAH_DATA 3 // Jumlah data yang akan diberikan oleh ARDUINO UNO

static const uint32_t DELAY_1_SECOND  = 1000UL;
static const uint32_t DELAY_1_MINUTE  = DELAY_1_SECOND * 60UL;
static const uint32_t DELAY_1_HOUR    = DELAY_1_MINUTE * 60UL;
static const char DELIMITER       = ',';

AntaresESP8266HTTP antares(ACCESS_KEY_ANTARES);    // Buat objek antares
String KEY_WEATHER[JUMLAH_KEY_WEATHER] = {"5XwtpzTw39V6hGRmooW2Hj4LmEuPPBz2", "bk5Vaq2qNZqTWP1aOTRTYQ3XljFN0Qas", "BQSO97QPG9A15OUjr3F8F67hFLjjTS7S",
                                          "hGi2W05yLFb1UklusRYMcaQGkL4THXit", "RAAALBVs7JnCMCx8R6mJL5yJylJQlAb0"
                                         };
int pointerKeyWeather = 0;
boolean isDataTaken = false;

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "id.pool.ntp.org");

void setup() {
  //Serial S Begin at 9600 Baud
  Serial.begin(9600);
  antares.setDebug(false);   // Nyalakan debug. Set menjadi "false" jika tidak ingin pesan-pesan tampil di serial monitor
  antares.wifiConnection(WIFISSID, PASSWORD); // Menyambungkan ke WiFi

  WiFi.begin(WIFISSID, PASSWORD); // Menyambungkan ke WiFi
  while (WiFi.status() != WL_CONNECTED) delay(100);

  // Initialize a NTPClient to get time
  timeClient.begin();
}

void loop() {
  String data = "";
  String splitDataSetting[JUMLAH_DATA];

  if (Serial.available() > 0) {
    data = Serial.readStringUntil('\n');
    Serial.flush();
    data.trim();

    if (data == "[GET DATA ANTARES]" && !isDataTaken) {
      // Menggambil data setting pada Antares
      float penyiramanOtomatis;
      float tinggiTandon;
      float luasPermukaan;
      float jarakSensor;
      int rainProbability;

      do {
        antares.getNonSecure(APPLICATION_NAME, DEVICE_NAME_SETTING);
        penyiramanOtomatis = antares.getFloat("penyiramanOtomatis");
        tinggiTandon = antares.getFloat("tinggiTandon");
        luasPermukaan = antares.getFloat("luasPermukaan");
        jarakSensor = antares.getFloat("jarakSensor");
      } while (!antares.getSuccess());

      if (penyiramanOtomatis == 1) {
        isDataTaken = true;
        rainProbability = getRainProbability();
      } else {
        rainProbability = 0;
      }

      // Mengirim data ke serial
      Serial.println((String) "[NODEMCU]" + DELIMITER + penyiramanOtomatis + DELIMITER + tinggiTandon + DELIMITER +
                     luasPermukaan + DELIMITER + jarakSensor + DELIMITER +
                     rainProbability);
      Serial.flush();
    }

    if (data == "[ARDUINO UNO]" && isDataTaken) {  // Data yang diberikan ada identifier "ARDUINO UNO"-nya
      delay(5UL * DELAY_1_SECOND); // Delay 5 detik
      data = Serial.readStringUntil('\n');
      Serial.flush();

      for (int i = 0; i < JUMLAH_DATA; i++) {
        splitDataSetting[i] = getValue(data, i);
      }

      float nilaiKelembabanTanah = atof(splitDataSetting[0].c_str());
      float volumeAirTerpakai = atof(splitDataSetting[1].c_str());
      float totalAirTandon = atof(splitDataSetting[2].c_str());

      // timestamp
      while (!timeClient.update()) {
        timeClient.forceUpdate();
      }
      unsigned long timestamp = timeClient.getEpochTime();

      // kirim data ke tabel Report
      antares.add("timestamp", String(timestamp));
      antares.add("nilaiKelembabanTanah", nilaiKelembabanTanah);
      antares.add("volumeAirTerpakai", volumeAirTerpakai);
      antares.sendNonSecure(APPLICATION_NAME, DEVICE_NAME_REPORT);

      // kirim data ke tabel S_Ultrasonik
      antares.add("totalAirTandon", totalAirTandon);
      antares.sendNonSecure(APPLICATION_NAME, DEVICE_NAME_S_ULTRASONIK);

      isDataTaken = false;
    }
  }
}

int getRainProbability() {
  AccuweatherHourlyData dataH[3];
  Accuweather aw(KEY_WEATHER[pointerKeyWeather].c_str(), 3451070, "en-us", true);
  int ret = aw.getHourly(dataH, 3);

  while (ret != 0) {
    if (pointerKeyWeather < JUMLAH_KEY_WEATHER - 1) {
      pointerKeyWeather = pointerKeyWeather + 1;
      Accuweather aw(KEY_WEATHER[pointerKeyWeather].c_str(), 3451070, "en-us", true);
      ret = aw.getHourly(dataH, 3);
      while (aw.continueDownload() > 0) {}
    }
    else {
      pointerKeyWeather = 0;
    }
  }

  while (aw.continueDownload() > 0) {}

  int rainProbability = dataH[2].RainProbability;
  return rainProbability;
}

String getValue(String data, int index)
{
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == DELIMITER || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }

  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
