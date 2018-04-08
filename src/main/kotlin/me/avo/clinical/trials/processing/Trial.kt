package me.avo.clinical.trials.processing

data class Trial(
    val id: String,
    val keywords: List<String>,
    val title: String,
    val summary: String,
    val conditions: List<String>,
    val more: String? = null
)