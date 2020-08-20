package com.summit.sistemaautorizacion.data.retrofit


import com.summit.sistemaautorizacion.common.Constants.BASE_URL_API
import com.summit.sistemaautorizacion.data.model.*
import com.thesummitdev.daloo.rider.data.remote.model.request.requestSignIn
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiRetrofit {



    @Multipart
    @PUT("user/uploadImg/{id}")
    suspend fun sendImageWithId(
        @Part file: MultipartBody.Part,
        @Part("imagen") name: RequestBody,
        @Path("id") key:String?
    ): Response<responseGeneral>
    @POST("auth/signin")
    suspend fun loginUser(
        @Body request: requestSignIn
    ): Response<responseGeneral>

    @GET("user/information")
    suspend fun getUserInfo(): Response<Usuario>
    //PERSONAL
    @GET("user/all")
    suspend fun getListPersonal(): Response<List<PersonalList>>
    @PUT("user/{id}")
    suspend fun putPersonal(
        @Path("id") id:String,
        @Body usuario: Usuario
    ): Response<responseGeneral>
    @FormUrlEncoded
    @PUT("user/changePassword/{id}")
    suspend fun changePasswordPersonal(
        @Path("id") id:String,
        @Field("password") password: String
    ): Response<responseGeneral>
    @DELETE("user/{id}")
    suspend fun deletepersonal(
        @Path("id") id:String
    ): Response<responseGeneral>
    @POST("auth/signup")
    suspend fun createPersonal(
        @Body usuario: Usuario
    ): Response<responseGeneral>
    @GET("service/information/{id}")
    suspend fun getInfoPersonal(
        @Path("id") id:String
    ):Response<Usuario>
    //Comerciante
    @GET("trader/all")
    suspend fun getListComerciante(): Response<List<ComercianteList>>

    @GET("trader/information/{id}")
    suspend fun getInfoComerciante(
        @Path("id") id:String
    ):Response<Comerciantes>

    @DELETE("trader/{id}")
    suspend fun deletecomerciante(
        @Path("id") id:String
    ): Response<responseGeneral>

    @GET("trader/qr/{id}")
    suspend fun getQRcomerciante(
        @Path("id") id:String
    ): Response<responseGeneral>


    @Multipart
    @POST("trader/csv")
    suspend fun uploadExcel(
        @Part file: MultipartBody.Part,
        @Part("fileCsv") name: RequestBody
    ): Response<responseGeneral>

    @PUT("trader/{id}")
    suspend fun putComerciante(
        @Path("id") id:String,
        @Body comerciantes: Comerciantes
    ): Response<responseGeneral>




    companion object{
        operator fun invoke() : ApiRetrofit{
            val okHttpClienteBuilder= OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
            okHttpClienteBuilder.addInterceptor(InterceptorToken())
            val cliente: OkHttpClient = okHttpClienteBuilder.build()
            return   Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiRetrofit::class.java)
        }
    }
}