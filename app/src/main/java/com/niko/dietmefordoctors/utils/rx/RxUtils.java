package com.niko.dietmefordoctors.utils.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxUtils {

  public static <T> ObservableTransformer<T, T> applySchedulers() {
    return obs -> obs.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
  }
}