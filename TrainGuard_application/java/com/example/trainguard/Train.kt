package com.example.trainguard

class Train {
    var id = 0
    var name = "Поезд"
    var locationLatitude = 0.0
    var locationLongtitude = 0.0

    constructor(id: Int, name: String, locationLatitude: Double, locationLongtitude: Double) {
        this.id = id
        this.name = name
        this.locationLatitude = locationLatitude
        this.locationLongtitude = locationLongtitude
    }
}