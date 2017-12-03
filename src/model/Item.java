/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Lucien adn Leonardo
 */
public class Item {
    private String description;
    private String nome;
    private int weight;
    
    public Item(String description, int weight, String nome) {
        this.description = description;
        this.weight = weight;
        this.nome = nome;
    }
    
    public String getNome(){
        return nome;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLongDescription() {
        return description + " que pesa " + weight;
    }
}
