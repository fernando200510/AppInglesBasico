package org.fernandoblanco.inglesbasico.data

data class Companero(
    val id: String,
    val nombre: String,
    val emoji: String,
    val colorFondo: String,
    val frasesBienvenida: List<String>,
    val frasesAcierto: List<String>,
    val frasesFallo: List<String>,
    val frasesRacha: List<String>,
    val frasesInicio: List<String>
)

object CompaneroData {

    val todos = listOf(
        Companero(
            id = "zorro",
            nombre = "Kiko",
            emoji = "🦊",
            colorFondo = "#FFF3E0",
            frasesBienvenida = listOf(
                "¡Hola! Soy Kiko. ¡Hoy vamos a aprender inglés juntos!",
                "¡De vuelta! Me alegra verte. ¿Listo para la aventura?"
            ),
            frasesAcierto = listOf(
                "¡Eso es! ¡Eres increíble!",
                "¡Sabía que podías! ¡Sigue así!",
                "¡Wiii! ¡Correcto! ¡Somos un gran equipo!"
            ),
            frasesFallo = listOf(
                "¡Casi! No te rindas, juntos lo lograremos.",
                "Esa no era, pero aprendemos del error. ¡Vamos!"
            ),
            frasesRacha = listOf(
                "¡{n} días seguidos! ¡Eres una máquina!",
                "¡Racha de {n}! ¡El bosque entero te aplaude!"
            ),
            frasesInicio = listOf(
                "¿Qué aventura elegimos hoy?",
                "¡El inglés nos espera! ¿Empezamos?"
            )
        ),
        Companero(
            id = "buho",
            nombre = "Sabio",
            emoji = "🦉",
            colorFondo = "#EDE7F6",
            frasesBienvenida = listOf(
                "¡Buenas! Soy Sabio. El conocimiento nos espera.",
                "¡Volviste! Cada día que aprendes eres más sabio."
            ),
            frasesAcierto = listOf(
                "¡Excelente razonamiento!",
                "¡Lo sabía! Tienes una mente brillante.",
                "¡Perfecto! Así es como se aprende inglés."
            ),
            frasesFallo = listOf(
                "Hmm, no fue esa. Pero el error enseña más que el acierto.",
                "Cerca, cerca. ¡Piensa un poco más y lo tendrás!"
            ),
            frasesRacha = listOf(
                "¡{n} días! La constancia es la clave del saber.",
                "¡Racha de {n}! Los sabios nunca paran de aprender."
            ),
            frasesInicio = listOf(
                "El saber no ocupa lugar. ¿Empezamos?",
                "¡Hoy aprenderemos algo nuevo juntos!"
            )
        ),
        Companero(
            id = "dragon",
            nombre = "Brasa",
            emoji = "🐲",
            colorFondo = "#FCE4EC",
            frasesBienvenida = listOf(
                "¡ROOAR! Soy Brasa. ¡Vamos a conquistar el inglés!",
                "¡Estás de vuelta, guerrero! ¡Yo ya estaba calentando motores!"
            ),
            frasesAcierto = listOf(
                "¡FUEGO! ¡Lo hiciste de maravilla!",
                "¡Eso es! ¡Brasa aprueba!",
                "¡Imparable! ¡Sigamos quemando palabras!"
            ),
            frasesFallo = listOf(
                "¡Grr! No importa, los dragones nunca se rinden.",
                "¡Esa no era, pero tenemos más vidas que un dragón!"
            ),
            frasesRacha = listOf(
                "¡{n} días de fuego! ¡NADIE NOS DETIENE!",
                "¡Racha de {n}! ¡Brasa está orgulloso de ti!"
            ),
            frasesInicio = listOf(
                "¡El castillo del inglés nos espera! ¿Atacamos?",
                "¡Hoy vencemos nuevas palabras! ¡Adelante!"
            )
        ),
        Companero(
            id = "conejo",
            nombre = "Salto",
            emoji = "🐰",
            colorFondo = "#E8F5E9",
            frasesBienvenida = listOf(
                "¡Buuuu! Soy Salto. ¡Me alegra tanto verte!",
                "¡Saltando de alegría porque volviste! ¡Aprendamos!"
            ),
            frasesAcierto = listOf(
                "¡Salto de felicidad! ¡Lo lograste!",
                "¡Correcto! ¡Eres mi héroe favorito!",
                "¡Yupi! ¡Sabía que lo harías!"
            ),
            frasesFallo = listOf(
                "¡Ayyy! No fue esa, pero tú puedes. ¡Yo creo en ti!",
                "¡No te pongas triste! Los conejitos siempre intentamos de nuevo."
            ),
            frasesRacha = listOf(
                "¡{n} días seguidos! ¡Salto {n} veces de alegría!",
                "¡Racha de {n}! ¡Eres el mejor amigo que Salto podría tener!"
            ),
            frasesInicio = listOf(
                "¡Saltemos juntos al mundo del inglés!",
                "¿Qué aprendemos hoy? ¡Estoy emocionado!"
            )
        ),
        Companero(
            id = "tigre",
            nombre = "Rayo",
            emoji = "🐯",
            colorFondo = "#FFF8E1",
            frasesBienvenida = listOf(
                "¡Grrrr! Soy Rayo. ¡Rápidos como el viento aprendemos inglés!",
                "¡Volviste! Los tigres nunca abandonan la cacería del saber."
            ),
            frasesAcierto = listOf(
                "¡Veloz y certero! ¡Eso es ser como Rayo!",
                "¡Exacto! ¡Ninguna palabra se te escapa!",
                "¡Bien rugido! Digo… ¡bien respondido!"
            ),
            frasesFallo = listOf(
                "¡Los tigres fallamos a veces, pero nunca perdemos el ritmo!",
                "Esa se escapó. ¡Pero Rayo ya la tiene en la mira!"
            ),
            frasesRacha = listOf(
                "¡{n} días! ¡Más rápido que Rayo en la jungla!",
                "¡Racha de {n}! ¡La jungla del inglés ya es tuya!"
            ),
            frasesInicio = listOf(
                "¡La jungla del inglés nos espera! ¡Corramos!",
                "¿Listo para cazar nuevas palabras? ¡Yo sí!"
            )
        )
    )

    fun obtenerPorId(id: String): Companero = todos.find { it.id == id } ?: todos.first()

    fun fraseRacha(companero: Companero, racha: Int): String =
        companero.frasesRacha.random().replace("{n}", racha.toString())
}