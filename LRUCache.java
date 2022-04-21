package com.ywt.shuati.leetcode;

import java.util.HashMap;

/**
 * HashMap + 双向链表
 * HashMap的 value指向链表中的结点，一个 key代表一个结点，从而实现 O(1)的访问
 * 双向链表：这里用两个指针 head，tail。
 *      越靠近 head，代表最近被访问。相反，越靠近 tail,代表最近未访问
 *      新添加的元素应该从 head插入链表
 *      移除最近未使用应该从 tail移出
 *      从而实现 O(1)的添加操作
 */
public class LRUCache {
    /**
     * 双向链表的结点
     */
    private static class Node {
        public int key;
        public int val;
        public Node prev;
        public Node next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }

    private final HashMap<Integer, Node> map;
    private final int capacity;
    /**
     * head指向链表头部，表示最近使用过的
     */
    private final Node head;
    /**
     * tail指向链表尾部，表示最近最久未使用
     */
    private final Node tail;
    public LRUCache(int capacity) {
        map = new HashMap<>();
        this.capacity = capacity;
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * put表示更新操作
     *   1、如果待添加的 key存在，只更新
     *   2、如果不存在，添加
     *   3、当执行get操作后，同样也需要更新
     */
    public void put(int key, int val) {
        Node newNode = new Node(key, val);
        if (map.containsKey(key)) {  // 如果有key，只做更新
            // 更新，说明访问了这个key,那么需要将它重新放入缓存中，表示才访问
            // 具体来讲，就是先删除缓存中已有的
            delete(map.get(key));
            // 再将其放入缓存，因为是刚访问过，所以从头结点插入
            addFirst(newNode);
            // 同时更新map
            map.put(key, newNode);
        }else {  // 如果key不存在，
            if(map.size() == capacity) {  // 如果已经到了最大容量，需要移除最近未使用
                // 移除有两个操作，从缓存（链表）中移除和从map中移除
                // map中移除需要根据key操作，所以这里需要从链表中移除后能返回最近未使用的结点对应的key
                int k = deleteLast();
                map.remove(k);
            }
            // 将数据添加，同样是两个操作，添加到缓存中和map
            map.put(key, newNode);
            addFirst(newNode);
        }
    }

    /**
     * 获取 key对应的值
     */
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        int val = map.get(key).val;
        // 获取操作表示最近被访问，因此需要对其进行更新
        put(key, val);
        return val;
    }

    /**
     * 新加入的表示新访问的，应该从头结点加入
     */
    private void addFirst(Node node) {
        // 将node结点连接两端
        node.next = head.next;
        node.prev = head;

        // 断开之前的连接
        head.next.prev = node;
        head.next = node;
    }

    /**
     * 删除结点
     */
    private int delete(Node node) {
        int key = node.key;
        node.next.prev = node.prev;
        node.prev.next = node.next;
        return key;
    }

    /**
     * 删除尾结点
     */
    private int deleteLast() {
        if (head.next == tail) {
            return -1;
        }
        return delete(tail.prev);
    }
}


