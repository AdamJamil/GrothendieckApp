package com.example.adama.helloworld;

public class Term implements Comparable
{
    int[] mu;
    Word word;
    int coeff;
    int pow;

    Term()
    {

    }

    Term(Term term)
    {
        mu = term.mu.clone();
        coeff = term.coeff;
        pow = term.pow;
    }

    @Override
    public String toString()
    {
        int rows;
        for (rows = 0; rows < mu.length; rows++)
            if (mu[rows] == 0)
                break;

        //if (rows == 0)
        //    return "";


        //if (rows < 3 || mu[2] == 1)
        //    return "";

        String[] out = new String[rows];
        String[] buffers = new String[rows + 1];

        for (int i = 0; i < out.length; i++)
        {
            out[i] = "|";

            for (int j = 0; j < mu[i] - 1; j++)
                out[i] += " |";

            out[i] += " |";
        }

        buffers[0] = "-";
        for (int i = 1; i < out[0].length() - 1; i++)
            if (i % 2 == 1)
                buffers[0] += "-";
            else
                buffers[0] += "-";
        buffers[0] += "-";

        for (int i = 1; i < out.length; i++)
        {
            buffers[i] = "|";
            for (int j = 1; j < out[i - 1].length() - 1; j++)
                if (j % 2 == 1)
                    buffers[i] += "-";
                else if (out[i].length() - 1 >= j)
                    buffers[i] += "|";
                else
                    buffers[i] += "-";
            if (out[i - 1].length() == out[i].length())
                buffers[i] += "|";
            else
                buffers[i] += "-";
        }

        buffers[buffers.length - 1] = "-";
        for (int i = 1; i < out[out.length - 1].length() - 1; i++)
            if (i % 2 == 1)
                buffers[buffers.length - 1] += "-";
            else
                buffers[buffers.length - 1] += "-";
        buffers[buffers.length - 1] += "-";

        int coeffLength = Integer.toString(coeff).length();

        String spaces = "";
        while (coeffLength != 0)
        {
            spaces += " ";
            coeffLength--;
        }
        if (coeff > 0)
            spaces += " ";
        spaces += " ";

        String output = "\n" + spaces + buffers[0];

        int upper = out[0].length();

        for (int i = 1; i < out.length; i++)
        {
            for (int j = out[i].length(); j < upper; j++)
            {
                out[i] += " ";
                buffers[i + 1] += " ";
            }
        }

        String coeffString = "";
        if (coeff > 0)
            coeffString = "+";

        if (rows % 2 == 1)
        {
            for (int i = 0; i < out.length; i++)
            {
                if (i * 2 == out.length || i * 2 == out.length - 1)
                    output += "\n " + coeffString + coeff + out[i] + "\n" + spaces + buffers[i + 1];
                else
                    output += "\n" + spaces + out[i] + "\n" + spaces + buffers[i + 1];
            }
        }
        else
        {
            for (int i = 0; i < out.length; i++)
            {
                if ((i + 1) * 2 == out.length || (i + 1) * 2 == out.length - 1)
                    output += "\n" + spaces + out[i] + "\n " + coeffString + coeff + buffers[i + 1];
                else
                    output += "\n" + spaces + out[i] + "\n" + spaces + buffers[i + 1];
            }
        }

        String last = spaces + "[" + mu[0];
        for (int i = 1; i < rows; i++)
            last += "" + mu[i];
        last += "]";

        for (int i = last.length() - 2; i <= out[0].length(); i++)
            last += " ";

        return output + "\n" + last;
    }

    @Override
    public int compareTo(Object o)
    {
        Partition partition1 = new Partition(mu);
        Partition partition2 = new Partition(((Term) o).mu);

        if (partition1.rows != partition2.rows)
            return partition2.rows - partition1.rows;

        int i = 0;
        if (MainActivity.getRows(partition1.mu) >= 4 && MainActivity.getRows(partition2.mu) >= 4)
        {
            for (i = partition1.mu.length - 1; i >= 0; i--)
                if (partition1.mu[i] != partition2.mu[i])
                    return partition2.mu[i] - partition1.mu[i];

            return 0;
        }

        for (; i < partition1.rows; i++)
            if (partition1.mu[i] != partition2.mu[i])
                return partition2.mu[i] - partition1.mu[i];

        if (MainActivity.getRows(partition1.mu) >= 4 && MainActivity.getRows(partition2.mu) >= 4)
            return partition2.mu[0] - partition1.mu[0];

        return 0;
    }
}
