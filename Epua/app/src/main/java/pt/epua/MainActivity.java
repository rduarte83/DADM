package pt.epua;
//TODO menu sobre?
//TODO menu c opcoes : so mostar almocos, mudar tempo do timeout
//TODO mudar lista p recyclerview
//TODO Loading bar (progress bar)
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this.getApplicationContext();

        ImageButton bt1 = findViewById(R.id.bt1);
        ImageButton bt2 = findViewById(R.id.bt2);

        //Start activity after 5000ms
        final Handler mHandler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
            }
        };

        long timeout = 5000;
        mHandler.postDelayed(runnable, timeout);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(runnable);
                Intent it = new Intent(ctx, Cantinas.class);
                startActivity(it);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacks(runnable);
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}