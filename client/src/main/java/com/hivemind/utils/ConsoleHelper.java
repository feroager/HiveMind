package com.hivemind.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * The class provides static methods for writing and reading data.
 */
public class ConsoleHelper
{
    /**
     * Field serve to read data from Standard Input
     */
    static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * It mediates in writing a text message
     * @param message Text to write
     */
    public static void writeMessage(String message)
    {
        System.out.println(message);
    }

    /**
     * It mediates in reading a text message.
     * If an error occurs, it informs you about it.
     * And read the input again.
     * @return String containing text message.
     */
    public static String readString()
    {
        while(true)
        {
            try
            {
                String result = bufferedReader.readLine();
                if(result != null)
                    return result;
            }
            catch(IOException e)
            {
                writeMessage("An error occurred. Try again.");
            }
        }
    }

    /**
     * It mediates in reading integer number.
     * If an error occurs, it informs you about it.
     * And read the input again.
     * Used to {@link ConsoleHelper#readString()} method to read data next convert it.
     * @return Returns the read int value.
     * @see ConsoleHelper#readString()
     */
    public static int readInt()
    {
        while(true)
        {
            try
            {
                int result = Integer.parseInt(readString());
                return result;
            }
            catch(NumberFormatException e)
            {
                writeMessage("An error occurred. Try again.");
            }
        }
    }


}
