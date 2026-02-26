FROM postgres:17.6-alpine3.22

ENV POSTGRES_DB=trace_publisher_db
ENV POSTGRES_USER=postgres
COPY init.sql /docker-entrypoint-initdb.d/

USER postgres

EXPOSE 5432