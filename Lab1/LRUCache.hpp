
#ifndef LRU_CACHE_LRUCACHE_HPP
#define LRU_CACHE_LRUCACHE_HPP

#include <cstddef>
#include <optional>
#include <list>
#include <unordered_map>
#include <cassert>
#include <algorithm>

template<typename K, typename V>
class LRUCache {
public:
    explicit LRUCache(size_t capacity) : capacity_(capacity) {
        assert(capacity > 0);
        assert(size_ == 0);
        size_assertions();
    }

    std::optional<V> get(K key) {
        size_assertions();
        size_t init_size = size_;
        auto itr = values.find(key);
        if (itr != values.end()) {
            auto &list_itr = itr->second.second;
            assert(*list_itr == key);
            assert(std::count(list_.begin(), list_.end(), key) == 1);
            assert(std::find(list_.begin(), list_.end(), key) == list_itr);
            list_.erase(list_itr);
            list_.push_front(key);
            assert(std::count(list_.begin(), list_.end(), key) == 1);
            assert(std::find(list_.begin(), list_.end(), key) == list_.begin());
            itr->second.second = list_.begin();
            assert(itr == values.find(key));
            assert(*(values.find(key)->second.second) == key);
            return {itr->second.first};
        }
        assert(size_ == init_size);
        size_assertions();
        return std::nullopt;
    }


    bool put(K key, V value) {
        size_assertions();
        auto itr = values.find(key);
        if (itr == values.end()) {
            int expected_size = size_ + 1;
            if (size_ == capacity_) {
                expected_size = size_;
                remove_lru_item();
            }
            assert(std::find(list_.begin(), list_.end(), key) == list_.end());
            list_.push_front(key);
            assert(*list_.begin() == key);
            assert(std::count(list_.begin(), list_.end(), key) == 1);
            values.insert({key, {value, list_.begin()}});
            ++size_;
            assert(values.find(key) != values.end());
            assert((*values.find(key)).second.first == value);
            assert((*values.find(key)).second.second == list_.begin());
            assert(size_ == expected_size);
            size_assertions();
            return true;
        }
        return false;
    }

    size_t get_size() const {
        size_assertions();
        return size_;
    }

    bool remove(K key) {
        size_assertions();
        auto itr = values.find(key);
        if (itr != values.end()) {
            auto &list_itr = itr->second.second;
            assert(*list_itr == key);
            assert(std::count(list_.begin(), list_.end(), key) == 1);
            assert(std::find(list_.begin(), list_.end(), key) == list_itr);
            list_.erase(list_itr);
            list_.push_back(key);
            assert(std::count(list_.begin(), list_.end(), key) == 1);
            assert(std::find(list_.begin(), list_.end(), key) == std::prev(list_.end()));
            remove_lru_item();
            return true;
        }
        return false;
    }

private:

    void size_assertions() const {
        assert(size_ <= capacity_);
        assert (size_ >= 0);
        assert(size_ == list_.size());
        assert(values.size() == size_);
    }

    void remove_lru_item() {
        size_t prev_size = size_;
        size_assertions();
        K &lru_item_key = list_.back();
        assert(values.find(lru_item_key) != values.end());
        values.erase(lru_item_key);
        assert(values.find(lru_item_key) == values.end());
        K erased_key_copy = lru_item_key;
        list_.pop_back();
        assert(std::find(list_.begin(), list_.end(), erased_key_copy) == list_.end());
        --size_;
        assert(size_ == prev_size - 1);
        size_assertions();
    }

    const size_t capacity_;
    size_t size_ = 0;
    std::list<K> list_;
    std::unordered_map<K, std::pair<V, typename std::list<K>::iterator>> values;
};

#endif //LRU_CACHE_LRUCACHE_HPP
