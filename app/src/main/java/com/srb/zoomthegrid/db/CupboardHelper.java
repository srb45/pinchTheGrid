package com.srb.zoomthegrid.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.srb.zoomthegrid.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CupboardHelper {

    public static final String TAG = CupboardHelper.class.getSimpleName();

    private static SQLiteDatabase getDatabase() {
        return DataBaseHelper.getInstance().getDatabase();
    }

    public static <T extends BaseDbItem> void insertOrReplace(T item) {
        if (cupboard().isRegisteredEntity(item.getClass())) {
            cupboard().withDatabase(getDatabase()).put(item);
        } else {
            Logger.e(TAG, "Class not registered with Cupboard.");
        }
    }

    public static <T extends BaseDbItem> void insertOrReplaceList(final List<T> items, final DbSaveCallback saveCallback) {
        if (items.isEmpty())  {
            saveCallback.done();
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (cupboard().isRegisteredEntity(items.get(0).getClass())) {
                    cupboard().withDatabase(getDatabase()).put(items);
                } else {
                    Logger.e(TAG, "Class not registered with Cupboard.");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                saveCallback.done();
            }
        }.execute();
    }

    public static <T extends BaseDbItem> T get(Class<T> clazz, String whereClause) {
        DatabaseCompartment.QueryBuilder<T> builder = cupboard()
                .withDatabase(DataBaseHelper.getInstance().getDatabase()).query(clazz);

        return builder.withSelection(whereClause).limit(1).get();
    }

    public static <T extends BaseDbItem> List<T> getAll(Class<T> clazz) {
        if (cupboard().isRegisteredEntity(clazz)) {
            DatabaseCompartment.QueryBuilder<T> builder = cupboard().withDatabase(getDatabase()).query(clazz);
            return builder.query().list();
        } else {
            Logger.e(TAG, clazz.getSimpleName() + ": Class not registered with Cupboard.");
            return new ArrayList<>();
        }
    }

    public static <T extends BaseDbItem> void delete(Class<T> clazz, String whereClause) {
        cupboard().withDatabase(DataBaseHelper.getInstance().getDatabase()).delete(clazz, whereClause);
    }
}
