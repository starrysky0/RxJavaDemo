package com.starry.sky.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends AppCompatActivity {
    private String[] arr = {"abcd", "bcde", "cdef"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 现有一个数组 String[] arr = {"abcd", "bcde", "cdef"}, 把其中以字母"a"开头的字符串找出来并加上"from Alpha",最后打印出新的字符串的长度

        case02();
    }

    //如果只需要关心Observer 里的onNext()方法;可以创建Action1的对象,这样代码会更加简洁了
    private void case04() {
        Observable.from(arr)
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.startsWith("a");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        String temp = s += " from Alpha";
                        System.out.println("RxJava:" + temp + temp.length());
                    }
                });
    }

    //RxJava简化后的方法,其实也就是case02()方法串了一下;
    //这时候你会吃惊的发现代码变化会很大
    private void case03() {
        Observable.from(arr)
                //过滤
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.startsWith("a");
                    }
                    //订阅关联
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                        //参数列表中的s就是数组arr中过滤出来满足条件的字符串了
                        String temp = s += " from Alpha";
                        System.out.println("RxJava:" + temp + temp.length());
                    }
                });

    }

    //RxJava方法
    private void case02() {
        //Observable被观察者的作用: 决定什么时候触发事件以及触发怎样的事件,有两种创建方式:
        //(1): Observable.just(T...) 参数为单个的
        //(2): Observable.from(T[]) / Observable.from(Iterable<? extends T>)  参数为数组或Iterable
        //这里操作的是数组所以用from()方法
        Observable<String> observable = Observable.from(arr);

        //通过filter()方法得到过滤的结果,而过滤的逻辑在Func1里面的call()方法中操作;
        Observable<String> filterResult = observable.filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.startsWith("a");
            }
        });

        //创建观察者Observer,当被观察者处理数据得到结果时,观察者这边会有对应的回调方法得到反馈;
        //主要是以下三个方法
        //onNext()：普通事件,在这里会反馈出过滤的结果
        //onCompleted():事件队列完结
        //onError(): 事件队列异常
        //需要注意的是onCompleted()和onError()是互斥的.调用了其中一个就不应该触发另一个.
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("RxJava:onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("RxJava:onError" + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                //参数列表中的s就是数组arr中过滤出来满足条件的字符串了
                String temp = s += " from Alpha";
                System.out.println("RxJava:" + temp + temp.length());
            }
        };

        //最后就是传递 (观察者)observer 与 (被观察者Observable过滤的结果)filterResult 通过subscribe()方法的关联,
        //这里观察者observer就是警察 , 被观察者Observable就是小偷, filterResult偷东西的小偷,通俗点翻译就是下面这个结果
        // 偷东西的小偷. 被订阅 (警察);     <呵呵, 虽然有点强行,不过你会发现这逻辑会记得比较清楚>
        filterResult.subscribe(observer);
    }

    //传统方法,直接通过for遍历来操作
    private void case01() {
        for (int i = 0; i < arr.length; i++) {
            String temp = arr[i];
            if (temp.startsWith("a")) {
                temp += " from Alpha";
                System.out.println("traditional:" + temp + temp.length());
            }
        }
    }
}
