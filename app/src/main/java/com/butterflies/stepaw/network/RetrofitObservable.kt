package com.butterflies.stepaw.network

import java.util.*

class RetrofitObservable : Observable() {
    private var instance: RetrofitObservable? = null

    fun getInstance(): RetrofitObservable? {
        if (instance == null) {
            instance = RetrofitObservable()
        }
        return instance
    }

    fun notifyObserverWithResponse(response: Any?) {
        setChanged()
        notifyObservers(response)
    }
}