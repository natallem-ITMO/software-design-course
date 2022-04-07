#include <gtest/gtest.h>
#include "LRUCache.hpp"

const int MAX_CAPACITY = 30;

template<typename K, typename V>
void one_simple_put_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_EQ(cache.get(keys[i]), std::optional<V>(values[i]));
    }
}

template<typename K, typename V>
void one_simple_remove_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.remove(keys[i]));
        EXPECT_EQ(cache.get(keys[i]), std::nullopt);
    }
}

template<typename K, typename V>
void one_simple_get_size_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    EXPECT_EQ(cache.get_size(), 0);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
        EXPECT_EQ(cache.get_size(), i + 1);
    }
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.remove(keys[i]));
        EXPECT_EQ(cache.get(keys[i]), std::nullopt);
        EXPECT_EQ(cache.get_size(), keys.size() - i - 1);
    }
}

template<typename K, typename V>
void one_put_already_existing_element_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values,
                                           const std::vector<V> &replace_values) {
    assert(keys.size() == values.size());
    assert(replace_values.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
        EXPECT_FALSE(cache.put(keys[i], replace_values[i]));
    }
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_EQ(cache.get(keys[i]), std::optional<V>(values[i]));
    }
}

template<typename K, typename V>
void one_remove_already_removed_element_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.remove(keys[i]));
        EXPECT_EQ(cache.get(keys[i]), std::nullopt);
        EXPECT_FALSE(cache.remove(keys[i]));
    }
}

template<typename K, typename V>
void one_remove_not_existing_element_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values,
                                          const std::vector<K> &not_existing_keys) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < not_existing_keys.size(); ++i) {
        EXPECT_FALSE(cache.remove(not_existing_keys[i]));
    }
}

template<typename K, typename V>
void one_put_overflow_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < capacity; ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < capacity; ++i) {
        EXPECT_EQ(cache.get(keys[i]), std::optional<V>(values[i]));
    }
    for (int i = 0; i < capacity; ++i) {
        EXPECT_TRUE(cache.put(keys[i + capacity], values[i + capacity]));
        EXPECT_EQ(cache.get(keys[i]), std::nullopt);
        EXPECT_EQ(cache.get(keys[i + capacity]), std::optional<V>(values[i + capacity]));
    }
}

template<typename K, typename V>
void one_remove_overflowed_element_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < keys.size() - capacity; ++i) {
        EXPECT_FALSE(cache.remove(keys[i]));
    }
}

template<typename K, typename V>
void one_get_size_overflow_test(int capacity, const std::vector<K> &keys, const std::vector<V> &values) {
    assert(keys.size() == values.size());
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
        EXPECT_EQ(cache.get_size(), std::min(capacity, i + 1));
    }
    for (int i = 0; i < capacity; ++i) {
        EXPECT_TRUE(cache.remove(keys[keys.size() - 1 - i]));
        EXPECT_EQ(cache.get_size(), capacity - i - 1);
    }
}


template<typename K, typename V>
void
one_LRU_delete_rule_test(int capacity, std::vector<K> &keys, std::vector<K> &additional_keys,
                         std::vector<K> &recalled_keys, std::vector<V> &values, std::vector<V> &additional_values) {
    LRUCache<K, V> cache(capacity);
    for (int i = 0; i < keys.size(); ++i) {
        EXPECT_TRUE(cache.put(keys[i], values[i]));
    }
    for (int i = 0; i < recalled_keys.size(); ++i) {
        EXPECT_EQ(cache.get(recalled_keys[i]), std::optional<V>(values[i]));
    }
    for (int i = 0; i < additional_keys.size(); ++i) {
        EXPECT_TRUE(cache.put(additional_keys[i], additional_values[i]));
    }
    for (int i = 0; i < recalled_keys.size(); ++i) {
        EXPECT_EQ(cache.get(recalled_keys[i]), std::optional<V>(values[i]));
    }
    for (int i = 0; i < additional_keys.size(); ++i) {
        EXPECT_EQ(cache.get(additional_keys[i]), std::optional<V>(additional_values[i]));
    }
}

TEST(LRUCache_put_operation, simple_put_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<std::string> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_simple_put_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<char> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'a' + i;
        });
        one_simple_put_test(capacity, keys, values);
    }
}

TEST(LRUCache_put_operation, put_already_existing_element_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<std::string> values(capacity);
        std::vector<std::string> replace_values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        std::generate(replace_values.begin(), replace_values.end(), []() {
            static int i = 1;
            return "replace_value_for_key_" + std::to_string(++i);
        });
        one_put_already_existing_element_test(capacity, keys, values, replace_values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<char> values(capacity);
        std::vector<char> replace_values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'v' + i;
        });
        std::generate(replace_values.begin(), replace_values.end(), []() {
            static int i = 1;
            return 'r' + i;
        });
        one_put_already_existing_element_test(capacity, keys, values, replace_values);
    }
}

TEST(LRUCache_put_operation, put_overflow_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity * 2);
        std::vector<std::string> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_put_overflow_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity * 2);
        std::vector<char> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'v' + i;
        });
        one_put_overflow_test(capacity, keys, values);
    }
}

TEST(LRUCache_remove_operation, simple_remove_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<std::string> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_simple_remove_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<char> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'a' + i;
        });
        one_simple_remove_test(capacity, keys, values);
    }
}

TEST(LRUCache_remove_operation, remove_already_removed_element_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<std::string> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_remove_already_removed_element_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<char> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'a' + i;
        });
        one_remove_already_removed_element_test(capacity, keys, values);
    }
}

TEST(LRUCache_remove_operation, remove_overflowed_element_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity * 2);
        std::vector<std::string> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_remove_overflowed_element_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity * 2);
        std::vector<char> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'v' + i;
        });
        one_remove_overflowed_element_test(capacity, keys, values);
    }
}

TEST(LRUCache_remove_operation, remove_not_existing_element_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<int> not_existing_keys(capacity);
        std::vector<std::string> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        std::generate(not_existing_keys.begin(), not_existing_keys.end(), []() {
            static int i = -1;
            return --i;
        });
        one_remove_not_existing_element_test(capacity, keys, values, not_existing_keys);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<long> not_existing_keys(capacity);
        std::vector<char> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'a' + i;
        });
        std::generate(not_existing_keys.begin(), not_existing_keys.end(), []() {
            static long i = -1;
            return --i;
        });
        one_remove_not_existing_element_test(capacity, keys, values, not_existing_keys);
    }
}

TEST(LRUCache_get_size_operation, simple_get_size_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity);
        std::vector<std::string> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_simple_get_size_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity);
        std::vector<char> values(capacity);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'a' + i;
        });
        one_simple_get_size_test(capacity, keys, values);
    }
}

TEST(LRUCache_get_size_operation, get_size_overflow_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<int> keys(capacity * 2);
        std::vector<std::string> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static int i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return "value_for_key_" + std::to_string(++i);
        });
        one_get_size_overflow_test(capacity, keys, values);
    }

    for (int capacity = 1; capacity <= MAX_CAPACITY; ++capacity) {
        std::vector<long> keys(capacity * 2);
        std::vector<char> values(capacity * 2);
        std::generate(keys.begin(), keys.end(), []() {
            static long i = 1;
            return ++i;
        });
        std::generate(values.begin(), values.end(), []() {
            static int i = 1;
            return 'v' + i;
        });
        one_get_size_overflow_test(capacity, keys, values);
    }
}

TEST(LRUCache_LRU_rule, LRU_delete_rule_test) {
    for (int capacity = 1; capacity <= MAX_CAPACITY / 2; ++capacity) {
        std::vector<int> keys(capacity * 2);
        std::vector<int> additional_keys(capacity);
        std::vector<int> recalled_keys(capacity);
        std::vector<std::string> values(capacity * 2);
        std::vector<std::string> additional_values(capacity);
        int next_index = keys.size() + 1;
        int begin_index = 1;
        std::generate(keys.begin(), keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = 1;
        std::generate(recalled_keys.begin(), recalled_keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = next_index;
        std::generate(additional_keys.begin(), additional_keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = 1;
        std::generate(values.begin(), values.end(), [&begin_index]() {
            return "value_for_key_" + std::to_string(++begin_index);
        });
        begin_index = next_index;
        std::generate(additional_values.begin(), additional_values.end(), [&begin_index]() {
            return "value_for_key_" + std::to_string(++begin_index);
        });
        one_LRU_delete_rule_test(capacity * 2, keys, additional_keys, recalled_keys, values, additional_values);
    }
    for (int capacity = 1; capacity <= MAX_CAPACITY / 2; ++capacity) {
        std::vector<long> keys(capacity * 2);
        std::vector<long> additional_keys(capacity);
        std::vector<long> recalled_keys(capacity);
        std::vector<char> values(capacity * 2);
        std::vector<char> additional_values(capacity);
        long next_index = keys.size() + 1;
        long begin_index = 1;
        std::generate(keys.begin(), keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = 1;
        std::generate(recalled_keys.begin(), recalled_keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = next_index;
        std::generate(additional_keys.begin(), additional_keys.end(), [&begin_index]() {
            return ++begin_index;
        });
        begin_index = 1;
        std::generate(values.begin(), values.end(), [&begin_index]() {
            return 'a' + ++begin_index;
        });
        begin_index = next_index;
        std::generate(additional_values.begin(), additional_values.end(), [&begin_index]() {
            return 'a' + ++begin_index;
        });
        one_LRU_delete_rule_test(capacity * 2, keys, additional_keys, recalled_keys, values, additional_values);
    }
}

int main(int argc, char **argv) {
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}