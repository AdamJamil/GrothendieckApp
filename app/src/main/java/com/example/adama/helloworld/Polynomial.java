package com.example.adama.helloworld;

import java.util.ArrayList;
import java.util.Collections;

public class Polynomial
{
    ArrayList<Term> terms = new ArrayList<>();

    void dropZeroes()
    {
        //terms.removeIf((Term term) -> term.coeff == 0 || term.mu.length == 0 || term.mu[0] == 0);
        for (int i = terms.size() - 1; i >= 0; i--)
            if (terms.get(i).coeff == 0 || terms.get(i).mu.length == 0 || terms.get(i).mu[0] == 0)
                terms.remove(i);
    }

    void combineLikeTerms()
    {
        for (int i = 0; i < terms.size() - 1; i++)
        {
            Term term1 = terms.get(i);
            int rows1 = MainActivity.getRows(term1.mu);

            outer: for (int j = i + 1; j < terms.size(); j++)
            {
                Term term2 = terms.get(j);
                int rows2 = MainActivity.getRows(term2.mu);

                if (term1.pow == term2.pow && rows1 == rows2)
                {
                    for (int k = 0; k < rows1; k++)
                        if (term1.mu[k] != term2.mu[k])
                            continue outer;

                    term1.coeff += term2.coeff;
                    terms.remove(j);
                    j--;
                }
            }
        }
    }

    Polynomial multiply(Partition partition)
    {
        Polynomial output = new Polynomial();

        for (Term term : terms)
        {
            Partition partition1 = new Partition(term.mu);

            Polynomial temp = partition.multiply(partition1);
            temp.multiply(term.coeff);

            output.terms.addAll(temp.terms);
        }

        output.combineLikeTerms();
        output.dropZeroes();

        return output;
    }

    Polynomial multiply(Polynomial polynomial)
    {
        Polynomial output = new Polynomial();

        for (Term term1 : terms)
        {
            for (Term term2 : polynomial.terms)
            {
                Partition partition1 = new Partition(term1.mu);
                Partition partition2 = new Partition(term2.mu);

                Polynomial temp = partition1.multiply(partition2);
                temp.multiply(term1.coeff * term2.coeff);

                output.terms.addAll(temp.terms);
            }
        }
        
        return output;
    }

    void multiply(int coeff)
    {
        for (Term term : terms)
            term.coeff *= coeff;
    }

    Polynomial multiplyCopy(int coeff)
    {
        Polynomial output = new Polynomial();

        for (Term term : terms)
        {
            Term newTerm = new Term(term);
            newTerm.coeff *= coeff;
            output.terms.add(newTerm);
        }

        return output;
    }

    Polynomial subtract(Polynomial polynomial)
    {
        return linComb(polynomial, 1, -1);
    }

    Polynomial add(Polynomial polynomial)
    {
        return linComb(polynomial, 1, 1);
    }

    Polynomial linComb(Polynomial polynomial, int a, int b)
    {
        Polynomial output = new Polynomial();

        Polynomial polynomial1 = this.multiplyCopy(a);

        output.terms.addAll(polynomial1.terms);

        Polynomial polynomial2 = polynomial.multiplyCopy(b);

        output.terms.addAll(polynomial2.terms);

        output.combineLikeTerms();
        output.dropZeroes();

        return output;
    }

    @Override
    public String toString()
    {
        Collections.sort(terms);

        combineLikeTerms();
        dropZeroes();

        for (int i = terms.size() - 1; i >= 0; i--)
        {
            Term term = terms.get(i);

            int[] mu = term.mu;
            int rows = MainActivity.getRows(mu);

            if ((MainActivity.modAll || MainActivity.reduceThreeRow) && rows == 3 && mu[0] > mu[1])
            {
                mu[0] = mu[1];/*
                Polynomial polynomial = (new Partition(mu)).multiply(new Partition(new int[]{0}));
                polynomial.combineLikeTerms();
                polynomial.dropZeroes();*/
                term.mu = /*polynomial.terms.get(0).*/mu;
                combineLikeTerms();
                dropZeroes();
            }

            if ((MainActivity.modAll || MainActivity.reduceFourRow) && rows == 4 && mu[2] == mu[3] && mu[3] == 1)
            {
                mu[0] = mu[1];/*
                Polynomial polynomial = (new Partition(mu)).multiply(new Partition(new int[]{0}));
                polynomial.combineLikeTerms();
                polynomial.dropZeroes();*/
                term.mu = /*polynomial.terms.get(0).*/mu;
                combineLikeTerms();
                dropZeroes();
            }

            Collections.sort(terms);

            if ((MainActivity.modAll || MainActivity.modRectangles) && rows != 0 && mu[0] == mu[rows - 1])
                term.coeff = 0;

            if ((MainActivity.modAll || MainActivity.modLs) && (rows > 1 && mu[1] == 1))
                term.coeff = 0;

            if ((MainActivity.modAll || MainActivity.modTwoRow) && rows == 2)
                term.coeff = 0;
        }

        combineLikeTerms();
        dropZeroes();

        ArrayList<String> lines = new ArrayList<>();

        for (Term term : terms)
        {
            String[] toString = term.toString().split("\n");

            for (int i = 0; i < toString.length; i++)
                if (lines.size() == i)
                    lines.add("");
        }

        for (Term term : terms)
        {
            String[] toString = term.toString().split("\n");

            for (int i = 0; i < toString.length; i++)
                lines.set(i - ((toString.length - lines.size()) / 2), lines.get(i - ((toString.length - lines.size()) / 2)) + toString[i]);
        }

        String output = "";

        for (String line : lines)
            output += line + "\n";

        return output + "\n" + terms.size() + " terms";
    }
}
