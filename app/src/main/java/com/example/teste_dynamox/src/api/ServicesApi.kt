package com.example.teste_dynamox.src.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ServicesApi {
   @GET(BASE_URL)
   fun getPergunta(): Call<QuizModel>

   @POST
   fun checkAnswer(@Url url: String?, @Body answerRequest: AnswerRequest): Call<ServerResponse>
}