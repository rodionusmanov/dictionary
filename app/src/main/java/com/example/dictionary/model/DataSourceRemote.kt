package com.example.dictionary.model

import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.retrofit.RetrofitImplementation
import com.example.dictionary.model.room.RoomDataBaseImplementation
import io.reactivex.rxjava3.core.Observable

class DataSourceRemote (private val remoteProvider: RetrofitImplementation =
                            RetrofitImplementation()
) :
    DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> =
        remoteProvider.getData(word)
}

class DataSourceLocal(private val remoteProvider: RoomDataBaseImplementation =
                          RoomDataBaseImplementation()
) :
    DataSource<List<DataModel>> {
    override fun getData(word: String): Observable<List<DataModel>> =
        remoteProvider.getData(word)
}