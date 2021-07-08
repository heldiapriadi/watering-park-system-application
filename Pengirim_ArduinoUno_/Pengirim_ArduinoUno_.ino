#include <StringSplitter.h>

#define PIN_ULTRASONIK 2 // pin echo ke Sensor Ultrasonik
#define PIN_TRIG 3 // pin trig ke Sensor Ultrasonik
#define PIN_KELEMBABAN_TANAH A0 // pin ke Sensor Kelembaban Tanah
#define PIN_RELAY 13 // pin ke Relay

#define MIN_NILAI_KERING 601
#define MAX_NILAI_KERING 1023

#define RAIN_PROBABILTY_LIMIT 55 // Kemungkinan dapat disebut hujan adalah jika lebih dari 55%
#define LIMIT_PERSEN 25

#define JUMLAH_DATA 6 // Jumlah data yang akan diberikan oleh NODE MCU

static const uint32_t DELAY_1_SECOND  = 1000UL;
static const uint32_t DELAY_1_MINUTE  = DELAY_1_SECOND * 60UL;
static const uint32_t DELAY_1_HOUR    = DELAY_1_MINUTE * 60UL;
static const char DELIMITER       = ',';

void setup() {
  Serial.begin(9600);

  pinMode(PIN_RELAY, OUTPUT);
  digitalWrite(PIN_RELAY, LOW);
  
  pinMode(PIN_TRIG, OUTPUT);
  pinMode(PIN_ULTRASONIK, INPUT);
}

void loop() {
  String data = "";
  String splitDataSetting[JUMLAH_DATA];
  Serial.println("[GET DATA ANTARES]");
  Serial.flush();
  if (Serial.available() > 0) {
    data = Serial.readStringUntil('\n');
    Serial.flush();

    for (int i = 0; i < JUMLAH_DATA; i++) {
      splitDataSetting[i] = getValue(data, i);
    }

    if (splitDataSetting[0] == "[NODEMCU]") { // Data yang diberikan ada identifier "NODEMCU"-nya
      int penyiramanOtomatis = splitDataSetting[1].toInt();
      float tinggiTandon = atof(splitDataSetting[2].c_str());
      float luasPermukaan = atof(splitDataSetting[3].c_str());
      float jarakSensor = atof(splitDataSetting[4].c_str());
      int rainProbability = splitDataSetting[5].toInt();

      float limitLiter = (tinggiTandon * luasPermukaan / 1000) * LIMIT_PERSEN / 100;

      if (penyiramanOtomatis == 1) { // Penyiraman Otomatisnya TRUE
        float nilaiKelembabanTanah = analogRead(PIN_KELEMBABAN_TANAH);
        float totalAirTandon = getVolumeTandon(jarakSensor, luasPermukaan);
        float volumeAirTerpakai = 0;

        if (rainProbability < RAIN_PROBABILTY_LIMIT) { // Dalam 3 jam kedepan, tidak akan terjadi hujan
          float tempVolumeTandon = totalAirTandon; // Menampung volume tandon sebelum di gunakan airnya (before)

          while (nilaiKelembabanTanah >= MIN_NILAI_KERING && nilaiKelembabanTanah <= MAX_NILAI_KERING) { // Tanah sedang kering
            if (totalAirTandon >= limitLiter) {
              digitalWrite(PIN_RELAY, HIGH);
            }
            else {
              Serial.println((String) "Air pada tandon kurang dari " + LIMIT_PERSEN + "%");
              break;
            }

            Serial.println("Menyiram air");
            delay(10UL * DELAY_1_SECOND); // Delay 2 menit

            nilaiKelembabanTanah = analogRead(PIN_KELEMBABAN_TANAH);
            totalAirTandon = getVolumeTandon(jarakSensor, luasPermukaan); // Menampung volume tandon setelah di gunakan airnya (after)
            Serial.println((String) "nilaiKelembabanTanah = " + nilaiKelembabanTanah);
            Serial.println((String) "totalAirTandon = " + totalAirTandon);
          }

          digitalWrite(PIN_RELAY, LOW);
          volumeAirTerpakai = tempVolumeTandon - totalAirTandon; // Menghitung keluarnya air (before - after)
        }

        // kirim data ke tabel Report dan S_Ultrasonik
        Serial.println("[ARDUINO UNO]");
        Serial.println((String) nilaiKelembabanTanah + DELIMITER + volumeAirTerpakai + DELIMITER + totalAirTandon);
        Serial.flush();
      }
      Serial.println((String) "Tanah tidak sedang kering atau air pada tandon kurang dari " + LIMIT_PERSEN + "%");
      delay(30UL * DELAY_1_SECOND); // Delay 2 menit
    }
  }
}

float convertCentimeter(long duration) {
  return duration * 0.034 / 2; // Speed of sound wave divided by 2 (go and back)
}

float getVolumeTandon(float jarakSensor, float luasPermukaan) {
  digitalWrite(PIN_TRIG, LOW);
  digitalWrite(PIN_TRIG, HIGH);

  // Membaca jarak sensor ke permukaan air, mengembalikan dalam satuan microseconds
  long duration = pulseIn(PIN_ULTRASONIK, HIGH);

  // Mengkalkulasikan jarak tersebut menjadi volume dengan satuan liter
  float jarakAir = convertCentimeter(duration);

  float tinggiAir = jarakSensor - jarakAir;
  float totalAirTandon = (tinggiAir * luasPermukaan) / 1000;

  return totalAirTandon;
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
