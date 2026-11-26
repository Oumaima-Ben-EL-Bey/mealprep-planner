package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.PantryItemRequestDto;
import de.gruppe1.mealprep.entity.PantryItem;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.repository.PantryItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PantryService {
    private final PantryItemRepository pantryItemRepository;

    public PantryService(PantryItemRepository pantryItemRepository) {
        this.pantryItemRepository = pantryItemRepository;
    }

    @Transactional(readOnly = true)
    public List<PantryItem> getAll(){
        return pantryItemRepository.findAll();
    }

    @Transactional
    public PantryItem create(PantryItemRequestDto requestDto){
        PantryItem item = new PantryItem();
        item.setName(requestDto.name().trim());
        item.setMeasure(requestDto.measure());
        return pantryItemRepository.save(item);
    }

    @Transactional
    public PantryItem update(Long id, PantryItemRequestDto requestDto){
        Optional<PantryItem> optionalItem = pantryItemRepository.findById(id);
        if(optionalItem.isEmpty()){
            throw new NotFoundException("Vorratsartikel nicht gefunden");
        }
        PantryItem item = optionalItem.get();
        item.setName(requestDto.name().trim());
        item.setMeasure(requestDto.measure());
        return pantryItemRepository.save(item);
    }

    @Transactional
    public void delete(Long id){
        if(!pantryItemRepository.existsById(id)){
            throw new NotFoundException("Vorratsartikel nicht gefunden");
        }
        pantryItemRepository.deleteById(id);
    }
}
