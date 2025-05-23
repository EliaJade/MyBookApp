package com.example.mybookapp.data

import com.example.mybookapp.R

enum class Status (val title: Int, val icon: Int, val color: Int) {
    WANT_TO_READ(R.string.status_want_to_read, R.drawable.ic_status_want_to_read, R.color.want_to_read),
    READING(R.string.status_reading, R.drawable.ic_status_reading, R.color.reading),
    READ(R.string.status_read, R.drawable.ic_status_read, R.color.read)
}