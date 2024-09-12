package main.java.business.Interfaces;

public interface ReservableInterface {
    void reserveDocument(String id);
    void cancelReservation(String id);
}
