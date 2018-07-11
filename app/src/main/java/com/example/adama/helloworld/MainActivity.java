package com.example.adama.helloworld;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    static int length = 10;
    static boolean modAll = false;
    static boolean modLs = false || modAll;
    static boolean modTwoRow = false || modAll;
    static boolean reduceThreeRow = false || modAll;
    static boolean reduceFourRow = false || modAll;
    static boolean modRectangles = false || modAll;

    String input = "";
    TextView formattedInput;
    TextView output;
    ArrayList<Integer> allSymbols = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formattedInput = findViewById(R.id.formattedInput);
        formattedInput.setText("Generating power set, please wait!");
        output = findViewById(R.id.output);
        output.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Consolas.ttf"));
        output.setText("");

        allSymbols.add(R.id.number1);
        allSymbols.add(R.id.number2);
        allSymbols.add(R.id.number3);
        allSymbols.add(R.id.number4);
        allSymbols.add(R.id.number5);
        allSymbols.add(R.id.number6);
        allSymbols.add(R.id.number7);
        allSymbols.add(R.id.number8);
        allSymbols.add(R.id.number9);
        allSymbols.add(R.id.add);
        allSymbols.add(R.id.subtract);
        allSymbols.add(R.id.multiply);

        for (Integer symbol : allSymbols)
            findViewById(symbol).setOnClickListener(this);

        findViewById(R.id.enter).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);

        new OnStartThread().execute();
    }

    @Override
    public void onClick(View v)
    {
        if (allSymbols.contains(v.getId()))
            input += ((Button) v).getText();

        if (v.getId() == R.id.clear)
            input = "";

        if (v.getId() == R.id.delete && !input.isEmpty())
            input = input.substring(0, input.length() - 1);

        if (v.getId() == R.id.enter)
        {
            this.output.setText("Loading...");
            new ComputationThread().execute();
        }

        if (input.isEmpty())
        {
            formattedInput.setText("");
            return;
        }

        String[] monomial = (input.replaceAll("[+]", "+$").replaceAll("[-]", "$-").split("[$]"));
        String end = "[";
        for (String s : monomial)
        {
            String temp = s.replaceAll("\\*", "]\\*[");
            temp = temp.replaceAll("-", "] - [");
            temp = temp.replaceAll("\\+", "] + [");
            end += temp;
            System.out.println(s);
        }
        end += "]";

        formattedInput.setText(end);
    }

    static int getRows(int[] mu)
    {
        int rows;
        for (rows = 0; rows < mu.length; rows++)
            if (mu[rows] == 0)
                break;

        return rows;
    }

    protected class ComputationThread extends AsyncTask<String, String, String>
    {
        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            ((TextView) findViewById(R.id.output)).setText(values[0]);
        }

        @Override
        protected String doInBackground(String... urls)
        {
            Polynomial output = (new Partition(new int[]{0})).multiply(new Partition(new int[]{0}));
            String tempString = input.replaceAll("\\+", "x");
            tempString = tempString.replaceAll("-", "x-");
            System.out.println(input + "  " + tempString);
            String[] polynomial = tempString.split("x");
            for (String monomial : polynomial)
            {
                System.out.println(monomial + "mono");
                boolean neg = false;
                if (monomial.contains("-"))
                {
                    monomial = monomial.substring(1);
                    neg = true;
                }

                String[] parsedInput = monomial.split("\\*");
                int[] first = new int[parsedInput[0].length()];

                for (int i = 0; i < parsedInput[0].length(); i++)
                    first[i] = Character.getNumericValue(parsedInput[0].charAt(i));

                System.out.println(first[0]);

                Polynomial temp = (new Partition(first)).multiply(new Partition(new int[]{0}));

                for (int i = 1; i < parsedInput.length; i++)
                {
                    int[] partition = new int[parsedInput[i].length()];

                    for (int j = 0; j < parsedInput[i].length(); j++)
                        partition[j] = Character.getNumericValue(parsedInput[i].charAt(j));

                    temp = temp.multiply(new Partition(partition));
                }

                if (neg)
                    output = output.subtract(temp);
                else
                    output = output.add(temp);
            }

            System.out.println(output.toString());

            publishProgress(output.toString());

            return "";
        }
    }

    protected class OnStartThread extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            ((TextView) findViewById(R.id.formattedInput)).setText("Power set complete!");
        }

        @Override
        protected String doInBackground(String... urls)
        {
            Partition.arrayLists.add(new ArrayList<Integer>());

            for (int i = 0; i < 20; i++)
            {
                ArrayList<ArrayList<Integer>> copies = new ArrayList<>();
                for (ArrayList<Integer> listss : Partition.arrayLists)
                {
                    ArrayList<Integer> copy = new ArrayList<>(listss);
                    copy.add(i);
                    copies.add(copy);
                }

                Partition.arrayLists.addAll(copies);
            }

            System.out.println(Partition.arrayLists);

            publishProgress();

            return "";
        }
    }
}