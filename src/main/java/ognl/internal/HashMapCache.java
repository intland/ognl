/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ognl.internal;

import ognl.internal.entry.CacheEntryFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HashMapCache<K, V> implements Cache<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>(512);

    private final CacheEntryFactory<K, V> cacheEntryFactory;

    public HashMapCache(CacheEntryFactory<K, V> cacheEntryFactory) {
        this.cacheEntryFactory = cacheEntryFactory;
    }

    public void clear() {
        cache.clear();
    }

    public int getSize() {
        return cache.size();
    }

    public V get(K key) throws CacheException {
        V v = cache.get(key);
        if (shouldCreate(cacheEntryFactory, v)) {
            return cache.computeIfAbsent(key, cacheEntryFactory::create);
        }
        return v;
    }

    protected boolean shouldCreate(CacheEntryFactory<K, V> cacheEntryFactory, V v) throws CacheException {
        return cacheEntryFactory != null && v == null;
    }

    public V put(K key, V value) {
        cache.put(key, value);
        return value;
    }

    public boolean contains(K key) {
        return cache.containsKey(key);
    }

}