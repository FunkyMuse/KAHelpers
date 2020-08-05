package com.crazylegend.kotlinextensions.security

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.crazylegend.kotlinextensions.inputstream.readBytesAndClose
import com.crazylegend.kotlinextensions.inputstream.readTextAndClose
import java.io.File
import java.io.IOException
import java.nio.charset.Charset


/**
 * Created by crazy on 8/5/20 to long live and prosper !
 */


/** Proguard
-keepclassmembers class * extends com.google.protobuf.GeneratedMessageLite {
<fields>;
}
 */

/**
 * Create key using default alias [MasterKey.DEFAULT_MASTER_KEY_ALIAS]
 */
val Context.getMasterKeyDefaultAlias
    get() = MasterKey.Builder(this)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


val Context.getMasterKeyBuilder get() = MasterKey.Builder(this)
fun Context.getMasterKeyBuilder(alias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS) = MasterKey.Builder(this, alias)

fun Context.masterKey(alias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS) {
    MasterKey.Builder(this, alias)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}

@RequiresApi(Build.VERSION_CODES.P)
fun Context.masterKeyStrongBox(alias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS) {
    MasterKey.Builder(this, alias)
            .setRequestStrongBoxBacked(true)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}

@RequiresApi(Build.VERSION_CODES.M)
fun Context.masterKeyCustomSpec(alias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS, keyGenParameterSpec: KeyGenParameterSpec) {
    MasterKey.Builder(this, alias)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
}

fun Context.masterKey(alias: String = MasterKey.DEFAULT_MASTER_KEY_ALIAS, builder: MasterKey.Builder.() -> MasterKey.Builder = { this }) {
    MasterKey.Builder(this, alias)
            .builder()
            .build()
}

fun Context.getEncryptedFile(file: File, masterKey: MasterKey = getMasterKeyDefaultAlias) = EncryptedFile.Builder(this, file, masterKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB).build()

fun EncryptedFile.readBytes(): ByteArray = openFileInput().readBytesAndClose()
fun EncryptedFile.readText(charset: Charset = Charsets.UTF_8) = openFileInput().readTextAndClose(charset)
fun EncryptedFile.reader() = openFileInput().reader()


@Throws(IOException::class)
fun Context.encryptFile(file: File, masterKey: MasterKey = getMasterKeyDefaultAlias, fileContent: ByteArray) {
    getEncryptedFile(file, masterKey).openFileOutput().apply {
        write(fileContent)
        flush()
        close()
    }
}

@Throws(IOException::class)
fun Context.encryptFileSafely(file: File, masterKey: MasterKey = getMasterKeyDefaultAlias, fileContent: ByteArray) {
    if (file.exists()) file.delete()

    getEncryptedFile(file, masterKey).openFileOutput().apply {
        write(fileContent)
        flush()
        close()
    }
}

const val ENCRYPTED_PREFS_DEFAULT_NAME = "_encryptedPrefsDefaultFileName_"
fun Context.encryptedSharedPreferences(fileName: String = ENCRYPTED_PREFS_DEFAULT_NAME, masterKey: MasterKey = getMasterKeyDefaultAlias) =
        EncryptedSharedPreferences.create(this, fileName, masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

