package com.hetacz.mendtask.responses;

public record CodeAndResponse<T>(int code, T body) {

}
