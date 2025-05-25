package proj.concert.service.services;

import java.awt.print.Book;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.persistence.*;
import javax.swing.*;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;

import org.apache.commons.logging.Log;
import org.jboss.resteasy.annotations.Query;
import proj.concert.common.dto.*;
import proj.concert.service.domain.*;
import proj.concert.service.jaxrs.LocalDateTimeParam;
import proj.concert.service.mapper.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/concert-service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    private static final Map<String, User> tokens = new ConcurrentHashMap<>();

    private User getUserFromToken(String token) {
        return tokens.get(token);
    }

    // WORKING METHODS

    @GET
    @Path("/concerts/{id}")
    public Response getConcert(@PathParam("id") Long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            Concert concert = em.find(Concert.class, id);
            if (concert == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            ConcertDTO dto = ConcertMapper.toDto(concert);

            return Response.ok(dto).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts")
    public Response getConcerts(){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class).getResultList();
            if (concerts.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            List<ConcertDTO> dtos = new ArrayList<>();
            for (Concert concert : concerts){
                dtos.add(ConcertMapper.toDto(concert));
            }

            return Response.ok(dtos).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/concerts/summaries")
    public Response getConcertsSummaries(){
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            List<Concert> concerts = em.createQuery("select c from Concert c", Concert.class).getResultList();
            if (concerts.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            List<ConcertSummaryDTO> summaryDTOs = new ArrayList<>();
            for (Concert concert : concerts){
                summaryDTOs.add(ConcertMapper.toSummaryDTO(concert));
            }
            return Response.ok(summaryDTOs).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers/{id}")
    public Response getPerformer(@PathParam("id") Long id) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try{
            Performer performer = em.find(Performer.class, id);
            if (performer == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            PerformerDTO dto = PerformerMapper.toDto(performer);
            return Response.ok(dto).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/performers")
    public Response getPerformers() {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            List<Performer> performers = em.createQuery("SELECT p FROM Performer p", Performer.class).getResultList();
            List<PerformerDTO> dtoPerformers = performers.stream()
                    .map(PerformerMapper::toDto)
                    .collect(Collectors.toList());
            return Response.ok(dtoPerformers).build();
        } finally {
            em.close();
        }
    }


    //TODO - WORK IN PROGRESS METHODS - ROHAAN


    //TODO - UNTESTED METHODS - NIK

    @POST
    @Path("/login")
    public Response login(UserDTO credentials) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", credentials.getUsername());

            List<User> users = query.getResultList();
            if (users.isEmpty() || !users.get(0).getPassword().equals(credentials.getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            User user = users.get(0);
            String token = UUID.randomUUID().toString();
            tokens.put(token, user);

            NewCookie authCookie = new NewCookie("auth", token, "/", null, null, -1, false);

            return Response.ok().cookie(authCookie).build();
        } finally {
            em.close();
        }
    }




    @GET
    @Path("{id}/availability")
    public Response getAvailability(@PathParam("id") Long concertId,
                                    @QueryParam("date") LocalDateTimeParam dateParam) {
        if (dateParam == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        LocalDateTime date = dateParam.getLocalDateTime();
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            Concert concert = em.find(Concert.class, concertId);
            if (concert == null || !concert.getDates().contains(date)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            TypedQuery<Seat> query = em.createQuery(
                    "SELECT s FROM Seat s WHERE s.date = :date", Seat.class);
            query.setParameter("date", date);
            List<SeatDTO> result = query.getResultList().stream()
                    .map(seat -> new SeatDTO(seat.getLabel(), seat.getCost()))
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } finally {
            em.close();
        }
    }

    @POST
    @Path("/bookings")
    public Response makeBooking(BookingRequestDTO request,
                                @Context HttpHeaders headers,
                                @Context UriInfo uriInfo
                                ) {

        Cookie cookie = headers.getCookies().get("auth");
        if (cookie == null || cookie.getValue().isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = cookie.getValue();
        User user = getUserFromToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            Concert concert = em.find(Concert.class, request.getConcertId());
            if (concert == null || concert.getDates() == null || !concert.getDates().contains(request.getDate())) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            tx.begin();

            Set<Seat> selectedSeats = new HashSet<>();
            for (String label : request.getSeatLabels()) {
                Seat.SeatId seatId = new Seat.SeatId(label, request.getDate());
                Seat seat = em.find(Seat.class, seatId);

                if (seat == null) {
                    tx.rollback();
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }

                if (seat.isBooked()) {
                    tx.rollback();
                    return Response.status(Response.Status.FORBIDDEN).build();
                }

                seat.setBooked(true);
                em.merge(seat);
                selectedSeats.add(seat);
            }

            Reservation reservation = new Reservation(user, concert, request.getDate(), selectedSeats);
            em.persist(reservation);

            tx.commit();

            notifySubscribers(request.getDate(), concert.getId());

            URI bookingUri = uriInfo.getAbsolutePathBuilder()
                    .path(Long.toString(reservation.getId()))
                    .build();
            return Response.created(bookingUri).build();

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } finally {
            em.close();
        }
    }





    @GET
    @Path("/bookings")
    public Response getBookings(@Context HttpHeaders headers) {

        Cookie cookie = headers.getCookies().get("auth");
        if (cookie == null || cookie.getValue().isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = cookie.getValue();
        User user = getUserFromToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            TypedQuery<Reservation> query = em.createQuery(
                    "SELECT r FROM Reservation r WHERE r.user = :user", Reservation.class);
            query.setParameter("user", user);

            List<BookingDTO> bookings = query.getResultList().stream().map(res -> {
                List<SeatDTO> seats = res.getSeats().stream()
                        .map(seat -> new SeatDTO(seat.getLabel(), seat.getCost()))
                        .collect(Collectors.toList());
                return new BookingDTO(res.getConcert().getId(), res.getDate(), seats);
            }).collect(Collectors.toList());

            return Response.ok(bookings).build();
        } finally {
            em.close();
        }
    }



    @GET
    @Path("/seats/{date}")
    public Response getSeats(@PathParam("date") String dateStr,
                             @QueryParam("status") String statusParam) {
        LocalDateTime date;
        try {
            date = LocalDateTime.parse(dateStr);
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            TypedQuery<Seat> query = em.createQuery(
                    "SELECT s FROM Seat s WHERE s.date = :date", Seat.class);
            query.setParameter("date", date);
            List<Seat> seats = query.getResultList();

            List<SeatDTO> result = seats.stream()
                    .filter(seat -> {
                        switch (statusParam) {
                            case "Booked": return seat.isBooked();
                            case "Unbooked": return !seat.isBooked();
                            case "Any": return true;
                            default: return false;
                        }
                    })
                    .map(seat -> new SeatDTO(seat.getLabel(), seat.getCost()))
                    .collect(Collectors.toList());

            return Response.ok(result).build();
        } finally {
            em.close();
        }
    }

    @GET
    @Path("/bookings/{id}")
    public Response getBooking(@PathParam("id") Long id,
                               @Context HttpHeaders headers) {

        Cookie cookie = headers.getCookies().get("auth");
        if (cookie == null || cookie.getValue().isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String token = cookie.getValue();
        User user = getUserFromToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            Reservation reservation = em.find(Reservation.class, id);
            if (reservation == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (!reservation.getUser().getId().equals(user.getId())) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            List<SeatDTO> seats = reservation.getSeats().stream()
                    .map(seat -> new SeatDTO(seat.getLabel(), seat.getCost()))
                    .collect(Collectors.toList());

            BookingDTO bookingDTO = new BookingDTO(
                    reservation.getConcert().getId(),
                    reservation.getDate(),
                    seats);

            return Response.ok(bookingDTO).build();
        } finally {
            em.close();
        }
    }

    private static class Subscription {
        AsyncResponse response;
        ConcertInfoSubscriptionDTO info;
        User user;

        Subscription(AsyncResponse response, ConcertInfoSubscriptionDTO info, User user) {
            this.response = response;
            this.info = info;
            this.user = user;
        }
    }

    private static final List<Subscription> activeSubscriptions = Collections.synchronizedList(new ArrayList<>());

    @POST
    @Path("/subscribe/concertInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void subscribeToConcertInfo(ConcertInfoSubscriptionDTO info,
                                       @Context HttpHeaders headers,
                                       @Suspended AsyncResponse asyncResponse) {

        Cookie cookie = headers.getCookies().get("auth");
        if (cookie == null || cookie.getValue().isEmpty()) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = cookie.getValue();
        User user = getUserFromToken(token);
        if (user == null) {
            asyncResponse.resume(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            Concert concert = em.find(Concert.class, info.getConcertId());
            if (concert == null || concert.getDates() == null || !concert.getDates().contains(info.getDate())) {
                asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).build());
                return;
            }

            Subscription sub = new Subscription(asyncResponse, info, user);
            activeSubscriptions.add(sub);

        } finally {
            em.close();
        }
    }

    private void notifySubscribers(LocalDateTime date, Long concertId) {
        EntityManager em = PersistenceManager.instance().createEntityManager();
        try {
            TypedQuery<Seat> seatQuery = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date", Seat.class);
            seatQuery.setParameter("date", date);
            List<Seat> allSeats = seatQuery.getResultList();

            int totalSeats = allSeats.size();
            if (totalSeats == 0) return;

            long unbooked = allSeats.stream().filter(s -> !s.isBooked()).count();
            int bookedPercentage = (int) (((totalSeats - unbooked) * 100.0) / totalSeats);

            List<Subscription> toNotify = new ArrayList<>();
            synchronized (activeSubscriptions) {
                Iterator<Subscription> iterator = activeSubscriptions.iterator();
                while (iterator.hasNext()) {
                    Subscription s = iterator.next();
                    if (s.info.getConcertId() == concertId &&
                            s.info.getDate().equals(date) &&
                            bookedPercentage >= s.info.getPercentageBooked()) {
                        toNotify.add(s);
                        iterator.remove();
                    }
                }
            }

            for (Subscription s : toNotify) {
                s.response.resume(new ConcertInfoNotificationDTO((int) unbooked));
            }

        } finally {
            em.close();
        }
    }



}
