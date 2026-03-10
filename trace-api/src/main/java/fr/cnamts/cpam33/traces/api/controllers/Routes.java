package fr.cnamts.cpam33.traces.api.controllers;

public final class Routes {

    private Routes() {
        // Utility Class Not Implemented
    }

    public static final String URL_ID = "/{id}";

    public static final class Admin {

        private Admin() {
            // Sub Class Not Implemented
        }

        public static final String ROOT = "/admin";
        public static final String ADMIN_RESOURCE = Admin.ROOT + DlqMessage.RESOURCE;

    }

    public static final class DlqMessage {

        private DlqMessage() {
            // Sub Class Not Implemented
        }

        public static final String RESOURCE = "/dlq/messages";
        public static final String REPUBLISH = "/republish";
        public static final String DISCARD = "/discard";

    }

}
