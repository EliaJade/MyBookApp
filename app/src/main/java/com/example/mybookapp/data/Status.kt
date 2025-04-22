package com.example.mybookapp.data

import com.example.mybookapp.R

enum class Status (val title: Int) {
    WANT_TO_READ(R.string.status_want_to_read),
    READING(R.string.status_reading),
    READ(R.string.status_read)
}