/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.SpringRestAppDemo.service.impl;

import com.example.SpringRestAppDemo.dto.*;
import com.example.SpringRestAppDemo.entity.*;
import com.example.SpringRestAppDemo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KorisnickiProfilServiceImplTest {

    @Mock private KorisnickiProfilRepository korisnickiProfilRepository;
    @Mock private UlogaRepository ulogaRepository;
    @Mock private VerifikacijaRepository verifikacijaRepository;
    @Mock private NastavnikRepository nastavnikRepository;
    @Mock private ZvanjeRepository zvanjeRepository;

    @InjectMocks
    private KorisnickiProfilServiceImpl service;

    // =========================
    // LOGIN
    // =========================

    @Test
    void login_success() throws Exception {
        LoginRequestDto request = new LoginRequestDto("test@fon.bg.ac.rs", "Password1!");

        Uloga uloga = new Uloga();
        uloga.setTip("Administrator");

        KorisnickiProfil korisnik = new KorisnickiProfil();
        korisnik.setEmail("test@fon.bg.ac.rs");
        korisnik.setLozinka("Password1!");
        korisnik.setEnabled(true);
        korisnik.setUloga(uloga);
        korisnik.setKorisnickiProfilID(1L);

        when(korisnickiProfilRepository.findByEmail("test@fon.bg.ac.rs"))
                .thenReturn(Optional.of(korisnik));

        LoginResponseDto response = service.login(request);

        assertNotNull(response);
        assertEquals("test@fon.bg.ac.rs", response.getEmail());
        assertEquals("Administrator", response.getUloga());

        verify(korisnickiProfilRepository, times(1))
                .findByEmail("test@fon.bg.ac.rs");
    }

    @Test
    void login_user_not_found() {
        when(korisnickiProfilRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(Exception.class,
                () -> service.login(new LoginRequestDto("x", "y")));

        assertEquals("Korisnik ne postoji", ex.getMessage());
    }

    @Test
    void login_wrong_password() {
        KorisnickiProfil k = new KorisnickiProfil();
        k.setEmail("test@fon.bg.ac.rs");
        k.setLozinka("Password1!");
        k.setEnabled(true);

        when(korisnickiProfilRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(k));

        Exception ex = assertThrows(Exception.class,
                () -> service.login(new LoginRequestDto("test@fon.bg.ac.rs", "wrong")));

        assertEquals("Pogrešna lozinka", ex.getMessage());
    }

    @Test
    void login_not_enabled() {
        KorisnickiProfil k = new KorisnickiProfil();
        k.setEmail("test@fon.bg.ac.rs");
        k.setLozinka("Password1!");
        k.setEnabled(false);

        when(korisnickiProfilRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(k));

        Exception ex = assertThrows(Exception.class,
                () -> service.login(new LoginRequestDto("test@fon.bg.ac.rs", "Password1!")));

        assertEquals("Nalog nije aktiviran. Proverite email i potvrdite kod.", ex.getMessage());
    }

    // =========================
    // REGISTER (minimal test)
    // =========================

    @Test
    void register_email_exists() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@fon.bg.ac.rs");
        dto.setLozinka("Password1!");
        dto.setUlogaID(1L);

        when(korisnickiProfilRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(new KorisnickiProfil()));

        Exception ex = assertThrows(Exception.class,
                () -> service.register(dto));

        assertEquals("Korisnik sa ovim email-om već postoji.", ex.getMessage());
    }

    // =========================
    // DELETE PROFILE
    // =========================

    @Test
    void delete_profile_success() throws Exception {
        KorisnickiProfil k = new KorisnickiProfil();
        k.setLozinka("Password1!");

        when(korisnickiProfilRepository.findById(1L))
                .thenReturn(Optional.of(k));

        BrisanjeProfilaDto dto = new BrisanjeProfilaDto();
        dto.setLozinka("Password1!");

        service.obrisiProfil(1L, dto);

        verify(korisnickiProfilRepository, times(1))
                .delete(k);
    }

    // =========================
    // CHANGE PASSWORD
    // =========================

    @Test
    void change_password_wrong_old() {
        KorisnickiProfil k = new KorisnickiProfil();
        k.setLozinka("Old123!");

        when(korisnickiProfilRepository.findById(1L))
                .thenReturn(Optional.of(k));

        PromenaLozinkeDto dto = new PromenaLozinkeDto();
        dto.setStaraLozinka("wrong");
        dto.setNovaLozinka("NewPass1!");

        Exception ex = assertThrows(Exception.class,
                () -> service.promeniLozinku(1L, dto));

        assertEquals("Stara lozinka nije tačna!", ex.getMessage());
    }
    
    @Test
    void register_shouldFail_whenEmailInvalidDomain() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@gmail.com");
        dto.setLozinka("Password1!");
        dto.setUlogaID(1L);

        Exception ex = assertThrows(Exception.class,
                () -> service.register(dto));

        assertEquals("Email mora biti u formatu @fon.bg.ac.rs", ex.getMessage());
    }
    
    @Test
    void register_shouldFail_whenPasswordTooShort() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("test@fon.bg.ac.rs");
        dto.setLozinka("123");
        dto.setUlogaID(1L);

        Exception ex = assertThrows(Exception.class,
                () -> service.register(dto));

        assertEquals("Lozinka mora imati najmanje 8 karaktera", ex.getMessage());
    }
}