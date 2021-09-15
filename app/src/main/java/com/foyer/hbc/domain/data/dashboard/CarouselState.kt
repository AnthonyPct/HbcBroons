package com.foyer.hbc.domain.data.dashboard

sealed class CarouselState {
    data class Success(val carouselCells: List<CarouselCellType>) : CarouselState()
    object Error : CarouselState()
    object Loading : CarouselState()
}
