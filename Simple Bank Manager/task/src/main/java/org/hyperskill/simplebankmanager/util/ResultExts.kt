package org.hyperskill.simplebankmanager.util

fun Boolean.asResult(): Result<Unit> {
    return if (this) {
        Result.success(Unit)
    } else {
        Result.failure(Exception("Operation failed"))
    }
}
