package com.mycompany.bookbridge.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Enrico Garcia
 */
public record CirculationSummary(
    int availableBooks,
    int checkedOutBooks,
    int activeLoans,
    long totalEstimatedFines
) {}