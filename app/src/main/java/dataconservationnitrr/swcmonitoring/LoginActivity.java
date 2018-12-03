package dataconservationnitrr.swcmonitoring;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
   private EditText name, pass;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("mypref",Context.MODE_PRIVATE);

        name = (EditText) findViewById(R.id.prof);
        pass = (EditText) findViewById(R.id.pass);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setError(null);
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass.setError(null);
            }
        });
    }

    public void login(View view) {
        final String proff = name.getText().toString();
        final String password = pass.getText().toString();

        if (proff.isEmpty() && password.isEmpty()) {
            name.setError("Empty Field");
            pass.setError("Empty Field");

        } else if (proff.isEmpty()) {
            name.setError("Empty Field");


        } else if (password.isEmpty()) {

            pass.setError("Empty Field");

        }else if (proff.equalsIgnoreCase("admin")&&password.equalsIgnoreCase("password")){


        }
    }
}
