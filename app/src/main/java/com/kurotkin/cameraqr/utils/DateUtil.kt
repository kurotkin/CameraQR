package com.kurotkin.cameraqr.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    fun getDateTime() = SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Date());
    fun getTime() = SimpleDateFormat("HH:mm:ss").format(Date());
}