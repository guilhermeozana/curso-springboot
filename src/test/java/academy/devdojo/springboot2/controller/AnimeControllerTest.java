package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeCreator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));


        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessful(){
        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertNotNull(animePage);
        Assertions.assertFalse(animePage.isEmpty());
        Assertions.assertEquals(1,animePage.getSize());
        Assertions.assertEquals(AnimeCreator.createValidAnime().getName(),animePage.toList().get(0).getName());
    }

    @Test
    @DisplayName("listAll Returns list of anime when successful")
    void listAll_ReturnsListOfAnime_WhenSuccessful(){
        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertFalse(animes.isEmpty());
        Assertions.assertNotNull(animes);
        Assertions.assertEquals(1,animes.size());
        Assertions.assertEquals(AnimeCreator.createValidAnime().getName(), animes.get(0).getName());
    }

    @Test
    @DisplayName("findById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful(){
        Anime anime = animeController.findById(1).getBody();

        Assertions.assertNotNull(anime);
        Assertions.assertNotNull(anime.getId());
        Assertions.assertEquals(AnimeCreator.createValidAnime().getId(),anime.getId());
    }

    @Test
    @DisplayName("findByName returns list of anime when successful")
    void findByName_ReturnsListOfAnime_WhenSuccessful(){
        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertFalse(animes.isEmpty());
        Assertions.assertNotNull(animes);
        Assertions.assertEquals(AnimeCreator.createValidAnime().getName(),animes.get(0).getName());
        Assertions.assertEquals(1,animes.size());
    }

    @Test
    @DisplayName("findByName returns empty list when anime is not found")
    void findByName_ReturnsEmptyList_WhenAnimeIsNotFound(){
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("anime").getBody();

        Assertions.assertTrue(animes.isEmpty());
        Assertions.assertNotNull(animes);
    }

    @Test
    @DisplayName("save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful(){
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertNotNull(anime);
        Assertions.assertEquals(AnimeCreator.createValidAnime(),anime);

    }

    @Test
    @DisplayName("replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful(){
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,entity.getStatusCode());
    }

    @Test
    @DisplayName("delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful(){
        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(HttpStatus.NO_CONTENT,entity.getStatusCode());
    }

}