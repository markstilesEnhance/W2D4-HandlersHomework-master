package com.raywenderlich.mcwenderlich

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import java.util.*

class OrderHandlerThread(private var uiHandler: MainActivity.UiHandler) : HandlerThread("OrderHandlerThread") {

    private var handler: Handler? = null
    private val random = Random()

    private fun convertCurrency(foodPriceInDollars : Float) : Float {
        return foodPriceInDollars * 68.45f
    }

    private fun attachSideOrder(): String {
        val randomOrder = random.nextInt(3)
        return when (randomOrder) {
            0 -> "Chips"
            1 -> "Salad"
            else -> "Nachos"
        }
    }

    private fun getHandler(looper: Looper): Handler {
        return object:Handler(looper) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val foodOrder = msg?.obj as FoodOrder
                foodOrder.foodPrice = convertCurrency(foodOrder.foodPrice)
                foodOrder.sideOrder = attachSideOrder()
                val processedMessage = Message()
                processedMessage.obj = foodOrder
                uiHandler.sendMessage(processedMessage)
            }
        }
    }

    fun sendOrder(foodOrder: FoodOrder) {
        val message = Message()
        message.obj = foodOrder
        handler?.sendMessage(message)
    }

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        handler = getHandler(looper)
    }
}