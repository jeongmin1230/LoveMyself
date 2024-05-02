package com.example.lovemyself.etc

object User {
    var screenPin = ""

    var uid = ""
    var name = ""
    var email = ""
    var password = ""

    fun deleteInfo() {
        uid = ""
        name = ""
        email = ""
        password = ""
    }
}