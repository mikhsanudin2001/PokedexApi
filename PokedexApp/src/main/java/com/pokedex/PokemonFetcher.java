package com.pokedex;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PokemonFetcher {
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon";

    public static List<Pokemon> fetchPokemon() {
        List<Pokemon> pokemonList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "?limit=10")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode resultsNode = objectMapper.readTree(responseData).path("results");

                for (JsonNode node : resultsNode) {
                    String pokemonUrl = node.get("url").asText();
                    Pokemon pokemon = fetchPokemonDetails(client, objectMapper, pokemonUrl);
                    pokemonList.add(pokemon);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pokemonList;
    }

    private static Pokemon fetchPokemonDetails(OkHttpClient client, ObjectMapper objectMapper, String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Pokemon pokemon = new Pokemon();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                JsonNode node = objectMapper.readTree(responseData);
                pokemon.setName(node.get("name").asText());
                pokemon.setHeight(node.get("height").asInt());
                pokemon.setWeight(node.get("weight").asInt());

                List<String> types = new ArrayList<>();
                for (JsonNode typeNode : node.path("types")) {
                    types.add(typeNode.path("type").path("name").asText());
                }
                pokemon.setTypes(types);

                String imageUrl = node.path("sprites").path("front_default").asText();
                pokemon.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pokemon;
    }
}
