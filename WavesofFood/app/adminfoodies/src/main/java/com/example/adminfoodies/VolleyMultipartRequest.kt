package com.example.adminfoodies

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import java.io.*

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, errorListener) {

    private val headersMap: MutableMap<String, String> = mutableMapOf()
    private val paramsMap: MutableMap<String, String> = mutableMapOf()
    private val fileUploads: MutableMap<String, DataPart> = mutableMapOf()

    override fun getHeaders(): MutableMap<String, String> = headersMap
    override fun getParams(): MutableMap<String, String> = paramsMap

    fun setHeaders(headers: Map<String, String>) {
        headersMap.putAll(headers)
    }

    fun setParams(params: Map<String, String>) {
        paramsMap.putAll(params)
    }

    fun addFileUpload(fieldName: String, data: DataPart) {
        fileUploads[fieldName] = data
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data; boundary=$boundary"
    }

    override fun getBody(): ByteArray {
        return buildMultipartBody()
    }

    private fun buildMultipartBody(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val writer = DataOutputStream(outputStream)

        for ((key, value) in paramsMap) {
            writer.writeBytes("--$boundary\r\n")
            writer.writeBytes("Content-Disposition: form-data; name=\"$key\"\r\n\r\n")
            writer.writeBytes("$value\r\n")
        }

        for ((key, dataPart) in fileUploads) {
            writer.writeBytes("--$boundary\r\n")
            writer.writeBytes("Content-Disposition: form-data; name=\"$key\"; filename=\"${dataPart.fileName}\"\r\n")
            writer.writeBytes("Content-Type: ${dataPart.mimeType}\r\n\r\n")
            writer.write(dataPart.data)
            writer.writeBytes("\r\n")
        }

        writer.writeBytes("--$boundary--\r\n")
        writer.flush()
        writer.close()
        return outputStream.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: NetworkResponse) {
        listener.onResponse(response)
    }

    // ✅ Fix: Ensure `getByteData()` is correctly defined
    open fun getByteData(): Map<String, DataPart> {
        return emptyMap()
    }

    companion object {
        private val boundary = "apiclient-" + System.currentTimeMillis()
    }

    data class DataPart(val fileName: String, val data: ByteArray, val mimeType: String)
}
