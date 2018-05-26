package pt.epua;

public class Parque {
    String id, nome;
    int capacidade, livre;
    float distancia;

    public Parque(String id, String nome, int capacidade, int livre, float distancia) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.livre = livre;
        this.distancia = distancia;
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

    public float getDistancia() {
        return distancia;
    }
}