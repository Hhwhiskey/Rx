package com.khfire22gmail.rx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @BindView(R.id.actv_name)
    AutoCompleteTextView nameField;

    @OnClick(R.id.bttn_enter)
    public void validateUserNameOnClick() {
        subscribeToTakenUserNames();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    private void validateName(String userName, String[] users) {

        if (userName.length() == 0) {
            Toast.makeText(this, "Please enter a user name.", Toast.LENGTH_LONG).show();

        } else if (userName.length() < 6) {
            Toast.makeText(this, "User name must be at least 6 characters.", Toast.LENGTH_LONG).show();

        } else if (!userName.matches(".*\\d+.*")) {
            Toast.makeText(this, "User name must contain at least 1 number.", Toast.LENGTH_LONG).show();

        } else if (Arrays.asList(users).contains(userName)) {
            Toast.makeText(this, "User name is already taken.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "User name is valid. Your account has been created.", Toast.LENGTH_LONG).show();
        }
    }

    private Observable<String[]> createTakenUserNameObservable() {

        return Observable.create(e -> {

            if (getCurrentUserNames() != null) {

                e.onNext(getCurrentUserNames());
                e.onComplete();

            } else {
                e.onError(new Throwable(new IllegalArgumentException("There was a problem setting up your account. Please try again later.")));
            }
        });
    }

    private void subscribeToTakenUserNames() {
        if (createTakenUserNameObservable() != null) {

            createTakenUserNameObservable().subscribe(new Observer<String[]>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String[] strings) {
                    validateName(getName(), strings);
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }



    // Simulated Data response
    private String[] getCurrentUserNames() {
        return new String[]{"Kevin88", "DaveDaveDave20", "khfire22@gmail.com", "Ivy555"};
    }

    private String getName() {
        return nameField.getText().toString();
    }

    @Override
    protected void onPause() {
        super.onPause();

        compositeDisposable.dispose();
    }
}
