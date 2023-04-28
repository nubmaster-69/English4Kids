package com.hisu.english4kids.data.model.course

data class Course (
    val courseTitle: String,
    val totalLevel: Int,
    var currentLevel: Int = 0,
    var isComplete: Boolean = false,
    var isLock: Boolean = false,
    var courseDesc: String,
    var courseLevels: List<Lesson>
)