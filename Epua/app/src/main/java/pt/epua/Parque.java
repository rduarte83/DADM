package pt.epua;

class Parque {
    private final String id;
    private final String nome;
    public final int livre;
    public final int capacidade;
    private final float distancia;

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

    public float getDistancia() { return distancia; }
}