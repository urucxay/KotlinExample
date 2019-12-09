package ru.skillbranch.kotlinexample.extentions

fun String.trimPhone(): String = this.replace("[^+\\d]".toRegex(), "")