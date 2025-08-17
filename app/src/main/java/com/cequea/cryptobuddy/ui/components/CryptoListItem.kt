package com.cequea.cryptobuddy.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cequea.cryptobuddy.R
import com.cequea.cryptobuddy.domain.model.Crypto
import com.cequea.cryptobuddy.domain.model.getDummyCrypto
import com.cequea.cryptobuddy.ui.theme.CryptoBuddyTheme
import com.cequea.cryptobuddy.utils.getBalanceFormated

@Composable
fun CryptoListItem(
    crypto: Crypto,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoinIcon(
                symbol = crypto.symbol
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = "${crypto.name} (${crypto.symbol})",
                    fontWeight = FontWeight.W500
                )
                Text(text = crypto.quote.USD.price.getBalanceFormated("$"))
            }
        }

        val color = remember { mutableStateOf(Color(0xFF9E9E9E)) }
        val leadingText = remember { mutableStateOf( "")}

        when {
            crypto.quote.USD.percentChange24h > 0 -> {
                color.value = Color(0xFF1E9F4E)
                leadingText.value = "+"
            }
            crypto.quote.USD.percentChange24h < 0 -> {
                color.value = Color(0xFFf05252)
                leadingText.value = ""
            }
        }

        Text(
            text = crypto.quote.USD.percentChange24h.getBalanceFormated(leadingText = leadingText.value, trailingText = "%"),
            color = color.value,
            fontWeight = FontWeight.W500
        )
    }
}

@Composable
private fun CoinIcon(
    symbol: String,
    modifier: Modifier = Modifier
) {
    val iconUrl = "https://cryptoicons.org/api/color/${symbol.lowercase()}/50"

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconUrl)
            .crossfade(true)
            .build(),
        contentDescription = "$symbol Icon",
        modifier = modifier.size(50.dp),
        error = painterResource(R.drawable.bitcoin),
        placeholder = painterResource(R.drawable.bitcoin)
    )
}


@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun CryptoListItemPreview(){
    CryptoBuddyTheme {
        CryptoListItem(
            getDummyCrypto()
        )
    }
}