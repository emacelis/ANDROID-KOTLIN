package com.ejemplo.insert.database.oauth2udemyretrofitjava.ui;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ejemplo.insert.database.oauth2udemyretrofitjava.R;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.api.WebServiceOuath2;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.api.WebServiceOuathApi;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.MovimientoBancario;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.Token;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.model.User;
import com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager;

import org.json.JSONObject;

import java.util.List;

import static com.ejemplo.insert.database.oauth2udemyretrofitjava.shared_preferences.TokenManager.SHARED_PREFERENCES;

public class LogeadoActivity extends AppCompatActivity {

    private Button btVerTodolosMovimeintosBancarios;
    private Button btVerTodolosMovimeintosBancariosUsuario;
    private TokenManager tokenManager;

    private Activity activity;//pada deslogearnos en caso de refresh caduco

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);


        setUpView();
        activity =this;//para decirle en que actividad estamos
    }

    private void setUpView() {
        tokenManager= TokenManager.getInstance(getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE));

        btVerTodolosMovimeintosBancarios=findViewById(R.id.btVerTodosLosMovBancarios);
        btVerTodolosMovimeintosBancarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verTodolosmovimientos();
            }
        });

        btVerTodolosMovimeintosBancariosUsuario=findViewById(R.id.btVertodoslosmovimientosBancariosUser);
        btVerTodolosMovimeintosBancariosUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vertTodosoLosMovimientosBancariosUser();
            }
        });
    }

    private void verTodolosmovimientos() {
        Call<List<MovimientoBancario>>call=WebServiceOuath2
                .getInstance()
                .createService(WebServiceOuathApi.class)
                .obtenermovimeitnos("Bearer "+tokenManager.getToken().getAccesToken());
        call.enqueue(new Callback<List<MovimientoBancario>>() {
            @Override
            public void onResponse(Call<List<MovimientoBancario>> call, Response<List<MovimientoBancario>> response) {
                if(response.code()==200){
                    for(int i=0;i<response.body().size();i++){
                        Log.d("TAG1","UserId: "+response.body().get(i).getUserId()
                                +"Importe: "+response.body().get(i).getImporte()+
                                "Nombre"+response.body().get(i).getName());
                    }
                }else if(response.code()==404){
                    Log.d("TAG1","Eror");
                }else{
                    Log.d("TAG1","Eror");
                }
            }

            @Override
            public void onFailure(Call<List<MovimientoBancario>> call, Throwable t) {

            }
        });
    }


    private void vertTodosoLosMovimientosBancariosUser() {
    User user=new User();
    user.setId(3l);

        Call<List<MovimientoBancario>> call= WebServiceOuath2
                .getInstance()
                .createServiceWhithOauth2(WebServiceOuathApi.class,tokenManager)
                .obtenermovimeitnosUser(user);
        call.enqueue(new Callback<List<MovimientoBancario>>() {
            @Override
            public void onResponse(Call<List<MovimientoBancario>> call, Response<List<MovimientoBancario>> response) {
                if(response.code()==200){
                    for(int i=0;i<response.body().size();i++){
                        Log.d("Tag1","UserId"+response.body().get(i).getUserId()+
                                "Importe: "+response.body().get(i).getImporte()+
                                "Nombre: "+response.body().get(i).getName());
                    }
                }else if(response.code()==404){
                    Log.d("Tag1","No hay movimientos");
                }else if(response.code()==401) {
                    //se crea un nuevo token para refrescarlo
                    Token newtoken = new Token();
                    newtoken.setRefreshToken("");
                    newtoken.setRefreshToken("");
                    //guardamos el token referscado
                    tokenManager.savedToken(newtoken);
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Log.d("TAG1", "Invalid Acces Token"
                                + jsonObject.getString("error"));
                    } catch (Exception e) {
                        Log.d("TAG1", "Invalid Acces Token" + e.getMessage());
                    }
                    //para terminar la actividad en caso de que refresh TOKEN caduque
                    activity.finish();
                }else
                {
                    Log.d("TAG1", "Eroro: Invalid Acces Token");
                }
            }

            @Override
            public void onFailure(Call<List<MovimientoBancario>> call, Throwable t) {

            }
        });
    }
}
