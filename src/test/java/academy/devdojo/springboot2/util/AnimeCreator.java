package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("DBZ")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("DBZ")
                .id(1L)
                .build();
    }

    public static Anime createValidUpdatedAnime(){
        return Anime.builder()
                .name("Naruto")
                .id(1L)
                .build();
    }
}
