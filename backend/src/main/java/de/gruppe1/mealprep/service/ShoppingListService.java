package de.gruppe1.mealprep.service;

import de.gruppe1.mealprep.dto.ManualItemRequestDto;
import de.gruppe1.mealprep.dto.ShoppingListItemDto;
import de.gruppe1.mealprep.entity.ShoppingListItem;
import de.gruppe1.mealprep.exception.NotFoundException;
import de.gruppe1.mealprep.mapper.ShoppingListMapper;
import de.gruppe1.mealprep.repository.ShoppingListItemRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListItemRepository itemRepository;
    private final ShoppingListMapper mapper;

    public ShoppingListService(ShoppingListItemRepository itemRepository, ShoppingListMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    public List<ShoppingListItemDto> getList() {
        return itemRepository.findAll(Sort.by("checked", "name"))
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ShoppingListItemDto addManualItem(ManualItemRequestDto request) {
        ShoppingListItem item = new ShoppingListItem();
        item.setName(request.name().trim());
        item.setMeasure(request.measure() == null ? "" : request.measure().trim());
        item.setManual(true);
        return mapper.toDto(itemRepository.save(item));
    }

    public ShoppingListItemDto setChecked(Long id, boolean checked) {
        ShoppingListItem item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Artikel nicht gefunden"));
        item.setChecked(checked);
        return mapper.toDto(itemRepository.save(item));
    }

    @Transactional
    public void deleteCheckedItems() {
        itemRepository.deleteByCheckedTrue();
    }
}