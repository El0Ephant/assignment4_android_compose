package mmcs.assignment4_compose.viewmodel

import kotlinx.coroutines.flow.StateFlow

enum class Operation(val str: String) {
    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    PERCENT("%")
}

interface Calculator {
    // Observable display, should be referenced in layout
    var valueDisplay: StateFlow<String>
    var operationDisplay: StateFlow<String>

    // Add new digit to current input and update display
    fun addDigit(dig: Int)

    // Add a floating point to current input and update display
    fun addPoint()

    fun backspace()

    // Add new operation into stack:
    //     perform a previously stored operation and update display
    //     start a new input
    fun addOperation(op: Operation)

    fun toggleSign()

    // Compute current operation and display a result
    fun compute()

    // Clear input
    fun clear()

    // Clear entire calculator state
    fun reset()
}