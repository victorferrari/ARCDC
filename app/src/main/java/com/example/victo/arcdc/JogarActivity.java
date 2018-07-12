package com.example.victo.arcdc;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.victo.arcdc.Adapter.ListaComandosAdapter;
import com.example.victo.arcdc.Adapter.TabelaComandosAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class JogarActivity extends AppCompatActivity {

    GridView tabela_comandos;
    ListView lista_comandos;

    int[] comandos = {R.drawable.cima, R.drawable.baixo, R.drawable.direita, R.drawable.esquerda,
            R.drawable.gira_direita, R.drawable.gira_esquerda};
    int[] lista = new int[9];
    int posicao = 0;

    private static final int SOLICITA_ATIVACAO = 1;
    private static final int SOLICITA_CONEXAO = 2;

    ConnectedThread connectedThread;

    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ListaComandosAdapter adapterLista;
    TabelaComandosAdapter adapterTabela;

    BluetoothAdapter meuBluetoothAdapter = null;
    BluetoothDevice bluetoothDevice = null;
    BluetoothSocket bluetoothSocket = null;

    boolean conexao = false;

    ArrayList<String> comandosEnviar = new ArrayList<String>();
    //String comandosEnviar = null;

    private static String MAC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogar);

        Toolbar toolbar = findViewById(R.id.toolbar_jogar);
        setSupportActionBar(toolbar);

        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (meuBluetoothAdapter == null) {
            Toast.makeText(JogarActivity.this, "Dispositivo não conectado", Toast.LENGTH_SHORT).show();
        } else if (!meuBluetoothAdapter.isEnabled()) {
            Intent ativaBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativaBluetooth, SOLICITA_ATIVACAO);
        }

        tabela_comandos = findViewById(R.id.tabela_comandos);
        lista_comandos = findViewById(R.id.lista_comandos);

        adapterLista = new ListaComandosAdapter(JogarActivity.this, comandos);
        lista_comandos.setAdapter(adapterLista);

        adapterTabela = new TabelaComandosAdapter(JogarActivity.this, lista);
        tabela_comandos.setAdapter(adapterTabela);

        lista_comandos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        if (posicao < 9) {
                            comandosEnviar.add("frente");
                            adicionaComando(lista, posicao, R.drawable.cima);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1:
                        if (posicao < 9) {
                            comandosEnviar.add("tras");
                            adicionaComando(lista, posicao, R.drawable.baixo);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 2:
                        if (posicao < 9) {
                            comandosEnviar.add("frente");
                            adicionaComando(lista, posicao, R.drawable.direita);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 3:
                        if (posicao < 9) {
                            comandosEnviar.add("frente");
                            adicionaComando(lista, posicao, R.drawable.esquerda);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 4:
                        if (posicao < 9) {
                            comandosEnviar.add("giraDireita");
                            adicionaComando(lista, posicao, R.drawable.gira_direita);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 5:
                        if (posicao < 9) {
                            comandosEnviar.add("giraEsquerda");
                            adicionaComando(lista, posicao, R.drawable.gira_esquerda);
                            posicao++;
                        } else {
                            Toast.makeText(JogarActivity.this, "Limite de comandos atingido", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        carregaLista(lista);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SOLICITA_ATIVACAO:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(JogarActivity.this, "Bluetooth ativado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JogarActivity.this, "Bluetooth não ativado", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;

            case SOLICITA_CONEXAO:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = data.getExtras().getString(ListaDispositivos.ENDERECO_MAC);
                    Toast.makeText(JogarActivity.this, "MAC: " + MAC, Toast.LENGTH_SHORT).show();
                    bluetoothDevice = meuBluetoothAdapter.getRemoteDevice(MAC);

                    try {
                        bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MEU_UUID);
                        bluetoothSocket.connect();
                        conexao = true;

                        connectedThread = new ConnectedThread(bluetoothSocket);
                        connectedThread.start();

                        Toast.makeText(JogarActivity.this, "Conectado com: " + MAC, Toast.LENGTH_SHORT).show();
                    } catch (IOException erro) {
                        conexao = false;
                        Toast.makeText(JogarActivity.this, "Obteve erro: " + erro, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(JogarActivity.this, "Não obteve o MAC", Toast.LENGTH_SHORT).show();
                }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaLista(lista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_jogar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_jogar_voltar:
                finish();
                break;

            case R.id.menu_jogar_enviar:
                if (listaVazia(posicao)) {
                    Toast.makeText(JogarActivity.this, "Lista de comandos vazia", Toast.LENGTH_SHORT).show();
                } else {
                    for (int j = 0; j < comandosEnviar.size(); j++) {
                        connectedThread.enviarDados(comandosEnviar.get(j));
                    }
                    comandosEnviar.clear();
                    lista = new int[9];
                    adapterTabela = null;
                    carregaLista(lista);
                    posicao = 0;
                }
                break;

            case R.id.menu_jogar_retirar:
                if (listaVazia(posicao)) {
                    Toast.makeText(JogarActivity.this, "Lista de comandos vazia", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JogarActivity.this, "Comando Retirado", Toast.LENGTH_SHORT).show();
                    posicao--;
                    lista = removeComando(lista, posicao);
                    adapterTabela = null;
                    carregaLista(lista);
                }
                break;

            case R.id.menu_jogar_bluetooth:
                if (conexao) {
                    try {
                        bluetoothSocket.close();
                        Toast.makeText(JogarActivity.this, "Bluetooth desconectado", Toast.LENGTH_SHORT).show();
                    } catch (IOException erro) {
                        Toast.makeText(JogarActivity.this, "Ocorreu erro: " + erro, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Intent abreLista = new Intent(JogarActivity.this, ListaDispositivos.class);
                    startActivityForResult(abreLista, SOLICITA_CONEXAO);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    private void carregaLista(int[] lista1) {

        if (adapterTabela == null) {
            adapterTabela = new TabelaComandosAdapter(JogarActivity.this, lista1);
            tabela_comandos.setAdapter(adapterTabela);
        } else {
            adapterTabela.notifyDataSetChanged();
        }
    }

    private void adicionaComando(int[] lista, int posicao, int comando) {
        lista[posicao] = comando;
        carregaLista(lista);
    }

    private int[] removeComando(int[] lista, int posicao) {
        int[] listaAux = new int[9];
        for (int i = 0; i < posicao; i++) {
            listaAux[i] = lista[i];
        }
        return listaAux;
    }

    private boolean listaVazia(int posicao) {
        if (posicao == 0) {
            return true;
        } else {
            return false;
        }
    }

    private class ConnectedThread extends Thread {

        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        /* Call this from the main activity to send data to the remote device */
        public void enviarDados(String enviarDados) {
            byte[] msgBuffer = enviarDados.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
            }
        }
    }
}
