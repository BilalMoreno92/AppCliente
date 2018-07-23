package e.android9ed.appcliente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextView numBandera;
    ArrayList<String> numeros;
    String numerosStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("Imagenes", 0);
        String[] valores = settings.getString("numeros", "").split(",");
        if (valores != null) {
            for (String numero :
                    valores) {
                numeros.add(numero);
            }
        }


        numBandera = findViewById(R.id.numBandera);
    }

    public void onClick(View v){
        String numero = numBandera.getText().toString();
        if (numero.length() == 0){
            Toast.makeText(getApplicationContext(), "Indique un número.", Toast.LENGTH_SHORT).show();
        } else if (Integer.valueOf(numero) <= 0 || Integer.valueOf(numero) >= 195){
            Toast.makeText(getApplicationContext(), "El ID de bandera indicado no existe.", Toast.LENGTH_LONG).show();
        } else {
            //ToDo Mostrar bandera;
            //Crear intent y llamar a la actividad indicando la bandera a añadir.
            if (numeros == null){
                numeros = new ArrayList<String>();
            }
            numeros.add(numero);
            Intent intent = new Intent(this, Main2Activity.class);
            intent.putExtra("bandera", Integer.valueOf(numero));
            intent.putExtra("numeros", numeros);
            numerosStr = numeros.toString();
            numerosStr = numerosStr.substring(1, numerosStr.length());
            SharedPreferences settings = getSharedPreferences("Imagenes", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("numeros", numerosStr);
            editor.commit();
            startActivity(intent);
        }
    }
}
