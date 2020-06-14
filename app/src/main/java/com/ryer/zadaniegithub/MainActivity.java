package com.ryer.zadaniegithub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.ryer.zadaniegithub.api.Client;
import com.ryer.zadaniegithub.api.Repo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity{
    public static final String PREFS_NAME ="Prefs";
    public static final String PREFS_SEARCH_HISTORY = "SearchHistory";
    private AutoCompleteTextView editText;
    public static RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    private SharedPreferences settings;
    private Set<String> history;
    public static List<Repo> repoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText =findViewById(R.id.editTextGHValues);
        recyclerView = findViewById(R.id.recyclerView);
        settings = getSharedPreferences(PREFS_NAME,0);
        history = settings.getStringSet(PREFS_SEARCH_HISTORY,new HashSet<String>());

        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER))
                {
                    addSearchInput(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void checkGitHub(View view) {
        Client client = new Client();
        String[] stringFromText = editText.getText().toString().split("/");
        if (stringFromText.length == 2) {
            addSearchInput(editText.getText().toString());
            client.start(stringFromText[0], stringFromText[1],this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(this, "zle dane", Toast.LENGTH_SHORT).show();
        }

    }

    private void setAutoCompleteSource()
    {
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.editTextGHValues);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, history.toArray(new String[history.size()]));
        textView.setAdapter(adapter);
    }
    private void addSearchInput(String input)
    {
        if (!history.contains(input))
        {
            history.add(input);
            setAutoCompleteSource();
        }
    }
    private void savePrefs()
    {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(PREFS_SEARCH_HISTORY, history);

        editor.commit();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        savePrefs();
    }

    public void sendValues(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String title = getResources().getString(R.string.title);
        String message = "";
        for (Repo rep:repoList) {
            if (rep.isActive())
            {
                message += rep.getSha()+" "+rep.getCommit().getMessage()+" "+rep.getCommit().getAuthor().getName()+"\r\n";
            }

        }
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent,title);

        startActivity(chooser);
    }
}
