package ru.skillbranch.kotlinexample.extensions

fun String.trimPhone(): String = this.replace("[^+\\d]".toRegex(), "")