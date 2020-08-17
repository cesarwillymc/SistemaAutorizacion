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

    @POST("service/signup")
    suspend fun createDocente(
        @Body usuario: Usuario
    ): Response<responseGeneral>



    @Multipart
    @PUT("service/logoService")
    suspend fun sendImageWithId(
        @Part file: MultipartBody.Part,
        @Part("image") name: RequestBody,
        @Query("id") key:String?
    ): Response<responseGeneral>
    @POST("service/signin")
    suspend fun loginUser(
        @Body request: requestSignIn
    ): Response<responseGeneral>

    @GET("service/information")
    suspend fun getUserInfo(): Response<Usuario>
    //PERSONAL
    @GET("service/information")
    suspend fun getListPersonal(): Response<List<PersonalList>>
    @PUT("service/information/{id}")
    suspend fun putPersonal(
        @Path("id") id:String,
        @Body usuario: Usuario
    ): Response<responseGeneral>
    @DELETE("service/information/{id}")
    suspend fun deletepersonal(
        @Path("id") id:String
    ): Response<responseGeneral>
    @POST("service/information/{id}")
    suspend fun createPersonal(
        @Body usuario: Usuario
    ): Response<responseGeneral>
    @GET("service/information/{id}")
    suspend fun getInfoPersonal(
        @Path("id") id:String
    ):Response<Usuario>
    //Comerciante
    @GET("service/information")
    suspend fun getListComerciante(): Response<List<ComercianteList>>

    @GET("service/information/{id}")
    suspend fun getInfoComerciante(
        @Path("id") id:String
    ):Response<Comerciantes>

    @DELETE("service/information/{id}")
    suspend fun deletecomerciante(
        @Path("id") id:String
    ): Response<responseGeneral>

    @GET("service/information/{id}")
    suspend fun getQRcomerciante(
        @Path("id") id:String
    ): Response<responseGeneral>


    @Multipart
    @PUT("service/logoService")
    suspend fun uploadExcel(
        @Part file: MultipartBody.Part,
        @Part("excel") name: RequestBody
    ): Response<responseGeneral>

    @PUT("service/information/{id}")
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