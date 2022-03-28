package com.ig.email.model.custom

import com.ig.email.model.Response

class ResponseObject {

    fun responseObject(obj: Any): MutableMap<String, Any> {
        val response: MutableMap<String, Any> = HashMap()
        response["response"] = Response(200, "Success!!")
        response["result:"] = obj
        return response
    }

    fun responseStatusCode(code: Int, message: String): MutableMap<String, Any> {
        val res = Response(code, message)
        val response: MutableMap<String, Any> = HashMap()
        response["response"] = res
        return response
    }
}