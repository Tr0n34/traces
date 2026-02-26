package fr.cnamts.cpam33.traces.publisher.configurations;

public enum RabbitMessageHeader {

    SCHEMA_VERSION("schemaVersion"),
    ACTE_METIER_CODE("acteMetierCode"),
    UTILISATEUR_ID("utilisateurId"),
    TIMESTAMP("timestamp");

    private String name;

    RabbitMessageHeader(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
