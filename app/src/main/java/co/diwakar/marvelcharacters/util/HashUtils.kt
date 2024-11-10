package co.diwakar.marvelcharacters.util

import co.diwakar.marvelcharacters.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Uses to generate MD5 hash with the help of
 * [timeStamp], [privateKey] & [publicKey]
 * */
object HashUtils {
    fun hash(timeStamp: Long): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        val privateKey = BuildConfig.MARVEL_API_PRIVATE_KEY
        val publicKey = BuildConfig.MARVEL_API_PUBLIC_KEY
        val toConvert = "$timeStamp$privateKey$publicKey"

        return BigInteger(
            1,
            messageDigest.digest(toConvert.toByteArray())
        ).toString(16).padStart(32, '0')
    }
}