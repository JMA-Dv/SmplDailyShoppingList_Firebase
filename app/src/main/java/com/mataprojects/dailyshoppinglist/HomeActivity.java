package com.mataprojects.dailyshoppinglist;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mataprojects.dailyshoppinglist.Model.databaseData;

import java.text.DateFormat;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

    private TextView totalAmountText;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;

    private String type;
    private int amount;
    private String note;
    private String post_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Toolbar");

        totalAmountText= findViewById(R.id.total_amount_home);
        firebaseAuth =FirebaseAuth.getInstance();


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();


        System.out.println("Usuario actual "+ firebaseAuth.getCurrentUser());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Daily Shopping List").child(currentUserId);
        databaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView_home);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //total sum number
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalAmmount =0;
                for (DataSnapshot snap: dataSnapshot.getChildren()){
                    databaseData databaseData = snap.getValue(databaseData.class);
                    totalAmmount+= databaseData.getAmount();
                    String sTotoal = String.valueOf(totalAmmount);

                    totalAmountText.setText(sTotoal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        floatingActionButton = findViewById(R.id.floatingButton_home);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog();
            }
        });
    }
    public void customDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View myView = inflater.inflate(R.layout.input_data,null);

        final AlertDialog dialog = dialogBuilder.create();
        dialog.setView(myView);

        final EditText type = myView.findViewById(R.id.typeText);
        final EditText amount = myView.findViewById(R.id.amountText_input);
        final EditText note = myView.findViewById(R.id.noteText_input);
        Button saveButton = myView.findViewById(R.id.saveButton_input);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mType = type.getText().toString().trim();
                String mAmount = amount.getText().toString().trim();
                String mNote = note.getText().toString().trim();

                int ammount = Integer.parseInt(mAmount);

                if (TextUtils.isEmpty(mType)){
                    type.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mAmount)){
                    amount.setError("Required Field...");
                    return;
                }
                if (TextUtils.isEmpty(mNote)){
                    note.setError("Required Field...");
                    return;
                }

                String id = databaseReference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());

                databaseData databaseD = new databaseData(mType,mNote,date,id,ammount);

                databaseReference.child(id).setValue(databaseD);
                Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_SHORT).show();

                dialog.dismiss();

                //teresita-avila123@hotmail.com

            }
        });



        dialog.show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<databaseData,MyViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<databaseData,MyViewHolder>
                (
                databaseData.class,
                R.layout.item_data,
                MyViewHolder.class,
                databaseReference
                ){

            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final databaseData model, final int position) {
                viewHolder.setDate(model.getDate());
                viewHolder.setType(model.getType());
                viewHolder.setAmmount(model.getAmount());
                viewHolder.setNote(model.getNote());

                viewHolder.myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        post_key = getRef(position).getKey();
                        type= model.getType();
                        amount = model.getAmount();
                        note= model.getNote();
                        System.out.println("entro al hacer click");
                        updateData();

                    }
                });
            }
        };
        recyclerView.setAdapter(recyclerAdapter);

    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View myView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }
        public void setType(String type){
            TextView mType=myView.findViewById(R.id.typeText_item);
            mType.setText(type);
        }
        public void setNote(String note){
            TextView mNote=myView.findViewById(R.id.noteText_item);
            mNote.setText(note);
        }
        public void setDate(String date){
            TextView mDate=myView.findViewById(R.id.dateText_item);
            mDate.setText(date);
        }
        public void setAmmount(int ammount){
            TextView mAmount=myView.findViewById(R.id.amountText_item);
            String stam = String.valueOf(ammount);
            mAmount.setText(stam);
        }
    }

    public void updateData() {
        System.out.println("Entro a UpdateData");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomeActivity.this);

        LayoutInflater inflater = LayoutInflater.from(this);

        View myView = inflater.inflate(R.layout.update_inputfield, null);

        final AlertDialog dialog = alertBuilder.create();
        dialog.setView(myView);
        dialog.show();

        final EditText typeText = myView.findViewById(R.id.typeText_update);
        final EditText ammountText = myView.findViewById(R.id.amountText_update);
        final EditText noteText = myView.findViewById(R.id.noteText_update);


        typeText.setText(type);
        typeText.setSelection(type.length());

        ammountText.setText(String.valueOf(amount));
        ammountText.setSelection(String.valueOf(amount).length());

        noteText.setText(note);
        noteText.setSelection(note.length());

        Button updateButton = myView.findViewById(R.id.updateButton_update);
        Button deleteButton = myView.findViewById(R.id.deleteButton_update);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = typeText.getText().toString().trim();

                String mAmmount = String.valueOf(amount);
                mAmmount= ammountText.getText().toString().trim();

                note = noteText.getText().toString().trim();

                int intammount = Integer.parseInt(mAmmount);

                String date = DateFormat.getDateInstance().format(new Date());

                databaseData dataB= new databaseData(type,note,date,post_key,intammount);
                databaseReference.child(post_key).setValue(dataB);

                dialog.dismiss();


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(post_key).removeValue();
                dialog.dismiss();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.log_out:
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
