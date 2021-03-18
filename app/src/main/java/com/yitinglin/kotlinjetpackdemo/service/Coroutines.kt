package com.yitinglin.kotlinjetpackdemo.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Coroutines {
    fun main(work:suspend (()->Unit))= CoroutineScope(Dispatchers.Main).launch {
        work()
    }
}