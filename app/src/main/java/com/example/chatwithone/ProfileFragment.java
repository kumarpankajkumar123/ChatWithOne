package com.example.chatwithone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatwithone.Modal.UserModal;
import com.example.chatwithone.Utrils.FirebaseUtils;
import com.example.chatwithone.Utrils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    UserModal currentUserModal;
    ImageView profile_image;
    TextView profile_phoneNo, profile_userName, profile_logout;
    Button profile_update_button;
    ProgressBar profile_progressbar;
    ActivityResultLauncher<Intent> imagePickerLauncher;
    Uri selectedImageUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUrl = data.getData();
                            Utils.setImageProfile(getContext(),profile_image,selectedImageUrl);
                        }
                    }
                }
        );
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_image = (ImageView) view.findViewById(R.id.profile_image);
        profile_userName = (TextView) view.findViewById(R.id.profile_userName);
        profile_phoneNo = (TextView) view.findViewById(R.id.profile_phoneNo);
        profile_logout = (TextView) view.findViewById(R.id.profile_logout);
        profile_update_button = (Button) view.findViewById(R.id.profile_update_button);
        profile_progressbar = (ProgressBar) view.findViewById(R.id.profile_progressbar);

        getUserDetails();

        profile_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showToast(getContext(), "you moved to Splash Screen");
                FirebaseUtils.loggedOut();
                Intent intent = new Intent(getContext(), Splash_Screen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        profile_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToButton();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(getActivity()).compress(500).cropSquare()
                        .maxResultSize(512,512).createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickerLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });
        return view;
    }

    void updateToButton() {
        String newUsername = profile_userName.getText().toString();
        if (newUsername.isEmpty() || newUsername.length() <= 3) {
            profile_userName.setError("the username should be smaller than 3 characters");
        } else {
            currentUserModal.setUsername(newUsername);
            setProgress(true);

            if(selectedImageUrl != null){
                FirebaseUtils.getCurrentProfileStorageRef().putFile(selectedImageUrl)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                updateToFirebase();
                            }
                        });
            }
            else{
                updateToFirebase();
            }
        }

    }

    void updateToFirebase() {
        FirebaseUtils.currentUserDetails().set(currentUserModal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setProgress(false);
                if (task.isSuccessful()) {
                    Utils.showToast(getContext(), "the useName is set Successfully");
                } else {
                    Utils.showToast(getContext(), "upDate failed");
                }
            }
        });
    }

    void getUserDetails() {
        setProgress(true);

        FirebaseUtils.getCurrentProfileStorageRef().getDownloadUrl()
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    Uri uri = task.getResult();
                                    Utils.setImageProfile(getContext(),profile_image,uri);
                                }
                            }
                        });

        FirebaseUtils.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setProgress(false);
                currentUserModal = task.getResult().toObject(UserModal.class);
                profile_userName.setText(currentUserModal.getUsername());
                profile_phoneNo.setText(currentUserModal.getPhone());
            }
        });
    }

    void setProgress(boolean inProgress) {
        if (inProgress) {
            profile_progressbar.setVisibility(View.VISIBLE);
            profile_update_button.setVisibility(View.GONE);
        } else {
            profile_progressbar.setVisibility(View.GONE);
            profile_update_button.setVisibility(View.VISIBLE);
        }
    }
}