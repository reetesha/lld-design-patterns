package com.reetesh.lld;

import java.util.HashMap;
import java.util.Map;

public class LRU {
    int capacity=2;
    Map<Integer, Node> cache = new HashMap<>();
    Node head,tail;
    LRU(int capacity){
        this.capacity=capacity;
        this.head=new Node(-1,-1);
        this.tail=new Node(-1,-1);
        this.head.next=tail;
        this.tail.prev=head;
    }

    int get(int key){
        //int bucketKey=capacity%key;
        Node node = cache.get(key);
        if(node==null) return -1;
        moveToFront(node);
        return node.val;
    }

    void put(int key,int val){

        Node node = cache.get(key);
        if(node!=null) {
            node.val=val;
            moveToFront(node);
            return;

        }
        if(capacity== cache.size()) {
            Node lru=tail.prev;
            //remove from tail
            remove(lru);
            cache.remove(lru.key);
        };
        node = new Node(key,val);
        addToFront(node);

        cache.put(key,node);
    }


    void remove(Node node){
        node.prev.next=node.next;
        node.next.prev=node.prev;
    }

    void addToFront(Node node){
        node.next=head.next;
        node.prev=head;
        head.next.prev=node;
        head.next=node;
    }

    void moveToFront(Node node){
        remove(node);
        addToFront(node);
    }

    public static void main(String args[]){
        LRU lru=new LRU(2);
        lru.put(1,1);
        lru.put(2,2);
        System.out.println(lru.get(1));
        System.out.println(lru.get(2));
        System.out.println(lru.get(3));
        lru.put(3,3);
        System.out.println(lru.get(3));
        System.out.println(lru.get(1));
        System.out.println(lru.get(2));
    }
}

class Node{
    int key,val;
    Node next, prev;
    Node(int key, int val){
        this.key=key;
        this.val=val;
    }
}
