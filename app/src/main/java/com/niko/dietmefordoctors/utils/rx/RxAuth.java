package com.niko.dietmefordoctors.utils.rx;


import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import io.reactivex.Observable;

public class RxAuth {

  public static Observable<AuthResult> signInWithCredential(AuthCredential authCredential) {
    return Observable.create(observableEmitter -> {
      FirebaseAuth.getInstance().signInWithCredential(authCredential).addOnSuccessListener(
          result -> {
            observableEmitter.onNext(result);
            observableEmitter.onComplete();
          }).addOnFailureListener(observableEmitter::onError);
    });
  }


  public static Observable<String> getToken(AuthResult authResult) {
    return Observable.create(observableEmitter -> {
      authResult.getUser().getIdToken(true).addOnSuccessListener(getTokenResult -> {
        observableEmitter.onNext(getTokenResult.getToken());
        observableEmitter.onComplete();
      }).addOnFailureListener(observableEmitter::onError);
    });
  }
}
