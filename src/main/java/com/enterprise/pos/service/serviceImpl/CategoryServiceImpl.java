package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.dto.CategoryDto;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.exceptions.UnAuthorizedException;
import com.enterprise.pos.model.Category;
import com.enterprise.pos.model.Store;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.UserRole;
import com.enterprise.pos.repository.CategoryRepository;
import com.enterprise.pos.repository.StoreRepository;
import com.enterprise.pos.service.CategoryService;
import com.enterprise.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final UserService userService;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        UserDto currentUser = userService.getCurrentUser();
        Store store = storeRepository.findById(categoryDto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        Category category = Category.builder()
                .store(store)
                .categoryName(categoryDto.getCategoryName())
                .build();
        checkAuthority(modelMapper.map(currentUser , User.class) , category.getStore());

        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getCategoriesByStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        List<Category> categoryList = categoryRepository.findByStoreId(store.getId());

       return  categoryList.stream()
                .map(categories -> modelMapper.map(categories , CategoryDto.class))
                .toList();

    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return modelMapper.map(category , CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setCategoryName(categoryDto.getCategoryName());
        return  modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(category);
    }

    public void checkAuthority(User user  , Store store){
        boolean isAdmin = user.getRoles().contains(UserRole.ROLE_ADMIN);
        boolean isManager = user.getRoles().contains(UserRole.ROLE_STORE_MANAGER);
        boolean isSameStore = store.getStoreAdmin().getId().equals(user.getId());

        if(!(isAdmin && isSameStore) && !isManager ) {
            throw new UnAuthorizedException("You don't have permission to perform to manage this category");
        }
        //ram => Admin
        //store => Admin
         // it's !true = false or true =  false

        //Pablo => ROLE_CASHIER
        //true && true == true pablo dont have an permission
    }
}
