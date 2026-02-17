FROM rabbitmq:4.2.3-management-alpine

ENV RABBITMQ_DEFAULT_USER=admin
ENV RABBITMQ_DEFAULT_VHOST=/traces

EXPOSE 5672
EXPOSE 15672

# Healthcheck
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD rabbitmq-diagnostics ping || exit 1