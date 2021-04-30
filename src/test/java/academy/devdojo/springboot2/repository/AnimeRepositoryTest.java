package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest {
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when successful")
    void save_PersistsAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(animeToBeSaved);

        Assertions.assertEquals(animeToBeSaved,animeSaved);
        Assertions.assertEquals(animeToBeSaved.getName(),animeSaved.getName());
        Assertions.assertNotNull(animeSaved.getId());
    }

    @Test
    @DisplayName("Save updates anime when successful")
    void save_UpdatesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(animeToBeSaved);
        animeSaved.setName("Naruto");
        Anime animeUpdated = animeRepository.save(animeSaved);

        Assertions.assertNotNull(animeUpdated);
        Assertions.assertNotNull(animeUpdated.getId());
        Assertions.assertEquals(animeSaved.getName(),animeUpdated.getName());
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(animeToBeSaved);
        animeRepository.delete(animeSaved);
        Optional<Anime> animeOptional = animeRepository.findById(animeSaved.getId());

        Assertions.assertTrue(animeOptional.isEmpty());
    }

    @Test
    @DisplayName("Find by name returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        Anime animeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = animeRepository.save(animeToBeSaved);
        List<Anime> animes = animeRepository.findByName(animeSaved.getName());

        Assertions.assertFalse(animes.isEmpty());
        Assertions.assertTrue(animes.contains(animeSaved));
    }

    @Test
    @DisplayName("Find by name returns empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        List<Anime> animes = animeRepository.findByName("Naruto");

        Assertions.assertTrue(animes.isEmpty());
    }

    @Test
    @DisplayName("Save throw ConstraintViolationException when name is empty")
    void save_ThrowConstraintViolationException_WhenNameIsEmpty(){
        Anime anime = new Anime();

        Assertions.assertThrows(ConstraintViolationException.class,() -> animeRepository.save(anime),
                "The anime name cannot be empty");

    }
}