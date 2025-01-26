package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private static NavController mNavController;
    private  DatabaseReference mDatabase;
    private  FirebaseUser currentUser;
    private UserRecord userRecord;
    private ArrayList<GameRecord> itemList;
    private OpenGamesAdapter adapter;
    private ChildEventListener childEventListener;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setHasOptionsMenu(true); // Needed to display the action menu for this fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //if a user is not logged in, go to LoginFragment
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            NavDirections action = DashboardFragmentDirections.actionNeedAuth();
            mNavController.navigate(action);
            return;
        }

        loadUserRecords();

        //Recycler view code
        RecyclerView recyclerView = view.findViewById(R.id.list);
        itemList = new ArrayList<>();
        adapter = new OpenGamesAdapter(itemList,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupFirebaseListener();


        // Show a dialog when the user clicks the "new game" button
        view.findViewById(R.id.fab_new_game).setOnClickListener(v -> {

            // A listener for the positive and negative buttons of the dialog
            DialogInterface.OnClickListener listener = (dialog, which) -> {
                String gameType = "No type";
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    gameType = getString(R.string.two_player);
                    //Add a new open game
                    addOpenGame();
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    gameType = getString(R.string.one_player);
                    // Passing the game type as a parameter to the action
                    // extract it in GameFragment in a type safe way
                    NavDirections action = DashboardFragmentDirections.actionGameSingle();
                    mNavController.navigate(action);
                }
                Log.d(TAG, "New Game: " + gameType);


            };

            // create the dialog
            AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.new_game)
                    .setMessage(R.string.new_game_dialog_message)
                    .setPositiveButton(R.string.two_player, listener)
                    .setNegativeButton(R.string.one_player, listener)
                    .setNeutralButton(R.string.cancel, (d, which) -> d.dismiss())
                    .create();
            dialog.show();
        });
    }

    private void addOpenGame() {
        GameRecord newRecord = new GameRecord(currentUser.getUid());
        mDatabase.child("Game").push().setValue(newRecord);
    }

    public static void clickGameFirstUser(String id) {
//        NavController mNavController = Navigation.findNavController(getView());
        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
        mNavController.navigate(action);
    }

    public static void clickGameSecondUser(String id, GameRecord game) {
//        NavController mNavController = Navigation.findNavController(v);
        NavDirections action = DashboardFragmentDirections.actionGameDouble(id);
        mNavController.navigate(action);
        game.setSecondPlayerId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        saveGameRecord(id, game);
    }

    public static void saveGameRecord(String gameId, GameRecord game) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Game").child(gameId).setValue(game)
                .addOnSuccessListener(aVoid -> {
                    Log.d("adapter", "GameRecord saved successfully");
                })
                .addOnFailureListener(e -> {
//              Log.w(TAG, "Error saving gamerecord", e);
//                    Toast.makeText(getContext(), "Error saving profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupFirebaseListener() {
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "onChildAdded:" + snapshot.getKey());

                GameRecord newRecord = snapshot.getValue(GameRecord.class);
                Log.d(TAG, "GameRecord: " + newRecord);
                if(newRecord!=null || !itemList.contains(newRecord))
                {
                    newRecord.setid(snapshot.getKey());
                    mDatabase.child("Game").child(snapshot.getKey()).child("id").setValue(snapshot.getKey());
                    adapter.add_game(newRecord);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Child modified");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.child("Game").addChildEventListener(childEventListener);

    }




    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logout, menu);
        // this action menu is handled in MainActivity
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
                        updateUI();
                    } else {
                        Log.d(TAG, "Creating");
                        userRecord = new UserRecord();
                        updateUI();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error loading profile", e);
                    userRecord = new UserRecord();
//                    Toast.makeText(getActivity(), "Error loading profile", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUI () {
        View view = getView();
        ((TextView) view.findViewById(R.id.heading)).setText("Stats for " + currentUser.getEmail());
        ((TextView) view.findViewById(R.id.textView7)).setText(Integer.toString(userRecord.getSinglewin()));
        ((TextView) view.findViewById(R.id.textView9)).setText(Integer.toString(userRecord.getSingleloss()));
        ((TextView) view.findViewById(R.id.textView10)).setText(Integer.toString(userRecord.getSingledraw()));
        ((TextView) view.findViewById(R.id.textView8)).setText(Integer.toString(userRecord.getDoublewin()));
        ((TextView) view.findViewById(R.id.textView11)).setText(Integer.toString(userRecord.getDoubleloss()));
        ((TextView) view.findViewById(R.id.textView12)).setText(Integer.toString(userRecord.getDoubledraw()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the listener
        if (childEventListener != null) {
            mDatabase.child("Game").removeEventListener(childEventListener);
        }
    }

}