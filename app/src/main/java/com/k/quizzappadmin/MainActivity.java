package com.k.quizzappadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
  private static final  String[] konular = {"Hayvanlar 1","Hayvanlar 2","Meyveler","Meslekler","Spor","Sayılar","Ülkeler","Bitkiler"
                        ,"Mevsimler ve Aylar","Organlar","Ekonomi Terimleri","Askeri Terimler","Bilim", "Sanat","Teknoloji"};
    private Spinner spinner;
    private ArrayAdapter<String> dataAdapterForKonular;
    private EditText editSoru, editsec1,editsec2,editsec3,editsec4,editdogru,editsetNo, kategoriSets, kategoriUrl;
    private Button ekleButton, kategoriButton;
    private String databaseKonusu;
    private String setno;
    private Boolean durum=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //spinner
        spinner=(Spinner)findViewById(R.id.spinner);

        editSoru=findViewById(R.id.edit1);
        editsec1=findViewById(R.id.edit2);
        editsec2=findViewById(R.id.edit3);
        editsec3=findViewById(R.id.edit4);
        editsec4=findViewById(R.id.edit5);
        editdogru=findViewById(R.id.edit6);
        editsetNo=findViewById(R.id.edit7);
        kategoriButton=findViewById(R.id.kategoriButon);
        kategoriSets=findViewById(R.id.kategoriSets);
        kategoriUrl=findViewById(R.id.kategoriUrl);


        ekleButton=findViewById(R.id.pushButton);

        dataAdapterForKonular= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, konular);

        dataAdapterForKonular.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapterForKonular);

        final SoruModel soruModel=new SoruModel();
        final KategoriModel kategoriModel = new KategoriModel();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            databaseKonusu=adapterView.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ekleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                durum = boskontrol(editSoru,editsec1,editsec2,editsec3,editsec4,editdogru,editsetNo);
                if(databaseKonusu!=null){
                    myRef=database.getReference().child("Sets").child(databaseKonusu).child("Sorular");
                } else {
                    Toast.makeText(getApplicationContext(),"Spinner null!",Toast.LENGTH_SHORT).show();
                }
                if (durum){
                    Toast.makeText(getApplicationContext(),"Bütün boşluklari doldur!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"Başarılı!",Toast.LENGTH_SHORT).show();
                    soruModel.setSoru(editSoru.getText().toString());
                    soruModel.setSeca(editsec1.getText().toString());
                    soruModel.setSecb(editsec2.getText().toString());
                    soruModel.setSecc(editsec3.getText().toString());
                    soruModel.setSecd(editsec4.getText().toString());
                    soruModel.setCevap(editdogru.getText().toString());
                    setno = editsetNo.getText().toString();
                    soruModel.setSetNo(Integer.valueOf(setno));
                    myRef.push().setValue(soruModel);
                }
            }
        });

        kategoriButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no;
                if(databaseKonusu!=null){
                    myRef=database.getReference().child("Kategoriler");
                    no = kategoriSets.getText().toString();
                    kategoriModel.setBaslik(databaseKonusu);
                    kategoriModel.setSets(Integer.valueOf(no));
                    kategoriModel.setUrl(kategoriUrl.getText().toString());
                    myRef.push().setValue(kategoriModel);
                } else {
                    Toast.makeText(getApplicationContext(),"Spinner null!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean boskontrol(EditText editSoru, EditText editsec1, EditText editsec2, EditText editsec3, EditText editsec4, EditText editdogru, EditText editsetNo) {
        if(editSoru.getText().toString().trim().length()== 0 || editsec1.getText().toString().trim().length()== 0 || editsec2.getText().toString().trim().length()== 0 || editsec3.getText().toString().trim().length()== 0 || editsec4.getText().toString().trim().length()== 0 || editdogru.getText().toString().trim().length()== 0 || editsetNo.getText().toString().trim().length() == 0 ){
            return true;
        } else return false;

    }
}
