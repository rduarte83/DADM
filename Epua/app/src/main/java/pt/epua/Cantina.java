package pt.epua;

class Cantina {
    @SuppressWarnings("unused")
    private final String canteen;
    private final String meal;
    private final String weekday;
    private String sopa;
    private String carne;
    private String peixe;
    private String dieta;
    private String veget;
    private String opcao;
    private String salada;
    private String diversos;
    private String sobremesa;
    private final String disabled;

    public Cantina(String canteen, String weekday, String meal, String sopa, String carne, String peixe, String dieta, String veget, String opcao, String salada, String diversos, String sobremesa, String disabled) {
        this.canteen = canteen;
        this.weekday = weekday;
        this.meal = meal;
        this.sopa = sopa;
        this.carne = carne;
        this.peixe = peixe;
        this.dieta = dieta;
        this.veget = veget;
        this.opcao = opcao;
        this.salada = salada;
        this.diversos = diversos;
        this.sobremesa = sobremesa;
        this.disabled = disabled;
    }

    public Cantina(String canteen, String weekday, String meal, String disabled) {
        this.canteen = canteen;
        this.weekday = weekday;
        this.meal = meal;
        this.disabled = disabled;
    }

    public String getDisabled() {
        return disabled;
    }

    public String getMeal() {
        return meal;
    }

    public String getWeekday() {
        return weekday;
    }

    public String getSopa() {
        return sopa;
    }

    public String getCarne() {
        return carne;
    }

    public String getPeixe() {
        return peixe;
    }

    public String getDieta() {
        return dieta;
    }

    public String getVeget() {
        return veget;
    }

    public String getOpcao() {
        return opcao;
    }

    public String getSalada() {
        return salada;
    }

    public String getDiversos() {
        return diversos;
    }

    public String getSobremesa() {
        return sobremesa;
    }

}

