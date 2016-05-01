package in.deepaksood.pcsmaproject.mailpackage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import in.deepaksood.pcsmaproject.R;
import in.deepaksood.pcsmaproject.bookaddpackage.AddBook;
import in.deepaksood.pcsmaproject.mainactivitypackage.MainActivity;

public class AddBookForRent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_for_rent);

        final Button button = (Button) findViewById(R.id.btn_yes_add_rent_library);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(AddBookForRent.this, MainActivity.class);
                AddBookForRent.this.startActivity(intent);
                finish();
            }
        });

        final Button button1 = (Button) findViewById(R.id.btn_exit_add_rent_library);
        assert button1 != null;
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                moveTaskToBack(true);
            }
        });

    }
}
