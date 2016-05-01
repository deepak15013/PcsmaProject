package in.deepaksood.pcsmaproject.mailpackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.deepaksood.pcsmaproject.R;

public class RentBook extends AppCompatActivity {

    TextView userEmailId;
    TextView userBookIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lend_book);

        userEmailId = (TextView) findViewById(R.id.tv_renter_emailId);
        userBookIsbn = (TextView) findViewById(R.id.tv_renter_book_isbn);

        Intent intent = getIntent();
        String emailId = intent.getStringExtra("EMAILID");
        String isbn = intent.getStringExtra("ISBN");

        userEmailId.setText(emailId);
        userBookIsbn.setText(isbn);

        final Button button = (Button) findViewById(R.id.btn_yes_give);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(RentBook.this, "yes", Toast.LENGTH_SHORT).show();


            }
        });

        final Button button1 = (Button) findViewById(R.id.btn_exit_give);
        assert button1 != null;
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);

            }
        });

    }
}
