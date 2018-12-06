package me.britton.mud.account.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlayerProfile(val id: String, val uid: String, var email: String)

