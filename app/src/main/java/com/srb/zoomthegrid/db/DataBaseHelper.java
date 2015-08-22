package com.srb.zoomthegrid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import com.srb.zoomthegrid.db.models.Picture;

import java.util.Collection;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DataBaseHelper.class.getSimpleName();
    public static final String DB_NAME = "JaiGurudev.db";
    private static final int DB_VERSION = 1;
    private static DataBaseHelper mDataBaseHelper = null;
    private static SQLiteDatabase mDataBase = null;

    static {
        cupboard().register(Picture.class);
    }

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDataBase = getWritableDatabase();
    }

    public synchronized static void initDB(Context c) {
        if (mDataBaseHelper == null)
            mDataBaseHelper = new DataBaseHelper(c);
    }

    public static DataBaseHelper getInstance() throws IllegalStateException {
        if (mDataBaseHelper == null)
            throw new IllegalStateException("Database Not Initialized");
        return mDataBaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                db.setMaxSqlCacheSize(SQLiteDatabase.MAX_SQL_CACHE_SIZE);
        }
    }

    public void clearTables() {
        Collection<Class<?>> classes = cupboard().getRegisteredEntities();
        for (Class clazz : classes) {
            cupboard().withDatabase(mDataBase).delete(clazz, "");
        }
    }

    public SQLiteDatabase getDatabase() {
        return mDataBase;
    }
}