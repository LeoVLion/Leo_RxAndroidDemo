package com.example.administrator.leo_rxandroiddemo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_content = null;
    @Bind(R.id.btn_use)
    Button btn_use;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ll_content = (LinearLayout) MainActivity.this.findViewById(R.id.ll_content);
//        detailRxJava();//基本使用
        syncRxJava();//同步使用
        asyncRxJava();//异步使用
    }

    @OnClick({R.id.btn_use})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_use:
                startActivity(new Intent(MainActivity.this, RxJavaActivity.class));
                break;
        }
    }

    /**
     * 第一部分：基本使用
     */
    private void detailRxJava() {
        //=============================================观察者创建方式一===============================================
        /**
         * 观察者的第一种创建方式  但在 RxJava 的 subscribe 过程中，Observer 也总是会先被转换成一个 Subscriber 再使用
         */
        Observer<String> observer = new Observer<String>() {

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onCompleted() {

            }
        };

        //=============================================观察者创建方式二===============================================
        /**
         * 观察者第二种创建方式 Subscriber   他与Observer基本功能是一样的
         *      1、拓展的方法：onStart()
         *      2、unsubscribe(): 这是 Subscriber 所实现的另一个接口 Subscription 的方法，用于取消订阅。
         *      在这个方法被调用后，Subscriber 将不再接收事件。一般在这个方法调用前，可以使用 isUnsubscribed() 先判断一下状态。
         *      unsubscribe() 这个方法很重要，因为在 subscribe() 之后， Observable 会持有 Subscriber 的引用，
         *      这个引用如果不能及时被释放，将有内存泄露的风险。所以最好保持一个原则：要在不再使用的时候尽快在合适的
         *      地方（例如 onPause() onStop() 等方法中）调用 unsubscribe() 来解除引用关系，以避免内存泄露的发生。
         */
        final Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("onCompleted", "====onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("onError", "====" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e("onNext", "====" + s);
            }

            /**
             * onStart(): 这是 Subscriber 增加的方法。
             */
            @Override
            public void onStart() {
                super.onStart();
            }
        };

        //=============================================被观察者===============================================
        /**
         * create() 方法是 RxJava 最基本的创造事件序列的方法。基于这个方法，
         *      RxJava 还提供了一些方法用来快捷创建事件队列:
         *          1、just(T...): 将传入的参数依次发送出来。
         *              Observable observable = Observable.just("Hello", "Hi", "Aloha");
         *
         *          2、from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
         *                  String[] words = {"Hello", "Hi", "Aloha"};
         *                  Observable observable = Observable.from(words);
         *
         *          上面 just(T...) 的例子和 from(T[]) 的例子，都和之前的 create(OnSubscribe) 的例子是等价的。
         */
        Observable observable = Observable.create(new Observable.OnSubscribe() {
            /**
             * 当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，
             * @param o
             */
            @Override
            public void call(Object o) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        //=============================================订阅（关联）方式一 ===============================================
        //observable.subscribe(observer);
        // 或者：
        observable.subscribe(subscriber);


        //=============================================订阅（关联）方式二 ===============================================
        /**
         * 除了 subscribe(Observer) 和 subscribe(Subscriber) ，subscribe() 还支持不完整定义的回调，
         * RxJava 会自动根据定义创建出 Subscriber 。形式如下：
         */
        Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.e("onNextAction", s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.e("onCompletedAction", "completed");
            }
        };

// 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
        observable.subscribe(onNextAction);
// 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
        observable.subscribe(onNextAction, onErrorAction);
// 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }

    /**
     * 第二部分：同步使用RxJava
     * 1、在 RxJava 的默认规则中，事件的发出和消费都是在同一个线程的。也就是说，如果只用下面的方法，
     * 实现出来的只是一个同步的观察者模式。
     * 2、观察者模式本身的目的就是『后台处理，前台回调』的异步机制，
     * 因此异步对于 RxJava 是至关重要的。而要实现异步，则需要用到 RxJava 的另一个概念： Scheduler 。
     */
    private void syncRxJava() {
        //打印字符串数组
        String[] names = new String[]{"RxJava", "RxAndroid"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        Log.e("syncRxJava", name);
                    }
                });

        //由 id 取得图片并显示
        final int drawableRes = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
                ll_content.addView(imageView);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 第三部分：异步使用RxJava（使用Scheduler）
     * 1、观察者模式本身的目的就是『后台处理，前台回调』的异步机制，
     * 因此异步对于 RxJava 是至关重要的。而要实现异步，则需要用到 RxJava 的另一个概念： Scheduler 。
     * <p>
     * 2、在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，就在哪个线程生产事件；
     * 在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （调度器）。
     * <p>
     * 3、RxJava 已经内置了几个 Scheduler
     * Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
     * Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
     * Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread()差不多，
     * 区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有
     * 效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的
     * 操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，
     * 否则 I/O 操作的等待时间会浪费 CPU。
     * 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
     */
    private void asyncRxJava() {
        /**
         *  打印字符
         *  此方式适用于多数的 『后台线程取数据，主线程显示』的程序策略。 具体情况具体分析
         */
        Observable.just(1, 2, 3, 4)
                //subscribeOn(): 指定 subscribe() 所发生的线程 即 Observable.OnSubscribe 被激活时所处的线程 或者叫做事件产生的线程
                .subscribeOn(Schedulers.io())
                //observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e("asyncRxJava", "====" + integer);
                    }
                });


        /**
         * 异步加载图片
         *      图片加载在异步线程 设置图片在主线程
         */
        final int drawableId = R.mipmap.ic_launcher;
        final ImageView imageView = new ImageView(this);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = ContextCompat.getDrawable(MainActivity.this, drawableId);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Drawable>() {
            @Override
            public void onCompleted() {
                Log.e("syncRxJava_set_image", "onCompleted");
                Toast.makeText(MainActivity.this, "syncRxJava set image onCompleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(MainActivity.this, "syncRxJava set image onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
                ll_content.addView(imageView);
            }
        });
    }
}
