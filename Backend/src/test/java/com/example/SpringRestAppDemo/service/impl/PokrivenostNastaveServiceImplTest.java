/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SpringRestAppDemo.service.impl;

import com.example.SpringRestAppDemo.dto.*;
import com.example.SpringRestAppDemo.entity.*;
import com.example.SpringRestAppDemo.mapper.impl.PokrivenostNastaveDtoEntityMapper;
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
class PokrivenostNastaveServiceImplTest {

    @Mock private PokrivenostNastaveRepository pokrivenostNastaveRepository;
    @Mock private PokrivenostNastaveDtoEntityMapper mapper;
    @Mock private SkolskaGodinaRepository skolskaGodinaRepository;
    @Mock private NastavnikPredmetRepository nastavnikPredmetRepository;

    @InjectMocks
    private PokrivenostNastaveServiceImpl service;

    // =========================
    // FIND ALL
    // =========================

    @Test
    void findAll_success() {

        PokrivenostNastave entity = new PokrivenostNastave();
        PokrivenostNastaveDto dto = new PokrivenostNastaveDto();

        when(pokrivenostNastaveRepository.findAll())
                .thenReturn(List.of(entity));

        when(mapper.toDto(entity)).thenReturn(dto);

        List<PokrivenostNastaveDto> result = service.findAll();

        assertEquals(1, result.size());
        verify(mapper, times(1)).toDto(entity);
    }

    // =========================
    // DELETE ONE
    // =========================

    @Test
    void deleteOne_success() {

        service.deleteOne(1L);

        verify(pokrivenostNastaveRepository, times(1))
                .deleteById(1L);
    }

    // =========================
    // DELETE BATCH
    // =========================

    @Test
    void deleteBatch_success() {

        service.deleteBatch(List.of(1L, 2L, 3L));

        verify(pokrivenostNastaveRepository, times(1))
                .deleteAllById(List.of(1L, 2L, 3L));
    }

    // =========================
    // FIND BY PREDMET + GODINA
    // =========================

    @Test
    void findByPredmetAndGodina_success() {

        PokrivenostNastave entity = new PokrivenostNastave();
        PokrivenostNastaveDto dto = new PokrivenostNastaveDto();

        when(pokrivenostNastaveRepository
                .findByPredmet_PredmetIDAndSkolskaGodina_SkolskaGodinaID(1L, 2L))
                .thenReturn(List.of(entity));

        when(mapper.toDto(entity)).thenReturn(dto);

        List<PokrivenostNastaveDto> result =
                service.findByPredmetAndGodina(1L, 2L);

        assertEquals(1, result.size());
    }

    // =========================
    // SAVE PLAN - BASIC SUCCESS
    // =========================

    @Test
    void savePlan_success() {

        Predmet p = new Predmet();
        p.setPredmetID(1L);

        SkolskaGodina g = new SkolskaGodina();
        g.setSkolskaGodinaID(1L);

        OblikNastave o = new OblikNastave();
        o.setOblikNastaveID(1L);

        PokrivenostNastaveDto dto = new PokrivenostNastaveDto();
        dto.setPredmet(p);
        dto.setSkolskaGodina(g);
        dto.setOblikNastave(o);
        dto.setBrojSatiNastave(10);

        when(pokrivenostNastaveRepository.sumSati(1L, 1L, 1L))
                .thenReturn(0);

        when(pokrivenostNastaveRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(mapper.toEntity(dto)).thenReturn(new PokrivenostNastave());
        when(mapper.toDto(any())).thenReturn(dto);

        PokrivenostNastaveDto result = service.savePlan(dto);

        assertNotNull(result);
        verify(pokrivenostNastaveRepository, times(1)).save(any());
    }

    // =========================
    // SAVE PLAN - OVER LIMIT
    // =========================

    @Test
    void savePlan_overLimit_shouldThrow() {

        Predmet p = new Predmet();
        p.setPredmetID(1L);

        SkolskaGodina g = new SkolskaGodina();
        g.setSkolskaGodinaID(1L);

        OblikNastave o = new OblikNastave();
        o.setOblikNastaveID(1L);

        PokrivenostNastaveDto dto = new PokrivenostNastaveDto();
        dto.setPredmet(p);
        dto.setSkolskaGodina(g);
        dto.setOblikNastave(o);
        dto.setBrojSatiNastave(1000);

        when(pokrivenostNastaveRepository.sumSati(1L, 1L, 1L))
                .thenReturn(60);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.savePlan(dto));

        assertTrue(ex.getMessage().contains("Maksimum"));
    }

    // =========================
    // DELETE BY GODINA
    // =========================

    @Test
    void deleteByGodina_success() {

        service.deleteByGodina(1L);

        verify(pokrivenostNastaveRepository, times(1))
                .deleteBySkolskaGodina_SkolskaGodinaID(1L);
    }
}
