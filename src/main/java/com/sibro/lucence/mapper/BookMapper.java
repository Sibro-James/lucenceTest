package com.sibro.lucence.mapper;

import com.sibro.lucence.dto.BookDTO;

import java.util.List;

public interface BookMapper {
//    @Select("SELECT * FROM book WHERE 1=1;")
    public List<BookDTO> queryBookList();
}
