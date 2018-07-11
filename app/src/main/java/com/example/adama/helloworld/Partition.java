package com.example.adama.helloworld;

import android.widget.Toast;

import java.util.ArrayList;

class Partition
{
    int rows = 0;
    int[] mu;
    ArrayList<Word> words = new ArrayList<>(), newWords = new ArrayList<>();
    static int[] defaultCount = new int[MainActivity.length];
    static ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();

    Polynomial multiply(Partition partition)
    {
        ArrayList<Integer> arrayList = new ArrayList<>();

        for (int i = 0; i < 20; i++)
            arrayList.add(i);

        int[] count = defaultCount.clone();

        System.arraycopy(partition.mu, 0, count, 0, partition.mu.length);

        ArrayList<Integer>[][] filling = new ArrayList[mu.length][];

        for (int i = 0; i < mu.length; i++)
            filling[i] = new ArrayList[mu[i]];

        Word initial = new Word(count, filling);
        words.add(initial);

        initial.highest = partition.mu.length;

        for (int row = 0; row < mu.length; row++)
        {
            for (int col = mu[row] - 1; col >= 0; col--)
            {
                for (int j = 0; j < words.size(); j++)
                {
                    Word word = words.get(j);

                    int upperBound, lowerBound;

                    if (col == mu[row] - 1)
                        upperBound = Math.min(word.highest + 1, rows + partition.rows);
                    else
                        upperBound = Math.min(word.highest + 1, word.filling[row][col + 1].get(0));

                    if (row == 0)
                        lowerBound = 1;
                    else
                        lowerBound = word.filling[row - 1][col].get(word.filling[row - 1][col].size() - 1) + 1;

                    ArrayList<Integer> possibility = new ArrayList<>();

                    if (lowerBound == 1)
                        possibility.add(1);

                    for (int i = Math.max(lowerBound, 2); i <= upperBound; i++)
                    {
                        if (word.mu[i - 2] > word.mu[i - 1]) //0 indexing
                            possibility.add(i);
                    }

                    if (upperBound == word.highest + 1)
                        word.highest++;

                    ArrayList<ArrayList<Integer>> powerSet = new ArrayList<>();
                    powerSet.add(new ArrayList<Integer>());

                    for (int i = 0; i < possibility.size(); i++)
                    {
                        ArrayList<ArrayList<Integer>> copies = new ArrayList<>();

                        for (ArrayList<Integer> listss : powerSet)
                        {
                            ArrayList<Integer> copy = new ArrayList<>(listss);
                            copy.add(arrayList.get(i));
                            copies.add(copy);
                        }

                        powerSet.addAll(copies);
                    }

                    for (ArrayList<Integer> integers : powerSet)
                        for (int i = 0; i < integers.size(); i++)
                            integers.set(i, possibility.get(integers.get(i)));

                    powerSet.remove(0);

                    for (ArrayList<Integer> integers : powerSet)
                    {
                        Word newWord = new Word(word);
                        newWord.filling[row][col] = new ArrayList<>(integers);

                        for (Integer integer : integers)
                            newWord.mu[integer - 1]++;

                        newWord.terms += integers.size() - 1;

                        newWords.add(newWord);
                    }

                }

                words = new ArrayList<>(newWords);
                newWords.clear();
            }
        }

        ArrayList<Term> output = new ArrayList<>();

        for (Word word : words)
        {
            Term term = new Term();
            term.word = word;
            term.mu = word.mu.clone();
            if (word.terms % 2 == 0)
                term.coeff = 1;
            else
                term.coeff = -1;
            term.pow = 1;

            output.add(term);
        }

        Polynomial polynomial = new Polynomial();

        polynomial.terms = output;

        words.clear();

        polynomial.combineLikeTerms();
        polynomial.dropZeroes();

        return polynomial;
    }

    Partition(int[] input)
    {
        mu = new int[input.length];

        for (int i = 0; i < input.length; i++)
            mu[i] = input[i];

        for (rows = 0; rows < mu.length; rows++)
            if (mu[rows] == 0)
                break;
    }

    @Override
    public String toString()
    {
        int rows;
        for (rows = 0; rows < mu.length; rows++)
            if (mu[rows] == 0)
                break;

        String[] out = new String[rows];
        String[] buffers = new String[rows + 1];

        for (int i = 0; i < out.length; i++)
        {
            out[i] = "┃";

            for (int j = 0; j < mu[i] - 1; j++)
            {
                out[i] += " ┃";
            }

            out[i] += " ┃";
        }

        buffers[0] = "┏";
        for (int i = 1; i < out[0].length() - 1; i++)
            if (i % 2 == 1)
                buffers[0] += "━";
            else
                buffers[0] += "┳";
        buffers[0] += "┓";

        for (int i = 1; i < out.length; i++)
        {
                buffers[i] = "┣";
                for (int j = 1; j < out[i - 1].length() - 1; j++)
                    if (j % 2 == 1)
                        buffers[i] += "━";
                    else if (out[i].length() - 1 >= j)
                        buffers[i] += "╋";
                    else
                        buffers[i] += "┻";
                if (i == out.length || out[i - 1].length() == out[i].length())
                    buffers[i] += "┫";
                else
                    buffers[i] += "┛";
        }

        buffers[buffers.length - 1] = "┗";
        for (int i = 1; i < out[out.length - 1].length() - 1; i++)
            if (i % 2 == 1)
                buffers[buffers.length - 1] += "━";
            else
                buffers[buffers.length - 1] += "┻";
        buffers[buffers.length - 1] += "┛";

        String output = buffers[0] + "\n";

        for (int i = 0; i < out.length; i++)
            output += out[i] + "\n" + buffers[i + 1] + "\n";

        return output;
    }
}