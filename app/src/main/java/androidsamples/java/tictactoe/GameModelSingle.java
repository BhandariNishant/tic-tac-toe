package androidsamples.java.tictactoe;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameModelSingle extends ViewModel {

    private static final String TAG = "GAmeModel";
    //0 means unfilled
    //1 means crosses(X)
    //2 means noughts(O)
    private int[] board = new int[9];
    private FirebaseUser currentUser;
    private UserRecord userRecord;
    private DatabaseReference mDatabase;


    //1 means first player turn
    //2 means second player turn or computer turn;
    private int turn;

    public GameModelSingle(){
        for(int i = 0; i < 9; i++){
            board[i] = 0;
        }
        turn = 1;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        loadUserRecords();
    }

    public int getTurn() {
        return turn;
    }
    public void setTurn(int i) {
            turn = i;
    }

    public void setValue(int pos, int val){
        board[pos] = val;
    }
    public int getValue(int pos){
        return board[pos];
    }

    public boolean isWinning(){
        //check columns
        for(int i = 0; i < 3; i++) {
            if (board[i] != 0 && board[i] == board[i + 3] && board[i] == board[i + 6]) {
                if(turn == 1)
                    userRecord.incrementSinglewin();
                else
                    userRecord.incrementSingleloss();
                saveUserRecord();
                return true;
            }
        }

        //check rows
        for(int i = 0; i < 3; i++) {
            if(board[i * 3] != 0 && board[i * 3] == board[i * 3 + 1] && board[i * 3] == board[i * 3 + 2]){
                if(turn == 1)
                    userRecord.incrementSinglewin();
                else
                    userRecord.incrementSingleloss();
                saveUserRecord();
                return true;
            }
        }

        //check diagonals
        if(board[0] != 0 && board[0] == board[4] && board[0] == board[8]){
            if(turn == 1)
                userRecord.incrementSinglewin();
            else
                userRecord.incrementSingleloss();
            saveUserRecord();
            return true;
        }

        if(board[2] != 0 && board[2] == board[4] && board[2] == board[6]){
            if(turn == 1)
                userRecord.incrementSinglewin();
            else
                userRecord.incrementSingleloss();
            saveUserRecord();
            return true;
        }

        return false;
    }

    public void ComputerMove(){
        int pos = -1;
        Random rnd = new Random();
        while(pos==-1)
        {
           pos = rnd.nextInt(9);
           if(board[pos]==0)
               break;
           else
               pos = -1;
        }
       setValue(pos,2);
    }

    public boolean isGridComplete() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                return false;
            }
        }
        userRecord.incrementSingledraw();
        saveUserRecord();
        return true;
    }

    private void loadUserRecords() {
        Log.d(TAG, "In load");
        mDatabase.child("userRecords").child(currentUser.getUid()).get()
                .addOnSuccessListener(dataSnapshot -> {
                    Log.d(TAG, "In load2");
                    UserRecord profile = dataSnapshot.getValue(UserRecord.class);
                    if (profile != null) {
                        // Store profile data locally or pass to game
                        Log.d(TAG, "Retrieved");
                        userRecord = profile;
                    } else {
                        Log.d(TAG, "Creating");
                        userRecord = new UserRecord();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error loading profile", e);
                    userRecord = new UserRecord();
//                    Toast.makeText(getActivity(), "Error loading profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveUserRecord() {
        Log.d(TAG, "In save");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        Log.d(TAG, currentUser.toString());
        mDatabase.child("userRecords").child(currentUser.getUid()).setValue(userRecord)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Profile saved successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error saving profile", e);
//                    Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
                });
    }

    public void backPressed() {
        userRecord.incrementSingleloss();
        saveUserRecord();
    }

}
