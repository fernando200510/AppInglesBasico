package org.fernandoblanco.inglesbasico.ui.kid

private val AVATARES = listOf(
    "🦊", "🐼", "🦁", "🐸", "🦄", "🐵", "🐻", "🐨", "🐯", "🐶",
    "🐱", "🐰", "🐹", "🦉", "🦋", "🐝", "🦖", "🐙", "🦀", "🐧"
)

fun avatarEmojiParaUsuario(id: Long): String =
    AVATARES[(kotlin.math.abs(id) % AVATARES.size).toInt()]
