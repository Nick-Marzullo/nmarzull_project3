package com.example.nmarzull_project3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var input = LinkedList<String>()
    lateinit var tray: String
    var decimal: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tray = findViewById<EditText>(R.id.NumberField).text.toString()
    }

    fun inputNumber(view: View) {
        if (tray.equals("Enter Number") || tray.equals("Thou Fool!") || tray.equals("0")) {
            if (view.tag.equals(".")) {
                tray = "0."
                decimal = true
            } else {
                tray = view.tag as String
            }
        } else {
            if (view.tag.equals(".")) { // do not allow two decimal points
                if (decimal) return
                else decimal = true
            }
            tray += view.tag as String
        }
        updateTextFields()
    }

    fun clearTray(view:View) {
        tray = "0"
        updateTextFields()
    }

    fun clearAll(view:View) {
        input.clear()
        tray = "0"
        updateTextFields()
    }

    fun negate(view:View) {
        if (!(tray.equals("Enter Number") && tray.equals("Thou Fool!"))) {
            if (tray[0] != '-') {
                tray = "-" + tray
            } else {
                tray = tray.removePrefix("-")
            }
            updateTextFields()
        }
    }

    fun updateTextFields(){
        val numberField = findViewById<EditText>(R.id.NumberField)
        numberField.text = Editable.Factory.getInstance().newEditable(tray)
        val inputField = findViewById<EditText>(R.id.OldInput)
        var fullInput: String = ""
        for (i in input) fullInput += i
        inputField.text = Editable.Factory.getInstance().newEditable(fullInput)

    }

    fun add(view: View) {
        if (!(tray.equals("Enter Number")) && !(tray.equals("Thou Fool!"))) {
            input.add(tray)
            input.add("+")
            tray = "0"
            updateTextFields()
        }
    }

    fun subtract(view: View) {
        if (!(tray.equals("Enter Number")) && !(tray.equals("Thou Fool!"))) {
            input.add(tray)
            input.add("-")
            tray = "0"
            updateTextFields()
        }
    }

    fun multiply(view: View) {
        if (!(tray.equals("Enter Number")) && !(tray.equals("Thou Fool!"))) {
            input.add(tray)
            input.add("*")
            tray = "0"
            updateTextFields()
        }
    }

    fun divide(view: View) {
        if (!(tray.equals("Enter Number")) && !(tray.equals("Thou Fool!"))) {
            input.add(tray)
            input.add("/")
            tray = "0"
            updateTextFields()
        }
    }

    fun evaluate(view: View) {
        if ((tray.equals("Enter Number")) || (tray.equals("Thou Fool!"))) return
        input.add(tray)
        //Multiplication and Division from left to right
        var i:Int = 0
        while(i<input.size) {
            if (input[i].equals("*")) {
                var temp = input[i-1].toDouble() * input[i+1].toDouble()
                input[i-1] = temp.toString() //put result as a string in place of the multiplicand
                input.removeAt(i) // remove multiplication sign
                input.removeAt(i) // remove multiplier, i is now pointing to the next number
                continue // without updating i
            } else if (input[i].equals("/")) {
                if (input[i + 1].toDouble() != 0.0) { // if it's safe to divide
                    var temp = input[i - 1].toDouble() / input[i + 1].toDouble()
                    input[i - 1] = temp.toString() //put result as a string in place of the dividend
                    input.removeAt(i) // remove division sign
                    input.removeAt(i) // remove divisor, i is now pointing to the next number
                    continue // without updating i
                } else { // User attempted to divide by zero
                    input.clear() // clear all previous numbers, because losing all progress is fun
                    tray = "Thou Fool!" // give polite, helpful error message
                    updateTextFields() // show the user what we really think of him/her
                    return //get out of this function
                }
            }
            i++
        }
        //Addition and subtraction from left to right
        i = 0
        while(i<input.size) {
            if (input[i].equals("+")) {
                var temp = input[i-1].toDouble() + input[i+1].toDouble()
                input[i-1] = temp.toString() //put result as a string in place of the first addend
                input.removeAt(i) // remove plus sign
                input.removeAt(i) // remove second addend, i is now pointing to the next number
                continue // without updating i
            } else if (input[i].equals("-")) {
                var temp = input[i-1].toDouble() - input[i+1].toDouble()
                input[i-1] = temp.toString() //put result as a string in place of the minuend
                input.removeAt(i) // remove minus sign
                input.removeAt(i) // remove subtrahend, i is now pointing to the next number
                continue // without updating i
            }
            i++
        }

        tray = input[0]
        input.clear()
        when {
            tray.equals("0.0") -> tray = "0"
        } //removing the .0 when the result is 0 helps the user because of how user-input handles 0s
        updateTextFields()
    }
}
