package com.example.sharkstyle.data.remote.dto

import com.example.sharkstyle.data.local.entities.TallaEntity

data class TallaDto(
    val tallaId: Int,
    val medida: String
)

fun TallaDto.toEntity(): TallaEntity {
    return TallaEntity(
        tallaId = tallaId,
        medida = medida
    )
}
