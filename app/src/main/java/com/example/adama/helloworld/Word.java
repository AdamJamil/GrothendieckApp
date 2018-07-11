package com.example.adama.helloworld;

import java.util.ArrayList;

class Word
{
    int[] mu;
    ArrayList<Integer>[][] filling;
    int highest = 0;
    int terms = 0;

    Word(Word input)
    {
        mu = input.mu.clone();
        filling = new ArrayList[input.filling.length][];

        for (int i = 0; i < filling.length; i++)
        {
            filling[i] = new ArrayList[input.filling[i].length];

            for (int j = filling[i].length - 1; j >= 0; j--)
            {
                if (input.filling[i][j] == null)
                    break;
                filling[i][j] = new ArrayList<>(input.filling[i][j]);
            }
        }

        highest = input.highest;
        terms = input.terms;
    }

    Word(int[] map, ArrayList<Integer>[][] arrayLists)
    {
        mu = map;
        filling = arrayLists;
    }

    @Override
    public String toString()
    {
        String[] out = new String[filling.length];

        for (int i = 0; i < out.length; i++)
        {
            out[i] = "|";

            for (ArrayList<Integer> arrayList : filling[i])
                out[i] += arrayList + "|";
        }

        String output = "\n";

        for (int i = 0; i < out[0].length(); i++)
            output += "-";
        output += "\n";

        for (String s : out)
        {
            output += s;
            output += "\n";
            for (int i = 0; i < s.length(); i++)
                output += "-";
            output += "\n";
        }

        return output;
    }
}
