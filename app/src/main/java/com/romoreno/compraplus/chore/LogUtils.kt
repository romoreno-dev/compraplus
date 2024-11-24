package com.romoreno.compraplus.chore

import android.util.Log

object LogUtils {

        const val NETWORK_LOG = "networkLog"

        fun networkLog(throwable: Throwable) = Log.i(NETWORK_LOG, "ERROR ${throwable.message}")

}