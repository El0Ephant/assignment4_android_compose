package mmcs.assignment4_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mmcs.assignment4_compose.ButtonType.Action
import mmcs.assignment4_compose.ButtonType.Number
import mmcs.assignment4_compose.ui.theme.Assignment4_composeTheme
import mmcs.assignment4_compose.viewmodel.Operation
import mmcs.assignment4_compose.viewmodel.Calculator
import mmcs.assignment4_compose.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assignment4_composeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen(viewModel)
                    //CalcButton("7", { Log.i("Log", "BUTTON PRESSED")}, )
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen(viewModel: Calculator) {
    val valueDisplay by viewModel.valueDisplay.collectAsState()
    val operationDisplay by viewModel.operationDisplay.collectAsState()
    Column {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        Box(
            modifier = Modifier.size(screenWidth, screenHeight - screenWidth * 5 / 4)
        ) {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                Text(text = valueDisplay, fontSize = 45.sp)
                Box(Modifier.height(5.dp))
                Text(text = operationDisplay, fontSize = 40.sp)
                Box(Modifier.height(5.dp))
            }
        }
        Row {
            CalcButton("С", { viewModel.clear() }, type = ButtonType.Action)
            CalcButton("←", { viewModel.backspace() }, type = ButtonType.Action)
            CalcButton("%", { viewModel.addOperation(Operation.PERCENT) }, type = ButtonType.Operation)
            CalcButton("/", { viewModel.addOperation(Operation.DIVIDE) }, type = ButtonType.Operation)
        }
        Row {
            CalcButton("7", { viewModel.addDigit(7) })
            CalcButton("8", { viewModel.addDigit(8) })
            CalcButton("9", { viewModel.addDigit(9) })
            CalcButton("*", { viewModel.addOperation(Operation.MULTIPLY) }, type = ButtonType.Operation)
        }
        Row {
            CalcButton("4", { viewModel.addDigit(4) })
            CalcButton("5", { viewModel.addDigit(5) })
            CalcButton("6", { viewModel.addDigit(6) })
            CalcButton("-", { viewModel.addOperation(Operation.SUBTRACT) }, type = ButtonType.Operation)
        }
        Row {
            CalcButton("1", { viewModel.addDigit(1) })
            CalcButton("2", { viewModel.addDigit(2) })
            CalcButton("3", { viewModel.addDigit(3) })
            CalcButton("+", { viewModel.addOperation(Operation.ADD) }, type = ButtonType.Operation)
        }
        Row {
            CalcButton("+/-", { viewModel.toggleSign() }, type = ButtonType.Operation)
            CalcButton("0", { viewModel.addDigit(0) })
            CalcButton(".", { viewModel.addPoint() }, type = ButtonType.Operation)
            CalcButton("=", { viewModel.compute() }, type = ButtonType.Action)
        }

    }

}

enum class ButtonType {
    Number,
    Operation,
    Action;
}

@Composable
fun CalcButton(text: String, callback: () -> Unit, type: ButtonType = ButtonType.Number) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    ElevatedButton(
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = when (type) {
                ButtonType.Number -> MaterialTheme.colorScheme.primary
                ButtonType.Operation -> MaterialTheme.colorScheme.secondary
                ButtonType.Action -> MaterialTheme.colorScheme.tertiary
            },
            contentColor = when (type) {
                ButtonType.Number -> MaterialTheme.colorScheme.onPrimary
                ButtonType.Operation -> MaterialTheme.colorScheme.onSecondary
                ButtonType.Action -> MaterialTheme.colorScheme.onTertiary
            },
        ),
        onClick = { callback() },
        modifier = Modifier.size(screenWidth / 4),
        shape = CircleShape,
        enabled = true,
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )

    ) {
        Text(text, fontSize = 18.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    Assignment4_composeTheme {
        CalculatorScreen(CalculatorViewModel())
    }
}