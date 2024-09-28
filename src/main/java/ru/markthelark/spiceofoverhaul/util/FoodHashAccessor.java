package ru.markthelark.spiceofoverhaul.util;

import java.util.HashMap;
import java.util.LinkedList;

public interface FoodHashAccessor {

    HashMap<String, Integer> getFoodHash();
    LinkedList<String> getFoodQueue();
    String getFoodQueueString();
    int getFoodHistory();
    void setFoodQueue(String saveFoodQueue);
}
