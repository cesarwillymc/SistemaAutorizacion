package com.summit.sistemaautorizacion.common

object Constants {
    //Api Url
    const val BASE_URL_API="https://comandocovid.herokuapp.com/api/v1/"
    //const val BASE_URL_API="http://18.188.74.142:4000/api/"
    const val BASE_URL_AMAZON_IMG="https://summit-puno.s3.us-east-2.amazonaws.com/"


    //Shared Preferencs TITLE
    const val APP_SETTINGS_FILE="APP_SETTINGS"
    const val PREF_ID_USER="PREF_ID_USER"
    const val PREF_ID_TIPE_ADMIN="admin"
    const val PREF_ID_TIPE_BRIGADA="user"
    //Shared Preferencs Val
    const val PREF_TOKEN="PREF_TOKEN"
    const val PREF_TIME_LASTED="PREF_TIME_LASTED"


    //DD Room
    const val NAME_DATABASE="NAME_DATABASE"
    const val NAME_TABLE_USER="NAME_TABLE_USER"
    const val NAME_POST_LIKE="NAME_POST_LIKE"
    const val NAME_POST="NAME_POST"
    //CLave auth gmail
    const val  TIPE_ADMIN="ADMIN"
    const val TIPE_SUPERVISOR= "SUPERVISOR"

    val profesiones= listOf("Medicina Humana","Enfermería","Nutrición Humana","Odontología","Biología", "Psicología",
        "Trabajo Social","Obstetricia","Tecnología Médica","Farmacia y Bioquímica","Ciencias de la Comunicación Social", "Derecho",
        "Ingeniería Económica","Educación Inicial","Educación Primaria","Educación Secundaria","Ingeniería de Sistemas","Ingenierìa Ambiental")

    val temas= listOf("Todos","Salud","Sintomatología","Tecnología")
}