package com.snikitinde.cryptpassword

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class EncryptString {

    fun hashString(type: String, str: String): String {
        val hexChars = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(str.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(hexChars[i shr 4 and 0x0f])
            result.append(hexChars[i and 0x0f])
        }

        return result.toString()
    }

    private fun reverseString(str: String): String {
        return StringBuilder(str).reverse().toString()
    }

    fun fixedSaltEncryptString(text: String,
                               password: String,
                               codeword: String,
                               type: String): String {
        val salt = "^&#%@~$*^"
        val str = salt + text + codeword + password + salt
        var result = ""

        try {
            result = hashString(type, str).toLowerCase()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        val array = reverseString(result).substring(5, 30).toCharArray()

        for (i in array.indices) {
            if (!Character.isDigit(array[i])) {
                array[i] = Character.toUpperCase(array[i])
                break
            }
        }

        for (i in array.indices) {
            if (Character.isDigit(array[i])) {
                array[i] = '%'
                break
            }
        }

        return String(array)
    }
}