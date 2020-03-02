package com.crazylegend.kotlinextensions.crypto
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.io.File
import java.security.KeyPair
import java.security.KeyPairGenerator

/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */



@RequiresApi(Build.VERSION_CODES.M)
val aes256KeySpec = MasterKeys.AES256_GCM_SPEC

@RequiresApi(Build.VERSION_CODES.M)
val aes256KeyAlias = MasterKeys.getOrCreate(aes256KeySpec)


/**
 * Encrypts a file to the storage, decrypt using [decryptDataFromStorage]
 * @receiver Context
 * @param fileToWrite the file to be written to, usually on your storage
 * @param keyAlias [aes256KeyAlias] or your custom, if not provided it will be used aes256
 * @param bytesToWrite the content that needs to be written to the file
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.encryptDataToStorage(fileToWrite: File, keyAlias: String? = null, bytesToWrite: ByteArray) {
    buildEncryptedFile(fileToWrite, keyAlias).openFileOutput().use {
        it.write(bytesToWrite)
    }
}

/**
 *
 * Decrypts a file from the storage the inverse of [encryptDataToStorage]
 * @receiver Context
 * @param fileToDecrypt File
 * @param keyAlias String?
 * @param bytesRead Callback with the bytes input
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.decryptDataFromStorage(fileToDecrypt: File, keyAlias: String? = null, bytesRead: (byteArray: ByteArray) -> Unit = {}) {
    buildEncryptedFile(fileToDecrypt, keyAlias).openFileInput().use {
        bytesRead(it.readBytes())
    }
}


/**
 * Generates key with some restrictions
 * @param keystoreAlias String
 * @return String
 */
@RequiresApi(Build.VERSION_CODES.M)
fun getAdvancedKeyWithNoPaddingAndGCM(keystoreAlias: String): String {
    val genParameterSpec = KeyGenParameterSpec.Builder(keystoreAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    ).apply {
        setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        setKeySize(256)
        setUserAuthenticationRequired(true)
        setUserAuthenticationValidityDurationSeconds(30)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setUnlockedDeviceRequired(true)
        }
    }.build()
    return MasterKeys.getOrCreate(genParameterSpec)
}

/**
 * Generates sign or verification key Pairs with sha256 and sha512
 * @param keystoreAlias String
 * @return KeyPair?
 */
@RequiresApi(Build.VERSION_CODES.M)
fun keyPairGenerator(keystoreAlias: String, provider:String = "AndroidKeyStore"): KeyPair? {
    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_EC,
            provider
    )

    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(keystoreAlias,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
    ).run {
        setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
        build()
    }
    kpg.initialize(parameterSpec)

    return kpg.generateKeyPair()
}


/**
 * Generates more advanced key pairs
 * @param keystoreAlias String can be [aes256KeyAlias] if you don't have any in mind
 * @param provider String usually android key store
 * @param keyProperties String [KeyProperties]
 * @param purposes Int [KeyProperties]
 * @param keygenCallback [@kotlin.ExtensionFunctionType] Function1<Builder, Unit>
 * @return KeyPair?
 */
@RequiresApi(Build.VERSION_CODES.M)
fun keyPairGenerator(keystoreAlias: String, provider:String = "AndroidKeyStore", keyProperties:String, purposes:Int, keygenCallback: KeyGenParameterSpec.Builder.() -> Unit = {}): KeyPair? {
    val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(keyProperties, provider)
    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(keystoreAlias, purposes).run {
        keygenCallback()
        build()
    }
    kpg.initialize(parameterSpec)
    return kpg.generateKeyPair()
}

/**
 * Encrypts the shared preferences that are returned to you
 * @receiver Context
 * @param myMasterKeyAlias String or [aes256KeyAlias] by default
 * @param prefsName String
 * @return SharedPreferences
 */
@RequiresApi(Build.VERSION_CODES.M)
fun Context.encryptedSharedPreferences(myMasterKeyAlias:String = aes256KeyAlias, prefsName:String): SharedPreferences {
    return EncryptedSharedPreferences.create(
            prefsName,
            myMasterKeyAlias,
            this,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, //for encrypting Keys
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM ////for encrypting Values
    )
}

// private funs
@RequiresApi(Build.VERSION_CODES.M)
private fun Context.buildEncryptedFile(file: File, keyAlias: String? = null): EncryptedFile {
    return EncryptedFile.Builder(
            file,
            this,
            keyAlias ?: aes256KeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB).build()
}