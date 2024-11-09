// AppUserDataService.java
package com.carparkspring.demo.service;

import com.carparkspring.demo.model.AppUserData;
import com.carparkspring.demo.repository.AppUserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserDataService {

    @Autowired
    private AppUserDataRepository appUserDataRepository;

    public List<AppUserData> getAllUsers() {
        return appUserDataRepository.findAll();
    }

    public Optional<AppUserData> getUserById(Long id) {
        return appUserDataRepository.findById(id);
    }

    public AppUserData saveUser(AppUserData appUserData) {
        return appUserDataRepository.save(appUserData);
    }

    public void deleteUser(Long id) {
        appUserDataRepository.deleteById(id);
    }
}