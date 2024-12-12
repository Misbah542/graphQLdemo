package com.misbahminiproject.graphqldemo


import com.apollographql.apollo3.ApolloClient

object RickAndMortyApiClient {
    val apolloClient: ApolloClient = ApolloClient.Builder()
        .serverUrl("https://rickandmortyapi.com/graphql")
        .build()
}
