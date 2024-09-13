package com.example.eztask.config.page;

import java.util.List;
import org.springframework.data.domain.Page;

public class PageResponseDto<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;

    public static <T> PageResponseDto<T> of(Page<T> page) {
        PageResponseDto<T> pageResponseDto = new PageResponseDto<>();
        pageResponseDto.content = page.getContent();
        pageResponseDto.totalElements = page.getTotalElements();
        pageResponseDto.totalPages = page.getTotalPages();
        pageResponseDto.number = page.getNumber();
        pageResponseDto.size = page.getSize();
        return pageResponseDto;
    }

}
