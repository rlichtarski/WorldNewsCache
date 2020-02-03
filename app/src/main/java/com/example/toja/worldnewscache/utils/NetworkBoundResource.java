package com.example.toja.worldnewscache.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.toja.worldnewscache.responses.ApiResponse;

//CacheObject: Type for the Resource data (database cache)
//RequestObject: Type for the API response (network request)

public abstract class NetworkBoundResource<CacheObject,RequestObject> {

    private MediatorLiveData<Resource<CacheObject>> results = new MediatorLiveData<>();

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestObject item);

    @MainThread
    protected abstract boolean shouldFetch(@Nullable CacheObject data);

    @NonNull @MainThread
    protected abstract LiveData<CacheObject> loadFromDb();

    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestObject>> createCall();

    public final LiveData<Resource<CacheObject>> getAsLiveData() {
        return results;
    }
}
