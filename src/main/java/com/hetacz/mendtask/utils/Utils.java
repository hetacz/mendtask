package com.hetacz.mendtask.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public boolean isNullOrBlank(String s) {
        return s == null || s.isBlank();
    }
}
