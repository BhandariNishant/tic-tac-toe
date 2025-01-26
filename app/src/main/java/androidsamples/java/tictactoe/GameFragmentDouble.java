package androidsamples.java.tictactoe;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GameFragmentDouble extends Fragment {
    private static final String TAG = "GameFragmentDouble";
    private static final int GRID_SIZE = 9;
    private String gameId;
    private GameModelDouble vm;
    private DatabaseReference mDatabase;
    private TextView turntext;

    private final Button[] mButtons = new Button[GRID_SIZE];
    private NavController mNavController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vm = new ViewModelProvider(this).get(GameModelDouble.class);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setHasOptionsMenu(true); // Needed to display the action menu for this fragment

        // Extract the argument passed with the action in a type-safe way
        GameFragmentDoubleArgs args = GameFragmentDoubleArgs.fromBundle(getArguments());
        gameId = args.getGameId();
        Log.d(TAG, "GameId" + gameId);
        vm.setGameID(gameId);


        // Handle the back press by adding a confirmation dialog
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "Back pressed");


                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setTitle(R.string.confirm)
                        .setMessage(R.string.forfeit_game_dialog_message)
                        .setPositiveButton(R.string.yes, (d, which) -> {

//                            vm.incrementcompleted();
                            vm.backPressed();

                            mNavController.popBackStack();
                        })
                        .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                        .create();
                dialog.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gamedouble, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = Navigation.findNavController(view);

        mButtons[0] = view.findViewById(R.id.button0);
        mButtons[1] = view.findViewById(R.id.button1);
        mButtons[2] = view.findViewById(R.id.button2);

        mButtons[3] = view.findViewById(R.id.button3);
        mButtons[4] = view.findViewById(R.id.button4);
        mButtons[5] = view.findViewById(R.id.button5);

        mButtons[6] = view.findViewById(R.id.button6);
        mButtons[7] = view.findViewById(R.id.button7);
        mButtons[8] = view.findViewById(R.id.button8);

        turntext = view.findViewById(R.id.textView15);

        vm.getBoardLiveData().observe(getViewLifecycleOwner(), this::updateBoardUI);
        vm.getTurnLiveData().observe(getViewLifecycleOwner(), this::updateTurnUI);

//        updateUI();

        for (int i = 0; i < GRID_SIZE; i++) {
            int finalI = i;
            mButtons[i].setOnClickListener(v -> {
                doublePlayerLogic(finalI);
            });
        }
    }

    private void updateBoardUI(int[] board) {
        String test = "";
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i] == 1) {
                test+="1";
                mButtons[i].setText("X");
            } else if (board[i] == 2) {
                mButtons[i].setText("O");
                test+="2";
            }
            else {
                test += "0";
                mButtons[i].setText("");
            }
        }
        Log.d("RESULT", test);
        if(vm.getTurn() == vm.getMyTurn())
            displayResults();
    }

    private void updateTurnUI(int turn) {
        if (turn == vm.getMyTurn()) {
            turntext.setText("Your turn");
        } else {
            turntext.setText("Opponent's turn");
        }

//        displayResults();
    }

    private void doublePlayerLogic(int finalI) {
        Log.d(TAG, "HEY" + vm.getTurn() + " " + vm.getMyTurn() + " " + vm.getValue(finalI));
        if(vm.getTurn() == vm.getMyTurn() && vm.getValue(finalI) == 0){
            Log.d(TAG, "HEY2");
            vm.setValue(finalI);
            vm.updateGameRecord();
            vm.saveGameRecord();
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logout, menu);
        // this action menu is handled in MainActivity
    }

//    public void updateUI() {
//        for (int i = 0; i < GRID_SIZE; i++) {
//            if(vm.getValue(i) == 1)
//                mButtons[i].setText("X");
//            else if(vm.getValue(i) == 2)
//                mButtons[i].setText("O");
//        }
//
//        if(vm.getTurn() == vm.getMyTurn())
//            turntext.setText("Your turn");
//        else
//            turntext.setText("Opponent's turn");
//    }

    public boolean displayResults() {
        //check if win
        int result = vm.isResult();
        if (result == 1) {
            //if first player turn
            AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                    .setMessage("Congratulations")
                    .setPositiveButton("Okay", (d, which) -> {
                        // TODO update win count
//                        vm.incrementcompleted();
                        Log.d(TAG, "Won");
                        mNavController.popBackStack();

                    }).create();
                dialog.show();
                return true;
        }
            //if second player turn
        else if (result == 2)
        {
                AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                        .setMessage("You Lose")
                        .setPositiveButton("Okay", (d, which) -> {
                            // TODO update lose count
//                            vm.incrementcompleted();
                            Log.d(TAG, "Lost");
                            mNavController.popBackStack();
                        }).create();
                dialog.show();
                return true;
        }

        else if (result == 3){
            AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                    .setMessage("It's a draw!!")
                    .setPositiveButton("Okay", (d, which) -> {
                        // TODO update draw count
//                        vm.incrementcompleted();
                        mNavController.popBackStack();
                    }).create();
            dialog.show();
            return true;
        }

        else if(result==4) {
            AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                    .setMessage("Opponent forfeited")
                    .setPositiveButton("Okay", (d, which) -> {
                        // TODO update draw count
//                        vm.incrementcompleted();
                        mNavController.popBackStack();
                    }).create();
            dialog.show();
            return true;
        }
        return false;
    }
}