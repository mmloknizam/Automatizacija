/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SpringRestAppDemo.service.impl;

import com.example.SpringRestAppDemo.entity.*;
import com.example.SpringRestAppDemo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NastavnikPredmetServiceImplTest {

    @Mock private NastavnikPredmetRepository npRepository;
    @Mock private NastavnikRepository nastavnikRepository;
    @Mock private PredmetRepository predmetRepository;

    @InjectMocks
    private NastavnikPredmetServiceImpl service;

    // =========================
    // DODAJ VEZU
    // =========================

    @Test
    void dodaj_success() {

        Nastavnik n = new Nastavnik();
        n.setNastavnikID(1L);

        Predmet p = new Predmet();
        p.setPredmetID(2L);

        when(nastavnikRepository.findById(1L))
                .thenReturn(Optional.of(n));

        when(predmetRepository.findById(2L))
                .thenReturn(Optional.of(p));

        when(npRepository.findByNastavnik_NastavnikIDAndPredmet_PredmetID(1L, 2L))
                .thenReturn(Optional.empty());

        NastavnikPredmet np = new NastavnikPredmet(p, n);

        when(npRepository.save(any())).thenReturn(np);

        NastavnikPredmet result = service.dodaj(1L, 2L);

        assertNotNull(result);
        verify(npRepository, times(1)).save(any());
    }

    @Test
    void dodaj_already_exists() {

        when(nastavnikRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Nastavnik()));

        when(predmetRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Predmet()));

        when(npRepository.findByNastavnik_NastavnikIDAndPredmet_PredmetID(anyLong(), anyLong()))
                .thenReturn(Optional.of(new NastavnikPredmet()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.dodaj(1L, 2L));

        assertEquals("Veza između nastavnika i predmeta već postoji!", ex.getMessage());
    }

    // =========================
    // PREDMETI NASTAVNIKA
    // =========================

    @Test
    void predmetiNastavnika_success() {

        Predmet p1 = new Predmet();
        Predmet p2 = new Predmet();

        NastavnikPredmet np1 = new NastavnikPredmet();
        np1.setPredmet(p1);

        NastavnikPredmet np2 = new NastavnikPredmet();
        np2.setPredmet(p2);

        when(npRepository.findByNastavnik_NastavnikID(1L))
                .thenReturn(List.of(np1, np2));

        List<Predmet> result = service.predmetiNastavnika(1L);

        assertEquals(2, result.size());
    }

    // =========================
    // NASTAVNICI PREDMETA
    // =========================

    @Test
    void nastavniciPredmeta_success() {

        Nastavnik n1 = new Nastavnik();
        Nastavnik n2 = new Nastavnik();

        NastavnikPredmet np1 = new NastavnikPredmet();
        np1.setNastavnik(n1);

        NastavnikPredmet np2 = new NastavnikPredmet();
        np2.setNastavnik(n2);

        when(npRepository.findByPredmet_PredmetID(1L))
                .thenReturn(List.of(np1, np2));

        List<Nastavnik> result = service.nastavniciPredmeta(1L);

        assertEquals(2, result.size());
    }

    // =========================
    // OBRISI VEZU
    // =========================

    @Test
    void obrisiPredmetZaNastavnika_success() {

        NastavnikPredmet np = new NastavnikPredmet();

        when(npRepository.findByNastavnik_NastavnikIDAndPredmet_PredmetID(1L, 2L))
                .thenReturn(Optional.of(np));

        service.obrisiPredmetZaNastavnika(1L, 2L);

        verify(npRepository, times(1)).delete(np);
    }

    // =========================
    // OBRISI NASTAVNIKA
    // =========================

    @Test
    void obrisiNastavnika_success() {

        Nastavnik n = new Nastavnik();

        when(nastavnikRepository.findById(1L))
                .thenReturn(Optional.of(n));

        service.obrisiNastavnika(1L);

        verify(npRepository, times(1)).deleteAllByNastavnik_NastavnikID(1L);
        verify(nastavnikRepository, times(1)).delete(n);
    }
}