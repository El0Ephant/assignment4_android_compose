package mmcs.assignment4_compose.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorViewModel() : ViewModel(), Calculator {
    var _valueDisplay = MutableStateFlow<String>("0")
    override var valueDisplay = _valueDisplay.asStateFlow()

    var _operationDisplay = MutableStateFlow<String>("")
    override var operationDisplay = _operationDisplay.asStateFlow()

    private val operation = """([+\-\*\/])""".toRegex()
    private val number = """(-?\d+(?:\.\d+)?)""".toRegex()
    private val expressionRegex = "$number$operation$number".toRegex()

    private val integer = """(-?\d+)""".toRegex()
    private val pointPossibleRegex = "($integer)|($number$operation$integer)".toRegex()

    private fun _compute(expression: String): Double {
        var expr = expression
        if (expr == "")
            return 0.0

        if (expr.last() == '.' || operation.matches("${expr.last()}"))
            expr = expr.dropLast(1)

        if (number.matches(expr)) {
            return expr.toDouble()
        }

        val match = expressionRegex.matchEntire(expr) ?: throw Exception("compute error")
        val left = match.groupValues[1].toDouble()

        val right = match.groupValues[3].toDouble()

        val result = when (match.groupValues[2]) {
            "+" -> left + right
            "-" -> left - right
            "*" -> left * right
            "/" ->  if (right - 0.0 > 1e-10) left / right else throw IllegalArgumentException("Zero division")
            else -> {throw Exception("Unknown operation")}
        }

        return result
    }

    override fun addDigit(dig: Int) {
        val opValue = _operationDisplay.value ?: return
        _operationDisplay.value = opValue + dig.toString()
    }

    override fun addPoint() {
        val opValue = _operationDisplay.value ?: return

        if (pointPossibleRegex.matches(opValue))
            _operationDisplay.value = "$opValue."
    }

    override fun backspace() {
        val opValue = _operationDisplay.value ?: return
        _operationDisplay.value = opValue.dropLast(1)
    }

    override fun addOperation(op: Operation) {

        val opValue = _operationDisplay.value ?: return

        var newValue: Double;

        try {
            newValue = _compute(opValue)
        }
        catch (e: IllegalArgumentException) {
            _valueDisplay.value = e.message.toString()
            _operationDisplay.value = ""
            return
        }

        if (op == Operation.PERCENT) {
            newValue /= 100
            _valueDisplay.value = newValue.toString()
            _operationDisplay.value = newValue.toString()

            return
        }

        _valueDisplay.value = newValue.toString()
        _operationDisplay.value = newValue.toString() + op.str
    }

    override fun toggleSign() {
        val opValue = _operationDisplay.value ?: return

        val newValue = _compute(opValue)

        _valueDisplay.value = (newValue*-1).toString()
        _operationDisplay.value = (newValue*-1).toString()
    }

    override fun compute() {
        val opValue = _operationDisplay.value ?: return

        val newValue: Double
        try {
            newValue = _compute(opValue)
        }
        catch (e: IllegalArgumentException) {
            _valueDisplay.value = e.message.toString()
            _operationDisplay.value = ""
            return
        }

        _valueDisplay.value = newValue.toString()
        _operationDisplay.value = newValue.toString()
    }

    override fun clear() {
        _operationDisplay.value = ""
        _valueDisplay.value = ""
    }

    override fun reset() {
        _operationDisplay.value = ""
        _valueDisplay.value = ""
    }
}