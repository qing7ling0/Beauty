package com.lq.beauty.app.RxBus;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wuqingqing on 2017/3/30.
 */

public class RxBus {
    private static volatile RxBus instance = null;

    /**
     * PublishSubject只会把订阅发生的时间点之后来自原始Observable的数据发送给观察者
     */
    private Subject<Object> mRxBus = PublishSubject.create();

    public static RxBus getInstance(){
        if (instance == null){
            synchronized (RxBus.class){
                if (instance == null){
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    /**
     * 判断当前是否有可用的Observable
     * @return
     */
    private boolean hasObservable(){
        return mRxBus.hasObservers();
    }

    /**
     * 发送新的事件
     * @param o
     */
    public void post(Object o){
        if (hasObservable()){
            mRxBus.onNext(o);
        }
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * @param eventType
     * @param <T>
     * @return
     */
    public <T> Observable<T> toObservable(Class<T> eventType){
        return mRxBus.ofType(eventType);
    }

    /**
     * 取消事件订阅
     * @param subscription
     */
    public void unRegister(Subscription subscription){
        if (subscription !=null ){
            subscription.cancel();
        }
    }

}
