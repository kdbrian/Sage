# Drag API

A PDF ingestion / RAG platform: upload PDFs, tag them with topics, get notified
when something happens on topics you follow, search/browse what's been
ingested, and pull basic usage reports.

## Architecture

```
client
  │  (the only address clients ever talk to)
  ▼
gateway            :8080  — Spring Cloud Gateway, routes everything to spring-app,
                             retries transient upstream failures
  │
  ▼
spring-app         :8990 (internal only) — Kotlin/Spring Boot, REST + GraphQL,
  │                                        JWT auth, owns all domain data
  │
  ├── documents.exchange (RabbitMQ, topic exchange)
  │     document.uploaded  → pdf-processing-queue      (processor consumes)
  │                        → notify-uploaded-queue     (spring-app fans out)
  │     document.processed → document-processed-queue  (spring-app: status → PROCESSED)
  │                        → notify-processed-queue    (spring-app fans out)
  │     document.failed    → document-failed-queue     (spring-app: status → FAILED)
  │                        → notify-failed-queue       (spring-app fans out)
  │
  ▼
processor          Python, consumes pdf-processing-queue only. Reads the PDF
                    straight off the shared uploads volume, chunks + embeds it,
                    and writes vectors into the SAME Postgres database
                    spring-app uses (`pdf_document_chunks.document_id` ties
                    each chunk back to a `documents` row). Publishes
                    document.processed / document.failed when done.
```

Every request a client makes goes through the gateway. Document lifecycle
events propagate through RabbitMQ, not direct service-to-service calls — that's
what lets both "update the document's status" and "notify topic subscribers"
happen independently off the same event.

## Running it

```
docker compose up --build
```

This starts Postgres (pgvector), RabbitMQ, the processor, spring-app, and the
gateway. The gateway is the only published port (`GATEWAY_PORT` in `.env`,
default `8080`) — everything else is internal to the compose network.

Set `JWT_SECRET` in `.env` to a real secret before running anywhere but a
laptop; the checked-in default is dev-only.

## Auth

Registration and login are self-issued JWT, handled entirely by spring-app
(no external identity provider).

```
POST /api/auth/register
{ "username": "alice", "email": "alice@example.com", "password": "pw12345" }
→ { "token": "...", "userId": "...", "username": "alice" }

POST /api/auth/login
{ "usernameOrEmail": "alice", "password": "pw12345" }
→ { "token": "...", "userId": "...", "username": "alice" }
```

Send the token on every other request:

```
Authorization: Bearer <token>
```

Tokens expire after `jwt.expiration-ms` (default 24h). `/api/auth/register`,
`/api/auth/login`, `/api/auth/passkey/authenticate/**`, the GraphiQL shell
(`/graph`), the GraphQL websocket handshake (`/graphql-ws`), actuator, and API
docs are the only routes that don't require a token.

### Passkeys (WebAuthn), alongside password login

Additive, not a replacement — an account always has a password and can
optionally also register one or more passkeys. Registering one requires
already being signed in (you're attaching a credential to your account);
authenticating with one is necessarily anonymous, since that's how you get
signed in.

```
# already logged in (Authorization: Bearer <token>):
POST /api/auth/passkey/register/options         → { "publicKey": { ...creation options... } }
# client calls navigator.credentials.create({ publicKey }) (or the platform
# equivalent on mobile), then POSTs the raw result:
POST /api/auth/passkey/register/finish           body: credential.toJSON() verbatim

# anonymous:
POST /api/auth/passkey/authenticate/options       { "usernameOrEmail": "alice" }  (optional — omit for a
                                                     resident/discoverable-credential prompt with no username)
→ { "publicKey": { ...request options... } }
# client calls navigator.credentials.get({ publicKey }), then POSTs the raw result:
POST /api/auth/passkey/authenticate/finish        body: credential.toJSON() verbatim
→ { "token": "...", "userId": "...", "username": "alice" }
```

The two "finish" endpoints take the authenticator's response exactly as the
client serializes it — don't wrap it in another JSON envelope.

`webauthn.rp-id` / `webauthn.origins` (env: `WEBAUTHN_RP_ID`,
`WEBAUTHN_ORIGINS`) default to `localhost` / `http://localhost:8080` for local
dev. **A passkey is bound to the `rp-id` it was created under** — set these to
your real domain before anyone registers a passkey against a deployment, or
every credential registered under the dev default becomes unusable once you
change it.

### Rate limits

Per authenticated user, in-memory token buckets: 120 requests/minute overall,
10 uploads/hour on `POST /api/documents/upload` specifically. Exceeding either
gets a `429` with `{"error": "Too many requests, slow down."}`. Anonymous
requests (registration, login, passkey authentication) aren't limited by this
— only calls carrying a valid token are.

## REST endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Create an account |
| POST | `/api/auth/login` | Get a token |
| GET | `/api/documents/` | List documents (paged). `?mine=true` restricts to the caller's own uploads; omit/false to see everyone's |
| GET | `/api/documents/{id}` | Fetch one document |
| GET | `/api/documents/title?title=` | Search by title |
| GET | `/api/documents/source?source=` | Search by source |
| GET | `/api/documents/category/{topicId}/` | Documents in a topic |
| POST | `/api/documents/create` | Create a document record (no file) |
| POST | `/api/documents/upload` | Multipart upload: `multipartFile`, `title`, `summary`, `source`. Owned by the caller |
| PATCH | `/api/documents/document/{id}/add-categories/?categories=id1,id2` | Attach topics to a document (a document can carry any number of topics); notifies that topic's subscribers |
| GET / POST / PATCH / DELETE | `/api/documents/category/**` | Topic (category) CRUD |
| POST | `/api/topics/{topicId}/subscribe` | Subscribe the caller to a topic |
| DELETE | `/api/topics/{topicId}/subscribe` | Unsubscribe |
| GET | `/api/topics/subscriptions` | Caller's subscriptions |
| GET | `/api/notifications?unreadOnly=&page=&size=` | Caller's notifications, paged |
| PATCH | `/api/notifications/{id}/read` | Mark one read |
| GET | `/api/reports/summary` | Totals: documents by status, users, topics |
| GET | `/api/reports/topics` | Per-topic document + subscriber counts |
| GET | `/api/reports/uploads` | Per-user upload counts |

## GraphQL

Endpoint: `POST /graphql`. Interactive explorer: `/graph`. Schema lives at
`src/main/resources/graphql/schemas.graphql` — `Document`, `DocumentCategory`
(topics), `User`, `Notification` types, full `Query`/`Mutation`, and one
`Subscription`.

`/graph` itself loads for anyone, but it fetches the schema by running a real
introspection query against `/graphql`, which requires auth like everything
else there. Two ways to unlock that without a full login:

- Paste a real bearer token into GraphiQL's Headers panel (full access), or
- Paste `X-Api-Key: <graphql.api-key>` (env: `GRAPHQL_API_KEY`) instead —
  unlocks schema browsing only. Any operation that isn't pure introspection
  (`__schema`/`__type`) is rejected for API-key-only requests, so the key
  can't be used to read or write real data, only to see the schema shape.

```graphql
query {
  allDocs(parameters: { page: 0, size: 20 }, mine: true) {
    content { id title status categories { name } uploadedBy { username } }
  }
}

mutation {
  addCategoriesToDocument(categories: ["<topicId>"], documentId: "<docId>") {
    id categories { name }
  }
}
```

### Live notifications over the websocket

Connect to `/graphql-ws`. On `connection_init`, pass the bearer token in the
payload (not a header — browsers can't set custom headers on a WS handshake,
and this is the standard `graphql-ws` convention):

```json
{ "type": "connection_init", "payload": { "authorization": "Bearer <token>" } }
```

Then subscribe:

```graphql
subscription {
  notifications { id type message documentTitle categoryName createdAt }
}
```

You'll get pushed a `Notification` the moment one is created for you — no
polling needed. The same notifications are also persisted, so
`GET /api/notifications` works as a durable/paged fallback for clients that
don't want to hold a socket open.

## Topics & notifications, end to end

1. A document can carry any number of topics (`DocumentCategory` — the schema
   calls them "categories", the product concept is "topics"). Attach them at
   upload time via `create`, or after the fact via `add-categories`.
2. Any user can `POST /api/topics/{topicId}/subscribe`.
3. When a document is uploaded, attached to a topic, finishes processing, or
   fails processing, everyone subscribed to that document's topic(s) gets a
   `Notification` — persisted and pushed live over the GraphQL subscription.

## Known limitations

- `aiSummary` generation isn't wired up — the processor currently produces
  chunk embeddings only, no summarization model.
- `pdf_document_chunks.document_id` is not a DB-level foreign key on purpose:
  the processor must not depend on spring-app's migrations having already run
  at boot. It's still always populated with a real `documents.id`.
