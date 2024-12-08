package com.yourcompany.excesseats.data.model

enum class FoodCategory(
    val displayName: String,
    val minWeight: Double,
    val maxWeight: Double
) {
    AMERICAN("American", 0.75, 1.25),
    ASIAN_FUSION("Asian Fusion", 0.75, 1.25),
    BAKERY("Bakery", 0.25, 0.5),
    BARBECUE("Barbecue", 0.75, 1.25),
    BREAKFAST("Breakfast", 0.75, 1.25),
    CHINESE("Chinese", 0.75, 1.25),
    DESSERTS("Desserts", 0.25, 0.5),
    INDIAN("Indian", 0.75, 1.25),
    ITALIAN("Italian", 0.75, 1.25),
    JAPANESE("Japanese", 0.75, 1.25),
    KOREAN("Korean", 0.75, 1.25),
    MEDITERRANEAN("Mediterranean", 0.75, 1.25),
    MEXICAN("Mexican", 0.75, 1.25),
    MIDDLE_EASTERN("Middle Eastern", 0.75, 1.25),
    PIZZA("Pizza", 0.75, 1.25),
    SANDWICHES("Sandwiches", 0.5, 0.75),
    SEAFOOD("Seafood", 0.5, 1.0),
    THAI("Thai", 0.75, 1.25),
    VEGETARIAN("Vegetarian", 1.0, 1.25),
    OTHER("Other", 0.75, 1.5);

    fun getRandomWeight(): Double {
        return minWeight + Math.random() * (maxWeight - minWeight)
    }

    companion object {
        fun fromString(value: String): FoodCategory {
            return values().find { it.displayName == value }
                ?: throw IllegalArgumentException("Unknown food category: $value")
        }
    }
}
