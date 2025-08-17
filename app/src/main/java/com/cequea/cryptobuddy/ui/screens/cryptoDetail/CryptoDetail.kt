package com.cequea.cryptobuddy.ui.screens.cryptoDetail

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cequea.cryptobuddy.domain.model.Crypto
import com.cequea.cryptobuddy.domain.model.getDummyCrypto
import com.cequea.cryptobuddy.ui.theme.CryptoBuddyTheme
import com.cequea.cryptobuddy.utils.getBalanceFormated
import com.cequea.cryptobuddy.utils.getCompatNumber
import com.cequea.cryptobuddy.utils.getDateFormatted
import org.koin.compose.viewmodel.koinViewModel
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun CryptoDetailRoot(
    cryptoId: Int,
    navController: NavController
) {
    val viewModel: CryptoDetailViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCryptoById(cryptoId)
    }

    CryptoDetailScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CryptoDetailScreen(
    state: CryptoDetailState,
    onAction: (CryptoDetailAction) -> Unit,
) {
    if (state.crypto == null) return
    val crypto = state.crypto
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        Text(
            text = "${crypto.symbol}/USD",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W600
            ),
        )

        Text(
            text = crypto.quote.USD.price.getBalanceFormated("$"),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W700
            ),
        )

        val leadingPercentageColor = remember { mutableStateOf(Color(0xFF9E9E9E)) }
        val leadingPercentageText = remember { mutableStateOf("") }
        when {
            crypto.quote.USD.percentChange24h > 0 -> {
                leadingPercentageColor.value = Color(0xFF1E9F4E)
                leadingPercentageText.value = "+"
            }

            crypto.quote.USD.percentChange24h < 0 -> {
                leadingPercentageColor.value = Color(0xFFf05252)
            }
        }
        Text(
            text = buildAnnotatedString {
                append("24h change ")
                withStyle(
                    style = SpanStyle(
                        color = leadingPercentageColor.value,
                        fontWeight = FontWeight.W700
                    )
                ) {
                    append(
                        crypto.quote.USD.percentChange24h.getBalanceFormated(
                            leadingText = leadingPercentageText.value,
                            trailingText = "%"
                        )
                    )
                }
            },
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        LineCurrencyChart(crypto)
        Spacer(modifier = Modifier.height(16.dp))


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Last updated: ${crypto.lastUpdated.getDateFormatted()}"
            )
            val numberFormat = NumberFormat.getInstance(Locale.getDefault())
            numberFormat.maximumFractionDigits = 8
            Row {
                Text(
                    text = "Market Cap: "
                )

                Column {
                    Text(
                        text = "$${numberFormat.format(crypto.quote.USD.marketCap)}"
                    )
                    Text(
                        text = "(${crypto.quote.USD.marketCap.getCompatNumber("$")})",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Row {
                Text(
                    text = "Volume 24h: "
                )

                Column {
                    Text(
                        text = "$${numberFormat.format(crypto.quote.USD.volume24h)}"
                    )
                    Text(
                        text = "(${crypto.quote.USD.volume24h.getCompatNumber("$")})",
                        fontWeight = FontWeight.Medium
                    )
                }

            }


            Text(
                text = "Circulating Supply: ${numberFormat.format(crypto.circulatingSupply)} ${crypto.symbol}"
            )
        }


    }
}

@Composable
private fun LineCurrencyChart(crypto: Crypto, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(

        )
    ) {
        val bitcoinChanges = listOf(
            TimeChange("1h", crypto.quote.USD.percentChange1h.toFloat()),
            TimeChange("24h", crypto.quote.USD.percentChange24h.toFloat()),
            TimeChange("7d", crypto.quote.USD.percentChange7d.toFloat()),
            TimeChange("30d", crypto.quote.USD.percentChange30d.toFloat()),
            TimeChange("60d", crypto.quote.USD.percentChange60d.toFloat()),
            TimeChange("90d", crypto.quote.USD.percentChange90d.toFloat())
        )

        DivergingBarChartScreen(bitcoinChanges)
    }
}


data class TimeChange(val label: String, val value: Float)

private val bitcoinChanges = listOf(
    TimeChange("1h", -0.08524333f),
    TimeChange("24h", -0.8051f),
    TimeChange("7d", -3.24912748f),
    TimeChange("30d", 4.48054914f),
    TimeChange("60d", 9.30505591f),
    TimeChange("90d", 17.37225529f)
)

@Composable
fun DivergingBarChartScreen(
    data: List<TimeChange>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        DivergingBarChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun DivergingBarChart(
    data: List<TimeChange>,
    modifier: Modifier = Modifier,
    barHeightDp: Float = 24f,
    barSpacingDp: Float = 16f,
    paddingDp: Float = 24f
) {
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    Canvas(modifier = modifier) {
        val padding = paddingDp.dp.toPx()
        val barHeight = barHeightDp.dp.toPx()
        val barSpacing = barSpacingDp.dp.toPx()

        val chartWidth = size.width - padding * 2
        val centerX = padding + chartWidth / 2

        // find the largest absolute value for scaling
        val maxAbs = data.maxOf { abs(it.value) }.takeIf { it > 0f } ?: 1f

        // zero‐line vertical axis
        drawLine(
            color = Color.Gray,
            start = Offset(centerX, padding / 2),
            end = Offset(centerX, size.height - padding / 2),
            strokeWidth = 2f
        )

        data.forEachIndexed { index, item ->
            val y = padding + index * (barHeight + barSpacing)

            // compute bar length
            val barLength = (abs(item.value) / maxAbs) * (chartWidth / 2)
            val isPositive = item.value >= 0f

            // determine bar start X
            val startX = if (isPositive) centerX else centerX - barLength

            // choose color
            val barColor = if (isPositive) Color(0xFF4CAF50) else Color(0xFFF44336)

            // draw the bar
            drawRect(
                color = barColor,
                topLeft = Offset(startX, y),
                size = Size(barLength, barHeight)
            )

            // Prepare Paint for text
            val labelPaint = Paint().apply {
                color = textColor.toArgb()
                textSize = 14.sp.toPx()
                textAlign = Paint.Align.RIGHT
            }

            // draw label on left
            drawContext.canvas.nativeCanvas.drawText(
                item.label,
                padding - 8f,
                y + barHeight / 2 + (labelPaint.textSize / 2 - labelPaint.fontMetrics.bottom),
                labelPaint
            )

            // prepare Paint for value
            val valuePaint = Paint().apply {
                color = textColor.toArgb()
                textSize = 14.sp.toPx()
                textAlign = if (isPositive) Paint.Align.LEFT else Paint.Align.RIGHT
            }

            // draw value at end of bar
            val valueX = if (isPositive) startX + barLength + 8f else startX - 8f
            val valueText = String.format("%.2f%%", item.value)
            drawContext.canvas.nativeCanvas.drawText(
                valueText,
                valueX,
                y + barHeight / 2 + (valuePaint.textSize / 2 - valuePaint.fontMetrics.bottom),
                valuePaint
            )
        }
    }
}


// 1. Available time-frame tabs
enum class TimeFrame(val label: String) {
    H1("1h"), H4("4h"), D1("1d"), W1("1w"), M1("1m"), ALL("All")
}

// 2. Sample data—you’ll replace this with your real series
private val sampleData: Map<TimeFrame, List<Float>> = mapOf(
    TimeFrame.H1 to listOf(10f, 12f, 8f, 14f, 11f, 13f, 9f),
    TimeFrame.H4 to listOf(8f, 15f, 9f, 18f, 14f, 17f, 12f),
    TimeFrame.D1 to listOf(12f, 18f, 15f, 20f, 17f, 19f, 16f),
    TimeFrame.W1 to listOf(14f, 22f, 18f, 24f, 20f, 23f, 21f),
    TimeFrame.M1 to listOf(20f, 28f, 25f, 30f, 26f, 29f, 27f),
    TimeFrame.ALL to listOf(5f, 12f, 18f, 25f, 22f, 30f, 28f)
)

// 3. Top-level composable: selector + chart
@Composable
fun TimeSeriesChartDemo(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(TimeFrame.H1) }
    val dataPoints = sampleData[selected] ?: emptyList()

    Column(
        modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SmoothLineChart(
            points = dataPoints,
            xLabels = List(dataPoints.size) { selected.label },
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFFFAFAFA))
        )

        Spacer(Modifier.height(16.dp))

        TimeFrameSelector(
            options = TimeFrame.values().toList(),
            selected = selected,
            onSelected = { selected = it }
        )
    }
}

// 4. The horizontal tab row
@Composable
fun TimeFrameSelector(
    options: List<TimeFrame>,
    selected: TimeFrame,
    onSelected: (TimeFrame) -> Unit
) {
    Row(Modifier.fillMaxWidth()) {
        options.forEach { tf ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clickable { onSelected(tf) }
                    .background(
                        if (tf == selected) MaterialTheme.colorScheme.primary else Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = tf.label,
                    color = if (tf == selected) Color.White else Color.Black,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// 5. A Bézier-smoothed line chart with gradient fill, grid, axes & markers
@Composable
fun SmoothLineChart(
    points: List<Float>,
    xLabels: List<String>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF2979FF),
    gradientStart: Color = lineColor.copy(alpha = 0.4f),
    gradientEnd: Color = lineColor.copy(alpha = 0f),
    gridColor: Color = Color.LightGray,
    axisColor: Color = Color.Gray,
    labelColor: Color = Color.DarkGray
) {
    if (points.isEmpty() || xLabels.size != points.size) return

    Canvas(modifier = modifier) {
        val padding = 48f
        val w = size.width - padding * 2
        val h = size.height - padding * 2

        val minY = points.minOrNull() ?: 0f
        val maxY = points.maxOrNull() ?: 0f
        val yRange = (maxY - minY).takeIf { it > 0f } ?: 1f

        // scale data points into canvas coordinates
        val pts = points.mapIndexed { i, v ->
            val x = padding + (i / (points.size - 1f)) * w
            val y = padding + ((maxY - v) / yRange) * h
            Offset(x, y)
        }

        // build a smooth cubic path
        fun cubicPathOf(pts: List<Offset>): Path {
            val path = Path()
            if (pts.size < 2) return path
            path.moveTo(pts[0].x, pts[0].y)
            for (i in 1 until pts.size) {
                val p0 = pts[i - 1]
                val p1 = pts[i]
                val midX = (p0.x + p1.x) / 2
                path.cubicTo(
                    midX, p0.y,
                    midX, p1.y,
                    p1.x, p1.y
                )
            }
            return path
        }

        val linePath = cubicPathOf(pts)

        // fill area under curve
        val fillPath = Path().apply {
            lineTo(pts.last().x, size.height - padding)
            lineTo(pts.first().x, size.height - padding)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(gradientStart, gradientEnd),
                startY = padding,
                endY = size.height - padding
            )
        )

        // gridlines (4 horizontal + vertical for each point)
        val yStep = h / 4f
        repeat(5) { i ->
            val y = padding + i * yStep
            drawLine(gridColor, Offset(padding, y), Offset(size.width - padding, y), 1f)
        }
        val xStep = w / (pts.size - 1)
        pts.forEach { off ->
            drawLine(gridColor, Offset(off.x, padding), Offset(off.x, size.height - padding), 1f)
        }

        // axes
        drawLine(axisColor, Offset(padding, padding), Offset(padding, size.height - padding), 2f)
        drawLine(
            axisColor,
            Offset(padding, size.height - padding),
            Offset(size.width - padding, size.height - padding),
            2f
        )

        // stroke the smooth line
        drawPath(linePath, lineColor, style = Stroke(width = 4f))

        // draw circle markers
        pts.forEach {
            drawCircle(Color.White, radius = 6f, center = it)
            drawCircle(lineColor, radius = 4f, center = it)
        }

        // X-axis labels
        pts.forEachIndexed { idx, off ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    xLabels[idx],
                    off.x,
                    size.height - padding + 30f,
                    Paint().apply {
                        textSize = 26f
                        textAlign = Paint.Align.CENTER
                        color = labelColor.toArgb()
                    }
                )
            }
        }

        // Y-axis min/max labels
        listOf(minY, maxY).forEachIndexed { i, yv ->
            val y = if (i == 0) size.height - padding else padding
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    yv.toInt().toString(),
                    padding - 12f,
                    y + (if (i == 0) -4f else 8f),
                    Paint().apply {
                        textSize = 24f
                        textAlign = Paint.Align.RIGHT
                        color = labelColor.toArgb()
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    CryptoBuddyTheme {
        CryptoDetailScreen(
            state = CryptoDetailState(
                crypto = getDummyCrypto()
            ),
            onAction = {}
        )
    }
}
