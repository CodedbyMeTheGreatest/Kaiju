package com.kaiju.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kaiju.app.remote.PokemonDto

@Composable
fun TarjetaFichaPokemon(pokemon: PokemonDto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = pokemon.sprites.frontDefault,
                contentDescription = pokemon.name,
                modifier = Modifier.size(80.dp)
            )
            Column {
                Text(text = "#${pokemon.id} ${pokemon.name.uppercase()}", fontWeight = FontWeight.Bold)
                Text(text = "Altura: ${pokemon.height / 10.0} m")
                Text(text = "Tipos: ${pokemon.types.joinToString { it.type.name }}")
            }
        }
    }
}