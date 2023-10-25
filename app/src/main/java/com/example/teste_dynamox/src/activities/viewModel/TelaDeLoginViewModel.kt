package com.example.teste_dynamox.src.activities.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.teste_dynamox.src.activities.telas.optionss
import com.example.teste_dynamox.src.activities.telas.statementt
import com.example.teste_dynamox.src.api.QuizModel
import com.example.teste_dynamox.src.databaseLocal.Users
import com.example.teste_dynamox.src.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

private const val TAG = "TelaDeQuestões"

class TelaDeLoginViewModel(
   val repository: Repository,
   //   private val context: Context,
) : ViewModel() {
   private val _listaDeUsernames = MutableLiveData<List<String>>()
   val listaDeUsernames = _listaDeUsernames

   //VARIÁVEIS DAS PERGUNTAS
   private val _statement = MutableLiveData<String>(null)
   val statement = _statement
   private val _id = MutableLiveData("default")
   val id = _id
   private val _options = MutableLiveData<List<String>>(mutableListOf("", "1"))
   val options = _options


   private val _ocorreuErro = MutableLiveData(false)
   val ocorreuErro = _ocorreuErro

   private val _timeOut = MutableLiveData(false)
   val timeOut = _timeOut

   private val _userNameDigitado = MutableStateFlow("")  //INTERNO
   val userNameDigitado = _userNameDigitado.asStateFlow()

   private val _usuarioLogado = MutableStateFlow<Users>(Users(userName = ""))     //INTERNO
   val userNameLogado = _usuarioLogado.asStateFlow()

   fun fazerRequisicaoENavegarParaProximaTela (navController: NavController) {
      try {  //buscar dados das perguntas na API
         val call = repository.getPerguntaRepository()

         call.enqueue(object : Callback<QuizModel> {
            override fun onResponse(call: Call<QuizModel>, response: Response<QuizModel>) {
               if (response.isSuccessful) {   //Atualiza as variáveis das perguntas

                     val quizResponse = response.body()
                     statementt = quizResponse?.statement
                     optionss = quizResponse?.options
                     _id.value = quizResponse?.id

                     navController.navigate("tela_de_questoes/$statementt")   //Traca de tela
               } else {     //mostra erro
                  Log.i(TAG, "onResponse:  A requisição Falhou!")
               }
            }

            override fun onFailure(call: Call<QuizModel>, t: Throwable) {
               _ocorreuErro.value = true


               if (t is SocketTimeoutException) {
                  _timeOut.value = true
                  Log.i(TAG, "SOCKETTIMEOUTEXCEPTION aconteceu e _timeOut vale : ${_timeOut.value}")
               } else {
                  println("onFailure foi chamado:   O erro encontrado foi: $t")
               }
            }

         })

      } catch (e: Exception) {
         Log.i(TAG, "caiu no catch: fazerRequisicaoENavegarParaProximaTela:  Erro encontrado é : $e")
      }
   }

   fun atualizaIdDaPergunta(idDaPergunta: String?) {
      _id.value = idDaPergunta
   }

   fun buscaListaDeUserNames() {
      CoroutineScope(Dispatchers.IO).launch {
         val usuarios = repository.buscaTodosUsuariosRepository()
         withContext(Dispatchers.Main) { _listaDeUsernames.value = usuarios.map { users -> users.userName } }
      }
   }

   fun buscaUsuarioLogadoPeloUserName(userNameLogado: String) {
      CoroutineScope(Dispatchers.IO).launch {
         withContext(Dispatchers.Main) {
            _usuarioLogado.value = repository.buscaUsuarioLogadoPeloUsername(userNameLogado)
         }
         Log.i(
            TAG,
            "buscaUsuarioLogadoPeloUserName: o usuario encontrado na lista de usuários  é :  $userNameLogado"
         )
      }
   }

   fun atualizaUserNameDigitado(novoUserName: String) {
      _userNameDigitado.value = novoUserName
   }
}