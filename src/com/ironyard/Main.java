package com.ironyard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Main {

    static HashSet<Card> createDeck() {

        HashSet<Card> deck = new HashSet();

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                Card c = new Card(suit, rank);
                deck.add(c);
            }
        }
        return deck;
    }

    //draw a 4-card hand
    public static HashSet<HashSet<Card>> createHands (HashSet<Card> deck){
        HashSet<HashSet<Card>> hands = new HashSet();
        for (Card c1 : deck) {
            HashSet<Card> deck2 = (HashSet<Card>) deck.clone(); //new deck (minus card 1)
            deck2.remove(c1);
            for (Card c2 : deck2) {
                HashSet<Card> deck3 = (HashSet<Card>) deck2.clone();
                deck3.remove(c2);
                for (Card c3 : deck3) {
                    HashSet<Card> deck4 = (HashSet<Card>) deck3.clone();
                    deck4.remove(c3);
                    for (Card c4 : deck4) {
                        HashSet<Card> hand = new HashSet();
                        hand.add(c1);
                        hand.add(c2);
                        hand.add(c3);
                        hand.add(c4);
                        hands.add(hand);
                    }//end 4th card
                }//end 3rd card
            }//3nd 2nd card
        }//end 1st card
        return hands;
    }

    //4 cards of same suit
    public static boolean isFlush(HashSet<Card> hand) {
        HashSet<Card.Suit> suits = hand.stream().map(card -> {
            return card.suit;
        })
                .collect(Collectors.toCollection(HashSet::new));
        return suits.size() == 1;
    }

    //4 cards in sequence
    static boolean isStraight(HashSet<Card> hand) {
        ArrayList<Integer> ranks = hand.stream().map(card -> card.rank.ordinal()) //int from the enum
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> ranked = new ArrayList<>(); //arraylist to hold the sequence
        int s = ranks.get(0);
        for (int r = 0; r < ranks.size(); r++) { //verify sequential rank
            ranked.add(s + r);
        }
        return ranks.equals(ranked);
    }

    //combine flush and straight for 4 cards of same suit AND in sequence
    public static boolean isStraightFlush (HashSet<Card> hand){
            return isStraight(hand) && isFlush(hand);
    }

    //4 cards of equal rank
    static boolean isFourOfAKind (HashSet<Card> hand) {
        HashSet<Card.Rank> ranks = hand.stream().map(card -> {
            return card.rank;
        })
                .collect(Collectors.toCollection(HashSet::new));
        return ranks.size() == 1;
    }

    //3 cards of equal rank plus one other card
    static boolean isThreeOfAKind(HashSet<Card> hand) {
        ArrayList<Card.Rank> ranks = hand.stream().map(card -> {
            return card.rank;
        })
                .collect(Collectors.toCollection(ArrayList<Card.Rank>::new));
        HashSet<Integer> frequency = ranks.stream().map(rank ->
                Collections.frequency(ranks, rank))
                .collect(Collectors.toCollection(HashSet<Integer>::new));
        int sum = 0;
        for (int freq : frequency) {
            sum += freq;
        }
        return sum == 4 && frequency.size() != 1;
    }

    //2 cards of equal rank plus 2 other cards
    static boolean twoPairs (HashSet<Card> hand) {
        ArrayList<Card.Rank> ranks = hand.stream().map(card -> {
            return card.rank;
        })
                .collect(Collectors.toCollection(ArrayList<Card.Rank>::new));
        ArrayList<Integer> freq = ranks.stream().map(rank ->
                Collections.frequency(ranks, rank))
                .collect(Collectors.toCollection(HashSet<Integer>::new))
                .stream()
                .collect(Collectors.toCollection(ArrayList<Integer>::new));
        return  freq.get(0) == 2 && freq.size() == 1;
    }

    //run drawing 4-card hands
    public static void main(String[] args) {
        HashSet<Card> deck = createDeck();
        HashSet<HashSet<Card>> hands = createHands(deck);

        HashSet<HashSet<Card>> flushes = hands.stream()
                .filter(Main::isFlush)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        HashSet<HashSet<Card>>straight = hands.stream()
                .filter(Main::isStraight)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        HashSet<HashSet<Card>>straightFlush = hands.stream()
                .filter(Main::isStraightFlush)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        HashSet<HashSet<Card>>isFourOfAKind = hands.stream()
                .filter(Main::isFourOfAKind)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        HashSet<HashSet<Card>>isThreeOfAKind = hands.stream()
                .filter(Main::isThreeOfAKind)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        HashSet<HashSet<Card>>twoPairs = hands.stream()
                .filter(Main::twoPairs)
                .collect(Collectors.toCollection(HashSet<HashSet<Card>>::new));

        System.out.println("flush: " + flushes.size());
        System.out.println("straight: " + straight.size());
        System.out.println("straight flush: " + straightFlush.size());
        System.out.println("4 of a kind: " + isFourOfAKind.size());
        System.out.println("3 of a kind: " + isThreeOfAKind.size());
        System.out.println("2 pairs: " + twoPairs.size());
    }
}
