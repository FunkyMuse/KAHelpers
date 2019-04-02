package com.crazylegend.kotlinextensions.dataStructuresAndAlgorithms.cryptography

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec
import javax.security.auth.x500.X500Principal
import javax.security.cert.CertificateException


/**
 * Created by hristijan on 4/2/19 to long live and prosper !
 */

 object EncriptionUtil {
    private val AndroidKeyStore = "AndroidKeyStore"
    private val AES_MODE = "AES/GCM/NoPadding"
    private val FIXED_IV = "randomizemsg" // to randomize the encrypted data( give any values to randomize)
    private val KEY_ALIAS =
        "samplekeyalias" //give any key alias based on your application. It is different from the key alias used to sign the app.
    private val RSA_MODE = "RSA/ECB/PKCS1Padding" // RSA algorithm which has to be used for OS version less than M
    private var keyStore: KeyStore? = null

    fun generateKey(context: Context) {

        try {
            keyStore = KeyStore.getInstance(AndroidKeyStore)
            keyStore!!.load(null)

            if (!keyStore!!.containsAlias(KEY_ALIAS)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore)
                    keyGenerator.init(
                        KeyGenParameterSpec.Builder(
                            KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                        )
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build()
                    )
                    keyGenerator.generateKey()
                } else {

                    // Generate a key pair for encryption
                    val start = Calendar.getInstance()
                    val end = Calendar.getInstance()
                    end.add(Calendar.YEAR, 30)
                    val spec: KeyPairGeneratorSpec? =
                        KeyPairGeneratorSpec.Builder(context)
                        .setAlias(KEY_ALIAS)
                        .setSubject(X500Principal("CN=$KEY_ALIAS"))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.time)
                        .setEndDate(end.time)
                        .build()
                    val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, AndroidKeyStore)
                    kpg.initialize(spec)
                    kpg.generateKeyPair()
                }
            } else {
                Log.d("keyStore", "key already available")
            }
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    private fun getSecretKey(): java.security.Key {
        return keyStore!!.getKey(KEY_ALIAS, null)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun encryptM(input: String?): String {
        val c: Cipher?
        try {
            c = Cipher.getInstance(AES_MODE)
            c!!.init(Cipher.ENCRYPT_MODE, getSecretKey(), GCMParameterSpec(128, FIXED_IV.toByteArray()))
            val encodedBytes = c.doFinal(input!!.toByteArray())
            return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun decryptM(encrypted: String): ByteArray? {
        val c: Cipher?
        try {
            c = Cipher.getInstance(AES_MODE)
            c!!.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, FIXED_IV.toByteArray()))
            val barr = Base64.decode(encrypted, Base64.DEFAULT)
            return c.doFinal(barr)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    @Throws(Exception::class)
    private fun rsaEncrypt(secret: ByteArray): String {
        val privateKeyEntry = keyStore!!.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        // Encrypt the text
        val inputCipher = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.certificate.publicKey)

        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, inputCipher)
        cipherOutputStream.write(secret)
        cipherOutputStream.close()

        val vals = outputStream.toByteArray()
        return Base64.encodeToString(vals, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun rsaDecrypt(encrypted: String): ByteArray {
        val privateKeyEntry = keyStore!!.getEntry(KEY_ALIAS, null) as KeyStore.PrivateKeyEntry
        val output = Cipher.getInstance(RSA_MODE, "AndroidOpenSSL")
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)
        val barr = Base64.decode(encrypted, Base64.DEFAULT)
        val cipherInputStream = CipherInputStream(
            ByteArrayInputStream(barr), output
        )
        val values = ArrayList<Byte>()
        cipherInputStream.reader().forEachLine {
            values.add(it.toByte())
        }

        val bytes = ByteArray(values.size)
        for (i in bytes.indices) {
            bytes[i] = values[i]
        }
        return bytes
    }

    fun decrypt(text: String): String {
        try {
            var d: ByteArray? = ByteArray(0)
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                d = EncriptionUtil.decryptM(text)
            } else {
                try {
                    d = EncriptionUtil.rsaDecrypt(text)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            assert(d != null)
            return d?.let { String(it, Charsets.UTF_8) }.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun encrypt(text: String?): String {
        var input = text
        try {
            val d = ByteArray(0)
            input = if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                EncriptionUtil.encryptM(input)
            } else {
                input?.toByteArray()?.let { EncriptionUtil.rsaEncrypt(it) }
            }
            return input.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}