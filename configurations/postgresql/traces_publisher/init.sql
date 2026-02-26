create table if not exists ingested_trace (
    trace_id        varchar(80) primary key,
    received_at     timestamptz not null,
    bounded_context varchar(80) not null,
    acte_metier     varchar(120) not null
    );

create index if not exists idx_ingested_trace_received_at on ingested_trace(received_at);