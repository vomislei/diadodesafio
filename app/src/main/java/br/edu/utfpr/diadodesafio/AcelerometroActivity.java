package br.edu.utfpr.diadodesafio;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AcelerometroActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvNivel;
    private TextView tvContador;
    private int countLeitura = 0;
    private double contadorx, contadory, contadorz, countxyz;
    private SensorManager sm;
    private Sensor sensor;
    private Boolean iniciar = false;
    private Button btMonitoramento;
    private Chronometer cronometro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acelerometro);

        tvNivel = (TextView) findViewById(R.id.tvNivel);
        tvContador = (TextView) findViewById(R.id.tvContador);
        cronometro = (Chronometer) findViewById(R.id.cronometro);
        btMonitoramento = (Button) findViewById(R.id.btMonitoramento);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener( this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];
        if(iniciar == true) {
            countLeitura++;
            tvContador.setText(String.valueOf(countLeitura));
//        contadorx = contadorx + x;
//        contadory = contadory + y;
//        contadorz = contadorz + z;
            countxyz = (x + y + z) / 3;

            if (countLeitura == 100) {
                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
                String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
                countLeitura = 0;
                //double media = countxyz / 100;

                //salvar essa media no banco
                double media = (contadorx + contadory +contadorx) / 100;

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
}
