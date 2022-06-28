package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private int numberOfOperands = 0;

    private final char ADDITION = '+';
    private final char SUBTRACTION = '-';
    private final char MULTIPLICATION = 'x';
    private final char DIVISION = '÷';
    private final char EQUALS = '=';
    private final char MODULUS = '%';
    private char OPERATION;

    private final static int IS_NUMBER = 0;
    private final static int IS_OPERAND = 1;
    private final static int IS_DOT = 2;

    List<IconSpinnerItem> iconSpinnerItems = new ArrayList<>();

    //format to remove trailing zeros
    DecimalFormat decimalFormat = new DecimalFormat("0.#####");

    Button buttonNumber0;
    Button buttonNumber1;
    Button buttonNumber2;
    Button buttonNumber3;
    Button buttonNumber4;
    Button buttonNumber5;
    Button buttonNumber6;
    Button buttonNumber7;
    Button buttonNumber8;
    Button buttonNumber9;

    Button buttonClear;
    Button buttonClearOnce;
    Button buttonPercent;
    Button buttonDivision;
    Button buttonMultiplication;
    Button buttonSubtraction;
    Button buttonAddition;
    Button buttonEqual;
    Button buttonDot;

    Button buttonIntent;
    TextView inputNumbers;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewVariables();

        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        buttonNumber0.setOnClickListener(this);
        buttonNumber1.setOnClickListener(this);
        buttonNumber2.setOnClickListener(this);
        buttonNumber3.setOnClickListener(this);
        buttonNumber4.setOnClickListener(this);
        buttonNumber5.setOnClickListener(this);
        buttonNumber6.setOnClickListener(this);
        buttonNumber7.setOnClickListener(this);
        buttonNumber8.setOnClickListener(this);
        buttonNumber9.setOnClickListener(this);

        buttonClear.setOnClickListener(this);
        buttonClearOnce.setOnClickListener(this);
        buttonPercent.setOnClickListener(this);
        buttonDivision.setOnClickListener(this);
        buttonMultiplication.setOnClickListener(this);
        buttonSubtraction.setOnClickListener(this);
        buttonAddition.setOnClickListener(this);
        buttonEqual.setOnClickListener(this);
        buttonDot.setOnClickListener(this);
        buttonIntent.setOnClickListener(this);
    }

    private void initializeViewVariables()
    {
        buttonNumber0 = (Button) findViewById(R.id.button_zero);
        buttonNumber1 = (Button) findViewById(R.id.button_one);
        buttonNumber2 = (Button) findViewById(R.id.button_two);
        buttonNumber3 = (Button) findViewById(R.id.button_three);
        buttonNumber4 = (Button) findViewById(R.id.button_four);
        buttonNumber5 = (Button) findViewById(R.id.button_five);
        buttonNumber6 = (Button) findViewById(R.id.button_six);
        buttonNumber7 = (Button) findViewById(R.id.button_seven);
        buttonNumber8 = (Button) findViewById(R.id.button_eight);
        buttonNumber9 = (Button) findViewById(R.id.button_nine);

        buttonClear = (Button) findViewById(R.id.button_clear);
        buttonClearOnce = (Button) findViewById(R.id.button_clear_once);
        buttonPercent = (Button) findViewById(R.id.button_percent);
        buttonDivision = (Button) findViewById(R.id.button_division);
        buttonMultiplication = (Button) findViewById(R.id.button_multiplication);
        buttonSubtraction = (Button) findViewById(R.id.button_subtraction);
        buttonAddition = (Button) findViewById(R.id.button_addition);
        buttonEqual = (Button) findViewById(R.id.button_equal);
        buttonDot = (Button) findViewById(R.id.button_dot);
        buttonIntent = (Button) findViewById(R.id.button_intent);
        inputNumbers = (TextView) findViewById(R.id.textView_input_numbers);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.button_zero:
                addNumber("0");
                break;
            case R.id.button_one:
                addNumber("1");
                break;
            case R.id.button_two:
                addNumber("2");
                break;
            case R.id.button_three:
                addNumber("3");
                break;
            case R.id.button_four:
                addNumber("4");
                break;
            case R.id.button_five:
                addNumber("5");
                break;
            case R.id.button_six:
                addNumber("6");
                break;
            case R.id.button_seven:
                addNumber("7");
                break;
            case R.id.button_eight:
                addNumber("8");
                break;
            case R.id.button_nine:
                addNumber("9");
                break;
            case R.id.button_addition:
                addOperand("+");
                break;
            case R.id.button_subtraction:
                addOperand("-");
                break;
            case R.id.button_multiplication:
                addOperand("x");
                break;
            case R.id.button_division:
                addOperand("÷");
                break;
            case R.id.button_percent:
                addOperand("%");
                break;
            case R.id.button_dot:
                addDot();
                break;
            case R.id.button_intent:
                //intent to conversion activity
                Intent intent = new Intent(MainActivity.this, ConversionActivity.class);
                intent.putExtra("amount", inputNumbers.getText().toString().replaceAll("[-\\+x÷%]",""));
                MainActivity.this.startActivity(intent);
                break;
            case R.id.button_clear:
                inputNumbers.setText("");
                numberOfOperands = 0;
                break;
            case R.id.button_clear_once:
                //delete one char
                if(inputNumbers.getText().length()>0){
                    inputNumbers.setText(inputNumbers
                            .getText()
                            .toString()
                            .substring(0, inputNumbers.getText().length() - 1)
                    );
                    numberOfOperands = 0;
                }
                break;
            case R.id.button_equal:
                if (inputNumbers.getText().toString() != null && !inputNumbers.getText().toString().equals(""))
                {
                    Pattern pattern = Pattern.compile("(-?\\d+(?:\\.\\d+)?)([-+x÷%\\/])(-?\\d+(?:\\.\\d+)?)");
                    Matcher matcher = pattern.matcher(inputNumbers.getText().toString());
                    while (matcher.find()) {
                        //get firstNumber, operation and secondNumber from regex match
                        Double firstNumber = Double.parseDouble(matcher.group(1));
                        OPERATION = matcher.group(2).charAt(0);
                        Double secondNumber =  Double.parseDouble(matcher.group(3));
                        operation(firstNumber, secondNumber);
                    }
                }
                break;
        }

    }


    private boolean addDot()
    {
        boolean completed = false;

        if (inputNumbers.getText().length() == 0)
        {
            inputNumbers.setText("0.");
            completed = true;
        } else if (defineLastCharacter(inputNumbers.getText().charAt(inputNumbers.getText().length() - 1) + "") == IS_OPERAND)
        {
            inputNumbers.setText(inputNumbers.getText() + "0.");
            completed = true;
        } else if (defineLastCharacter(inputNumbers.getText().charAt(inputNumbers.getText().length() - 1) + "") == IS_NUMBER)
        {
            inputNumbers.setText(inputNumbers.getText() + ".");
            completed = true;
        }
        return completed;
    }

    private boolean addOperand(String operand)
    {
        boolean completed = false;
        //if >= 1 operand do the operation and add the operand at the end
        if(numberOfOperands>=1)
        {
            Pattern pattern = Pattern.compile("(-?\\d+(?:\\.\\d+)?)([-+x÷%\\/])(-?\\d+(?:\\.\\d+)?)");
            Matcher matcher = pattern.matcher(inputNumbers.getText().toString());
            while (matcher.find()) {
                Double firstNumber = Double.parseDouble(matcher.group(1));
                OPERATION = matcher.group(2).charAt(0);
                Double secondNumber =  Double.parseDouble(matcher.group(3));
                operation(firstNumber, secondNumber);
            }
            numberOfOperands=0;
        }

        int operationLength = inputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastInput = inputNumbers.getText().charAt(operationLength - 1) + "";

            //last input was operand you cant have operand again
            if ((lastInput.equals("+")
                    || lastInput.equals("-")
                    || lastInput.equals("x")
                    || lastInput.equals("÷")
                    || lastInput.equals("%")))
            {
                Toast.makeText(getApplicationContext(), "Operand already exists", Toast.LENGTH_LONG).show();
            } else if (operand.equals("%") && defineLastCharacter(lastInput) == IS_NUMBER)
            {
                inputNumbers.setText(inputNumbers.getText() + operand);
                numberOfOperands++;
                completed = true;
            } else if (!operand.equals("%"))
            {
                inputNumbers.setText(inputNumbers.getText() + operand);
                numberOfOperands++;
                completed = true;
            }
        } else
        {
            Toast.makeText(getApplicationContext(), "Wrong Format. Operand without numbers", Toast.LENGTH_LONG).show();
        }

        return completed;
    }

    private boolean addNumber(String number)
    {
        boolean completed = false;
        int operationLength = inputNumbers.getText().length();
        if (operationLength > 0)
        {
            String lastCharacter = inputNumbers.getText().charAt(operationLength - 1) + "";
            int lastCharacterState = defineLastCharacter(lastCharacter);

            if (operationLength == 1 && lastCharacterState == IS_NUMBER && lastCharacter.equals("0"))
            {
                inputNumbers.setText(number);
                completed = true;
            }  else if (lastCharacter.equals("%"))
            {
                inputNumbers.setText(inputNumbers.getText() + number);
                completed = true;
            } else if (lastCharacterState == IS_NUMBER || lastCharacterState == IS_OPERAND || lastCharacterState == IS_DOT)
            {
                inputNumbers.setText(inputNumbers.getText() + number);
                completed = true;
            }
        } else
        {
            inputNumbers.setText(inputNumbers.getText() + number);
            completed = true;
        }
        return completed;
    }



    //converts from old selection to new selection the amount and sets the textview
    private void getCurrencyConversion(String oldSpinnerText, String newSpinnerText, String amount) {
        try {
            //amount has an operand so we dont convert
            if(amount.matches(".*[-\\+x÷%].*")){
                Toast.makeText(getApplicationContext(), "Operand found. Conversion can only happen on a number!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Double.parseDouble(amount)<=0){
                Toast.makeText(getApplicationContext(), "Please enter amount greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getApplicationContext(), "Converting..", Toast.LENGTH_SHORT).show();

            String convertedCurrency = new RetrieveCurrencyConversionTask()
                    .execute(oldSpinnerText, newSpinnerText, amount)
                    .get();

            //if empty response means conversion failed
            if(convertedCurrency.equals("null") || convertedCurrency.isEmpty()){
                Toast.makeText(getApplicationContext(), "Conversion failed!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), "Conversion completed!", Toast.LENGTH_SHORT).show();
            inputNumbers.setText(convertedCurrency);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Conversion failed!", Toast.LENGTH_SHORT).show();
        }
    }


   private void operation(Double firstNumber, Double secondNumber) {
        switch (OPERATION) {
            //decimal format to remove trailing zeros
                case ADDITION:
                    inputNumbers.setText(decimalFormat.format(firstNumber + secondNumber));
                    break;
                case SUBTRACTION:
                    inputNumbers.setText(decimalFormat.format(firstNumber - secondNumber));
                    break;
                case MULTIPLICATION:
                    inputNumbers.setText(decimalFormat.format(firstNumber * secondNumber));
                    break;
                case DIVISION:
                    inputNumbers.setText(decimalFormat.format(firstNumber / secondNumber));
                    break;
                case MODULUS:
                    inputNumbers.setText(decimalFormat.format(firstNumber % secondNumber));
                    break;
                case EQUALS:
                    break;
        }
    }

    private int defineLastCharacter(String lastCharacter)
    {
        try
        {
            Integer.parseInt(lastCharacter);
            return IS_NUMBER;
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        }

        if ((lastCharacter.equals("+")
                || lastCharacter.equals("-")
                || lastCharacter.equals("x")
                || lastCharacter.equals("÷")
                || lastCharacter.equals("%")))
            return IS_OPERAND;

        if (lastCharacter.equals("."))
            return IS_DOT;

        return -1;
    }

}