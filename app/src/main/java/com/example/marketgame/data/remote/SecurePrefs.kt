package com.example.marketgame.data.remote

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Single source of truth for securely storing auth data.
 *
 * Uses an AES-256-GCM key held by the Android Keystore so tokens are never
 * stored in plain text on the device. Ciphertext (IV + tag) is Base64-encoded
 * in regular preferences. Falls back to plain prefs only when the Keystore is
 * unavailable (e.g. some unit-test environments).
 *
 * Replaces the deprecated androidx.security:security-crypto wrapper with the
 * same [SharedPreferences] surface.
 */
object SecurePrefs {

    private const val PREFS_NAME = "secure_auth_prefs"
    private const val KEY_ALIAS = "market_auth_keystore_v1"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_LENGTH_BYTES = 12
    private const val TAG_LENGTH_BITS = 128

    fun open(context: Context): SharedPreferences {
        return try {
            SecureSharedPreferences(
                delegate = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE),
                key = getOrCreateKey(),
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        generator.init(
            KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
        )
        return generator.generateKey()
    }

    private fun encrypt(key: SecretKey, plain: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
    }

    private fun decrypt(key: SecretKey, raw: String): String {
        val data = Base64.decode(raw, Base64.NO_WRAP)
        val iv = data.copyOfRange(0, IV_LENGTH_BYTES)
        val encrypted = data.copyOfRange(IV_LENGTH_BYTES, data.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH_BITS, iv))
        return String(cipher.doFinal(encrypted), Charsets.UTF_8)
    }

    private class SecureSharedPreferences(
        private val delegate: SharedPreferences,
        private val key: SecretKey,
    ) : SharedPreferences {

        override fun getString(name: String, defValue: String?): String? {
            val raw = delegate.getString(name, null) ?: return defValue
            return runCatching { decrypt(key, raw) }.getOrElse { defValue }
        }

        override fun getStringSet(name: String, defValues: MutableSet<String>?): MutableSet<String>? =
            delegate.getStringSet(name, defValues)

        override fun getInt(name: String, defValue: Int): Int = delegate.getInt(name, defValue)
        override fun getLong(name: String, defValue: Long): Long = delegate.getLong(name, defValue)
        override fun getFloat(name: String, defValue: Float): Float = delegate.getFloat(name, defValue)
        override fun getBoolean(name: String, defValue: Boolean): Boolean = delegate.getBoolean(name, defValue)
        override fun contains(name: String): Boolean = delegate.contains(name)
        override fun getAll(): MutableMap<String, Any?> = HashMap(delegate.all)

        override fun edit(): SharedPreferences.Editor = Editor(delegate.edit())

        override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            delegate.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            delegate.unregisterOnSharedPreferenceChangeListener(listener)
        }

        private fun encrypt(plain: String): String {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val iv = cipher.iv
            val encrypted = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(iv + encrypted, Base64.NO_WRAP)
        }

        private inner class Editor(
            private val delegate: SharedPreferences.Editor,
        ) : SharedPreferences.Editor {

            override fun putString(name: String, value: String?): SharedPreferences.Editor {
                if (value == null) {
                    delegate.remove(name)
                    return this
                }
                delegate.putString(name, this@SecureSharedPreferences.encrypt(value))
                return this
            }

            override fun putStringSet(name: String, values: MutableSet<String>?): SharedPreferences.Editor {
                delegate.putStringSet(name, values)
                return this
            }

            override fun putInt(name: String, value: Int): SharedPreferences.Editor = apply { delegate.putInt(name, value) }
            override fun putLong(name: String, value: Long): SharedPreferences.Editor = apply { delegate.putLong(name, value) }
            override fun putFloat(name: String, value: Float): SharedPreferences.Editor = apply { delegate.putFloat(name, value) }
            override fun putBoolean(name: String, value: Boolean): SharedPreferences.Editor = apply { delegate.putBoolean(name, value) }
            override fun remove(name: String): SharedPreferences.Editor = apply { delegate.remove(name) }
            override fun clear(): SharedPreferences.Editor = apply { delegate.clear() }
            override fun commit(): Boolean = delegate.commit()
            override fun apply() = delegate.apply()
        }
    }
}