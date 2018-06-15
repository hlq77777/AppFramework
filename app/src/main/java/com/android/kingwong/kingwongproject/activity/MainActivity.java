package com.android.kingwong.kingwongproject.activity;

 import android.os.Bundle;
 import android.text.TextUtils;
 import android.view.View;
 import android.widget.Button;

 import com.android.kingwong.appframework.Activity.BaseActivity;
 import com.android.kingwong.appframework.util.IntentUtil;
 import com.android.kingwong.appframework.util.LogUtil;
 import com.android.kingwong.appframework.util.OneClickUtil.AntiShake;
 import com.android.kingwong.appframework.util.RxJavaUtil.RxJavaUtil;
 import com.android.kingwong.kingwongproject.R;
 import com.android.kingwong.kingwongproject.bean.User;
 import com.android.kingwong.kingwongproject.module.Updata;
 import com.android.kingwong.kingwongproject.novate.ExampleActivity;
 import com.jakewharton.rxbinding2.view.RxView;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.concurrent.Callable;
 import java.util.concurrent.TimeUnit;

 import butterknife.BindView;
 import butterknife.OnClick;
 import io.reactivex.Completable;
 import io.reactivex.Maybe;
 import io.reactivex.MaybeEmitter;
 import io.reactivex.MaybeOnSubscribe;
 import io.reactivex.Notification;
 import io.reactivex.Observable;
 import io.reactivex.ObservableEmitter;
 import io.reactivex.ObservableOnSubscribe;
 import io.reactivex.ObservableSource;
 import io.reactivex.ObservableTransformer;
 import io.reactivex.Observer;
 import io.reactivex.Single;
 import io.reactivex.SingleEmitter;
 import io.reactivex.SingleOnSubscribe;
 import io.reactivex.android.schedulers.AndroidSchedulers;
 import io.reactivex.disposables.Disposable;
 import io.reactivex.functions.Action;
 import io.reactivex.functions.BiConsumer;
 import io.reactivex.functions.BiFunction;
 import io.reactivex.functions.Consumer;
 import io.reactivex.functions.Function;
 import io.reactivex.functions.Predicate;
 import io.reactivex.observables.GroupedObservable;
 import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.button_updata)
    Button button_updata;
    @BindView(R.id.button_novate)
    Button button_novate;

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void OnCreate(Bundle savedInstanceState) {
        initView();
        showRxjava();
    }

//    @OnClick({R.id.button_updata, R.id.button_novate})
//    public void onClick(View view) {
//        if (AntiShake.check(view.getId())) {
//            //判断是否多次点击
//            return;
//        }
//        switch (view.getId()) {
//            case R.id.button_updata:
//                IntentUtil.startActivity(this, Updata.class);
//                break;
//            case R.id.button_novate:
//                IntentUtil.startActivity(this, ExampleActivity.class);
//                break;
//        }
//    }

    private void initView(){
        RxView.clicks(button_updata)
                .compose(RxJavaUtil.useRxViewTransformer(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        IntentUtil.startActivity(MainActivity.this, Updata.class);
                    }
                });
        RxView.clicks(button_novate)
                .compose(RxJavaUtil.useRxViewTransformer(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        IntentUtil.startActivity(MainActivity.this, ExampleActivity.class);
                    }
                });
    }

    private void newRxjavaHelloWorld(){//Rxjava Hello World
        Disposable mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("Hello World");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaHelloWorld", s);
            }
        });
    }

    private void newRxjavaDo(){//do操作符
        Disposable mDisposable = Observable.just("Hello World").doOnSubscribe(new Consumer<Disposable>() {
            //订阅之后回调的方法
            @Override
            public void accept(Disposable disposable) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnSubscribe");
            }
        }).doOnLifecycle(new Consumer<Disposable>() {
            //在观察者订阅之后，设置是否取消订阅
            @Override
            public void accept(Disposable disposable) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnLifecycle disposable.isDisposed(): " + disposable.isDisposed());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doOnLifecycle run");
            }
        }).doOnNext(new Consumer<String>() {
            //它产生的Observable每发射一项数据就会调用它一次，它的Consumer接收发射的数据项，一般用于在subscriber之前对数据进行处理
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnNext:" + s);
            }
        }).doAfterNext(new Consumer<String>() {
            //在onNext之后执行，而doOnNext()是在onNext之前执行
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "doAfterNext:" + s);
            }
        }).doOnComplete(new Action() {
            //当它产生的Observable在正常终止调用onComplete时会被调用
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doOnComplete");
            }
        }).doFinally(new Action() {
            //当它产生的Observable终止之后会被调用，无论是正常终止还是异常终止。doFinally优先于doAfterTerminate的调用
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doFinally");
            }
        }).doAfterTerminate(new Action() {
            //注册一个Action，当Observable调用onComplete或Error时触发
            @Override
            public void run() throws Exception {
                LogUtil.e("newRxjavaDo", "doAfterTerminate");
            }
        }).doOnEach(new Consumer<Notification<String>>() {
            //它产生的Observable每发射一项数据就会调用它一次，不仅包括onNext，还包括onError和onCompleted
            @Override
            public void accept(Notification<String> stringNotification) throws Exception {
                LogUtil.e("newRxjavaDo", "doOnEach: " +
                        (stringNotification.isOnNext() ? "OnNext" : stringNotification.isOnComplete() ? "OnComplete" : "onError"));
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newRxjavaDo", "subscribe: " + s);
            }
        });
    }

    private void newSingle(){//被观察者Single，只有onSuccess和onError事件
        Disposable mDisposable = Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("Hello Single");
            }
        }).subscribe(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) throws Exception {
                LogUtil.e("newSingle", "subscribe: " + s);
            }
        });
    }

    private void newCompletable(){//被观察者Completable，不会发射任何数据，只有onComplete和onError事件
        Disposable mDisposable = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newCompletable", "Hello Completable");
            }
        }).subscribe();
    }

    private void newMaybe(){//被观察者Maybe，Single和Completable的结合，只有onSuccess，onComplete和onError事件
        Disposable mDisposable = Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> e) throws Exception {
                e.onSuccess("Hello Maybe");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                LogUtil.e("newMaybe", "subscribe: " + s);
            }
        });
    }

    private void newCreate(){//操作符create，使用一个函数从头创建一个Observable
        Disposable mDisposable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                try {
                    if(!emitter.isDisposed()){
                        for (int i = 0; i < 3; i++){
                            emitter.onNext(i);
                        }
                        emitter.onComplete();
                    }
                }catch (Exception e){
                    emitter.onError(e);
                }
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                LogUtil.e("newCreate", "Next: " + integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                LogUtil.e("newCreate", "Error: " + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                LogUtil.e("newCreate", "Complete");
            }
        });
    }

    private void newJust(){//操作符just，将一个或多个对象转换成发射这个或这些对象的一个Observable
        Disposable mDisposable = Observable.just(1, 2, 3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("newJust", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("newJust", "Throwable: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("newJust", "Complete");
                    }
                });
    }

    private void newFromArray(){//操作符fromArray，将一个数组转换成一个Observable
        Disposable mDisposable = Observable.fromArray("hello", "from")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("newFromArray", "subscribe: " + s);
                    }
                });
    }

    private void newFromIterable(){//操作符fromIterable，将Iterable转换成一个Observable
        List<Integer> items = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            items.add(i);
        }

        Disposable mDisposable = Observable.fromIterable(items)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("newFromIterable", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("newFromIterable", "Throwable: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("newFromIterable", "Complete");
                    }
                });
    }

    private void newRepeat(){//操作符repeat，创建一个发射特定数据重复多次的Observable
        Disposable mDisposable = Observable.just("hello repeat")
                .repeat(3)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("newRepeat", "subscribe: " + s);
                    }
                });
    }

    private void newDefer(){//操作符defer，只有当订阅者订阅才创建Observable，为每一个订阅创建一个新的Observable
        Observable observable = Observable
                .defer(new Callable<ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> call() throws Exception {
                        return Observable.just("hello defer");
                    }
                });

        Disposable mDisposable = observable.subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("newDefer", "subscribe: " + s);
                    }
                });
    }

    private void newInterval(){//操作符interval，创建一个按照给定的时间间隔发射整数序列的Observable
        Disposable mDisposable = Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .take(3)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.e("newInterval", "subscribe: " + aLong);
                    }
                });
    }

    private void newTimer(){//操作符timer，创建一个在给定的延时之后发射单个数据的Observable
        Disposable mDisposable = Observable.timer(2, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtil.e("newTimer", "hello timer");
                    }
                });
    }

    private void newRange(){//操作符range,创建一个发射指定范围的整数序列的Observable
        Disposable mDisposable = Observable.range(1,3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("newRange", "subscribe: " + integer);
                    }
                });
    }

    private void rxjavaScheduler(){//Scheduler线程调度器
        Disposable mDisposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                LogUtil.e("rxjavaScheduler", "Scheduler: " + Thread.currentThread().getName());
                e.onNext("hello scheduler");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("rxjavaScheduler", "Scheduler: " + Thread.currentThread().getName());
                        LogUtil.e("rxjavaScheduler", "subscribe: " + s);
                    }
                });
    }

    private void rxjavaMap(){//变换操作符map，对序列的每一项都用一个函数来变换Observable发射的数据序列
        Disposable mDisposable = Observable.just("HELLO")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s.toLowerCase();
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        return s + " world";
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        LogUtil.e("rxjavaMap", "subscribe: " + s);
                    }
                });
    }

    private void rxjavaMap2(){//变换操作符map
        User user = new User();
        user.setUserName("KingWong");
        user.setAddresses("123", "456");
        user.setAddresses("abc", "def");

        Disposable mDisposable = Observable.just(user)
                .map(new Function<User, List<User.Address>>() {
                    @Override
                    public List<User.Address> apply(User user) throws Exception {
                        return user.getAddresses();
                    }
                })
                .subscribe(new Consumer<List<User.Address>>() {
                    @Override
                    public void accept(List<User.Address> addresses) throws Exception {
                        for(User.Address address : addresses){
                            LogUtil.e("rxjavaMap2", "subscribe address: " + address.getStreet());
                        }
                    }
                });
    }

    private void rxjavaFlatMap(){//变换操作符flatMap，将一个发射数据的Observable变换成为多个Observables，然后将它们发射的数据合并后放进一个单独的Observable
        User user = new User();
        user.setUserName("KingWong");
        user.setAddresses("123", "456");
        user.setAddresses("abc", "def");

        Disposable mDisposable = Observable.just(user)
                .flatMap(new Function<User, ObservableSource<User.Address>>() {
                    @Override
                    public ObservableSource<User.Address> apply(User user) throws Exception {
                        return Observable.fromIterable(user.getAddresses());
                    }
                })
                .subscribe(new Consumer<User.Address>() {
                    @Override
                    public void accept(User.Address address) throws Exception {
                        LogUtil.e("rxjavaFlatMap", "subscribe address: " + address.getStreet());
                    }
                });
    }

    private void rxjavaGroupBy(){//变换操作符groupBy,将一个Observable拆分为一些Observables集合，它们中的每一个都发射原始Observable的一个子序列
        Disposable mDisposable = Observable.range(1, 8)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return (integer % 2 == 0) ? "偶数组" : "奇数组";
                    }
                })
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(final GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                        if(stringIntegerGroupedObservable.getKey().equals("奇数组")){
                            Disposable myDisposable = stringIntegerGroupedObservable.subscribe(new Consumer<Integer>() {
                                @Override
                                public void accept(Integer integer) throws Exception {
                                    LogUtil.e("rxjavaGroupBy", stringIntegerGroupedObservable.getKey() + "member: " + integer);
                                }
                            });
                        }
                    }
                });
    }

    private void rxjavaBuffer(){//变换操作符buffer，将一个Observable变换为另一个，由变换产生的Observable发射这些数据的缓存集合
        Disposable mDisposable = Observable.range(1, 9)
                .buffer(3)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        LogUtil.e("rxjavaBuffer", "subscribe: " + integers);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaBuffer", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaBuffer", "Complete");
                    }
                });
    }

    private void rxjavaWindow(){//变换操作符window,定期将来自原始Observable的数据分解为一个Observable窗口，发射这些窗口，window发射的是Observables
        Disposable mDisposable = Observable.range(1, 6)
                .window(2)
                .subscribe(new Consumer<Observable<Integer>>() {
                    @Override
                    public void accept(Observable<Integer> integerObservable) throws Exception {
                        LogUtil.e("rxjavaWindow", "onNext");
                        Disposable myDisposable = integerObservable
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer integer) throws Exception {
                                        LogUtil.e("rxjavaWindow", "subscribe: " + integer);
                                    }
                                });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaWindow", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaWindow", "Complete");
                    }
                });
    }

    private void rxjavaFirst(){//过滤操作符first,只对Observable发射的第一项数据，或者满足某个条件的第一项数据
        Disposable mDisposable = Observable.just(1, 2, 3)
                .first(1)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaFirst", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaFirst", "onError: " + throwable.getMessage());
                    }
                });
    }

    private void rxjavaLast(){//过滤操作符last,只对Observable发射的最后一项数据，或者满足某个条件的最后一项数据
        Disposable mDisposable = Observable.just(1, 2, 3)
                .last(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaLast", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaLast", "onError: " + throwable.getMessage());
                    }
                });
    }

    private void rxjavaTake(){//过滤操作符take,只发射前面的n项数据，发射完成通知，忽略剩余数据
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .take(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaTake", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaTake", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaTake", "Complete");
                    }
                });
    }

    private void rxjavaTakeLast(){//过滤操作符takeLast,只发射最后n项数据，发射完成通知，忽略前面数据
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .takeLast(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaTakeLast", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaTakeLast", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaTakeLast", "Complete");
                    }
                });
    }

    private void rxjavaSkip(){//过滤操作符skip,忽略Observable发射的前n项数据，保留之后的数据
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .skip(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaSkip", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaSkip", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaSkip", "Complete");
                    }
                });
    }

    private void rxjavaSkipLast(){//过滤操作符skipLast,忽略Observable发射的后n项数据，保留前面的数据
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .skipLast(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaSkipLast", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaSkipLast", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaSkipLast", "Complete");
                    }
                });
    }

    private void rxjavaElementAt(){//过滤操作符skip,只发射第n项数据，0为第一项索引值
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .elementAt(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaElementAt", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaElementAt", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaElementAt", "Complete");
                    }
                });
    }

    private void rxjavaIgnoreElements(){//过滤操作符ignoreElements,不发射任何数据，只发射Observable的终止通知
        Disposable mDisposable = Observable.just(1, 2, 3, 4, 5)
                .ignoreElements()
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaIgnoreElements", "Complete");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaIgnoreElements", "onError: " + throwable.getMessage());
                    }
                });
    }

    private void rxjavaDistinct(){//过滤操作符distinct,过滤掉重复的数据项，只允许还没有发射过的数据项通过
        Disposable mDisposable = Observable.just(1, 2, 1, 2, 3, 4, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaDistinct", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaDistinct", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaDistinct", "Complete");
                    }
                });
    }

    private void rxjavaFilter(){//过滤操作符filter,只发射通过谓词测试函数的数据项
        Disposable mDisposable = Observable.just(2, 30, 22, 5, 60, 1)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 10;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaFilter", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaFilter", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaFilter", "Complete");
                    }
                });
    }

    private void rxjavaMerge(){//合并操作符merge,合并多个Observable的发射物
        Disposable mDisposable = Observable.merge(Observable.just(1, 3, 5), Observable.just(2, 4, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaMerge", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaMerge", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaMerge", "Complete");
                    }
                });
    }

    private void rxjavaZip(){//合并操作符zip,通过一个函数将多个Observable的发射物结合在一起，基于这个函数的结果为每个结合体发射单个数据项
        Disposable mDisposable = Observable.zip(Observable.just(1, 3, 5), Observable.just(2, 4, 6),
                new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                })
                .compose(RxJavaUtil.observableToMain())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.e("rxjavaZip", "subscribe: " + integer);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.e("rxjavaZip", "onError: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.e("rxjavaZip", "Complete");
                    }
                });
    }

    private ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private ObservableTransformer upperCaseTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream.map(new Function<String, String>() {
                @Override
                public String apply(String o) throws Exception {
                    return o.toUpperCase();
                }
            });
        }
    };

    private void rxjavaCompose(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String str = "hello rxjava";
                try {
                    if(!TextUtils.isEmpty(str)){
                        emitter.onNext(str);
                        emitter.onComplete();
                    }
                }catch (Exception e){
                    emitter.onError(e);
                }
            }
        })
                .compose(upperCaseTransformer)
                .compose(schedulersTransformer)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtil.e("rxjavaCompose", "onSubscribe d.isDisposed(): " + d.isDisposed());
                    }
                    @Override
                    public void onNext(String o) {
                        LogUtil.e("rxjavaCompose", "subscribe: " + o);
                    }
                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e("rxjavaCompose", "onError: " + e.getMessage());
                    }
                    @Override
                    public void onComplete() {
                        LogUtil.e("rxjavaCompose", "Complete");
                    }
                });
    }

    private void showRxjava(){
        newRxjavaHelloWorld();
        newRxjavaDo();
        newSingle();
        newCompletable();
        newMaybe();
        newCreate();
        newJust();
        newFromArray();
        newFromIterable();
        newRepeat();
        newDefer();
        newInterval();
        newTimer();
        newRange();
        rxjavaScheduler();
        rxjavaMap();
        rxjavaMap2();
        rxjavaFlatMap();
        rxjavaGroupBy();
        rxjavaBuffer();
        rxjavaWindow();
        rxjavaFirst();
        rxjavaLast();
        rxjavaTake();
        rxjavaTakeLast();
        rxjavaSkip();
        rxjavaSkipLast();
        rxjavaElementAt();
        rxjavaIgnoreElements();
        rxjavaDistinct();
        rxjavaFilter();
        rxjavaMerge();
        rxjavaZip();
        rxjavaCompose();
    }

}
