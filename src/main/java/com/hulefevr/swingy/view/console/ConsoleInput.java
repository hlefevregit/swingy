// lit input (BufferedReader)
package com.hulefevr.swingy.view.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.hulefevr.swingy.view.Input;


public class ConsoleInput implements Input {
    private BufferedReader reader;

    public ConsoleInput() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt + ": ");
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}