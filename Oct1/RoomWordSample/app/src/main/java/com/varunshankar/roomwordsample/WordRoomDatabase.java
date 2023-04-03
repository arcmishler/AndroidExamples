package com.varunshankar.roomwordsample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Word.class}, version=1,exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    private static volatile WordRoomDatabase mInstance;

    public abstract WordDao wordDao();


    static WordRoomDatabase getDatabase(final Context context){
        if(mInstance==null){
            synchronized(WordRoomDatabase.class){
                if(mInstance==null){
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),WordRoomDatabase.class,"word_database")
                                .addCallback(sRoomDatabaseCallback)
                                .build();
                }
            }
        }
        return mInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(mInstance).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {
        private final WordDao mDao;

        PopulateDbAsync(WordRoomDatabase db){
            mDao = db.wordDao();
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            mDao.deleteAll();
            Word word = new Word("Hello");
            mDao.insert(word);
            return null;
        }
    }
}
