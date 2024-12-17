package com.misbahminiproject.graphqldemo


import com.apollographql.apollo3.ApolloClient

object RickAndMortyApiClient {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://rickandmortyapi.com/graphql")
        .build()
}


// For Subscription
/* implementation "com.apollographql.apollo3:apollo-websocket:4.0.0"

val apolloClient = ApolloClient.Builder()
    .serverUrl("example_api")
    .subscriptionNetworkTransport(WebSocketNetworkTransport.Builder().serverUrl("wss:example_api").build())
    .build()

 */


// For Normalized Cache
/* implementation "com.apollographql.apollo3:apollo-normalized-cache-sqlite:4.0.0"
val cacheFactory = SqlNormalizedCacheFactory(context, "apollo_cache")
val apolloClient = ApolloClient.Builder()
    .serverUrl("example_api")
    .normalizedCache(cacheFactory)
    .build()


context: The application context needed to access the database.
"apollo_cache": The name of the SQLite database where the cache is stored.
 */

/*
FetchPolicy.CacheFirst: Use cache first, then fallback to network.
FetchPolicy.NetworkOnly: Always fetch fresh data from the network.
FetchPolicy.CacheAndNetwork: Fetch from cache and update with network data.
 */

/*
initial response

{
  "characters": [
    { "id": "1", "name": "Rick", "status": "Alive" },
    { "id": "2", "name": "Morty", "status": "Alive" }
  ]
}

normalized cache response

{
  "Character:1": { "id": "1", "name": "Rick", "status": "Alive" },
  "Character:2": { "id": "2", "name": "Morty", "status": "Alive" }
}


 */



/*

apolloClient.mutation(CreateCharacterMutation())
    .refetchQueries(listOf(GetCharactersQuery()))
    .execute()


apolloClient.apolloStore.writeOperation(
    GetCharactersQuery(),
    GetCharactersQuery.Data(characters = mutationResult.data?.newCharacter)
)
 */













/*
./gradlew :app:generateApolloSource

./gradlew downloadApolloSchema \
  --endpoint="https://rickandmortyapi.com/graphql" \
  --schema="app/src/main/graphql/com/misbahminiproject/graphqldemo/schema.graphqls"

 */