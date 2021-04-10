package com.bear.wanandroidbyjava.Storage.DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bear.wanandroidbyjava.Data.Entity.ArticleE;
import com.bear.wanandroidbyjava.Data.Entity.ProjectTabArticleRef;
import com.bear.wanandroidbyjava.Data.Entity.ProjectTabE;
import com.bear.wanandroidbyjava.Data.Entity.PublicTabArticleRef;
import com.bear.wanandroidbyjava.Data.Entity.PublicTabE;
import com.example.liblog.SLog;

@Database(version = 1, exportSchema = false,
        entities = {ArticleE.class, PublicTabE.class, PublicTabArticleRef.class, ProjectTabE.class, ProjectTabArticleRef.class})
public abstract class WanRoomDataBase extends RoomDatabase {
    private static final String TAG = "WanRoomDataBase";
    private static WanRoomDataBase INSTANCE;

    public abstract ArticleDao articleDao();
    public abstract PublicDao publicDao();
    public abstract ProjectDao projectDao();

    public static void init(Application app) {
        INSTANCE = Room.databaseBuilder(app, WanRoomDataBase.class, "wan_database")
                .addCallback(sCallback)
                .addMigrations(sMigration_1_2)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static WanRoomDataBase get() {
        return INSTANCE;
    }

    private static Callback sCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            SLog.d(TAG, "onOpen");
            // Using triggers is more difficult
//            db.execSQL(
//                    "CREATE TRIGGER IF NOT EXISTS trigger_insert_article BEFORE INSERT ON ArticleE FOR EACH ROW BEGIN UPDATE ArticleE SET reference = 1 + OLD.reference WHERE articleId = NEW.articleId; END"
//            );
//            db.execSQL(
//                    "CREATE TRIGGER IF NOT EXISTS trigger_delete_article AFTER DELETE ON ArticleE FOR EACH ROW BEGIN INSERT INTO ArticleE OLD WHERE articleId = NEW.articleId; END"
//            );
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            SLog.d(TAG, "onCreate");
        }

        @Override
        public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
            super.onDestructiveMigration(db);
            SLog.d(TAG, "onDestructiveMigration");
        }
    };

    private static Migration sMigration_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
