package androidsamples.java.tictactoe;

import android.app.GameState;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;


public class GameModelDouble extends ViewModel {

    private static final String TAG = "GameModelDouble";
    //0 means unfilled
    //1 means crosses(X)
    //2 means noughts(O)
    private int[] board = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private FirebaseUser currentUser;
    private UserRecord userRecord;
    private DatabaseReference mDatabase;
    private GameRecord gameRecord;
    private String gameId;

    //1 means first player turn
    //2 means second player turn;
    private int myturn;

    private final MutableLiveData<int[]> boardLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> turnLiveData = new MutableLiveData<>();

    public GameModelDouble(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        loadUserRecords();
        gameRecord = new GameRecord();
        userRecord = new UserRecord();
        gameId = "";
        myturn = -1;
//        boardLiveData.setValue(board);
//        turnLiveData.setValue(gameRecord.getTurn());

        mDatabase.child("Game").child(gameId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "In onChildChanged");
                gameRecord = dataSnapshot.getValue(GameRecord.class);

                if(Objects.equals(gameRecord.getFirstPlayerId(), currentUser.getUid())) {
                    myturn = 1;
                }
                else
                    myturn = 2;

                String state = gameRecord.getGameState();
                for(int i=0;i<9;i++)
                {
                    board[i] = state.charAt(i) - '0';
                }

//                GameFragmentDouble.updateUI();
                turnLiveData.setValue(gameRecord.getTurn());
                boardLiveData.setValue(board);
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "In onChildAddeded");
                gameRecord = dataSnapshot.getValue(GameRecord.class);

                if(Objects.equals(gameRecord.getFirstPlayerId(), currentUser.getUid())) {
                    myturn = 1;
                }
                else
                    myturn = 2;

                String state = gameRecord.getGameState();
                for(int i=0;i<9;i++)
                {
                    board[i] = state.charAt(i) - '0';
                }

                turnLiveData.setValue(gameRecord.getTurn());
//                boardLiveData.setValue(board);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public LiveData<int[]> getBoardLiveData() {
        return boardLiveData;
    }

    public LiveData<Integer> getTurnLiveData() {
        return turnLiveData;
    }

    public void setGameID(String gameId) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "gameid: " + gameId);
        this.gameId = gameId;
//        loadGameRecord();
        loadUserRecords();

//        for(int i = 0; i < 9; i++){
//            board[i] = 0;
//        }


    }

//    public void loadGameRecord() {
//        Log.d(TAG, "tryiong to load game record" + gameId);
//        mDatabase.child("Game").child(gameId).get()
//                .addOnSuccessListener(dataSnapshot -> {
//                    Log.d(TAG, "In load3");
//                    GameRecord profile = dataSnapshot.getValue(GameRecord.class);
//                    if (profile != null) {
//                        // Store profile data locally or pass to game
//                        Log.d(TAG, "Retrieved3");
//                        gameRecord = profile;
//                        Log.d(TAG, "firstid " + gameRecord.getFirstPlayerId());
//                        Log.d(TAG, "cuid " + currentUser.getUid());
//                        String state = gameRecord.getGameState();
//                        for(int i=0;i<9;i++)
//                        {
//                            board[i] = state.charAt(i) - '0';
//                        }
//                        Log.d(TAG, board.toString());
//                        if(Objects.equals(gameRecord.getFirstPlayerId(), currentUser.getUid())) {
//                            myturn = 1;
//                        }
//                        else
//                            myturn = 2;
//
//                    } else {
//                        Log.d(TAG, "Failure");
////                        gameRecord = new GameRecord();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.w(TAG, "Error loading gameRecord", e);
////                    gameRecord = new GameRecord();
//                });
//    }

    public int getTurn() {
        return gameRecord.getTurn();
    }
    public int getMyTurn() {
        return myturn;
    }

    public void setValue(int pos){
        board[pos] = myturn;
        boardLiveData.setValue(board);
    }
    public int getValue(int pos){
        return board[pos];
    }

    public int isResult(){
//        1->mywin
//        2->myloss
//        3->draw
        //check columns
        int forfeitcount=0;
        for(int i=0;i<9;i++)
        {
            if(board[i]==myturn)
                forfeitcount++;
        }
        if(forfeitcount == 9)
        {
            userRecord.incrementDoublewin();
            saveUserRecord();

            return 4;
        }
        for(int i = 0; i < 3; i++) {
            if (board[i] != 0 && board[i] == board[i + 3] && board[i] == board[i + 6]) {
                if(board[i] == myturn) {
                    userRecord.incrementDoublewin();
                    saveUserRecord();
                    Log.d("RESULTS", "hey1");
                    return 1;
                }
                else {
                    userRecord.incrementDoubleloss();
                    saveUserRecord();
                    Log.d("RESULTS", "hey2");

                    return 2;
                }
            }
        }

        //check rows
        for(int i = 0; i < 3; i++) {
            if(board[i * 3] != 0 && board[i * 3] == board[i * 3 + 1] && board[i * 3] == board[i * 3 + 2]){
                if(board[i] == myturn) {
                    userRecord.incrementDoublewin();
                    saveUserRecord();
                    Log.d("RESULTS", "hey3");

                    return 1;
                }
                else {
                    userRecord.incrementDoubleloss();
                    saveUserRecord();
                    Log.d("RESULTS", "hey4");
                    return 2;
                }
            }
        }

        //check diagonals
        if(board[0] != 0 && board[0] == board[4] && board[0] == board[8]){
            if(board[0] == myturn) {
                userRecord.incrementDoublewin();
                saveUserRecord();
                Log.d("RESULTS", "hey5");

                return 1;
            }
            else {
                userRecord.incrementDoubleloss();
                saveUserRecord();
                Log.d("RESULTS", "hey6");

                return 2;
            }
        }

        if(board[2] != 0 && board[2] == board[4] && board[2] == board[6]){
            if(board[2] == myturn) {
                userRecord.incrementDoublewin();
                saveUserRecord();
                Log.d("RESULTS", "hey7");

                return 1;
            }
            else {
                userRecord.incrementDoubleloss();
                saveUserRecord();
                Log.d("RESULTS", "hey8");

                return 2;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) {
                Log.d("RESULTS", "hey9");
                return 0;
            }
        }

        userRecord.incrementDoubledraw();
        saveUserRecord();
        Log.d("RESULTS", "hey10");

        return 3;
    }

//    public boolean isGridComplete() {
//        for (int i = 0; i < 9; i++) {
//            if (board[i] == 0) {
//                return false;
//            }
//        }
//        userRecord.incrementDoubledraw();
//        saveUserRecord();
//        return true;
//    }

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

        userRecord.incrementDoubleloss();
        String gamestate="";
        String fill="1";
        if(myturn==1)
            fill="2";
        for(int i=0;i<9;i++)
        {
            gamestate += fill;
        }
        gameRecord.setGameState(gamestate);
        if (myturn == 1)
            gameRecord.setTurn(2);
        else
            gameRecord.setTurn(1);

        turnLiveData.setValue(gameRecord.getTurn());
        saveUserRecord();
        saveGameRecord();
    }

    public void updateGameRecord() {
        Log.d(TAG, "In update");
        String gameState = "";

        for(int i = 0; i < 9; i++) {
            if (board[i] == 0)
                gameState += "0";
            else if (board[i] == 1)
                gameState += "1";
            else
                gameState += "2";
        }

        gameRecord.setGameState(gameState);

//        boardLiveData.setValue(board);

        if (myturn == 1)
            gameRecord.setTurn(2);
        else
            gameRecord.setTurn(1);

        turnLiveData.setValue(gameRecord.getTurn());
    }

    public void saveGameRecord() {
        mDatabase.child("Game").child(gameId).setValue(gameRecord)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "GameRecord saved successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error saving gamerecord", e);
//                    Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
                });
    }
}
