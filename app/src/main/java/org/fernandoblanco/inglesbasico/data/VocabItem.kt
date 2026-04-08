package org.fernandoblanco.inglesbasico.data

data class VocabItem(
    val emoji: String,
    val en: String,
    val es: String
) {
    val enKey: String get() = en.lowercase()
}
