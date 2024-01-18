package com.funkymuse.security



fun String.md5() = EncryptionUtils.encryptMD5ToString(this)

fun String.sha1() = EncryptionUtils.encryptSHA1ToString(this)

fun String.sha256() = EncryptionUtils.encryptSHA256ToString(this)

fun String.sha512() = EncryptionUtils.encryptSHA512ToString(this)


fun String.md5Hmac(salt: String) = EncryptionUtils.encryptHmacMD5ToString(this, salt)

fun String.sha1Hmac(salt: String) = EncryptionUtils.encryptHmacSHA1ToString(this, salt)

fun String.sha256Hmac(salt: String) = EncryptionUtils.encryptHmacSHA256ToString(this, salt)


fun String.encryptDES(key: String) = EncryptionUtils.encryptDES(this, key)

fun String.decryptDES(key: String) = EncryptionUtils.decryptDES(this, key)

fun String.encryptAESUtils(key: String) = EncryptionUtils.encryptAES(this, key)

fun String.decryptAESUtils(key: String) = EncryptionUtils.decryptAES(this, key)