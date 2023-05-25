package tn.esprit.spring.sevices;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tn.esprit.spring.entities.Train;
import tn.esprit.spring.entities.Ville;
import tn.esprit.spring.entities.Voyage;
import tn.esprit.spring.entities.Voyageur;
import tn.esprit.spring.repository.TrainRepository;
import tn.esprit.spring.repository.VoyageRepository;
import tn.esprit.spring.repository.VoyageurRepository;
import tn.esprit.spring.services.TrainServiceImpl;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class trainServiceTrain {

    @Mock
    private VoyageurRepository voyageurRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private VoyageRepository voyageRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    @Test
    public void testAjouterTrain() {
        Train train = new Train();

        trainService.ajouterTrain(train);

        verify(trainRepository, times(1)).save(train);
    }

    @Test
    public void testTrainPlacesLibres() {
        Ville nomGareDepart = Ville.tunis;

        List<Voyage> voyages = new ArrayList<>();

        Train train = new Train();
        train.setNbPlaceLibre(2);
        Voyage voyage = new Voyage();
        voyage.setTrain(train);
        voyage.setGareDepart(Ville.tunis);

        voyages.add(voyage);

        when(voyageRepository.findAll()).thenReturn(voyages);

        int placesLibres = trainService.TrainPlacesLibres(nomGareDepart);

        assertEquals(2, placesLibres);
    }

    @Test
    public void testListerTrainsIndirects() {
        Ville nomGareDepart = Ville.tunis;
        Ville nomGareArrivee = Ville.SOUSSE;

        List<Voyage> voyages = new ArrayList<>();
        Voyage voyage1 = new Voyage();
        Voyage voyage2 = new Voyage();

        voyage1.setGareDepart(Ville.tunis);
        voyage1.setGareArrivee(Ville.SOUSSE);
        voyage2.setGareDepart(Ville.SOUSSE);
        voyage2.setGareArrivee(Ville.SOUSSE);
        voyages.add(voyage1);
        voyages.add(voyage2);

        when(voyageRepository.findAll()).thenReturn(voyages);

        List<Train> trainsIndirects = trainService.ListerTrainsIndirects(nomGareDepart, nomGareArrivee);

        assertEquals(2, trainsIndirects.size());
    }

    @Test
    public void testAffecterTainAVoyageur() {
        Long idVoyageur = 1L;
        Ville nomGareDepart = Ville.tunis;
        Ville nomGareArrivee = Ville.SOUSSE;
        double heureDepart = 9.0;

        List<Voyage> voyages = new ArrayList<>();
        Voyage voyage = new Voyage();
        Train train = new Train();
        train.setNbPlaceLibre(1);
        voyage.setTrain(train);
        voyage.setMesVoyageurs(Collections.emptyList());
        voyages.add(voyage);
        when(voyageRepository.RechercheVoyage(nomGareDepart, nomGareDepart, heureDepart)).thenReturn(voyages);

        Voyageur voyageur = new Voyageur();
        voyageur.setIdVoyageur(115L);
        when(voyageurRepository.findById(idVoyageur)).thenReturn(Optional.of(voyageur));


        trainService.affecterTainAVoyageur(idVoyageur, nomGareDepart, nomGareArrivee, heureDepart);

        verify(voyageRepository, times(1)).save(voyage);
        assertTrue(voyage.getMesVoyageurs().contains(voyageur));
    }

    @Test
    public void testDesaffecterVoyageursTrain() {
        Ville nomGareDepart = Ville.tunis;
        Ville nomGareArrivee = Ville.SOUSSE;
        double heureDepart = 9.0;

        List<Voyage> voyages = new ArrayList<>();
        Voyage voyage = new Voyage();
        Train train = new Train();

        voyage.setTrain(train);
        voyage.setMesVoyageurs(Arrays.asList(new Voyageur(), new Voyageur(), new Voyageur(), new Voyageur()));
        voyages.add(voyage);

        when(voyageRepository.RechercheVoyage(nomGareDepart, nomGareArrivee, heureDepart)).thenReturn(voyages);

        trainService.DesaffecterVoyageursTrain(nomGareDepart, nomGareArrivee, heureDepart);

        verify(voyageRepository, times(1)).save(any(Voyage.class));
        verify(trainRepository, times(1)).save(any(Train.class));
    }

}