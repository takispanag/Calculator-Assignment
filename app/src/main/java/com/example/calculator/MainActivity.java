package com.example.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private int numberOfOperands = 0;
    private int numberOfDots = 0;

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
        buttonIntent = (Button) findViewById(R.id.button_conversion_intent);
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
            case R.id.button_conversion_intent:
                //intent to conversion activity
                Intent intent = new Intent(MainActivity.this, ConversionActivity.class);
//                intent.putExtra("amount", inputNumbers.getText().toString().replaceAll("[-\\+x÷%]",""));
                MainActivity.this.startActivity(intent);
                break;
            case R.id.button_clear:
                inputNumbers.setText("");
                numberOfOperands = 0;
                numberOfDots = 0;
                break;
            case R.id.button_clear_once:
                //delete one char
                if(inputNumbers.getText().length()>0){
                    //if char to be deleted is dot, reset dot counter
                    if(getLastCharacerType(inputNumbers.getText().charAt(inputNumbers.getText().length() - 1) + "") == IS_DOT){
                        numberOfDots = 0;
                    }
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

        if(numberOfDots <1){
            if (inputNumbers.getText().length() == 0)
            {
                inputNumbers.setText("0.");
                completed = true;
                numberOfDots++;
            } else if (getLastCharacerType(inputNumbers.getText().charAt(inputNumbers.getText().length() - 1) + "") == IS_OPERAND)
            {
                inputNumbers.setText(inputNumbers.getText() + "0.");
                completed = true;
                numberOfDots++;
            } else if (getLastCharacerType(inputNumbers.getText().charAt(inputNumbers.getText().length() - 1) + "") == IS_NUMBER)
            {
                inputNumbers.setText(inputNumbers.getText() + ".");
                completed = true;
                numberOfDots++;
            }
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
                //set dots to 0 after operand
                numberOfDots = 0;
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
            }else if (lastInput.equals("."))
            {
                Toast.makeText(getApplicationContext(), "You can't add operand when last character is dot (.)", Toast.LENGTH_LONG).show();
            }
            else if (operand.equals("%") && getLastCharacerType(lastInput) == IS_NUMBER)
            {
                inputNumbers.setText(inputNumbers.getText() + operand);
                numberOfOperands++;
                completed = true;
                numberOfDots = 0;
            } else if (!operand.equals("%"))
            {
                inputNumbers.setText(inputNumbers.getText() + operand);
                numberOfOperands++;
                completed = true;
                numberOfDots = 0;
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
            int lastCharacterState = getLastCharacerType(lastCharacter);

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


   private void operation(Double firstNumber, Double secondNumber) {
        switch (OPERATION) {
            //decimal format to remove trailing zeros
                case ADDITION:
                    //if number %1 != 0 means number is decimal. So dots=1 to not be able to add more
                    if((firstNumber + secondNumber) % 1 != 0 ){
                        numberOfDots = 1;
                    }
                    inputNumbers.setText(decimalFormat.format(firstNumber + secondNumber));
                    break;
                case SUBTRACTION:
                    if((firstNumber - secondNumber) % 1 != 0 ){
                        numberOfDots = 1;
                    }
                    inputNumbers.setText(decimalFormat.format(firstNumber - secondNumber));
                    break;
                case MULTIPLICATION:
                    if((firstNumber * secondNumber) % 1 != 0 ){
                        numberOfDots = 1;
                    }
                    inputNumbers.setText(decimalFormat.format(firstNumber * secondNumber));
                    break;
                case DIVISION:
                    if((firstNumber / secondNumber) % 1 != 0 ){
                        numberOfDots = 1;
                    }
                    inputNumbers.setText(decimalFormat.format(firstNumber / secondNumber));
                    break;
                case MODULUS:
                    if((firstNumber % secondNumber) % 1 != 0 ){
                        numberOfDots = 1;
                    }
                    inputNumbers.setText(decimalFormat.format(firstNumber % secondNumber));
                    break;
                case EQUALS:
                    break;
        }
    }

    private int getLastCharacerType(String lastCharacter)
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