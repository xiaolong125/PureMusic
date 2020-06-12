package com.flygo.architecture.data.convert

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.lzy.okgo.callback.AbsCallback
import okhttp3.Response
import java.lang.reflect.ParameterizedType

/**
 * 作者：ly-xuxiaolong
 * 版本：1.0
 * 创建日期：2020/6/5
 * 描述：
 * 修订历史：
 */
abstract class JsonCallback<T> : AbsCallback<T>() {

    @Throws(Throwable::class)
    override fun convertResponse(response: Response): T? {
        if (response.isSuccessful) {
            if (response.body() == null) {
                return null
            }
            val data: T?
            val gson = Gson()
            val jsonReader =
                JsonReader(response.body()!!.charStream())
            val genType = javaClass.genericSuperclass!!
            val type =
                (genType as ParameterizedType).actualTypeArguments[0]
            data = gson.fromJson(jsonReader, type)
            //            String json = gson.toJson(data);
            return data
        }
        return null
    }

    override fun onSuccess(response: com.lzy.okgo.model.Response<T>?) {
        TODO("Not yet implemented")
    }
}