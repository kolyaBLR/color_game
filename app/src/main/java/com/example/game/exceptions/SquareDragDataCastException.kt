package com.example.game.exceptions

class SquareDragDataCastException: ClassCastException() {

    override val message: String?
        get() = "localState can't be null, and now supported only SquareDragData"
}