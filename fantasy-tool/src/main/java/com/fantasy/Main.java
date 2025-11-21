package com.fantasy;

import java.io.IOException;
import java.util.Scanner;

import com.Logger.GlobalLogger;
import com.fantasy.Repository.*;
import com.fantasy.Service.FantasyToolService;

import ch.qos.logback.classic.Level;


public class Main {
    public static void main(String[] args) {
        // init logger

    
        GlobalLogger.info("Starting application");

        IRepository repo = new JpaRepository();
        Scanner scan = new Scanner(System.in);
        try (FantasyToolService service = new FantasyToolService(repo, scan)) {

            service.start();
        } catch (IOException e) {
            GlobalLogger.error("Trouble closing application", e);
        }
        GlobalLogger.info("Closing application");

        
    }
}