package e.android9ed.appcliente;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    GridView banderas;
    ArrayList<String> numeros;
    String direccion = "192.168.1.67";
    final int PERMISO_INTERNET = 20;
    BitmapAdapter bitmapAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PERMISO_INTERNET);
        } else {
            Toast.makeText(this, "Permiso concedido.", Toast.LENGTH_SHORT).show();
        }
        banderas = findViewById(R.id.gvBanderas);
        Intent intent = getIntent();
        int valorNum=intent.getIntExtra("bandera",-1);

        bitmapAdapter = new BitmapAdapter(this, R.layout.gridview_item);
        if (intent != null){
            numeros = intent.getStringArrayListExtra("numeros");
            for (String numero :
                    numeros) {
                DescargarImagenes descargarImagenes = new DescargarImagenes();
                descargarImagenes.execute(numero);
            }

        }
//        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), R.layout.gridview_item, (ArrayList) imagenes);
        banderas.setAdapter(bitmapAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISO_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permiso concedido.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private  class DescargarImagenes extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL("http://"+ direccion +":8080/image.php?n=" + strings[0]);
                InputStream is = url.openStream();
                Bitmap imagen = BitmapFactory.decodeStream(is);
                Log.d("Downloaded", imagen.getConfig().toString());
                return imagen;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                bitmapAdapter.add(bitmap);
            }
        }
    }

    private class BitmapAdapter extends ArrayAdapter<Bitmap>{
        Context context;
        public BitmapAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ImageView imagen = new ImageView(context);
            int columnWidth = ((GridView)parent).getColumnWidth();
            imagen.setLayoutParams(new
                    GridView.LayoutParams(columnWidth,columnWidth));
            imagen.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Bitmap bitmap = getItem(position);
            imagen.setImageBitmap(bitmap);
            return imagen;
        }
    }

//    private class ImageAdapter extends BaseAdapter{
//
//        Context context;
//        int recursoId;
//        ArrayList datos;
//
//        public ImageAdapter(Context context, int recursoId, ArrayList datos) {
//            this.context = context;
//            this.recursoId = recursoId;
//            this.datos = datos;
//        }
//
//        @Override
//        public int getCount() {
//            return datos.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return datos.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
////            ImageView imageView;
//            View view;
//            DescargarImagenes descargarImagenes = new DescargarImagenes();
//
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            if (convertView == null) { //Nuevo es necesario crear
//                view = inflater.inflate(R.layout.gridview_item, null);
//
//            } else { //Reciclado, ya creado
//                view = (View) convertView;
//            }
//            ImageView bandera = view.findViewById(R.id.imgBandera);
//            //ToDo Obtener bandera en concreto
//            int imagen = (int) datos.get(position);
//            descargarImagenes.execute(Integer.toString(imagen));
//            return view;
//        }
//    }
}
