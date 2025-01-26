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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;



import java.util.Objects;

public class GameFragmentSingle extends Fragment {
  private static final String TAG = "GameFragmentSingle";
  private static final int GRID_SIZE = 9;
  private GameModelSingle vm;

  private final Button[] mButtons = new Button[GRID_SIZE];
  private NavController mNavController;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    vm = new ViewModelProvider(this).get(GameModelSingle.class);

    setHasOptionsMenu(true); // Needed to display the action menu for this fragment

    // Extract the argument passed with the action in a type-safe way
    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");


        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.confirm)
                .setMessage(R.string.forfeit_game_dialog_message)
                .setPositiveButton(R.string.yes, (d, which) -> {
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
    return inflater.inflate(R.layout.fragment_gamesingle, container, false);
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

    updateUI();

    for (int i = 0; i < GRID_SIZE; i++) {
      int finalI = i;
      mButtons[i].setOnClickListener(v -> {
        Log.d(TAG, "Button " + finalI + " clicked");
        singlePlayerLogic(finalI);
      });
    }

  }


  private void singlePlayerLogic(int finalI) {
    if (vm.getTurn() == 1 && vm.getValue(finalI) == 0) {
      vm.setValue(finalI, 1);
      updateUI();
      if(!displayResults())
      {
        vm.setTurn(2);
        vm.ComputerMove();
        updateUI();
        displayResults();
        vm.setTurn(1);
      }
    }
  }


  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }

  public void updateUI() {
    for (int i = 0; i < GRID_SIZE; i++) {
      if(vm.getValue(i) == 1)
        mButtons[i].setText("X");
      else if(vm.getValue(i) == 2)
        mButtons[i].setText("O");
    }
  }

  public boolean displayResults() {
    //check if win
    if (vm.isWinning()) {
      //if first player turn
      if (vm.getTurn() == 1) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setMessage("Congratulations")
                .setPositiveButton("Okay", (d, which) -> {
                  Log.d(TAG, "Won");
                  mNavController.popBackStack();
                }).create();
        dialog.show();
        return true;
      }
      //if second player turn
      else if (vm.getTurn() == 2) {
        AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                .setMessage("You Lose")
                .setPositiveButton("Okay", (d, which) -> {
                  Log.d(TAG, "Lost");
                  mNavController.popBackStack();
                }).create();
        dialog.show();
        return true;
      }
    }

    if (vm.isGridComplete()) {
      AlertDialog dialog = new AlertDialog.Builder(requireActivity())
              .setMessage("It's a draw!!")
              .setPositiveButton("Okay", (d, which) -> {
                mNavController.popBackStack();
              }).create();
      dialog.show();
      return true;
    }
    return false;
  }

}