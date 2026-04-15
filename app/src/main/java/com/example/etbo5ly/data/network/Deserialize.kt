package com.example.etbo5ly.data.network

import com.example.etbo5ly.data.dto.MealX
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class Deserialize: JsonDeserializer<MealX> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): MealX {
        val jsonObject = json.asJsonObject
        val ingredientsList = mutableListOf<Pair<String, String>>()

        for (i in 1..20) {
            val ingredient = jsonObject.get("strIngredient$i")?.takeIf { !it.isJsonNull }?.asString
            val measure = jsonObject.get("strMeasure$i")?.takeIf { !it.isJsonNull }?.asString

            if (!ingredient.isNullOrBlank()) {
                ingredientsList.add(ingredient to (measure ?: ""))
            }
        }

        return MealX(
            idMeal = jsonObject.get("idMeal").asString,
            strMeal = jsonObject.get("strMeal").asString,
            strCategory = jsonObject.get("strCategory").asString,
            strArea = jsonObject.get("strArea").asString,
            strInstructions = jsonObject.get("strInstructions").asString,
            strMealThumb = jsonObject.get("strMealThumb").asString,
            strTags = jsonObject.get("strTags")?.takeIf { !it.isJsonNull }?.asString ?: "",
            strYoutube = jsonObject.get("strYoutube")?.takeIf { !it.isJsonNull }?.asString ?: "",
            ingredients = ingredientsList,
        )
    }
}