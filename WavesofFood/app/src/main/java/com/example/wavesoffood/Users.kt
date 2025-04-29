package com.example.wavesoffood

data class Users(val id: Int, val name: String, val mobile: String, val address:String, val email:String, val password:String)
//for database we need to create a kotlin class/ file with dataclass // frist create Users kotlic class then create UsersDatabaseHelper class and then done in signup activity with binding