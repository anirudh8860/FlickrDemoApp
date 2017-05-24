package simplegamer003.flickrdemoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText email,passwd;
    Button login;
    String mailid, pass;
    String flickrlogin = "http://api.flickr.com/services/rest/?method=flickr.test.login&api_key=%ee3b2f1b3dc6dee8786f9e3d481315c9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.email);
        passwd = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailid = email.getText().toString();
                pass = passwd.getText().toString();
            }
        });
    }

}
