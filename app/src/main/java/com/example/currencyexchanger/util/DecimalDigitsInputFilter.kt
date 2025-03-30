package com.example.currencyexchanger.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(digitsBeforeZero: Int, digitsAfterZero: Int) : InputFilter {
    private val pattern: Pattern = Pattern.compile(
        "[0-9]{0,$digitsBeforeZero}+((\\.[0-9]{0,$digitsAfterZero})?)||(\\.)?"
    )

    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        val newString = StringBuilder(dest)
        newString.replace(dstart, dend, source.subSequence(start, end).toString())
        val matcher = pattern.matcher(newString.toString())
        return if (!matcher.matches()) "" else null
    }
}