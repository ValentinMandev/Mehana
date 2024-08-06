package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.PromoEntity;
import com.softuni.mehana.repository.PromoRepository;
import com.softuni.mehana.service.PromoService;
import com.softuni.mehana.service.exception.PromotionsNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoServiceImpl implements PromoService {

    private final PromoRepository promoRepository;

    public PromoServiceImpl(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }


    @Override
    public List<PromoEntity> getAllPromotions() {
        List<PromoEntity> promotions = promoRepository.findAll();

        if (promotions.isEmpty()) {
            throw new PromotionsNotFoundException("No promotions found in PromoRepository");
        }

        return promotions;
    }
}
