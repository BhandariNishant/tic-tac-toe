package androidsamples.java.tictactoe;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private ArrayList<GameRecord> mGames;
    private String firstPlayerId;
    private String secondPlayerId;
    private static String TAG = "adapter";
    private Set<String> set;

    public OpenGamesAdapter(ArrayList<GameRecord> mGames, Context context) {
        this.mGames = mGames;
        set = new HashSet<>();
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if(mGames!=null){
            GameRecord current = mGames.get(position);
            firstPlayerId = current.getFirstPlayerId();
            secondPlayerId = current.getSecondPlayerId();
            if(!Objects.equals(firstPlayerId, "") && !Objects.equals(secondPlayerId, ""))
                holder.itemView.setVisibility(View.GONE);

            else {
                    holder.itemView.setVisibility(View.VISIBLE);
            }



            holder.mIdView.setText(String.valueOf(current.getid()));
            //holder.mContentView.setText(current.getFirstPlayerId());
        }

    }

    @Override
    public int getItemCount() {
        return (mGames == null) ? 0 : mGames.size();
    }

    public void add_game(GameRecord games) {
        mGames.add(games);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        //public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            //mContentView = view.findViewById(R.id.content);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(v.getContext(), "Click Registered, going to double", Toast.LENGTH_SHORT).show();
                    Log.d("hello", "Click Registered, going to double");
                    onClickingGame(mIdView.getText().toString());
//          NavController mNavController = Navigation.findNavController(v);
//          NavDirections action = DashboardFragmentDirections.actionGameDouble(mIdView.getText().toString());
//          mNavController.navigate(action);
                }
            });
        }

    }

    public void onClickingGame(String gameId){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, "trying to load game record" + gameId);
        mDatabase.child("Game").child(gameId).get()
                .addOnSuccessListener(dataSnapshot -> {
                    Log.d(TAG, "In load3");
                    GameRecord gameRecord = dataSnapshot.getValue(GameRecord.class);
                    if (gameRecord != null) {
                        // Store profile data locally or pass to game
                        Log.d(TAG, "Retrieved3");

                        Log.d(TAG, "first id " + gameRecord.getFirstPlayerId());
                        Log.d(TAG, "cu id " + currentUser.getUid());

                        String firstPlayerAddress = gameRecord.getFirstPlayerId();
                        String secondPlayerAddress = gameRecord.getSecondPlayerId();

                        if(Objects.equals(firstPlayerAddress, currentUser.getUid()))
                        {
                            Log.d("Firebase", "same user");
    //                        NavController mNavController = Navigation.findNavController(v);
    //                        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
    //                        mNavController.navigate(action);
                            DashboardFragment.clickGameFirstUser(gameId);
                        }
                        else if (!Objects.equals(secondPlayerAddress, "")) {
                            Log.d("Firebase", "game already filled");
                            Toast.makeText(mInflater.getContext(), "game already filled", Toast.LENGTH_SHORT).show();
                        }
                        else if(Objects.equals(secondPlayerAddress, "") && !Objects.equals(firstPlayerAddress, currentUser.getUid())){
    //                        NavController mNavController = Navigation.findNavController(v);
    //                        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
    //                        mNavController.navigate(action);
    //                        secondPlayerAddress = cu.getUid();
    //                        game.setSecondPlayerId(secondPlayerAddress);
    //                        saveGameRecord(id, game);
                            DashboardFragment.clickGameSecondUser(gameId, gameRecord);
                        }

                    } else {
                        Log.d(TAG, "Failure");
//                        gameRecord = new GameRecord();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error loading gameRecord", e);
//                    gameRecord = new GameRecord();
                });
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    GameRecord game = snapshot.getValue(GameRecord.class);
//                    String firstPlayerAddress = game.getFirstPlayerId();
//                    String secondPlayerAddress = game.getSecondPlayerId();
//                    Log.d("Firebase", "Gameid: " + id);
//
//                    Log.d("Firebase", "First Player Address: " + firstPlayerAddress);
//                    Log.d("Firebase", "Second Player Address:" + secondPlayerAddress);
//                    if(Objects.equals(firstPlayerAddress, cu.getUid()))
//                    {
//                        Log.d("Firebase", "same user");
////                        NavController mNavController = Navigation.findNavController(v);
////                        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
////                        mNavController.navigate(action);
//                        DashboardFragment.clickGameFirstUser(id);
//                    }
//                    else if (!Objects.equals(secondPlayerAddress, "")) {
//                        Log.d("Firebase", "game already filled");
//                    }
//                    else if(Objects.equals(secondPlayerAddress, "") && !Objects.equals(firstPlayerAddress, cu.getUid())){
////                        NavController mNavController = Navigation.findNavController(v);
////                        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
////                        mNavController.navigate(action);
////                        secondPlayerAddress = cu.getUid();
////                        game.setSecondPlayerId(secondPlayerAddress);
////                        saveGameRecord(id, game);
//                        DashboardFragment.clickGameSecondUser(id, game);
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Firebase", "Error retrieving data", error.toException());
//            }
//        });
    }

//    public void saveGameRecord(String gameId, GameRecord game) {
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("Game").child(gameId).setValue(game)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d("adapter", "GameRecord saved successfully");
//                })
//                .addOnFailureListener(e -> {
////              Log.w(TAG, "Error saving gamerecord", e);
////                    Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
//                });
//    }
}