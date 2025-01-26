package androidsamples.java.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null && getView() != null){
            NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
            Navigation.findNavController(requireView()).navigate(action);
        } else if (currentUser != null) {
            // Alternative navigation method if view is not available
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
            navController.navigate(LoginFragmentDirections.actionLoginSuccessful());
        }
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(getContext(), "Registered", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                            Navigation.findNavController(view).navigate(action);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Logged in");
                            Toast.makeText(getContext(), "Logged in", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            NavDirections action = LoginFragmentDirections.actionLoginSuccessful();
                            Navigation.findNavController(view).navigate(action);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "Login failed, trying to register", task.getException());
                            Toast.makeText(getContext(), "Trying to register",
                                    Toast.LENGTH_SHORT).show();
                            createAccount(email, password);
                        }
                    }
                });
        // [END sign_in_with_email]
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.btn_log_in)
                .setOnClickListener(v -> {
                    EditText et_email = view.findViewById(R.id.edit_email);
                    EditText et_password = view.findViewById(R.id.edit_password);

                    if(et_email.getText().toString().isEmpty() || et_password.getText().toString().isEmpty())
                    {
                        Toast.makeText(getContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(et_password.getText().toString().length()<6)
                    {
                        Toast.makeText(getContext(), "Password must be larger that 6 characters", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    signIn(et_email.getText().toString(), et_password.getText().toString());
                    });

        return view;
    }
}