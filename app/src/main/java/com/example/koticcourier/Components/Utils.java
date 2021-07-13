package com.example.koticcourier.Components;
import android.text.TextUtils;
import java.util.regex.Pattern;

public class Utils {
    public final static boolean isValidPhone(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            //
            return Pattern.matches("^5(0[5-7]|[3-5]\\d)\\d{3}\\d{4}$", target);
        }
    }

}