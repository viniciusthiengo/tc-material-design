package br.com.thiengo.tcmaterialdesign;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.domain.Contact;
import br.com.thiengo.tcmaterialdesign.domain.Response;
import br.com.thiengo.tcmaterialdesign.domain.WrapObjToNetwork;
import br.com.thiengo.tcmaterialdesign.extras.UtilTCM;
import br.com.thiengo.tcmaterialdesign.network.NetworkConnection;
import br.com.thiengo.tcmaterialdesign.network.Transaction;

/**
 * Created by viniciusthiengo on 8/2/15.
 */
public class ContactActivity extends AppCompatActivity implements Transaction {

    private Toolbar mToolbar;

    private View flProxy;
    private TextInputLayout tilSubject;
    private EditText etSubject;
    private TextInputLayout tilMessage;
    private EditText etMessage;

    private Contact contact;
    private Car car;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Contato Empresa");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);


        if( getIntent() != null && getIntent().getParcelableExtra("car") != null ){
            car = getIntent().getParcelableExtra("car");
        }

        flProxy = findViewById(R.id.fl_proxy);

        tilSubject = (TextInputLayout) findViewById(R.id.til_subject);
        etSubject = (EditText) findViewById(R.id.et_subject);
        tilMessage = (TextInputLayout) findViewById(R.id.til_message);
        etMessage = (EditText) findViewById(R.id.et_message);
    }


    @Override
    protected void onStop() {
        super.onStop();
        NetworkConnection.getInstance(this).getRequestQueue().cancelAll(ContactActivity.class.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        if( id == R.id.action_send ){
            boolean hasError = false;

            if( etSubject.getText().toString().trim().length() == 0 ){
                tilSubject.setErrorEnabled(true);
                tilSubject.setError("Entre com um assunto");
                hasError = true;
            }
            else{
                tilSubject.setErrorEnabled(false);
            }
            if( etMessage.getText().toString().trim().length() == 0 ){
                tilMessage.setErrorEnabled(true);
                tilMessage.setError("Entre com uma mensagem de contato");
                hasError = true;
            }
            else{
                tilMessage.setErrorEnabled(false);
            }

            if( !hasError ){
                contact = new Contact();
                contact.setEmail( UtilTCM.getEmailAccountManager(this) );
                contact.setSubject(etSubject.getText().toString() );
                contact.setMessage( etMessage.getText().toString() );

                NetworkConnection.getInstance(this).execute( this, ContactActivity.class.getName() );
            }
        }

        return true;
    }


    @Override
    public WrapObjToNetwork doBefore() {
        flProxy.setVisibility(View.VISIBLE);

        if( UtilTCM.verifyConnection(this) ){
            return( new WrapObjToNetwork(car, "send-contact", contact ) );
        }
        return null;
    }


    @Override
    public void doAfter(JSONArray jsonArray) {
        flProxy.setVisibility(View.GONE);

        if(jsonArray != null){
            try{
                Gson gson = new Gson();
                Response response = gson.fromJson( jsonArray.getJSONObject( 0 ).toString(), Response.class );

                android.support.design.widget.Snackbar.make(findViewById(R.id.cl_container),
                        response.getMessage(),
                        android.support.design.widget.Snackbar.LENGTH_LONG)
                        .show();

                if( response.getStatus() ){
                    etSubject.setText("");
                    etMessage.setText("");
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        else{
            android.support.design.widget.Snackbar.make(findViewById(R.id.cl_container),
                    "Falhou, tente novamente.",
                    android.support.design.widget.Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
