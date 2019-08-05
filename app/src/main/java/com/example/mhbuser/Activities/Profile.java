package com.example.mhbuser.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.mhbuser.Classes.CDeleteAccount;
import com.example.mhbuser.R;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_settings, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private Preference

                pUpdateProfile = null,
                pUpdatePassword = null,
                pDeleteAccount = null;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_preferences, rootKey);

            connectivity();


            pUpdateProfile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    //Toast.makeText(getActivity(), "Update Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), UpdateProfile.class));
                    return false;
                }
            });
            pUpdatePassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), UpdatePassword.class));
                    Toast.makeText(getActivity(), "Update password", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            pDeleteAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Are you sure to delete entire hall?\nThis will delete all sub halls too.");
                    builder.setTitle("Please Confirm!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CDeleteAccount(getActivity());
                            //Toast.makeText(getActivity(), "Delete Account", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog1 = builder.create();
                    dialog1.show();
                    return false;
                }
            });


        }

        private void connectivity() {

            pUpdateProfile = (Preference) findPreference("p_update_profile");
            pUpdatePassword = (Preference) findPreference("p_update_password");
            pDeleteAccount = (Preference) findPreference("p_delete_account");
        }
    }
}