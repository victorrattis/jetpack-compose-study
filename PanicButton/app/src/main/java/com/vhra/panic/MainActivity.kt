package com.vhra.panic

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vhra.panic.ui.theme.PanicButtonTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MyViewModel = viewModel()
            PanicButtonTheme {
                Log.d("devlog", "ComposeTheme")
                val isActivated: Boolean = viewModel.isActivated.collectAsState().value
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = if (isActivated) Color(0xFF33303D) else MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val viewModel = viewModel<MyViewModel>()

    Log.d("devlog", "Greeting")
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Log.d("devlog", "Greeting.Column")
        val isActivated = viewModel.isActivated.collectAsState().value

        PanicButton(
            "Pânico",
            canvasSize = 280f,
            disableClick = isActivated,
            backgroundColor = if (isActivated) Color(0xFF612F43) else Color(0xFFEEEEEE)
        ) {
            viewModel.clickToActive()
        }
        Spacer(modifier = Modifier.padding(12.dp))
        if (isActivated) {
            Text("EMERGÊNCIA ATIVADA", style = TextStyle(color = Color.White))
            Text("As Autoridades estão sendo Contactadas com Urgência", style = TextStyle(color = Color.White))
        } else {
            Text("EMERGÊNCIA")
            ClickToActivateText(viewModel.clicksToActivate.collectAsState())
        }
    }
}

@Composable
fun ClickToActivateText(state: State<Int>) {
    Text("Toque ${state.value} Vezes para ativar")
}


@OptIn(ExperimentalTextApi::class)
@Composable
fun PanicButton(
    text: String,
    duration: Int = 200,
    canvasSize: Float = 280f,
    backgroundColor: Color,
    disableClick: Boolean = false,
    onClickCallback: () -> Unit = {}
) {
    val animateFloat = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val textMeasure = rememberTextMeasurer()

    val textStyle = TextStyle(
        color = Color.White,
        fontSize = 32.sp,
    )

    val textSize = textMeasure.measure(text = AnnotatedString(text), style = textStyle).size
    val halfDuration = duration / 2
    Canvas(
        modifier = Modifier
            .clickable(
                enabled = !disableClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                coroutineScope.launch {
                    onClickCallback()
                    animateFloat.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = halfDuration, easing = LinearEasing)
                    )
                    animateFloat.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = halfDuration, easing = LinearEasing)
                    )
                }
            }
            .size(canvasSize.dp),

        onDraw = {
            val circleRadius = canvasSize / 2f
            val highlightRadius = canvasSize * 0.3 / 2f
            val smallCircleRadius = (canvasSize / 2f) - highlightRadius

            drawCircle(
                radius = circleRadius.dp.toPx(),
                color = backgroundColor
            )

            drawCircle(
                radius = (smallCircleRadius + (highlightRadius * animateFloat.value)).dp.toPx(),
                color = Color(0x8CEC2D4F)
            )

            drawCircle(
                radius = smallCircleRadius.dp.toPx(),
                color = Color(0xFFEC2D4F)
            )

            drawText(
                textMeasurer = textMeasure,
                text = text,
                style = textStyle,
                topLeft = Offset(
                    (size.width - textSize.width) / 2f,
                    (size.height - textSize.height) / 2f
                )
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PanicButtonTheme {
        Greeting("Android")
    }
}