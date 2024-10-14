package org.movieflix.movieapp.Service;

import org.movieflix.movieapp.dto.MovieDto;

import java.util.List;

public record MoviePageResponse(List<MovieDto> movieDto,
                                Integer currentPageNumber,
                                Integer totalPages,
                                Integer pageSize,
                                Integer totalElements,
                                boolean isLast
) {


}
