package com.example.dictonaryapp

data class DictionaryResult(
    val word: String,
    val phonetic: String?,
    val phonetics: List<Phonetic>?,
    val origin: String?,
    val meanings: List<Meaning> = emptyList()
)

data class Phonetic(
    val text: String,
    val audio: String? = null // Optional field for phonetic audio
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)
