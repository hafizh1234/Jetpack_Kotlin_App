package com.projek.jetpack.academies.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.projek.jetpack.academies.data.source.remote.ApiResponse
import com.projek.jetpack.academies.data.source.remote.StatusResponse
import com.projek.jetpack.academies.utils.AppExecutors
import com.projek.jetpack.academies.vo.Resource

abstract class NetworkBoundResources<ResultType, RequestType>(private val mExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)

        @Suppress("LeakingThis")
        val dbSource = loadFromDB()
        //mengawasi hasil live dari livedata lain dbSource dari db, setelah selesai dapat datany
        result.addSource(dbSource) { data ->
            //data yang lagi diawasi di lepas pas datanya sudah dapat
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.value = Resource.succes(newData)
                }
            }
        }
    }

    protected fun onFetchFailed() {}
    protected abstract fun loadFromDB(): LiveData<ResultType>
    protected abstract fun shouldFetch(data: ResultType?): Boolean
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
    protected abstract fun saveCallResult(data: RequestType)
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData ->
            result.value = Resource.loading(newData)
        }
        //mendengarkan apiResponse sampai dapat hasilnya, terus hasilnya diterusin di listener dalam bentuk lambda
        result.addSource(apiResponse) { response ->
            //saat dapat hasilnya dalam bentuk response, cek
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response.status) {
                StatusResponse.SUCCESS ->
                    mExecutors.diskIO().execute {
                        saveCallResult(response.body)
                        mExecutors.mainThread().execute {
                            result.addSource(loadFromDB()) { newData ->
                                result.value = Resource.succes(newData)
                            }
                        }
                    }
                StatusResponse.EMPTY->mExecutors.mainThread().execute{
                    result.addSource(loadFromDB()){newData->
                        result.value=Resource.succes(newData)
                    }
                }
                StatusResponse.ERROR->{
                    onFetchFailed()
                    result.addSource(dbSource){newData->
                        result.value=Resource.error(response.message,newData)
                    }
                }
            }
        }
    }
    fun asLiveData():LiveData<Resource<ResultType>> =result
}