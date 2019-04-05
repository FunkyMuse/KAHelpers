package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Created by hristijan on 3/28/19 to long live and prosper !
 */
object FileEncryption {

    private val BUFFER_SIZE = 1024
    private val ENCRYPTOR = "AES/CBC/PKCS5padding"
    private val SECRET_KEY = "AES"

    /**
     * Store the secret key and the cypher string to your server not locally
     */

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, IOException::class)
    fun encryptToFile(secretKey:String, cypherSpecString:String, input:InputStream, output:OutputStream){
        var out = output
        try {
            val iv = IvParameterSpec(cypherSpecString.toByteArray(Charsets.UTF_8))
            val keyspec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8),
                SECRET_KEY
            )

            val cipher = Cipher.getInstance(ENCRYPTOR)
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv)
            out = CipherOutputStream(output, cipher)

            val buffer = ByteArray(BUFFER_SIZE)

            var bytesRead: Int
            while (input.read(buffer).also { bytesRead=it } > 0){
                out.write(buffer, 0, bytesRead)
            }
        }
        finally {
            out.close()
        }
    }


    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, IOException::class)
    fun decryptToFile(secretKey:String, cypherSpecString:String, input:InputStream, output:OutputStream){
        var out = output
        try {
            val iv = IvParameterSpec(cypherSpecString.toByteArray(Charsets.UTF_8))
            val keyspec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8),
                SECRET_KEY
            )

            val cipher = Cipher.getInstance(ENCRYPTOR)
            cipher.init(Cipher.DECRYPT_MODE, keyspec, iv)
            out = CipherOutputStream(output, cipher)

            val buffer = ByteArray(BUFFER_SIZE)

            var bytesRead: Int
            while (input.read(buffer).also { bytesRead=it } > 0){
                out.write(buffer, 0, bytesRead)
            }
        }
        finally {
            out.close()
        }
    }
}