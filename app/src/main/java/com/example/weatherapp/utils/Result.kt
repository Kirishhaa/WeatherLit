package com.example.weatherapp.utils

sealed class Result<T>

sealed class FinalResult<T> : Result<T>()

class SuccessfulResult<T>(val data: T) : FinalResult<T>()

class ErrorResult<T>(val error: Throwable) : FinalResult<T>()

class PendingResult<T> : Result<T>()

class EmptyResult<T> : Result<T>()