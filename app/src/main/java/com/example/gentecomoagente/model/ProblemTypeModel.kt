package com.example.gentecomoagente.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class ProblemTypeModel(
    @get:Exclude @set:Exclude
    var id: String = "",

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String = "",

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = ""
)
