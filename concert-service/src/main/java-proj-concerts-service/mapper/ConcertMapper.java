package proj.concert.service.mapper;

import proj.concert.service.domain.*;
import proj.concert.common.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import proj.concert.service.jaxrs.LocalDateTimeParam;

/**
 * Helper class to convert between domain-model and DTO objects representing
 * Concerts
 */


public class ConcertMapper {

    public static Concert toDomainModel(ConcertDTO dtoConcert) {
        Concert fullconcert = new Concert(
                dtoConcert.getId(),
                dtoConcert.getTitle(),
                dtoConcert.getImageName(),
                dtoConcert.getBlurb());

        Set<LocalDateTime> dates = new HashSet<>(dtoConcert.getDates());
        fullconcert.setDates(dates);

        List<PerformerDTO> performersDTOs = new ArrayList<>(dtoConcert.getPerformers());
        List<Performer> performers = new ArrayList<>();
        for (PerformerDTO performerDTO : performersDTOs) {
            performers.add(PerformerMapper.toDomainModel(performerDTO));
        }
        fullconcert.setPerformers(performers);

        return fullconcert;
    }

    public static ConcertDTO toDto(Concert concert) {
        ConcertDTO dtoConcert =
                new ConcertDTO(
                        concert.getId(),
                        concert.getTitle(),
                        concert.getImageName(),
                        concert.getBlurb());
                List<LocalDateTime> dates = new ArrayList<LocalDateTime>(concert.getDates());

                dtoConcert.setDates(dates);
                List<Performer> performers = concert.getPerformers();
                List<PerformerDTO> performerDTOs = new ArrayList<PerformerDTO>();
                for (Performer performer : performers) {
                    performerDTOs.add(PerformerMapper.toDto(performer));
                }
                dtoConcert.setPerformers(performerDTOs);
        return dtoConcert;
    }

    public static ConcertSummaryDTO toSummaryDTO(Concert concert) {
        ConcertSummaryDTO dtoConcertSummary = new ConcertSummaryDTO(
                concert.getId(),
                concert.getTitle(),
                concert.getImageName()
        );
        return dtoConcertSummary;
    }
}
