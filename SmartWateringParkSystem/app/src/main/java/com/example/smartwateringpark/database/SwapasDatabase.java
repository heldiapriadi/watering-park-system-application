package com.example.smartwateringpark.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.smartwateringpark.data.ReportsData;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Report.class, Setting.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
abstract class SwapasDatabase extends RoomDatabase {

    abstract ReportDao reportDao();
    abstract SettingDao settingDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile SwapasDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static SwapasDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (SwapasDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SwapasDatabase.class, "swapas_db")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                SettingDao daoSetting = INSTANCE.settingDao();

                Setting setting = new Setting("penyiramanOtomatis",1);
                daoSetting.insert(setting);
                setting = new Setting("tinggiTandon",84);
                daoSetting.insert(setting);
                setting = new Setting("luasPermukaan",(float)4069.44);
                daoSetting.insert(setting);
                setting = new Setting("jarakSensor",91);
                daoSetting.insert(setting);
                setting = new Setting("totalAirTandon",(float)300.832);
                daoSetting.insert(setting);

                ArrayList<Report> reports = ReportsData.getListReports();
                ReportDao daoReport = INSTANCE.reportDao();
                for (Report r : reports){
                    Report report = new Report(r.getTimeStamp(),r.getNilaiKelembabanTanah(),r.getVolumeAirTerpakai());
                    daoReport.insert(report);
                }
            });
        }
    };
}