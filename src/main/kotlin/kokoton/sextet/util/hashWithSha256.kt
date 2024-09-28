package kokoton.sextet.util

import java.security.MessageDigest

fun hashWithSHA256(str: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val hashedBytes = md.digest(str.toByteArray())
    return hashedBytes.joinToString("") { "%02x".format(it) }
}