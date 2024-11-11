package com.example.user_module;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    // Convert List<Integer> to a comma-separated String
    @TypeConverter
    public String fromList(List<Integer> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @TypeConverter
    public List<Integer> toList(String concatenated) {
        return Arrays.stream(concatenated.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
