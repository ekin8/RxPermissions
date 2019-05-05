package com.tbruyelle.rxpermissions2;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class RxPermissionsUtils {

    @UiThread
    public static RxPermissions getInstance(@NonNull Context context) {
        if (!(context instanceof Activity)) {
            throw new IllegalArgumentException("To use RxPermissionsUtils,context must be FragmentActivity");
        }
        return new RxPermissions((FragmentActivity) context);
    }

    private static Observable<FragmentActivity> getActivityObservable(@NonNull Context context) {
        return Observable.just(context)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<Context, FragmentActivity>() {
                    @Override
                    public FragmentActivity apply(Context context) throws Exception {
                        if (!(context instanceof FragmentActivity)) {
                            throw new IllegalArgumentException("To use RxPermissionsUtils,context must be FragmentActivity");
                        }
                        return (FragmentActivity) context;
                    }
                });
    }

    public static Observable<Boolean> request(@NonNull Context context, final String... permissions) {
        return getActivityObservable(context)
                .flatMap(new Function<FragmentActivity, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(FragmentActivity activity) throws Exception {
                        return new RxPermissions(activity).request(permissions);
                    }
                });
    }

    public static Observable<Permission> requestEach(@NonNull Context context, final String... permissions) {
        return getActivityObservable(context)
                .flatMap(new Function<FragmentActivity, ObservableSource<Permission>>() {
                    @Override
                    public ObservableSource<Permission> apply(FragmentActivity activity) throws Exception {
                        return new RxPermissions(activity).requestEach(permissions);
                    }
                });
    }

}