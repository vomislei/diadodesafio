package br.edu.utfpr.diadodesafio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AcelerometroActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    private TextView tvNivel;
    private TextView tvContador;
    private int countLeitura = 0;
    private double contadorx, contadory, contadorz, countxyz;
    private SensorManager sm;
    private Sensor sensor;
    private Boolean iniciar = false;
    private Button btMonitoramento;
    private Chronometer cronometro;
    private TextView tvLatitude;
    private TextView tvLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);


        tvNivel = (TextView) findViewById(R.id.tvNivel);
        tvContador = (TextView) findViewById(R.id.tvContador);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        cronometro = (Chronometer) findViewById(R.id.cronometro);
        btMonitoramento = (Button) findViewById(R.id.btMonitoramento);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);



        //verifica se o gps do celular está ligado, caso não esteja é mostrada uma mensagem
        //e manda para a tela de habilitar o gps
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this,"Habilitar GPS", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(i);
        }
        //ESSE COMANDO ADICIONA A PERMISSÃO DO GPS AUTOMATICAMENTE
        // ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1) ;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }       lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];
        if(iniciar == true) {
            countLeitura++;
            tvContador.setText(String.valueOf(countLeitura));
            contadorx = contadorx + x;
            contadory = contadory + y;
            contadorz = contadorz + z;

            countxyz = (x + y + z) / 3;
            // countxyz = (contadorx + contadory +contadorx) / 3;

            if (countLeitura == 100) {
                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
                String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
                countLeitura = 0;
                double media = countxyz / 100;

                //salvar essa media no banco
                //  double media = (contadorx + contadory +contadorx) / 100;
                //  double media = (contadorx + contadory +contadorx) / 3;
                countxyz = 0;

                tvNivel.setText(String.valueOf(media));

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void btMonitoramento(View view) {
        if(iniciar==false){
            btMonitoramento.setText("Parar");
            cronometro.setBase(SystemClock.elapsedRealtime());
            cronometro.start();
            iniciar = true;
        }else{
            btMonitoramento.setText("Iniciar");
            cronometro.stop();
            countxyz = 0;
            countLeitura = 0;
            iniciar = false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        tvLatitude.setText(String.valueOf(latitude));
        tvLongitude.setText(String.valueOf(longitude));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
