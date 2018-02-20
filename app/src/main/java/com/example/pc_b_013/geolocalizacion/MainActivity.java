package com.example.pc_b_013.geolocalizacion;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    LinearLayout l;
    Button localizame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = findViewById(R.id.milayout);
        localizame = findViewById(R.id.button2);
        localizame.setEnabled(false);
        localizame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });
        if(verificarPermisos())
        {
            cargarApp();
        }
        else
        {
            solicitarPermisos();
        }

    }

    private void solicitarPermisos() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, ACCESS_COARSE_LOCATION))
        {
            Snackbar.make(l,"Te has olvidado de los permisos.",Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION
                    },100);
                }
            }).show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
            },100);
        }
    }

    private void cargarApp() {
        localizame.setEnabled(true);
    }

    private boolean verificarPermisos() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            return true;
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults)
    {
        switch(requestCode)
        {
            case 100:
                if(grantResults.length == 0
                        || grantResults[1] == PackageManager.PERMISSION_DENIED
                        || grantResults[2] == PackageManager.PERMISSION_DENIED)
                {
                    // Este codigo solo se activa si se ah rechazado alguna peticion
                    // Codigo Opcional para abrir la configuracion de la aplicacion
                    AlertDialog.Builder ventana = new AlertDialog.Builder(MainActivity.this);
                    ventana.setTitle("Permisos Negados");
                    ventana.setMessage("Necesitas otorgar los Permisos");
                    ventana.setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent configuracion = new Intent();
                            configuracion.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri direccion = Uri.fromParts("package",getPackageName(),null);
                            configuracion.setData(direccion);
                            startActivity(configuracion);
                        }
                    });
                    ventana.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Esta es una tostada, la app se cerro",Toast.LENGTH_LONG).show();
                        }
                    });
                    ventana.show();
                }
                else
                {
                    cargarApp();
                }
            break;
        }
    }
}
