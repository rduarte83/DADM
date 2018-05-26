package pt.epua;

public class Parque {
    String id, nome;
    int capacidade, livre;

    public Parque(String id, String nome, int capacidade, int livre) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.livre = livre;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getLivre() {
        return livre;
    }
}